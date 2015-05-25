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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIICaptionProviderModel;


public class MoveGroupElementCommand extends Command
{
	final private UIFormElementModel element;
	private Rectangle oldConstraint;
	final private Rectangle newConstraint;
	
	public MoveGroupElementCommand(UIFormElementModel element, Rectangle newConstraint)
	{
    this.element = element;   
		this.newConstraint = newConstraint;
    this.oldConstraint = element.getConstraint();
	}
	
	public void execute()
	{
		element.setConstraint(newConstraint);
    // Falls das Element eine Caption hat, dann muss diese hinterher gezogen werden
    //
    if(element instanceof UIICaptionProviderModel)
    {
      UIICaptionProviderModel provider = (UIICaptionProviderModel)element;
      if(provider.getCastorCaption()==null || provider.getCaptionModel() == provider)
        return;
      Point oldPos = provider.getCaptionModel().getLocation();
      oldPos.x = oldPos.x + newConstraint.x-oldConstraint.x;
      oldPos.y = oldPos.y + newConstraint.y-oldConstraint.y;
      provider.getCaptionModel().setLocation(oldPos);
    }
	}
	
	
	public void undo()
	{
		element.setConstraint(oldConstraint);
	}
}
