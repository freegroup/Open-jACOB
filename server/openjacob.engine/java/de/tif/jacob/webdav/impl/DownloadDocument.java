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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.impl.HTTPClientSession;

/**
 * @author Andreas Herz
 *
 */
public class DownloadDocument extends HttpServlet
{
  static public final transient String RCS_ID = "$Id: DownloadDocument.java,v 1.2 2007/07/24 21:03:17 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public void service(HttpServletRequest request, HttpServletResponse response)
  {
		try
		{
      // Die session wird nicht direkt benötigt. Aber so kann man nicht einen
      // Link auf ein Dokument per eMail versenden, der Empfänger sieht das
      // Dokument, der Versender schliesst den Browser, Empänger wundert
      // sich warum das Dokument dann nicht mehr da ist.
      //
      // Das Dokument ist NUR für die eingeloggte Person welche es in eine Maske
      // zurükgefüllt hat sichtbar
      //
      // FREEGROUP: Ist das notwendig?
//      HTTPClientSession jacobSession= HTTPClientSession.get(request.getSession());
//      if(jacobSession==null)
//        return; // silently

      String id  = request.getParameter("documentId");
      if(id==null)
        return; // silently
      WebdavDocument document= WebdavDocumentManager.get(id);
      if(document==null)
        return; // silently
      response.setHeader("Content-Disposition", "attachment; filename=\""+document.getName()+"\"");
      response.getOutputStream().write(document.getContent());
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(e);
    }
    finally
    {
      Context.setCurrent(null);
    }
  }
}
