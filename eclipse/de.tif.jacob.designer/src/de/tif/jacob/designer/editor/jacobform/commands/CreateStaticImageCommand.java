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

import java.io.File;
import java.io.FileInputStream;
import javax.swing.ImageIcon;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.dialogs.ImageSelectorDialog;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIStaticImageModel;
/**
 * Einfügen eines nicht DB UI-Elements
 *
 */
public class CreateStaticImageCommand extends Command
{
  protected final UIGroupModel group;
  protected final Point location;
  protected final UIStaticImageModel model;

  boolean canUndo = false;
  
  public CreateStaticImageCommand(UIGroupModel group, UIStaticImageModel model, Point location)
	{
    this.group = group;
    this.model = model;
    this.location = location;
	}
	
	public void execute()
	{
    IFolder folder = JacobDesigner.getPlugin().getSelectedProject().getFolder(JacobModel.STATIC_IMAGE_PATH);
    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    ImageSelectorDialog dialog =new ImageSelectorDialog(shell,new File(folder.getLocationURI().getPath()));
    String filePath = dialog.open();
    
    if(filePath!=null)
    {
      try
      {
        model.setLocation(location);
        model.setGroup(group);
        model.setSrc(filePath);
  
        // ein StaticImage ist immer ganz unten in der Z-Order. Deswegen wird hier "0" als Index
        // übergeben
        //
        group.addElement(model,true);
  
        String defaultName = model.getDefaultName();
        String newName     = defaultName;
        int counter=2;
        while(group.getGroupContainerModel().isUIElementNameFree(newName)==false)
        {
          newName = defaultName+counter;
          counter++;
        }
        model.setName(newName);
        canUndo = true;
      }
      catch (Exception e)
      {
        JacobDesigner.showException(e);
      }
    }
	}
	
	public void redo()
	{
    if(canUndo==true)
      group.addElement(model);
	}
	
	public void undo()
	{
    if(canUndo==true)
      group.removeElement(model);
	}

}
