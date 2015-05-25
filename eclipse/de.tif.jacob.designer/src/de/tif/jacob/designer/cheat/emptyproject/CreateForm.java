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
 * Created on 28.09.2006
 *
 */
package de.tif.jacob.designer.cheat.emptyproject;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.NewDomainAction;
import de.tif.jacob.designer.actions.NewJacobFormAction;
import de.tif.jacob.designer.actions.NewLinkedJacobFormAction;
import de.tif.jacob.designer.actions.NewPhysicalDataModelAction;
import de.tif.jacob.designer.actions.NewTableAction;
import de.tif.jacob.designer.editor.jacobform.commands.CreateGroupCommand;
import de.tif.jacob.designer.model.ApplicationModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.PhysicalDataModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIIFormContainer;

public class CreateForm extends Action
{
  @Override
  public void run()
  {
    final JacobModel jacobModel = JacobDesigner.getPlugin().getModel();
    if(jacobModel!=null)
    {
      new NewLinkedJacobFormAction()
      {
        @Override
        public UIIFormContainer getFormContainerModel()
        {
          return jacobModel.getDomainModels().get(0);
        }
      }.run(this);
      
      // Zu der Form gleich eine Gruppe hinzufügen
      //
      UIJacobFormModel formModel = jacobModel.getJacobFormModels().get(0);
      if(formModel!=null)
      {
        CreateGroupCommand cmd = new CreateGroupCommand(jacobModel, formModel, new UIGroupModel(), new Point(10,10),null);
        cmd.setAliasModel(jacobModel.getTableAliasModels().get(0));
        cmd.execute();
      }
      else
      {
        notifyResult(false);
      }
    }
    else
    {
      notifyResult(false);
    }
  }
}
