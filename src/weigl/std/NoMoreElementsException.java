package weigl.std;

/**
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 */
public class NoMoreElementsException extends Exception {

	private static final long serialVersionUID = 8785689377493934763L;

	public NoMoreElementsException() {
		super();

	}

	public NoMoreElementsException(String message, Throwable cause) {
		super(message, cause);

	}

	public NoMoreElementsException(String message) {
		super(message);

	}

	public NoMoreElementsException(Throwable cause) {
		super(cause);

	}

}
