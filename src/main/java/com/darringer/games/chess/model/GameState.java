package com.darringer.games.chess.model;

/**
 * At any given time a game can be in one of the states contained
 * in this enumeration.  We provide a use friendly message for each 
 * state as well as a flag to tell use whether the game is still 
 * in progress when in the given state.
 * 
 * @author cdarringer
 *
 */
public enum GameState {
	New(true, "New"), OK(true, "OK"), 
	InvalidMove(false, "Invalid move"), 
	WhiteInCheck(true, "White is in check!"), 
	BlackInCheck(true, "Black is in check!"), 
	WhiteInCheckMate(false, "White is in check mate!"), 
	BlackInCheckMate(false, "Black is in check mate!"), 
	SystemException(false, "System exception");
	
	boolean isInProgress;
	String message;
	
	private GameState(boolean isInProgress, String message) {
		this.isInProgress = isInProgress;
		this.message = message;
	}
	
	public boolean isInProgress() {
		return isInProgress;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
