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

package de.tif.jacob.core.definition.guielements;

import java.util.ArrayList;
import java.util.List;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.JadSelectionActionDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InFormBrowserDefinition extends GUIElementDefinition
{
  static public final transient String RCS_ID = "$Id: InFormBrowserDefinition.java,v 1.7 2010/02/25 13:48:38 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  private final IBrowserDefinition browserToUse;
  private final boolean addingEnabled;
  private final boolean updatingEnabled;
  private final boolean deletingEnabled;
  private final boolean searchEnabled;
  private final List    selectionActions;
  
	public InFormBrowserDefinition(String name, String description, String eventHandler, Dimension dimension, 
                                 boolean visible, int tabIndex, int paneIndex, Caption caption, IBrowserDefinition browserToUse, 
                                 boolean addingEnabled, boolean updatingEnabled, 
                                 boolean deletingEnabled, boolean searchEnabled,
                                 SelectionActionEventHandler[] actions)
	{
		super(name, description, eventHandler, dimension, visible, tabIndex, paneIndex, caption,-1,null,null);
    this.browserToUse = browserToUse;
    this.addingEnabled = addingEnabled;
    this.updatingEnabled = updatingEnabled;
    this.deletingEnabled = deletingEnabled;
    this.searchEnabled = searchEnabled;
    this.selectionActions = new ArrayList();
    // fetch selection actions
    for (int i = 0; i < actions.length; i++)
    {
      SelectionActionEventHandler castorAction = actions[i];
      JadSelectionActionDefinition selectionActionDefinition = new JadSelectionActionDefinition(castorAction);
      if (castorAction.getPropertyCount() > 0)
        selectionActionDefinition.putCastorProperties(castorAction.getProperty());
      selectionActions.add(selectionActionDefinition);
    }
	}


	/**
	 * @return Returns the browserToUse.
	 */
	public IBrowserDefinition getBrowserToUse()
	{
		return browserToUse;
	}



	/**
	 * Checks whether deleting of entries (i.e. records) is enabled for this informbrowser.
	 * 
	 * @return <code>true</code> deleting is enabled, otherwise <code>false</code>.
	 */
  public boolean isDeletingEnabled()
  {
    return deletingEnabled;
  }

  /**
   * Checks whether search of entries (i.e. records) is enabled for this informbrowser.
   * 
   * @return <code>true</code> deleting is enabled, otherwise <code>false</code>.
   */
  public boolean isSearchEnabled()
  {
    return searchEnabled;
  }


  /**
   * Checks whether adding of entries (i.e. creating new records) is enabled for this informbrowser.
   * 
   * @return <code>true</code> adding is enabled, otherwise <code>false</code>.
   */
  public boolean isAddingEnabled()
	{
		return addingEnabled;
	}

  /**
   * Checks whether updating of entries (i.e. records) is enabled for this informbrowser.
   * 
   * @return <code>true</code> updating is enabled, otherwise <code>false</code>.
   */
  public boolean isUpdatingEnabled()
	{
		return updatingEnabled;
	}

  public List getSelectionActions()
  {
    return selectionActions;
  }

  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
    return factory.createInFormBrowser(app, parent,this); 
  }
  
  protected void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    InFormBrowser jacobInFormBrowser = new InFormBrowser();
    if (null != getCaption())
      jacobInFormBrowser.setCaption(getCaption().toJacob());
    jacobInFormBrowser.setDimension(getDimension().toJacob());
//    jacobInFormBrowser.setRelationToUse("<leiche>");
    jacobInFormBrowser.setBrowserToUse(getBrowserToUse().getName());
    jacobInFormBrowser.setNewMode(this.addingEnabled);
    jacobInFormBrowser.setUpdateMode(this.updatingEnabled);
    jacobInFormBrowser.setDeleteMode(this.deletingEnabled);
    jacobInFormBrowser.setSearchMode(this.searchEnabled);
    jacobGuiElement.getCastorGuiElementChoice().setInFormBrowser(jacobInFormBrowser);
  }
}
