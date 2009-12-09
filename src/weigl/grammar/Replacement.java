package weigl.grammar;

import java.util.Arrays;

/**
 * Represents one {@link Replacement} for a non-terminal symbol. I contains of
 * array of {@link Token}s
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 * 
 */
public class Replacement implements Comparable<Replacement> {
	private Token[] tokens;

	/**
	 * build a replacement from the given string. Each transition between a
	 * upper/lower and vice versa determines a split point.
	 * 
	 * @param line
	 */
	public Replacement(String line) {
		String[] split = line
				.split("((?<=[a-zA-Z])(?=[A-Z]))|((?<=[A-Z])(?=[a-z]))");
		tokens = new Token[split.length];
		for (int i = 0; i < tokens.length; i++) {
			if (Character.isUpperCase(split[i].charAt(0)))
				tokens[i] = new Token.NonTerminalSymbol(split[i]);
			else
				tokens[i] = new Token.TerminalSymbol(split[i]);
		}
	}

	/**
	 * only implemented 
	 */
	@Override
	public int compareTo(Replacement o) {
		if(o.tokens.length!= tokens.length)
			return tokens.length-o.tokens.length;
		
		for (int i = 0; i < tokens.length; i++) {
			int j = tokens[i].compareTo(o.tokens[i]);
			if (j != 0)
				return j;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Replacement [tokens=" + Arrays.toString(tokens) + "]";
	}

	public Token[] getTokens() {
		return tokens;
	}

	public boolean hasEpsilon() {
		for (Token token : tokens)
			if (token.equals(Token.EPSILON))
				return true;
		return false;
	}

	public boolean isEpsilon() {
		return tokens[0].equals(Token.EPSILON);
	}
}
