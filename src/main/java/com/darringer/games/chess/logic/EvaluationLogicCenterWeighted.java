package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Piece.None;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link EvaluationLogic} that is a 
 * simple linear evaluation function driven primarily by material 
 * advantage and then partially by location (with the assumption that 
 * controlling the center of the board is advantageous). 
 * <p />
 * Floating point multiplication is actually quite slow, so we should
 * be more intelligent about applying weightings only when they are
 * not 0
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.logic.EvaluationLogic
 *
 */
public class EvaluationLogicCenterWeighted implements EvaluationLogic {
	
	/**
	 * Central locations are weighted higher than perimeter locations.
	 * For performance reasons, changing a value here does not necessarily
	 * mean it will be applied automatically - check the 
	 * {@link EvaluationLogicCenterWeighted#evaluateModel(ChessModel)}
	 * method below.
	 */
    private static final Float[][] weightings = {
    		{1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f},
    		{1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f},
    		{1.000f, 1.000f, 1.002f, 1.005f, 1.005f, 1.002f, 1.000f, 1.000f},
    		{1.000f, 1.000f, 1.005f, 1.009f, 1.009f, 1.005f, 1.000f, 1.000f},
    		{1.000f, 1.000f, 1.005f, 1.009f, 1.009f, 1.005f, 1.000f, 1.000f},
    		{1.000f, 1.000f, 1.002f, 1.005f, 1.005f, 1.002f, 1.000f, 1.000f},
    		{1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f},
    		{1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f, 1.000f}    		
    };
	    
	/**
	 * Evaluation of the board is a simple summation of points with
	 * an additional weighting factor for control of the center of the board.
	 * 
	 * @param model
	 * @return
	 */
	public float evaluateModel(ChessModel model) {
		float score = 0.0f;
		for (int x=0; x < 8; x++) {
			for (int y=0; y < 8; y++) {
				Piece piece = model.getPieceAtIndex(x, y);
				if (piece != None) {
					if ((model.getFullmoveNumber() < 20) && (x > 1) && (x < 6) && (y > 1) && (y < 6)) {
						// we value control of the center until the game is well underway
						score += (piece.getPoints() * weightings[y][x]);							
					} else {
						// after the 20th move we focus solely on piece capture and check mate
						score += piece.getPoints();
					}
				}
			}
		}
		return score;
	}	
}
