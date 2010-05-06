package weigl.grammar.lltck;

public class RuleUnknownException extends Exception
{
    private static final long serialVersionUID = 1L;

    public RuleUnknownException()
    {
    }

    public RuleUnknownException(String message)
    {
        super(message);

    }

    public RuleUnknownException(Throwable cause)
    {
        super(cause);

    }

    public RuleUnknownException(String message, Throwable cause)
    {
        super(message, cause);

    }

}
