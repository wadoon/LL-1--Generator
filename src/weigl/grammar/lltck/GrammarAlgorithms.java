package weigl.grammar.lltck;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import weigl.std.StringUtils;

/**
 * <h2>Left recursion</h2> tries to find all <b>direct</b> left recursion in the
 * grammar <h2>First Sets</h2> Calculates the first for each rule.
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
public class GrammarAlgorithms
{
    public static final String EPSILON = "€";
    private List<SynRule>      rules;

    /**
     * after this call, in each rules derivations object is the first set set.
     * 
     * @param list
     *            the rules
     * @throws LeftRecursionException
     * @throws RuleUnknownException 
     */
    public GrammarAlgorithms(List<SynRule> list) throws LeftRecursionException, RuleUnknownException
    {
        this.rules = list;

        findLeftRecursion();

        for (SynRule e : list)
        {
            Set<String> rfs = new TreeSet<String>();
            for (SynDerivation d : e.derivation)
            {
                Set<String> fs = calculateFirstSet(d.tokenList);
                d.firstTokens = fs;
                rfs.addAll(fs);
            }
            e.setFirstSet(rfs);
        }
    }

    private void findLeftRecursion() throws LeftRecursionException
    {
        for (SynRule e : rules)
        {
            for (SynDerivation d : e.derivation)
            {
                String firstTok = d.tokenList.get(0);
                if (firstTok.equals(e.getName()))
                    throw new LeftRecursionException("Found left recursion at rule: " + e.getName());
            }
        }
    }

    /**
     * calculate the first set for an derivation
     * 
     * @param value
     *            a derivation
     * @return the set of the names of first token
     * @throws RuleUnknownException 
     */
    private Set<String> calculateFirstSet(List<String> value) throws RuleUnknownException
    {
        Set<String> firstTokens = new TreeSet<String>();

        for (String s : value)
        {
            if (StringUtils.isUpper(s))
            {
                SynRule synRule = get(s);
                
                //hey, w've already calculated that!
                if(synRule.getFirstSet()!=null) 
                    return synRule.getFirstSet();
                
                for (SynDerivation e : synRule.getDerivations())
                    firstTokens.addAll(calculateFirstSet(e.tokenList));

                if (!firstTokens.contains(EPSILON))
                    return firstTokens;
            }
            else
            {
                if (s.equals(EPSILON))
                    continue;
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
     * @throws RuleUnknownException 
     */
    private SynRule get(String searched) throws RuleUnknownException
    {
        for (SynRule s : rules)
            if (s.name.equals(searched))
                return s;
        throw new RuleUnknownException(String.format("the rule %s ist unknown ", searched));
    }

    public static void check(List<SynRule> rules) throws LeftRecursionException, RuleUnknownException
    {
        new GrammarAlgorithms(rules);
    }
}