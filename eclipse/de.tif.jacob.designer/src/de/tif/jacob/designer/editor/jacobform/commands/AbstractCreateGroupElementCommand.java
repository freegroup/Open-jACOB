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
/*
 * Created on 10.07.2006
 *
 */
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
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;

public abstract class AbstractCreateGroupElementCommand extends Command
{
  protected final UIGroupModel group;
  protected final Point location;
  protected final Dimension size;
  protected final UIGroupElementModel model;
  /**
   * @param group
   * @param location
   * @param size
   */
  public AbstractCreateGroupElementCommand(UIGroupModel group,UIGroupElementModel newModel, Point location, Dimension size)
  {
    if(size==null)
      size= new Dimension(-1,-1);
    this.group = group;
    this.location = location;
    this.size = size;
    this.model = newModel;
  }
  
  
  protected void fitLocationAndPosition()
  {
    // Berechnen eine "guten" Größe und Position für die frisch 
    // einzufügende Elemente wenn dem Command keine vernünftige Größe vorgeschrieben wurde.
    //
    if(this.size.height==-1 && this.size.width==-1)
    {
      // suche alle Objekte welche über dem Punkt liegt wo der Benutzer hingeklickt hat
      // und ermittle das Elemente welches am nächsten ist.
      //
      Rectangle upRect   = new Rectangle(location.x,0,1,location.y);
      Rectangle downRect = new Rectangle(location.x,location.y,1, Integer.MAX_VALUE/2);
      SortedMap upElements = new FastTreeMap();
      
      // Die Gruppe enthält bereits Elemente. Anhand der bestehenden Elemente wird
      // jetzt eine vernüftige Position und Größe ermittelt.
      //
      Iterator iter = group.getElements().iterator();
      
      List buttonLessElements = new ArrayList();
      List buttonElements = new ArrayList();
      while (iter.hasNext())
      {
        UIGroupElementModel element = (UIGroupElementModel) iter.next();
        if(element instanceof UIButtonModel)
          buttonElements.add(element);
        else
          buttonLessElements.add(element);
      }
      
      // Es wird anhand des einzufügenden Elementtypes (Input/Button) bestimmt welche 
      // Collection betrachtet werden muss
      //
      List relevantElements;
      if(model instanceof UIButtonModel)
        relevantElements = buttonElements;
      else
        relevantElements = buttonLessElements;
      
      // Falls es keine Eingabefelder in der Gruppe gibt wird das Elemente oben links 
      // positioniert.
      //
      if(relevantElements.size()==0)
      {
        location.x = ObjectModel.DEFAULT_ELEMENT_SPACING+ObjectModel.DEFAULT_CAPTION_WIDTH+ObjectModel.DEFAULT_CAPTION_SPACING;
        location.y = ObjectModel.DEFAULT_ELEMENT_FIRST_Y;
        size.height= model.getDefaultHeight();
        size.width = model.getDefaultWidth();
      }
      // Es wird untersucht ob sich oberhalb des Klick Elemente befinden an den man sich
      // orientieren kann
      //
      else
      {
        iter = relevantElements.iterator();
        while (iter.hasNext())
        {
          UIGroupElementModel element = (UIGroupElementModel) iter.next();
          if(element.getConstraint().intersects(upRect))
          {
            upElements.put(new Integer(location.y-element.getConstraint().y),element);
          }
        }
        // Ermittle alle Elemente welche unterhalb sind.
        //
        iter = relevantElements.iterator();
        while (iter.hasNext())
        {
          UIGroupElementModel element = (UIGroupElementModel) iter.next();
          // Falls unterhalb noch Elemente sind, kann leider kein Autolayout erfolgen.
          // Grund: Untere Elemente können ja nicht einfach weiter nach unten rutschen und
          //        es ist auch schwer zu beurteilen ob das neue Element zwischen den oberen und
          //        und den unteren Elemente dazwischen passt => lieber kein Autolayout.
          if(element.getConstraint().intersects(downRect))
          {
            upElements=new FastTreeMap();
            break;
          }
        }
        
        // Es wurden Elemente gefunden an dem man sich orientieren kann. Von diesen wird das
        // Elemente genommen welches am nächsten ist.
        if(upElements.size()>0)
        {
          UIGroupElementModel groupAbove = (UIGroupElementModel)upElements.get(upElements.firstKey());
          Rectangle aboveConstraint = groupAbove.getConstraint();
          this.location.x = aboveConstraint.x;
          this.location.y = aboveConstraint.y+aboveConstraint.height+ObjectModel.DEFAULT_GROUP_SPACING;
          this.size.width = aboveConstraint.width;
          this.size.height= model.getDefaultHeight();
        }
        else
        {
          // Es konnte kein Element gefunden werden an dem man sich orientieren kann.
          // Es wurde dem command auch keine vernünftige Größe des zu erzeugenden Elementes übergeben.
          // => Vernünftige Größe setzen.
          //
          size.height= model.getDefaultHeight();
          size.width = model.getDefaultWidth();
        }
      }
    }
  }
  
}
