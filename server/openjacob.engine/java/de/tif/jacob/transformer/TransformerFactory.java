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
package de.tif.jacob.transformer;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.exception.UnavailableTransformerException;
import de.tif.jacob.util.config.Config;


/**
 * @author Andreas Herz
 *
 */
public class TransformerFactory extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: TransformerFactory.java,v 1.4 2009/12/09 19:01:42 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  static private final transient Log logger = LogFactory.getLog(BootstrapEntry.class);
 
  private static HashMap transformers = new HashMap();
   
  /**
   * Get a configured transformer for the hands over mimeType.
   * 
   * @param mimeType
   * @return
   */
  public static ITransformer get(String mimeType) throws Exception
  {
    Object trans = transformers.get(mimeType);
    if(trans ==null)
      throw new UnavailableTransformerException(mimeType);
    
    
  	return (ITransformer)((Class)trans).newInstance();
  }

  // load all transformers which are defined in the etr.properties
  //
  public void init() throws Throwable
  {
    logger.info("\ttry to register transformers for mime types");
    Config conf = Config.getCommonConfig();
    for(int i=0; i<1000; i++)
    {
    	String transClassName = conf.getProperty("transformer.adapter.class."+i);
      if(transClassName!=null)
      {
        Class clazz = Class.forName(transClassName);
       	  ITransformer trans = (ITransformer)clazz.newInstance();
          logger.info("\tregister transformer for datatype:"+trans.getMimeType());
          transformers.put(trans.getMimeType(),clazz);
      }
    }
    logger.info("\tTransformerFactory successfully loaded.");
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // do nothing here
  }
}
