package com.darringer.games.chess.model;

/**
 * Helper class for looking up {@link Location}s from (x, y) 
 * "coordinates".
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.Location
 *
 */
public class Coordinate {
	  private final Integer x;
	  private final Integer y;

	  public Coordinate(Integer x, Integer y) {
	    this.x = x;
	    this.y = y;
	  }

	  public Integer getX() { return x; }
	  public Integer getY() { return y; }

	  @Override
	  public int hashCode() { return x.hashCode() ^ y.hashCode(); }

	  @Override
	  public boolean equals(Object o) {
		  if (o == null) return false;
		  if (!(o instanceof Coordinate)) return false;
		  Coordinate co = (Coordinate) o;
		  return this.x.equals(co.getX()) && this.y.equals(co.getY());
	  }
}