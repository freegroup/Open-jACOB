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
package de.tif.jacob.designer.editor.jacobform.misc;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import de.tif.jacob.designer.JacobDesigner;


public class CheckboxPropertyDescriptor extends PropertyDescriptor
{
  /**
   * Creates an property descriptor with the given id, display name, and list
   * of value labels to display in the combo box cell editor.
   * 
   * @param id the id of the property
   * @param displayName the name to display for the property
   * @param valuesArray the list of possible values to display in the combo box
   */
  public CheckboxPropertyDescriptor(Object id, String displayName, String group) 
  {
      super(id, displayName);
      setCategory(group);
  }

  /**
   * The <code>ComboBoxPropertyDescriptor</code> implementation of this 
   * <code>IPropertyDescriptor</code> method creates and returns a new
   * <code>ComboBoxCellEditor</code>.
   * <p>
   * The editor is configured with the current validator if there is one.
   * </p>
   */
  public CellEditor createPropertyEditor(Composite parent) 
  {
      return new MyCheckboxCellEditor(parent);
  }
  
  public ILabelProvider getLabelProvider() 
  {
  	return new LabelProvider()
	  	{
	      public Image getImage(Object element) {
	      	if(element instanceof Boolean)
	      	{
	      		return JacobDesigner.getPlugin().getImage(((Boolean)element).booleanValue() ? "checked.gif" : "unchecked.gif");
	      	}
	      	return null;
	    }
	      public String getText (Object element)
	      {
	      	return null;
	      }
  	};
  }
}
