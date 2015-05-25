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

import java.io.InputStream;
import java.io.StringBufferInputStream;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.gef.commands.Command;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;

public class DeleteGroupElementCommand extends Command
{
  private UIGroupModel group;
  private UIGroupElementModel element;
  private String eventCode;
  private String eventClassName;
  private IPath eventFilePath;
  
  public DeleteGroupElementCommand(UIGroupModel group, UIGroupElementModel element)
  {
    this.group = group;
    this.element = element;
  }

  public void execute()
  {
    eventClassName = element.getHookClassName();

    if (eventClassName != null)
    {
      Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
      if(!MessageDialog.openQuestion(shell,"Question","Delete the Eventhandler of the UI element on filesystem too?"))
        eventClassName = null;
    }    
    group.removeElement(element);

    if (eventClassName != null)
    {
     
      try
      {
        IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
        IType type = myJavaProject.findType(eventClassName);
        if(type!=null && type.getCompilationUnit()!=null)
        {
          IFile file = (IFile) type.getCompilationUnit().getCorrespondingResource();
          if (file != null)
          {
            InputStream in = file.getContents();
            eventCode = IOUtils.toString(in);
            in.close();
            eventFilePath = file.getProjectRelativePath();
            file.refreshLocal(IResource.DEPTH_ONE, null);
            file.delete(true, true, null);
          }
        }
      }
      catch (Exception e)
      {
        JacobDesigner.showException(e);
      }
    }
  }

  public void redo()
  {
    execute();
  }

  public void undo()
  {
    try
    {
      group.addElement(element);

      if (eventCode != null && eventFilePath != null)
      {
        IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
        IFile file = JacobDesigner.getPlugin().getSelectedProject().getFile(eventFilePath);

        file.create(new StringBufferInputStream(eventCode), true, null);
      }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
  }
}
