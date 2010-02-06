package weigl.grammar.lltck.rt.interfaces;

public interface Leaf<E> {
	public abstract E getTerminalSymbol();
	public abstract void setTerminalSymbol(E terminalSymbol);
	public abstract boolean hasChildren();
}