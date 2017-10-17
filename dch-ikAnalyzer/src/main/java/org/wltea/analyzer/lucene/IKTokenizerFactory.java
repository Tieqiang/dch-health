package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/22.
 */
public final class IKTokenizerFactory extends TokenizerFactory {
    private boolean useSmart;

    public IKTokenizerFactory(Map<String, String> args)
    {
        super(args);
        this.useSmart = getBoolean(args, "useSmart", false);
        if (!(args.isEmpty()))
            throw new IllegalArgumentException("Unknown parameters: " + args);
    }

    public Tokenizer create(AttributeFactory factory)
    {
        return new IKTokenizer(factory, this.useSmart);
    }
}
