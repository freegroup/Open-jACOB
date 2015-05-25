/*
   This file is part of Open-jACOB
   Copyright (C) 2005-2010 Andreas Herz | FreeGroup

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; version 2 of the License.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License     
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
   USA

*/
package de.tif.jacob.designer.resourcelistener;

import java.io.File;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.EntryPointCMDModel;
import de.tif.jacob.designer.model.JacobModel;

/**
 * 
 */
public class ResourceChangeListenerJAD extends AbstractResourceChangeListener
{
  
  /**
   * 
   */
  public ResourceChangeListenerJAD()
  {
    
  }
  
  /**
   * 
   * 
   * @param event 
   */
  public void resourceChanged(IResourceChangeEvent event) 
  {
    
     if (event.getType() != IResourceChangeEvent.POST_CHANGE)
        return;
     
     if(JacobDesigner.getPlugin().getSelectedProject()==null)
       return;

     IResourceDelta rootDelta = event.getDelta();

     IPath path = new Path(JacobDesigner.getPlugin().getSelectedProject().getName()+File.separator+"application.jad");
     final IResourceDelta docDelta = rootDelta.findMember(path);
     if (docDelta == null)
        return;

     final IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() 
     {
        boolean hasReloaded = false;
        public boolean visit(IResourceDelta delta) 
        {
           if(hasReloaded)
             return true;
           if (delta.getKind() != IResourceDelta.CHANGED)
              return true;
           if (((delta.getFlags() & IResourceDelta.CONTENT) != 0) || ((delta.getFlags()  & IResourceDelta.REPLACED) != 0)) 
           {
             if(JacobDesigner.getPlugin().hasModelFileChangedOnFilesystem())
             {
               String name = JacobDesigner.getPlugin().getModel().getApplicationModel().getName();
               if(MessageDialog.openQuestion(null,"Reload application.jad","The application definition ["+name+"] has been changed outside the jACOB Designer.\n\nDo you want to reload it?"))
                 JacobDesigner.getPlugin().reloadModel();
             }
             hasReloaded=true;
           }
           return true;
        }
     };
    	Display.getDefault().syncExec(new Runnable()
          {
            public void run()
            {
              try
              {
                docDelta.accept(visitor);
              } 
              catch (CoreException e) 
              {
                 //open error dialog with syncExec or print to plugin log file
              }
            }
          });
  }
}
