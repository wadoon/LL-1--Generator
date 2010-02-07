package weigl.grammar.lltck;

import java.util.regex.Pattern;
import weigl.grammar.lltck.rt.*;
import weigl.grammar.lltck.rt.interfaces.*;

/**
 * generated Parser class for grammar:
 * 
 * <pre>
 * string = \w+
 * $whitespaces = \s+
 * START: "user: " string | "password: " string | START
 * </pre>
 */
public class ParserTest extends TokenParserFather<TokenParserTest>
{
    private RuleParserTestCallback  ruleListener;
    private TokenParserTestCallback tokListener;

    public ParserTest()
    {
        super(TokenParserTest.values());
        setRuleListener(new RuleParserTestCallbackAdapter());
    }

    public void setRuleListener(RuleParserTestCallback listener)
    {
        this.ruleListener = listener;
    }

    public void setTokenListener(TokenParserTestCallback listener)
    {
        this.tokListener = listener;
    }

    /**
     * <pre>
     * START -> Array[_QUOTE, START ]
     * START -> Array[_user, string ]
     * </pre>
     */
    public Node<Token<TokenParserTest>> START()
    {
        final Node<Token<TokenParserTest>> n = newNode(TokenParserTest.START);
        boolean matched = false;
        if (lookahead(TokenParserTest._QUOTE))
        {
            matched = true;
            n.add(match(_QUOTE()));
            n.add(match(START()));
        }
        else if (lookahead(TokenParserTest._user))
        {
            matched = true;
            n.add(match(TokenParserTest._user));
            n.add(match(TokenParserTest.string));
        }

        if (!matched) error(/* ... */);
        return ruleListener.START(n);
    }

    @Override
    public Node<Token<TokenParserTest>> start()
    {
        return START();
    }

    public void run(String source)
    {
        reset();
        input = new Tokenizer<TokenParserTest>(source, tokListener, getTokens());
        consume();
        start();
    }

    public static void main(String argc[]) throws java.io.IOException
    {
        java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(
                        System.in));
        ParserTest parser = new ParserTest();
        parser.run(br.readLine());
        System.out.println(parser.getParseTree());
    }

    public static interface RuleParserTestCallback
    {
        public Node<Token<TokenParserTest>> START(Node<Token<TokenParserTest>> node);
    }

    public static class RuleParserTestCallbackAdapter implements RuleParserTestCallback
    {
        @Override
        public Node<Token<TokenParserTest>> START(Node<Token<TokenParserTest>> node)
        {
            return node;
        }
    }

    public interface TokenParserTestCallback
    {
        public Token<TokenParserTest> $whitespaces(Token<TokenParserTest> tok);

        public Token<TokenParserTest> _QUOTE(Token<TokenParserTest> tok);

        public Token<TokenParserTest> _user(Token<TokenParserTest> tok);

        public Token<TokenParserTest> string(Token<TokenParserTest> tok);
    }
}

// TOKEN ENUM;
enum TokenParserTest implements TokenDefinition<TokenParserTest>
{
    // tokens
    $whitespaces("\\s+", true), _QUOTE("\\QUO\\E", false), _user("\\Quser:\\E", false), string(
                    "\\w+", false),
    // rules
    START(true);

    private Pattern pattern;
    private boolean hidden, rule;

    private TokenParserTest(String regex)
    {
        pattern = Pattern.compile(regex);
    }

    private TokenParserTest(String regex, boolean h)
    {
        this(regex);
        hidden = h;
    }

    private TokenParserTest(boolean r)
    {
        this("", true);
        rule = r;
    }

    public Pattern getPattern()
    {
        return pattern;
    }

    public TokenParserTest getType()
    {
        return this;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public boolean isRule()
    {
        return rule;
    }
}
