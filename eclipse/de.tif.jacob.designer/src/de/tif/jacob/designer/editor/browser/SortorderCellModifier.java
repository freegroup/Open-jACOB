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

package de.tif.jacob.designer.editor.browser;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UserRoleModel;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifes a cell in the 
 * tableViewer
 */

public class SortorderCellModifier implements ICellModifier 
{
	private BrowserModel model;
	
	/**
	 * Constructor 
	 * @param TableViewerExample an instance of a TableViewerExample 
	 */
	public SortorderCellModifier(BrowserModel model) 
	{
		super();
		this.model = model;
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
	  
		Object result = "";
		BrowserFieldModel field = (BrowserFieldModel) element;

		if(property==ColumnMasterBlock.ORDER_COLUMN)
				result = field.getSortOrder();
				
		return result;

	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) 
	{	
		TableItem item = (TableItem) element;
		BrowserFieldModel field = (BrowserFieldModel) item.getData();

		if(property==ColumnMasterBlock.ORDER_COLUMN)
				field.setSortOrder((String)value);
		
	}
}
