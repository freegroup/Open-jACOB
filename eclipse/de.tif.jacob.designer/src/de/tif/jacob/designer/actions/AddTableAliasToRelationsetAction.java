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
package de.tif.jacob.designer.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.relationset.command.AddTableAliasCommand;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * 
 */
public abstract class AddTableAliasToRelationsetAction  implements IObjectActionDelegate 
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract RelationsetModel getRelationsetModel();
  
  /*
   * return the command stack if you want a undo/redo function or null
   * 
   */
  /**
   * 
   * 
   * @return 
   */
  public CommandStack getCommandStack()
  {
    return null;
  }
  
	/**
	 * 
	 * 
	 * @param action 
	 * @param targetPart 
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
	{
	}

	/**
	 * 
	 * 
	 * @param action 
	 * @param selection 
	 */
	public void selectionChanged(IAction action, ISelection selection) 
	{
	}
	
	/**
	 * 
	 * 
	 * @param action 
	 */
	public final void run(IAction action)
  {
    try
    {
      
  		List result = new ArrayList();
      List aliases = getRelationsetModel().getJacobModel().getTableAliasModels();
      Iterator iter = aliases.iterator();
      while (iter.hasNext())
      {
        TableAliasModel alias = (TableAliasModel) iter.next();
        if(!getRelationsetModel().contains(alias))
          result.add(alias);
      }
      
      ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
          {
        public Image getImage(Object element)
        {
          return JacobDesigner.getImage(((TableAliasModel)element).getImageName());
        }

        public String getText(Object element)
        {
          return ((TableAliasModel) element).getName();
        }
      });
      dialog.setImage(JacobDesigner.getImage("tablealias.png"));
      dialog.setTitle("Select table alias to add.");
      dialog.setMessage("Select table alias to add.");
      dialog.setElements(result.toArray());
      dialog.setMultipleSelection(true);
      dialog.create();
      if(dialog.open()==Window.OK)
      {
        Object[] results= dialog.getResult();
        if(result!=null)
        {
          for (int i = 0; i < results.length; i++)
          {
            TableAliasModel alias=(TableAliasModel)results[i];
		        if(getCommandStack()!=null)
		          getCommandStack().execute(new AddTableAliasCommand(getRelationsetModel(), alias, new Point(0,0)));
		        else
		          new AddTableAliasCommand(getRelationsetModel(), alias, new Point(0,0)).execute();
          }
        }
      }      
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
  }
}
