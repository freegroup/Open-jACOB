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

package de.tif.jacob.screen.impl.html;

import de.tif.jacob.screen.IApplication;




public class ContextMenuEntryPaste  extends ContextMenuEntryClientSide
{
  static public final transient String RCS_ID = "$Id: ContextMenuEntryPaste.java,v 1.1 2007/01/19 09:50:31 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  protected ContextMenuEntryPaste(IApplication app)
  {
    super(app, "%CONTEXTMENU_PASTE");
  }
 
  public String getJavaScriptEventHandlerName()
  {
    return "pasteFromClipboard";
  }
  
  public String getEventHandlerReference()
  {
    return null;
  }
  
}
