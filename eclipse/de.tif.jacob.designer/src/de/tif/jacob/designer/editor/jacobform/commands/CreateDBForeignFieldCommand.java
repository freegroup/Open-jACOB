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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIDBForeignFieldModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIICaptionProviderModel;
import de.tif.jacob.designer.preferences.I18NPreferences;

public class CreateDBForeignFieldCommand extends Command
{
	private UIDBForeignFieldModel  foreign;
	private UIGroupModel group;
	private Point location;
	private Dimension size;
	
	
	public CreateDBForeignFieldCommand(UIGroupModel group, UIDBForeignFieldModel foreign, Point location, Dimension size)
	{
		this.group    = group;
		this.location = location;
		this.size     = size;
		this.foreign  = foreign;
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

		List x= group.getTableAliasModel().getFromLinkedTableAliases();
    dialog.setElements(x.toArray());
    dialog.setTitle("Select the table alias for the GUI element");
    dialog.setMessage("Select the table alias for the GUI element.");
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
        
	  		foreign.setLocation(location);
				foreign.setSize(size);
	  		foreign.setJacobModel(group.getJacobModel());
	  		foreign.setGroup(group);
        foreign.setForeignTableAlias(alias.getName());
        // die Caption des Elementes ebenfalls positioniert werden
        //
        if(foreign instanceof UIICaptionProviderModel)
          ((UIICaptionProviderModel)foreign).getCaptionModel().setConstraint(foreign.getDefaultCaptionConstraint(foreign.getConstraint()));

        String defaultName = foreign.getDefaultName();
				String newName     = defaultName;
				int counter=2;
				while(group.getGroupContainerModel().isUIElementNameFree(newName)==false)
				{
				  newName = defaultName+counter;
				  counter++;
				}
				foreign.setName(newName);
				if(group.getJacobModel().useI18N())
				{
					foreign.setCaption("%"+foreign.suggestI18NKey());
					if(!group.getJacobModel().hasI18NKey(foreign.suggestI18NKey()))
					  group.getJacobModel().addI18N(foreign.suggestI18NKey(),foreign.getDefaultCaption());
				}
				else
				{
					foreign.setCaption(foreign.getDefaultCaption());
				}
	  		group.addElement(foreign);
      }
      catch(Exception ex)
      {
        JacobDesigner.showException(ex);
      }
    }
    else
    {
      foreign=null;
    }
	}
	
	public void redo()
	{
	  if(foreign!=null)
	    group.addElement(foreign);
	}
	
	public void undo()
	{
	  if(foreign!=null)
	    group.removeElement(foreign);
	}

}
