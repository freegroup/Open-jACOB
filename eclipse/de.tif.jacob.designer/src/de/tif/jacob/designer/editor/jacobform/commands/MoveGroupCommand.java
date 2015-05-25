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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIFormGuideModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;


public class MoveGroupCommand extends Command
{
	final private UIGroupModel element;
	final private Rectangle oldConstraint;
	final private Rectangle newConstraint;
	
  private Map leftGuides;
  private Map topGuides;
  private Map rightGuides;
  private Map bottomGuides ;
  
	public MoveGroupCommand(UIGroupModel element, Rectangle newConstraint)
	{
		this.newConstraint = newConstraint;
    this.oldConstraint = element.getConstraint();
		this.element = element;		
	}
	
	public void execute()
	{
		element.setConstraint(newConstraint);
    
    // Man muss alle Elemente in der Gruppe von den Guides entkoppeln, da guide und Kindelemente
    // der Gruppe von der Position nicht mehr passen
    // Würde man den Guide danach bewegen würden sich dann Elemente welche nicht mehr optisch an
    // dem Guide hängen sich mit bewegen.
    //
    leftGuides   = new HashMap();
    topGuides    = new HashMap();
    rightGuides  = new HashMap();
    bottomGuides = new HashMap();

    Iterator iter = element.getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj = (UIGroupElementModel) iter.next();
      // alte guides merken
      //
      leftGuides.put(obj,obj.getLeftGuide());
      topGuides.put(obj,obj.getTopGuide());
      rightGuides.put(obj,obj.getRightGuide());
      bottomGuides.put(obj,obj.getBottomGuide());
      
      // Elemente entkoppeln
      //
      obj.setLeftGuide(null);
      obj.setTopGuide(null);
      obj.setRightGuide(null);
      obj.setBottomGuide(null);
    }
	}
	
	public void undo()
	{
		element.setConstraint(oldConstraint);
    
    // guides wieder an den Elemente restaurieren...wenn die Guides 
    // denn noch vorhanden sind
    //
    Iterator iter = leftGuides.keySet().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj   = (UIGroupElementModel) iter.next();
      UIFormGuideModel    guide = (UIFormGuideModel)leftGuides.get(obj); 
      if(guide!=null)
        guide.attachPart(obj,-1);
    }

    iter = topGuides.keySet().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj   = (UIGroupElementModel) iter.next();
      UIFormGuideModel    guide = (UIFormGuideModel)topGuides.get(obj); 
      if(guide!=null)
        guide.attachPart(obj,-1);
    }

    iter = rightGuides.keySet().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj   = (UIGroupElementModel) iter.next();
      UIFormGuideModel    guide = (UIFormGuideModel)rightGuides.get(obj); 
      if(guide!=null)
        guide.attachPart(obj,1);
    }

    iter = bottomGuides.keySet().iterator();
    while (iter.hasNext())
    {
      UIGroupElementModel obj   = (UIGroupElementModel) iter.next();
      UIFormGuideModel    guide = (UIFormGuideModel)bottomGuides.get(obj); 
      if(guide!=null)
        guide.attachPart(obj,1);
    }
  }
}
