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
package de.tif.jacob.core.adjustment.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.adjustment.IHistory;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class History implements IHistory
{
  static public final transient String RCS_ID = "$Id: History.java,v 1.1 2007/01/19 09:50:32 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  protected static final Log logger = LogFactory.getLog(History.class);

  private static final History NO_HISTORY = new NoHistory();
  
  
  public static History get(String className)
  {
    if (null == className || "".equals(className))
      return NO_HISTORY;
    
    try
    {
      Class clazz = Class.forName(className);
      return (History) clazz.newInstance();
    }
    catch (Exception ex)
    {
      logger.fatal("Getting history implementation "+className+" failed!", ex);
      throw new RuntimeException(ex.toString()); 
    }
  }

  private static class NoHistory extends History
  {
		/* (non-Javadoc)
		 * @see de.tif.jacob.core.adjustment.IHistory#build(de.tif.jacob.core.data.impl.ta.TADeleteRecordAction)
		 */
		public void build(TADeleteRecordAction action) throws Exception
		{
      // nothing more to do
    }

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.adjustment.IHistory#build(de.tif.jacob.core.data.impl.ta.TAInsertRecordAction)
		 */
		public void build(TAInsertRecordAction action) throws Exception
		{
      // nothing more to do
    }

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.adjustment.IHistory#build(de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction)
		 */
		public void build(TAUpdateRecordAction action) throws Exception
		{
      // nothing more to do
    }
  }
}
