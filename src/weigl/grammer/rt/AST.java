package weigl.grammer.rt;

import java.util.LinkedList;
import java.util.List;

public class AST {
	private Node rootNode;

	public AST() {

	}

	public static class Node extends Leaf {
		private List<Leaf> childs;

		public Node(String symbol) {
			super(symbol);
			childs = new LinkedList<Leaf>();
		}

		public void add(Leaf n) {
			childs.add(n);
		}
	}

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

	}
}
