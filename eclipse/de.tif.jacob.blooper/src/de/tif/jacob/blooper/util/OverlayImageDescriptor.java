/*
 * Created on 20.07.2005
 *
 */
package de.tif.jacob.blooper.util;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.internal.OverlayIcon;
import org.eclipse.ui.internal.util.Util;
/**
 * An OverlayIcon consists of a main icon and an overlay icon
 */
public class OverlayImageDescriptor extends CompositeImageDescriptor
{
  private ImageData base    = null;
  private ImageData overlay = null;

  public OverlayImageDescriptor(ImageDescriptor base, ImageDescriptor overlay)
  {
    this.base    = base.getImageData();
    this.overlay = overlay.getImageData();
    if(this.base==null)
      this.base=ImageDescriptor.getMissingImageDescriptor().getImageData();
  }

  protected void drawCompositeImage(int width, int height) 
  {
    drawImage(base, 0, 0);
    int y = base.height - overlay.height;
    drawImage(overlay, 0, y);
 } 
  

  protected Point getSize()
  {
    return new Point(base.width, base.height);
  }
}