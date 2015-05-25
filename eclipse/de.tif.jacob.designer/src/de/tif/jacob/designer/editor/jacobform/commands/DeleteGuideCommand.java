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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.UIFormGuideModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIGroupElementModel;

/**
 * @author Pratik Shah
 */
public class DeleteGuideCommand extends Command
{
  private UIJacobFormModel form;
  private UIFormGuideModel guide;
  private Map oldParts;

  public DeleteGuideCommand(UIJacobFormModel form, UIFormGuideModel guide) 
  {
    super("%delete_guid_command");
    this.guide = guide;
    this.form  = form;
  }

  public boolean canUndo()
  {
    return true;
  }

  public void execute()
  {
    oldParts = new HashMap(guide.getMap());
    Iterator iter = oldParts.keySet().iterator();
    while (iter.hasNext())
    {
      guide.detachPart((UIGroupElementModel) iter.next());
    }
    form.removeElement(guide);
  }

  public void undo()
  {
    Iterator iter = oldParts.keySet().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel part = (UIGroupElementModel) iter.next();
      guide.attachPart(part, ((Integer) oldParts.get(part)).intValue());
    }
    form.addElement(guide);
  }
}
