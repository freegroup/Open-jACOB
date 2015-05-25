package de.tif.jacob.core.data.impl.index.analyser;

public class GermanSubstituter
{
  /**
   * Buffer for the terms while stemming them.
   */
  private StringBuffer sb = new StringBuffer();

  /**
   * Substitutes the given term to an unique <tt>discriminator</tt>.
   * 
   * @param term
   *          The term that should be substituted.
   * @return Discriminator for <tt>term</tt>
   */
  public String substitute(String term)
  {
    // Use lowercase for medium stemming.
    term = term.toLowerCase();
    // Reset the StringBuffer.
    sb.delete(0, sb.length());
    sb.insert(0, term);
    // Substitution starts here...
    substitute(sb);
    return sb.toString();
  }

  /**
   * Do some substitutions for the term:
   * 
   * - Substitute Umlauts with their corresponding vowel: ÄäÖöÜü -> aou, "ß" is
   * substituted by "ss"
   */
  private void substitute(StringBuffer buffer)
  {
    for (int c = 0; c < buffer.length(); c++)
    {
      // Substitute Umlauts.
      if (buffer.charAt(c) == 'ä')
      {
        buffer.setCharAt(c, 'a');
      }
      else if (buffer.charAt(c) == 'ö')
      {
        buffer.setCharAt(c, 'o');
      }
      else if (buffer.charAt(c) == 'ü')
      {
        buffer.setCharAt(c, 'u');
      }
      else if (buffer.charAt(c) == 'ß')
      {
        buffer.setCharAt(c, 's');
        buffer.insert(c + 1, 's');
      }
    }
  }

  public static void main(String[] args)
  {

    for (int i = 0; i < args.length; i++)
    {
      String arg = args[i];
      System.out.println("\"" + arg + "\" -> \"" + (new GermanSubstituter()).substitute(arg) + "\"");
    }
  }
}
