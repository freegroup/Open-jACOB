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
package de.tif.jacob.designer.editor.relationset.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.relationset.JacobRelationsetEditor;
import de.tif.jacob.designer.editor.relationset.command.AddTableAliasCommand;
import de.tif.jacob.designer.editor.relationset.editpart.TableAliasEditPart;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.util.clazz.ClassUtil;

public final class AddRelatedTableAliasAction  implements IObjectActionDelegate 
{
  TableAliasModel model=null;
  RelationsetModel relationset;
  JacobRelationsetEditor editor;
  
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
	{
	  editor = ((JacobRelationsetEditor)targetPart);
	  relationset = editor.getRelationsetModel();
	}

	public void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
    {
      TableAliasEditPart editPart = (TableAliasEditPart)((IStructuredSelection)selection).getFirstElement();
      model = (TableAliasModel)editPart.getModel();
    }
    else
      model=null;
	}
	
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public final void run(IAction action)
  {
    long start = System.currentTimeMillis();
    try
    {
  		List aliases = new ArrayList();
      List relations = model.getJacobModel().getRelationModelsFrom(model);
      Iterator iter = relations.iterator();
      while (iter.hasNext())
      {
        RelationModel relation = (RelationModel) iter.next();
        if(!relationset.contains(relation.getToTableAlias()))
          aliases.add(relation.getToTableAlias());
      }
      relations = model.getJacobModel().getRelationModelsTo(model);
      iter = relations.iterator();
      while (iter.hasNext())
      {
        RelationModel relation = (RelationModel) iter.next();
        if(!relationset.contains(relation.getFromTableAlias()))
          aliases.add(relation.getFromTableAlias());
      }
      
      // Falls es nur ein Tabellenalias ist, dann kann dieser gleich eingefügt werden
      // ohnen einen Dialog anzuzeigen
      //
      if(aliases.size()==1)
      {
        editor.getCommandStack().execute(new AddTableAliasCommand(relationset,(TableAliasModel)aliases.get(0), new Point(30,30)));
      }
      else
      {
	      ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
	          {
	        public Image getImage(Object element)
	        {
	          return JacobDesigner.getImage(((ObjectModel) element).getImageName());
	        }
	
	        public String getText(Object element)
	        {
	          return ((TableAliasModel) element).getName();
	        }
	      });
	      dialog.setImage(JacobDesigner.getImage("tablealias.png"));
	      dialog.setTitle("Select table alias to add.");
	      dialog.setMessage("Select related table alias to add.");
	      dialog.setElements(aliases.toArray());
	      dialog.setMultipleSelection(true);
	      dialog.create();
	      if(dialog.open()==Window.OK)
	      {
	        Object[] results= dialog.getResult();
	        if(results!=null)
	        {
	          for (int i = 0; i < results.length; i++)
	          {
	            TableAliasModel alias=(TableAliasModel)results[i];
			        if(editor.getCommandStack()!=null)
			          editor.getCommandStack().execute(new AddTableAliasCommand(relationset, alias, new Point(30*i,30*i)));
			        else
			          new AddTableAliasCommand(relationset, alias, new Point(30*i,30*i)).execute();
	          }
	        }
	      }
      }
      
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
    finally
    {
      System.out.println(ClassUtil.getShortClassName(this.getClass())+" duration:"+(System.currentTimeMillis()-start));
    }
  }
}

