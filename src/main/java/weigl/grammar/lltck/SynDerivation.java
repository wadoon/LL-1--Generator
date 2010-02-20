package weigl.grammar.lltck;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import weigl.std.collection.Array;

/**
 * class represent a derivation of an rule
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 */
public class SynDerivation
{
    public final List<String> tokenList;
    public Set<String>        firstTokens = new TreeSet<String>();

    public SynDerivation(Array<String> tokenList)
    {
        this.tokenList = tokenList.toList();
    }

    /**
     * @return the name of the leading token
     */
    public Set<String> getFirstTokens()
    {
        return firstTokens;
    }

    /**
     * @return the list of all tokens, in order
     */
    public List<String> getTokenList()
    {
        return tokenList;
    }
    
    public boolean isEpsilon()
    {
	return tokenList.contains(GrammarAlgorithms.EPSILON);
    }
}