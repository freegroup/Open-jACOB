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
package de.tif.jacob.designer.actions;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * 
 */
public abstract  class NewTableAliasAction extends NewTableAliasFromTableAction 
{
  
  /**
   * 
   * 
   * @return 
   */
  public abstract JacobModel getJacobModel();
  
  /**
   * 
   * 
   * @return 
   */
  public final TableModel getTableModel()
  {
    if (getJacobModel() != null)
    {
      ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
          {
        public Image getImage(Object element)
        {
          return JacobDesigner.getImage(((TableModel)element).getImageName());
        }

        public String getText(Object element)
        {
          return ((TableModel) element).getName();
        }
      });
      dialog.setElements(getJacobModel().getTableModels().toArray());
      
      dialog.create();
      if(dialog.open()==Window.OK)
      {
        return (TableModel)dialog.getFirstResult();
      }
    }
    return null;
  }
}

