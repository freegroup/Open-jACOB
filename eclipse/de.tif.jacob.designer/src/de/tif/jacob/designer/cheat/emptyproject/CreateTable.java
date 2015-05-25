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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.NewPhysicalDataModelAction;
import de.tif.jacob.designer.actions.NewTableAction;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.PhysicalDataModel;

public class CreateTable extends Action
{
  @Override
  public void run()
  {
    JacobModel jacobModel = JacobDesigner.getPlugin().getModel();
    if(jacobModel!=null)
    {
      PhysicalDataModel datasource = jacobModel.getDatasourceModels().get(0);
      if(datasource!=null)
      {
        NewTableAction action = new NewTableAction(datasource);
        action.run(this);
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
