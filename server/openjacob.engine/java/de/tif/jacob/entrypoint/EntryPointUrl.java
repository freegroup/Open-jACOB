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

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;
import com.sun.jndi.toolkit.url.UrlUtil;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 * The URL to a dedicated entry point to a jACOB application.<br>
 * You can create a URL for a GUI base EntryPoint or to Cmd (command) base entry Point.<br>
 * 
 * <b>usage:</b><br>
  <pre>
      Properties props=new Properties();
      props.setProperty("template",(String)name2templates.get(userSelection));
      props.setProperty("pkey",call.getStringValue("pkey"));
      EntryPointUrl.show(context,new MyGuiEntryPoint(),props);
  </pre>
 */
public class EntryPointUrl
{
  static class EntryPointType
  {
    final String name;
    EntryPointType(String entryName)
    {
      name=entryName;
    }
    public String toString()
    {
      return name;
    }
  }
  public final static EntryPointType ENTRYPOINT_GUI=new EntryPointType("enter");
  public final static EntryPointType ENTRYPOINT_CMD=new EntryPointType("cmdenter");

  private final Properties     props;
  private final String         entryName;
  private final EntryPointType type;
  
  /**
   * 
   * @param type
   * @param entryName
   * @param props
   * @deprecated This method will be protected in the next release.
   *               Use the <b>static</b> methods 'popup(...)' and 'show(...)' to work with entry points.
   */
  public EntryPointUrl(EntryPointType type, String entryName, Properties props)
  {
    this.type      = type;
    this.props     = props;
    this.entryName = entryName;
  }
  
  /**
   * Constructs the entry point URL for a given command entry point.
   * 
   * @param context
   *          the current session context
   * @param entry
   *          the desired command entry point
   * @param props
   *          the entry point properties
   * @param useVersion
   *          if set to <code>true</code> the entry point will only succeed
   *          for the current application version, otherwise the entry point
   *          will enter the most resent version of the application.
   * @return the URL to call the entry point from a http client
   */
  public static String getUrl(SessionContext context, ICmdEntryPoint entry, Properties props, boolean useVersion)
  {
    return new EntryPointUrl(ENTRYPOINT_CMD,ClassUtil.getShortClassName(entry.getClass()),props).toURL(context, useVersion);
  }
  
  /**
   * Constructs the entry point URL for a given GUI entry point.
   * 
   * @param context
   *          the current session context
   * @param entry
   *          the desired GUI entry point
   * @param props
   *          the entry point properties
   * @param useVersion
   *          if set to <code>true</code> the entry point will only succeed
   *          for the current application version, otherwise the entry point
   *          will enter the most resent version of the application.
   * @return the URL to call the entry point from a http client
   */
  public static String getUrl(SessionContext context, IGuiEntryPoint entry, Properties props, boolean useVersion)
  {
    return new EntryPointUrl(ENTRYPOINT_GUI,ClassUtil.getShortClassName(entry.getClass()),props).toURL(context, useVersion);
  }
  
  /**
   * Show the hands over EntryPoint.
   * <br>
   * <b>example:</b><br>
    <pre>
      Properties props=new Properties();
      props.setProperty("template",(String)name2templates.get(userSelection));
      props.setProperty("pkey",call.getStringValue("pkey"));
      EntryPointUrl.popup(context,new MyCmdEntryPoint(),props);
    </pre>
   *
   * @param context
   * @param entry
   * @param props
   */
  public static void popup(IClientContext context, ICmdEntryPoint entry, Properties props)
  {
    new EntryPointUrl(ENTRYPOINT_CMD,ClassUtil.getShortClassName(entry.getClass()),props).popup(context);
  }
  
  /**
   * Show the hands over EntryPoint.
   * <br>
   * <b>example:</b><br>
    <pre>
      Properties props=new Properties();
      props.setProperty("template",(String)name2templates.get(userSelection));
      props.setProperty("pkey",call.getStringValue("pkey"));
      EntryPointUrl.show(context,new MyGuiEntryPoint(),props, 300,400);
    </pre>
   *
   * @param context the curent working context
   * @param entry   an instance of the GuiEntryPoint
   * @param props   any properties which will be hands over to the entry point.
   * @param width   the width of the new screen
   * @param height  the height of the new screen
   */
  public static void popup(IClientContext context, IGuiEntryPoint entry, Properties props, int height, int width)
  {
    new EntryPointUrl(ENTRYPOINT_GUI,ClassUtil.getShortClassName(entry.getClass()),props).popup(context, height, width);
  }

  /**
   * Show the hands over EntryPoint.
   * <br>
   * <b>example:</b><br>
    <pre>
      Properties props=new Properties();
      props.setProperty("template",(String)name2templates.get(userSelection));
      props.setProperty("pkey",call.getStringValue("pkey"));
      EntryPointUrl.show(context,new MyGuiEntryPoint(),props);
    </pre>
   *
   * @param context the current working context
   * @param entry   an instance of the GuiEntryPoint
   * @param props   any properties which will be hands over to the entry point.
   */  
  public static void popup(IClientContext context, IGuiEntryPoint entry, Properties props)
  {
    new EntryPointUrl(ENTRYPOINT_GUI,ClassUtil.getShortClassName(entry.getClass()),props).popup(context);
  }
  
  /**
   * Open a new window with the handsover size and display the EntryPoint
   * 
   * @param context the current working context
   * @param width   the width of the new screen
   * @param height  the height of the new screen
   * @deprecated use the static method popup
   */
  public void popup(IClientContext context, int width, int height)
  {
    IUrlDialog dialog = context.createUrlDialog(toURL(context));
    dialog.enableNavigation(true);
    dialog.show(width,height);
  }
 
  /**
   * Open a new window and display the EntryPoint
   * 
   * @param context
   * @deprecated use the static method 'popup(...)'
   */
  public void popup(IClientContext context)
  {
    popup(context, 450,500);
  }
  
  /**
   * Use the current window and replace the content with the content of the EntryPoint.
   * 
   * @param context
   * @deprecated use the static method 'show(...)'
   */
  public void show(IClientContext context)
  {
    if(context instanceof ClientContext)
    {
      ClientContext c=(ClientContext)context;
      c.addAdditionalHTML("<script type=\"text/javascript\">top.location.href='"+toURL(context)+"';</script>");
    }
    else
      popup(context);
   }
  
  
  /**
   *
   * @deprecated This method will be protected in the next release.
   *               Use the <b>static</b> methods 'popup(...)' and 'show(...)' to handle entry points.
   *              
   */
  public String toURL(SessionContext context)
  {
    return toURL(context, true);
  }
  
  private String toURL(SessionContext context, boolean useVersion) 
  {
    String server;
    String params;
    try
    {
      //add the system internal parameter to the properties
      //
      props.setProperty("entry",entryName);
      if(!props.containsKey("user"))
        props.setProperty("user",context.getUser().getLoginId());
      
      props.setProperty("app",context.getApplicationDefinition().getName());
      if (useVersion)
        props.setProperty("version",context.getApplicationDefinition().getVersion().toString());
      
      if (context.getSession() instanceof HTTPClientSession)
        server = ((HTTPClientSession) context.getSession()).getBaseUrl();
      else
        server = "http://" + context.getSession().getHost() + "/" + Bootstrap.getApplicationName() + "/";
      
      params = "?";
      for(Iterator iter=props.keySet().iterator();iter.hasNext();)
      {
        String key=(String)iter.next();
        String value=props.getProperty(key);
//      params= params+key+"="+URLEncoder.encode(value);
        params= params+key+"="+UrlUtil.encode(value,"ISO-8859-1");
        if(iter.hasNext())
          params = params+"&";
      }
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
    return server+type+params;
  }
}
