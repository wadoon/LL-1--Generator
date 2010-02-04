package weigl.grammar.lltck.rt;

import java.util.regex.Pattern;

/**
 * Defintionen of an tocken
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @param <E> the enum token class
 */
public interface TokenDefinition<E> {
	public E getType();
	public Pattern getPattern();
}
