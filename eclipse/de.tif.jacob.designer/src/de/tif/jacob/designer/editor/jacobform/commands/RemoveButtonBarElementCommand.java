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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIButtonBarModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;

public class RemoveButtonBarElementCommand extends Command
{
  private UIButtonBarModel bar;
  private UIButtonModel    button;

  public RemoveButtonBarElementCommand(UIButtonBarModel bar, UIButtonModel button)
  {
    this.bar = bar;
    this.button = button;
  }

  public void execute()
  {
    bar.removeElement(button);
    // Wenn wir den Button aus der Bar entfernen geben wir Ihm als "Aussteuer"
    // eine dimension mit. In Einer buttonBar hat ein Button die [width:0;height:0]
    // Wird der Button dann in eine normale Gruppe gezogen ist dieser nicht sichtbar, da keine
    // Weite/Höhe vorhanden ist.
    button.setSize(new Dimension(ObjectModel.DEFAULT_BUTTON_WIDTH, ObjectModel.DEFAULT_BUTTON_HEIGHT));
  }

  public void redo()
  {
    execute();
  }

  public void undo()
  {
    bar.addElement(button);
  }
}
