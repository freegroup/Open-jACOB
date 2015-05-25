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
package de.tif.jacob.designer.editor.jacobform.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;
import de.tif.jacob.designer.editor.jacobform.JacobFormEditor;
import de.tif.jacob.designer.editor.jacobform.commands.CreateGuideCommand;
import de.tif.jacob.designer.editor.jacobform.commands.DeleteGuideCommand;
import de.tif.jacob.designer.editor.jacobform.commands.MoveGuideCommand;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIFormGuideModel;
import de.tif.jacob.designer.model.UIJacobFormModel;

/**
 * @author Pratik Shah
 */
public class FormRulerProvider extends RulerProvider
{
  private UIJacobFormModel form;
  private boolean     horizontal;
  
  public FormRulerProvider(JacobFormEditor editor, boolean horizontal) 
  {
    this.form       = editor.getContents();
    this.horizontal = horizontal;
    
    this.form.addPropertyChangeListener(rulerListener);
    
    List guides = getGuides();
    for (int i = 0; i < guides.size(); i++)
    {
      ((UIFormGuideModel) guides.get(i)).addPropertyChangeListener(guideListener);
    }
  }
  
  private PropertyChangeListener rulerListener = new PropertyChangeListener()
  {
    public void propertyChange(PropertyChangeEvent evt)
    {
      UIFormGuideModel guide=null;
      if(evt.getPropertyName() == ObjectModel.PROPERTY_GUIDE_ADDED)
      {
        guide = (UIFormGuideModel) evt.getNewValue();
        guide.addPropertyChangeListener(guideListener);
      }
      else if(evt.getPropertyName() == ObjectModel.PROPERTY_GUIDE_REMOVED)
      {
        guide = (UIFormGuideModel) evt.getOldValue();
        guide.removePropertyChangeListener(guideListener);
      }
      for (int i = 0; guide!=null && i < listeners.size(); i++)
      {
        ((RulerChangeListener) listeners.get(i)).notifyGuideReparented(guide);
      }
    }
  };

  private PropertyChangeListener guideListener = new PropertyChangeListener()
  {
    public void propertyChange(PropertyChangeEvent evt)
    {
      if (evt.getPropertyName().equals(UIFormGuideModel.PROPERTY_CHILDREN))
      {
        for (int i = 0; i < listeners.size(); i++)
        {
          ((RulerChangeListener) listeners.get(i)).notifyPartAttachmentChanged(evt.getNewValue(), evt.getSource());
        }
      }
      else
      {
        for (int i = 0; i < listeners.size(); i++)
        {
          ((RulerChangeListener) listeners.get(i)).notifyGuideMoved(evt.getSource());
        }
      }
    }
  };


  public List getAttachedModelObjects(Object guide)
  {
    return new ArrayList(((UIFormGuideModel) guide).getParts());
  }

  public Command getCreateGuideCommand(int position)
  {
    return new CreateGuideCommand(form, this.horizontal, position);
  }

  public Command getDeleteGuideCommand(Object guide)
  {
    return new DeleteGuideCommand(form, (UIFormGuideModel) guide);
  }

  public Command getMoveGuideCommand(Object guide, int pDelta)
  {
    return new MoveGuideCommand((UIFormGuideModel) guide, pDelta);
  }

  public int[] getGuidePositions()
  {
    List guides = getGuides();
    int[] result = new int[guides.size()];
    for (int i = 0; i < guides.size(); i++)
    {
      result[i] = ((UIFormGuideModel) guides.get(i)).getPosition();
    }
    return result;
  }

  public Object getRuler()
  {
    return this;
  }

  public int getUnit()
  {
    return RulerProvider.UNIT_PIXELS;
  }

  public int getGuidePosition(Object guide)
  {
    return ((UIFormGuideModel) guide).getPosition();
  }

  public List getGuides()
  {
    if(horizontal)
      return form.getHorizontalGuides();
    else
      return form.getVerticalGuides();
  }
}