package weigl.grammar;


import java.io.IOException;

import weigl.grammar.rt.AST;
import weigl.grammar.rt.Parser;
import weigl.grammar.rt.ParserFather;
import weigl.tools.compiler.OnTheFlyCompiler;

/**
 * Decorator for JavaSources of parser. It compiles the sources and instantiate
 * a parser class. You can access the parser functions through this interface.
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 * 
 */
public class ParserWrapper implements Parser {
	private static final String CLAZZ_NAME = "Parser";
	private Parser parser;

	public ParserWrapper(String javaSource) throws IOException,
			InstantiationException, IllegalAccessException {

		OnTheFlyCompiler otlExecutor = new OnTheFlyCompiler();
		Class<?> clazz = otlExecutor.compile(CLAZZ_NAME, javaSource);
		parser = ParserFather.class.cast(clazz.newInstance());
	}

	// private void write(String javaSource, String string) {
	// try {
	// OutputStreamWriter fw = new FileWriter(new File(string));
	// fw.write(javaSource);
	// fw.close();
	// } catch (IOException e) {
	// }
	// }

	@Override
	public AST getParseTree() {
		return parser.getParseTree();
	}

	@Override
	public void run(String source) {
		parser.run(source);
	}

}
