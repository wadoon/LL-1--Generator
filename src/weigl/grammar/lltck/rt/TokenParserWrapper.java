package weigl.grammar.lltck.rt;

import java.io.IOException;

import weigl.grammar.lltck.rt.interfaces.AST;
import weigl.tools.compiler.OnTheFlyCompiler;

/**
 * only use for dynamic generated parser, cause of missing type safety
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 2010-02-07
 */
@SuppressWarnings("unchecked")
public class TokenParserWrapper implements Parser
{
    private Parser<?> parser;

    public TokenParserWrapper(String javaSource, String clazzName, String[] secondary) throws IOException, InstantiationException, IllegalAccessException
    {
        OnTheFlyCompiler otlExecutor = new OnTheFlyCompiler();
        Class<?> clazz = otlExecutor.compile(javaSource, clazzName,secondary);
        parser = TokenParserFather.class.cast(clazz.newInstance());
    }

    @Override
    public AST getParseTree()
    {
        return parser.getParseTree();
    }

    @Override
    public void run(String source)
    {
        parser.run(source);
    }
}
