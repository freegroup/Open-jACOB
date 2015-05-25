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
package de.tif.jacob.designer.resourcelistener;

import java.io.File;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Display;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.EntryPointGUIModel;
import de.tif.jacob.designer.model.JacobModel;

/**
 * 
 */
public class ResourceChangeListenerGUI extends AbstractResourceChangeListener
{
  
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

     IPath DOC_PATH = getJavaSourcePath("jacob"+File.separator+"entrypoint"+File.separator+"gui");

     final IResourceDelta docDelta = rootDelta.findMember(DOC_PATH);
     if (docDelta == null)
        return;

     final IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() 
     {
        public boolean visit(IResourceDelta delta) 
        {
           if (delta.getKind() != IResourceDelta.REMOVED)
              return true;
           //only interested in content changes
           IResource resource = delta.getResource();
           if (resource.getType() == IResource.FILE && 	"java".equalsIgnoreCase(resource.getFileExtension())) 
           {
             String name = toShortClassName(resource);
             JacobModel jacob = JacobDesigner.getPlugin().getModel();
             EntryPointGUIModel model =jacob.getEntryPointGUIModel(name);
             if(model!=null)
               jacob.removeElement(model);
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
