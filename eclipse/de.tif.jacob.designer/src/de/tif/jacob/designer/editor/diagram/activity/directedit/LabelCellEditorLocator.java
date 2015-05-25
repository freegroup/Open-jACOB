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
 * Created on Oct 20, 2004
 *
 */
package de.tif.jacob.designer.editor.diagram.activity.directedit;


import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * A CellEditorLocator for a specified label
 * 
 * @author Phil Zoio
 */
public class LabelCellEditorLocator implements CellEditorLocator
{
	private Label label;

	/**
	 * Creates a new CellEditorLocator for the given Label
	 * 
	 * @param label
	 *            the Label
	 */
	public LabelCellEditorLocator(Label label)
	{
	  System.out.println("LabelCellEditorLocator.LabelCellEditorLocator");
		this.label=label;
	}

	/**
	 * expands the size of the control by 1 pixel in each direction
	 */
	public void relocate(CellEditor celleditor)
	{
	  System.out.println("LabelCellEditorLocator.relocate");
		Text text = (Text) celleditor.getControl();

		Point pref = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = label.getTextBounds().getCopy();
		label.translateToAbsolute(rect);
		if (text.getCharCount() > 1)
			text.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);
		else
			text.setBounds(rect.x - 1, rect.y - 1, pref.y + 1, pref.y + 1);

	}

}
