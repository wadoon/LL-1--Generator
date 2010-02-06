package weigl.std;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Class for holding an {@link Array} of type E 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @param <E> an {@link Comparable} type
 */
public class Array<E extends Comparable<E>> 
						implements Comparable<Array<E>>,
									Iterable<E>
						{
	private E[] array;

	public Array(E... tokens) {
		this.array = tokens;
	}

	@Override
	public String toString() {
		return "Array" + Arrays.toString(array);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(array);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Array<E> other = (Array<E>) obj;
		if (!Arrays.equals(array, other.array))
			return false;
		return true;
	}

	@Override
	public int compareTo(Array<E> o) {
		for (int i = 0; i < array.length; i++) {
			if (i < o.array.length) {
				int c = array[i].compareTo(o.array[i]);
				if (c != 0)
					return c;
			} else {
				return array.length - o.array.length;
			}
		}
		return o.array.length - array.length;
	}

	public E[] elements() {
		return array;
	}

	public List<E> toList() {
		return Arrays.asList(array);
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			int i = 0;
			
			@Override
			public boolean hasNext() {
				return i<array.length;
			}

			@Override
			public E next() {
				return array[i++];
			}

			@Override
			public void remove() {}
		};
	}
}
