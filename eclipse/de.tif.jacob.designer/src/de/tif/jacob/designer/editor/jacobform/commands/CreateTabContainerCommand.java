/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
package de.tif.jacob.designer.editor.jacobform.commands;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabContainerModel;
import de.tif.jacob.designer.preferences.I18NPreferences;

public class CreateTabContainerCommand extends CreateGroupElementCommand
{
  private final UITabContainerModel container;
  
	public CreateTabContainerCommand(UIGroupModel group, UITabContainerModel model, Point location, Dimension size)
	{
    super(group,model,location,size);
    this.container = model;
	}
	
	public void execute()
	{
    super.execute();
  
    try
    {
      // initial an den TabContainer einen Tab einfügen
      //
      UIGroupModel newGroup = new   UIGroupModel();
      newGroup.setGroupContainerModel(container.getPane());
      newGroup.setJacobModel(container.getJacobModel());
      newGroup.setTableAlias(container.getGroupTableAlias(),false);
      if(container.getJacobModel().useI18N())
        newGroup.setLabel("%GROUP"+container.getJacobModel().getSeparator()+(newGroup.getTableAlias().toUpperCase()));
      else
        newGroup.setLabel(StringUtils.capitalise(newGroup.getTableAlias()));
      newGroup.setName(newGroup.getTableAlias()+"Group");
      
      List possibleBrowsers = newGroup.getJacobModel().getBrowserModels(newGroup.getTableAliasModel());
      
      newGroup.setBrowserModel((BrowserModel)possibleBrowsers.get(0));
      newGroup.getElements();
      container.addPane(newGroup, 0);
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
	}
}
