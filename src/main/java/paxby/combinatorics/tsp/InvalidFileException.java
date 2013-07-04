package paxby.combinatorics.tsp;

/**
 * Exception thrown if file is invalid and cannot be parsed
 * 
 * @author Petter Axby
 * 
 */
public class InvalidFileException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidFileException(String s) {
		super(s);
	}
}
