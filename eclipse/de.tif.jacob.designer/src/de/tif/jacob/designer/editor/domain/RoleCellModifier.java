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
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UserRoleModel;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifes a cell in the 
 * tableViewer
 */

public class RoleCellModifier implements ICellModifier 
{
	private UIDomainModel domainModel;
	
	/**
	 * Constructor 
	 * @param TableViewerExample an instance of a TableViewerExample 
	 */
	public RoleCellModifier(UIDomainModel domainModel) 
	{
		super();
		this.domainModel = domainModel;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) 
	{
		return true;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) 
	{
		// Find the index of the column
		int columnIndex	= property==DomainPropertiesPage.COMPLETED_COLUMN?0:1;

		Object result = null;
		UserRoleModel role = (UserRoleModel) element;

		switch (columnIndex) {
			case 0 : // COMPLETED_COLUMN 
				result = new Boolean(domainModel.hasRole(role));
				break;
			case 1 : // DESCRIPTION_COLUMN 
				result = role.getName();
				break;
			default :
				result = "";
		}
		return result;	
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) 
	{	
		// Find the index of the column 
		int columnIndex	= property==DomainPropertiesPage.COMPLETED_COLUMN?0:1;
			
		TableItem item = (TableItem) element;
		UserRoleModel role = (UserRoleModel) item.getData();
		String valueString;

		switch (columnIndex) 
		{
			case 0 : // COMPLETED_COLUMN
			  boolean checked = ((Boolean) value).booleanValue();
			  if(checked)
			    domainModel.addElement(role);
			  else
			    domainModel.removeElement(role);
				break;
			case 1 : // DESCRIPTION_COLUMN 
				valueString = ((String) value).trim();
				role.setName(valueString);
				break;
			default :
			}
//		tableViewerExample.tableViewer.update(role,null);
		
	}
}
