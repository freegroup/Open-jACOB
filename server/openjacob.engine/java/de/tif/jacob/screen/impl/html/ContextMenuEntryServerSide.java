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

import java.io.Writer;

import de.tif.jacob.core.definition.IContextMenuEntry;
import de.tif.jacob.core.definition.impl.ContextMenuEntryDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.util.StringUtil;


public class ContextMenuEntryServerSide  extends ContextMenuEntry
{
  static public final transient String RCS_ID = "$Id: ContextMenuEntryServerSide.java,v 1.3 2007/06/19 14:04:51 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final ContextMenuEntryDefinition definition;
  
  protected ContextMenuEntryServerSide(IApplication app, String labelId)
  {
    super(app, "",  labelId,  true, null, null);
    this.definition=null;
  }

  protected ContextMenuEntryServerSide(IApplication app,  IContextMenuEntry entry)
  {
    super(app, entry.getName(),  entry.getLabel(),  true, null, entry.getActionType());
    this.definition=(ContextMenuEntryDefinition)entry;
  }
  

	/* 
	 * @see de.tif.jacob.screen.GUIElement#renderHTML(java.io.Writer)
	 */
	public void calculateHTML(ClientContext c) throws Exception
	{
	  if(getCache()==null)
	  {  
	    Writer w = newCache();
	    w.write("<tr><td class='contextMenuItem'>");
	    if(isEnabled())
	    {
	    	w.write("<a class='contextMenuItem' href='#' onClick=\"");
	    	w.write("FireEvent('");
	    	w.write(Integer.toString(getId()));
	    	w.write("','click')\">");
		    w.write(StringUtil.htmlEncode(getI18NLabel(c.getLocale())));
		    w.write("</a>\n");
	    }
	    else
	    {
		    w.write("<a class='contextMenuItemDisabled'>");
		    w.write(StringUtil.htmlEncode(getI18NLabel(c.getLocale())));
		    w.write("</a>");
	    }
	    w.write("</td></tr>\n");
	  }
  }
	
	/**
	 * 
	 */
	public void resetCache()
	{
	  // the parent caches the HTML from the menu entries. If the child invalidate the parent must too.
	  // 
	  parent.resetCache();
	  super.resetCache();
	}
	
	public String getEventHandlerReference()
  {
    if(definition==null)
      return null;
    return definition.getEventHandler();
  }
}

