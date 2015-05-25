package de.tif.jacob.core.data.impl.index.analyser;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer; // for javadoc
import org.apache.lucene.util.Version;

/**
 * {@link Analyzer} for German language.
 * <p>
 * Supports an external list of stopwords (words that will not be indexed at
 * all). A default set of stopwords is used unless an alternative list is
 * specified, but the exclusion list is empty by default.
 * </p>
 * 
 * <p>
 * <b>NOTE</b>: This class uses the same {@link Version} dependent settings as
 * {@link StandardAnalyzer}.
 * </p>
 * 
 * @since 2.10
 */
public class SimpleGermanAnalyzer extends Analyzer
{

  /**
   * List of typical german stopwords.
   */
  public final static String[] GERMAN_STOP_WORDS = {
      "einer", "eine", "eines", "einem", "einen", "der", "die", "den", "denen", "dem", "deren", //
      "dies", "diese", "dieses", "dieser", "diesem", "des", "vor", "von", "vom",
      "das", "dass", "daß", "du", "er", "sie", "es", //
      "was", "wer", "wie", "wir", "und", "oder", "ohne", "mit", //
      "am", "an", "ab", "im", "in", "aus", "auf", "ist", "sind", "sein", "war", "wird", "ihr",
      "ihre", "ihres", "ihrer", "ihren", "ihrem", "ihnen", "als", "für", "von", "dich", "dir", "sich", //
      "mich", "mir", "mein", "sein", "kein", "keine", "keiner", "so", "da", "ob",  //
      "durch", "bei", "um", "beim", "über", "unter", "wegen", "wird", "werden", "zu", "zur", "zum" };

  /**
   * Contains the stopwords used with the {@link StopFilter}.
   */
  private Set stopSet = new HashSet();

  private final Version matchVersion;

  /**
   * Builds an analyzer with the default stop words: {@link #GERMAN_STOP_WORDS}.
   */
  public SimpleGermanAnalyzer(Version matchVersion)
  {
    stopSet = StopFilter.makeStopSet(GERMAN_STOP_WORDS);
    this.matchVersion = matchVersion;
  }

  /**
   * Builds an analyzer with the given stop words.
   */
  public SimpleGermanAnalyzer(Version matchVersion, String[] stopwords)
  {
    stopSet = StopFilter.makeStopSet(stopwords);
    this.matchVersion = matchVersion;
  }

  /**
   * Builds an analyzer with the given stop words.
   */
  public SimpleGermanAnalyzer(Version matchVersion, Map stopwords)
  {
    stopSet = new HashSet(stopwords.keySet());
    this.matchVersion = matchVersion;
  }

  /**
   * Builds an analyzer with the given stop words.
   */
  public SimpleGermanAnalyzer(Version matchVersion, File stopwords) throws IOException
  {
    stopSet = WordlistLoader.getWordSet(stopwords);
    this.matchVersion = matchVersion;
  }

  /**
   * Creates a {@link TokenStream} which tokenizes all the text in the provided
   * {@link Reader}.
   * 
   * @return A {@link TokenStream} built from a {@link StandardTokenizer}
   *         filtered with {@link StandardFilter}, {@link LowerCaseFilter},
   *         {@link StopFilter}, and {@link GermanSubstituteFilter}
   */
  public TokenStream tokenStream(String fieldName, Reader reader)
  {
    TokenStream result = new StandardTokenizer(matchVersion, reader);
    result = new StandardFilter(result);
    result = new LowerCaseFilter(result);
    result = new StopFilter(StopFilter.getEnablePositionIncrementsVersionDefault(matchVersion), result, stopSet);
    result = new GermanSubstituteFilter(result);
    return result;
  }

  private class SavedStreams
  {
    Tokenizer source;
    TokenStream result;
  };

  /**
   * Returns a (possibly reused) {@link TokenStream} which tokenizes all the
   * text in the provided {@link Reader}.
   * 
   * @return A {@link TokenStream} built from a {@link StandardTokenizer}
   *         filtered with {@link StandardFilter}, {@link LowerCaseFilter},
   *         {@link StopFilter}, and {@link GermanSubstituteFilter}
   */
  public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException
  {
    if (overridesTokenStreamMethod)
    {
      // LUCENE-1678: force fallback to tokenStream() if we
      // have been subclassed and that subclass overrides
      // tokenStream but not reusableTokenStream
      return tokenStream(fieldName, reader);
    }

    SavedStreams streams = (SavedStreams) getPreviousTokenStream();
    if (streams == null)
    {
      streams = new SavedStreams();
      streams.source = new StandardTokenizer(matchVersion, reader);
      streams.result = new StandardFilter(streams.source);
      streams.result = new LowerCaseFilter(streams.result);
      streams.result = new StopFilter(StopFilter.getEnablePositionIncrementsVersionDefault(matchVersion), streams.result, stopSet);
      streams.result = new GermanSubstituteFilter(streams.result);
      setPreviousTokenStream(streams);
    }
    else
    {
      streams.source.reset(reader);
    }
    return streams.result;
  }
}
