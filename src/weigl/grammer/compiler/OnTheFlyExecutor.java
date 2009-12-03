package weigl.grammer.compiler;

import java.io.IOException;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

public class OnTheFlyExecutor {
	public Class<?> buildParserFor(String className, String source) throws IOException {
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
			System.out.println(Arrays.toString(b));		
			return ClassLoaderByteArray.load(b);
		}
		return null;
	}
}
