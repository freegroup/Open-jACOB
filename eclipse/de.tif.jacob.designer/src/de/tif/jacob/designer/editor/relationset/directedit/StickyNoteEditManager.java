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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.editor.relationset.figures.StickyNoteFigure;


public class StickyNoteEditManager extends DirectEditManager
{

  Font scaledFont;
	private boolean committing = false;

  public StickyNoteEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) 
  {
    super(source, editorType, locator);
  }

  /**
   * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
   */
  protected void bringDown()
  {
    //This method might be re-entered when super.bringDown() is called.
    Font disposeFont = scaledFont;
    scaledFont = null;
    super.bringDown();
    if (disposeFont != null)
      disposeFont.dispose();
  }

  protected void initCellEditor()
  {
    Text text = (Text) getCellEditor().getControl();

    System.out.println( getEditPart().getFigure());
    StickyNoteFigure stickyNote = (StickyNoteFigure) getEditPart().getFigure();
    String initialLabelText = stickyNote.getText();
    getCellEditor().setValue(initialLabelText);
    IFigure figure = getEditPart().getFigure();
    scaledFont = figure.getFont();
    FontData data = scaledFont.getFontData()[0];
    Dimension fontSize = new Dimension(0, data.getHeight());
    stickyNote.translateToAbsolute(fontSize);
    data.setHeight(fontSize.height);
    scaledFont = new Font(null, data);

    text.setFont(scaledFont);
    text.selectAll();
  }

	
	protected void commit()
	{
	  System.out.println("ExtendedDirectEditManager.commit");
		if (committing)
			return;
		committing = true;
		try
		{
			//we set the cell editor control to invisible to remove any
			// possible flicker
			getCellEditor().getControl().setVisible(false);
			if (isDirty())
			{
				CommandStack stack = getEditPart().getViewer().getEditDomain().getCommandStack();
				Command command = getEditPart().getCommand(getDirectEditRequest());

				if (command != null && command.canExecute())
					stack.execute(command);
			}
		}
		finally
		{
			bringDown();
			committing = false;
		}
	}

	/**
   * Creates the cell editor on the given composite. The cell editor is created
   * by instantiating the cell editor type passed into this DirectEditManager's
   * constuctor.
   * 
   * @param composite
   *          the composite to create the cell editor on
   * @return the newly created cell editor
   */
  protected CellEditor createCellEditorOn(Composite composite)
  {
    return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP);
  }

}
