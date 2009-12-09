package weigl.grammar.rt;

import java.util.LinkedList;
import java.util.List;

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
	 * A leaf in an AST. it cannot references to any nodes. It holds only a matched
	 * Terminal-String.
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
	}

	/**
	 * 
	 *
	 *
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 * @date   06.12.2009
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
//			StringBuilder sb = new StringBuilder();
//			sb.append(super.toString());
//			for (Leaf l : childs)
//				sb.append('(').append(l).append(')');
//			return sb.toString();
		}

		public List<Leaf> getElements() {
			return childs;
		}

	}

	public Node getRoot() {
		return rootNode;
	}
}
