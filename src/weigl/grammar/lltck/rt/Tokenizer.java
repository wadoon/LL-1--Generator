package weigl.grammar.lltck.rt;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import weigl.std.NoMoreElementsException;
import weigl.std.WIterator;

/**
 * Build an {@link WIterator} for tokens over the given {@link String}
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @param <E>
 *            the token type class
 */
public class Tokenizer<E extends TokenDefinition<E>> implements
		WIterator<Token<E>> {
	private List<TokenDefinition<E>> tokens;
	private String input;

	private MatchType matchType = MatchType.FIRST;

	/**
	 * @param input
	 *            the input string
	 * @param tokens
	 *            {@link TokenDefinition} to searching for
	 */
	public Tokenizer(String input, TokenDefinition<E>... tokens) {
		this.tokens = Arrays.asList(tokens);
		this.input = input;
	}

	/**
	 * @see #Tokenizer(String, TokenDefinition...)
	 * @param input
	 * @param mt
	 * @param tokens
	 */
	public Tokenizer(String input, MatchType mt, TokenDefinition<E>... tokens) {
		this(input, tokens);
		matchType = mt;
	}

	/**
	 * returns the next found token
	 */
	@Override
	public Token<E> next() throws NoMoreElementsException, RecognitionException {
		if (input.isEmpty())
			throw new NoMoreElementsException();
		Token<E> tok;
		if (matchType == MatchType.FIRST)
			tok = firstMatch();
		else
			tok = bestMatch();

		if (tok.getType().isHidden())
			return next();

		return tok;
	}

	public Token<E> bestMatch() {
		int best = 0;
		Token<E> matched = null;
		for (TokenDefinition<E> token : tokens) {
			if (token.isRule())
				continue;
			Matcher m = token.getPattern().matcher(input);
			if (m.lookingAt() && m.end() > best) {
				best = m.end();
				String matchedText = input.substring(0, m.end());
				matched = new Token<E>(token.getType(), matchedText);
			}
		}
		input = input.substring(best);
		return matched;
	}

	public Token<E> firstMatch() throws RecognitionException {
		for (TokenDefinition<E> token : tokens) {
			if (token.isRule())
				continue;
			Matcher m = token.getPattern().matcher(input);
			if (m.lookingAt()) {
				String matched = input.substring(0, m.end());
				input = input.substring(m.end());
				return new Token<E>(token.getType(), matched);
			}
		}
		throw new RecognitionException(
				"no token definition matched for rest string: '" + input + "'");
	}

	public static void main(String[] args) throws RecognitionException {
		Tokenizer<MathTokens> t = new Tokenizer<MathTokens>("2*2", MathTokens
				.values());

		try {
			while (true) {
				Token<MathTokens> f1 = t.next();
				Token<MathTokens> o = t.next();
				Token<MathTokens> f2 = t.next();

				int i = Integer.parseInt(f1.getValue()), j = Integer
						.parseInt(f2.getValue());

				switch (o.getType()) {
				case DIV:
					System.out.println(i / j);
					break;
				case MULTIPLY:
					System.out.println(i * j);
					break;
				case MINUS:
					System.out.println(i - j);
					break;
				case PLUS:
					System.out.println(i + j);
					break;
				default:
					System.err.println("ERROR: " + o.getType());
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NoMoreElementsException e) {
		}
	}
}