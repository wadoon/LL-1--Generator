package weigl.grammar.lltck;

import weigl.grammar.gui.TestDialog;
import weigl.grammar.rt.*;
import weigl.grammar.rt.AST.Leaf;
import weigl.grammar.rt.AST.Node;

public class TokenGrammarParser extends ParserFather {
	private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWERCASE = UPPERCASE.toLowerCase();
	private static final String ANYCASE = UPPERCASE + LOWERCASE
			+ "\\+*-/[]{}: ";

	public void start() {
		syntaxTree = new AST(file());
	}

	public AST.Node anything() {
		final AST.Node n = newNode("ANYCASE");
		while (lookahead(ANYCASE)) {
			n.add(match(curpos()));
		}
		n.add(new AST.Leaf("€"));
		return n;
	}

	public AST.Node ruleList_() {
		final AST.Node n = newNode("RULELIST_");
		if (lookahead("|")) {
			match('|');
			n.add(ruleList());
		} else {
			n.add(new Leaf("€"));
		}
		return n;
	}

	public AST.Node ruleList() {
		final AST.Node n = newNode("RULELIST");
		n.add(ruleElements());
		n.add(ruleList_());
		return n;
	}

	/**
	 * "abc" "abc" "abc"
	 * 
	 * E: QF|UF F: ' 'E|€
	 */
	private Leaf ruleElements_() {
		final AST.Node n = newNode("ELEMENTS_");
		if (lookahead())
			n.add(new AST.Leaf("€"));
		else if (lookahead(" "))
			n.add(ruleElements());
		return n;
	}

	private Leaf ruleElements() {
		final AST.Node n = newNode("ELEMENTS");
		ignore(' ');
		if (lookahead("\"")) {
			Node sub = new Node("QUOTE");
			sub.add(match('"'));
			sub.add(anything());
			sub.add(match('"'));
			n.add(sub);
		} else if (lookahead(ANYCASE)) {
			n.add(anything());
		}
		n.add(ruleElements_());
		return n;
	}

	public AST.Node lowerCase() {
		final AST.Node n = newNode("LOWERCASE");
		while (lookahead(LOWERCASE)) {
			n.add(match(curpos()));
		}
		n.add(new AST.Leaf("€"));
		return n;
	}

	public AST.Node delimiter() {
		final AST.Node n = newNode("NEWLINE");
		n.add(match('\n'));
		return n;
	}

	public AST.Node rule() {
		final AST.Node n = newNode("RULE");
		n.add(upperCase());
		ignore(' ');
		n.add(match(':'));
		ignore(' ');
		n.add(ruleList());
		n.add(delimiter());
		return n;
	}

	public AST.Node file() {
		final AST.Node n = newNode("FILE");

		boolean matched = false;
		if (lookahead("€")) {
			matched = true;
			n.add(match('€'));
		} else if (lookahead(UPPERCASE)) {
			matched = true;
			n.add(rule());
			n.add(file());
		} else if (lookahead(LOWERCASE)) {
			matched = true;
			n.add(token());
			n.add(file());
		}
		if (!matched)
			error((LOWERCASE + UPPERCASE).toCharArray());
		return n;
	}

	public AST.Node token() {
		final AST.Node n = newNode("TOKEN");
		n.add(lowerCase());
		ignore(' ');
		n.add(match('='));
		ignore(' ');
		n.add(anything());
		n.add(delimiter());
		return n;
	}

	public AST.Node upperCase() {
		final AST.Node n = newNode("UPPERCASE");
		while (lookahead(UPPERCASE)) {
			n.add(match(curpos()));
		}
		n.add(new AST.Leaf("€"));
		return n;
	}

	private void ignore(char... c) {
		for (char d : c) {
			if (curpos() == d) {
				consume();
				ignore(c);
			}
		}
	}

	public static void main(String[] args) {
		String s = "string = \\w+\n"
				+ "START: \"user: \" string | \"password: \" string\n";
		System.out.println(s);
		Parser p = new TokenGrammarParser();
		p.run(s);
		AST tree = p.getParseTree();
		tree = new AST((Node) SyntaxTree.compress(tree.getRoot()));
		TestDialog.showFrame(tree);
	}
}
