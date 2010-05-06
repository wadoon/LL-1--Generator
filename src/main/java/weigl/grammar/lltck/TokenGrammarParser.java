package weigl.grammar.lltck;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import weigl.std.StringUtils;
import weigl.std.collection.Array;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

/**
 * LL(1) for the token parser definition grammar.<br>
 * <br>
 * <h2>Syntax definition<h2>
 * 
 * <pre>
 *          +------------+   +----------+
 * file: ---| tokenlist  |---| ruleList |---
 *          +------------+   +----------+
 *          
 *            +-----------------------+ 
 *            ↓ +-------------------+ | 
 * tokenList ---| tokendef          |----
 *             |+-------------------+   |
 *             +------------------------+
 *          
 *            +-----------------------+         +---------------------+
 *            ↓ +-------------------+ ↓ +-----+ ↓ +-----------------+ |  +----+
 * tokendef  ---| LOWER CASE DIGIT  |---| '=' |---| ANY CASE DIGIT  |--- | \n |
 *              +-------------------+   +-----+   +-----------------+    +----+
 * 
 *            +-----------------------+ 
 *            ↓ +-------------------+ | 
 * ruleList  ---| ruledef           |----
 *             |+-------------------+   |
 *             +------------------------+
 *          
 *            +-----------------------+         +---------------------+
 *            ↓ +-------------------+ ↓ +-----+ ↓ +-----------------+ |  +----+
 * ruledef   ---| UPPER CASE DIGIT  |---| ':' |---| ANY CASE DIGIT  |--- | \n |
 *              +-------------------+   +-----+   +-----------------+    +----+
 * </pre>
 * 
 * in ruledef you can use the pipe char ("|") for seperating derivations rules.
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 2010-01-30
 */
public class TokenGrammarParser {
	public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LOWERCASE = "$" + UPPERCASE.toLowerCase();
	public static final String ANYCASE = UPPERCASE + LOWERCASE + "\\+*-/[]{}: ";

	private Map<String, String> tokenList = new TreeMap<String, String>();
	private Multimap<String, Array<String>> ruleList = TreeMultimap.create();
	private String input;

	public TokenGrammarParser(String source) {
		input = source;
		parse();
	}

	private void parse() {
		for (String line : input.split("\\n")) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#"))
				continue;
			final char first = line.charAt(0);
			if (LOWERCASE.indexOf(first) >= 0)
				parseToken(line);
			else
				parseRule(line);
		}
	}

	private void parseToken(String line) {
		String[] a = line.split("=", 2);
		String name = a[0].trim();
		String regex = a[1].trim();
		addToken(name, regex);
	}

	private void parseRule(String line) {
		String[] a = line.split(":", 2);
		String name = a[0].trim();
		String derivatons = a[1].trim();

		for (String derivation : derivatons.split("(?<=[^\\\\])\\|"))
			addDerivation(name, derivation.trim());
	}

	private void addDerivation(String name, String derivation) {
	    	String toks[] = StringUtils.split(derivation, " \t\n", '"', '"');
		
		for (int i = 0; i < toks.length; i++) {
			toks[i] = toks[i].trim();

			if (toks[i].charAt(0) == '"') {
				toks[i] = quotedToToken(toks[i]);
			}

		}
		ruleList.put(name, Array.create(toks));
	}

	private String quotedToToken(String quotedText) {
		String tokenName = createTokenName(quotedText);
		tokenList.put(tokenName, escapeRegex(quotedText));
		return tokenName;
	}

	private String createTokenName(String name) {
		do {
			name = "_" + name.replaceAll("[^a-zA-Z_]", "");
			// till we found a name that does not exists
			// or the name and the regex matches!
		} while (tokenList.containsKey(name)
				&& !!tokenList.get(name).equals(name));
		return name;
	}

	private String escapeRegex(String pattern) {
		// '-2' for removing leading and trailing parens
		return Pattern.quote(pattern.substring(1, pattern.length() - 1));
	}

	private void addToken(String name, String pattern) {
		tokenList.put(name, pattern);
	}

	/**
	 * @return the generated list of token definition
	 */
	public List<SynToken> getTokens() {
		List<SynToken> l = new LinkedList<SynToken>();
		for (Entry<String, String> e : tokenList.entrySet())
			l.add(new SynToken(e.getKey(), e.getValue(),
					e.getKey().charAt(0) == '$'));
		return l;
	}

	/**
	 * @return the generated list of rule definitions
	 */
	public List<SynRule> getRules() {
		List<SynRule> rules = new LinkedList<SynRule>();
		for (Entry<String, Collection<Array<String>>> e : ruleList.asMap()
				.entrySet()) {
			SynRule sr = new SynRule(e.getKey());
			StringBuilder doc = new StringBuilder();
			for (Array<String> a : e.getValue()) {
				sr.add(a);
				doc.append(sr.getName()).append(" -> ").append(a).append('\n');
			}
			sr.setDoc(doc.toString());
			rules.add(sr);
		}
		return rules;

	}

	public static void main(String[] args) {
		String s = "string = \\w+\n" + "$whitespaces = \\s\n"
				+ "START: \"user: \" string | \"password: \" string\n";

		TokenGrammarParser p = new TokenGrammarParser(s);
		p.parse();
		for (SynToken t : p.getTokens())
			System.out.format("%20s = %-20s%n", t.getName(), t.getRegex());
		System.out.println("-----------------------------------------------");
		for (SynRule t : p.getRules()) {
			System.out.format("%20s%n", t.getName());
			for (SynDerivation d : t.getDerivations()) {
				System.out.format("\t\t%-20s%n", d.getTokenList().toString());
			}
		}
	}

	public String getClassName() {
		return null;
	}
}
