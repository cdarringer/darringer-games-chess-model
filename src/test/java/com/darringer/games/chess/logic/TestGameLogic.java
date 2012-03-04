package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import org.junit.Ignore;
import org.junit.Test;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.ChessMoveEnPassantCapture;
import com.darringer.games.chess.model.ChessMovePawnPromotion;
import com.darringer.games.chess.model.ChessSearchTimeoutException;
import com.darringer.games.chess.model.Color;

/**
 * Test cases around the search logic in {@link GameLogic},
 * with a bunch of challenging mate in 1, 2, and 3 examples.
 * If the lower level unit tests aren't working then I wouldn't
 * expect these to.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.logic.GameLogic
 *
 */
public class TestGameLogic {

	private GameLogic logic = new GameLogic();
			
	@Test
	public void testEmptyGame() {
		ChessModel model = new ChessModel();
		ChessMove move = getBestMove(model, Black, 0);
		assert move == null : "There should be no best black move for an empty game";
		move = getBestMove(model, White, 0);
		assert move == null : "There should be no best white move for an empty game";
	}

	
	@Test
	public void testOverGame() {
		ChessModel model = new ChessModel();	
		// white has lost its king
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(A2, WhitePawn);		
		ChessMove move = getBestMove(model, Black, 0);
		assert move == null : "There should be no best black move for an over game";
		move = getBestMove(model, White, 0);
		assert move == null : "There should be no best white move for an over game";
	}
	
	/**
	 * Search test with some very basic pawn scenarios.
	 * We should get the same results regardless of search depth.
	 */
	@Test
	public void testSinglePawns() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(D4, WhitePawn);
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(E5, BlackPawn);
		model.setPieceAtLocation(F7, BlackPawn);		
		model.getCastlingAvailability().setBlackCanCastleKingSide(false);
		model.getCastlingAvailability().setBlackCanCastleQueenSide(false);
		model.getCastlingAvailability().setWhiteCanCastleKingSide(false);
		model.getCastlingAvailability().setWhiteCanCastleQueenSide(false);
		
		ChessMove move = getBestMove(model, Black, 0);
		ChessMove expected = new ChessMove(BlackPawn, E5, D4);
		assert move.equals(expected) : "Expected black pawn to capture white pawn with depth 0";
		move = getBestMove(model, Black, 4);
		expected = new ChessMove(BlackPawn, E5, D4);
		assert move.equals(expected) : "Expected black pawn to capture white pawn with depth 4";
		
		model = new ChessModel();
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(D5, BlackPawn);
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(E4, WhitePawn);
		model.setPieceAtLocation(F2, WhitePawn);
		model.getCastlingAvailability().setBlackCanCastleKingSide(false);
		model.getCastlingAvailability().setBlackCanCastleQueenSide(false);
		model.getCastlingAvailability().setWhiteCanCastleKingSide(false);
		model.getCastlingAvailability().setWhiteCanCastleQueenSide(false);

		move = getBestMove(model, White, 0);
		expected = new ChessMove(WhitePawn, E4, D5);
		assert move.equals(expected) : "Expected white pawn to capture black pawn with depth 0";		
		move = getBestMove(model, White, 4);
		expected = new ChessMove(WhitePawn, E4, D5);
		assert move.equals(expected) : "Expected white pawn to capture black pawn with depth 4";

	}
	
	/**
	 * Test scenarios involving significant point gains
	 * through capture.
	 */
	@Test
	public void testBetterCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(B5, WhitePawn);
		model.setPieceAtLocation(E5, WhitePawn);
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(A6, BlackPawn);
		model.setPieceAtLocation(D6, BlackRook);
		model.setPieceAtLocation(F6, BlackQueen);
		model.setPieceAtLocation(E8, BlackKing);
		ChessMove move = getBestMove(model, White, 0);
		ChessMove expected = new ChessMove(WhitePawn, E5, F6);
		assert move.equals(expected) : "Expected white pawn to capture black queen with depth 0";		

	}
	
	
	/**
	 * Ensure en passant capture moves are correcly considered 
	 * during search.
	 */
	@Test
	public void testEnPassantCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(G2, WhitePawn);
		model.setPieceAtLocation(C5, WhitePawn);
		model.setPieceAtLocation(B5, BlackPawn);
		ChessMove move = getBestMove(model, White, 0);
		ChessMove expectedMove = new ChessMoveEnPassantCapture(WhitePawn, C5, B6);
		assert !move.equals(expectedMove) : "White pawn should not make the en passant capture when location unknown";
		model.setEnPassant(B6);
		move = getBestMove(model, White, 0);
		assert move.equals(expectedMove) : "White pawn should make the en passant capture when location set";

		model = new ChessModel();
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(F4, BlackPawn);
		model.setPieceAtLocation(E4, WhitePawn);
		move = getBestMove(model, Black, 0);
		expectedMove = new ChessMoveEnPassantCapture(BlackPawn, F4, E3);
		assert !move.equals(expectedMove) : "Black pawn should not make the en passant capture when location unknown";
		model.setEnPassant(E3);
		move = getBestMove(model, Black, 0);
		assert move.equals(expectedMove) : "Black pawn should make the en passant capture when location set";
	}
	
	
	/**
	 * Ensure pawn promotion scenarios are correctly considered 
	 * during search. 
	 */
	@Test
	public void testPawnPromotion() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A6, WhitePawn);
		model.setPieceAtLocation(B7, WhitePawn);
		model.setPieceAtLocation(C5, WhitePawn);
		model.setPieceAtLocation(D4, WhitePawn);

		model.setPieceAtLocation(E3, BlackPawn);
		model.setPieceAtLocation(F2, BlackPawn);
		model.setPieceAtLocation(G4, BlackPawn);
		model.setPieceAtLocation(H5, BlackPawn);

		ChessMove move = getBestMove(model, White, 0);
		ChessMove expectedMove = new ChessMovePawnPromotion(WhitePawn, B7, B8, WhiteQueen);
		assert move.equals(expectedMove) : "All other things being equal, white pawn should promote to white queen";

		move = getBestMove(model, Black, 0);
		expectedMove = new ChessMovePawnPromotion(BlackPawn, F2, F1, BlackQueen);
		assert move.equals(expectedMove) : "All other things being equal, black pawn should promote to black queen";
	}
	
	
	@Test
	public void testMateInOne1() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A2, BlackPawn);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(C7, WhiteBishop);
		model.setPieceAtLocation(G6, WhiteKing);
		model.setPieceAtLocation(H6, WhiteKnight);
		model.setPieceAtLocation(H8, BlackKing);
		ChessMove move = getBestMove(model, White, 2);
		assert move.equals(new ChessMove(WhiteBishop, C7, E5)) : "White bishop should make mate in one";
		assert move.isBlackInCheckMate() : "Black should be in check mate";
	}

	
	@Test
	public void testMateInOne2() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A3, WhitePawn);
		model.setPieceAtLocation(A5, BlackPawn);
		model.setPieceAtLocation(A6, BlackRook);
		model.setPieceAtLocation(B2, WhiteBishop);
		model.setPieceAtLocation(B4, WhitePawn);
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(C7, BlackPawn);
		model.setPieceAtLocation(F2, WhitePawn);
		model.setPieceAtLocation(F3, WhiteBishop);
		model.setPieceAtLocation(F7, BlackPawn);
		model.setPieceAtLocation(F8, BlackRook);
		model.setPieceAtLocation(G1, WhiteKing);
		model.setPieceAtLocation(G3, WhitePawn);
		model.setPieceAtLocation(G4, WhiteKnight);
		model.setPieceAtLocation(G6, BlackPawn);
		model.setPieceAtLocation(G8, BlackKing);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H7, BlackPawn);
		ChessMove move = getBestMove(model, White, 2);
		assert move.equals(new ChessMove(WhiteKnight, G4, H6)) : "White knight should make mate in one";
		assert move.isBlackInCheckMate() : "Black should be in check mate";
	}
	
	
	@Test
	public void testMateInOne3() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(B2, WhitePawn);
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(C3, WhiteKnight);
		model.setPieceAtLocation(C4, WhiteBishop);
		model.setPieceAtLocation(C5, BlackBishop);
		model.setPieceAtLocation(C6, BlackKnight);
		model.setPieceAtLocation(C7, BlackPawn);
		model.setPieceAtLocation(D6, BlackPawn);
		model.setPieceAtLocation(E1, WhiteRook);
		model.setPieceAtLocation(E8, BlackBishop);
		model.setPieceAtLocation(F1, WhiteRook);
		model.setPieceAtLocation(F4, WhiteBishop);
		model.setPieceAtLocation(F8, BlackKing);
		model.setPieceAtLocation(G2, WhitePawn);
		model.setPieceAtLocation(G4, BlackKnight);
		model.setPieceAtLocation(G7, BlackPawn);
		model.setPieceAtLocation(H1, WhiteKing);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H4, BlackQueen);
		model.setPieceAtLocation(H6, BlackPawn);		
		ChessMove move = getBestMove(model, White, 2);
		assert move.equals(new ChessMove(WhiteBishop, F4, D6)) : "White bishop should make mate in one";
		assert move.isBlackInCheckMate() : "Black should be in check mate";
	}

	
	@Test
	public void testMateInOne4() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A4, WhitePawn);
		model.setPieceAtLocation(A5, BlackPawn);
		model.setPieceAtLocation(B2, WhitePawn);
		model.setPieceAtLocation(B4, BlackKnight);
		model.setPieceAtLocation(B5, WhiteBishop);
		model.setPieceAtLocation(B8, BlackRook);
		model.setPieceAtLocation(C1, WhiteKing);
		model.setPieceAtLocation(C5, BlackPawn);
		model.setPieceAtLocation(D1, WhiteRook);
		model.setPieceAtLocation(D4, WhiteKnight);
		model.setPieceAtLocation(E3, BlackPawn);
		model.setPieceAtLocation(F8, BlackRook);
		model.setPieceAtLocation(G4, WhitePawn);
		model.setPieceAtLocation(G5, WhiteBishop);
		model.setPieceAtLocation(G6, BlackBishop);
		model.setPieceAtLocation(G7, BlackPawn);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H3, WhitePawn);
		model.setPieceAtLocation(H7, BlackPawn);
		model.setPieceAtLocation(H8, BlackKing);
		ChessMove move = getBestMove(model, Black, 2);
		assert move.equals(new ChessMove(BlackKnight, B4, A2)) : "Black knight should make mate in one";
		assert move.isWhiteInCheckMate() : "White should be in check mate";		
	}

	
	@Test
	public void testMateInOne5() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A1, BlackBishop);
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(C1, WhiteBishop);
		model.setPieceAtLocation(C6, BlackBishop);
		model.setPieceAtLocation(C7, BlackPawn);
		model.setPieceAtLocation(D6, BlackPawn);
		model.setPieceAtLocation(E4, BlackQueen);
		model.setPieceAtLocation(E5, WhitePawn);
		model.setPieceAtLocation(F1, WhiteRook);
		model.setPieceAtLocation(F5, WhiteKnight);
		model.setPieceAtLocation(F8, BlackRook);
		model.setPieceAtLocation(G2, WhitePawn);
		model.setPieceAtLocation(G7, BlackPawn);
		model.setPieceAtLocation(G8, BlackKing);
		model.setPieceAtLocation(H1, WhiteKing);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H5, WhiteQueen);
		ChessMove move = getBestMove(model, White, 2);
		assert move.equals(new ChessMove(WhiteKnight, F5, E7)) : "White knight should make mate in one";
		assert move.isBlackInCheckMate() : "Black should be in check mate";
		
	}

	
	@Test
	public void testMateInOne6() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B2, WhiteBishop);
		model.setPieceAtLocation(B3, WhitePawn);
		model.setPieceAtLocation(B7, BlackBishop);
		model.setPieceAtLocation(C5, BlackPawn);
		model.setPieceAtLocation(D5, WhitePawn);
		model.setPieceAtLocation(D8, BlackQueen);
		model.setPieceAtLocation(E1, WhiteRook);
		model.setPieceAtLocation(F3, WhiteKnight);
		model.setPieceAtLocation(F5, WhiteBishop);
		model.setPieceAtLocation(F8, BlackKnight);
		model.setPieceAtLocation(G1, WhiteKing);
		model.setPieceAtLocation(G5, WhitePawn);
		model.setPieceAtLocation(G6, BlackPawn);
		model.setPieceAtLocation(G8, BlackRook);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H4, WhiteQueen);
		model.setPieceAtLocation(H5, BlackPawn);
		model.setPieceAtLocation(H7, BlackKing);
		ChessMove move = getBestMove(model, White, 2);
		assert move.equals(new ChessMove(WhiteQueen, H4, H5)) : "White queen should make mate in one";
		assert move.isBlackInCheckMate() : "Black should be in check mate";		
	}
		
	
	/**
	 * Very high branching factor here.	  
	 * Search with depth 4 took 86529ms on February 1.
	 * Search with depth 4 took 55955ms on February 6 (reduced floating multiplies)
	 * Search with depth 4 took 59172ms on February 6 (no floating multiplies)
	 * Search with depth 4 took 28338ms on February 20 (chess model copy improvements)
	 */
	@Test
	public void testMateInTwo1() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B2, WhitePawn);
		model.setPieceAtLocation(B6, BlackBishop);
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(C3, WhitePawn);
		model.setPieceAtLocation(C4, WhiteBishop);
		model.setPieceAtLocation(C7, BlackPawn);
		model.setPieceAtLocation(C8, BlackBishop);
		model.setPieceAtLocation(D4, BlackPawn);
		model.setPieceAtLocation(D6, BlackPawn);
		model.setPieceAtLocation(E1, WhiteRook);
		model.setPieceAtLocation(F2, WhitePawn);
		model.setPieceAtLocation(F5, BlackQueen);
		model.setPieceAtLocation(F6, WhiteBishop);
		model.setPieceAtLocation(F7, BlackPawn);
		model.setPieceAtLocation(F8, BlackRook);
		model.setPieceAtLocation(G1, WhiteKing);
		model.setPieceAtLocation(G2, WhitePawn);
		model.setPieceAtLocation(G8, BlackKing);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H5, WhiteQueen);
		model.setPieceAtLocation(H7, BlackPawn);

		// figure out white's first move
		ChessMove move = getBestMove(model, White, 4);
		assert move.equals(new ChessMove(WhiteQueen, H5, F7)) : "White queen should make first move";
		assert !move.isBlackInCheckMate() : "Black should not be in check mate yet";
		model.setPieceAtLocation(H5, None);
		model.setPieceAtLocation(F7, WhiteQueen);
		
		// black counter move
		model.setPieceAtLocation(F8, None);
		model.setPieceAtLocation(F7, BlackRook);
		
		// figure out white's check mate move
		move = getBestMove(model, White, 2);
		assert move.equals(new ChessMove(WhiteRook, E1, E8)) : "White rook makes mate in second move";
		assert move.isBlackInCheckMate() : "Check mate in two";
	}
	
	
	/**
	 * Very high branching factor here.	  
	 * Search with depth 4 took 48196ms on February 1.
	 * Search with depth 4 took 27294ms on February 6 (reduced floating mult).
	 * Search with depth 4 took 22036ms on February 6 (no floating multiplies)
	 * Search with depth 4 took 4032ms on February 20 (chess model copy improvements)
	 */
	@Test
	public void testMateInTwo2() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B2, WhitePawn);
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(C1, WhiteBishop);
		model.setPieceAtLocation(C2, WhitePawn);
		model.setPieceAtLocation(C3, WhiteKnight);
		model.setPieceAtLocation(C7, BlackPawn);
		model.setPieceAtLocation(D1, WhiteRook);
		model.setPieceAtLocation(E8, BlackRook);
		model.setPieceAtLocation(F2, WhitePawn);
		model.setPieceAtLocation(F6, BlackKnight);
		model.setPieceAtLocation(F7, BlackPawn);
		model.setPieceAtLocation(G1, WhiteKing);
		model.setPieceAtLocation(G3, WhiteQueen);
		model.setPieceAtLocation(G7, BlackPawn);
		model.setPieceAtLocation(G8, BlackKing);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H3, BlackBishop);
		model.setPieceAtLocation(H5, BlackQueen);
		model.setPieceAtLocation(H7, BlackPawn);
		
		// figure out black's first move
		ChessMove move = getBestMove(model, Black, 4);
		assert move.equals(new ChessMove(BlackQueen, H5, D1)) : "Black queen should make first move";
		assert !move.isWhiteInCheckMate() : "White should not be in check mate yet";
		model.setPieceAtLocation(H5, None);
		model.setPieceAtLocation(D1, BlackQueen);
		
		// white's counter move
		model.setPieceAtLocation(C3, None);
		model.setPieceAtLocation(D1, WhiteKnight);
		
		// black's check mate move
		move = getBestMove(model, Black, 2);
		assert move.equals(new ChessMove(BlackRook, E8, E1)) : "Black rook should make the mate move";
		assert move.isWhiteInCheckMate() : "Check mate in two";
	}

	
	/**
	 * Very high branching factor here.	  
	 * Search with depth 4 took 83721ms on February 1.
	 * Search with depth 4 took 87204ms on February 6 (reduced floating multiplies)
	 * Search with depth 4 took 69059ms on February 6 (no floating multiplies)
	 * Search with depth 4 took 16276ms on February 20 (chess model copying improvements)
	 */
	@Test
	public void testMateInTwo3() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B1, WhiteKnight);
		model.setPieceAtLocation(B2, WhitePawn);
		model.setPieceAtLocation(B6, BlackBishop);
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(B8, BlackKnight);
		model.setPieceAtLocation(C2, WhitePawn);
		model.setPieceAtLocation(C4, WhiteBishop);
		model.setPieceAtLocation(C7, BlackPawn);
		model.setPieceAtLocation(C8, BlackBishop);
		model.setPieceAtLocation(D4, WhitePawn);
		model.setPieceAtLocation(D7, BlackPawn);
		model.setPieceAtLocation(D8, BlackQueen);
		model.setPieceAtLocation(E4, WhitePawn);
		model.setPieceAtLocation(E7, BlackKnight);
		model.setPieceAtLocation(E8, BlackRook);
		model.setPieceAtLocation(F1, WhiteKing);
		model.setPieceAtLocation(F4, WhiteBishop);
		model.setPieceAtLocation(G2, WhitePawn);
		model.setPieceAtLocation(G5, WhiteKnight);
		model.setPieceAtLocation(G7, BlackPawn);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H5, WhiteQueen);
		model.setPieceAtLocation(H6, BlackPawn);
		model.setPieceAtLocation(H8, BlackKing);

		// figure out white's first move
		ChessMove move = getBestMove(model, White, 4);
		assert move.equals(new ChessMove(WhiteQueen, H5, H6)) : "White queen should make first move";
		assert !move.isBlackInCheckMate() : "Black should not be in check mate yet";
		model.setPieceAtLocation(H5, None);
		model.setPieceAtLocation(H6, WhiteQueen);
		
		// black counter move
		model.setPieceAtLocation(G7, None);
		model.setPieceAtLocation(H6, BlackPawn);
		
		// figure out white's check mate move
		move = getBestMove(model, White, 2);
		assert move.equals(new ChessMove(WhiteBishop, F4, E5)) : "White bishop should make the mate move";
		assert move.isBlackInCheckMate() : "Check mate in two";		
	}

	
	/**
	 * Very high branching factor here.	  
	 * Search with depth 4 took 53343ms on February 1.
	 * Search with depth 4 took 46038ms on February 6 (reduced floating multiplies)
	 * Search with depth 4 took 36811ms on February 6 (no floating multiplies)
	 * Search with depth 4 took 8160ms on February 20 (chess model copying improvements)
	 */
	@Test
	public void testMateInTwo4() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B2, WhitePawn);
		model.setPieceAtLocation(B3, WhiteQueen);
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(B8, BlackKnight);
		model.setPieceAtLocation(C1, WhiteBishop);
		model.setPieceAtLocation(C2, WhitePawn);
		model.setPieceAtLocation(C6, BlackPawn);
		model.setPieceAtLocation(C7, BlackQueen);
		model.setPieceAtLocation(D4, WhitePawn);
		model.setPieceAtLocation(D6, BlackBishop);
		model.setPieceAtLocation(E4, WhiteRook);
		model.setPieceAtLocation(F2, WhitePawn);
		model.setPieceAtLocation(F6, BlackPawn);
		model.setPieceAtLocation(F8, BlackRook);
		model.setPieceAtLocation(G1, WhiteKing);
		model.setPieceAtLocation(G2, WhitePawn);
		model.setPieceAtLocation(G7, BlackPawn);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H4, WhiteKnight);
		model.setPieceAtLocation(H7, BlackPawn);
		model.setPieceAtLocation(H8, BlackKing);

		// figure out white's first move
		ChessMove move = getBestMove(model, White, 4);
		assert move.equals(new ChessMove(WhiteKnight, H4, G6)) : "White knight should make first move";
		assert !move.isBlackInCheckMate() : "Black should not be in check mate yet";
		model.setPieceAtLocation(H4, None);
		model.setPieceAtLocation(G6, WhiteKnight);
		
		// black's counter move
		model.setPieceAtLocation(H7, None);
		model.setPieceAtLocation(G6, BlackPawn);
		
		// figure out white's check mate move
		move = getBestMove(model, White, 2);
		assert move.equals(new ChessMove(WhiteRook, E4, H4)) : "White rook should make the mate in two move";
		assert move.isBlackInCheckMate() : "Check mate in two";						
	}
	
	
	/**
	 * Ignoring because of the time it takes to run this at the moment.
	 * Search with depth 6 took 13197852 on February 1
	 * Search with depth 6 took 3102739 on February 20 
	 * (with 1,638,215,664 modes searched)
	 */
	@Ignore
	public void testMateInThree1() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A2, WhitePawn);
		model.setPieceAtLocation(A7, BlackPawn);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(B1, WhiteKnight);
		model.setPieceAtLocation(B2, WhitePawn);
		model.setPieceAtLocation(B7, BlackPawn);
		model.setPieceAtLocation(B8, BlackKnight);
		model.setPieceAtLocation(C1, WhiteBishop);
		model.setPieceAtLocation(C2, WhitePawn);
		model.setPieceAtLocation(C7, BlackPawn);
		model.setPieceAtLocation(C8, BlackBishop);
		model.setPieceAtLocation(D2, WhitePawn);
		model.setPieceAtLocation(D7, BlackPawn);
		model.setPieceAtLocation(D8, BlackQueen);
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(E4, BlackPawn);
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(F1, WhiteBishop);
		model.setPieceAtLocation(F4, WhitePawn);
		model.setPieceAtLocation(F8, BlackBishop);
		model.setPieceAtLocation(G1, WhiteKnight);
		model.setPieceAtLocation(G2, WhitePawn);
		model.setPieceAtLocation(G6, WhitePawn);
		model.setPieceAtLocation(G8, BlackKnight);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H2, WhitePawn);
		model.setPieceAtLocation(H5, WhiteQueen);
		model.setPieceAtLocation(H6, BlackPawn);
		model.setPieceAtLocation(H8, BlackRook);
		
		// figure out white's first move
		ChessMove move = getBestMove(model, White, 6);
		assert move.equals(new ChessMove(WhitePawn, G6, G7)) : "White pawn should make first move";
		assert !move.isBlackInCheckMate() : "Black should not be in check mate yet";
		model.setPieceAtLocation(G6, None);
		model.setPieceAtLocation(G7, WhitePawn);

		// black makes counter move
		model.setPieceAtLocation(E8, None);
		model.setPieceAtLocation(E7, BlackKing);
		
		// white makes second move
		move = getBestMove(model, White, 4);
		assert move.equals(new ChessMove(WhiteQueen, H5, E5)) : "White queen should make the second move";
		assert !move.isBlackInCheckMate() : "Black should not be in check mate yet";
		model.setPieceAtLocation(H5, None);
		model.setPieceAtLocation(E5, WhiteQueen);
		
		// black makes counter move
		model.setPieceAtLocation(E7, None);
		model.setPieceAtLocation(F7, BlackKing);
		
		// white makes mate in 3 with pawn promotion to knight 
		move = getBestMove(model, White, 2);
		assert move.equals(new ChessMovePawnPromotion(WhitePawn, G7, H8, WhiteKnight)) : "White pawn should make the mate in 3 move";
		assert move.isBlackInCheckMate() : "Mate in three";
	}
	
	
	/**
	 * Helper function to invoke search logic without worrying about timeouts
	 * 
	 * @param model
	 * @param colorToMove
	 * @param searchDepth
	 * @return
	 */
	private ChessMove getBestMove(ChessModel model, Color colorToMove, int searchDepth) {
		ChessMove bestMove = null;
		try {
			bestMove = logic.getBestMove(model, colorToMove, searchDepth);
		} catch (ChessSearchTimeoutException cste) {
			assert false : "We should not get search timeouts unles we explicitly set a timer";
		}
		return bestMove;
	}
}
