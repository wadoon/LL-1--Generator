package weigl.grammar.lltck.rt;

import java.util.ArrayList;


import java.util.LinkedList;

import java.util.List;

import javax.swing.tree.TreePath;

import weigl.grammar.lltck.rt.interfaces.*;

/**
 * Representation for an <b>Abstract Syntax Tree</b><br>
 * It holds the root node for the tree.
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 06.12.2009
 * @version 1
 * 
 */
public class DefaultAbstractSyntaxTree<E> implements AST<E> {
	Node<E> rootNode;

	public DefaultAbstractSyntaxTree(Node<E> node) {
		rootNode = node;
	}

	@Override
	public String toString() {
		return "AST: " + rootNode;
	}

	/**
	 * A leaf in an AST. it cannot references to any nodes. It holds only a
	 * matched Terminal-String.
	 * 
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 * @date 06.12.2009
	 * @version 1
	 * 
	 */
	public static class DefaultLeaf<E> implements Leaf<E> {
		private E terminalSymbol;

		public DefaultLeaf(E terminalSymbol) {
			this.terminalSymbol = terminalSymbol;
		}

		/* (non-Javadoc)
		 * @see weigl.grammar.lltck.rt.Leaf#getTerminalSymbol()
		 */
		public E getTerminalSymbol() {
			return terminalSymbol;
		}

		/* (non-Javadoc)
		 * @see weigl.grammar.lltck.rt.Leaf#setTerminalSymbol(E)
		 */
		public void setTerminalSymbol(E terminalSymbol) {
			this.terminalSymbol = terminalSymbol;
		}

		/* (non-Javadoc)
		 * @see weigl.grammar.lltck.rt.Leaf#toString()
		 */
		@Override
		public String toString() {
			return terminalSymbol.toString();
		}

		/* (non-Javadoc)
		 * @see weigl.grammar.lltck.rt.Leaf#hasChildren()
		 */
		public boolean hasChildren() {
			return false;
		}
	}

	/**
	 * 
	 * 
	 * 
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 * @date 06.12.2009
	 * @version 1
	 * 
	 */
	public static class DefaultNode<E> extends DefaultLeaf<E> implements Node<E> {
		private List<Leaf<E>> childs = new LinkedList<Leaf<E>>();
		public DefaultNode(E symbol) {
			super(symbol);

		}

		/* (non-Javadoc)
		 * @see weigl.grammar.lltck.rt.Node#add(weigl.grammar.lltck.rt.AST.Leaf)
		 */
		public void add(Leaf<E> n) {
			childs.add(n);
		}

		/* (non-Javadoc)
		 * @see weigl.grammar.lltck.rt.Node#toString()
		 */
		@Override
		public String toString() {
			return super.toString();
			// StringBuilder sb = new StringBuilder();
			// sb.append(super.toString());
			// for (Leaf l : childs)
			// sb.append('(').append(l).append(')');
			// return sb.toString();
		}

		/* (non-Javadoc)
		 * @see weigl.grammar.lltck.rt.Node#getElements()
		 */
		public List<Leaf<E>> getElements() {
			return childs;
		}

		/* (non-Javadoc)
		 * @see weigl.grammar.lltck.rt.Node#hasChildren()
		 */
		@Override
		public boolean hasChildren() {
			return true;
		}
	}

	public Node<E> getRoot() {
		return rootNode;
	}

	public String getLeafWord() {
		StringBuilder b = new StringBuilder();
		getLeafWord(rootNode, b);
		return b.toString().replace("€", "");
	}

	void getLeafWord(Node<E> n, StringBuilder sb) {
		for (Leaf<E> nd : n.getElements()) {
			if (nd.hasChildren())
				getLeafWord((Node<E>) nd, sb);
			else
				sb.append(nd.getTerminalSymbol());
		}
	}

	public TreePath deepestPath() {
		List<Leaf<E>> list = deepestPath(rootNode);
		Object[] o = new Object[list.size()];
		int i = 0;
		for (Leaf<E> l : list)
			o[i++] = l;
		return new TreePath(o);
	}

	List<Leaf<E>> deepestPath(Leaf<E> n) {
		List<Leaf<E>> t = new ArrayList<Leaf<E>>();

		if (n.hasChildren()) {

			Node<E> node = (Node<E>) n;
			List<Leaf<E>> childs = node.getElements();

			List<Leaf<E>> c = t;

			for (Leaf<E> leaf : childs) {
				List<Leaf<E>> l = deepestPath(leaf);
				if (l.size() > c.size())
					c = l;
			}
			t.addAll(c);
		} else
			t.add(n);
		return t;
	}
}
