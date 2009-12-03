package weigl.grammer;

import java.util.Set;

import weigl.grammer.Token.TerminalSymbol;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

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
		for (Replacement r : productionRules.values()) {
			for (Token tok : r.getTokens()) {
				Set<Character> firstSet = firstSetTsNT.get(tok);
				boolean epslion = firstSet.remove(Token.EPSILON_CHAR);

				for (Character character : firstSet) {
					System.out.println(character);
					firstSetsProd.put(r, character);
				}

				if (!epslion)
					break;
			}
		}
		System.out.println(firstSetsProd);
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
