package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * Created by Administrator on 2017/9/8.
 */
public class IKCrudeAnalyzer extends Analyzer {
    private boolean useSmart;

    public boolean useSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    /**
     * IK分词器Lucene  Analyzer接口实现类
     *
     * 默认细粒度切分算法
     */
    public IKCrudeAnalyzer() {
        this(true);
    }

    /**
     * IK分词器Lucene Analyzer接口实现类
     *
     * @param useSmart 当为true时，分词器进行智能切分
     */
    public IKCrudeAnalyzer(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }
    /**
     * 重载Analyzer接口，构造分词组件
     */
    protected TokenStreamComponents createComponents(String fieldName)
    {
        Tokenizer _IKTokenizer = new IKTokenizer(true);
        return new TokenStreamComponents(_IKTokenizer);
    }
}
