package paxby.combinatorics.tsp;

/**
 * Exception thrown if TSPLIB file is of unsupported type
 * 
 * @author Petter Axby
 * 
 */
public class UnsupportedFileException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnsupportedFileException(String s) {
		super(s);
	}
}
