package weigl.grammar.lltck;

import java.util.regex.Pattern;

import weigl.grammar.lltck.rt.DefaultAbstractSyntaxTree;
import weigl.grammar.lltck.rt.Token;
import weigl.grammar.lltck.rt.TokenDefinition;
import weigl.grammar.lltck.rt.TokenParserFather;
import weigl.grammar.lltck.rt.Tokenizer;
import weigl.grammar.lltck.rt.interfaces.Node;

/**
 *
 * generated Parser class for grammar:
 * <pre>string = \w+
$whitespaces = \s+
START: "user: " string | "password: " string | STRING 
STRING: string</pre>
 */ 
public class ParserTest extends TokenParserFather<TokenParserTest> {
	private RuleParserTestCallback ruleListener;
	private TokenParserTestCallback tokListener;	
	
	public ParserTest() {
		super(TokenParserTest.values());
		setRuleListener( new RuleParserTestCallbackAdapter() );
	}
	
	public void setRuleListener(RuleParserTestCallback listener)
	{
		this.ruleListener = listener;
	}

	public void setTokenListener(TokenParserTestCallback listener)
	{
		this.tokListener =  listener;
	}
	
	/**
	 * <pre>START -> Array[STRING]
START -> Array[_password, string]
START -> Array[_user, string]
</pre>
	 */
	public Node<TokenParserTest> START()
	{	
		final Node<TokenParserTest>  n = newNode( TokenParserTest.START );	
		boolean matched = false;
			if(lookahead( TokenParserTest.string))			{
				matched=true;
						n.add(STRING());
			}
			else
			if(lookahead( TokenParserTest._password))			{
				matched=true;
						n.add(match(TokenParserTest._password));
						n.add(match(TokenParserTest.string));
			}
			else
			if(lookahead( TokenParserTest._user))			{
				matched=true;
						n.add(match(TokenParserTest._user));
						n.add(match(TokenParserTest.string));
			}
			
		
		if(!matched)
			error(/*...*/);
		return ruleListener.START(n);
	}
	/**
	 * <pre>STRING -> Array[string]
</pre>
	 */
	public Node<TokenParserTest> STRING()
	{	
		final Node<TokenParserTest>  n = newNode( TokenParserTest.STRING );	
		boolean matched = false;
			if(lookahead( TokenParserTest.string))			{
				matched=true;
						n.add(match(TokenParserTest.string));
			}
			
		
		if(!matched)
			error(/*...*/);
		return ruleListener.STRING(n);
	}
	
	
	@Override
	public Node<TokenParserTest> start() {return START();}

	public void run(String source)
	{
		reset();
		input = new Tokenizer<TokenParserTest>(source,  tokListener , getTokens());
		consume();
		syntaxTree = new DefaultAbstractSyntaxTree<TokenParserTest>( start() );
	}
		
	
	public static void main(String argc[]) throws java.io.IOException
	{
		java.io.BufferedReader br = new java.io.BufferedReader(
			new java.io.InputStreamReader(System.in));
		ParserTest parser = new ParserTest();
		parser.run(br.readLine());
		System.out.println(parser.getParseTree());
	}
	
	
	public static interface RuleParserTestCallback 
	{
		public Node<TokenParserTest> START(Node<TokenParserTest> node);
		public Node<TokenParserTest> STRING(Node<TokenParserTest> node);
	}

	public static class RuleParserTestCallbackAdapter implements RuleParserTestCallback 
	{
		@Override
		public Node<TokenParserTest> START(Node<TokenParserTest> node)
		{
			return node;
		}
		
		@Override
		public Node<TokenParserTest> STRING(Node<TokenParserTest> node)
		{
			return node;
		}
	}
	
	public interface TokenParserTestCallback {
		public Token<TokenParserTest> $whitespaces( Token<TokenParserTest> tok);
		public Token<TokenParserTest> _password( Token<TokenParserTest> tok);
		public Token<TokenParserTest> _user( Token<TokenParserTest> tok);
		public Token<TokenParserTest> string( Token<TokenParserTest> tok);
	}
}



 
//TOKEN ENUM;
enum TokenParserTest implements TokenDefinition<TokenParserTest>
{
	//tokens
	 $whitespaces("\\s+", true) , _password("\\Qpassword:\\E", false) , _user("\\Quser:\\E", false) , string("\\w+", false) ,
	//rules
	 START(true) ,  STRING(true)  
	;
	
	private Pattern pattern;
	private boolean hidden,rule;
	private TokenParserTest(String regex)
	{
		pattern = Pattern.compile(regex);
	}

	private TokenParserTest(String regex, boolean h)
	{
		this(regex);
		hidden = h;				
	}

	private TokenParserTest(boolean r )
	{
		this("",true);
		rule = r;
	}
	
	public Pattern getPattern() { return pattern;     }
	public TokenParserTest getType() { return this; }
	public boolean isHidden() { return hidden;        }
	public boolean isRule()   {		return rule;      }
}
