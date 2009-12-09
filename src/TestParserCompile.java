import java.io.FileReader;
import java.io.IOException;

import weigl.grammar.compiler.OnTheFlyCompiler;

public class TestParserCompile {
	public static void main(String[] args) throws IOException {
		OnTheFlyCompiler otfe = new OnTheFlyCompiler();
		Class<?> c = otfe.compile("weigl.grammar.genparsers.Parser", readIn("ClassTemplate.java"));
		System.out.println(c.getCanonicalName());
	}

	private static String readIn(String string) throws IOException {
		FileReader fr = new FileReader(string);
		char[] c = new char[4 * 1024];
		int i = fr.read(c);
		return new String(c, 0, i);
	}
}
