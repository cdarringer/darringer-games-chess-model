package com.darringer.games.chess.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all the possible chess board locations, providing additional
 * (x, y) coordinate values so that we can easily map between the two.
 * 
 * @author cdarringer
 *
 */
public enum Location { 
	A1(0, 0), B1(1, 0), C1(2, 0), D1(3, 0), E1(4, 0), F1(5, 0), G1(6, 0), H1(7, 0),
	A2(0, 1), B2(1, 1), C2(2, 1), D2(3, 1), E2(4, 1), F2(5, 1), G2(6, 1), H2(7, 1),
	A3(0, 2), B3(1, 2), C3(2, 2), D3(3, 2), E3(4, 2), F3(5, 2), G3(6, 2), H3(7, 2),
	A4(0, 3), B4(1, 3), C4(2, 3), D4(3, 3), E4(4, 3), F4(5, 3), G4(6, 3), H4(7, 3),
	A5(0, 4), B5(1, 4), C5(2, 4), D5(3, 4), E5(4, 4), F5(5, 4), G5(6, 4), H5(7, 4),
	A6(0, 5), B6(1, 5), C6(2, 5), D6(3, 5), E6(4, 5), F6(5, 5), G6(6, 5), H6(7, 5),
	A7(0, 6), B7(1, 6), C7(2, 6), D7(3, 6), E7(4, 6), F7(5, 6), G7(6, 6), H7(7, 6),
	A8(0, 7), B8(1, 7), C8(2, 7), D8(3, 7), E8(4, 7), F8(5, 7), G8(6, 7), H8(7, 7),
	Unknown(-1, -1);
	
	private int x;
	private int y;
	
    private static final Map<Coordinate, Location> lookup = new HashMap<Coordinate, Location>();

    static {
    	for (Location l : EnumSet.allOf(Location.class)) {
    		Coordinate key = new Coordinate(l.getX(), l.getY());
    		lookup.put(key, l);
    	}
	}
	
	private Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Location getRelativeLocation(int dx, int dy) {
		int newX = this.x + dx;
		int newY = this.y + dy;
		return get(newX, newY);
	}

	public static Location get(int x, int y) {
		Coordinate key = new Coordinate(x, y);
		Location location = lookup.get(key);
		if (location == null) {
			return Unknown;
		} else {
			return location;
		}
	}
}
