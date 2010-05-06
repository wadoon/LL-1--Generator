package weigl.grammar.lltck;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import weigl.std.collection.Array;

/**
 * Representation of an production rule
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 */
public class SynRule
{
    public final String              name;
    public String                    doc;
    public final List<SynDerivation> derivation = new LinkedList<SynDerivation>();
    private Set<String>              firstSet;

    /**
     * creates an new production rule with an empty documentation
     * 
     * @param name
     */
    public SynRule(String name)
    {
        this.name = name;
        this.doc = "";
    }

    /**
     * add a derivation
     * 
     * @param derivation
     *            tokens of the derivation
     */
    public void add(Array<String> derivation)
    {
        this.derivation.add(new SynDerivation(derivation));
    }

    /**
     * @return the java documentation for this rule/method
     */
    public String getDoc()
    {
        return doc;
    }

    /**
     * sets the javadoc for this rule/method
     * 
     * @param doc
     *            -- javadoc
     */
    public void setDoc(String doc)
    {
        this.doc = doc;
    }

    /**
     * @return the name of the rule
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the list of derivation
     */
    public List<SynDerivation> getDerivations()
    {
        return derivation;
    }

    /**
     * @return true -- if you can derive this production rule to epsilon,
     *         !directly!
     */
    public boolean isEpsilon()
    {
        for (SynDerivation d : derivation)
        {
            if (d.firstTokens.contains(GrammarAlgorithms.EPSILON))
                return true;
        }
        return false;
    }

    public void setFirstSet(Set<String> rfs)
    {
        firstSet = rfs;
    }

    public Set<String> getFirstSet()
    {
        return firstSet;
    }

    @Override
    public String toString() {
	return "SynRule [name=" + name + ", derivation=" + derivation + " ]";
    }
    
    
}
