import weigl.grammer.Parser;

public class TestGrammar {
	public static void main(String[] args) {
		final String s = 
			"A: aA| B \n"  + 
			"B: € |bB \n" + 
			"C: € |cC";
		new Parser(s);
	}
}
