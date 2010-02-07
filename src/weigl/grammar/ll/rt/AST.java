package weigl.grammar.ll.rt;

import java.util.ArrayList;

import java.util.LinkedList;

import java.util.List;

import javax.swing.tree.TreePath;

/**
 * Representation for an <b>Abstract Syntax Tree</b><br>
 * It holds the root node for the tree.
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 06.12.2009
 * @version 1
 * 
 */
public class AST {
	private Node rootNode;

	public AST(Node node) {
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
	public static class Leaf {
		private String terminalSymbol;

		public Leaf(String terminalSymbol) {
			this.terminalSymbol = terminalSymbol;
		}

		public String getTerminalSymbol() {
			return terminalSymbol;
		}

		public void setTerminalSymbol(String terminalSymbol) {
			this.terminalSymbol = terminalSymbol;
		}

		@Override
		public String toString() {
			return terminalSymbol;
		}

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
	public static class Node extends Leaf {
		private List<Leaf> childs;

		public Node(String symbol) {
			super(symbol);
			childs = new LinkedList<Leaf>();
		}

		public void add(Leaf n) {
			childs.add(n);
		}

		@Override
		public String toString() {
			return super.toString();
			// StringBuilder sb = new StringBuilder();
			// sb.append(super.toString());
			// for (Leaf l : childs)
			// sb.append('(').append(l).append(')');
			// return sb.toString();
		}

		public List<Leaf> getElements() {
			return childs;
		}

		@Override
		public boolean hasChildren() {
			return true;
		}
	}

	public Node getRoot() {
		return rootNode;
	}

	public String getLeafWord() {
		StringBuilder b = new StringBuilder();
		getLeafWord(rootNode, b);
		return b.toString().replace("€", "");
	}

	private void getLeafWord(Node n, StringBuilder sb) {
		for (Leaf nd : n.childs) {
			if (nd.hasChildren())
				getLeafWord((Node) nd, sb);
			else
				sb.append(nd.getTerminalSymbol());
		}
	}

	public TreePath deepestPath() {
		List<Leaf> list = deepestPath(rootNode);
		Object[] o = new Object[list.size()];
		int i = 0;
		for (Leaf l : list) 
			o[i++]=l;
		return new TreePath(o);
	}

	List<Leaf> deepestPath(Leaf n) {
		List<Leaf> t = new ArrayList<Leaf>();

		if (n.hasChildren()) {

			Node node = (Node) n;
			List<Leaf> childs = node.childs;

			List<Leaf> c = t; 

			for (Leaf leaf : childs) {
				List<Leaf> l = deepestPath(leaf);
				if (l.size() > c.size())
					c = l;
			}
			t.addAll(c);
		} else
			t.add(n);
		return t;
	}
}
