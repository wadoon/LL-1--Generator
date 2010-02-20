package weigl.grammar.lltck.rt.interfaces;

import java.util.List;
import weigl.grammar.lltck.rt.TokenDefinition;

public interface Node<E extends TokenDefinition<E>> extends Leaf<E>
{
    public abstract void add(Leaf<E> n);
    public abstract List<Leaf<E>> getElements();
    public abstract Leaf<E> get(int i);
    public abstract int size();
}