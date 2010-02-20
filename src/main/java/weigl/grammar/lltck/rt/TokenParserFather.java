package weigl.grammar.lltck.rt;

import java.util.Arrays;


import static weigl.grammar.lltck.rt.DefaultAbstractSyntaxTree.*;
import weigl.grammar.lltck.rt.interfaces.AST;
import weigl.grammar.lltck.rt.interfaces.Leaf;
import weigl.grammar.lltck.rt.interfaces.Node;
import weigl.std.NoMoreElementsException;

public abstract class TokenParserFather<E extends TokenDefinition<E>>
	implements Parser<E> {
    protected AST<E> syntaxTree;
    private TokenDefinition<E> tokens[];
    protected Tokenizer<E> input;
    private Token<E> curtok;

    public TokenParserFather(TokenDefinition<E>... tokens) {
	this.setTokens(tokens);
    }

    /**
     * creates a new node with the given text
     * 
     * @param tok
     *            nodes contents
     * @return a node
     */
    public Leaf<E> newNode(Token<E> tok) {
	return new DefaultLeaf<E>(tok);
    }

    public Node<E> newNode(E tok) {
	return new DefaultNode<E>(new Token<E>(tok, "<rule>"));
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

    protected boolean lookahead(TokenDefinition<E> token) {
	if(curtok==null)
	    return (token.getType().toString().equalsIgnoreCase("EPSILON"));
	return curtok.getType() == token.getType();
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

    public Leaf<E> match(TokenDefinition<E> td) {
	if (td.getType().toString().equalsIgnoreCase("EPSILON")) {
	    return newNode(new Token<E>(td.getType(), "€"));
	} else {
	    if (lookahead(td)) {
		Token<E> tok = curpos();
		consume();
		return newNode(tok);
	    } else {
		error(td);
	    }
	}
	return null;
    }

    /**
     * start method. should only invoke the start symbol of the grammar
     */
    public abstract Node<E> start();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void run(String source);

    public void reset() {
	syntaxTree = null;
    }

    /** {@inheritDoc} */
    @Override
    public AST<E> getParseTree() {
	return syntaxTree;
    }

    public void setTokens(TokenDefinition<E> tokens[]) {
	this.tokens = tokens;
    }

    public TokenDefinition<E>[] getTokens() {
	return tokens;
    };
}
