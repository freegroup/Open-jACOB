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
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;

/**
 * 
 */
public class ResourceChangeListenerI18N extends AbstractResourceChangeListener
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

     IPath i18nFile = new Path(JacobDesigner.getPlugin().getSelectedProject().getName()+File.separator+JacobModel.I18N_PATH);

     final IResourceDelta i18nDelta = rootDelta.findMember(i18nFile);
     if (i18nDelta == null)
        return;

     final IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() 
     {
       boolean reloaded=false;
        public boolean visit(IResourceDelta delta) 
        {
          if(reloaded==true)
            return true;
          IResource resource = delta.getResource();
          if (resource.getType() == IResource.FILE && "properties".equalsIgnoreCase(resource.getFileExtension()))
          {
            // Falls nicht das gesamte project gelöscht worden ist, dann wird
            // jetzt die I18N Resourcen nachgeladen
            //
            if(JacobDesigner.getPlugin().getSelectedProject().exists())
              JacobDesigner.getPlugin().getModel().reloadI18N();
            reloaded=true;
          }
          return true;
        }
     };
      Display display = Display.getDefault();
      if (!display.isDisposed()) 
      {
	    	display.syncExec(new Runnable()
	          {
	            public void run()
	            {
	              try
	              {
	                i18nDelta.accept(visitor);
	              } 
                catch (OperationCanceledException e) 
                {
                   // ignore
                }
	              catch (CoreException e) 
	              {
	                 //open error dialog with syncExec or print to plugin log file
	              }
	            }
	          });
      }
  }
}
