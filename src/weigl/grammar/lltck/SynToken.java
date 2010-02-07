package weigl.grammar.lltck;

public class SynToken
{
    public final String  name;
    public final String  regex;
    public final boolean hidden;

    public SynToken(final String name, final String regex)
    {
        this(name, regex, false);
    }

    public SynToken(final String name, final String regex, final boolean hidden)
    {
        this.name = name;
        this.regex = regex;
        this.hidden = hidden;
    }

    public String getName()
    {
        return name;
    }

    public String getRegex()
    {
        return regex;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public String getHidden()
    {
        return "" + hidden;
    }
}
