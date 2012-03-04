package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import java.util.Set;

import org.junit.Test;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.ChessMoveEnPassantCapture;
import com.darringer.games.chess.model.ChessMovePawnPromotion;

/**
 * Test cases for pawn behavior.  
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.logic.PawnLogic
 *
 */
public class TestPawnLogic {
	
	@Test
	public void testWhitePawnOnClearBoardBottom() {
		ChessModel model = new ChessModel();
		PawnLogic logic = new PawnLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, B2);
		assert moves.size() == 2 : "White pawn on clear board start has two possible moves";
		assert moves.contains(new ChessMove(WhitePawn, B2, B3)) : "White pawn on clear board start can move forward one";
		assert moves.contains(new ChessMove(WhitePawn, B2, B4)) : "White pawn on clear board start can move forward two";
		
		moves = logic.getPossibleMoves(model, B4);
		assert moves.size() == 1 : "White pawn on clear board middle has one possible move";
		assert moves.contains(new ChessMove(WhitePawn, B4, B5)) : "White pawn on clear board middle can move forward only one";		
	}

	
	@Test
	public void testBlackPawnOnClearBoardTop() {
		ChessModel model = new ChessModel();
		PawnLogic logic = new PawnLogic(Black);
		Set<ChessMove> moves = logic.getPossibleMoves(model, B7);
		assert moves.size() == 2 : "Black pawn on clear board start has two possible moves";
		assert moves.contains(new ChessMove(BlackPawn, B7, B6)) : "Black pawn on clear board start can move forward one";
		assert moves.contains(new ChessMove(BlackPawn, B7, B5)) : "Black pawn on clear board start can move forward two";
		
		moves = logic.getPossibleMoves(model, B5);
		assert moves.size() == 1 : "Black pawn on clear board middle has one possible move";
		assert moves.contains(new ChessMove(BlackPawn, B5, B4)) : "Black pawn on clear board middle can move forward only one";		
	}
	

	@Test
	public void testPawnBlocked() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(B4, BlackPawn);
		PawnLogic logic = new PawnLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, B2);
		assert moves.size() == 1 : "Pawn blocked two spaces ahead has one possible move";
		assert moves.contains(new ChessMove(WhitePawn, B2, B3)) : "Pawn blocked two spaces ahead can move forward one";
		
		model.setPieceAtLocation(B3, BlackPawn);
		moves = logic.getPossibleMoves(model, B2);
		assert moves.size() == 0 : "Pawn blocked directly ahead has no moves";		
	}

	
	@Test
	public void testWhitePawnCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A3, BlackPawn);
		PawnLogic logic = new PawnLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, B2);
		assert moves.size() == 3 : "White pawn has one capture move";
		assert moves.contains(new ChessMove(WhitePawn, B2 ,A3)) : "White pawn can capture diagonal left";
		
		model.setPieceAtLocation(C3, WhitePawn);
		moves = logic.getPossibleMoves(model, B2);
		assert moves.size() == 3 : "White pawn cannot capture its own color";
		assert !moves.contains(new ChessMove(WhitePawn, B2, C3)) : "White pawn cannot capture its own color";

		model.setPieceAtLocation(C3, BlackPawn);
		moves = logic.getPossibleMoves(model, B2);
		assert moves.size() == 4 : "White pawn has two capture moves";
		assert moves.contains(new ChessMove(WhitePawn, B2, A3)) : "White pawn can capture diagonal left";
		assert moves.contains(new ChessMove(WhitePawn, B2, C3)) : "White pawn can capture diagonal right";		
	}	

	
	@Test
	public void testBlackPawnCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A6, WhitePawn);
		PawnLogic logic = new PawnLogic(Black);
		Set<ChessMove> moves = logic.getPossibleMoves(model, B7);
		assert moves.size() == 3 : "Black pawn has one capture move";
		assert moves.contains(new ChessMove(BlackPawn, B7, A6)) : "Black pawn can capture diagonal right";
		
		model.setPieceAtLocation(C6, BlackPawn);
		moves = logic.getPossibleMoves(model, B7);
		assert moves.size() == 3 : "Black pawn cannot capture its own color";
		assert !moves.contains(new ChessMove(BlackPawn, B7, C6)) : "Black pawn cannot capture its own color";

		model.setPieceAtLocation(C6, WhitePawn);
		moves = logic.getPossibleMoves(model, B7);
		assert moves.size() == 4 : "Black pawn has two capture moves";
		assert moves.contains(new ChessMove(BlackPawn, B7, A6)) : "Black pawn can capture diagonal right";
		assert moves.contains(new ChessMove(BlackPawn, B7, C6)) : "Black pawn can capture diagonal left";		
	}	
	
		
	@Test
	public void testPawnOnEdge() {
		ChessModel model = new ChessModel();
		PawnLogic logic = new PawnLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, B8);
		assert moves.size() == 0 : "Pawn on edge has no moves";
	}

	
	@Test
	public void testEnPassantCapture() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(B5, BlackPawn);
		model.setEnPassant(B6);
		PawnLogic logic = new PawnLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, C5);
		assert moves.contains(new ChessMoveEnPassantCapture(WhitePawn, C5, B6)) : "White pawn can make en passant capture left";
		
		model = new ChessModel();
		model.setPieceAtLocation(B4, WhitePawn);
		model.setEnPassant(B3);
		logic = new PawnLogic(Black);
		moves = logic.getPossibleMoves(model, C4);
		assert moves.contains(new ChessMoveEnPassantCapture(BlackPawn, C4, B3)) : "Black pawn can make en passant capture right";
	}

	
	@Test
	public void testPawnPromotion() {
		ChessModel model = new ChessModel();
		
		// white promotion after forward move
		PawnLogic logic = new PawnLogic(White);
		Set<ChessMove> moves = logic.getPossibleMoves(model, A7);
		assert moves.size() == 4 : "Promoted white pawn has 4 moves";
		assert moves.contains(new ChessMovePawnPromotion(WhitePawn, A7, A8, WhiteQueen)) : "White pawn can be promoted to white queen";
		assert moves.contains(new ChessMovePawnPromotion(WhitePawn, A7, A8, WhiteRook)) : "White pawn can be promoted to white rook";
		assert moves.contains(new ChessMovePawnPromotion(WhitePawn, A7, A8, WhiteBishop)) : "White pawn can be promoted to white bishop";
		assert moves.contains(new ChessMovePawnPromotion(WhitePawn, A7, A8, WhiteKnight)) : "White pawn can be promoted to white knight";

		// white promotion after diagonal capture
		model.setPieceAtLocation(B8, BlackRook);
		moves = logic.getPossibleMoves(model, A7);
		assert moves.size() == 8 : "Promoted white pawn can also capture and now has 8 moves";
		assert moves.contains(new ChessMovePawnPromotion(WhitePawn, A7, B8, WhiteQueen)) : "White pawn can be promoted after capture to white queen";
		assert moves.contains(new ChessMovePawnPromotion(WhitePawn, A7, B8, WhiteRook)) : "White pawn can be promoted after capture to white rook";
		assert moves.contains(new ChessMovePawnPromotion(WhitePawn, A7, B8, WhiteBishop)) : "White pawn can be promoted after capture to white bishop";
		assert moves.contains(new ChessMovePawnPromotion(WhitePawn, A7, B8, WhiteKnight)) : "White pawn can be promoted after capture to white knight";		
		
		// black promotion after forward move
		logic = new PawnLogic(Black);
		moves = logic.getPossibleMoves(model, A2);
		assert moves.size() == 4 : "Promoted black pawn has 4 moves";
		assert moves.contains(new ChessMovePawnPromotion(BlackPawn, A2, A1, BlackQueen)) : "Black pawn can be promoted to black queen";
		assert moves.contains(new ChessMovePawnPromotion(BlackPawn, A2, A1, BlackRook)) : "Black pawn can be promoted to black rook";
		assert moves.contains(new ChessMovePawnPromotion(BlackPawn, A2, A1, BlackBishop)) : "Black pawn can be promoted to black bishop";
		assert moves.contains(new ChessMovePawnPromotion(BlackPawn, A2, A1, BlackKnight)) : "Black pawn can be promoted to black knight";		
		
		// black promotion after diagonal capture
		model.setPieceAtLocation(B1, WhiteRook);
		moves = logic.getPossibleMoves(model, A2);
		assert moves.size() == 8 : "Promoted black pawn can also capture and now has 8 moves";
		assert moves.contains(new ChessMovePawnPromotion(BlackPawn, A2, B1, BlackQueen)) : "Black pawn can be promoted after capture to black queen";
		assert moves.contains(new ChessMovePawnPromotion(BlackPawn, A2, B1, BlackRook)) : "Black pawn can be promoted after capture to black rook";
		assert moves.contains(new ChessMovePawnPromotion(BlackPawn, A2, B1, BlackBishop)) : "Black pawn can be promoted after capture to black bishop";
		assert moves.contains(new ChessMovePawnPromotion(BlackPawn, A2, B1, BlackKnight)) : "Black pawn can be promoted after capture to black knight";		
	}
}
