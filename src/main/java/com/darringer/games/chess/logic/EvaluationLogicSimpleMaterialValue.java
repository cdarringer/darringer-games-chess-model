package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Piece.None;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link EvaluationLogic} that is a 
 * simple linear evaluation function driven solely by
 * the material value of the pieces on the board.  Fast, but 
 * overly simplistic (and probably not ideal).
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.logic.EvaluationLogic
 */
public class EvaluationLogicSimpleMaterialValue implements EvaluationLogic {
		    
	/**
	 * Evaluation of the board is a simple summation of points 
	 * assigned to each piece.
	 * 
	 * @param model
	 * @return
	 */
	public float evaluateModel(ChessModel model) {
		int score = 0;
		for (int x=0; x < 8; x++) {
			for (int y=0; y < 8; y++) {
				Piece piece = model.getPieceAtIndex(x, y);
				if (piece != None) {
					score += piece.getPoints();
				}
			}
		}
		return score;
	}	
}
