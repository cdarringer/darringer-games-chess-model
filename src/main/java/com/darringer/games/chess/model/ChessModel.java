package com.darringer.games.chess.model;

import static com.darringer.games.chess.model.GameState.OK;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;
import static com.darringer.games.chess.model.Color.*;

/**
 * Model representing an entire chess game.  In addition to 
 * the state of the chess board, this model maintains information
 * about the number of moves, active player, castling availability, 
 * etc.
 * <p />
 * It should be relatively easy to convert this model to/from a 
 * string representation.  
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.converter.ChessModelConverter
 * @see com.darringer.games.chess.model.CastlingAvailability
 *
 */
public class ChessModel {
	private Piece[][] pieces;
	private Color activeColor;
	private CastlingAvailability castlingAvailability;
	private Location enPassant;
	private int halfmoveClock;
	private int fullmoveNumber;
	private GameState state;
	private float score;
	
	private static Logger log = Logger.getLogger(ChessModel.class);
	
	/**
	 * Default constructor creates an empty model
	 */
	public ChessModel() {
		 pieces = new Piece[8][8];
		 for (int x=0; x < 8; x++) {
			 for (int y=0; y < 8; y++) {
				 pieces[y][x] = None;
			 }
		 }
		 activeColor = White;
		 castlingAvailability = new CastlingAvailability();
		 enPassant = Unknown;
		 halfmoveClock = 0;
		 fullmoveNumber = 1;
		 state = OK;
		 score = 0.0f;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param model
	 */	
	public ChessModel(ChessModel model) {
		 this.pieces = new Piece[8][8];
		 for (int x=0; x < 8; x++) {
			 for (int y=0; y < 8; y++) {
				 this.pieces[y][x] = model.pieces[y][x];
			 }
		 }
		 this.activeColor = model.activeColor;
		 this.castlingAvailability = new CastlingAvailability(model.castlingAvailability);
		 this.enPassant = model.enPassant;
		 this.halfmoveClock = model.halfmoveClock;
		 this.fullmoveNumber = model.fullmoveNumber;
		 this.state = model.state;
		 this.score = model.score;
	}
	
	/**
	 * Utility function for locating a {@link Piece} by 
	 * its (x, y) coordinates.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Piece getPieceAtIndex(int x, int y) {
		assert (x >= 0) && (x < 8) : "x out of bounds";
		assert (y >= 0) && (y < 8) : "y out of bounds";
		return pieces[y][x];
	}
	
	public Piece getPieceAtLocation(Location location) {
		return pieces[location.getY()][location.getX()];
	}
	
	public void setPieceAtLocation(Location location, Piece piece) {
		pieces[location.getY()][location.getX()] = piece;
	}

	public boolean isLocationEmpty(Location location) {
		return (pieces[location.getY()][location.getX()] == None);
	}
	
	public boolean isLocationOccupied(Location location) {
		return (pieces[location.getY()][location.getX()] != None);
	}
	
	public boolean isLocationOccupiedByColor(Location location, Color color) {
		Piece piece = pieces[location.getY()][location.getX()];
		return ((piece != None) && (piece.getColor() == color)); 
	}
	
	public Color getActiveColor() {
		return this.activeColor;
	}
	
	public CastlingAvailability getCastlingAvailability() {
		return this.castlingAvailability;
	}
	
	public Location getEnPassant() {
		return this.enPassant;
	}
	
	public int getHalfmoveClock() {
		return this.halfmoveClock;
	}
	
	public int getFullmoveNumber() {
		return this.fullmoveNumber;
	}
	
	public GameState getState() {
		return this.state;
	}
	
	public void setRankAtIndex(List<Piece> rankPieces, int y) {
		if ((rankPieces == null) || (rankPieces.size() != 8)) {
 			log.error("Invalid rank");
		} else {
			for (int x=0; x < 8; x++) {
				this.pieces[y][x] = rankPieces.get(x);
			}
		}
	}
	
	public void setActiveColor(Color activeColor) {
		this.activeColor = activeColor;
	}
	
	public void setCastlingAvailability(CastlingAvailability castlingAvailability) {
		this.castlingAvailability = castlingAvailability;
	}
	
	public void setEnPassant(Location enPassant) {
		this.enPassant = enPassant;
	}
	
	public void setHalfmoveClock(int halfmoveClock) {
		this.halfmoveClock = halfmoveClock;
	}
	
	public void setFullmoveNumber(int fullmoveNumber) {
		this.fullmoveNumber = fullmoveNumber;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
	public float getScore() {
		return this.score;
	}
	
	public void setScore(float score) {
		this.score = score;
	}
	
	/**
	 * Use a velocity template to pretty-print this model
	 * 
	 * @return
	 */
	public String prettyFormat() {
		log.info( "Pretty formatting chess model..." );
		Reader reader = new InputStreamReader( getClass().getClassLoader().getResourceAsStream("model.vm"));
		VelocityContext context = new VelocityContext();
		context.put("model", this);
		StringWriter writer = new StringWriter();
		try {
			Velocity.evaluate(context, writer, "", reader);
		} catch (IOException ioe) {
			log.error("Failed to pretty format the chess model: " + ioe.getMessage());
			return "FORMATTING ERROR";
		}
		return writer.toString();		
	}	
}
