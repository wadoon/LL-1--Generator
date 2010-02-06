package weigl.grammar.lltck;

import java.util.regex.Pattern;
import weigl.grammar.lltck.rt.*;
import weigl.grammar.lltck.rt.interfaces.*;


public class ParserTest extends TokenParserFather<TokenParserTest> {
	
	public ParserTest() {
		super(TokenParserTest.values());
	}
	
	public Node<Token<TokenParserTest>> START()
	{	
		final Node< Token < TokenParserTest > > n = newNode( TokenParserTest.START );	
		boolean matched = false;
			if(lookahead( TokenParserTest._password))			{
				matched=true;
				n.add(match(TokenParserTest._password));
				n.add(match(TokenParserTest.string));
			}
			else
			if(lookahead( TokenParserTest._user))			{
				matched=true;
				n.add(match(TokenParserTest._user));
				n.add(match(TokenParserTest.string ));
			}
			
		
		if(!matched)
			error(/*...*/);
		return n;
	}
	
	
	@Override
	public Node<Token<TokenParserTest>> start() {return START();}
}
 
//TOKEN ENUM;
enum TokenParserTest implements TokenDefinition<TokenParserTest>
{
	//tokens
	 _password("\\Qpassword:\\E") , _user("\\Quser:\\E") , string("\\w+") ,
	//rules
	 START(true)  
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
	public boolean hiddenChannel() { return hidden;   }
	public boolean isRule() {		return rule;      }
}
