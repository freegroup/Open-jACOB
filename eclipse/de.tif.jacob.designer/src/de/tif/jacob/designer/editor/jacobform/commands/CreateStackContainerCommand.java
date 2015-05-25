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
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIDBForeignFieldModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIICaptionProviderModel;
import de.tif.jacob.designer.model.UIStackContainerModel;
import de.tif.jacob.designer.preferences.I18NPreferences;

public class CreateStackContainerCommand extends Command
{
	private UIGroupModel  newGroup;
	private UIGroupModel group;
	private Point location;
	private Dimension size;
	private UIStackContainerModel stack;
	
	public CreateStackContainerCommand(UIGroupModel group, UIGroupModel newGroup, Point location, Dimension size)
	{
		this.group    = group;
		this.location = location;
		this.size     = size;
		this.newGroup = newGroup;
	}
	
	public void execute()
	{
		if(size != null)
		{
		  if(size.height<=0)
		    size.height=20;
		  if(size.width<=0)
		    size.width=100;
		}
    ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
        {
          public Image getImage(Object element)
          {
            return ((TableAliasModel)element).getImage();
          }

          public String getText(Object element)
          {
            return ((TableAliasModel)element).getName()+"<"+((TableAliasModel)element).getTableModel().getName()+">";
          }
        });

    List x= group.getJacobModel().getTableAliasModels();
//		List x= group.getTableAliasModel().getFromLinkedTableAliases();
    dialog.setElements(x.toArray());
    dialog.setTitle("Select the table alias for the group");
    dialog.setMessage("Select the table alias for the group.");
    dialog.setMultipleSelection(false);
    dialog.create();
    if(dialog.open()==Window.OK && dialog.getFirstResult()!=null)
    {
      try
      {
	      TableAliasModel alias= (TableAliasModel)dialog.getFirstResult();
        // TODO:vernüftige Meldung ausgeben oder Browser anlegen
        //
        if(alias.getPossibleBrowserNames().size()==0)
          return;
        
        stack = new UIStackContainerModel();
	  		stack.setLocation(location);
				stack.setSize(size);
	  		stack.setJacobModel(group.getJacobModel());
	  		stack.setGroup(group);

        newGroup.setGroupContainerModel(stack);
        newGroup.setJacobModel(group.getJacobModel());
        newGroup.setTableAlias(alias.getName(),false);
        newGroup.setName(newGroup.getTableAlias()+"StackPane"+System.currentTimeMillis());
        newGroup.setLabel(newGroup.getName());
        List possibleBrowsers = newGroup.getJacobModel().getBrowserModels(newGroup.getTableAliasModel());
        
        newGroup.setBrowserModel((BrowserModel)possibleBrowsers.get(0));
        newGroup.getElements();
        stack.addElement(newGroup);

        String defaultName = stack.getDefaultName();
				String newName     = defaultName;
				int counter=2;
				while(group.getGroupContainerModel().isUIElementNameFree(newName)==false)
				{
				  newName = defaultName+counter;
				  counter++;
				}
				stack.setName(newName);
	  		group.addElement(stack);
      }
      catch(Exception ex)
      {
        JacobDesigner.showException(ex);
      }
    }
    else
    {
      stack=null;
    }
	}
	
	public void redo()
	{
	  if(stack!=null)
	    group.addElement(stack);
	}
	
	public void undo()
	{
	  if(stack!=null)
	    group.removeElement(stack);
	}

}
