package weigl.grammar;

import java.util.Set;

import weigl.grammar.Token.TerminalSymbol;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

/**
 * Calculation of the first sets for (non-)terminals.<br>
 * There are two steps: <br>
 * (1) all terminals and non-terminals were calculated. <br>
 * (2) all words of (non-terminals and terminals) power * were calculated <br>
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 * 
 */
public class FirstSetCalculator {
	private TreeMultimap<Token, Character> firstSetTsNT = TreeMultimap.create();
	private Multimap<String, Replacement> productionRules;
	private TreeMultimap<Replacement, Character> firstSetsProd = TreeMultimap
			.create();

	public FirstSetCalculator(Multimap<String, Replacement> pr) {
		productionRules = pr;
		boolean changed = true;
		while (changed) {
			for (Replacement r : productionRules.values()) {
				for (int i = 0; i < r.getTokens().length; i++) {
					changed = firstFor(r.getTokens()[i]);
				}
			}
		}
		buildProduction();
	}

	private void buildProduction() {
		boolean epslion = false;
		for (Replacement r : productionRules.values()) {
			for (Token tok : r.getTokens()) {
				Set<Character> firstSet = firstSetTsNT.get(tok);
				epslion = firstSet.contains(Token.EPSILON_CHAR);

				for (Character character : firstSet) {
					if (character.equals(Token.EPSILON_CHAR))
						continue;
					firstSetsProd.put(r, character);
				}

				if (!epslion)
					break;
			}
			if (epslion)
				firstSetsProd.put(r, Token.EPSILON_CHAR);
		}
	}

	private boolean firstFor(Token token) {
		if (token instanceof Token.TerminalSymbol) {
			return firstForTerminals(token);
		} else {
			return firstForNonTerminals(token);
		}
	}

	private boolean firstForTerminals(Token s) {
		return firstSetTsNT.put(s, s.TEXT.charAt(0));
	}

	private boolean firstForNonTerminals(Token s) {
		boolean b = false;
		if (existsEpsilonProd(s))
			b = firstSetTsNT.put(s, Token.EPSILON_CHAR);

		for (Replacement r : productionRules.get(s.TEXT)) {
			for (Token tok : r.getTokens()) {
				if (tok instanceof TerminalSymbol) {
					if (tok.equals(Token.EPSILON)) {
						b |= firstSetTsNT.put(s, Token.EPSILON_CHAR);
					} else {
						b |= firstSetTsNT.put(s, tok.TEXT.charAt(0));
						break;
					}
				} else {
					for (Character c : firstSetTsNT.get(tok))
						b |= firstSetTsNT.put(s, c);

					// Non-Terminal
					if (!existsEpsilonProd(tok))
						break;
				}
			}
		}
		return b;
	}

	private boolean existsEpsilonProd(Token s) {
		if (Token.EPSILON.equals(s))
			return true;
		for (Replacement r : productionRules.get(s.TEXT)) {
			if (r.getTokens()[0].equals(Token.EPSILON))
				return true;
		}
		return false;
	}

	public static TreeMultimap<Replacement, Character> invoke(
			Multimap<String, Replacement> productionRules) {
		FirstSetCalculator fsc = new FirstSetCalculator(productionRules);
		return fsc.firstSetsProd;
	}
}
