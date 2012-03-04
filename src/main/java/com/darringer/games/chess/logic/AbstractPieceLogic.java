package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.Unknown;

import java.util.Set;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.Location;

/**
 * Implementation of {@link PieceLogic} that contains functions
 * common to multiple pieces.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.logic.PieceLogic
 *
 */
abstract class AbstractPieceLogic implements PieceLogic {

	private Color color;
	
	/**
	 * All piece logics need to know their {@link Color}
	 * 
	 * @param color
	 */
	AbstractPieceLogic(Color color) {
		this.color = color;
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected Color getColor() {
		return this.color;
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected Color getOtherColor() {
		if (this.color == White) {
			return Black;
		} else {
			return White;
		}
	}
	
	
	/**
	 * White is assumed to be playing from the bottom of the board
	 * @return
	 */
	protected int getDirectionMultiplier() {
		return this.color == White ? 1 : -1; 
	}

	
	/**
	 * Common search function for rooks, bishops, and queens
	 * 
	 * @param model
	 * @param location
	 * @param movesSoFar
	 * @param xIncrement
	 * @param yIncrement
	 * @return
	 */
	protected Set<ChessMove> getPossibleMovesInDirection(ChessModel model, Location location, Set<ChessMove> movesSoFar, int xIncrement, int yIncrement) {
		Location possibleLocation;
		boolean blocked = false;
		int deltaX = 0 + xIncrement;
		int deltaY = 0 + yIncrement;
		while (!blocked) {
			possibleLocation = location.getRelativeLocation(deltaX, deltaY);
			if (possibleLocation == Unknown) {
				// we ran out of board in this direction, we are done
				blocked = true;
			} else if (model.isLocationEmpty(possibleLocation)) {
				// empty space -> possible move, keep going in this direction
				movesSoFar.add(new ChessMove(getPiece(), location, possibleLocation));
				deltaX += xIncrement;
				deltaY += yIncrement;
			} else if (model.isLocationOccupiedByColor(possibleLocation, getOtherColor())) {
				// opponent's piece -> possible move, stop searching in this direction
				movesSoFar.add(new ChessMove(getPiece(), location, possibleLocation));
				blocked = true;
			} else {
				// one of my own pieces -> stop searching in this direction
				blocked = true;
			}
		}
		return movesSoFar;
	}
}
