package ${pgk};

import java.util.regex.Pattern;
import weigl.grammar.lltck.rt.*;
import weigl.grammar.lltck.rt.interfaces.*;

public interface Rule${classname}Callback 
{
	<#list rules as rule>
	public Node<${tokenname}> ${rule.name}(Node<${tokenname}> node);
	</#list>
}