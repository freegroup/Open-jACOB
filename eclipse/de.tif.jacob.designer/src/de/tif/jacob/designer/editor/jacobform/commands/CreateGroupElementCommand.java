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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIICaptionProviderModel;
/**
 * Einfügen eines nicht DB UI-Elements
 *
 */
public class CreateGroupElementCommand extends AbstractCreateGroupElementCommand
{
	public CreateGroupElementCommand(UIGroupModel group, UIGroupElementModel model, Point location, Dimension size)
	{
    super(group,model,location,size);
	}
	
	public void execute()
	{
    fitLocationAndPosition();

    model.setLocation(location);
    model.setSize(size);
		model.setGroup(group);
    // die Caption des Elementes ebenfalls positioniert werden
    //
    if(model instanceof UIICaptionProviderModel)
      ((UIICaptionProviderModel)model).getCaptionModel().setConstraint(model.getDefaultCaptionConstraint(model.getConstraint()));
      
		group.addElement(model);
    try
    {
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
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
	}
	
	public void redo()
	{
		group.addElement(model);
	}
	
	public void undo()
	{
		group.removeElement(model);
	}

}
