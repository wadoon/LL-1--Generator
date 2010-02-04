import weigl.grammar.rt.*;
public class Parser extends ParserFather {

	public static void main(String[] args) {
		Parser p = new Parser();
		p.run("a+a");
		System.out.println(p.getParseTree().getLeafWord());
	}
	
	public void start() { syntaxTree=new AST(E());  }
	public AST.Node E() {
		final AST.Node n = newNode("E");

			n.add(T());
			n.add(Z());
		return n;
	}
	public AST.Node F() {
		final AST.Node n = newNode("F");

		boolean matched = false;
		if ( lookahead("ab") )
		{
			matched=true;
			n.add(I());
		}
		else if ( lookahead("(") )
		{
			matched=true;
			n.add(match('('));
			n.add(F());
			n.add(match(')'));
		}
		if(!matched) error('(','a','b');

		return n;
	}
	public AST.Node I() {
		final AST.Node n = newNode("I");

		boolean matched = false;
		if ( lookahead("a") )
		{
			matched=true;
			n.add(match('a'));
			n.add(Y());
		}
		else if ( lookahead("b") )
		{
			matched=true;
			n.add(match('b'));
			n.add(Y());
		}
		if(!matched) error('a','b');

		return n;
	}
	public AST.Node T() {
		final AST.Node n = newNode("T");

			n.add(F());
			n.add(X());
		return n;
	}
	public AST.Node X() {
		final AST.Node n = newNode("X");

		boolean matched = false;
		if ( lookahead("*") )
		{
			matched=true;
			n.add(match('*'));
			n.add(X());
		}
		if(!matched) {
			matched=true;
			n.add(new AST.Leaf("€"));
		}
		if(!matched) error('*','€');

		return n;
	}
	public AST.Node Y() {
		final AST.Node n = newNode("Y");

		boolean matched = false;
		if ( lookahead("0") )
		{
			matched=true;
			n.add(match('0'));
			n.add(Y());
		}
		else if ( lookahead("1") )
		{
			matched=true;
			n.add(match('1'));
			n.add(Y());
		}
		else if ( lookahead("a") )
		{
			matched=true;
			n.add(match('a'));
			n.add(Y());
		}
		else if ( lookahead("b") )
		{
			matched=true;
			n.add(match('b'));
			n.add(Y());
		}
		if(!matched) {
			matched=true;
			n.add(new AST.Leaf("€"));
		}
		if(!matched) error('0','1','a','b','€');

		return n;
	}
	public AST.Node Z() {
		final AST.Node n = newNode("Z");

		boolean matched = false;
		if ( lookahead("+") )
		{
			matched=true;
			n.add(match('+'));
			n.add(T());
			n.add(Z());
		}
		if(!matched) {
			matched=true;
			n.add(new AST.Leaf("€"));
		}
		if(!matched) error('+','€');

		return n;
	}

}//end class
