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

package de.tif.jacob.entrypoint;

import java.util.Iterator;
import java.util.Properties;
import com.sun.jndi.toolkit.url.UrlUtil;

import de.tif.jacob.core.Context;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IMessageDialog;

/**
 * @author Andreas Herz
 * 
 */
public abstract class IGuiEntryPoint implements IEntryPoint
{
  static public final transient String RCS_ID = "$Id: IGuiEntryPoint.java,v 1.1 2007/01/19 09:50:50 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public abstract void enter(IClientContext context, Properties props) throws Exception;
  
  /**
   * @return the domainId which you want see first
   */
  public abstract String getDomain();
  
  /**
   * @return the formId which you want see first
   */
  public abstract String getForm();
  
  /**
   * 
   * @return return false if you don't want see the domain&amp;form navigation.
   */
  public abstract boolean hasNavigation();
  
  /**
   * 
   * @return false if you don't want see the SearchBrowser at the top.
   */
  public abstract boolean hasSearchBrowser();
  
  /**
   * 
   * @return false if you don't want see the toolbar at the top.
   */
  public abstract boolean hasToolbar();
  
  
  /**
   * Display an alert message on the user interface.
   * 
   * @param message The message itself of the dialog box.
   */
  public final void alert( String message)
  {
    IClientContext currentClientContext=(IClientContext)Context.getCurrent();
    if(currentClientContext!=null )
    {  
      IMessageDialog dialog = currentClientContext.createMessageDialog(message);
      dialog.show(350,300);
    }
  }
  
  
  /**
   * The returned URL is the common access URL for this EntryPoint.
   * If you call this URL, the EntryPoint will be called by the system.
   * 
   * @deprecated use {@link EntryPointUrl#getUrl(SessionContext, IGuiEntryPoint, Properties, boolean)} instead.
   * 
   * @param baseUrl the base URL of the jACOB server (like http://123.43.11.11:8080/jacob/ )
   * @param appId the application in which the EntryPoint is
   * @param props additional Properties which should hand over to the entryPoint.
   * @return the access URL for this EntryPoint
   */
  
  public final String getUrl(String baseUrl, String appId, Properties props) throws Exception
  {
    String params = "";
    String entryName = this.getClass().getName();
    entryName = entryName.substring(entryName.lastIndexOf(".")+1);
    if(props!=null)
    {
      Iterator iter = props.keySet().iterator();
      while (iter.hasNext())
    	{
    		String key   = (String) iter.next();
        String value = props.getProperty(key);
        params = params +"&"+key+"="+UrlUtil.encode(value,"ISO-8859-1");
    	}
    }
   
    return baseUrl+"enter?entry="+entryName+"&app="+appId+params;
  }
}


