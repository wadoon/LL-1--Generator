package weigl.grammer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.Map.Entry;

import weigl.grammer.Token.NonTerminalSymbol;
import weigl.grammer.Token.TerminalSymbol;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class ParserBuilder {

	private Multimap<String, Replacement> rules;
	private TreeMultimap<Replacement, Character> firstSets;

	private PrintWriter out;

	public ParserBuilder(Multimap<String, Replacement> productionRules,
			TreeMultimap<Replacement, Character> firstSets) {
		try {
			out = new PrintWriter("src/Parser.java");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		this.rules = productionRules;
		this.firstSets = firstSets;

		header();
		start();
		footer();
		out.flush();
	}

	private void header() {
		// out.println("package weigl.grammar.genparsers;\n\n"
		out.println("\n\n" + "import weigl.grammer.rt.*;\n\n"
				+ "class Parser extends ParserFather{\n\n");

	}

	private void start() {
		Map<String, Collection<Replacement>> m = rules.asMap();
		for (Entry<String, Collection<Replacement>> e : m.entrySet()) {
			if (isNonTerminal(e.getKey()))
				generateMethod(e);
		}
	}

	private void generateMethod(Entry<String, Collection<Replacement>> e) {
		if (e.getValue().size() == 0) {
			System.err.format("rule %s has no replacement.%n", e.getKey());
			return;
		}

		out.format("public AST.Node %s()%n{%n", e.getKey());
		out.format("final AST.Node n = newNode(\"%s\");%n", e.getKey());
		if (e.getValue().size() == 1)
			simpleMethodBody(e.getValue().iterator().next());// only one element
		else
			multipleMethodBody(e);

		out.println("return n;");
		out.println("}");
	}

	private void simpleMethodBody(Replacement r) {
		for (Token tok : r.getTokens()) {
			if (isTerminal(tok)) {
				matchingRules(tok);
			} else {
				out.format("n.add(%s());%n", tok.TEXT);
			}
		}
	}

	private void matchingRules(Token tok) {
		for (char c : tok.TEXT.toCharArray()) {
			out.format("n.add(match('%c'));%n", c);
		}
	}

	private void multipleMethodBody(Entry<String, Collection<Replacement>> e) {
		out.println("boolean matched = false;\n");
		for (Replacement r : e.getValue()) // for every alternativ
		{
			out.format("if ( lookahead(\"%s\") ) %n { %n",
					setToString(firstSets.get(r)));
			out.println("matched=true;");
			simpleMethodBody(r);
			out.println("\n}\n");
		}
		out.println("if(!matched) error();\n");
	}

	private void footer() {
		out.print("\n\n}//end class");
	}

	private String setToString(SortedSet<Character> sortedSet) {
		StringBuilder sb = new StringBuilder();
		for (Character c : sortedSet)
			sb.append(c);
		return sb.toString();
	}

	private boolean isTerminal(String tok) {
		return Character.isLowerCase(tok.charAt(0));
	}

	private boolean isNonTerminal(String tok) {
		return !isTerminal(tok);
	}

	private boolean isTerminal(Token tok) {
		return (tok instanceof TerminalSymbol);
	}

	private boolean isNonTerminal(Token tok) {
		return (tok instanceof NonTerminalSymbol);
	}

	public static void run(Multimap<String, Replacement> productionRules,
			TreeMultimap<Replacement, Character> firstSets) {
		ParserBuilder pb = new ParserBuilder(productionRules, firstSets);
	}
}
