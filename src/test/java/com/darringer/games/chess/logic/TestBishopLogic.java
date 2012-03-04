package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.BlackBishop;
import static com.darringer.games.chess.model.Piece.BlackPawn;
import static com.darringer.games.chess.model.Piece.WhiteBishop;
import static com.darringer.games.chess.model.Piece.WhitePawn;

import java.util.Set;

import org.junit.Test;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;

/**
 * Test cases for bishop behavior.
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.logic.BishopLogic
 */
public class TestBishopLogic {

	@Test
	public void testWhiteBishopStartOnClearBoard() {
		ChessModel model = new ChessModel();
		BishopLogic logic = new BishopLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, C1);
		assert moves.size() == 7 : "White bishop in starting position has 7 moves";
		assert moves.contains(new ChessMove(WhiteBishop, C1, B2)) : "White bishop can move diagonally left 1";
		assert moves.contains(new ChessMove(WhiteBishop, C1, A3)) : "White bishop can move diagonally left 2";
		assert moves.contains(new ChessMove(WhiteBishop, C1, D2)) : "White bishop can move diagonally right 1";
		assert moves.contains(new ChessMove(WhiteBishop, C1, E3)) : "White bishop can move diagonally right 2";
		assert moves.contains(new ChessMove(WhiteBishop, C1, F4)) : "White bishop can move diagonally right 3";
		assert moves.contains(new ChessMove(WhiteBishop, C1, G5)) : "White bishop can move diagonally right 4";
		assert moves.contains(new ChessMove(WhiteBishop, C1, H6)) : "White bishop can move diagonally right 5";
	}
	

	@Test
	public void testBlackBishopStartOnClearBoard() {
		ChessModel model = new ChessModel();
		BishopLogic logic = new BishopLogic(Black);
		Set<ChessMove> moves = logic.getPossibleMoves(model, F8);
		assert moves.size() == 7 : "Black bishop in starting position has 7 moves";
		assert moves.contains(new ChessMove(BlackBishop, F8, E7)) : "Black bishop can move diagonally left down 1";
		assert moves.contains(new ChessMove(BlackBishop, F8, D6)) : "Black bishop can move diagonally left down 2";
		assert moves.contains(new ChessMove(BlackBishop, F8, C5)) : "Black bishop can move diagonally left down 3";
		assert moves.contains(new ChessMove(BlackBishop, F8, B4)) : "Black bishop can move diagonally left down 4";
		assert moves.contains(new ChessMove(BlackBishop, F8, A3)) : "Black bishop can move diagonally left down 5";
		assert moves.contains(new ChessMove(BlackBishop, F8, G7)) : "Black bishop can move diagonally right down 1";
		assert moves.contains(new ChessMove(BlackBishop, F8, H6)) : "Black bishop can move diagonally right down 2";		
	}
	

	@Test
	public void testWhiteBishopMiddleOnClearBoard() {
		ChessModel model = new ChessModel();
		BishopLogic logic = new BishopLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.size() == 13 : "White bishop in middle has 13 possible moves";
		assert moves.contains(new ChessMove(WhiteBishop, D4, E5)) : "White bishop can move diagonally up right 1";
		assert moves.contains(new ChessMove(WhiteBishop, D4, F6)) : "White bishop can move diagonally up right 2";
		assert moves.contains(new ChessMove(WhiteBishop, D4, G7)) : "White bishop can move diagonally up right 3";
		assert moves.contains(new ChessMove(WhiteBishop, D4, H8)) : "White bishop can move diagonally up right 4";
		assert moves.contains(new ChessMove(WhiteBishop, D4, C5)) : "White bishop can move diagonally up left 1";
		assert moves.contains(new ChessMove(WhiteBishop, D4, B6)) : "White bishop can move diagonally up left 2";
		assert moves.contains(new ChessMove(WhiteBishop, D4, A7)) : "White bishop can move diagonally up left 3";
		assert moves.contains(new ChessMove(WhiteBishop, D4, C3)) : "White bishop can move diagonally down left 1";
		assert moves.contains(new ChessMove(WhiteBishop, D4, B2)) : "White bishop can move diagonally down left 2";
		assert moves.contains(new ChessMove(WhiteBishop, D4, A1)) : "White bishop can move diagonally down left 3";
		assert moves.contains(new ChessMove(WhiteBishop, D4, E3)) : "White bishop can move diagonally down right 1";
		assert moves.contains(new ChessMove(WhiteBishop, D4, F2)) : "White bishop can move diagonally down right 2";
		assert moves.contains(new ChessMove(WhiteBishop, D4, G1)) : "White bishop can move diagonally down right 3";
	}
	

	@Test
	public void testWhiteBishopCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(F6, BlackPawn);
		BishopLogic logic = new BishopLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.contains(new ChessMove(WhiteBishop, D4, E5)) : "White bishop can move diagonally up right 1";
		assert moves.contains(new ChessMove(WhiteBishop, D4, F6)) : "White bishop can capture black pawn at F6";
		assert !moves.contains(new ChessMove(WhiteBishop, D4, G7)) : "White bishop cannot move beyond the capture";
		assert !moves.contains(new ChessMove(WhiteBishop, D4, H8)) : "White bishop cannot move beyond the capture";
	}

	
	@Test
	public void testWhiteBishopBlocked() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(F6, WhitePawn);
		BishopLogic logic = new BishopLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.contains(new ChessMove(WhiteBishop, D4, E5)) : "White bishop can move diagonally up right 1";
		assert !moves.contains(new ChessMove(WhiteBishop, D4, F6)) : "White bishop cannot capture white pawn at F6";
		assert !moves.contains(new ChessMove(WhiteBishop, D4, G7)) : "White bishop cannot move beyond white pawn";
		assert !moves.contains(new ChessMove(WhiteBishop, D4, H8)) : "White bishop cannot move beyond white pawn";
	}	
}
	
