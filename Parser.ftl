package weigl.grammar.lltck;

import java.util.regex.Pattern;
import weigl.grammar.lltck.rt.*;
import weigl.grammar.lltck.rt.interfaces.*;

<#assign tokenname = "Token${classname}">

public class ${classname} extends TokenParserFather<${tokenname}> {
	
	public ${classname}() {
		super(Token${classname}.values());
	}
	
	<#list rules as rule>
	public Node<Token<${tokenname}>> ${rule.name}()
	{	
		final Node< Token < ${tokenname} > > n = newNode( ${tokenname}.${rule.name} );	
		boolean matched = false;
		<#list rule.derivations as derivation>
<#t>			if(lookahead(
				<#list derivation.firstTokens as tok> ${tokenname}.${tok}<#t> 
				<#if tok_has_next>,</#if></#list>))<#t>
			{
				matched=true;
				<#list derivation.tokenList as exptokens>
				n.add(match(${tokenname}.${exptokens}));
				</#list>
			}
			<#if derivation_has_next>else</#if>
		</#list>
		
		<#if rule.isEpsilon()>
		if(!matched)
		{
			matched=true;
			n.add(newNode("€"));
		}
		<#else>
		if(!matched)
			error(/*...*/);
		</#if>		
		return n;
	}
	</#list>
	
	
	@Override
	public Node<Token<${tokenname}>> start() {return START();}
}
 
//TOKEN ENUM;
enum ${tokenname} implements TokenDefinition<${tokenname}>
{
	//tokens
	<#list tokens as token> ${token.name}("${token.regex?j_string}") ,</#list>
	//rules
	<#list rules as rule> ${rule.name}(true) <#if rule_has_next>,</#if> </#list>
	;
	
	private Pattern pattern;
	private boolean hidden,rule;
	private ${tokenname}(String regex)
	{
		pattern = Pattern.compile(regex);
	}

	private ${tokenname}(String regex, boolean h)
	{
		this(regex);
		hidden = h;				
	}

	private ${tokenname}(boolean r )
	{
		this("",true);
		rule = r;
	}
	
	public Pattern getPattern() { return pattern;     }
	public Token${classname} getType() { return this; }
	public boolean hiddenChannel() { return hidden;   }
	public boolean isRule() {		return rule;      }
}
