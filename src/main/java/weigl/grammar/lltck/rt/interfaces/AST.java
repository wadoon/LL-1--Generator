package weigl.grammar.lltck.rt.interfaces;

import javax.swing.tree.TreePath;

import weigl.grammar.lltck.rt.TokenDefinition;


public interface AST<E extends TokenDefinition<E>> {
	public Node<E> getRoot();
	public String getLeafWord();
	public TreePath deepestPath();
}