package weigl.grammar.lltck;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;

import weigl.std.Array;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TokenParserGenerator {
	public static void main(String[] args) throws IOException,
			TemplateException {
		final String s = "string = \\w+\n"
				+ "START: \"user: \" string | \"password: \" string\n";
		TokenGrammarParser tgp = new TokenGrammarParser();
		tgp.run(s);
		SyntaxTree stx = new SyntaxTree(tgp.getParseTree());
		for (SynToken e : stx.getTokens()) {
			System.out.format("%-20s = %20s%n", e.name, e.regex);
		}

		for (SynRule e : stx.getRules()) {
			System.out.format("%-20s = %20s%n", e.name, e.derivation);
		}

		FirstSetTokenCalculator fstc = new FirstSetTokenCalculator(stx
				.getRules());

		for (Entry<Array<String>, Set<String>> e : fstc.getFirstSets()
				.entrySet())
			System.out.format("%-20s = %20s%n", e.getKey().toString(), e
					.getValue().toString());

		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File("."));
		cfg.setObjectWrapper(DefaultObjectWrapper.DEFAULT_WRAPPER);
		cfg.setDefaultEncoding("UTF-8");

		Template tpl = cfg.getTemplate("Parser.ftl");

		SimpleHash rootMap = new SimpleHash();
		rootMap.put("classname", "ParserTest");
		rootMap.put("test", new SynRule("test"));
		rootMap.put("tokens", stx.getTokens());
		rootMap.put("rules", stx.getRules());
		rootMap.put("firstSets", fstc.getFirstSets());

		tpl.process(rootMap, new FileWriter(
				"src/weigl/grammar/test/ParserTest.java"));
	}
}
