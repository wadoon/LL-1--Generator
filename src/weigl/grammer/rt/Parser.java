package weigl.grammer.rt;

import java.lang.reflect.InvocationTargetException;

public interface Parser {
	public AST getParseTree();
	public void run(String source) throws Exception;
}
