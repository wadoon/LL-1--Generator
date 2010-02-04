package weigl.grammar.lltck.rt;

import java.util.Arrays;

import weigl.grammar.rt.AST;
import weigl.grammar.rt.Parser;
import weigl.std.NoMoreElementsException;

public abstract class TokenParserFather<E> implements Parser {
	protected AST syntaxTree;
	private TokenDefinition<E> tokens[];
	private Tokenizer<E> input;
	private Token<E> curtok;

	public TokenParserFather(TokenDefinition<E>... tokens ) 
	{
		this.tokens = tokens;
	}
	
	/**
	 * creates a new node with the given text
	 * 
	 * @param text
	 *            nodes contents
	 * @return a node
	 */
	public AST.Node newNode(String text) {
		return new AST.Node(text);
	}

	/**
	 * method for error reporting
	 * 
	 * @throws IllegalStateException
	 */
	protected void error() throws IllegalStateException {
		throw new IllegalStateException("ERROR! expected: []");
	}

	/**
	 * method for error reporting
	 * 
	 * @param c
	 *            character that was expected
	 * @throws IllegalStateException
	 */
	protected void error(TokenDefinition<E> td) throws IllegalStateException {
		throw new IllegalStateException("ERROR! current:" + curpos()
				+ " expected: [" + td.getType() + "]\n" + errorPosition());
	}

	/**
	 * 
	 * @return
	 */
	protected String errorPosition() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	/**
	 * method for error reporting
	 * 
	 * @param c
	 *            characters that were expected
	 * @throws IllegalStateException
	 */
	protected void error(TokenDefinition<E>... c) throws IllegalStateException {
		throw new IllegalStateException("ERROR! CURRENT:" + curpos()
				+ " expected: " + Arrays.toString(c));
	}

	/**
	 * @return current character aka the lookahead
	 */
	protected Token<E> curpos() {
		return curtok;
	}

	/**
	 * lookahead for the end of the input.
	 * 
	 * @return true if end is reached else false
	 */
	protected boolean lookahead() {
		return curtok == null;
	}

	/**
	 * searches the lookahead after each char in the string. on match return
	 * true
	 * 
	 * @param s
	 *            chars for looking ahead
	 * @return true if one character in s matched the lookahead
	 */
	protected boolean lookahead(TokenDefinition<E>... token) {
		for (TokenDefinition<E> tokenDef : token) {
			if (curtok.getType() == tokenDef.getType()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * read one char forward
	 */
	protected void consume() {
		try {
			curtok = input.next();
		} catch (NoMoreElementsException e) {
			curtok = null;
		} catch (RecognitionException e) {
			System.err.println(e.getMessage());
			error();
		}
	}

	/**
	 * start method. should only invoke the start symbol of the grammar
	 */
	public abstract void start();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run(String source) {
		reset();
		input = new Tokenizer<E>(source, tokens);
		start();
	}

	private void reset() {
		syntaxTree = null;
	}

	/** {@inheritDoc} */
	@Override
	public AST getParseTree() {
		return syntaxTree;
	};
}
