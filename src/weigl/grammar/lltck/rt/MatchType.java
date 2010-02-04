package weigl.grammar.lltck.rt;

/**
 * defines the kind of searching for an token
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * 
 */
public enum MatchType {
	/**
	 * returns the first match
	 */
	FIRST,
	/**
	 * returns the best (most characters) match
	 */
	BEST;
}
