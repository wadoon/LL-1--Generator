package weigl.grammar.lltck.rt.interfaces;

import javax.swing.tree.TreePath;


public interface AST<E> {
	public Node<E> getRoot();
	public String getLeafWord();
	public TreePath deepestPath();
}