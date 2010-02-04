package weigl.std;

/**
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @param <E>
 */
public interface WIterator<E> {
	public E next() throws NoMoreElementsException, Exception;
}
