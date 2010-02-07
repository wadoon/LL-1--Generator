package weigl.grammar.lltck.rt;

/**
 * Describes a found token with the matched string and token type
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @param <E>
 *            the token enum class
 */
public class Token<E extends TokenDefinition<E>> {
	private final E type;
	private final int position;
	private Object value;

	public Token(E name, String str) {
		this(name, str, -1);
	}

	public Token(E name, String value, int pos) {
		this.type = name;
		this.value = value;
		this.position = pos;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getPosition() {
		return position;
	}

	public E getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Token [name=" + type + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
