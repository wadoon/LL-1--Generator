package ${pgk};

import java.util.regex.Pattern;
import weigl.grammar.lltck.rt.*;
import weigl.grammar.lltck.rt.interfaces.*;

public interface Token${classname}Callback {
		<#list tokens as token> 
		public Object ${token.name}( Token<${tokenname}> tok);
		</#list>
	}