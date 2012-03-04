package com.darringer.games.chess.main;

import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.darringer.games.chess.converter.ChessModelFENConverter;
import com.darringer.games.chess.logic.GameLogic;
import com.darringer.games.chess.model.ChessInvalidMoveException;
import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.ChessMoveCastleKingSide;
import com.darringer.games.chess.model.ChessMoveCastleQueenSide;
import com.darringer.games.chess.model.ChessMoveEnPassantCapture;
import com.darringer.games.chess.model.ChessSystemException;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * Simple console interface for testing chess logic.
 * Supports only the most basic functionality and error
 * handling.
 *
 * TODO: Ability to load and save game to/from FEN strings
 * 
 * @author cdarringer
 *
 */
public class ChessApplication {
		
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
			
		try {
			// create a new chess game
			ChessModel model = createNewGame();
			GameLogic logic = new GameLogic();
			displayGame(model);
		
			// process each user move until the game is over
			while (model.getState().isInProgress()) {
				ChessMove move = getUserMove(model);
				try {
					// give the computer no more than 20 seconds think time 
					model = logic.processMove(model, move, 20);
				} catch (ChessInvalidMoveException cime) {
					System.err.println("Invalid move exception: " + cime.getMessage());
				}
				displayGame(model);
			}
		} catch (ChessSystemException cse) {
			System.err.println("Unexpected system exception: " + cse.getMessage());
		}
	}
		
	/**
	 * The default new game is good enough for the console
	 * interface.
	 * 
	 * @return
	 * @throws ChessSystemException
	 */
	private static ChessModel createNewGame() throws ChessSystemException {
		ChessModelFENConverter converter = new ChessModelFENConverter();
		return converter.getModelFromString(ChessModelFENConverter.FEN_NEW);
	}

	/**
	 * Pretty print the board and game state
	 * 
	 * @param model
	 */
	private static void displayGame(ChessModel model) {
		System.out.println(model.prettyFormat());
	}
		
		
	/**
	 * Get the next user move.  Do not return unless the inputs are valid
	 * locations.
	 * 
	 * @param model
	 * @return
	 */
	private static ChessMove getUserMove(ChessModel model) {
		ChessMove move = null;
		Location fromLocation, toLocation;
		boolean isValidMove = false;
		while (!isValidMove) {
			fromLocation = getUserLocation("Next move from location?");
			toLocation = getUserLocation("Next move to location?");
			Piece piece = model.getPieceAtLocation(fromLocation);
			if ((piece == WhiteKing) && (fromLocation == E1) && (toLocation == G1)) {
				move = new ChessMoveCastleKingSide(piece, fromLocation, toLocation);
			} else if ((piece == WhiteKing) && (fromLocation == E1) && (toLocation == C1)) {
				move = new ChessMoveCastleQueenSide(piece, fromLocation, toLocation);
			} else if ((piece == WhitePawn) && (toLocation == model.getEnPassant())) {
				move = new ChessMoveEnPassantCapture(piece, fromLocation, toLocation);
			} else {
				move = new ChessMove(piece, fromLocation, toLocation);
			}
			isValidMove = true;
		}
		return move;
	}
	
			
	/**
	 * Helper function for reading a {@link Location} from the console.
	 * 
	 * @param question
	 * @return
	 */
	private static Location getUserLocation(String question) {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		Location location = null;
		boolean isValidInput = false;
		while (!isValidInput) {
			System.out.println(question);
			try {
				String input = stdin.readLine();
				location = Location.valueOf(input.toUpperCase());
				isValidInput = true;
			} catch (IllegalArgumentException iae) {
				System.out.println("That was not a recognized location (i.e. a1)");
			} catch (IOException ioe) {
				System.out.println("An unexpected system problem occurred.  Please try again.");
			} 
		}	
		return location;
	}	
}
