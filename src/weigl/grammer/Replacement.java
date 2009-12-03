package weigl.grammer;

import java.util.Arrays;

public class Replacement implements Comparable<Replacement> {
	private Token[] tokens;

	public Replacement(String line) {
		String[] split = line
				.split("((?<=[a-z])(?=[A-Z]))|((?<=[A-Z])(?=[a-z]))");
		tokens = new Token[split.length];
		for (int i = 0; i < tokens.length; i++) {
			if (Character.isUpperCase(split[i].charAt(0)))
				tokens[i] = new Token.NonTerminalSymbol(split[i]);
			else
				tokens[i] = new Token.TerminalSymbol(split[i]);
		}
	}

	@Override
	public int compareTo(Replacement o) {
		return tokens.length - o.tokens.length;
	}

	@Override
	public String toString() {
		return "Replacement [tokens=" + Arrays.toString(tokens) + "]";
	}

	public Token[] getTokens() {
		return tokens;
	}
}
