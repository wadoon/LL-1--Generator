package weigl.std;

import java.util.Iterator;

/**
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @param <E>
 */
public class IteratorAdapter<E> implements WIterator<E>{
	private Iterator<E> iterator;
	
	public IteratorAdapter(Iterator<E> iterator) {
		this.iterator = iterator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() throws NoMoreElementsException 
	{
		if(iterator.hasNext())
		{
			return iterator.next();
		}
		else
		{
			throw new NoMoreElementsException();
		}
	}

}
