package weigl.grammar.lltck;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import weigl.std.StringUtils;

/**
 * Calculates the first for each rule.
 * 
 * <pre>
 * forall rules
 * 	forall derivation in rule
 * calculateFirstSet(derivation)
 * 
 * <pre>
 * First Set from a derivation is the leading token or 
 * the first set from the leading rule. if EPSILON is element in this first set, the next element will taken...
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 2010-02-05
 */
public class FirstSetTokenCalculator
{
    public static final String EPSILON = "€";
    private List<SynRule>      rules;

    /**
     * after this call, in each rules derivations object is the first set set.
     * 
     * @param list
     *            the rules
     */
    public FirstSetTokenCalculator(List<SynRule> list)
    {
        this.rules = list;
        for (SynRule e : list)
        {
            for (SynDerivation d : e.derivation)
            {
                Set<String> fs = calculateFirstSet(d.tokenList);
                d.firstTokens = fs;
                // firstSets.put(e.name,fs);
            }
        }
    }

    /**
     * calculate the first set for an derivation
     * 
     * @param value
     *            a derivation
     * @return the set of the names of first token
     */
    private Set<String> calculateFirstSet(List<String> value)
    {
        Set<String> firstTokens = new TreeSet<String>();
        for (String s : value)
        {
            if (StringUtils.isUpper(s))
            {
                for (SynDerivation e : get(s))
                    firstTokens.addAll(calculateFirstSet(e.tokenList));

                if (!firstTokens.contains(EPSILON)) return firstTokens;
            }
            else
            {
                if (s.equals(EPSILON)) continue;
                firstTokens.add(s);
                return firstTokens;
            }
        }
        firstTokens.add(EPSILON);
        return firstTokens;
    }

    /**
     * the the derivation for an specified rule name
     * 
     * @param searched
     *            -- name of a rule
     * @return possible derivations
     */
    private List<SynDerivation> get(String searched)
    {
        for (SynRule s : rules)
            if (s.name.equals(searched)) return s.derivation;
        return null;
    }
}