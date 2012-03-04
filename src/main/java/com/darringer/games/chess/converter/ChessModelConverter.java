package com.darringer.games.chess.converter;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessSystemException;

/**
 * Interface for various ways of building a {@link ChessModel} from 
 * a string or generating a string representation of an existing 
 * {@link ChessModel}.  Useful for things like saving games or 
 * passing them through service requests in a light weight fashion.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessModel
 * @see com.darringer.games.chess.model.ChessSystemException;
 *
 */
public interface ChessModelConverter {

	/**
	 * Return a {@link ChessModel} associated with the given string
	 * or throw an exception if the string was not in a format expected
	 * by the underlying converter.
	 * 
	 * @param modelAsString
	 * @return
	 * @throws ChessSystemException
	 */
	ChessModel getModelFromString(String modelAsString) throws ChessSystemException;
	
	/**
	 * Return a string associated with the given {@link ChessModel object}.
	 * 
	 * @param model
	 * @return
	 */
	String getStringFromModel(ChessModel model);
}
