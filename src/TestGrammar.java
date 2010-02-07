import java.io.IOException;

import weigl.grammar.ll.ParserWrapper;
import weigl.grammar.ll.ProductionGrammerParser;
import weigl.grammar.ll.gui.TestDialog;

public class TestGrammar {
	public static void main(String[] args) throws IOException,
			InstantiationException, IllegalAccessException, Exception {
		final String s = "A: aA| B \n" + "B: cC|bB \n" + "C: € |cC";
		String javaSource = ProductionGrammerParser.generateSource(s);
		ParserWrapper pw = new ParserWrapper(javaSource);
		pw.run("aabbcc");
		System.out.println(pw.getParseTree());
		TestDialog.showFrame(pw.getParseTree());
	}
}
