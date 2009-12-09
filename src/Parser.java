import weigl.grammar.rt.*;
public class Parser extends ParserFather {

		public void start() { syntaxTree=new AST(A());  }
		public AST.Node A() {
			final AST.Node n = newNode("A");

			boolean matched = false;
			if ( lookahead("bc") )
				{
				matched=true;
				n.add(B());
			}
			if ( lookahead("a") )
				{
				matched=true;
				n.add(match('a'));
				n.add(A());
			}
			if(!matched) error('a','b','c');

			return n;
		}
		public AST.Node B() {
			final AST.Node n = newNode("B");

			boolean matched = false;
			if ( lookahead("b") )
				{
				matched=true;
				n.add(match('b'));
				n.add(B());
			}
			if ( lookahead("c") )
				{
				matched=true;
				n.add(match('c'));
				n.add(C());
			}
			if(!matched) error('b','c');

			return n;
		}
		public AST.Node C() {
			final AST.Node n = newNode("C");

			boolean matched = false;
			if ( lookahead("c") )
				{
				matched=true;
				n.add(match('c'));
				n.add(C());
			}
			if(!matched) {
				matched=true;
				n.add(new AST.Leaf("€"));
			}
			if(!matched) error('c','€');

			return n;
		}

}//end class
