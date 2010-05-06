import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import weigl.grammar.lltck.LeftRecursionException;
import weigl.grammar.lltck.RuleUnknownException;
import weigl.grammar.lltck.TokenParserGenerator;
import weigl.io.file.FileUtils;
import weigl.std.GetOpt;
import weigl.std.GetOpt.MissingOptArgException;

public class CreateTokenParser {
    public static void usage() {
	System.out.println("Generating token-based LL-Parsers");
	System.out
		.println("(c) 2009, 2010 Alexander Weigl <weigla@fh-trier.de>");
	System.out.println();
	System.out.println("Options:");
	System.out.println("\t-o\toutput file (java source)");
	System.out.println("\t-i\tgrammar file");
	System.out.println();
	System.out.println("Input file:");
	System.out.println("<TOKENNAME>=<REGEX>");
	System.out.println("<RULENAME>={ TOKENNAME | RULENAME | LITERAL} ...");
	System.out
		.println("RULENAME in upper case, TOKENNAME in lowercases, LITERAL between \"...\"");
	System.out.println();
    }

    public static void main(String[] args) throws IllegalArgumentException,
	    MissingOptArgException, IOException, LeftRecursionException,
	    RuleUnknownException {

	GetOpt getopt = new GetOpt(args, "d:o:i:vuc:p:");
	int i;
	String inputFile = null;
	// boolean verbose = false;

	String packge = "", clazz = "", outputdir=".";
	while ((i = getopt.getNextOption()) > 0) {
	    switch (i) {
	    case 'i':
		inputFile = getopt.getOptionArg();
		break;
	    case 'v':
		// verbose = true;
		break;
	    case 'u':
		usage();
		System.exit(0);
	    case 'c':
		clazz = getopt.getOptionArg();break;
	    case 'p':
		packge = getopt.getOptionArg();
		break;
	    case 'd':
		outputdir = getopt.getOptionArg();
		break;
	    }
	}

	if (inputFile == null) {
	    System.err.println("no input file was given!");
	    System.exit(1);
	}

	String input = FileUtils.readFile(inputFile);
	TokenParserGenerator tpg = new TokenParserGenerator(input);
	Map<File, String> javaSource = tpg.getJavaSource(packge, clazz);
	
	File dir = new File(outputdir);
	File folder = new File( dir ,packge.replace('.', '/'));
	folder.mkdirs();
	System.out.println("Created:  " + folder);
	for (Entry<File, String> string : javaSource.entrySet())
	    FileUtils.writeTo(new File(dir,string.getKey().toString()), string.getValue());
    }
}
