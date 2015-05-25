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

package de.tif.jacob.screen.event;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDocument;

/**
 * Abstract event handler class for document fields. Derived implementations of
 * this event handler class have to be used to "hook" application-specific
 * business logic to document fields.
 * 
 * @author Andreas Herz
 */
public abstract class IDocumentFieldEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IDocumentFieldEventHandler.java,v 1.2 2008/12/01 21:06:35 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * Notfication if the user has upload a new document.
   * 
   * @param context
   *          The current client context
   * @param documentField
   *          The document field itself
   * @param doc 
   * @param docName 
   */
  public void afterUpload(IClientContext context, IDocument documentField, String docName, byte[] doc) throws Exception
  {
  }


  /**
   * Notification if the user has delete an existing document (clean).
   * 
   * @param context
   *          The current client context
   * @param documentField
   *          The document field itself
   */
  public void afterDelete(IClientContext context, IDocument documentField) throws Exception
  {
  }
}
