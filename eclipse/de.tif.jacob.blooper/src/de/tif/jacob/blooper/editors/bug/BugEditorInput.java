/*
 * Created on 19.12.2005
 *
 */
package de.tif.jacob.blooper.editors.bug;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import de.tif.jacob.blooper.model.Bug;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class BugEditorInput implements IEditorInput
{
  private final Bug bug;
  
  public BugEditorInput(Bug bug)
  {
    this.bug = bug;
  }

    /**
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    public boolean exists() 
    {
      // Do nothing
      return false;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() 
    {
      // Do nothing
      return null;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getName()
     */
    public String getName() 
    {
      return "Browser";
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getPersistable()
     */
    public IPersistableElement getPersistable() 
    {
      // Do nothing
      return null;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getToolTipText()
     */
    public String getToolTipText() 
    {
      // Do nothing
      return "";
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter) 
    {
      // Do nothing
      return null;
    }

    public Bug getBug()
    {
      return bug;
    }
}