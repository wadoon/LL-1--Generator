package weigl.grammar.lltck.rt;

import weigl.grammar.lltck.rt.interfaces.AST;

/**
 * Interface for describing a parser.<br>
 * <b>Parsers are not thread-safe</b>
 *
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date   06.12.2009
 * @version 1
 *
 */
public interface Parser<E> {
	/**
	 * return the Abstract Syntax Tree for the currently parsed input. 
	 * @return {@link AST}
	 */
	public AST<E> getParseTree();
	
	/**
	 * run the parser after the given <pre>source</pre> 
	 * @param source {@link String} to be parsed
	 * @throws IllegalStateException if <pre>source</pre> is not parseable
	 */
	public void run(String source);
}
