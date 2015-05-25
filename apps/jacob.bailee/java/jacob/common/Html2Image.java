package jacob.common;
import java.awt.image.BufferedImage;

import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.FSImageWriter;
public class Html2Image
{
  private static volatile boolean loaded;

  public static void main(String[] args) throws Exception
  {
    // can specify width alone, or width + height
    // constructing does not render; not until getImage() is called
    Java2DRenderer renderer = new Java2DRenderer("http://www.verside.org", 1200);

    // this renders and returns the image, which is stored in the J2R; will not
    // be re-rendered, calls to getImage() return the same instance
    BufferedImage img = renderer.getImage();

    // write it out, full size, PNG
    // FSImageWriter instance can be reused for different images,
    // defaults to PNG
    FSImageWriter imageWriter = new FSImageWriter();
    imageWriter.write(img, "x-full.png");
  }

}
