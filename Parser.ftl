package weigl.grammar.lltck;

import java.util.regex.Pattern;
import weigl.grammar.lltck.rt.*;
import weigl.grammar.lltck.rt.interfaces.*;

<#assign tokenname = "Token${classname}">
/**
 *
 * generated Parser class for grammar:
 * <pre>${input}</pre>
 */ 
public class ${classname} extends TokenParserFather<${tokenname}> {
	private Rule${classname}Callback ruleListener;
	private Token${classname}Callback tokListener;	
	
	public ${classname}() {
		super(Token${classname}.values());
		setRuleListener( new Rule${classname}CallbackAdapter() );
	}
	
	public void setRuleListener(Rule${classname}Callback listener)
	{
		this.ruleListener = listener;
	}

	public void setTokenListener(Token${classname}Callback listener)
	{
		this.tokListener =  listener;
	}
	
	<#list rules as rule>
	/**
	 * <pre>${rule.doc}</pre>
	 */
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
				<#list derivation.tokenList as exptoken>
					<#if exptoken == exptoken?lower_case >
						n.add(match(${tokenname}.${exptoken}));
					<#else>
						n.add(match(${exptoken}()));
					</#if>
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
		return ruleListener.${rule.name}(n);
	}
	</#list>
	
	
	@Override
	public Node<Token<${tokenname}>> start() {return START();}

	public void run(String source)
	{
		reset();
		input = new Tokenizer<${tokenname}>(source,  tokListener , getTokens());
		consume();
		start();
	}
		
	
	public static void main(String argc[]) throws java.io.IOException
	{
		java.io.BufferedReader br = new java.io.BufferedReader(
			new java.io.InputStreamReader(System.in));
		${classname} parser = new ${classname}();
		parser.run(br.readLine());
		System.out.println(parser.getParseTree());
	}
	
	
	public static interface Rule${classname}Callback 
	{
		<#list rules as rule>
		public Node<Token<${tokenname}>> ${rule.name}(Node<Token<${tokenname}>> node);
		</#list>
	}

	public static class Rule${classname}CallbackAdapter implements Rule${classname}Callback 
	{
		<#list rules as rule>
		@Override
		public Node<Token<${tokenname}>> ${rule.name}(Node<Token<${tokenname}>> node)
		{
			return node;
		}
		</#list>
	}
	
	public interface Token${classname}Callback {
		<#list tokens as token> 
		public Token<${tokenname}> ${token.name}( Token<${tokenname}> tok);
		</#list>
	}
}



 
//TOKEN ENUM;
enum ${tokenname} implements TokenDefinition<${tokenname}>
{
	//tokens
	<#list tokens as token> ${token.name}("${token.regex?j_string}", ${token.hidden?string}) ,</#list>
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
	public boolean isHidden() { return hidden;        }
	public boolean isRule()   {		return rule;      }
}
