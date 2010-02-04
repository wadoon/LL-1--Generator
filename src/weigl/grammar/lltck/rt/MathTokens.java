package weigl.grammar.lltck.rt;

import java.util.regex.Pattern;

/**
 * Example {@link TokenDefinition}s 
 * @author Alexander Weigl <alexweigl@gmail.com>
 */
public enum MathTokens implements TokenDefinition<MathTokens> {
	PLUS("\\+"), MINUS("-"), MULTIPLY("\\*"), DIV("/"),NUMBER("\\d+"), ID("[A-Za-z]+");

	private Pattern pattern;

	private MathTokens(String regex) {
		pattern = Pattern.compile(regex);
	}

	@Override
	public MathTokens getType() {
		return this;
	}

	@Override
	public Pattern getPattern() {
		return pattern;
	}
}
