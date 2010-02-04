package weigl.grammar.lltck.rt;
/**
 * You get this exception if no token rule matched 
 * @author Alexander Weigl <alexweigl@gmail.com>
 */
public class RecognitionException extends Exception {
	private static final long serialVersionUID = 4966594820080378192L;

	public RecognitionException(String message) {
		super(message);

	}

	public RecognitionException(Throwable cause) {
		super(cause);

	}

	public RecognitionException(String message, Throwable cause) {
		super(message, cause);

	}

}
