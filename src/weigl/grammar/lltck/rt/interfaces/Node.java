package weigl.grammar.lltck.rt.interfaces;

import java.util.List;

public interface Node<E> extends Leaf<E> {
	public abstract void add(Leaf<E> n);
	public abstract List<Leaf<E>> getElements();
}