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

package de.tif.jacob.letter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class RTFLetter extends AbstractTextLetter
{
  private static final Map SPECIAL_CHARS;
  private static final Character DBLQUOTE = new Character('\"');
  private static final Character QUOTE = new Character('\'');
  private static final Character SPACE = new Character(' ');

  /** List of characters to escape with corresponding replacement strings */
  static 
  {
      SPECIAL_CHARS = new HashMap();
      SPECIAL_CHARS.put(new Character('\t'), "tab");
      SPECIAL_CHARS.put(new Character('\n'), "line");
      SPECIAL_CHARS.put(new Character('\''), "rquote");
      SPECIAL_CHARS.put(new Character('\"'), "rdblquote");
      SPECIAL_CHARS.put(new Character('\\'), "\\");
      SPECIAL_CHARS.put(new Character('{'), "{");
      SPECIAL_CHARS.put(new Character('}'), "}");
  }
  
  public String getMimeType()
  {
    return LetterFactory.MIMETYPE_RTF;
  }
  
  public String encode(String str) 
  {
    if (str == null) {
        return null;
    }

    StringBuffer sb = new StringBuffer(Math.max(16, str.length()));
    // TODO: could be made more efficient (binary lookup, etc.)
    for (int i = 0; i < str.length(); i++) 
    {
        final Character c = new Character(str.charAt(i));
        Character d;
        String replacement;
        if (i != 0) 
        {
            d = new Character(str.charAt(i - 1));
        } 
        else 
        {
            d = new Character(str.charAt(i));
        }

        //This section modified by Chris Scott
        //add "smart" quote recognition
        if (c.equals((Object)DBLQUOTE) && d.equals((Object)SPACE)) {
            replacement = "ldblquote";
        } else if (c.equals((Object)QUOTE) && d.equals((Object)SPACE)) {
            replacement = "lquote";
        } else {
            replacement = (String)SPECIAL_CHARS.get(c);
        }

        if (replacement != null) {
            // RTF-escaped char
            sb.append('\\');
            sb.append(replacement);
            sb.append(' ');
        } else if (c.charValue() > 127) {
            // write unicode representation
            sb.append("\\u");
            sb.append(Integer.toString((int)c.charValue()));
            sb.append("\\\'3f");
        } else {
            // plain char that is understood by RTF natively
            sb.append(c.charValue());
        }
    }
    return sb.toString();
  }
  
  public static void main(String[] args)
  {//\u0041\ufffd\ufffd\ufffd\ufffd\u0043\u0043
  	try
    {
      String s = new RTFLetter().encode("\n\täöü");
      System.out.println(s);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
