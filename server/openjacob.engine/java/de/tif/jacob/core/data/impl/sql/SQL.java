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

package de.tif.jacob.core.data.impl.sql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SQL
{
  static public transient final String RCS_ID = "$Id: SQL.java,v 1.1 2007/01/19 09:50:47 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Common SQL logger
   */
  static final transient Log logger = LogFactory.getLog(SQL.class);
  
  public static final String NULL = "null";
  public static final char QUOTE = '\'';
  public static final char WILDCARD = '%';

  // TODO: check whether this is standard for all supported RDBs
  public static final char SWILDCARD = '_';

  private SQL()
  {
    // prohibit to invoke
  }

  /**
   * Standard SQL text escape function.
   * 
   * @param input
   * @param doQuoting
   * @return
   */
  public static String convertToSQL(String input, boolean doQuoting)
  {
    // IBIS: This method should be moved to SQLDataSource. Currently this is not done because class AliasUserExpression uses it!
    if (input == null || input.length() == 0)
      return NULL;
    
    // check whether the string contains any quotes
    int index = input.indexOf(QUOTE);
    if (index == -1)
    {
      // no quotes 
      if (doQuoting)
      {
        StringBuffer erg = new StringBuffer(input.length() + 2);
        erg.append(QUOTE);
        erg.append(input);
        erg.append(QUOTE);
        return erg.toString();
      }
      return input;
    }

    // initialise buffer
    StringBuffer erg;
    int pos = index;
    if (doQuoting)
    {
      erg = new StringBuffer(input.length() + 2);
      erg.append(QUOTE);
      erg.append(input);
      erg.append(QUOTE);
      pos++;
    }
    else
    {
      erg = new StringBuffer(input);
    }

    // escape quotes where necessary
    int newindex = index;
    do
    {
      pos += newindex - index + 1;
      index = newindex;
      erg.insert(pos, QUOTE);
      newindex = input.indexOf(QUOTE, index + 1);
    }
    while (newindex != -1);

    return erg.toString();
  }
}