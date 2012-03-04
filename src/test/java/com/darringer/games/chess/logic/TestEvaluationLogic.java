package com.darringer.games.chess.logic;

import static com.darringer.games.chess.logic.EvaluationLogic.BLACK_WIN_THRESHOLD;
import static com.darringer.games.chess.logic.EvaluationLogic.WHITE_WIN_THRESHOLD;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import org.junit.Test;

import com.darringer.games.chess.converter.ChessModelFENConverter;
import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessSystemException;


/**
 * Simple test cases for any implementation of {@link EvaluationLogic}
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.logic.EvaluationLogic
 *
 */
public class TestEvaluationLogic {

	private EvaluationLogic logic = new EvaluationLogicSimpleMaterialValue();
	
	@Test
	public void testEmptyGame() {
		ChessModel model = new ChessModel();
		Float result = logic.evaluateModel(model);
		assert result == 0.0f : "An empty board should have no score";
	}
	

	@Test
	public void testNewGame() {
		ChessModel model = null;
		ChessModelFENConverter converter = new ChessModelFENConverter();
		try {
			model = converter.getModelFromString(ChessModelFENConverter.FEN_NEW);
		} catch (ChessSystemException cse) {
			assert false : "Unexpected exception when creating new model: " + cse.getMessage();
		}
		Float result = logic.evaluateModel(model);
		assert result == 0.0f : "A new game should have no score";
	}
	

	@Test
	public void testLeadScenarios() {
		ChessModel model = new ChessModel();
		
		// black just has a king left
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(B1, WhiteKnight);
		model.setPieceAtLocation(C1, WhiteBishop);
		model.setPieceAtLocation(D1, WhiteQueen);
		model.setPieceAtLocation(E1, WhiteKing);

		Float result = logic.evaluateModel(model);
		assert result > 0.0f : "White should have a significant point advantage";
		assert result < WHITE_WIN_THRESHOLD : "White should not havewon the game yet";
		
		// white just has a king left
		model = new ChessModel();
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B8, BlackKnight);
		model.setPieceAtLocation(C8, BlackBishop);
		model.setPieceAtLocation(D8, BlackQueen);
		model.setPieceAtLocation(E8, BlackKing);

		result = logic.evaluateModel(model);
		assert result < 0.0f : "Black should have a significant point advantage";
		assert result > BLACK_WIN_THRESHOLD : "Black should not have won the game yet";
	}
	
	
	@Test
	public void testWinScenarios() {
		ChessModel model = new ChessModel();
		
		// white has more pieces but lost its king
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(A7, BlackRook);
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(B2, WhiteRook);
		model.setPieceAtLocation(C2, WhiteQueen);
		model.setPieceAtLocation(D2, WhiteBishop);
		
		Float result = logic.evaluateModel(model);
		assert result < BLACK_WIN_THRESHOLD : "White lost its king, so it should be a black win";

		model = new ChessModel();
		
		// black has more pieces but lost its king
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(A2, WhiteRook);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(B7, BlackRook);
		model.setPieceAtLocation(C7, BlackQueen);
		model.setPieceAtLocation(D7, BlackBishop);
		
		result = logic.evaluateModel(model);
		assert result > WHITE_WIN_THRESHOLD : "Black lost its king, so it should be a white win";		
	}
}
