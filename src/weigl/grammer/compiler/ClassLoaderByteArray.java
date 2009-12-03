package weigl.grammer.compiler;

public class ClassLoaderByteArray extends ClassLoader 
{
	public static Class<?> load(byte[] b)
	{
		ClassLoaderByteArray clba = new ClassLoaderByteArray();
		return clba.defineClass(null, b, 0, b.length);
	}
}
