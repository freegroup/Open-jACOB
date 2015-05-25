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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.Session;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas Herz
 *
 */
public class CmdEntryPointContext extends SessionContext
{
  static public final transient String RCS_ID = "$Id: CmdEntryPointContext.java,v 1.6 2010/08/19 09:33:22 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  private OutputStream stream;
  final Session      session;
  private HttpServletResponse response;
  public CmdEntryPointContext(Session session,HttpServletResponse response, IApplicationDefinition app, IUser user)
  {
    super(app,user);
   this.session=session;
    this.response=response;
  }
  

  public Session getSession()
  {
    return session;
  }


  public void canAbort(boolean flag)
  {
    // ignore. A EntrypointContext didn't provide a abort feature
  }

  public boolean shouldAbort()
  {
    return false;
  }

  /**
   * There is no session for a CmdEntryPointContext. The CmdEntryPointContext is 
   * only valid for one request. Redirect the registration to the 
   * <code>registerForRequest(...)</code> life cycle handler.<br>
   * <br>
   * 
   * Call Context.getCurrent().unregister(..) if you have destroyed the resource
   * manually. If you don't call it, the resource will be released twice. 
   * 
   * @param resource The resource to manage.
   */
  public void registerForSession(ManagedResource resource)
  {
    registerForRequest(resource);
  }

  /**
   * There is no window for a CmdEntryPointContext. The CmdEntryPointContext is 
   * only valid for one request. Redirect the registration to the 
   * <code>registerForRequest(...)</code> life cycle handler.<br>
   * <br>
   * 
   * Call Context.getCurrent().unregister(..) if you have destroyed the resource
   * manually. If you don't call it, the resource will be released twice. 
   * 
   * @param resource The resource to manage.
   */
  public void registerForWindow(ManagedResource resource)
  {
    registerForRequest(resource);
  }
  
  
  /**
   * There is no session for a CmdEntryPointContext. The CmdEntryPointContext is 
   * only valid for one request. Redirect the property to the 
   * <code>setPropertyForRequest(key,value)</code> life cycle handler.<br>
   * 
   * Für jeden Aufruf eines Entrypoints wird eine neue Session angelegt, da der Client
   * eventuell keine Cookies unterstützt (z.b. ein Command line tool)
   */
  public void setPropertyForSession(Object key, Object value)
  {
    setPropertyForRequest(key,value);
  }
  
  /**
   * There is no window for a CmdEntryPointContext. The CmdEntryPointContext is 
   * only valid for one request. Redirect the property to the 
   * <code>setPropertyForRequest(key,value)</code> life cycle handler.<br>
   * <br>
   */
  public void setPropertyForWindow(Object key, Object value)
  {
    setPropertyForRequest(key,value);
  }
  
  /**
   * Return the outputstream of the request. Stream the content (XML, PDF,...) to this 
   * OutputStream.
   * 
   * @return Returns the stream.
   */
  public final OutputStream getStream()
  {
    try
    {
      if(this.stream==null)
        this.stream = this.response.getOutputStream();
      return stream;
    }
    catch (IOException e)
    {
      de.tif.jacob.core.exception.ExceptionHandler.handle(e);
      throw new RuntimeException(e);
     }
  }
  
  /**
   * Set the filename of the returned binary/text document.
   * 
   * @param name
   * @since 2.10
   */
  public final void setContentName(String name) 
  {
    if(this.stream!=null)
      throw new IllegalStateException("Unable to modifiy the content name after the method call { CmdEntryPointContext.getStream() }");
    
    this.response.setHeader( "Content-Disposition", "attachment; filename="+name);
  }

  
  /**
   * Set the mimetype of the returned binary/text document.
   * 
   * @param mimeType
   * @since 2.10
   */
  public final void setContentType(String mimeType)
  {
    if(this.stream!=null)
      throw new IllegalStateException("Unable to modifiy the content type after the method call { CmdEntryPointContext.getStream() }");
 
    this.response.setContentType(mimeType);
  }

}
