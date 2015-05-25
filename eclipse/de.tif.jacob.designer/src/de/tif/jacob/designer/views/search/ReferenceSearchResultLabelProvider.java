/*
 * Created on 29.06.2007
 *
 */
package de.tif.jacob.designer.views.search;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.designer.views.search.tree.TreeObject;


public class ReferenceSearchResultLabelProvider extends LabelProvider
{
  public String getText(Object obj)
  {
    return obj.toString();
  }

  public Image getImage(Object obj)
  {
    if (obj instanceof TreeObject)
    {
      TreeObject treeObj = (TreeObject) obj;
      return treeObj.getImage();
    }
    return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
  }
}

