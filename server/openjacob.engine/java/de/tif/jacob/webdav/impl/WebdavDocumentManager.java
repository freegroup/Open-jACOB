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
package de.tif.jacob.webdav.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * WebDAV document manager for accessing WebDAV document proxies, i.e.
 * implementations of {@link WebdavDocument}.
 * 
 * @author Andreas Sonntag
 */
public class WebdavDocumentManager
{
  static public final transient String RCS_ID = "$Id: WebdavDocumentManager.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static final Log logger = LogFactory.getLog(WebdavDocumentManager.class);
  
  private static int maxSizeForNextLog = 10;
  
  private static final Map documentMap = new HashMap();
  
  private WebdavDocumentManager()
  {
    // avoid instantiation
  }
  
  protected static synchronized void register(WebdavDocument document)
  {
    documentMap.put(document.getId(), document);

    // just to give a hint, if we have a memory leak here :-)
    //
    if (documentMap.size() >= maxSizeForNextLog)
    {
      if (logger.isInfoEnabled())
      {
        logger.info(documentMap.size() + " documents registered");

        maxSizeForNextLog = (documentMap.size() * 12) / 10;
      }
    }
  }

  protected static synchronized void unregister(WebdavDocument document) 
  {
    documentMap.remove(document.getId());
  }

  public static synchronized WebdavDocument get(String id)
  {
    return (WebdavDocument) documentMap.get(id);
  }
  
  private static synchronized List getDocuments()
  {
    // copy content
    return new ArrayList(documentMap.values());
  }

  /**
   * Returns all current document ids.
   * 
   * @return <code>List<code> of <code>String</code>, i.e. ids
   */
  public static List getDocumentIds()
  {
    // this method should not be synchronized to avoid
    // deadlocks with synchronized methods of
    // WebdavDocument implementations.
    //
    List documents = getDocuments();
    List result = new ArrayList(documents.size());
    for (int i = 0; i < documents.size(); i++)
    {
      WebdavDocument webdavDocument = (WebdavDocument) documents.get(i);
      String id = webdavDocument.getId();
      if (id != null)
        result.add(id);
    }
    
    if (logger.isDebugEnabled())
    {
      logger.debug(result.size() + " document ids returned");
    }
    
    return result;
  }
}
