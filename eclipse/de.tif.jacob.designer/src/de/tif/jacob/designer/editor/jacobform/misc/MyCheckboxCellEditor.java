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

import java.text.MessageFormat;

import org.eclipse.jdt.internal.ui.refactoring.nls.MultiStateCellEditor;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.ComboBoxLabelProvider;

import de.tif.jacob.designer.JacobDesigner;

public class MyCheckboxCellEditor extends CheckboxCellEditor 
{
	TreeItem item;
	
	public MyCheckboxCellEditor(Composite parent) 
	{
		super(parent);
		item = ((Tree)parent).getSelection()[0];
	}

	public void activate() 
	{
		// Unbedingt Ausschalten, da sonst  bei jedem Klick in der Zeile
		// der Wert sich ändert. Dieser soll sich aber nur ändern wenn man direkt
		// auf die Checkbox klickt
		//
	//	super.activate(); RAUS!!!
		
		// Das Image muss ausgeblendet werden wenn der Editor angezeigt wird.
		// Man hat sonst Image+checkbox gleichzeitig auf dem Screen
		//
		item.setImage(1,null);
	}


	protected Object doGetValue() 
	{
		return new Boolean(((Button)getControl()).getSelection());
	}

	protected void doSetValue(Object value) 
	{
		((Button)getControl()).setSelection(((Boolean)value).booleanValue());
	}


	public void deactivate() 
	{
		super.deactivate();
		if(item!=null && getControl()!=null)
		{
			Object value = getValue();
			if(value instanceof Boolean)
			{
				Image img=JacobDesigner.getImage(((Boolean)value).booleanValue() ? "checked.gif" : "unchecked.gif");
				item.setImage(1,img);
			}
		}
	}

	protected Control createControl(Composite parent) 
	{
		Button button= new Button(parent,SWT.CHECK);
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {}
			public void widgetSelected(SelectionEvent e) {
				MyCheckboxCellEditor.this.setValue(new Boolean(((Button)e.getSource()).getSelection()));
			}
		});
		return button;
	}
	
}
