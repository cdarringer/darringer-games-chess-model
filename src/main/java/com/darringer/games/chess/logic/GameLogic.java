package com.darringer.games.chess.logic;

import static com.darringer.games.chess.logic.EvaluationLogic.BLACK_WIN_THRESHOLD;
import static com.darringer.games.chess.logic.EvaluationLogic.WHITE_WIN_THRESHOLD;
import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.GameState.*;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import java.util.Set;

import org.apache.log4j.Logger;

import com.darringer.games.chess.model.ChessInvalidMoveException;
import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.ChessSearchTimeoutException;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.GameState;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;


/**
 * Main controller class for the game of chess.
 * Processes a user's {@link ChessMove} and then uses
 * a search function to find the best counter move.
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.logic.EvaluationLogic
 * @see com.darringer.games.chess.logic.MoveLogic
 * @see com.darringer.games.chess.model.ChessModel
 * @see com.darringer.games.chess.model.ChessMove
 */
public class GameLogic {
	
	private static Logger log = Logger.getLogger(GameLogic.class);
	
	private EvaluationLogic evaluationLogic = new EvaluationLogicCenterWeighted();
	private MoveLogic moveLogic = new MoveLogic();
	private GameTimer timer = new GameTimer();
	private long nodeCount;
	
	
	/**
	 * Given a {@link ChessMove} from a user, verify that it is a valid move 
	 * before applying it to our {@link ChessModel} and determining the best 
	 * counter move.
	 * 
	 * @param model
	 * @param move
	 * @param timeoutInSeconds
	 * @return
	 * @throws ChessInvalidMoveException
	 */
	public ChessModel processMove(ChessModel model, ChessMove move, int timeoutInSeconds) throws ChessInvalidMoveException {
		// evaluate game state
		GameState state = getGameStateFromModelAndMove(model, move);
		model.setState(state);
		
		// is the original game over?
		if (!state.isInProgress()) { 
			return model;
		}
		
		// game was still in progress, validate move and apply it
		model = applyWhiteMoveToModel(model, move);
			
		// it was a valid white move, perform the black counter move
		model = performBlackCounterMove(model, timeoutInSeconds);
			
		// we are done...
		return model;
	}	
	
	
	/**
	 * Verify the user's {@link ChessMove} is valid before applying it to 
	 * the {@link ChessModel}.
	 * 
	 * @param model
	 * @param move
	 * @return
	 * @throws ChessInvalidMoveException
	 */
	public ChessModel applyWhiteMoveToModel(ChessModel model, ChessMove move) throws ChessInvalidMoveException {
		// is the source occupied by a piece?
		Piece piece = model.getPieceAtLocation(move.getFrom());
		if (piece == None) {
			throw new ChessInvalidMoveException(String.format("There is no piece at location %s", move.getFrom()));
		}
		
		// is the source occupied by a player's piece?
		if (piece.getColor() != White) {
			throw new ChessInvalidMoveException("You cannot move your opponents piece");
		}
		
		// is the destination in the set of known moves for this piece?
		Set<ChessMove> possibleMoves = piece.getLogic().getPossibleMoves(model, move.getFrom());
		if (!possibleMoves.contains(move)) {
			throw new ChessInvalidMoveException(String.format("%s is not a valid destination for piece at %s", move.getTo(), move.getFrom()));
		}
		
		// if this move was actually made, would it put the player in check?
		ChessModel modelClone = new ChessModel(model);
		modelClone = moveLogic.applyMoveToModel(modelClone, move);
		Location kingLocation = getPieceLocation(modelClone, WhiteKing);
		if (isLocationReachableByColor(modelClone, kingLocation, Black)) {
			throw new ChessInvalidMoveException("You cannot make a move that would leave your king in check");
		}

		// it is a valid move, apply it to the actual model
		model = moveLogic.applyMoveToModel(model, move);

		// we are done
		return model;
	}
	
	
	/** 
	 * White has made a valid move and now we will use the search 
	 * function to find the best black counter move (while making 
	 * sure we do not exceed the given time limit).
	 * 
	 * @param model
	 * @param timeoutInSeconds
	 * @return
	 */
	public ChessModel performBlackCounterMove(ChessModel model, int timeoutInSeconds) {
		// evaluate game state
		float score = evaluationLogic.evaluateModel(model);
		model.setState(getGameStateFromScore(score));;
		
		// is the original game over?
		if (isTerminalState(score)) { 
			return model;
		}
		
		// black makes its counter move...
		// reset our global timer and then search progressively deeper 
		// until we find check mate (or we are out of time)
		int depth = 1;
		ChessMove bestCounterMove = null;
		timer = new GameTimer(timeoutInSeconds);
		timer.start();
		
		// do not bother trying another level of search
		// if more than half of the time is up
		while (!timer.isHalfTimeUp()) {
			try {
				bestCounterMove = getBestMove(model, Black, depth++);
			} catch (ChessSearchTimeoutException cste) {
				// we ran out of time in this iteration 
				break;
			}
			if (bestCounterMove.isWhiteInCheckMate()) break;
		}
		timer.stop();
		
		// apply best move to the model and update the game state
		model = moveLogic.applyMoveToModel(model, bestCounterMove);
		
		// final score and state update
		model.setScore(evaluationLogic.evaluateModel(model));
		model.setState(getGameStateFromModelAndMove(model, bestCounterMove));
		return model;
	}
	
	/**
	 * Uses Min-Max search to determine the "best" move.  
	 * We limit the search depth to the passed in count.
	 * The "best" move for white will be the one that yields
	 * the highest possible score (while the "best" move for black
	 * yields the lowest possible score).
	 * 
	 * @param model
	 * @param colorToMove
	 * @param searchDepth
	 * @return The "best" move or <code>null</code> if there are no options
	 *         (i.e. the board is empty or the game is over)
	 * @throws ChessSearchTimeoutException
	 */
	public ChessMove getBestMove(ChessModel model, Color colorToMove, int searchDepth) throws ChessSearchTimeoutException {
		ChessMove bestMove = null;
		float alpha = BLACK_WIN_THRESHOLD;
		float beta = WHITE_WIN_THRESHOLD;
		
		// start the timer, reset node count
		long startTime = System.currentTimeMillis();
		nodeCount = 0l;
		
		// make sure the game isn't over already...
		if (!isTerminalState(model)) {
			cutoff:
			for (int x=0; x < 8; x++) {
				for (int y=0; y < 8; y++) {
					Piece currentPiece = model.getPieceAtIndex(x, y);
					if ((currentPiece != None) && (currentPiece.getColor() == colorToMove)) {
						// this is a moveable piece
						PieceLogic logic = currentPiece.getLogic();
						Location currentLocation = Location.get(x, y);
						Set<ChessMove> possibleMoves = logic.getPossibleMoves(model, currentLocation);
						for (ChessMove currentMove : possibleMoves) {							
							// evaluate this possible move
							float currentScore;
							log.debug(String.format("Evaluating move %s at depth %d (x=%d, y=%d)...", currentMove, searchDepth, x, y));
							if (colorToMove == White) {
								currentScore = getMinBlackScore(model, currentMove, alpha, beta, searchDepth);
								log.debug(String.format("currentScore=%f, alpha=%f, beta=%f", currentScore, alpha, beta));
								if (currentScore > alpha) {
									bestMove = currentMove;
									alpha = currentScore;
									if (alpha >= beta) break cutoff; 
								}
							} else {
								currentScore = getMaxWhiteScore(model, currentMove, alpha, beta, searchDepth);
								log.debug(String.format("currentScore=%f, alpha=%f, beta=%f", currentScore, alpha, beta));
								if (currentScore < beta) {
									bestMove = currentMove;
									beta = currentScore;
									if (beta <= alpha) break cutoff;
								}
							}
						}
					}
				}
			}
		}
		
		// stop timer
		long time = System.currentTimeMillis() - startTime;		
		long nodesPerMS = (time  < 1l ? nodeCount : (nodeCount / time));		
		log.info(String.format("Best move for %s is %s. Status: %s", colorToMove, bestMove, model.getState()));
		log.info(String.format("%d nodes explored in %d ms (%d nodes per ms) at depth %d", nodeCount, time, nodesPerMS, searchDepth));
		return bestMove;
	}
	
	
	/**
	 * Given a move by black, return the score assuming white will make 
	 * the highest scoring (max) counter move.  
	 * 
	 * @param originalModel
	 * @param blackMove
	 * @param alpha
	 * @param beta
	 * @param currentSearchDepth
	 * @return
	 * @throws ChessSearchTimeoutException
	 */
	private float getMaxWhiteScore(ChessModel originalModel, ChessMove blackMove, float alpha, float beta, int currentSearchDepth) throws ChessSearchTimeoutException 
	{
		// are we out of time?
		if (timer.isTimeUp()) {
			throw new ChessSearchTimeoutException(String.format("Search timout at depth %d", currentSearchDepth));
		}
		
		// we are evaluating this move against a clone of the model
		ChessModel model = new ChessModel(originalModel);
		
		// apply black move to the cloned model
		model = moveLogic.applyMoveToModel(model, blackMove);
		
		// is this a terminal (game ending) move for black OR are we at our search depth limit?  
		float alphaScore = evaluationLogic.evaluateModel(model);
		if ((currentSearchDepth > 0) && (!isTerminalState(alphaScore))) { 
			// reset the score, check mate flag, and continue our search...
			alphaScore = alpha;
			blackMove.setWhiteInCheckMate(true);

			// score of black's move will be the best (highest) scoring white counter move
			betacutoff:
			for (int y=7; y >= 0; y--) {
				for (int x=0; x < 8; x++) {
					Piece currentPiece = model.getPieceAtIndex(x, y);
					if ((currentPiece != None) && (White == currentPiece.getColor())) {
						// this is a possible counter move that we will evaluate
						PieceLogic logic = currentPiece.getLogic();
						Location currentLocation = Location.get(x, y);
						Set<ChessMove> possibleMoves = logic.getPossibleMoves(model, currentLocation);
						for (ChessMove currentMove : possibleMoves) {
							// evaluate this possible move
							float currentScore = getMinBlackScore(model, currentMove, alphaScore, beta, currentSearchDepth - 1);

							// update check mate flag
							if (currentScore >= BLACK_WIN_THRESHOLD) {
								blackMove.setWhiteInCheckMate(false);
							}

							// alpha beta pruning check
							alphaScore = (currentScore > alphaScore ? currentScore : alphaScore);
							if (alphaScore >= beta) {
								blackMove.setWhiteInCheckMate(false); // we don't know for sure
								break betacutoff;
							}
						}  // end for moves iteration
					} // end if there is a move-able piece 
				} // end column iteration
			} // end row iteration
		} // end if we need to search deeper
		
		// return the max white score
		nodeCount = nodeCount + 1;
		return alphaScore;
	}
	
	
	/**
	 * Given a move by white, return the score assuming black will make 
	 * the lowest scoring (min) counter move.  
	 * 
	 * @param originalModel
	 * @param whiteMove
	 * @param alpha
	 * @param beta
	 * @param currentSearchDepth
	 * @return
	 * @throws ChessSearchTimeoutException
	 */
	private float getMinBlackScore(ChessModel originalModel, ChessMove whiteMove, float alpha, float beta, int currentSearchDepth) throws ChessSearchTimeoutException
	{
		// are we out of time?
		if (timer.isTimeUp()) {
			throw new ChessSearchTimeoutException(String.format("Search timout at depth %d", currentSearchDepth));
		}
		
		// we are evaluating this move against a clone of the model
		ChessModel model = new ChessModel(originalModel);

		// apply white move to the cloned model
		model = moveLogic.applyMoveToModel(model, whiteMove);

		// is this a terminal (game ending) move for white OR are we at our search depth limit?  
		float betaScore = evaluationLogic.evaluateModel(model);
		if ((currentSearchDepth > 0) && (!isTerminalState(betaScore))) { 
			// reset the score, check mate flag, and continue our search...
			betaScore = beta;
			whiteMove.setBlackInCheckMate(true);

			// score of white's move will be the best (lowest) scoring black counter move
			alphacutoff:
			for (int y=0; y < 8; y++) {
				for (int x=0; x < 8; x++) {
					Piece currentPiece = model.getPieceAtIndex(x, y);
					if ((currentPiece != None) && (Black == currentPiece.getColor())) {		
						// this is a possible counter move that we will evaluate
						PieceLogic logic = currentPiece.getLogic();
						Location currentLocation = Location.get(x, y);
						Set<ChessMove> possibleMoves = logic.getPossibleMoves(model, currentLocation);
						for (ChessMove currentMove : possibleMoves) {
							// evaluate this possible move
							float currentScore = getMaxWhiteScore(model, currentMove, alpha, betaScore, currentSearchDepth - 1);

							// update check and check mate flags
							if (currentScore <= WHITE_WIN_THRESHOLD) {
								whiteMove.setBlackInCheckMate(false);
							}
							
							// alpha beta pruning check
							betaScore = (currentScore < betaScore ? currentScore : betaScore);
							if (betaScore <= alpha) {
								whiteMove.setBlackInCheckMate(false);  // we don't know for sure
								break alphacutoff;
							}
						}  // end for moves iteration
					} // end if there is a move-able piece 
				} // end column iteration
			} // end row iteration
		} // end if we need to search deeper
							
		// undo the original white move to the model and return the min black score
		nodeCount = nodeCount + 1;
		return betaScore;		
	}
	
	/**
	 * Utility function to determine whether the given {@Location}
	 * is reachable by the player of the given {@link Color} 
	 * 
	 * @param model
	 * @param location
	 * @param color
	 * @return
	 */
	public boolean isLocationReachableByColor(ChessModel model, Location location, Color color) {
		for (int x=0; x < 8; x++) {
			for (int y=0; y < 8; y++) {
				Piece currentPiece = model.getPieceAtIndex(x, y);
				if ((currentPiece != None) && (color == currentPiece.getColor())) {
					// TODO: remove the following if check, currently necessary to prevent circular dependency
					// with king castling available move checks.
					if ((currentPiece != WhiteKing) && (currentPiece != BlackKing)) {
						PieceLogic logic = currentPiece.getLogic();
						Location currentLocation = Location.get(x, y);
						Set<ChessMove> possibleMoves = logic.getPossibleMoves(model, currentLocation);
						for (ChessMove possibleMove : possibleMoves) {
							if (possibleMove.getTo() == location) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Use our {@link EvaluationLogic} function to determine whether
	 * this is score associated with a terminal node in the search tree.
	 * 
	 * @param score
	 * @return
	 */
	private boolean isTerminalState(float score) {
		if ((score > WHITE_WIN_THRESHOLD) || (score < BLACK_WIN_THRESHOLD)) {
			return true;
		} else {
			return false;
		}		
	}
		
	/**
	 * Use our {@link EvaluationLogic} function to determine whether
	 * this is a terminal node in the {@link ChessModel} search tree.
	 * 
	 * @param model
	 * @return
	 */
	private boolean isTerminalState(ChessModel model) {
		return isTerminalState(evaluationLogic.evaluateModel(model));
	}
	
	
	/**
	 * Return the {@link GameState} associated with the given 
	 * score from our {@link EvaluationLogic} function.
	 * 
	 * @param score
	 * @return
	 */
	private GameState getGameStateFromScore(float score) {
		if (score > WHITE_WIN_THRESHOLD) {
			return BlackInCheckMate;
		} else if (score < BLACK_WIN_THRESHOLD) {
			return WhiteInCheckMate;
		} else {
			return OK;
		}		
		
	}
	
	/**
	 * Return the {@link GameState} associated with the given 
	 * {@link ChessModel} as well as the most recent {@link ChessMove}.
	 * 
	 * @param model
	 * @param move
	 * @return
	 */
	private GameState getGameStateFromModelAndMove(ChessModel model, ChessMove move) {
		float score = evaluationLogic.evaluateModel(model);
		if ((score > WHITE_WIN_THRESHOLD) || move.isBlackInCheckMate()) {
			return BlackInCheckMate;
		} else if ((score < BLACK_WIN_THRESHOLD) || move.isWhiteInCheckMate()) {
			return WhiteInCheckMate;
		} else {
			// check?
			Location whiteKingLocation = getPieceLocation(model, WhiteKing);
			Location blackKingLocation = getPieceLocation(model, BlackKing);
			if (isLocationReachableByColor(model, whiteKingLocation, Black)) {
				return WhiteInCheck;
			} else if (isLocationReachableByColor(model, blackKingLocation, White)) {
				return BlackInCheck;
			} else {
				return OK;
			}
		}		
	}
	
	/**
	 * Return the {@link Location} associated with the given {@link Piece}, 
	 * or <code>Unknown</code> if it is not present.  In the case multiple
	 * instances of the piece exist, the first instance discovered will 
	 * be returned.
	 * 
	 * @param model
	 * @param piece
	 * @return
	 */
	private Location getPieceLocation(ChessModel model, Piece piece) {
		for (int y=0; y < 8; y++) {
			for (int x=0; x < 8; x++) {
				Piece currentPiece = model.getPieceAtIndex(x, y);
				if (currentPiece == piece) {
					return Location.get(x,  y);
				}			
			}
		}
		return Unknown;
	}
}
