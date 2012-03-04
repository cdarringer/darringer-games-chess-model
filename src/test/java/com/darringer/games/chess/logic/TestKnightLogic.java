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
 * Test cases for knight behavior.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.logic.KnightBehavior;
 *
 */
public class TestKnightLogic {

	@Test
	public void testWhiteKnightStartOnClearBoard() {
		ChessModel model = new ChessModel();
		KnightLogic logic = new KnightLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, B1);
		assert moves.size() == 3 : "White knight in starting position has 3 moves";
		assert moves.contains(new ChessMove(WhiteKnight, B1, A3)) : "White knight can move left 1 up 2 from B1";
		assert moves.contains(new ChessMove(WhiteKnight, B1, C3)) : "White knight can move right 1 and up 2 from B1";
		assert moves.contains(new ChessMove(WhiteKnight, B1, D2)) : "White knight can move right 2 and up 1 from B1";
	}
	

	@Test
	public void testBlackKnightStartOnClearBoard() {
		ChessModel model = new ChessModel();
		KnightLogic logic = new KnightLogic(Black);
		Set<ChessMove> moves = logic.getPossibleMoves(model, G8);
		assert moves.size() == 3 : "Black knight in starting position has 3 moves";
		assert moves.contains(new ChessMove(BlackKnight, G8, H6)) : "Black knight can move left 1 down 2 from G8";
		assert moves.contains(new ChessMove(BlackKnight, G8, F6)) : "Black knight can move right 1 and down 2 from G8";
		assert moves.contains(new ChessMove(BlackKnight, G8, E7)) : "Black knight can move left 2 and down 1 from G8";
	}
	

	@Test
	public void testWhiteKnightMiddleOnClearBoard() {
		ChessModel model = new ChessModel();
		KnightLogic logic = new KnightLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.size() == 8 : "White knight on D4 has 8 moves";
		assert moves.contains(new ChessMove(WhiteKnight, D4, E6)) : "White knight can move right 1 and up 2 from D4";
		assert moves.contains(new ChessMove(WhiteKnight, D4, F5)) : "White knight can move right 2 and up 1 from D4";
		assert moves.contains(new ChessMove(WhiteKnight, D4, F3)) : "White knight can move right 2 and down 1 from D4";
		assert moves.contains(new ChessMove(WhiteKnight, D4, E2)) : "White knight can move right 1 and down 2 from D4";
		assert moves.contains(new ChessMove(WhiteKnight, D4, C2)) : "White knight can move left 1 and down 2 from D4";
		assert moves.contains(new ChessMove(WhiteKnight, D4, B3)) : "White knight can move left 2 and down 1 from D4";
		assert moves.contains(new ChessMove(WhiteKnight, D4, B5)) : "White knight can move left 2 and up 1 from D4";
		assert moves.contains(new ChessMove(WhiteKnight, D4, C6)) : "White knight can move left 1 and up 2 from D4";
	}
	

	@Test
	public void testWhiteKnightCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E6, BlackPawn);
		KnightLogic logic = new KnightLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.size() == 8 : "White knight on D4 has 8 moves from D4";
		assert moves.contains(new ChessMove(WhiteKnight, D4, E6)) : "White knight can capture black pawn at E6";
	}

	
	@Test
	public void testWhiteKnightBlocked() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E6, WhitePawn);
		KnightLogic logic = new KnightLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.size() == 7 : "White knight on D4 has 7 moves from D4 when blocked by white pawn at E6";
		assert !moves.contains(new ChessMove(WhiteKnight, D4, E6)) : "White knight cannot capture white pawn at E6";
	}	
}
	
