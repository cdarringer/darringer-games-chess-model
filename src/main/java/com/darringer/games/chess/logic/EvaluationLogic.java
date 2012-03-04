package com.darringer.games.chess.logic;

import com.darringer.games.chess.model.ChessModel;

/**
 * An evaluation function is responsible for returning a score for 
 * a {@link ChessModel} state.  This score will be used to determine
 * how much of an advantage white or black has at a given point in the
 * game.  A score of 0 implies an equally matched game, while positive
 * scores indicate a white advantage and negative scores indicate a 
 * black advantage.
 * <p />
 * Evaluation logic is called once for each node on the search tree -
 * many millions of times - so performance is an important consideration.
 * Poorly performing evaluation logic may limit search depth.
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.logic.chess.ChessModel
 * 
 */
public interface EvaluationLogic {
	
	/*
	 * Scores greater than <code>WHITE_WIN_THRESHOLD</code> mean 
	 * that white has won the game
	 */ 
	static float WHITE_WIN_THRESHOLD = 50.0f;

	/*
	 * Scores less than <code>BLACK_WIN_THRESHOLD</code> mean that 
	 * black has won the game.
	 */
	static float BLACK_WIN_THRESHOLD = -50.0f;

	 /**
	  * Return the score associated with the given {@link ChessModel}.
	  *  
	  * @param model
	  * @return
	  */
	public float evaluateModel(ChessModel model);
}
