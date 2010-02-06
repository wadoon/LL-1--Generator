package weigl.grammar.lltck;

import java.util.LinkedList;
import java.util.List;

import weigl.std.Array;

public class SynRule {
	public final String name;
	public final List<SynDerivation> derivation = new LinkedList<SynDerivation>();

	public SynRule(String name) {
		this.name = name;
	}

	public void add(Array<String> derivation) {
		this.derivation.add(new SynDerivation(derivation));
	}

	public String getName() {
		return name;
	}

	public List<SynDerivation> getDerivations() {
		return derivation;
	}
	
	public boolean isEpsilon()
	{
		for (SynDerivation d : derivation) 
		{
			if( d.firstTokens.contains(FirstSetTokenCalculator.EPSILON) )
				return true;
		}
		return false;
	}
}
