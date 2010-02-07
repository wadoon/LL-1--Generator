package weigl.grammar.lltck.rt.interfaces;

import weigl.grammar.lltck.rt.Token;
import weigl.grammar.lltck.rt.TokenDefinition;

public interface Leaf<E extends TokenDefinition<E>>
{
    public abstract Token<E> getTerminalSymbol();

    public abstract void setTerminalSymbol(Token<E> terminalSymbol);

    public abstract boolean hasChildren();
}