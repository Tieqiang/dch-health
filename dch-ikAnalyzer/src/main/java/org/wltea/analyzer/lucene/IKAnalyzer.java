package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public final class IKAnalyzer extends Analyzer
{
  private boolean useSmart;

  public boolean useSmart()
  {
    return this.useSmart;
  }

  public void setUseSmart(boolean useSmart) {
    this.useSmart = useSmart;
  }

  public IKAnalyzer()
  {
    this(false);
  }

  public IKAnalyzer(boolean useSmart)
  {
    this.useSmart = useSmart;
  }

  protected TokenStreamComponents createComponents(String fieldName)
  {
    Tokenizer _IKTokenizer = new IKTokenizer(useSmart());
    return new TokenStreamComponents(_IKTokenizer);
  }
}