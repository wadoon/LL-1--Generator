package weigl.grammar.compiler;

import java.io.IOException;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

/**
 *  A high level {@link JavaCompiler} interface for compiling and loading in memory at runtime.
 *
 *
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date   07.12.2009
 * @version 1
 *
 */
public class OnTheFlyCompiler {
	/**
	 * Compiles and loads the created class at runtime, return null on error.
	 * @param className
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public Class<?> compile(String className, String source) throws IOException {
		final JavaFileObject jss = new JavaStringSource(className, source);
		
		JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
		Iterable<JavaFileObject> compilationUnits = Arrays.asList(jss);
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

		JavaFileManager jfm = jc
				.getStandardFileManager(diagnostics, null, null);
		
		JavaFileManagerDelegate jfmd = new JavaFileManagerDelegate(jfm, className);

		CompilationTask ct = jc.getTask(null,jfmd, null, null, null, compilationUnits);
		if (ct.call()) 
		{
			byte[] b = jfmd.getCatcher().getClassByte();		
			return ClassLoaderByteArray.load(b);
		}
		return null;
	}
}
