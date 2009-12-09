package weigl.grammar.compiler;

/**
 * Hack for making {@link ClassLoader#defineClass} public.
 *
 *
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date   07.12.2009
 * @version 1
 *
 */
public class ClassLoaderByteArray extends ClassLoader {
	/**
	 * @param b byte array of the java class data
	 * @return the class behind the byte array
	 */
	public static Class<?> load(byte[] b) {
		ClassLoaderByteArray clba = new ClassLoaderByteArray();
		return clba.defineClass(null, b, 0, b.length);
	}
}
