package weigl.grammar.lltck;

public class SynToken {
	public final String name;
	public final String regex;

	public SynToken(String name, String regex) {
		this.name = name;
		this.regex = regex;
	}

	public String getName() {
		return name;
	}

	public String getRegex() {
		return regex;
	}

}
