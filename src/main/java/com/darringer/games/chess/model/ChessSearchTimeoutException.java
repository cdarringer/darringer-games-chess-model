package com.darringer.games.chess.model;

/**
 * It is impossible to exhaustively search the tree, so we will
 * need to cutoff the search after a certain amount of time has 
 * elapsed. 
 * 
 * @author cdarringer
 *
 */
public class ChessSearchTimeoutException extends Exception {

	private static final long serialVersionUID = 1L;

	public ChessSearchTimeoutException(Throwable rootCause) {
		super(rootCause);
	}
	
	public ChessSearchTimeoutException(String message) {
		super(message);
	}
	
}
