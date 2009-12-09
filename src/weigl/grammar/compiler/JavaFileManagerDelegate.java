package weigl.grammar.compiler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;

/**
 * special {@link JavaFileManager} to catch a specific write request for one
 * class data. <br>
 * You can retrieve the content via the {@link #getCatcher()} method.
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 * 
 */
class JavaFileManagerDelegate implements JavaFileManager {
	private JavaFileManager jfm;
	private String conancialParserName;
	private MemoryJavaFileObject classCatcher;

	/**
	 * @param jfm
	 *            delegated object
	 * @param catchClassName
	 *            class that should be catched up
	 */
	public JavaFileManagerDelegate(JavaFileManager jfm, String catchClassName) {
		conancialParserName = catchClassName;
		this.jfm = jfm;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * catch write request for a given conancial path.
	 */
	public JavaFileObject getJavaFileForOutput(Location location,
			String className, Kind kind, FileObject sibling) throws IOException {
		if (conancialParserName.equals(className)) {
			classCatcher = new MemoryJavaFileObject(className);
			return getCatcher();
		} else
			return jfm.getJavaFileForOutput(location, className, kind, sibling);
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException {
		// System.out.println("JavaFileManagerDelegate.close()");
		jfm.close();
	}

	/**
	 * {@inheritDoc}
	 */
	public void flush() throws IOException {
		// System.out.println("JavaFileManagerDelegate.flush()");
		jfm.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	public ClassLoader getClassLoader(Location location) {
		return getClass().getClassLoader();
		// return jfm.getClassLoader(location);
	}

	/**
	 * {@inheritDoc}
	 */
	public FileObject getFileForInput(Location location, String packageName,
			String relativeName) throws IOException {
		// System.out.println("JavaFileManagerDelegate.getFileForInput()");
		return jfm.getFileForInput(location, packageName, relativeName);
	}

	/**
	 * {@inheritDoc}
	 */
	public FileObject getFileForOutput(Location location, String packageName,
			String relativeName, FileObject sibling) throws IOException {
		// System.out.println("JavaFileManagerDelegate.getFileForOutput()");
		return jfm.getFileForOutput(location, packageName, relativeName,
				sibling);
	}

	/**
	 * {@inheritDoc}
	 */
	public JavaFileObject getJavaFileForInput(Location location,
			String className, javax.tools.JavaFileObject.Kind kind)
			throws IOException {
		// System.out.println("JavaFileManagerDelegate.getJavaFileForInput()");
		return jfm.getJavaFileForInput(location, className, kind);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean handleOption(String current, Iterator<String> remaining) {
		// System.out.println("JavaFileManagerDelegate.handleOption()");
		return jfm.handleOption(current, remaining);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasLocation(Location location) {
		// System.out.println("JavaFileManagerDelegate.hasLocation()");
		return jfm.hasLocation(location);
	}

	/**
	 * {@inheritDoc}
	 */
	public String inferBinaryName(Location location, JavaFileObject file) {
		String s = jfm.inferBinaryName(location, file);
		// System.out.println("JavaFileManagerDelegate.inferBinaryName() : "
		// +s);
		return s;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSameFile(FileObject a, FileObject b) {
		// System.out.println("JavaFileManagerDelegate.isSameFile()");
		return jfm.isSameFile(a, b);
	}

	/**
	 * {@inheritDoc}
	 */
	public int isSupportedOption(String option) {
		// System.out.println("JavaFileManagerDelegate.isSupportedOption()");
		return jfm.isSupportedOption(option);
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterable<JavaFileObject> list(Location location, String packageName,
			Set<Kind> kinds, boolean recurse) throws IOException {
		// System.out.println("JavaFileManagerDelegate.list()");
		return jfm.list(location, packageName, kinds, recurse);
	}

	/**
	 * @return the Catcher object. can be null.
	 */
	public MemoryJavaFileObject getCatcher() {
		// System.out.println("JavaFileManagerDelegate.getCatcher()");
		return classCatcher;
	}
}
