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
 * Created on 03.01.2005
 *
 */
package de.tif.jacob.docgen.util;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;

/**
 *
 */
public class FileUtil
{
  
  /**
   * Creates a folder resource given the folder handle.
   * 
   * @param folderHandle
   *          the folder handle to create a folder resource for
   * @param monitor
   *          the progress monitor to show visual progress with
   * @exception CoreException
   *              if the operation fails
   * @exception OperationCanceledException
   *              if the operation is canceled
   */
  public static IFolder createFolder(IFolder folderHandle) throws Exception
  {
    // refresh the tree. maybe the folder has been created in the background
    //
    folderHandle.refreshLocal(IResource.DEPTH_INFINITE,null);
    
    // Create the folder resource in the workspace
    // Update: Recursive to create any folders which do not exist already
    if (!folderHandle.exists())
    {
      IContainer parent = folderHandle.getParent();
      if (parent instanceof IFolder && (!((IFolder) parent).exists()))
        createFolder((IFolder) parent);
      folderHandle.create(false, true, null);
    }
    return folderHandle;
  }
}
