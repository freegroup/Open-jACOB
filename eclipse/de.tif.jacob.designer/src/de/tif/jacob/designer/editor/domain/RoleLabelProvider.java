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

package de.tif.jacob.designer.editor.domain;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UserRoleModel;


/**
 * Label provider for the TableViewerExample
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
public class RoleLabelProvider extends LabelProvider	implements ITableLabelProvider 
{
  final UIDomainModel domainModel;
  public RoleLabelProvider(UIDomainModel domainModel)
  {
    this.domainModel = domainModel;
  }
  
	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) 
	{
		String result = "";
		UserRoleModel role = (UserRoleModel) element;
		switch (columnIndex) {
			case 0:  // COMPLETED_COLUMN
				break;
			case 1 :
				result = role.getName();
				break;
			default :
				break; 	
		}
		return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) 
	{
		if (columnIndex == 0)
		{
		  UserRoleModel role = (UserRoleModel) element;
	    return JacobDesigner.getImage(domainModel.hasRole(role)?"checked.gif":"unchecked.gif");
		}  
		return null;
	}

}
