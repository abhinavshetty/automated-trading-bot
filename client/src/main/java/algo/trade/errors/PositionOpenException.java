package algo.trade.errors;

/**
 * Thrown by operations where open position prevents further processing
 * Exception thrown if position is still open.
 * @author Abhinav Shetty
 *
 */
public class PositionOpenException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2504563320862513885L;
}
