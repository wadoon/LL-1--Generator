import java.io.FileReader;
import java.io.IOException;

import weigl.grammer.compiler.OnTheFlyExecutor;

public class TestParserCompile {
	public static void main(String[] args) throws IOException {
		OnTheFlyExecutor otfe = new OnTheFlyExecutor();
		Class<?> c = otfe.buildParserFor("weigl.grammar.genparsers.Parser", readIn("ClassTemplate.java"));
		System.out.println(c.getCanonicalName());
	}

	private static String readIn(String string) throws IOException {
		FileReader fr = new FileReader(string);
		char[] c = new char[4 * 1024];
		int i = fr.read(c);
		return new String(c, 0, i);
	}
}
