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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import org.apache.commons.collections.FastTreeMap;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.NewTableFieldAction;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableModel;
import de.tif.jacob.designer.model.UIDBLocalInputFieldModel;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIICaptionProviderModel;
import de.tif.jacob.util.StringUtil;

public class CreateGroupDBElementCommand extends AbstractCreateGroupElementCommand
{
	private UIDBLocalInputFieldModel  templateModel;
	private List<UIDBLocalInputFieldModel>      newModels=new ArrayList<UIDBLocalInputFieldModel>();
	private FieldModel fieldModel;
  
  private String    NEW_FIELD_LABEL = "<<new>>";
  
	public CreateGroupDBElementCommand(UIGroupModel group, UIDBLocalInputFieldModel model, Point location, Dimension size)
	{
    super(group,(UIGroupElementModel)model, location,size);
		this.templateModel    = model;
	}
	
  public boolean canUndo()
  {
    return newModels.size()>0;
  }
  
	public void execute()
	{
    try
    {
      ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
      {
        public Image getImage(Object element)
        {
          return null;
        }

        public String getText(Object element)
        {
          if(element instanceof String)
            return (String)element;
          FieldModel model = (FieldModel)element;
          if(StringUtil.toSaveString(model.getDescription()).length()>0)
              return model.getName() + " ("+model.getDescription()+")";
          return model.getName();
        }
      });
      List names = new ArrayList();
      names.addAll(group.getTableAliasModel().getFields(templateModel));
      names.add(NEW_FIELD_LABEL);
      dialog.setElements(names.toArray());
      dialog.setTitle("Select the table columns for the GUI element");
      dialog.setMessage("Select the table columns for the GUI element.");
      dialog.setMultipleSelection(true);

      dialog.create();
      
      if(dialog.open()==Window.OK)
      {
        fitLocationAndPosition();
        
        Object[] fields= dialog.getResult();
        // Falls der Benutzer mehr als ein Feld ausgewählt hat, werden diese jetzt alle
        // in die Gruppe eingefügt.
        //
        if(fields.length>1)
        {
          for (int i = 1; i < fields.length; i++)
          {
            String field =null;
            if(fields[i] instanceof String)
              field = (String)fields[i];
            else
              field = ((FieldModel)fields[i]).getName();
            UIDBLocalInputFieldModel model = (UIDBLocalInputFieldModel)templateModel.getClass().newInstance();
            model.setSize(size);
            createElement(location.getTranslated(0,(model.getSize().height+ObjectModel.DEFAULT_ELEMENT_SPACING)*i), size,model,group,field,newModels);
          }
        }

        templateModel.setSize(size);
        if(fields[0] instanceof String)
          createElement(location, size,templateModel,group,(String)fields[0],newModels);
        else
          createElement(location, size,templateModel,group,((FieldModel)fields[0]).getName(),newModels);
      }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
	}
	
	private void createElement(Point loc, Dimension size,UIDBLocalInputFieldModel model,final UIGroupModel group,String field, List container )
	{
    // Falls der Benutzer ein neues Feld einfügt und noch kein Datenbankfeld
    // vorhanden ist, dann wird dieses jetzt angelegt
    //
    if(field == NEW_FIELD_LABEL)
    {
      NewTableFieldAction action = new NewTableFieldAction()
      {
        @Override
        public TableModel getTableModel()
        {
          return group.getTableAliasModel().getTableModel();
        }
      };
      action.run(null);
      fieldModel = action.getCreatedTableField();
      if(fieldModel==null)
        return;
      fieldModel.setType(model.getDefaultDbType());
      field = fieldModel.getName();
    }
    
	  try
	  {
	    model.setLocation(loc);
			model.setGroup(group);
			model.setField(field);
      // die Caption des Elementes ebenfalls positioniert werden
      //
      if(model instanceof UIICaptionProviderModel)
        ((UIICaptionProviderModel)model).getCaptionModel().setConstraint(model.getDefaultCaptionConstraint(model.getConstraint()));

      if(model instanceof UIICaptionProviderModel)
			{
			  UIICaptionProviderModel captionProvider = (UIICaptionProviderModel)model;
			  captionProvider.getCaptionModel().setCaption(captionProvider.getDefaultCaption());
			}
		  group.addElement(model);
			newModels.add(model);
      String defaultName = model.getDefaultName();
      String newName     = defaultName;
      int counter=2;
      while(group.getGroupContainerModel().isUIElementNameFree(newName)==false)
      {
        newName = defaultName+counter;
        counter++;
      }
      model.setName(newName);
	  }
	  catch(Exception e)
	  {
	     JacobDesigner.showException(e);
	  }
	}
	
	public void redo()
	{
    if(fieldModel!=null)
      fieldModel.getTableModel().addElement(fieldModel);

    Iterator iter = newModels.iterator();
	  while (iter.hasNext())
    {
      UIDBLocalInputFieldModel obj = (UIDBLocalInputFieldModel) iter.next();
  		group.addElement(obj);
    }
	}
	
	public void undo()
	{
	  Iterator iter = newModels.iterator();
	  while (iter.hasNext())
    {
      UIDBLocalInputFieldModel obj = (UIDBLocalInputFieldModel) iter.next();
  		group.removeElement(obj);
    }
    if(fieldModel!=null)
      fieldModel.getTableModel().removeElement(fieldModel);
	}
}
