grammar ForsythEdwards;

tokens {

WHITE_PAWN = 'P';
WHITE_KNIGHT = 'N';
WHITE_BISHOP = 'B';
WHITE_ROOK = 'R';
WHITE_QUEEN = 'Q';
WHITE_KING = 'K';

BLACK_PAWN = 'p';
BLACK_KNIGHT = 'n';
BLACK_BISHOP = 'b';
BLACK_ROOK = 'r';
BLACK_QUEEN = 'q';
BLACK_KING = 'k';

}

//
// Header for generated parser. 
// What package should the generated parser source exist in?
// What additional imports with the parser need?
//
@header {
package com.darringer.games.chess.converter;
import com.darringer.games.chess.model.CastlingAvailability;
import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.Location; 
import com.darringer.games.chess.model.Piece;
import com.darringer.games.chess.model.Color;
import static com.darringer.games.chess.model.Color.*;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;
}

//
// Header for generated lexer.
// What package should the generated lexer source exist in?
//
@lexer::header{ 
package com.darringer.games.chess.converter; 
}

@lexer::members {
List<RecognitionException> exceptions = new ArrayList<RecognitionException>();

public List<RecognitionException> getExceptions() {
  return exceptions;
}

@Override
public void reportError(RecognitionException e) {
  super.reportError(e);
  exceptions.add(e);
}
}

	
/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

model returns [ChessModel model]
@init {
    model = new ChessModel();
}
	: ( 
		rankResult = rank { model.setRankAtIndex(rankResult, 7); } '/'
		rankResult = rank { model.setRankAtIndex(rankResult, 6); } '/' 
		rankResult = rank { model.setRankAtIndex(rankResult, 5); } '/' 
		rankResult = rank { model.setRankAtIndex(rankResult, 4); } '/' 
		rankResult = rank { model.setRankAtIndex(rankResult, 3); } '/' 
		rankResult = rank { model.setRankAtIndex(rankResult, 2); } '/' 
		rankResult = rank { model.setRankAtIndex(rankResult, 1); } '/' 
		rankResult = rank { model.setRankAtIndex(rankResult, 0); }   
	  ' ' colorResult = activeColor { model.setActiveColor(colorResult); }
	  ' ' castlingResult = castlingAvailability { model.setCastlingAvailability(castlingResult); }
	  ' ' enPassantResult = enPassant { model.setEnPassant(enPassantResult); }
	  ' ' halfmoveCount = halfmoveClock { model.setHalfmoveClock(halfmoveCount); }
	  ' ' fullmoveCount = fullmoveNumber { model.setFullmoveNumber(fullmoveCount); }
	  NEWLINE?
	  )
    ;
    
rank returns [ List<Piece> rank ]
@init {
    rank = new ArrayList<Piece>();
}
	: ( 
	pieceResult=piece { rank.add(pieceResult); } | 
	spaceCount=spaces { 
		for (int i=0; i < spaceCount; i++) { 
			rank.add(None); 
		} 
	} )+
	;
	
activeColor returns [ Color color ]
	: 
	'w' { $color = White; } | 
	'b' { $color = Black; } 
	;

castlingAvailability returns [ CastlingAvailability castlingAvailability ]
@init {
    castlingAvailability = new CastlingAvailability();
    // no availability unless specified below
    castlingAvailability.setWhiteCanCastleKingSide(false);
    castlingAvailability.setWhiteCanCastleQueenSide(false); 
	castlingAvailability.setBlackCanCastleKingSide(false); 
	castlingAvailability.setBlackCanCastleQueenSide(false);
}
	: ( 
	'-' |
	'K' { castlingAvailability.setWhiteCanCastleKingSide(true); } |
	'Q' { castlingAvailability.setWhiteCanCastleQueenSide(true); } |
	'k' { castlingAvailability.setBlackCanCastleKingSide(true); } |
	'q' { castlingAvailability.setBlackCanCastleQueenSide(true); }
	)+
	; 

enPassant returns  [ Location enPassant ]
	: 
	'-' { $enPassant = Unknown; } | 
	locationResult = location { $enPassant = locationResult; }
	;

halfmoveClock returns [ Integer count ] 
	: i = NUMERAL+ { $count = Integer.parseInt(i.getText()); }
	;

fullmoveNumber returns [ Integer count ]
	: i = NUMERAL+ { $count = Integer.parseInt(i.getText()); }
	;
		
piece returns [ Piece piece ]
@init {
	piece = null;
}
	: 
	WHITE_PAWN   { $piece = WhitePawn; } | 
	WHITE_KNIGHT { $piece = WhiteKnight; } | 
	WHITE_BISHOP { $piece = WhiteBishop; } |
	WHITE_ROOK   { $piece = WhiteRook; } | 
	WHITE_QUEEN  { $piece = WhiteQueen; } | 
	WHITE_KING   { $piece = WhiteKing; } |
	BLACK_PAWN   { $piece = BlackPawn; } |
	BLACK_KNIGHT { $piece = BlackKnight; } |
	BLACK_BISHOP { $piece = BlackBishop; } | 
	BLACK_ROOK   { $piece = BlackRook; } | 
	BLACK_QUEEN  { $piece = BlackQueen; } | 
	BLACK_KING   { $piece = BlackKing; }
	;

location returns [ Location l ]
@init {
	l = Unknown;
}
	:
	'a1' { $l = A1; } | 'b1' { $l = B1; } | 'c1' { $l = C1; } | 'd1' { $l = D1; } | 'e1' { $l = E1; } | 'f1' { $l = F1; } | 'g1' { $l = G1; } | 'h1' { $l = H1; } |  	 
	'a2' { $l = A2; } | 'b2' { $l = B2; } | 'c2' { $l = C2; } | 'd2' { $l = D2; } | 'e2' { $l = E2; } | 'f2' { $l = F2; } | 'g2' { $l = G2; } | 'h2' { $l = H2; } |  	 
	'a3' { $l = A3; } | 'b3' { $l = B3; } | 'c3' { $l = C3; } | 'd3' { $l = D3; } | 'e3' { $l = E3; } | 'f3' { $l = F3; } | 'g3' { $l = G3; } | 'h3' { $l = H3; } |  	 
	'a4' { $l = A4; } | 'b4' { $l = B4; } | 'c4' { $l = C4; } | 'd4' { $l = D4; } | 'e4' { $l = E4; } | 'f4' { $l = F4; } | 'g4' { $l = G4; } | 'h4' { $l = H4; } |  	 
	'a5' { $l = A5; } | 'b5' { $l = B5; } | 'c5' { $l = C5; } | 'd5' { $l = D5; } | 'e5' { $l = E5; } | 'f5' { $l = F5; } | 'g5' { $l = G5; } | 'h5' { $l = H5; } |  	 
	'a6' { $l = A6; } | 'b6' { $l = B6; } | 'c6' { $l = C6; } | 'd6' { $l = D6; } | 'e6' { $l = E6; } | 'f6' { $l = F6; } | 'g6' { $l = G6; } | 'h6' { $l = H6; } |  	 
	'a7' { $l = A7; } | 'b7' { $l = B7; } | 'c7' { $l = C7; } | 'd7' { $l = D7; } | 'e7' { $l = E7; } | 'f7' { $l = F7; } | 'g7' { $l = G7; } | 'h7' { $l = H7; } |  	 
	'a8' { $l = A8; } | 'b8' { $l = B8; } | 'c8' { $l = C8; } | 'd8' { $l = D8; } | 'e8' { $l = E8; } | 'f8' { $l = F8; } | 'g8' { $l = G8; } | 'h8' { $l = H8; }   	 
	;
	
spaces returns [Integer count]
	: i = NUMERAL+ { $count = Integer.parseInt(i.getText()); }
	;

	
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

NUMERAL : ('0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9');	

NEWLINE	: '\r'? '\n';

