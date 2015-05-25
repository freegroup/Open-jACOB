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

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.BrowserAction;
import de.tif.jacob.screen.impl.BrowserActionClick;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ForeignFieldBrowser extends Browser
{
  static public final transient String RCS_ID = "$Id: ForeignFieldBrowser.java,v 1.4 2010/08/13 20:14:28 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

	/**
	 * @param name
	 * @param label
	 * @param isInvisibleString
	 */
	protected ForeignFieldBrowser(IApplication app,  IBrowserDefinition browser, ForeignField parent, IClientContext context, IDataBrowser data) throws Exception
	{
		super(app, browser, null);
		this.parent=parent;
		this.setData(context, data);
  }
  
  protected void initHeaderActions()
  {
    addHiddenAction(BrowserAction.ACTION_CLICK);           // The user clicks in a row. This is not a action with a icon
  }


  protected int getEmptyRowCount()
  {
    return 0;
  }


  /**
	 * Der ForeingFieldBrowser 'h�ngt' sich zwischen dem eigentlichen processieren des Event um zu gew�hrleisten,
	 * dass 'onSelect(...)' eines Hooks so fr�h wie m�glich ausgel�st wird.
	 * 
	 * Normalerweise wird dies in renderHTML erledigt, da dort 'getDisplayRecord(..)' aufgerufen wird (welche onSelect() des Hooks ruft),
	 * D.h. onSelect des Hooks wird eventuell erst aufgerufen wenn andere Elemente bereits Ihren Inhalt gerendert
	 * haben (im renderHTML-cycle). Man kann somit im 'onSelect' keine Elemente f�llen welche in der Reihenfolge VOR diesem
	 * ForeignField sind -> schlecht.
	 * 
	 * Folge: Falls das ForeingFiel durch ein anderen Hook gef�llt wird, wird onSelect erst im renderHTML ausgel�st, da
	 *        processEvent nur bei einer Benutzer interaktion ausgel�st wird.
	 */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(super.processEvent(context, guid,event,value))
    {
      // force the 'myHook.onSelect(..)' asap.
      ((ForeignField)this.parent).getSelectedRecord(context);
      return true;
    }
    return false;
  }
  
  /** 
   * Writes the HTML content to the stream
   * @param out
   */
  
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    writeCache(w);
    super.writeHTML(context,w);
  }
  
}
