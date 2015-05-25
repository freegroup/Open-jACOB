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
package de.tif.jacob.designer.editor.relationset.directedit;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.editor.relationset.figures.StickyNoteFigure;


final public class LabelCellEditorLocator
	implements CellEditorLocator
{

private StickyNoteFigure stickyNote;

private static int WIN_X_OFFSET = -4;
private static int WIN_W_OFFSET = 5;
private static int GTK_X_OFFSET = 0;
private static int GTK_W_OFFSET = 0;
private static int MAC_X_OFFSET = -3;
private static int MAC_W_OFFSET = 9;
private static int MAC_Y_OFFSET = -3;
private static int MAC_H_OFFSET = 6;

public LabelCellEditorLocator(StickyNoteFigure stickyNote) {
	setLabel(stickyNote);
}

public void relocate(CellEditor celleditor) {
	Text text = (Text)celleditor.getControl();

	Rectangle rect = stickyNote.getClientArea().getCopy();
	stickyNote.translateToAbsolute(rect);
	
	int xOffset = 0;
	int wOffset = 0;
	int yOffset = 0;
	int hOffset = 0;
	
	if (SWT.getPlatform().equalsIgnoreCase("gtk")) {
		xOffset = GTK_X_OFFSET;
		wOffset = GTK_W_OFFSET;
	} else if (SWT.getPlatform().equalsIgnoreCase("carbon")) {
		xOffset = MAC_X_OFFSET;
		wOffset = MAC_W_OFFSET;
		yOffset = MAC_Y_OFFSET;
		hOffset = MAC_H_OFFSET;
	} else {
		xOffset = WIN_X_OFFSET;
		wOffset = WIN_W_OFFSET;
	}

	text.setBounds(rect.x + xOffset, rect.y + yOffset, rect.width + wOffset, rect.height + hOffset);	
}

/**
 * Returns the stickyNote figure.
 */
protected StickyNoteFigure getLabel() {
	return stickyNote;
}

/**
 * Sets the Sticky note figure.
 * @param stickyNote The stickyNote to set
 */
protected void setLabel(StickyNoteFigure stickyNote) {
	this.stickyNote = stickyNote;
}

}
