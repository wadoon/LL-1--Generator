package weigl.grammar.lltck;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import weigl.io.file.FileUtils;
import weigl.std.WArrays;

import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class TokenParserGenerator {
    // public static final String CLAZZ_NAME = "TParser";
    // public static final String CONANCIAL_NAME = "weigl.grammar.lltck."
    // + CLAZZ_NAME;
    // public static final String[] CLASSES_NAMES = new String[] {
    // CONANCIAL_NAME,
    // CONANCIAL_NAME + "$Rule" + CLAZZ_NAME + "Callback",
    // CONANCIAL_NAME + "$Rule" + CLAZZ_NAME + "CallbackAdapter",
    // CONANCIAL_NAME + "$Token" + CLAZZ_NAME + "Callback",
    // "weigl.grammar.lltck.TokenTParser" };

    private static final String[] TEMPLATES = new String[] {
	    "weigl.grammar.lltck.ParserCLAZZ",
	    "weigl.grammar.lltck.RuleCLAZZCallback",
	    "weigl.grammar.lltck.TokenCLAZZ",
	    "weigl.grammar.lltck.TokenCLAZZCallback", };

    private String source;
    private TokenGrammarParser stx;
    private List<SynRule> rules;
    private List<SynToken> tokens;

    public TokenParserGenerator(String text) throws LeftRecursionException,
	    RuleUnknownException {
	source = text;
	parse();
    }

    private void parse() throws LeftRecursionException, RuleUnknownException {
	stx = new TokenGrammarParser(source);
	rules = stx.getRules();
	tokens = stx.getTokens();
	GrammarAlgorithms.check(rules);

	for (SynToken e : tokens)
	    System.out.format("%-20s = %20s%n", e.name, e.regex);

	for (SynRule e : rules) {
	    System.out.format("%-20s%n", e.name);
	    for (SynDerivation d : e.getDerivations()) {
		System.out.format("\t%-20s%n", d.getTokenList().toString());
		System.out.format("\t\t%20s%n", d.getFirstTokens().toString());
	    }
	}
    }

    public Map<File, String> getJavaSource(String pgk, String clazzname) {
	Map<File, String> generatedCode = Maps.newHashMap();
	try {

	    Configuration cfg = new Configuration();
	    cfg.setObjectWrapper(DefaultObjectWrapper.DEFAULT_WRAPPER);
	    cfg.setDefaultEncoding("UTF-8");

	    SimpleHash rootMap = new SimpleHash();
	    rootMap.put("classname", clazzname);
	    rootMap.put("tokenname", "Token" + clazzname);
	    rootMap.put("tokens", tokens);
	    rootMap.put("rules", rules);
	    rootMap.put("input", source);
	    rootMap.put("pgk", pgk);

	    for (int i = 0; i < TEMPLATES.length; i++) {
		System.out.println(TEMPLATES[i] + ".ftl");
		Reader reader = new InputStreamReader(getClass()
			.getResourceAsStream(
				"/" + TEMPLATES[i].replace('.', '/') + ".ftl"));
		Template tpl = new Template(TEMPLATES[i] + ".ftl", reader, cfg);
		StringWriter output = new StringWriter(1024);
		tpl.process(rootMap, output);
		generatedCode.put(toJavaFile(TEMPLATES[i], pgk, clazzname),
			output.getBuffer().toString());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return generatedCode;
    }

    public static File toJavaFile(String name, String pkg, String clazz) {
	name = name.replaceAll("CLAZZ", clazz);
	String[] chunks = name.split("\\.");

	File f = new File(pkg.replace('.', '/') + '/'
		+ chunks[chunks.length - 1] + ".java");
	return f;
    }

    public static void main(String[] args) throws IOException,
	    LeftRecursionException, RuleUnknownException {
	String s = "$whitespace = \\s\n" + "digits = \\d+\n"
		+ "letters = \\w+\n" + "\n" + "START: digits letters digits";

	TokenParserGenerator tgp = new TokenParserGenerator(s);

	Map<File, String> javaSource = tgp.getJavaSource("weigl.parser", "T");
	for (Entry<File, String> string : javaSource.entrySet())
	    FileUtils.writeTo(string.getValue(), string.getValue());
    }
}
