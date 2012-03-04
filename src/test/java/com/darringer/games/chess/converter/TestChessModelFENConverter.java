package com.darringer.games.chess.converter;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import org.junit.Test;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessSystemException;

/**
 * Ensure that the conversion between {@link ChessModel}s and
 * their Forsyth-Edwards Notation (FEN) string equivalents is 
 * working correctly
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessModel
 * @see com.darringer.games.chess.converter.ChessModelFENConverter
 *
 */
public class TestChessModelFENConverter {

	private static final String FEN_NEW = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	private static final String FEN_E4 = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";	
	private static final String FEN_E4_C5 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
	private static final String FEN_E4_C5_NF3 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
	private static final String FEN_CASTLING_KQk = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1";
	private static final String FEN_CASTLING_qkK = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w qkK - 0 1";
	private static final String FEN_CASTLING_Q = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Q - 0 1";
	private static final String FEN_CASTLING_NONE = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1";
	private static final String FEN_BAD = "XYZ";
	private static final String FEN_BAD_ACTIVE_COLOR = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR X KQkq - 0 1";
	private static final String FEN_BAD_CASTLING = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w XYZ - 0 1";
	private static final String FEN_BAD_EN_PASSANT = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq X 0 1";
	private static final String FEN_BAD_HALFMOVE_CLOCK = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - X 1";
	private static final String FEN_BAD_FULLMOVE_NUMBER = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 X";

	private ChessModelConverter converter = new ChessModelFENConverter();
	
	@Test
	public void testNewModel() {
		ChessModel model = null;
		try {
			model = converter.getModelFromString(FEN_NEW);	
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert model != null : "valid string, expected model";

		// pretty print the game
		System.out.print(model.prettyFormat());
		
		// piece assertions
		assert model.getPieceAtLocation(A1).equals(WhiteRook) : "expected white rook at A1";	
		assert model.getPieceAtLocation(B1).equals(WhiteKnight) : "expected white knight at B1";	
		assert model.getPieceAtLocation(C1).equals(WhiteBishop) : "expected white bishop at C1";	
		assert model.getPieceAtLocation(D1).equals(WhiteQueen) : "expected white queen at D1";	
		assert model.getPieceAtLocation(E1).equals(WhiteKing) : "expected white king at E1";	
		assert model.getPieceAtLocation(F1).equals(WhiteBishop) : "expected white bishop at F1";	
		assert model.getPieceAtLocation(G1).equals(WhiteKnight) : "expected white knight at G1";	
		assert model.getPieceAtLocation(H1).equals(WhiteRook) : "expected white rook at H1";	

		assert model.getPieceAtLocation(A2).equals(WhitePawn) : "expected white pawn at A2";	
		assert model.getPieceAtLocation(B2).equals(WhitePawn) : "expected white pawn at B2";	
		assert model.getPieceAtLocation(C2).equals(WhitePawn) : "expected white pawn at C2";	
		assert model.getPieceAtLocation(D2).equals(WhitePawn) : "expected white pawn at D2";	
		assert model.getPieceAtLocation(E2).equals(WhitePawn) : "expected white pawn at E2";	
		assert model.getPieceAtLocation(F2).equals(WhitePawn) : "expected white pawn at F2";	
		assert model.getPieceAtLocation(G2).equals(WhitePawn) : "expected white pawn at G2";	
		assert model.getPieceAtLocation(H2).equals(WhitePawn) : "expected white pawn at H2";	

		assert model.getPieceAtLocation(A3).equals(None) : "expected none at A3";	
		assert model.getPieceAtLocation(B3).equals(None) : "expected none at B3";	
		assert model.getPieceAtLocation(C3).equals(None) : "expected none at C3";	
		assert model.getPieceAtLocation(D3).equals(None) : "expected none at D3";	
		assert model.getPieceAtLocation(E3).equals(None) : "expected none at E3";	
		assert model.getPieceAtLocation(F3).equals(None) : "expected none at F3";	
		assert model.getPieceAtLocation(G3).equals(None) : "expected none at G3";	
		assert model.getPieceAtLocation(H3).equals(None) : "expected none at H3";	

		assert model.getPieceAtLocation(A4).equals(None) : "expected none at A4";	
		assert model.getPieceAtLocation(B4).equals(None) : "expected none at B4";	
		assert model.getPieceAtLocation(C4).equals(None) : "expected none at C4";	
		assert model.getPieceAtLocation(D4).equals(None) : "expected none at D4";	
		assert model.getPieceAtLocation(E4).equals(None) : "expected none at E4";	
		assert model.getPieceAtLocation(F4).equals(None) : "expected none at F4";	
		assert model.getPieceAtLocation(G4).equals(None) : "expected none at G4";	
		assert model.getPieceAtLocation(H4).equals(None) : "expected none at H4";	

		assert model.getPieceAtLocation(A5).equals(None) : "expected none at A5";	
		assert model.getPieceAtLocation(B5).equals(None) : "expected none at B5";	
		assert model.getPieceAtLocation(C5).equals(None) : "expected none at C5";	
		assert model.getPieceAtLocation(D5).equals(None) : "expected none at D5";	
		assert model.getPieceAtLocation(E5).equals(None) : "expected none at E5";	
		assert model.getPieceAtLocation(F5).equals(None) : "expected none at F5";	
		assert model.getPieceAtLocation(G5).equals(None) : "expected none at G5";	
		assert model.getPieceAtLocation(H5).equals(None) : "expected none at H5";	

		assert model.getPieceAtLocation(A6).equals(None) : "expected none at A6";	
		assert model.getPieceAtLocation(B6).equals(None) : "expected none at B6";	
		assert model.getPieceAtLocation(C6).equals(None) : "expected none at C6";	
		assert model.getPieceAtLocation(D6).equals(None) : "expected none at D6";	
		assert model.getPieceAtLocation(E6).equals(None) : "expected none at E6";	
		assert model.getPieceAtLocation(F6).equals(None) : "expected none at F6";	
		assert model.getPieceAtLocation(G6).equals(None) : "expected none at G6";	
		assert model.getPieceAtLocation(H6).equals(None) : "expected none at H6";	

		assert model.getPieceAtLocation(A7).equals(BlackPawn) : "expected black pawn at A7";	
		assert model.getPieceAtLocation(B7).equals(BlackPawn) : "expected black pawn at B7";	
		assert model.getPieceAtLocation(C7).equals(BlackPawn) : "expected black pawn at C7";	
		assert model.getPieceAtLocation(H7).equals(BlackPawn) : "expected black pawn at H7";	
		assert model.getPieceAtLocation(D7).equals(BlackPawn) : "expected black pawn at D7";	
		assert model.getPieceAtLocation(E7).equals(BlackPawn) : "expected black pawn at E7";	
		assert model.getPieceAtLocation(F7).equals(BlackPawn) : "expected black pawn at F7";	
		assert model.getPieceAtLocation(G7).equals(BlackPawn) : "expected black pawn at G7";	

		assert model.getPieceAtLocation(A8).equals(BlackRook) : "expected black rook at A8";	
		assert model.getPieceAtLocation(B8).equals(BlackKnight) : "expected black knight at B8";	
		assert model.getPieceAtLocation(C8).equals(BlackBishop) : "expected black bishop at C8";	
		assert model.getPieceAtLocation(D8).equals(BlackQueen) : "expected black queen at D8";	
		assert model.getPieceAtLocation(E8).equals(BlackKing) : "expected black king at E8";	
		assert model.getPieceAtLocation(F8).equals(BlackBishop) : "expected black bishop at F8";	
		assert model.getPieceAtLocation(G8).equals(BlackKnight) : "expected black knight at G8";	
		assert model.getPieceAtLocation(H8).equals(BlackRook) : "expected black rook at H8";			

		assert model.getActiveColor().equals(White) : "expected active color to be white";

		assert model.getCastlingAvailability().isWhiteCanCastleKingSide() : "expected white can castle king side";
		assert model.getCastlingAvailability().isWhiteCanCastleQueenSide() : "expected white can castle queen side";
		assert model.getCastlingAvailability().isBlackCanCastleKingSide() : "expected black can castle king side";
		assert model.getCastlingAvailability().isBlackCanCastleQueenSide() : "expected black can castle queen side";
		
		assert model.getEnPassant() == Unknown : "expected no enPassant target square";		
		
		assert model.getHalfmoveClock() == 0 : "expected halfmove clock to be 0";

		assert model.getFullmoveNumber() == 1 : "expected fullmove number to be 1";
	}
		
	@Test
	public void testFirstMove() {
		ChessModel model = null;
		try {
			model = converter.getModelFromString(FEN_E4);	
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert model != null : "valid string, expected model";

		// pawn is in its new position (E4), not its original position (E2)
		assert model.getPieceAtLocation(E2).equals(None) : "white pawn should no longer be at E2";		
		assert model.getPieceAtLocation(E4).equals(WhitePawn) : "white pawn should now be at E4";

		// white moved, black turn next
		assert model.getActiveColor().equals(Black) : "expected active color to be black";

		// en passant
		assert model.getEnPassant() == E3 : "expected e3 to be enPassant target square";	
		
		// move is still move 1
		assert model.getFullmoveNumber() == 1 : "expected fullmove number to be 1";
	}

	@Test
	public void testSecondMove() {
		ChessModel model = null;
		try {
			model = converter.getModelFromString(FEN_E4_C5);	
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert model != null : "valid string, expected model";

		// pawn is in its new position (C5), not its original position (C7)
		assert model.getPieceAtLocation(C7).equals(None) : "black pawn should no longer be at C7";		
		assert model.getPieceAtLocation(C5).equals(BlackPawn) : "black pawn should now be at C5";

		// black moved, white turn next
		assert model.getActiveColor().equals(White) : "expected active color to be white";		
		
		// en passant
		assert model.getEnPassant() == C6 : "expected c6 to be enPassant target square";	

		// black response is now move 2
		assert model.getFullmoveNumber() == 2 : "expected fullmove number to be 2";
	}
	
	@Test
	public void testThirdMove() {
		ChessModel model = null;
		try {
			model = converter.getModelFromString(FEN_E4_C5_NF3);	
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert model != null : "Valid string, expected model";

		// white knight is in its new position (F3), not its original position (G1)
		assert model.getPieceAtLocation(G1).equals(None) : "white knight should no longer be at G1";		
		assert model.getPieceAtLocation(F3).equals(WhiteKnight) : "white knight should now be at F3";
		
		// white moved, black turn next
		assert model.getActiveColor().equals(Black) : "expected active color to be black";
		
		// en passant
		assert model.getEnPassant() == Unknown : "expected enPassant target square to be unknown";	

		// white response is still move 2
		assert model.getFullmoveNumber() == 2 : "expected fullmove number to be 2";
	}
		
	
	@Test
	public void testCastlingAvailability() {
		// KQk
		ChessModel model = null;
		try {
			model = converter.getModelFromString(FEN_CASTLING_KQk);	
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert model != null : "valid string, expected model";
		assert model.getCastlingAvailability().isWhiteCanCastleKingSide() : "expected white can castle king side";
		assert model.getCastlingAvailability().isWhiteCanCastleQueenSide() : "expected white can castle queen side";
		assert model.getCastlingAvailability().isBlackCanCastleKingSide() : "expected black can castle king side";
		assert !model.getCastlingAvailability().isBlackCanCastleQueenSide() : "expected black cannot castle queen side";
		
		// qkK
		model = null;
		try {
			model = converter.getModelFromString(FEN_CASTLING_qkK);	
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}		
		assert model != null : "valid string, expected model";
		assert model.getCastlingAvailability().isWhiteCanCastleKingSide() : "expected white can castle king side";
		assert !model.getCastlingAvailability().isWhiteCanCastleQueenSide() : "expected white cannot castle queen side";
		assert model.getCastlingAvailability().isBlackCanCastleKingSide() : "expected black can castle king side";
		assert model.getCastlingAvailability().isBlackCanCastleQueenSide() : "expected black can castle queen side";
		
		// Q
		model = null;
		try {
			model = converter.getModelFromString(FEN_CASTLING_Q);	
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}			
		assert model != null : "valid string, expected model";
		assert !model.getCastlingAvailability().isWhiteCanCastleKingSide() : "expected white cannot castle king side";
		assert model.getCastlingAvailability().isWhiteCanCastleQueenSide() : "expected white can castle queen side";
		assert !model.getCastlingAvailability().isBlackCanCastleKingSide() : "expected black cannot castle king side";
		assert !model.getCastlingAvailability().isBlackCanCastleQueenSide() : "expected black cannot castle queen side";
		
		// - 
		model = null;
		try {
			model = converter.getModelFromString(FEN_CASTLING_NONE);	
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}			
		assert model != null : "Valid string, expected model";
		assert !model.getCastlingAvailability().isWhiteCanCastleKingSide() : "expected white cannot castle king side";
		assert !model.getCastlingAvailability().isWhiteCanCastleQueenSide() : "expected white cannot castle queen side";
		assert !model.getCastlingAvailability().isBlackCanCastleKingSide() : "expected black cannot castle king side";
		assert !model.getCastlingAvailability().isBlackCanCastleQueenSide() : "expected black cannot castle queen side";
	}	
		
	@Test 
	public void testTwoWayConversions() {
		ChessModel model = null;
		String modelAsString = null;
		try {
			model = converter.getModelFromString(FEN_NEW);	
			modelAsString = converter.getStringFromModel(model);
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert modelAsString.equals(FEN_NEW) : "converting a new game to a model and back to string should give the same result";
		
		try {
			model = converter.getModelFromString(FEN_E4);	
			modelAsString = converter.getStringFromModel(model);
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert modelAsString.equals(FEN_E4) : "converting a game (E4) to a model and back to string should give the same result";

		try {
			model = converter.getModelFromString(FEN_E4_C5);
			modelAsString = converter.getStringFromModel(model);
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert modelAsString.equals(FEN_E4_C5) : "converting a game (E4 C5) to a model and back to string should give the same result";

		try {
			model = converter.getModelFromString(FEN_E4_C5_NF3);	
			modelAsString = converter.getStringFromModel(model);
		} catch (ChessSystemException cse) {
			assert false : "unexpected system exception";
		}
		assert modelAsString.equals(FEN_E4_C5_NF3) : "converting a game (E4, C5, NF3) to a model and back to string should give the same result";		
	}
	
	
	@Test
	public void testInvalidStrings() {
		ChessModel model; 
	
		// nonsensical string
		model = new ChessModel();
		try {
			model = converter.getModelFromString(FEN_BAD);
		} catch (ChessSystemException cse) {
			model = null;
		}		
		assert model == null : "Bad string, expecting exception and null model";	
		
		// nonsensical active color
		model = new ChessModel();
		try {
			model = converter.getModelFromString(FEN_BAD_ACTIVE_COLOR);
		} catch (ChessSystemException cse) {
			model = null;
		}		
		assert model == null : "Bad active color, expecting exception and null model";	

		// nonsensical castling data
		model = new ChessModel();
		try {
			model = converter.getModelFromString(FEN_BAD_CASTLING);
		} catch (ChessSystemException cse) {
			model = null;
		}		
		assert model == null : "Bad castling data, expecting exception and null model";	

		// nonsensical en passant
		model = new ChessModel();
		try {
			model = converter.getModelFromString(FEN_BAD_EN_PASSANT);
		} catch (ChessSystemException cse) {
			model = null;
		}		
		assert model == null : "Bad en passant, expecting exception and null model";	
		
		// nonsensical halfmove clock
		model = new ChessModel();
		try {
			model = converter.getModelFromString(FEN_BAD_HALFMOVE_CLOCK);
		} catch (ChessSystemException cse) {
			model = null;
		}
		assert model == null : "Bad halfmove clock data, expecting exception and null model";	

		// nonsensical fullmove number
		model = new ChessModel();
		try {
			model = converter.getModelFromString(FEN_BAD_FULLMOVE_NUMBER);
		} catch (ChessSystemException cse) {
			model = null;
		}		
		assert model == null : "Bad fullmove number data, expecting exception and null model";		
	}
}
