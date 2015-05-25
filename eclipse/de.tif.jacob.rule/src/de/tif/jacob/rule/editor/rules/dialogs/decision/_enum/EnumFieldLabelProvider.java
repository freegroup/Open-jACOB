package de.tif.jacob.rule.editor.rules.dialogs.decision._enum;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


class EnumFieldLabelProvider extends LabelProvider
{
  public String getText(Object obj)
  {
    TreeObject treeObj = (TreeObject)obj;
    return treeObj.getLabel();
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
