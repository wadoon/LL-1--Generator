

import weigl.grammer.rt.*;

class Parser extends ParserFather{


public AST.Node A()
{
final AST.Node n = newNode("A");
boolean matched = false;

if ( lookahead("b") ) 
 { 
matched=true;
n.add(B());

}

if ( lookahead("abc") ) 
 { 
matched=true;
n.add(match('a'));
n.add(A());

}

if(!matched) error();

return n;
}
public AST.Node B()
{
final AST.Node n = newNode("B");
boolean matched = false;

if ( lookahead("b") ) 
 { 
matched=true;
n.add(match('€'));

}

if ( lookahead("abc") ) 
 { 
matched=true;
n.add(match('b'));
n.add(B());

}

if(!matched) error();

return n;
}
public AST.Node C()
{
final AST.Node n = newNode("C");
boolean matched = false;

if ( lookahead("b") ) 
 { 
matched=true;
n.add(match('€'));

}

if ( lookahead("abc") ) 
 { 
matched=true;
n.add(match('c'));
n.add(C());

}

if(!matched) error();

return n;
}


}//end class