package weigl.grammer;

public abstract class Token implements Comparable<Token> {
	public static final TerminalSymbol EPSILON = new TerminalSymbol(Token.EPSILON_CHAR);

	public final String TEXT;

	static final char EPSILON_CHAR = '€';

	public Token(String str) {
		TEXT = str;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " '" + TEXT + "'";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((TEXT == null) ? 0 : TEXT.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (TEXT == null) {
			if (other.TEXT != null)
				return false;
		} else if (!TEXT.equals(other.TEXT))
			return false;
		return true;
	}

	public int compareTo(Token t) {
		if(equals(t))return 0;
		return TEXT.compareTo(t.TEXT);
	}

	public static class NonTerminalSymbol extends Token {

		public NonTerminalSymbol(String str) {
			super(str);

		}
	}

	public static class TerminalSymbol extends Token {

		public TerminalSymbol(String str) {
			super(str);
		}
		
		public TerminalSymbol(char c)
		{
			super(new String(new char[] {c}));
		}
	}
}
