package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import org.junit.Ignore;
import org.junit.Test;

import com.darringer.games.chess.model.CastlingAvailability;
import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.ChessMoveCastleKingSide;
import com.darringer.games.chess.model.ChessMoveCastleQueenSide;
import com.darringer.games.chess.model.ChessMoveEnPassantCapture;
import com.darringer.games.chess.model.ChessMovePawnPromotion;
import com.darringer.games.chess.model.Location;


/**
 * Test cases for the {@link MoveLogic}, which is responsible for the application
 * of a {@link ChessMove} to a {@link ChessModel} and subsequent state updates.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.MoveLogic
 * @see com.darringer.games.chess.model.ChessModel
 * @see com.darringer.games.chess.model.ChessMove
 *
 */
public class TestMoveLogic {

	private MoveLogic logic = new MoveLogic();
		
	@Test
	public void testPiecePlacementUpdateRegularMove() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E1, WhiteKing);
		ChessMove move = new ChessMove(WhiteKing, E1, F2);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(E1) : "White king should no longer be at E1";
		assert WhiteKing == model.getPieceAtLocation(F2) : "White king should now be at E2";
	}

	
	@Test
	public void testPiecePlacementUpdateCastleKingSideMove() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(H8, BlackRook);
		
		ChessMove move = new ChessMoveCastleKingSide(WhiteKing, E1, G1);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(E1) : "White king should not longer be at E1 after castle king side";
		assert WhiteRook == model.getPieceAtLocation(F1) : "White rook should be at F1 after castle king side";
		assert WhiteKing == model.getPieceAtLocation(G1) : "White king should now be at G1 after castle king side";
			
		move = new ChessMoveCastleKingSide(BlackKing, E8, G8);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(E8) : "Black king should not longer be at E8 after castle king side";
		assert BlackRook == model.getPieceAtLocation(F8) : "Black rook should be at F8 after castle king side";
		assert BlackKing == model.getPieceAtLocation(G8) : "Black king should now be at G8 after castle king side";
	}

	
	@Test
	public void testPiecePlacementUpdateCastleQueenSideMove() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(A8, BlackRook);
		
		ChessMove move = new ChessMoveCastleQueenSide(WhiteKing, E1, C1);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(E1) : "White king should not longer be at E1 after castle queen side";
		assert WhiteRook == model.getPieceAtLocation(D1) : "White rook should be at F1 after castle queen side";
		assert WhiteKing == model.getPieceAtLocation(C1) : "White king should now be at G1 after castle queen side";
			
		move = new ChessMoveCastleQueenSide(BlackKing, E8, C8);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(E8) : "Black king should not longer be at E8 after castle queen side";
		assert BlackRook == model.getPieceAtLocation(D8) : "Black rook should be at F8 after castle queen side";
		assert BlackKing == model.getPieceAtLocation(C8) : "Black king should now be at G8 after castle queen side";
	}

	
	@Test
	public void testPiecePlacementUpdateEnPassantCaptureMove() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(C5, BlackPawn);
		model.setPieceAtLocation(D5, WhitePawn);
		
		model.setEnPassant(C6);
		ChessMove move = new ChessMoveEnPassantCapture(WhitePawn, D5, C6);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(C5) : "Black pawn should no longer be at C5 after en passant capture";
		assert WhitePawn == model.getPieceAtLocation(C6) : "White pawn should be at C6 after en passant capture";
		assert Unknown == model.getEnPassant() : "En passant location should be none after en passant capture";

		model.setEnPassant(D4);
		move = new ChessMoveEnPassantCapture(BlackPawn, C5, D4);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(D5) : "White pawn should no longer be at C5 after en passant capture";
		assert BlackPawn == model.getPieceAtLocation(D4) : "Black pawn should be at D4 after en passant capture";
		assert Unknown == model.getEnPassant() : "En passant location should be none after en passant capture";		
	}

	/**
	 * 
	 */
	@Test
	public void testPiecePlacementUpdatePawnPromotionMove() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(C7, WhitePawn);
		model.setPieceAtLocation(D2, BlackPawn);
		
		ChessMove move = new ChessMovePawnPromotion(WhitePawn, C7, C8, WhiteKnight);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(C7) : "Promoted white pawn is no longer at C7";
		assert WhiteKnight == model.getPieceAtLocation(C8) : "White pawn should be promoted to white knight";

		move = new ChessMovePawnPromotion(BlackPawn, D2, D1, BlackQueen);
		model = logic.applyMoveToModel(model, move);
		assert None == model.getPieceAtLocation(D2) : "Promoted black pawn is no longer at D2";
		assert BlackQueen == model.getPieceAtLocation(D1) : "Back pawn should be promoted to black queen";
	}

	
	@Test
	public void testActiveColorUpdate() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(E8, BlackKing);
		assert White == model.getActiveColor() : "White should be the active color in a new game";

		ChessMove move = new ChessMove(WhiteKing, E1, E2);
		model = logic.applyMoveToModel(model, move);
		assert Black == model.getActiveColor() : "Black should be the active color after white moves";

		move = new ChessMove(BlackKing, E8, E7);
		model = logic.applyMoveToModel(model, move);
		assert White == model.getActiveColor() : "White should be the active color after black moves";
	}
	
	
	@Test
	public void testCastlingAvailability() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(H1, WhiteRook);
		model.setPieceAtLocation(A8, BlackRook);
		model.setPieceAtLocation(E8, BlackKing);
		model.setPieceAtLocation(H8, BlackRook);
		
		CastlingAvailability castling = model.getCastlingAvailability();
		assert castling.isWhiteCanCastleKingSide() : "White can castle king side initially";
		assert castling.isWhiteCanCastleQueenSide() : "White can castle queen side initially";
		assert castling.isBlackCanCastleKingSide() : "Black can castle king side initially";
		assert castling.isBlackCanCastleQueenSide() : "Black can castle queen side initially";

		ChessMove move = new ChessMove(WhiteRook, A1, A2);
		model = logic.applyMoveToModel(model, move);
		castling = model.getCastlingAvailability();
		assert castling.isWhiteCanCastleKingSide() : "When A1 rook moves only queen side castle is removed";
		assert !castling.isWhiteCanCastleQueenSide() : "When A1 rook moves only queen side castle is removed";
		assert castling.isBlackCanCastleKingSide() : "When A1 rook moves only queen side castle is removed";
		assert castling.isBlackCanCastleQueenSide() : "When A1 rook moves only queen side castle is removed";

		model.setCastlingAvailability(new CastlingAvailability());
		move = new ChessMove(WhiteKing, E1, E2);
		model = logic.applyMoveToModel(model, move);
		castling = model.getCastlingAvailability();
		assert !castling.isWhiteCanCastleKingSide() : "When white king moves it can no longer castle on either side";
		assert !castling.isWhiteCanCastleQueenSide() : "When white king moves it can no longer castle on either side";
		assert castling.isBlackCanCastleKingSide() : "When white king moves it can no longer castle on either side";
		assert castling.isBlackCanCastleQueenSide() : "When white king moves it can no longer castle on either side";
		
		model.setCastlingAvailability(new CastlingAvailability());
		move = new ChessMove(WhiteRook, H1, H2);
		model = logic.applyMoveToModel(model, move);
		castling = model.getCastlingAvailability();
		assert !castling.isWhiteCanCastleKingSide() : "When H1 rook moves only king side castle is removed";
		assert castling.isWhiteCanCastleQueenSide() : "When H1 rook moves only king side castle is removed";
		assert castling.isBlackCanCastleKingSide() : "When H1 rook moves only king side castle is removed";
		assert castling.isBlackCanCastleQueenSide() : "When H1 rook moves only king side castle is removed";

		model.setCastlingAvailability(new CastlingAvailability());
		move = new ChessMove(BlackRook, A8, A7);
		model = logic.applyMoveToModel(model, move);
		castling = model.getCastlingAvailability();
		assert castling.isWhiteCanCastleKingSide() : "When A8 rook moves only queen side castle is removed";
		assert castling.isWhiteCanCastleQueenSide() : "When A8 rook moves only queen side castle is removed";
		assert castling.isBlackCanCastleKingSide() : "When A8 rook moves only queen side castle is removed";
		assert !castling.isBlackCanCastleQueenSide() : "When A8 rook moves only queen side castle is removed";

		model.setCastlingAvailability(new CastlingAvailability());
		move = new ChessMove(BlackKing, E8, E7);
		model = logic.applyMoveToModel(model, move);
		castling = model.getCastlingAvailability();
		assert castling.isWhiteCanCastleKingSide() : "When black king moves it can no longer castle on either side";
		assert castling.isWhiteCanCastleQueenSide() : "When black king moves it can no longer castle on either side";
		assert !castling.isBlackCanCastleKingSide() : "When black king moves it can no longer castle on either side";
		assert !castling.isBlackCanCastleQueenSide() : "When black king moves it can no longer castle on either side";

		model.setCastlingAvailability(new CastlingAvailability());
		move = new ChessMove(BlackRook, H8, H7);
		model = logic.applyMoveToModel(model, move);
		castling = model.getCastlingAvailability();
		assert castling.isWhiteCanCastleKingSide() : "When H8 rook moves only king side castle is removed";
		assert castling.isWhiteCanCastleQueenSide() : "When H8 rook moves only king side castle is removed";
		assert !castling.isBlackCanCastleKingSide() : "When H8 rook moves only king side castle is removed";
		assert castling.isBlackCanCastleQueenSide() : "When H8 rook moves only king side castle is removed";
	}
	
	
	@Test
	public void testEnPassantUpdate() {
		ChessModel model = new ChessModel();
		Location enPassant = model.getEnPassant(); 
		assert enPassant == Unknown : "en passant should be unknown in an empty board";

		model.setPieceAtLocation(A1, WhiteRook);
		model.setPieceAtLocation(E2, WhitePawn);
		model.setPieceAtLocation(F4, BlackPawn);
		model.setPieceAtLocation(D7, BlackPawn);

		ChessMove move = new ChessMove(WhiteRook, A1, A3);
		model = logic.applyMoveToModel(model, move);
		enPassant = model.getEnPassant();
		assert enPassant == Unknown : "en passant should be unknown for a non-pawn move";
		
		move = new ChessMove(WhitePawn, E2, E3);
		model = logic.applyMoveToModel(model, move);
		enPassant = model.getEnPassant();
		assert enPassant == Unknown : "en passant should be unknown for a 1 space pawn move";
		
		move = new ChessMove(WhitePawn, E3, F4);
		model = logic.applyMoveToModel(model, move);
		enPassant = model.getEnPassant();
		assert enPassant == Unknown : "en passant should be unknown for a diagonal capture";
		
		move = new ChessMove(WhitePawn, F4, F6);
		move.setEnPassantLocation(F5);
		model = logic.applyMoveToModel(model, move);
		enPassant = model.getEnPassant();
		assert enPassant == F5 : "en passant should be set to square behind white pawn's two space move";
		
		move = new ChessMove(BlackPawn, D7, D5);
		move.setEnPassantLocation(D6);
		model = logic.applyMoveToModel(model, move);
		enPassant = model.getEnPassant();
		assert enPassant == D6 : "en passant should be set to square behind black pawn's two space move";

		move = new ChessMove(BlackPawn, D5, D4);
		model = logic.applyMoveToModel(model, move);
		enPassant = model.getEnPassant();
		assert enPassant == Unknown : "en passant is set back to unknown after one space move";		
	}
	
	
	/**
	 * TODO: Implement halfmove clock tests (and logic)
	 */
	@Ignore
	public void testHalfmoveClockUpdate() {
		assert false;		
	}
	

	@Test
	public void testFullmoveNumberUpdate() {
		ChessModel model = new ChessModel();
		model.setPieceAtLocation(E1, WhiteKing);
		model.setPieceAtLocation(E8, BlackKing);
		assert 1 == model.getFullmoveNumber() : "Fullmove number should be 0 initially";

		ChessMove move = new ChessMove(WhiteKing, E1, E2);
		model = logic.applyMoveToModel(model, move);
		assert 1 == model.getFullmoveNumber() : "Fullmove number should be 0 after white makes first move";

		move = new ChessMove(BlackKing, E8, E7);
		model = logic.applyMoveToModel(model, move);
		assert 2 == model.getFullmoveNumber() : "Fullmove number is incremented to 1 after black's first move";
	}
}
