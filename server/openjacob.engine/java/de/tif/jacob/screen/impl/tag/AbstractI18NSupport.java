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

package de.tif.jacob.screen.impl.tag;

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.JspContext;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.html.ClientSession;

/**
 *
 */
public abstract class AbstractI18NSupport extends AbstractTagSupport
{
  public static transient final String RCS_ID = "$Id: AbstractI18NSupport.java,v 1.1 2009/07/30 23:16:01 ibissw Exp $";
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  protected String getLocalized(String id)
  {
    ServletRequest request = this.pageContext.getRequest();
    Context context = Context.getCurrent();
    // Eine Applikationspezifische JSP versucht das I18N Tag zu verwenden.
    // Da wird nicht das Core-I18N Resourcebundel verwendet sondern das von der Applikation
    //
    if(context instanceof JspContext)
    {
      // no client session existing so far (e.g. request to login page)
      // -> try to fetch locale from HTTP request, if existing
      //
      Locale locale = Locale.getDefault();
      if (request instanceof HttpServletRequest)
      {
        Locale httpLocale = ClientSession.getHttpLocale((HttpServletRequest) request);
        if (httpLocale != null)
          locale = httpLocale;
      }
      return de.tif.jacob.i18n.I18N.localizeLabel("%"+id, context,locale);
    }

    // a client session already exists?
    //
    HTTPClientSession clientSession = HTTPClientSession.get(this.pageContext.getSession(), request.getParameter("browser"));
    if (clientSession != null)
      return de.tif.jacob.i18n.I18N.getCoreLocalized(id, clientSession.getApplicationDefinition(), clientSession.getUser().getLocale());

    // no client session existing so far (e.g. request to login page)
    // -> try to fetch locale from HTTP request, if existing
    //
    if (request instanceof HttpServletRequest)
    {
      Locale locale = ClientSession.getHttpLocale((HttpServletRequest) request);
      if (locale != null)
        return de.tif.jacob.i18n.I18N.getCoreLocalized(id, (IApplicationDefinition) null, locale);
    }
    
    // What to do?
    // Ok, use default locale
    //
    return de.tif.jacob.i18n.I18N.getCoreLocalized(id, (IApplicationDefinition) null, Locale.getDefault());
  }
}