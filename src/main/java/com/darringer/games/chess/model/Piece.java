package com.darringer.games.chess.model;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;

import com.darringer.games.chess.logic.BishopLogic;
import com.darringer.games.chess.logic.KingLogic;
import com.darringer.games.chess.logic.KnightLogic;
import com.darringer.games.chess.logic.NoneLogic;
import com.darringer.games.chess.logic.PawnLogic;
import com.darringer.games.chess.logic.PieceLogic;
import com.darringer.games.chess.logic.QueenLogic;
import com.darringer.games.chess.logic.RookLogic;

/**
 * Enumeration of all the possible pieces on a board.
 * We use this enumeration to associate the following things
 * with each piece:
 * <ol>
 *   <li>A single character code</li>
 *   <li>A {@link Color}</li>
 *   <li>A relative point value signifying the importance of this piece</li>
 *   <li>A {@link PieceLogic} implementation</li>
 * </ol>
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.Color
 * @see com.darringer.games.chess.logic.EvalationLogic
 * @see com.darringer.games.chess.logic.PieceLogic
 *
 */
public enum Piece {
	WhitePawn('P', White, 1, new PawnLogic(White)), 
	WhiteKnight('N', White, 3, new KnightLogic(White)), 
	WhiteBishop('B', White, 3, new BishopLogic(White)), 
	WhiteRook('R', White, 5, new RookLogic(White)), 
	WhiteQueen('Q', White, 9, new QueenLogic(White)), 
	WhiteKing('K', White, 100, new KingLogic(White)),
	
	BlackPawn('p', Black, -1, new PawnLogic(Black)), 
	BlackKnight('n', Black, -3, new KnightLogic(Black)), 
	BlackBishop('b', Black, -3, new BishopLogic(Black)),
	BlackRook('r', Black, -5, new RookLogic(Black)), 
	BlackQueen('q', Black, -9, new QueenLogic(Black)), 
	BlackKing('k', Black, -100, new KingLogic(Black)),
	
	None(' ', null, 0, new NoneLogic(null));
	
	private char code;
	private Color color;
	private int points;
	private PieceLogic logic;

	private Piece(char code, Color color, int points, PieceLogic logic) {
		this.code = code;
		this.color = color;
		this.points = points;
		this.logic= logic;
	}
	
	public char getCode() {
		return this.code;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public PieceLogic getLogic() {
		return this.logic;
	}
	
}
