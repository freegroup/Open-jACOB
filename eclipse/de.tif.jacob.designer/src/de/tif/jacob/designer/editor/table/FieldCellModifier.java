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

package de.tif.jacob.designer.editor.table;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifes a cell in the 
 * tableViewer
 */

public class FieldCellModifier implements ICellModifier 
{
	
	/**
	 * Constructor 
	 * @param TableViewerExample an instance of a TableViewerExample 
	 */
	public FieldCellModifier() 
	{
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
		int columnIndex	= FieldMasterBlock.COLUMNS.indexOf(property);

		Object result = null;
		FieldModel field = (FieldModel) element;
		switch (columnIndex) {
			case 0 : // FieldName
				result = field.getName();
				break;
			case 1 : // Type
				result = field.getType();
				break;
			case 2 : // Required
				result = new Boolean(field.getRequired());
				break;
			case 3 : // Readonly
				result = new Boolean(field.getReadonly());
				break;
			case 4 : // History
				result = new Boolean(field.getHistory());
				break;
			case 5 : // Length
				result = field.getLengthAsString();
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
		int columnIndex	= FieldMasterBlock.COLUMNS.indexOf(property);
			
		TableItem item = (TableItem) element;
		FieldModel field = (FieldModel) item.getData();

		switch (columnIndex) 
		{
			case 0 : // FieldName
			  field.setName((String)value);
				break;
			case 1 : // Type
			  break;
			case 2 : // Required
			  field.setRequired(((Boolean)value).booleanValue());
				break;
			case 3 : // Readonly
			  field.setReadonly(((Boolean)value).booleanValue());
				break;
			case 4 : // History
			  field.setHistory(((Boolean)value).booleanValue());
				break;
			case 5 : // Length
			  break;
				default :
		}
		
	}
}
