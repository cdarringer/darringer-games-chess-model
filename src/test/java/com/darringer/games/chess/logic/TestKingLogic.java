package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import java.util.Set;

import org.junit.Test;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.ChessMoveCastleKingSide;
import com.darringer.games.chess.model.ChessMoveCastleQueenSide;

/**
 * Test cases for king behavior.  Since kings initiate castling in this
 * program, we include some of those test cases here as well.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.game.chess.logic.KingLogic
 *
 */
public class TestKingLogic {

	@Test
	public void testWhiteKingStartOnClearBoard() {
		ChessModel model = new ChessModel();
		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, E1);
		assert moves.size() == 7 : "White king in starting position has 7 moves";
		assert moves.contains(new ChessMove(WhiteKing, E1, D1)) : "White king in starting position can move 1 left";
		assert moves.contains(new ChessMove(WhiteKing, E1, D2)) : "White king in starting position can move 1 left and 1 up";
		assert moves.contains(new ChessMove(WhiteKing, E1, E2)) : "White king in starting position can move 1 up";
		assert moves.contains(new ChessMove(WhiteKing, E1, F2)) : "White king in starting position can move 1 right and 1 up";
		assert moves.contains(new ChessMove(WhiteKing, E1, F1)) : "White king in starting position can move 1 right";
		assert moves.contains(new ChessMoveCastleKingSide(WhiteKing, E1, G1)) : "White king in starting position can castle king side";
		assert moves.contains(new ChessMoveCastleQueenSide(WhiteKing, E1, C1)) : "White king in starting position can castle queen side";
	}
	

	@Test
	public void testBlackKingStartOnClearBoard() {
		ChessModel model = new ChessModel();
		KingLogic logic = new KingLogic(Black);
		Set<ChessMove> moves = logic.getPossibleMoves(model, E8);
		assert moves.size() == 7 : "Black king in starting position has 7 moves";
		assert moves.contains(new ChessMove(BlackKing, E8, D8)) : "Black king in starting position can move 1 left";
		assert moves.contains(new ChessMove(BlackKing, E8, D7)) : "Black king in starting position can move 1 left and 1 down";
		assert moves.contains(new ChessMove(BlackKing, E8, E7)) : "Black king in starting position can move 1 down";
		assert moves.contains(new ChessMove(BlackKing, E8, F7)) : "Black king in starting position can move 1 right and 1 down";
		assert moves.contains(new ChessMove(BlackKing, E8, F8)) : "Black king in starting position can move 1 right";
		assert moves.contains(new ChessMoveCastleKingSide(BlackKing, E8, G8)) : "Black king in starting position can castle king side";
		assert moves.contains(new ChessMoveCastleQueenSide(BlackKing, E8, C8)) : "Black king in starting position can castle queen side";
	}
	
	
	@Test
	public void testWhiteKingMiddleOnClearBoard() {
		ChessModel model = new ChessModel();
		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.size() == 8 : "White king at D4 has 8 moves";
		assert moves.contains(new ChessMove(WhiteKing, D4, C4)) : "White king in starting position can move 1 left";
		assert moves.contains(new ChessMove(WhiteKing, D4, C5)) : "White king in starting position can move 1 left and 1 up";
		assert moves.contains(new ChessMove(WhiteKing, D4, D5)) : "White king in starting position can move 1 up";
		assert moves.contains(new ChessMove(WhiteKing, D4, E5)) : "White king in starting position can move 1 right and 1 up";
		assert moves.contains(new ChessMove(WhiteKing, D4, E4)) : "White king in starting position can move 1 right";		
		assert moves.contains(new ChessMove(WhiteKing, D4, E3)) : "White king in starting position can move 1 right and 1 down";
		assert moves.contains(new ChessMove(WhiteKing, D4, D3)) : "White king in starting position can move 1 down";
		assert moves.contains(new ChessMove(WhiteKing, D4, C3)) : "White king in starting position can move 1 left and 1 down";
	}
	

	@Test
	public void testWhiteKingCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E5, BlackPawn);
		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert moves.contains(new ChessMove(WhiteKing, D4, E5)) : "White King can capture black pawn at E5";
	}

	
	@Test
	public void testWhiteKingBlocked() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E5, WhitePawn);
		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, D4);
		assert !moves.contains(new ChessMove(WhiteKing, D4, E5)) : "White King cannot capture white pawn at E5";
	}	
	
	
	/**
	 * Ensure that the castling moves are returned 
	 * in the list of available moves when there is 
	 * castling availability.
	 */
	@Test
	public void testCastlingAvailable() {
		ChessModel model = new ChessModel();
		model.getCastlingAvailability().setBlackCanCastleKingSide(true);
		model.getCastlingAvailability().setBlackCanCastleQueenSide(true);
		model.getCastlingAvailability().setWhiteCanCastleKingSide(true);
		model.getCastlingAvailability().setWhiteCanCastleQueenSide(true);
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H8, BlackRook);

		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, E1);
		assert moves.contains(new ChessMoveCastleKingSide(WhiteKing, E1, G1)) : "White king-side castle is available";
		assert moves.contains(new ChessMoveCastleQueenSide(WhiteKing, E1, C1)) : "White queen-side castle is available";
		
		logic = new KingLogic(Black);
		moves = logic.getPossibleMoves(model, E8);
		assert moves.contains(new ChessMoveCastleKingSide(BlackKing, E8, G8)) : "Black king-side castle is available";
		assert moves.contains(new ChessMoveCastleQueenSide(BlackKing, E8, C8)) : "Black queen-side castle is available";		
	}
	
	/**
	 * Ensure that castling moves are not returned 
	 * in the list of available moves when there is 
	 * no castling availability (i.e. the king or 
	 * rook have moved from their original positions).
	 */
	@Test 
	public void testCastlingNotAvailable() {
		ChessModel model = new ChessModel();
		model.getCastlingAvailability().setBlackCanCastleKingSide(false);
		model.getCastlingAvailability().setBlackCanCastleQueenSide(false);
		model.getCastlingAvailability().setWhiteCanCastleKingSide(false);
		model.getCastlingAvailability().setWhiteCanCastleQueenSide(false);
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H8, BlackRook);

		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, E1);
		assert !moves.contains(new ChessMoveCastleKingSide(WhiteKing, E1, G1)) : "White king-side castle is not available";
		assert !moves.contains(new ChessMoveCastleQueenSide(WhiteKing, E1, C1)) : "White queen-side castle is not available";
		
		logic = new KingLogic(Black);
		moves = logic.getPossibleMoves(model, E8);
		assert !moves.contains(new ChessMoveCastleKingSide(BlackKing, E8, G8)) : "Black king-side castle is not available";
		assert !moves.contains(new ChessMoveCastleQueenSide(BlackKing, E8, C8)) : "Black queen-side castle is not available";				
	}

	
	/**
	 * You cannot castle when a friendly or hostile piece
	 * stands between the king and rook being castled.
	 */
	@Test
	public void testCastlingBlocked() {
		ChessModel model = new ChessModel();
		model.getCastlingAvailability().setBlackCanCastleKingSide(true);
		model.getCastlingAvailability().setBlackCanCastleQueenSide(true);
		model.getCastlingAvailability().setWhiteCanCastleKingSide(true);
		model.getCastlingAvailability().setWhiteCanCastleQueenSide(true);
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B1, WhiteKnight);
		model.setPieceAtLocation(B8, BlackKnight);
		model.setPieceAtLocation(G1, WhiteKnight);
		model.setPieceAtLocation(G8, BlackKnight);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H8, BlackRook);

		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, E1);
		assert !moves.contains(new ChessMoveCastleKingSide(WhiteKing, E1, G1)) : "White king-side castle is blocked";
		assert !moves.contains(new ChessMoveCastleQueenSide(WhiteKing, E1, C1)) : "White queen-side castle is blocked";
		
		logic = new KingLogic(Black);
		moves = logic.getPossibleMoves(model, E8);
		assert !moves.contains(new ChessMoveCastleKingSide(BlackKing, E8, G8)) : "Black king-side castle is blocked";
		assert !moves.contains(new ChessMoveCastleQueenSide(BlackKing, E8, C8)) : "Black queen-side castle is blocked";						
	}
	
	
	/**
	 * You cannot castle when the king is currently in 
	 * check.
	 */
	@Test
	public void testCastlingKingInCheckBefore() {
		ChessModel model = new ChessModel();
		model.getCastlingAvailability().setBlackCanCastleKingSide(true);
		model.getCastlingAvailability().setWhiteCanCastleKingSide(true);
		model.setPieceAtLocation(B4, BlackBishop);
		model.setPieceAtLocation(B5, WhiteBishop);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H8, BlackRook);

		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, E1);
		assert !moves.contains(new ChessMoveCastleKingSide(WhiteKing, E1, G1)) : "White king-side castle is not possible when king in check";		

		logic = new KingLogic(Black);
		moves = logic.getPossibleMoves(model, E8);
		assert !moves.contains(new ChessMoveCastleKingSide(BlackKing, E8, G8)) : "Black king-side castle is not possible when king in check";		

	}
	
	
	/**
	 * You cannot castle when the king would be in 
	 * check after castling.
	 */
	@Test
	public void testCastlingKingInCheckAfter() {
		ChessModel model = new ChessModel();
		model.getCastlingAvailability().setBlackCanCastleKingSide(true);
		model.getCastlingAvailability().setWhiteCanCastleKingSide(true);
		model.setPieceAtLocation(C5, BlackBishop);
		model.setPieceAtLocation(C4, WhiteBishop);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H8, BlackRook);

		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, E1);
		assert !moves.contains(new ChessMoveCastleKingSide(WhiteKing, E1, G1)) : "White king-side castle is not possible when king would be in check";		

		logic = new KingLogic(Black);
		moves = logic.getPossibleMoves(model, E8);
		assert !moves.contains(new ChessMoveCastleKingSide(BlackKing, E8, G8)) : "Black king-side castle is not possible when king would be in check";		
	}
	
	
	/**
	 * You cannot castle when the king would move over
	 * a square that is in capturing range of a hostile 
	 * piece.
	 */
	@Test
	public void testCastlingKingInCheckDuring() {
		ChessModel model = new ChessModel();
		model.getCastlingAvailability().setBlackCanCastleKingSide(true);
		model.getCastlingAvailability().setWhiteCanCastleKingSide(true);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H3, BlackBishop);
		model.setPieceAtLocation(H6, WhiteBishop);		
		model.setPieceAtLocation(H8, BlackRook);

		KingLogic logic = new KingLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, E1);
		assert !moves.contains(new ChessMoveCastleKingSide(WhiteKing, E1, G1)) : "White king-side castle is not possible when king would pass over capturable location";		

		logic = new KingLogic(Black);
		moves = logic.getPossibleMoves(model, E8);
		assert !moves.contains(new ChessMoveCastleKingSide(BlackKing, E8, G8)) : "Black king-side castle is not possible when king would pass over capturable location";		
	}
}
	
