package weigl.grammar.ll;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import weigl.grammar.ll.Token.NonTerminalSymbol;
import weigl.grammar.ll.Token.TerminalSymbol;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

/**
 * Builder/JavaSource-Generator for given ProductionRules and First Sets
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 */
public class ParserBuilder
{

    private Multimap<String, Replacement>        rules;
    private TreeMultimap<Replacement, Character> firstSets;

    private StringWriter                         writer = new StringWriter();
    private PrintWriter                          out    = new PrintWriter(writer);

    public ParserBuilder(Multimap<String, Replacement> productionRules,
                    TreeMultimap<Replacement, Character> firstSets)
    {

        this.rules = productionRules;
        this.firstSets = firstSets;

        header();
        start();
        footer();
        out.flush();
    }

    private void header()
    {
        level1("import weigl.grammar.rt.*;");
        level1("public class Parser extends ParserFather {\n");

        level2("public void start() { syntaxTree=new AST(%s());  }", rules.keySet().iterator()
                        .next());

    }

    private void start()
    {
        Map<String, Collection<Replacement>> m = rules.asMap();
        for (Entry<String, Collection<Replacement>> e : m.entrySet())
        {
            if (isNonTerminal(e.getKey())) generateMethod(e);
        }
    }

    private void generateMethod(Entry<String, Collection<Replacement>> e)
    {
        if (e.getValue().size() == 0)
        {
            System.err.format("rule %s has no replacement.%n", e.getKey());
            return;
        }

        level2("public AST.Node %s() {", e.getKey());
        level3("final AST.Node n = newNode(\"%s\");%n", e.getKey());

        if (e.getValue().size() == 1) simpleMethodBody(e.getValue().iterator().next());// only
                                                                                       // one
                                                                                       // element
        else multipleMethodBody(e);

        level3("return n;");
        level2("}");
    }

    private void simpleMethodBody(Replacement r)
    {
        for (Token tok : r.getTokens())
        {
            if (isTerminal(tok))
            {
                if (!tok.equals(Token.EPSILON)) matchingRules(tok);
            }
            else
            {
                level4("n.add(%s());", tok.TEXT);
            }
        }
    }

    private void matchingRules(Token tok)
    {
        for (char c : tok.TEXT.toCharArray())
        {
            level4("n.add(match('%c'));", c);
        }
    }

    private void multipleMethodBody(Entry<String, Collection<Replacement>> e)
    {
        boolean epsilon = false;
        level3("boolean matched = false;");
        Set<Character> excpected = new TreeSet<Character>();
        boolean elseTree = false;
        for (Replacement r : e.getValue()) // for every alternativ
        {
            excpected.addAll(firstSets.get(r));

            epsilon = epsilon | r.isEpsilon();

            if (r.isEpsilon()) continue;

            if (elseTree) level3("else if ( lookahead(\"%s\") )", setToString(firstSets.get(r)));
            else level3("if ( lookahead(\"%s\") )", setToString(firstSets.get(r)));

            level3("{");
            level4("matched=true;");
            simpleMethodBody(r);
            level3("}");
            elseTree = true;
        }

        if (epsilon)
        {
            level3("if(!matched) {");
            level4("matched=true;");
            level4("n.add(new AST.Leaf(\"€\"));");
            level3("}");
        }

        level3("if(!matched) error(%s);%n", setToCommaString(excpected));
    }

    private void footer()
    {
        level1("%n}//end class");
    }

    private Object setToCommaString(Set<Character> excpected)
    {
        StringBuilder sb = new StringBuilder();
        for (Character c : excpected)
            sb.append("'").append(c).append("'").append(',');
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private String setToString(SortedSet<Character> sortedSet)
    {
        StringBuilder sb = new StringBuilder();
        for (Character c : sortedSet)
            sb.append(c);
        return sb.toString();
    }

    private boolean isTerminal(String tok)
    {
        return Character.isLowerCase(tok.charAt(0));
    }

    private boolean isNonTerminal(String tok)
    {
        return !isTerminal(tok);
    }

    private boolean isTerminal(Token tok)
    {
        return (tok instanceof TerminalSymbol);
    }

    @SuppressWarnings("unused")
    private boolean isNonTerminal(Token tok)
    {
        return (tok instanceof NonTerminalSymbol);
    }

    private void level1(String format, Object... args)
    {
        out.format(format + "%n", args);
    }

    private void level2(String format, Object... args)
    {
        out.format("\t" + format + "%n", args);
    }

    private void level3(String format, Object... args)
    {
        out.format("\t\t" + format + "%n", args);
    }

    private void level4(String format, Object... args)
    {
        out.format("\t\t\t" + format + "%n", args);
    }

    public static String run(Multimap<String, Replacement> productionRules,
                    TreeMultimap<Replacement, Character> firstSets)
    {
        ParserBuilder pb = new ParserBuilder(productionRules, firstSets);
        return pb.getSource();
    }

    private String getSource()
    {
        out.flush();
        writer.flush();
        return writer.toString();
    }

}
