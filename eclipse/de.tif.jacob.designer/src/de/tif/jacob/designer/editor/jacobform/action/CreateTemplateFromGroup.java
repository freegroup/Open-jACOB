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
package de.tif.jacob.designer.editor.jacobform.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.axis.utils.BeanUtils;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.PrintIFigureAction;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupEditPart;
import de.tif.jacob.designer.model.AbstractActionEmitterModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIGroupModel;

public final class CreateTemplateFromGroup implements IObjectActionDelegate
{
  private UIGroupModel groupModel;
  
  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
  }

  /**
   * @see IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection)
  {
    if (((IStructuredSelection) selection).getFirstElement() != null)
      groupModel = ((GroupEditPart) ((IStructuredSelection) selection).getFirstElement()).getGroupModel();
    else
      groupModel = null;
  }
  
  
  public void run(IAction action)
  {
    Rectangle boundingRect = groupModel.getButtonBoundingRect();
    
    if(boundingRect==null)
      return;

    // altes template löschen
    //
    resetOldSettings();
    
    // prüfen ob die Buttons näher an der linken oder rechten Kante der Gruppe
    // liegen
    //
    int distLeft  = boundingRect.x-groupModel.getConstraint().x;
    int distRight = (groupModel.getConstraint().x+groupModel.getConstraint().width)-(boundingRect.x+boundingRect.width);
    boolean alignLeft= distLeft<distRight;

    // prüfen ob die Buttons näher an der oberen oder unteren Kante liegen
    //
    int distTop    = boundingRect.y-groupModel.getConstraint().y;
    int distBottom = (groupModel.getConstraint().y+groupModel.getConstraint().height)-(boundingRect.y+boundingRect.height);
    boolean alignTop= distTop<distBottom;
    
    // Ausrichtungskanten im Plugin preference store speichern
    //
    JacobDesigner.setPluginProperty(JacobDesigner.TEMPLATE_BUTTON_LEFT, alignLeft);
    JacobDesigner.setPluginProperty(JacobDesigner.TEMPLATE_BUTTON_TOP,  alignTop);
    JacobDesigner.setPluginProperty(JacobDesigner.TEMPLATE_USAGE,  true);

    // Jetzt muss für die Standarbuttons bestimmt werden wie diese relativ zu der
    // ermittelten Ausrichtungskanten liegen.
    // (z.B. ButtonNew liegt immer [20,50] relative zu der linken oberen Kante) 
    //
    Iterator iter = groupModel.getStandardButtons().iterator();
    while (iter.hasNext())
    {
      UIButtonModel obj = (UIButtonModel) iter.next();
      int x = obj.getLocation().x;
      int y = obj.getLocation().y;
      if(alignTop)
        JacobDesigner.setPluginProperty("BUTTON_"+obj.getAction()+"_Y",Integer.toString(y));
      else
        JacobDesigner.setPluginProperty("BUTTON_"+obj.getAction()+"_Y",Integer.toString(groupModel.getSize().height-y));
        
      if(alignLeft)
        JacobDesigner.setPluginProperty("BUTTON_"+obj.getAction()+"_X",Integer.toString(x));
      else
        JacobDesigner.setPluginProperty("BUTTON_"+obj.getAction()+"_X",Integer.toString(groupModel.getSize().width-x));
    }
  }

  private void resetOldSettings()
  {
    Iterator iter = AbstractActionEmitterModel.FAVOUR_ACTIONS.iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      JacobDesigner.removePluginProperty("BUTTON_"+key+"_Y");
      JacobDesigner.removePluginProperty("BUTTON_"+key+"_X");
    }
    JacobDesigner.removePluginProperty(JacobDesigner.TEMPLATE_BUTTON_LEFT);
    JacobDesigner.removePluginProperty(JacobDesigner.TEMPLATE_BUTTON_TOP);
    JacobDesigner.removePluginProperty(JacobDesigner.TEMPLATE_USAGE);
  }


}

