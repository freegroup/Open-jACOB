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

package de.tif.jacob.screen.impl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.tif.jacob.core.Context;
import de.tif.jacob.screen.IImageProvider;


/**
 * @author Andreas Herz
 *
 */
public class AutosuggestProvider extends HttpServlet
{
  static public final transient String RCS_ID = "$Id: AutosuggestProvider.java,v 1.2 2007/07/24 21:03:15 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  static private final transient Log logger = LogFactory.getLog(AutosuggestProvider.class);
  
  
  public void service(HttpServletRequest request, HttpServletResponse response)
  {
    String browserId  = request.getParameter("browser");
    String providerId = request.getParameter("provider");
    
    try
    {
      HTTPClientSession jacobSession=HTTPClientSession.get(request);

      // Falls sich der Client abmeldet, kann es passieren, das die Menuleiste 'schneller' oder noch nicht fertig war seine
      // Bilder für die Outlookbar zu laden. Bei dem logout wird nun die session gel�scht und die Outlookbar kann
      // diese somit nicht finden. Kann man ignorieren.
      //
      if(jacobSession!=null)
      {  
          response.setHeader("Cache-Control", "no-cache");
          response.setHeader("Pragma", "no-cache");
          response.setDateHeader("Expires", 0);

          HTTPApplication app = (HTTPApplication)jacobSession.getApplication(browserId);
          
          HTTPClientContext context = jacobSession.createContext(jacobSession.getUser(),app, browserId);
          Context.setCurrent(context);
          
          IImageProvider image = (IImageProvider)app.getActiveDomain().getCurrentForm(context).findByName(providerId);

//          CopyUtils.copy(data,out);
      }
    }
    catch (Exception e)
    {
      logger.warn("Could not provide autosuggestion for '"+providerId+"'", e);
    }
  }
}

  