package weigl.grammar.test;

import java.util.regex.Pattern;
import weigl.grammar.rt.AST.Node;
import weigl.grammar.test.tokenizer.TokenDef;


public class ${classname} extends TokenParserFather<Token${classname}> {
	
	public ${classname}() {
		super(Token${classname}.values());
	}
	
	<#list rules as rule>
	public Node ${rule}()
	{	
		final Node n = new Node("${rule.name?j_string}");	
		boolean matched = false;
		<#list rule.derivations as derivation>
			if(lookahead(${derivation.firstSet}))
			{
				matched=true;
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
	public void start() {}
}
 
//TOKEN ENUM;
enum Token${classname} implements TokenDef<Token${classname}>{
	<#list tokens as token> ${token.name}("${token.regex?j_string}") <#if key_has_next>,</#if> </#list>;
	
	private Pattern pattern;
	private Token${classname}(String regex)
	{
		pattern = Pattern.compile(regex);
	}
	
	public Pattern getPattern() { return pattern; }
	public Token${classname} getType() { return this;}
}