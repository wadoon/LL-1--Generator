package weigl.grammer.compiler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;

class JavaFileManagerDelegate implements JavaFileManager {
	private JavaFileManager jfm;
	private String conancialParserName;
	private MemoryJavaFileObject classCatcher;

	public JavaFileManagerDelegate(JavaFileManager jfm, String catchClassName) {
		conancialParserName = catchClassName;
		this.jfm = jfm;
	}

	// catch up parser.class
	public JavaFileObject getJavaFileForOutput(Location location,
			String className, Kind kind, FileObject sibling) throws IOException {
		System.out.println("JavaFileManagerDelegate.getJavaFileForOutput()" + className);
		if (conancialParserName.equals(className)) {
			classCatcher = new MemoryJavaFileObject(className);
			return getCatcher();
		} else
			return jfm.getJavaFileForOutput(location, className, kind, sibling);
	}

	public void close() throws IOException {
		System.out.println("JavaFileManagerDelegate.close()");
		jfm.close();
	}

	public void flush() throws IOException {
		System.out.println("JavaFileManagerDelegate.flush()");
		jfm.flush();
	}

	public ClassLoader getClassLoader(Location location) {
		return getClass().getClassLoader();
		// return jfm.getClassLoader(location);
	}

	public FileObject getFileForInput(Location location, String packageName,
			String relativeName) throws IOException {
		System.out.println("JavaFileManagerDelegate.getFileForInput()");
		return jfm.getFileForInput(location, packageName, relativeName);
	}

	public FileObject getFileForOutput(Location location, String packageName,
			String relativeName, FileObject sibling) throws IOException {
		System.out.println("JavaFileManagerDelegate.getFileForOutput()");
		return jfm.getFileForOutput(location, packageName, relativeName,
				sibling);
	}

	public JavaFileObject getJavaFileForInput(Location location,
			String className, javax.tools.JavaFileObject.Kind kind)
			throws IOException {
		System.out.println("JavaFileManagerDelegate.getJavaFileForInput()");
		return jfm.getJavaFileForInput(location, className, kind);
	}

	public boolean handleOption(String current, Iterator<String> remaining) {
//		System.out.println("JavaFileManagerDelegate.handleOption()");
		return jfm.handleOption(current, remaining);
	}

	public boolean hasLocation(Location location) {
//		System.out.println("JavaFileManagerDelegate.hasLocation()");
		return jfm.hasLocation(location);
	}

	public String inferBinaryName(Location location, JavaFileObject file) {
		String s = jfm.inferBinaryName(location, file);
//		System.out.println("JavaFileManagerDelegate.inferBinaryName() : " +s);
		return s;
	}

	public boolean isSameFile(FileObject a, FileObject b) {
//		System.out.println("JavaFileManagerDelegate.isSameFile()");
		return jfm.isSameFile(a, b);
	}

	public int isSupportedOption(String option) {
//		System.out.println("JavaFileManagerDelegate.isSupportedOption()");
		return jfm.isSupportedOption(option);
	}

	public Iterable<JavaFileObject> list(Location location, String packageName,
			Set<Kind> kinds, boolean recurse) throws IOException {
//		System.out.println("JavaFileManagerDelegate.list()");
		return jfm.list(location, packageName, kinds, recurse);
	}

	public MemoryJavaFileObject getCatcher() {
		System.out.println("JavaFileManagerDelegate.getCatcher()");
		return classCatcher;
	}
}
