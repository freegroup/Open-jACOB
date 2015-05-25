package de.tif.jacob.core.data.impl.index.analyser;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

/**
 * A {@link TokenFilter} that substitutes German words.
 * <p>
 * 
 * @since 2.10
 */
public final class GermanSubstituteFilter extends TokenFilter
{
  private final GermanSubstituter substituter;

  private TermAttribute termAtt;

  public GermanSubstituteFilter(TokenStream in)
  {
    super(in);
    this.substituter = new GermanSubstituter();
    this.termAtt = (TermAttribute) addAttribute(TermAttribute.class);
  }

  /**
   * @return Returns true for next token in the stream, or false at EOS
   */
  public boolean incrementToken() throws IOException
  {
    if (input.incrementToken())
    {
      String term = termAtt.term();
      String s = substituter.substitute(term);
      // If not substituted, don't waste the time adjusting the token.
      if ((s != null) && !s.equals(term))
        termAtt.setTermBuffer(s);
      return true;
    }
    return false;
  }
}
