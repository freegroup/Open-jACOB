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
package de.tif.jacob.designer.editor.jacobform.commands;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.IButtonBarElementModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIButtonBarModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabContainerModel;
import de.tif.jacob.designer.model.UITabPanesModel;
import de.tif.jacob.designer.preferences.I18NPreferences;

public class CreateButtonBarElementCommand extends Command
{
  private final JacobModel     jacobModel;
  private final UIButtonBarModel bar;
	private final IButtonBarElementModel    button;
	private final int             insertIndex;
  
	public CreateButtonBarElementCommand(JacobModel jacobModel, UIButtonBarModel bar, IButtonBarElementModel button, int index)
	{
		this.button = button;
		this.bar    = bar;
		this.jacobModel  = jacobModel;
    this.insertIndex = index;
    button.setJacobModel(jacobModel);
	}
	
	public void execute()
	{
    try
    {
      redo();

      String defaultName = button.getDefaultName();
      String newName     = defaultName;
      int counter=2;
      while(bar.getGroupModel().isUIElementNameFree(newName)==false)
      {
        newName = defaultName+counter;
        counter++;
      }
      button.setName(newName);
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
	}
	
	public void redo()
	{
		bar.addElement(button);
	}
	
	public void undo()
	{
		bar.removeElement(button);
	}
}
