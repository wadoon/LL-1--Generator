package weigl.grammar.ll;

/**
 * Exception during parsing of an grammar definition.
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 */
public class GrammarParserException extends Exception
{
    private static final long serialVersionUID = 6459388912250328829L;

    private int               lineno;

    public GrammarParserException()
    {
        super();
    }

    public GrammarParserException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public GrammarParserException(String message)
    {
        super(message);
    }

    public GrammarParserException(Throwable cause)
    {
        super(cause);
    }

    public int getLineno()
    {
        return lineno;
    }

    public void setLineno(int lineno)
    {
        this.lineno = lineno;
    }

}
