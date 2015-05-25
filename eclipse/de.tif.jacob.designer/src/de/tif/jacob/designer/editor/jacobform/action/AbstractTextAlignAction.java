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
package de.tif.jacob.designer.editor.jacobform.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.editor.jacobform.commands.ChangeFontAlignmentCommand;
import de.tif.jacob.designer.editor.jacobform.commands.MoveGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.editpart.CaptionEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupElementEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.ObjectEditPart;
import de.tif.jacob.designer.model.IFontProviderModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;

public abstract class AbstractTextAlignAction implements IEditorActionDelegate 
{

  public abstract String getAlignment();
  
  // wird eine sortierte map benötigt. Key ist dann die y-Koordinate. Alle Elemente
  // werden dann 'von oben nach unten' ausgelesen.
  //
  List elements = new ArrayList();
  CommandStack stack;
  public final void setActiveEditor(IAction action, IEditorPart targetEditor)
  {
    if(targetEditor!=null)
      stack = (CommandStack)targetEditor.getAdapter(CommandStack.class);
    else
      stack=null;
  }
  
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public final void run(IAction action) 
	{
	  if(stack!=null)
	    stack.execute(createCommand());
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public final void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
    {
      elements =  new ArrayList();
      Iterator iter = ((IStructuredSelection)selection).iterator();
      while (iter.hasNext())
      {
        AbstractGraphicalEditPart obj = (AbstractGraphicalEditPart)iter.next();
        Object anyModel = obj.getModel();
        if(anyModel instanceof IFontProviderModel)
        {
          IFontProviderModel model = (IFontProviderModel)anyModel;
          elements.add(model);
        }
      }
      action.setEnabled(elements.size()>0);
    }
	}
	
	private final Command createCommand() 
	{
		Command cmd = null;
		CompoundCommand command = new CompoundCommand();
		
	  // Alle nachfolgende Elemente mit dem selben Abstand zum Vorgänger ausrichten
	  //
    Iterator iterator = elements.iterator();
	  while (iterator.hasNext())
    {
      IFontProviderModel model = (IFontProviderModel)iterator.next();
      cmd = new ChangeFontAlignmentCommand(model, getAlignment());
      command.add(cmd);
    }
		
		return command;
	}
}
