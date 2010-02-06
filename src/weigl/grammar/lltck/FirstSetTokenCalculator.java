package weigl.grammar.lltck;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import weigl.std.Array;
import weigl.std.StringUtils;

import com.google.common.collect.Maps;
/**
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 *
 */
public class FirstSetTokenCalculator {
	public static final String EPSILON = "€";
	private List<SynRule> rules;
	private Map<Array<String>, Set<String>> firstSets;

	/**
	 * 
	 * @param list
	 */
	public FirstSetTokenCalculator(List<SynRule> list) {
		this.firstSets = Maps.newHashMap();
		this.rules = list;
		for (SynRule e : list) 
		{
			for (SynDerivation d: e.derivation) {
				Set<String> fs = calculateFirstSet(d.tokenList);
				d.firstTokens=fs;
//				firstSets.put(e.name,fs);
			}
		}
	}

	private Set<String> calculateFirstSet(List<String> value) {
		Set<String> firstTokens = new TreeSet<String>();
		for (String s : value) {
			if (StringUtils.isUpper(s)) {
				for (SynDerivation e : get(s))
					firstTokens.addAll(calculateFirstSet(e.tokenList));

				if (!firstTokens.contains(EPSILON))
					return firstTokens;
			} else {
				if (s.equals(EPSILON))
					continue;
				firstTokens.add(s);
				return firstTokens;
			}
		}
		firstTokens.add(EPSILON);
		return firstTokens;
	}

	private List<SynDerivation> get(String searched) {
		for (SynRule s: rules) 
			if(s.name.equals(searched))
				return s.derivation;
		return null;
	}

	public Map<Array<String>, Set<String>> getFirstSets() {
		return firstSets;
	}
}