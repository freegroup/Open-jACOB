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
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIICaptionProviderModel;

public class AddGroupElementCommand extends Command
{
  private UIGroupModel toGroup;
  private UIGroupElementModel element;
  private Point  oldPos;
  private Point  newPos;
  private Point  oldCaptionPos;
  boolean hasOldGroup;
  
  public AddGroupElementCommand(UIGroupModel toGroup, UIGroupElementModel element, Rectangle rect)
  {
    this.toGroup = toGroup;
    this.element = element;
    this.oldPos  = element.getLocation();
    this.newPos  = rect.getTopLeft();
    // ist TRUE wenn man das Element zwischen zwei groupen hin und her bewegt. Z.b. TabPane=>group
    this.hasOldGroup = element.getGroupModel()!=null;
  }

  public void execute()
  {
    toGroup.addElement(element);
    element.setLocation(newPos);

    // Falls das Element eine Caption hat, dann muss diese angepasst werden gezogen werden
    //
    if(element instanceof UIICaptionProviderModel && hasOldGroup)
    {
      UIICaptionProviderModel provider = (UIICaptionProviderModel)element;
      if(provider.getCastorCaption()==null || provider.getCaptionModel() == provider)
        return;
      
      oldCaptionPos =provider.getCaptionModel().getLocation();
      Point captionPos = new Point(oldCaptionPos);
      captionPos.x = captionPos.x + newPos.x-oldPos.x;
      captionPos.y = captionPos.y + newPos.y-oldPos.y;
      provider.getCaptionModel().setLocation(captionPos);
    }
 }

  public void redo()
  {
    execute();
  }

  public void undo()
  {
    toGroup.removeElement(element);
    element.setLocation(oldPos);
    if(element instanceof UIICaptionProviderModel && hasOldGroup)
    {
      UIICaptionProviderModel provider = (UIICaptionProviderModel)element;
      if(provider.getCastorCaption()==null || provider.getCaptionModel() == provider)
        return;
      provider.getCaptionModel().setLocation(oldCaptionPos);
    }
  }
}
