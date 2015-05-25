/*
 * Created on 03.01.2005
 *
 */
package de.tif.jacob.module.popup.actions;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
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
