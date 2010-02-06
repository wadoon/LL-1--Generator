package weigl.grammar.lltck;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
		List<SynToken> tokens = stx.getTokens();
		List<SynRule> rules = stx.getRules();
		FirstSetTokenCalculator fstc = new FirstSetTokenCalculator(rules);

		for (SynToken e : tokens) 
			System.out.format("%-20s = %20s%n", e.name, e.regex);

		for (SynRule e : rules) 
		{
			System.out.format("%-20s%n", e.name);
			for (SynDerivation d : e.getDerivations()) 
			{
				System.out.format("\t%-20s%n", d.getTokenList().toString());
				System.out.format("\t\t%20s%n", d.getFirstTokens().toString());
			}
		}

		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File("."));
		cfg.setObjectWrapper(DefaultObjectWrapper.DEFAULT_WRAPPER);
		cfg.setDefaultEncoding("UTF-8");

		Template tpl = cfg.getTemplate("Parser.ftl");

		SimpleHash rootMap = new SimpleHash();
		rootMap.put("classname", "ParserTest");
		rootMap.put("test", new SynRule("test"));
		rootMap.put("tokens", tokens);
		rootMap.put("rules", rules);
		rootMap.put("firstSets", fstc.getFirstSets());

		tpl.process(rootMap, new FileWriter(
				"src/weigl/grammar/lltck/ParserTest.java"));
	}
}
