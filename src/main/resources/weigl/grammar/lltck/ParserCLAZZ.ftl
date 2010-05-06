package ${pgk};

import java.util.regex.Pattern;
import weigl.grammar.lltck.rt.*;
import weigl.grammar.lltck.rt.interfaces.*;


/**
 *
 * generated Parser class for grammar:
 * <pre>${input}</pre>
 */ 
public class Parser${classname} extends TokenParserFather<${tokenname}> {
	private Rule${classname}Callback ruleListener;
	private Token${classname}Callback tokListener;	
	
	public Parser${classname}() {
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
	public Node<${tokenname}> ${rule.name}()
	{	
		final Node<${tokenname}>  n = newNode( ${tokenname}.${rule.name} );	
		boolean matched = false;
		<#list rule.derivations as derivation>
			<#if derivation.isEpsilon() >
				<#t> if(true)
				{
					matched=true;
					n.add(match(${tokenname}.EPSILON));
				}
			<#else>
<#t>			if(lookahead(
				<#list derivation.firstTokens as tok> ${tokenname}.${tok}<#t> 
				<#if tok_has_next>,</#if></#list>))<#t>
			{
				matched=true;
				<#list derivation.tokenList as exptoken>
					<#if exptoken == exptoken?lower_case >
						n.add(match(${tokenname}.${exptoken}));
					<#else>
						n.add(${exptoken}());
					</#if>
				</#list>
			}
			</#if>
			<#if derivation_has_next>else</#if>
		</#list>
		
		if(!matched)
			error(/*...*/);
		return ruleListener.${rule.name}(n);
	}
	</#list>
	
	
	@Override
	public Node<${tokenname}> start() {return START();}

	public void run(String source)
	{
		reset();
		input = new Tokenizer<${tokenname}>(source,  tokListener , getTokens());
		consume();
		syntaxTree = new DefaultAbstractSyntaxTree<${tokenname}>( start() );
	}
		
	
	public static void main(String argc[]) throws java.io.IOException
	{
		java.io.BufferedReader br = new java.io.BufferedReader(
			new java.io.InputStreamReader(System.in));
		Parser${classname} parser = new Parser${classname}();
		parser.run(br.readLine());
		System.out.println(parser.getParseTree());
	}
	
	
	public static class Rule${classname}CallbackAdapter implements Rule${classname}Callback 
	{
		<#list rules as rule>
		@Override
		public Node<${tokenname}> ${rule.name}(Node<${tokenname}> node)
		{
			return node;
		}
		</#list>
	}
}