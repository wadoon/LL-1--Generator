package weigl.grammer.rt;

import java.lang.reflect.InvocationTargetException;

import weigl.grammer.rt.AST.Leaf;

public abstract class ParserFather implements Parser {
	private char[] input;
	private AST syntaxTree = new AST();
	private int position = 0;


	public ParserFather() {
	}

	public AST.Node newNode(String text) {
		return new AST.Node(text);
	}

	public Leaf match(char c) {
		if (c == curpos()) {
			consume();
			return new AST.Leaf(new String(new char[] { c }));
		}
		error();
		return null;
	}

	protected void error() throws IllegalStateException {
		throw new IllegalStateException("ERROR!");
	}

	protected char curpos() {
		return input[position];
	}

	protected boolean lookahead() {
		return position >= input.length;
	}

	protected boolean lookahead(String s) {
		for (char c : s.toCharArray())
			if (c == curpos())
				return true;
		return false;
	}

	protected void consume() {
		position++;
	}

	@Override
	@SuppressWarnings("all")
	public void run(String source) throws IllegalArgumentException,
			SecurityException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		input = source.toCharArray();
		start();
	}

	
	
	public abstract void start();

	@Override
	public AST getParseTree() {
		return syntaxTree;
	};
}
