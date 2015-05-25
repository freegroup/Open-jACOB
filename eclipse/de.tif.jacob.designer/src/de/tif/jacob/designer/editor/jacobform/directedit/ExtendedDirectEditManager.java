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
package de.tif.jacob.designer.editor.jacobform.directedit;


import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * A generic DirectEdit manager to be used for labels which includes validation
 * functionality by adding the ICellEditorValidator on startup
 */
public class ExtendedDirectEditManager extends DirectEditManager
{
	Font figureFont;
	protected VerifyListener verifyListener;
	protected Label label;
	protected String originalValue;
	private boolean committing = false;
//	private SubjectControlContentAssistant asistant;
	/**
	 * Creates a new ActivityDirectEditManager with the given attributes.
	 * 
	 * @param source
	 *            the source EditPart
	 * @param editorType
	 *            type of editor
	 * @param locator
	 *            the CellEditorLocator
	 */
	public ExtendedDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator, Label label)
	{
		super(source, editorType, locator);
		this.label = label;
		this.originalValue =label.getText();
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	protected void bringDown()
	{
		Font disposeFont = figureFont;
		figureFont = null;
		super.bringDown();
		if (disposeFont != null)
			disposeFont.dispose();
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	protected void initCellEditor()
	{
		Text text = (Text) getCellEditor().getControl();

    //add the verifyListener to apply changes to the control size
		verifyListener = new VerifyListener()
		{

			/**
			 * Changes the size of the editor control to reflect the changed
			 * text
			 */
			public void verifyText(VerifyEvent event)
			{
				Text text = (Text) getCellEditor().getControl();
				String oldText = text.getText();
				String leftText = oldText.substring(0, event.start);
				String rightText = oldText.substring(event.end, oldText.length());
				GC gc = new GC(text);
				if (leftText == null)
					leftText = "";
				if (rightText == null)
					rightText = "";

				String s = leftText + event.text + rightText;

				Point size = gc.textExtent(leftText + event.text + rightText);

				gc.dispose();
				if (size.x != 0)
					size = text.computeSize(size.x, SWT.DEFAULT);
				else
				{
					//just make it square
					size.x = size.y;
				}
				getCellEditor().getControl().setSize(size.x, size.y);
			}

		};
		text.addVerifyListener(verifyListener);

		//set the initial value of the
		originalValue = label.getText();
		getCellEditor().setValue(originalValue);

		//calculate the font size of the underlying
		IFigure figure =  getEditPart().getFigure();
		figureFont = figure.getFont();
		FontData data = figureFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, data.getHeight());

		//set the font to be used
		this.label.translateToAbsolute(fontSize);
		data.setHeight(fontSize.height);
		figureFont = new Font(null, data);


		text.setFont(figureFont);
		text.selectAll();
	}

	/**
	 * Commits the current value of the cell editor by getting a {@link Command}
	 * from the source edit part and executing it via the {@link CommandStack}.
	 * Finally, {@link #bringDown()}is called to perform and necessary cleanup.
	 */
	
	protected void commit()
	{
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
		getEditPart().refresh();
	}
	

	/**
	 * Need to override so as to remove the verify listener
	 */
	protected void unhookListeners()
	{
		Text text = (Text) getCellEditor().getControl();
		text.removeVerifyListener(verifyListener);
		verifyListener = null;

		super.unhookListeners();
	}

}