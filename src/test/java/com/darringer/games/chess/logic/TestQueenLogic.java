package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import java.util.Set;

import org.junit.Test;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;

/**
 * Test cases for queen behavior
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.logic.QueenLogic
 *
 */
public class TestQueenLogic {

	@Test
	public void testWhiteQueenStartOnClearBoard() {
		ChessModel model = new ChessModel();
		QueenLogic logic = new QueenLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D1);
		assert moves.size() == 21 : "White queen in starting position has 21 moves";
	}

	
	@Test
	public void testBlackQueenStartOnClearBoard() {
		ChessModel model = new ChessModel();
		QueenLogic logic = new QueenLogic(Black);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D8);
		assert moves.size() == 21 : "Black queen in starting position has 21 moves";
	}
	
	
	@Test
	public void testWhiteQueenMiddleOnClearBoard() {
		ChessModel model = new ChessModel();
		QueenLogic logic = new QueenLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.size() == 27 : "White queen in middle has 27 possible moves";
		assert moves.contains(new ChessMove(WhiteQueen, D4, D5)) : "White queen can move up 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, D6)) : "White queen can move up 2";
		assert moves.contains(new ChessMove(WhiteQueen, D4, D7)) : "White queen can move up 3";
		assert moves.contains(new ChessMove(WhiteQueen, D4, D8)) : "White queen can move up 4";
		assert moves.contains(new ChessMove(WhiteQueen, D4, E5)) : "White queen can move up right 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, F6)) : "White queen can move up right 2 ";
		assert moves.contains(new ChessMove(WhiteQueen, D4, G7)) : "White queen can move up right 3";
		assert moves.contains(new ChessMove(WhiteQueen, D4, H8)) : "White queen can move up right 4";
		assert moves.contains(new ChessMove(WhiteQueen, D4, E4)) : "White queen can move right 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, F4)) : "White queen can move right 2";
		assert moves.contains(new ChessMove(WhiteQueen, D4, G4)) : "White queen can move right 3";
		assert moves.contains(new ChessMove(WhiteQueen, D4, H4)) : "White queen can move right 4";
		assert moves.contains(new ChessMove(WhiteQueen, D4, E3)) : "White queen can move down right 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, F2)) : "White queen can move down right 2";
		assert moves.contains(new ChessMove(WhiteQueen, D4, G1)) : "White queen can move down right 3";
		assert moves.contains(new ChessMove(WhiteQueen, D4, D3)) : "White queen can move down 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, D2)) : "White queen can move down 2";
		assert moves.contains(new ChessMove(WhiteQueen, D4, D1)) : "White queen can move down 3";
		assert moves.contains(new ChessMove(WhiteQueen, D4, C3)) : "White queen can move down left 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, B2)) : "White queen can move down left 2";
		assert moves.contains(new ChessMove(WhiteQueen, D4, A1)) : "White queen can move down left 3";
		assert moves.contains(new ChessMove(WhiteQueen, D4, C4)) : "White queen can move left 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, B4)) : "White queen can move left 2";
		assert moves.contains(new ChessMove(WhiteQueen, D4, A4)) : "White queen can move left 3";
		assert moves.contains(new ChessMove(WhiteQueen, D4, C5)) : "White queen can move up left 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, B6)) : "White queen can move up left 2";
		assert moves.contains(new ChessMove(WhiteQueen, D4, A7)) : "White queen can move up left 3";
	}

	
	@Test
	public void testWhiteQueenCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(F6, BlackPawn);
		QueenLogic logic = new QueenLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.contains(new ChessMove(WhiteQueen, D4, E5)) : "White queen can move diagonally up right 1";
		assert moves.contains(new ChessMove(WhiteQueen, D4, F6)) : "White queen can capture black pawn at F6";
		assert !moves.contains(new ChessMove(WhiteQueen, D4, G7)) : "White queen cannot move beyond the capture";
		assert !moves.contains(new ChessMove(WhiteQueen, D4, H8)) : "White queen cannot move beyond the capture";
	}

	
	@Test
	public void testWhiteQueenBlocked() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(F6, WhitePawn);
		QueenLogic logic = new QueenLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.contains(new ChessMove(WhiteQueen, D4, E5)) : "White queen can move diagonally up right 1";
		assert !moves.contains(new ChessMove(WhiteQueen, D4, F6)) : "White queen cannot capture white pawn at F6";
		assert !moves.contains(new ChessMove(WhiteQueen, D4, G7)) : "White queen cannot move beyond white pawn";
		assert !moves.contains(new ChessMove(WhiteQueen, D4, H8)) : "White queen cannot move beyond white pawn";
	}	
}
	
