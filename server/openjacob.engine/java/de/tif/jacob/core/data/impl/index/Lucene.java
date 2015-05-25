/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.core.data.impl.index;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import de.tif.jacob.core.data.impl.index.event.StandardLuceneEventHandler;

/**
 * Lucene helper class.
 * 
 * @since 2.10
 * @author Andreas
 */
public final class Lucene
{
  static public transient final String RCS_ID = "$Id: Lucene.java,v 1.4 2010/09/22 11:22:47 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";

  /**
   * Common Lucene logger
   */
  public static final transient Log logger = LogFactory.getLog(Lucene.class);

  public static final char QUOTE = '\"';
  public static final char WILDCARD = '*';
  public static final char SWILDCARD = '?';

  private Lucene()
  {
    // prohibit to invoke
  }

  /**
   * Standard Lucene text escape function.
   * 
   * @param input
   * @param doQuoting
   * @return
   */
  public static String convertToLucene(String input, boolean doQuoting)
  {
    if (input == null || input.length() == 0)
      // IBIS: check
      return null;

    StringBuffer erg = new StringBuffer();
    if (doQuoting)
      erg.append(QUOTE);
    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt(i);
      switch (c)
      {
        case '+':
        case '-':
        case '&':
        case '|':
        case '!':
        case '(':
        case ')':
        case '{':
        case '}':
        case '[':
        case ']':
        case '^':
        case '~':
        case '*':
        case '?':
        case ':':
        case '"':
        case '\\':
        case ' ':
          erg.append('\\').append(c);
          break;

        default:
          erg.append(c);
          break;
      }
    }
    if (doQuoting)
      erg.append(QUOTE);
    return erg.toString();
  }

  public static void close(IndexWriter writer)
  {
    try
    {
      try
      {
        writer.close();
      }
      catch (OutOfMemoryError err)
      {
        // try close again (see Lucene JavaDoc!)
        writer.close();
      }
    }
    catch (Exception ex)
    {
      logger.error("Can not close index writer", ex);
    }
  }

  public static void main(String[] args)
  {
    try
    {
      String text = "(id:\"2\" OR id:\"3\") AND \"Andreas\\|Sonntag Nina\"";
      // Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_29);
      Analyzer analyzer = new KeywordAnalyzer();
      QueryParser parser = new QueryParser(Version.LUCENE_29, StandardLuceneEventHandler.CONTENTS_FIELD, analyzer);
      Query query = parser.parse(text);
      System.out.println(query);
      System.out.println(QueryParser.escape(text));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}