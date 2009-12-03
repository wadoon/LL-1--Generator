package weigl.grammer;


import com.google.common.collect.*;

public class Parser {
	private Multimap<String, Replacement> productionRules = TreeMultimap
			.create();
	private TreeMultimap<Replacement, Character> firstSets;

	public Parser(String text) {
		parse(text);
		calculateFirstSets();
		ParserBuilder.run(productionRules, firstSets);
	}

	private void parse(String text) {
		for (String line : text.split("\n"))
			parseLine(line.trim());
	}

	private void calculateFirstSets() {
		firstSets = FirstSetCalculator.invoke(productionRules);
	}

	
	private void parseLine(String line) {
		int semiColon = line.indexOf(':');
		String name = line.substring(0, semiColon).toUpperCase();
		for (String replacement : line.substring(semiColon + 1).split("[|]")) {
			replacement = replacement.trim();
			Replacement value = new Replacement(replacement);
			productionRules.put(name, value);
			System.out.println(name + "-->" + value);
		}
	}
}
