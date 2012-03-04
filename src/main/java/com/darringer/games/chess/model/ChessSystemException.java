package com.darringer.games.chess.model;

/**
 * System exceptions are thrown whenever the program enters an
 * unanticipated state (a chess model string could not be parsed, 
 * for example).  See the underlying message for additional details.
 * 
 * @author cdarringer
 *
 */
public class ChessSystemException extends Exception {

	private static final long serialVersionUID = 1L;

	public ChessSystemException(Throwable rootCause) {
		super(rootCause);
	}
	
	public ChessSystemException(String message) {
		super(message);
	}
}
