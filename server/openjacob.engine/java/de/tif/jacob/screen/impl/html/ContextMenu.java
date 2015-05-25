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

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.tif.jacob.core.definition.IContextMenuEntry;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IContextMenu;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ContextMenu extends GuiHtmlElement implements IContextMenu
{
  static public final transient String RCS_ID = "$Id: ContextMenu.java,v 1.5 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  private final String javaScriptEventHandler;
  
  protected ContextMenu(IApplication app, List entries)
  {
    this(app);
    if(entries.size()>0)
    {
      for (Iterator iter = entries.iterator(); iter.hasNext();)
  		{
  			IContextMenuEntry entry = (IContextMenuEntry) iter.next();
        addChild(new ContextMenuEntryServerSide(app, entry));
      }
      addChild(new ContextMenuEntrySeperator(app));
      addChild(new ContextMenuEntryCopy(app));
      addChild(new ContextMenuEntryPaste(app));
    }
    else
    {
      setVisible(false);
    }
    
  }

  /**
   * Create a generic ContextMenu
   *
   */
  protected ContextMenu(IApplication app)
  {
    super(app, "contextmenu","label",true, null, Collections.EMPTY_MAP);
    
    javaScriptEventHandler = "showContextMenu('contextmenu_"+Integer.toString(getId())+"');return false;";
  }

  protected String getContextMenuFunction()
  {
    if(isVisible())
      return javaScriptEventHandler;
    return "";
  }
  
  /**
   * return the HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext c) throws Exception
  {
    if(!isVisible())
      return;
    
    // no context menu entries. generate empty eventhandler for the corresponding parent
    //
    if(getCache()==null)
    {  
      Writer w = newCache();
      if(!getChildren().isEmpty())
	    {
	      // Element berechnen
	      Iterator iter=getChildren().iterator();
	      while(iter.hasNext())
	      {
	        ContextMenuEntry element = (ContextMenuEntry) iter.next();
	        if(element.isVisible())
	        {  
	          element.calculateHTML(c);
	        }
	      }
	      
	      // HTML der Contextmenues eintragen
	      //
	      w.write("<div class=\"contextMenu\" style=\"position:absolute;display:none;\" id='contextmenu_"+Integer.toString(getId())+"'>");
	      w.write("<table  cellspacing=\"0\" cellpadding=\"0\" class=\"contextMenu\">");
	      iter=getChildren().iterator();
	      while(iter.hasNext())
	      {
	        ContextMenuEntry element = (ContextMenuEntry) iter.next();
	        if(element.isVisible())
	        {  
	          element.writeCache(w);
	          break;
	        }
	      }
	      // folge elemente rausschreiben
	      while(iter.hasNext())
	      {
	        ContextMenuEntry element = (ContextMenuEntry) iter.next();
	        if(element.isVisible())
	        {  
	          element.writeCache(w);
	        }
	      }
	      
	      w.write("</table>");
	      w.write("</div>");
	    }
    }
  }
  
  /** 
   * Writes the HTML content to the stream
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    
    if (getCache() != null)
      context.addContextmenuAdditionalHTML(new String(getCache().toCharArray()));
  }
  
  /**
   * 
   */
  protected void addDataFields(Vector fields)
  {
    
  }
  
  public String getEventHandlerReference()
  {
    return null;
  }
}
