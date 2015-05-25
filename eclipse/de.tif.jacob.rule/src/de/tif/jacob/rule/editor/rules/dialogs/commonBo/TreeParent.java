/*
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.commonBo;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

import de.tif.jacob.designer.JacobDesigner;

public class TreeParent extends TreeObject
{
  private ArrayList children= new ArrayList();
  
  public TreeParent(TreeViewer viewer,TreeParent parent, String name)  throws Exception
  {
    super(viewer, parent, name);
  }
  
  public void addChildren() throws Exception
  {
  }
  
  public final void addChild(TreeObject child)
  {
    children.add(child);
  }

  public final void removeChild(TreeObject child)
  {
    children.remove(child);
  }

  public final TreeObject[] getChildren()
  {
    return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
  }
  

  public final boolean hasChildren()
  {
    return children.size() > 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getImage()
   */
  public Image getImage()
  {
    return JacobDesigner.getImage("BusinessObjectClassParent.png",JacobDesigner.DECORATION_NONE);
  }
}