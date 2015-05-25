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
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import de.tif.jacob.designer.editor.jacobform.commands.MoveGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupElementEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.ObjectEditPart;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;

public class AlignVerticalGroupElementsAction implements IEditorActionDelegate 
{
  public AlignVerticalGroupElementsAction(){}
  
  // wird eine sortierte map benötigt. Key ist dann die y-Koordinate. Alle Elemente
  // werden dann 'von oben nach unten' ausgelesen.
  //
  Map elements = new TreeMap();
  CommandStack stack;
  public void setActiveEditor(IAction action, IEditorPart targetEditor)
  {
    if(targetEditor!=null)
      stack = (CommandStack)targetEditor.getAdapter(CommandStack.class);
    else
      stack=null;
  }
  
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) 
	{
	  if(stack!=null)
	    stack.execute(createCommand());
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) 
	{
    if(((IStructuredSelection)selection).getFirstElement()!=null)
    {
      elements =  new TreeMap();
      Iterator iter = ((IStructuredSelection)selection).iterator();
      while (iter.hasNext())
      {
        Object obj = iter.next();
        if(obj instanceof GroupElementEditPart)
        {
          GroupElementEditPart editPart = (GroupElementEditPart)obj;
          elements.put(new Integer(editPart.getGroupElementModel().getConstraint().y),editPart.getGroupElementModel());
        }
      }
      action.setEnabled(elements.size()>2);
    }
	}
	
	private Command createCommand() 
	{
		if (elements.size()<=2)
			return null;

		Command cmd = null;
		CompoundCommand command = new CompoundCommand();
		
	  Iterator iter = elements.keySet().iterator();
	  UIGroupElementModel first  = (UIGroupElementModel)elements.get(iter.next());
	  UIGroupElementModel second = (UIGroupElementModel)elements.get(iter.next());
	  
	  // abstand zwischen dem ersten und zweiten element bestimmen
	  //
	  int firstDown  = first.getConstraint().bottom();
	  int secondTop  = second.getConstraint().y;
	  int secondDown = second.getConstraint().bottom();
	  
	  int space = secondTop - firstDown;
	  
	  // Alle nachfolgende Elemente mit dem selben Abstand zum Vorgänger ausrichten
	  //
	  while (iter.hasNext())
    {
      Integer y = (Integer) iter.next();
      UIGroupElementModel element = (UIGroupElementModel)elements.get(y);
      Rectangle rect = element.getConstraint().getCopy();
      rect.y = secondDown + space;
      cmd = new MoveGroupElementCommand(element, rect);
//      element.setConstraint(rect);
      secondDown = rect.bottom();
//      System.out.println("GroupElement:"+y+"->"+element);
      command.add(cmd);
    }
		
		return command;
	}
}
