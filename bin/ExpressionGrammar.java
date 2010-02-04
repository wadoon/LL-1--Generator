import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import weigl.grammar.GrammarParserException;
import weigl.grammar.ProductionGrammerParser;


public class ExpressionGrammar {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws GrammarParserException 
	 */
	public static void main(String[] args) throws IOException, GrammarParserException {
		String s =  "E: TZ\n"+
					"Z: +TZ|€\n"+
					"T: FX\n"+
					"X: *X|€\n"+
					"F: (F)|I\n"+
					"I: aY|bY\n"+
					"Y: aY|bY|0Y|1Y|€";
		
		String source = ProductionGrammerParser.generateSource(s);
		OutputStreamWriter fw = new FileWriter(new File("src/Parser.java"));
		fw.write(source);
		fw.close();
	}

}
