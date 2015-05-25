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
 * Created on 07.07.2006
 *
 */
package de.tif.jacob.designer.model;

import java.rmi.server.UID;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

public class UIFormGuideModel extends ObjectModel
{
  public static final String PROPERTY_CHILDREN = "subparts changed"; //$NON-NLS-1$
  private Map map;

  private final String id;
  private final boolean horizontal;
  private final UIJacobFormModel parent;

  int position;
  
  public UIFormGuideModel(UIJacobFormModel form, String id, int position, boolean horizontal)
  {
    this.parent     = form;
    this.id         = id;
    this.position   = position;
    this.horizontal = horizontal;
  }

  public UIFormGuideModel(UIJacobFormModel form, int position, boolean horizontal)
  {
    this(form, horizontal?"HGUIDE_"+new UID().toString():"VGUIDE_"+new UID().toString(), position,horizontal);
  }
  
  public int getPosition()
  {
    return position;
  }

  public void setPosition(int position)
  {
    if(position == this.position)
      return;
    int save = this.position;
    int div  = position-save;
    this.position = position;
    parent.setCastorProperty(this.id, Integer.toString(position));
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED,new Integer(save),new Integer(position));
    
    // Alle Objekte welche an diesem Guide kleben werden mit verändert (Position oder Größe)
    Iterator iter = getParts().iterator();
    while (iter.hasNext()) 
    {
      UIGroupElementModel part = (UIGroupElementModel)iter.next();
      Rectangle box = part.getConstraint().getCopy();
      // der Ruler ist oben (horizontal)
      // Ein Verschieben eines Guide auf diesem Ruler bewegt die Elemente auf der X-Achse
      //
      if (isHorizontal()) 
      {
        // Element hat nur Links einen Guide und recht keinen => Element wird verschoben
        if(part.getLeftGuide()==this && part.getRightGuide()==null)
        {
          box.translate(div,0);
          part.setConstraint(box);
        }
        // Element hat links einen Guide und rechts einen  => Element wird verschoben und Größe angepasst
        else if(part.getLeftGuide()==this && part.getRightGuide()!=null)
        {
          box.resize(-div,0);   
          box.translate(div,0);
          part.setConstraint(box);
        }
        // Element wird an der rechten Seite verändert => Größe wird angepasst
        else if(part.getRightGuide()==this)
        {
          box.resize(div,0);
          part.setConstraint(box);
        }
      } 
      else
      {
        // Element hat nur oben einen Guide => Element wird verschoben
        if(part.getTopGuide()==this && part.getBottomGuide()==null)
        {
          box.translate(0,div);
          part.setConstraint(box);
        }
        // Element hat oben und unten einen Guide => Element wird verschoben und die Größe wird angepasst
        else if(part.getTopGuide()==this && part.getBottomGuide()!=null)
        {
          box.resize(0,-div);   
          box.translate(0,div);
          part.setConstraint(box);
        }
        // Element hat unten eine Guide und oben keinen => Größe wird verändert
        else if(part.getBottomGuide()==this)
        {
          box.resize(0,div);   
          part.setConstraint(box);
        }
      }
    }
  }

  public String getError()
  {
    return null;
  }

  public String getInfo()
  {
    return null;
  }

  public String getWarning()
  {
    return null;
  }

  public boolean isInUse()
  {
    return true;
  }

  public boolean isHorizontal()
  {
    return horizontal;
  }

  public String getId()
  {
    return id;
  }
  
  /**
   * Attaches the given part along the given edge to this guide. The
   * LogicSubpart is also updated to reflect this attachment.
   * 
   * @param groupElement
   *          The part that is to be attached to this guide; if the part is
   *          already attached, its alignment is updated
   * @param alignment
   *          -1 is left or top; 0, center; 1, right or bottom
   */
  public void attachPart(UIGroupElementModel groupElement, int alignment)
  {
    if (getMap().containsKey(groupElement) && getAlignment(groupElement) == alignment)
      return;

    getMap().put(groupElement, new Integer(alignment));

    if(isHorizontal())
    {
      if(alignment==-1)
        groupElement.setLeftGuide(this);
      else if(alignment==1)
        groupElement.setRightGuide(this);
    }
    else
    {
      if(alignment==-1)
        groupElement.setTopGuide(this);
      else if(alignment==1)
        groupElement.setBottomGuide(this);
    }

    firePropertyChange(PROPERTY_CHILDREN, null, groupElement);
  }
   
  /**
   * Detaches the given part from this guide. The LogicSubpart is also updated
   * to reflect this change.
   * 
   * @param groupElement
   *          the part that is to be detached from this guide
   */
  public void detachPart(UIGroupElementModel groupElement)
  {
    if (getMap().containsKey(groupElement))
    {
      getMap().remove(groupElement);
      if (groupElement.getLeftGuide()==this)
        groupElement.setLeftGuide(null);
      if (groupElement.getTopGuide()==this)
        groupElement.setTopGuide(null);
      if (groupElement.getRightGuide()==this)
        groupElement.setRightGuide(null);
      if (groupElement.getBottomGuide()==this)
        groupElement.setBottomGuide(null);
      firePropertyChange(PROPERTY_CHILDREN, null, groupElement);
    }
  }
   
  /**
   * This methods returns the edge along which the given part is attached to
   * this guide. This information is used by
   * {@link org.eclipse.gef.examples.logicdesigner.edit.LogicXYLayoutEditPolicy LogicXYLayoutEditPolicy}
   * to determine whether to attach or detach a part from a guide during resize
   * operations.
   * 
   * @param part
   *          The part whose alignment has to be found
   * @return an int representing the edge along which the given part is attached
   *         to this guide; 1 is bottom or right; 0, center; -1, top or left; -2
   *         if the part is not attached to this guide
   * @see org.eclipse.gef.examples.logicdesigner.edit.LogicXYLayoutEditPolicy#createChangeConstraintCommand(ChangeBoundsRequest,
   *      EditPart, Object)
   */
  public int getAlignment(UIGroupElementModel part)
  {
    if (getMap().get(part) != null)
      return ((Integer) getMap().get(part)).intValue();
    return -2;
  }
   

  /**
   * @return The Map containing all the parts attached to this guide, and their
   *         alignments; the keys are LogicSubparts and values are Integers
   */
  public Map getMap()
  {
    if (map == null)
    {
      map = new Hashtable();
    }
    return map;
  }

  /**
   * @return the set of all the parts attached to this guide; a set is used
   *         because a part can only be attached to a guide along one edge.
   */
  public Set getParts()
  {
    return getMap().keySet();
  }

  @Override
  public String getName()
  {
    return "<unused>";
  }

  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
  
  @Override
  public ObjectModel getParent()
  {
    return parent;
  }
}
