package weigl.std;

import java.util.Iterator;

public class WIteratorAdapter<E> implements Iterator<E>{
	private WIterator<E> iterator;
	private E nextElement;

	public WIteratorAdapter(WIterator<E> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		try
		{
			nextElement = iterator.next();
			return true;
		}catch(Exception ne)
		{
			return false;
		}
	}

	@Override
	public E next() {
		return nextElement;
	}

	@Override
	public void remove() {
	}
}
