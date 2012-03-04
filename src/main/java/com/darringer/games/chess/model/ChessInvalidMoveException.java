package com.darringer.games.chess.model;

/**
 * Thrown when a given {@link ChessMove} is not valid. 
 * The underlying message will contain details on why the 
 * move was not considered valid.
 * 
 * @author cdarringer
 *
 */
public class ChessInvalidMoveException extends Exception {
	private static final long serialVersionUID = 1L;

	
	public ChessInvalidMoveException(Throwable rootCause) {
		super(rootCause);
	}
	
	public ChessInvalidMoveException(String message) {
		super(message);
	}
}
