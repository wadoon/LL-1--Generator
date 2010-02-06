package weigl.grammar.lltck;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import weigl.std.Array;

public class SynDerivation {
	public final List<String> tokenList;
	public Set<String> firstTokens = new TreeSet<String>();

	public SynDerivation(Array<String> tokenList) {
		this.tokenList = tokenList.toList();
	}

	public Set<String> getFirstTokens() {
		return firstTokens;
	}


	public List<String> getTokenList() {
		return tokenList;
	}
}