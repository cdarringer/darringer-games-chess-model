package com.darringer.games.chess.converter;

import static com.darringer.games.chess.model.Location.Unknown;
import static com.darringer.games.chess.model.Piece.None;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessSystemException;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link ChessModelConverter} that assumes the 
 * Forsyth Edwards Notation (FEN) is the underlying string representation
 * of the {@link ChessModel}.  Relies on ANTLR to perform the parsing of 
 * FEN string - aruably overkill - but more fun than writing tedious string
 * parsing code!
 * <p/>
 * See also <code>ForsythEdwards.g</code> for the grammar file
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.converter.ChessModelConverter
 * @see com.darringer.games.chess.model.ChessModel
 *
 */
public class ChessModelFENConverter implements ChessModelConverter {

	/*
	 * FEN representation of a new game
	 */
	public static final String FEN_NEW = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	/*
	 * FEN representatio of a game consisting only of pawns
	 * (useful for testing purposes)
	 */
	public static final String FEN_PAWN_GAME = "8/pppppppp/8/8/8/8/PPPPPPPP/8 w KQkq - 0 1";
	
	private static Logger log = Logger.getLogger(ChessModelConverter.class);
	
	/**
	 * Default constructor
	 */
	public ChessModelFENConverter() {
		
	}

	/**
	 * @see com.darringer.games.chess.converter.ChessModelConverter#getModelFromString(String)
	 */
	@Override
	public ChessModel getModelFromString(String modelAsString) throws ChessSystemException {
		ChessModel model = null;
		try {
			// create the lexer and parser
			log.info("Creating ANTRL lexers and parsers...");
	    	CharStream stream = new ANTLRStringStream(modelAsString + "\n");
	    	ForsythEdwardsLexer lexer = new ForsythEdwardsLexer(stream);
	        CommonTokenStream tokens = new CommonTokenStream(lexer);
	        ForsythEdwardsParser parser = new ForsythEdwardsParser(tokens);	
	        
	        // parse the string
			log.info("Using ANTLR to parse the FEN string and build a model...");
        	model = parser.model();
        	
        	// were there lexer errors?
            List<RecognitionException> lexerExceptions = lexer.getExceptions();
            if (!lexerExceptions.isEmpty()) {
            	log.error("Exceptions were encountered in the parsing process.");
            	throw new ChessSystemException(lexerExceptions.get(0));
            }        	
        } catch (RecognitionException re) {
        	log.error("Recognition exceptions encountered in the parsing process: " + re.getMessage());
        	throw new ChessSystemException(re);
        } catch (NullPointerException npe) {
        	log.error("Null pointer exceptions encounteed in the parsing process: " + npe.getMessage());
        }
        log.info("...parsing complete, returning model.");
		return model;
	}

	/**
	 * @see com.darringer.games.chess.converter.ChessModelConverter#getStringFromModel(ChessModel)
	 */
	@Override
	public String getStringFromModel(ChessModel model) {
		// for example, "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";	
		return String.format("%s/%s/%s/%s/%s/%s/%s/%s %s %s %s %d %d",
				getRankAtIndex(model, 7),
				getRankAtIndex(model, 6),
				getRankAtIndex(model, 5),
				getRankAtIndex(model, 4),
				getRankAtIndex(model, 3),
				getRankAtIndex(model, 2),
				getRankAtIndex(model, 1),
				getRankAtIndex(model, 0),
				model.getActiveColor().getCode(), 
				model.getCastlingAvailability(),
				model.getEnPassant() == Unknown ? "-" : model.getEnPassant().toString().toLowerCase(),
				model.getHalfmoveClock(), 
				model.getFullmoveNumber());		
	}

	
	/**
	 * Helper function for printing ranks in the FEN string
	 * 
	 * @param model
	 * @param y
	 * @return
	 */
	private String getRankAtIndex(ChessModel model, int y) {
		StringBuilder rankString = new StringBuilder();
		int spaceCount = 0;
		for (int x=0; x < 8; x++) {
			Piece currentPiece = model.getPieceAtIndex(x, y);
			if (currentPiece == None) {
				spaceCount++;
			} else {
				if (spaceCount != 0) {
					rankString.append(spaceCount);
					spaceCount = 0;
				}
				rankString.append(currentPiece.getCode());
			}
		}
		if (spaceCount != 0) {
			rankString.append(spaceCount);
		}
		return rankString.toString();
	}	
}
