package ${pgk};

import java.util.regex.Pattern;
import weigl.grammar.lltck.rt.*;
import weigl.grammar.lltck.rt.interfaces.*;

public enum ${tokenname} implements TokenDefinition<${tokenname}>
{
	//tokens	
<#t>  <#list tokens as token> ${token.name}("${token.regex?j_string}", ${token.hidden?string}) ,</#list>
	EPSILON(true),	 
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
