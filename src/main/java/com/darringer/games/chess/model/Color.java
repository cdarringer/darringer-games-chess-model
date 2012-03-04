package com.darringer.games.chess.model;

/**
 * We use this enumeration throuout the program 
 * to differentiate black pieces from white pieces.
 * 
 * @author cdarringer
 *
 */
public enum Color {
	White('w'), Black('b');
	
	private char code;
	
	Color(char code) {
		this.code = code;
	}
	
	public char getCode() {
		return this.code;
	}
}
