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
 * Test cases for rook behavior
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.logic.RookLogic
 *
 */
public class TestRookLogic {
	
	@Test
	public void testWhiteRookStartOnClearBoard() {
		ChessModel model = new ChessModel();
		RookLogic logic = new RookLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, A1);
		assert moves.size() == 14 : "White rook in starting position can move 7 right or 7 up";
		assert moves.contains(new ChessMove(WhiteRook, A1, B1)) : "White rook can move right 1";
		assert moves.contains(new ChessMove(WhiteRook,A1, C1)) : "White rook can move right 2";
		assert moves.contains(new ChessMove(WhiteRook,A1, D1)) : "White rook can move right 3";
		assert moves.contains(new ChessMove(WhiteRook,A1, E1)) : "White rook can move right 4";
		assert moves.contains(new ChessMove(WhiteRook,A1, F1)) : "White rook can move right 5";
		assert moves.contains(new ChessMove(WhiteRook,A1, G1)) : "White rook can move right 6";
		assert moves.contains(new ChessMove(WhiteRook,A1, H1)) : "White rook can move right 7";
		assert moves.contains(new ChessMove(WhiteRook,A1, A2)) : "White rook can move up 1";
		assert moves.contains(new ChessMove(WhiteRook,A1, A3)) : "White rook can move up 2";
		assert moves.contains(new ChessMove(WhiteRook,A1, A4)) : "White rook can move up 3";
		assert moves.contains(new ChessMove(WhiteRook,A1, A5)) : "White rook can move up 4";
		assert moves.contains(new ChessMove(WhiteRook,A1, A6)) : "White rook can move up 5";
		assert moves.contains(new ChessMove(WhiteRook,A1, A7)) : "White rook can move up 6";
		assert moves.contains(new ChessMove(WhiteRook,A1, A8)) : "White rook can move up 7";
	}

	
	@Test
	public void testBlackRookStartOnClearBoard() {
		ChessModel model = new ChessModel();
		RookLogic logic = new RookLogic(Black);
		Set<ChessMove> moves = logic.getPossibleMoves(model, H8);
		assert moves.size() == 14 : "Black rook in starting position can move 7 left or 7 down";
		assert moves.contains(new ChessMove(BlackRook, H8, G8)) : "Black rook can move left 1";
		assert moves.contains(new ChessMove(BlackRook, H8, F8)) : "Black rook can move left 2";
		assert moves.contains(new ChessMove(BlackRook, H8, E8)) : "Black rook can move left 3";
		assert moves.contains(new ChessMove(BlackRook, H8, D8)) : "Black rook can move left 4";
		assert moves.contains(new ChessMove(BlackRook, H8, C8)) : "Black rook can move left 5";
		assert moves.contains(new ChessMove(BlackRook, H8, B8)) : "Black rook can move left 6";
		assert moves.contains(new ChessMove(BlackRook, H8, A8)) : "Black rook can move left 7";
		assert moves.contains(new ChessMove(BlackRook, H8, H7)) : "Black rook can move down 1";
		assert moves.contains(new ChessMove(BlackRook, H8, H6)) : "Black rook can move down 2";
		assert moves.contains(new ChessMove(BlackRook, H8, H5)) : "Black rook can move down 3";
		assert moves.contains(new ChessMove(BlackRook, H8, H4)) : "Black rook can move down 4";
		assert moves.contains(new ChessMove(BlackRook, H8, H3)) : "Black rook can move down 5";
		assert moves.contains(new ChessMove(BlackRook, H8, H2)) : "Black rook can move down 6";
		assert moves.contains(new ChessMove(BlackRook, H8, H1)) : "Black rook can move down 7";
	}

	
	@Test
	public void testWhiteRookMiddleOnClearBoard() {
		ChessModel model = new ChessModel();
		RookLogic logic = new RookLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.size() == 14 : "White rook in middle has 14 possible moves";
		assert moves.contains(new ChessMove(WhiteRook, D4, E4)) : "White rook can move right 1";
		assert moves.contains(new ChessMove(WhiteRook, D4, F4)) : "White rook can move right 2";
		assert moves.contains(new ChessMove(WhiteRook, D4, G4)) : "White rook can move right 3";
		assert moves.contains(new ChessMove(WhiteRook, D4, H4)) : "White rook can move right 4";
		assert moves.contains(new ChessMove(WhiteRook, D4, C4)) : "White rook can move left 1";
		assert moves.contains(new ChessMove(WhiteRook, D4, B4)) : "White rook can move left 2";
		assert moves.contains(new ChessMove(WhiteRook, D4, A4)) : "White rook can move left 3";
		assert moves.contains(new ChessMove(WhiteRook, D4, D5)) : "White rook can move up 1";
		assert moves.contains(new ChessMove(WhiteRook, D4, D6)) : "White rook can move up 2";
		assert moves.contains(new ChessMove(WhiteRook, D4, D7)) : "White rook can move up 3";
		assert moves.contains(new ChessMove(WhiteRook, D4, D8)) : "White rook can move up 4";
		assert moves.contains(new ChessMove(WhiteRook, D4, D3)) : "White rook can move down 1";
		assert moves.contains(new ChessMove(WhiteRook, D4, D2)) : "White rook can move down 2";
		assert moves.contains(new ChessMove(WhiteRook, D4, D1)) : "White rook can move down 3";
	}

	
	@Test
	public void testWhiteRookCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(F4, BlackPawn);
		RookLogic logic = new RookLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.contains(new ChessMove(WhiteRook, D4, E4)) : "White rook can move right 1";
		assert moves.contains(new ChessMove(WhiteRook, D4, F4)) : "White rook can capture black pawn at F4";
		assert !moves.contains(new ChessMove(WhiteRook, D4, G4)) : "White rook cannot move beyond the capture";
		assert !moves.contains(new ChessMove(WhiteRook, D4, H4)) : "White rook cannot move beyond the capture";
	}

	
	@Test
	public void testWhiteRookBlocked() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(F4, WhitePawn);
		RookLogic logic = new RookLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.contains(new ChessMove(WhiteRook, D4, E4)) : "White rook can move right 1";
		assert !moves.contains(new ChessMove(WhiteRook, D4, F4)) : "White rook cannot capture white pawn at F4";
		assert !moves.contains(new ChessMove(WhiteRook, D4, G4)) : "White rook cannot move beyond the white pawn";
		assert !moves.contains(new ChessMove(WhiteRook, D4, H4)) : "White rook cannot move beyond the white pawn";
	}
}
