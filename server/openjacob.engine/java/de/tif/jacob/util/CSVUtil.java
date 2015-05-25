/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Tarragon GmbH
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

package de.tif.jacob.util;

/**
 * CSV utility class which double quotes CVS values, if they contain the
 * used delimiter itself. Double quoting has also to be done, if a CSV value
 * contains a line break character (CR or LF) and line break characters are not
 * handled, i.e. discarded out off the CSV value.
 * 
 * @since 2.9.2
 */
public class CSVUtil
{
  /**
   * The default CSV delimiter is a comma.
   */
  public static final char DEFAULT_CSV_DELIMITER = ',';

  private final char delimiter;
  private final boolean handleLinebreaks;

  /**
   * Constructor which uses a comma as delimiter.
   * 
   * @see #DEFAULT_CSV_DELIMITER
   */
  public CSVUtil()
  {
    this(DEFAULT_CSV_DELIMITER);
  }

  /**
   * Constructor which uses a delimiter given as argument.
   * 
   * @param delimiter
   *          the delimiter to use
   */
  public CSVUtil(char delimiter)
  {
    this(delimiter, true);
  }

  /**
   * Constructor which uses a delimiter given as argument and defines line break
   * character handling.
   * 
   * @param delimiter
   *          the delimiter to use
   * @param handleLinebreaks
   *          if line break characters (CR and LF) should be handled (i.e.
   *          discarded), set to <code>true</code>. Otherwise, if line break
   *          characters should be transfered, set to <code>false</code>.
   */
  public CSVUtil(char delimiter, boolean handleLinebreaks)
  {
    this.delimiter = delimiter;
    this.handleLinebreaks = handleLinebreaks;
  }

  /**
   * Returns the used delimiter.
   * 
   * @return the used delimiter
   */
  public char getDelimiter()
  {
    return delimiter;
  }

  /**
   * Returns whether line break characters (CR and LF) are handled (i.e.
   * discarded) or not (i.e. are transfered into the CSV value)
   * 
   * @return <code>true</code> line break characters are handled or
   *         <code>false</code>, if not.
   */
  public boolean handleLinebreaks()
  {
    return handleLinebreaks;
  }

  /**
   * Converts a given string value to a CSV value, which might be quoted, if the
   * delimiter is contained or not handled line break characters are existing.
   * 
   * @param value
   *          the string value to convert to a CSV value
   * @return the converted CSV value
   */
  public String toCSV(String value)
  {
    if (value == null)
      throw new NullPointerException("Argument 'value' must not be null");

    boolean quote = false;
    boolean linebreaks = false;

    for (int i = 0; i < value.length(); i++)
    {
      char c = value.charAt(i);
      switch (c)
      {
        case '\r':
        case '\n':
          // value must be quoted, if linebreaks are not discarded
          if (!this.handleLinebreaks)
            quote = true;
          linebreaks = true;
          break;

        default:
          if (c == this.delimiter)
            quote = true;
          break;
      }
    }

    if (quote || (linebreaks && this.handleLinebreaks))
    {
      StringBuffer result = new StringBuffer(value.length() + 10);

      // initial opening quote
      if (quote)
        result.append("\"");

      for (int i = 0; i < value.length(); i++)
      {
        char c = value.charAt(i);
        switch (c)
        {
          case '"':
            // escape double quotes by doubling them
            if (quote)
            {
              result.append("\"\"");
              continue;
            }
            break;

          case '\r':
          case '\n':
            if (this.handleLinebreaks)
              // discard line break characters
              continue;
            break;
        }
        result.append(c);
      }

      // finish quote
      if (quote)
        result.append("\"");

      return result.toString();
    }

    return value;
  }
}
