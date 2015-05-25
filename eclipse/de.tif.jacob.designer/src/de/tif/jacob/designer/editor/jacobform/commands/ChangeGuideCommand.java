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

import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.UIFormGuideModel;
import de.tif.jacob.designer.model.UIGroupElementModel;


public class ChangeGuideCommand extends Command
{
  private UIGroupElementModel part;
  private final UIFormGuideModel oldGuide;
  private final UIFormGuideModel newGuide;

  private final int oldAlign;
  private final int newAlign;

 /*
  *   <LI><code>-1</code> indicates the left (or top) side should be attached.
  *   <LI><code> 0</code> indicates the center should be attached.
  *   <LI><code> 1</code> indicates the right (or bottom) side should be attached.
  */
  public ChangeGuideCommand(UIGroupElementModel part, UIFormGuideModel newGuide, int newAlign,  boolean horizontalGuide) 
  {
    super();
    this.part = part;
    this.newGuide = newGuide;
    this.newAlign = newAlign;
    if(horizontalGuide)
    {
      if(newAlign==-1)
        this.oldGuide = part.getLeftGuide();
      else if(newAlign==1)
        this.oldGuide = part.getRightGuide();
      else
        this.oldGuide=null;
    }
    else
    {
      if(newAlign==-1)
        this.oldGuide = part.getTopGuide();
      else if(newAlign==1)
        this.oldGuide = part.getBottomGuide();
      else
        this.oldGuide=null;
    }
    
    if (oldGuide != null)
      oldAlign = oldGuide.getAlignment(part);
    else
      oldAlign = 0;
  }

  protected void changeGuide(UIFormGuideModel oldGuide, UIFormGuideModel newGuide, int newAlignment)
  {
    if (oldGuide != null)
      oldGuide.detachPart(part);
    
    // You need to re-attach the part even if the oldGuide and the newGuide are
    // the same because the alignment could have changed
    if (newGuide != null)
      newGuide.attachPart(part, newAlignment);
  }

  public boolean canExecute()
  {
    return true;
  }

  public void execute()
  {
    changeGuide(oldGuide, newGuide, newAlign);
  }

  public void undo()
  {
    changeGuide(newGuide, oldGuide, oldAlign);
  }
}
