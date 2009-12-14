package weigl.grammar;

import java.util.Map.Entry;

import com.google.common.collect.*;

/**
 * This class compiles the grammar rules given as a {@link String} into the
 * needed data structures. <br>
 * For calculation of the first sets see @see {@link FirstSetCalculator}.<br>
 * 
 * <h2>Syntax description:</h2>
 * <ol>
 * <li>every line is one rule
 * <li>non-terminals are written in upper-case
 * <li>terminals are written in lower-case
 * <li>the €-Symbol stands for epsilon
 * <li>the non-terminal is seperated with an : from the replacement rules
 * <li>the you can seperate several replacement rules wth |
 * </ol>
 * 
 * <pre>
 * name: r1 | r2 | r3 ...
 * 
 * <pre>
 * 
 * <b>example</b>
 * 
 * <pre>
 *  	A: aA | B
 *  	B: bB | €
 * </pre>
 * 
 * Produces words like this: a , aa , aaaaaabbbbbbb, b, € , ...
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 06.12.2009
 * @version 1
 * 
 */
public class ProductionGrammerParser {
	private Multimap<String, Replacement> productionRules = TreeMultimap
			.create();
	private TreeMultimap<Replacement, Character> firstSets;

	public ProductionGrammerParser(String text) throws GrammarParserException {
		parse(text);
		calculateFirstSets();
	}

	private void parse(String text) throws GrammarParserException {
		int lineno = 0;
		for (String line : text.split("\n")) {
			try {
				parseLine(line.trim());
			} catch (GrammarParserException e) {
				e.setLineno(lineno);
				throw e;
			}
		}
	}

	private void parseLine(String line) throws GrammarParserException {
		// get first semicolon
		int semiColon = line.indexOf(':');

		if (semiColon < 0)
			throw new GrammarParserException("no semicolon in line");

		// rip off the name
		String name = line.substring(0, semiColon).toUpperCase();

		
		if(name.isEmpty())
			throw new GrammarParserException("non-terminal name is empty.");
		
		// for each replacement (seperated with |
		for (String replacement : line.substring(semiColon + 1).split("[|]")) {
			replacement = replacement.trim();
			Replacement value = new Replacement(replacement);
			productionRules.put(name, value);
		}
	}

	private void calculateFirstSets() {
		firstSets = FirstSetCalculator.invoke(productionRules);
	}

	public Multimap<String, Replacement> getProductionRules() {
		return productionRules;
	}

	public void setProductionRules(Multimap<String, Replacement> productionRules) {
		this.productionRules = productionRules;
	}

	public TreeMultimap<Replacement, Character> getFirstSets() {
		return firstSets;
	}

	public void setFirstSets(TreeMultimap<Replacement, Character> firstSets) {
		this.firstSets = firstSets;
	}

	/**
	 * Generates the Javasources for the given rules.
	 * 
	 * @param grammarRules
	 * @return
	 * @throws GrammarParserException 
	 */
	public static String generateSource(String grammarRules) throws GrammarParserException {
		ProductionGrammerParser pgp = new ProductionGrammerParser(grammarRules);
		for (Entry<String, Replacement> e : pgp.productionRules.entries()) {
			System.out.println(e.getKey() + " -> " + e.getValue());
		}
		return ParserBuilder.run(pgp.productionRules, pgp.firstSets);
	}
}
