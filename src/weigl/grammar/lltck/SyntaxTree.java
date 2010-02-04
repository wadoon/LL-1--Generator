package weigl.grammar.lltck;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import weigl.grammar.rt.AST;
import weigl.grammar.rt.AST.Leaf;
import weigl.grammar.rt.AST.Node;
import weigl.std.Array;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

/**
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 *
 */
public class SyntaxTree {
	private Map<String, String> tokenList = new TreeMap<String, String>();
	private Multimap<String, Array<String>> ruleList = TreeMultimap.create();

	public SyntaxTree(AST tree) {
		Node root = (Node) compress(tree.getRoot());

		for (Leaf n : root.getElements()) {
			if (n.getTerminalSymbol().equals("TOKEN")) {
				addToken(n);
			} else if (n.getTerminalSymbol().equals("RULE")) {
				addRule(n);
			}
		}
	}

	private void addRule(Leaf n) {
		Node m = (Node) n;

		String name = m.getElements().get(0).getTerminalSymbol();
		List<Leaf> rulelist = ((Node) m.getElements().get(2)).getElements();

		for (Leaf l : rulelist) {
			ruleList.put(name, parseElement((Node) l));
		}
	}

	private Array<String> parseElement(Node l) {
		String[] tokenList = new String[l.getElements().size()];
		int i = 0;
		for (Leaf n : l.getElements()) {
			String tokenName;
			if (n.hasChildren())// quoted Text
			{
				tokenName = createToken(((Node) n).getElements().get(0)
						.getTerminalSymbol());
			} else {
				tokenName = n.getTerminalSymbol();
			}
			tokenList[i++] = tokenName;
		}
		return new Array<String>(tokenList);
	}

	private String createToken(String string) {
		String tokenName = createTokenName(string, string);
		tokenList.put(tokenName, escapeRegex(string));
		return tokenName;
	}

	private String createTokenName(String name, String regex) {
		do {
			name = "_" + name.replaceAll("[^a-zA-Z_]", "");
			// till we found a name that does not exits
			// or the name and the regex matches!
		} while (tokenList.containsKey(name)
				&& !!tokenList.get(name).equals(regex));
		return name;
	}

	private String escapeRegex(String pattern) {
		return Pattern.quote(pattern.substring(1, pattern.length() - 2));
	}

	private void addToken(Leaf n) {
		Node m = (Node) n;
		String name = m.getElements().get(0).getTerminalSymbol();
		String pattern = m.getElements().get(2).getTerminalSymbol();
		tokenList.put(name, pattern);
	}

	public static Leaf compress(Node node) {
		Leaf n = compressTree(node);
		compressLeaf((Node) n);
		return n;
	}

	public static Leaf compressLeaf(Node node) {
		List<Leaf> elements = node.getElements();
		List<Leaf> copy = new LinkedList<Leaf>(elements);
		for (Leaf child : copy)
			if (child.hasChildren()) {
				Leaf n = compressLeaf((Node) child);
				if (n != null)
					elements.set(elements.indexOf(child), n);
			}

		if (node.getTerminalSymbol().equals("LOWERCASE")
				|| node.getTerminalSymbol().equals("ANYCASE")
				|| node.getTerminalSymbol().equals("QUOTE")
				|| node.getTerminalSymbol().equals("UPPERCASE")) {
			StringBuilder sb = new StringBuilder();
			for (Leaf leaf : elements)
				if (!leaf.hasChildren())
					sb.append(leaf.getTerminalSymbol());
			elements.clear();
			Leaf l = new Leaf(sb.toString().replace("€", ""));
			if (!node.getTerminalSymbol().equals("QUOTE"))
				return l;
			else {
				Node n = new Node("QUOTE");
				n.add(l);
				return n;
			}
		}
		return node;
	}

	public static AST.Leaf compressTree(AST.Node node) {
		List<Leaf> old = node.getElements();
		List<Leaf> list = new LinkedList<Leaf>(old);

		for (Leaf l : list) {
			if (l.hasChildren()) {
				Leaf child = compressTree((AST.Node) l);
				if (child == null)
					old.remove(l);
				else
					old.set(old.indexOf(l), child);
			}
		}

		if (old.size() >= 2) {
			return node;
		} else {
			if (old.size() == 0)
				return null;
			Leaf child = old.get(0);
			return child.getTerminalSymbol().equals("€")
					|| child.getTerminalSymbol().equals("NEWLINE")
					|| child.getTerminalSymbol().equals("\n") ? null : child;
		}
	}

	public List<SynToken> getTokens() {
		List<SynToken> l = new LinkedList<SynToken>();
		for (Entry<String, String> e : tokenList.entrySet())
			l.add(new SynToken(e.getKey(), e.getValue()));
		return l;
	}

	public List<SynRule> getRules() {
		List<SynRule> rules = new LinkedList<SynRule>();
		for (Entry<String, Collection<Array<String>>> e : ruleList.asMap()
				.entrySet()) {
			SynRule sr = new SynRule(e.getKey());
			for (Array<String> a : e.getValue())
				sr.add(a);
			rules.add(sr);
		}
		return rules;
	}

}

class SynToken {
	public final String name;
	public final String regex;

	public SynToken(String name, String regex) {
		this.name = name;
		this.regex = regex;
	}

	public String getName() {
		return name;
	}

	public String getRegex() {
		return regex;
	}

}

class SynRule {
	public final String name;
	public final List<SynDerivation> derivation = new LinkedList<SynDerivation>();

	public SynRule(String name) {
		this.name = name;
	}

	public void add(Array<String> derivation) {
		this.derivation.add(new SynDerivation(derivation));
	}

	public String getName() {
		return name;
	}

	public List<SynDerivation> getDerivation() {
		return derivation;
	}
}

class SynDerivation {
	public final Array<String> tokenList;
	public Set<String> firstTokens = new TreeSet<String>();

	public SynDerivation(Array<String> tokenList) {
		this.tokenList = tokenList;
	}

	public Set<String> getFirstTokens() {
		return firstTokens;
	}


	public Array<String> getTokenList() {
		return tokenList;
	}
}