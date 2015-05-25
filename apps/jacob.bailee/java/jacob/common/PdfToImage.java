package jacob.common;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
public class PdfToImage
{
  private static final String STANDARD_METADATA_FORMAT = "javax_imageio_1.0";

  public static BufferedImage pdf2(byte[] pdfDocument) throws Exception
  {
    // load a pdf from a byte buffer
    ByteBuffer buf = ByteBuffer.wrap(pdfDocument);
    PDFFile pdffile = new PDFFile(buf);
    // draw the first page to an image
    PDFPage page = pdffile.getPage(0);
    // get the width and height for the doc at the default zoom
    Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
    // generate the image
    Image img = page.getImage(rect.width, rect.height, // width & height
                              rect, // clip rect
                              null, // null for the ImageObserver
                              true, // fill background with white
                              true // block until drawing is done
                                  );
    
    // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
    // into integer pixels
    BufferedImage bi = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);

    Graphics2D ig2 = bi.createGraphics();
    ig2.drawImage(img,0,0,null);
    
    return bi;
  }


  private static IIOMetadata createMetadata(RenderedImage image, ImageWriter imageWriter, ImageWriteParam writerParams, int resolution)
  {
    ImageTypeSpecifier type;
    if (writerParams.getDestinationType() != null)
    {
      type = writerParams.getDestinationType();
    }
    else
    {
      type = ImageTypeSpecifier.createFromRenderedImage(image);
    }
    IIOMetadata meta = imageWriter.getDefaultImageMetadata(type, writerParams);
    return (addResolution(meta, resolution) ? meta : null);
  }

  private static boolean addResolution(IIOMetadata meta, int resolution)
  {
    if (meta.isStandardMetadataFormatSupported())
    {
      IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(STANDARD_METADATA_FORMAT);
      IIOMetadataNode dim = getChildNode(root, "Dimension");
      IIOMetadataNode child;
      child = getChildNode(dim, "HorizontalPixelSize");
      if (child == null)
      {
        child = new IIOMetadataNode("HorizontalPixelSize");
        dim.appendChild(child);
      }
      child.setAttribute("value", Double.toString(resolution / 25.4));
      child = getChildNode(dim, "VerticalPixelSize");
      if (child == null)
      {
        child = new IIOMetadataNode("VerticalPixelSize");
        dim.appendChild(child);
      }
      child.setAttribute("value", Double.toString(resolution / 25.4));
      try
      {
        meta.mergeTree(STANDARD_METADATA_FORMAT, root);
      }
      catch (IIOInvalidTreeException e)
      {
        throw new RuntimeException("Cannot update image metadata: " + e.getMessage());
      }
      return true;
    }
    return false;
  }

  private static IIOMetadataNode getChildNode(Node n, String name)
  {
    NodeList nodes = n.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++)
    {
      Node child = nodes.item(i);
      if (name.equals(child.getNodeName()))
      {
        return (IIOMetadataNode) child;
      }
    }
    return null;
  }

  public static void main(String[] args) throws Exception
  {
    byte[] pdf = IOUtils.toByteArray(PdfToImage.class.getResourceAsStream("test.pdf"));
    BufferedImage img = pdf2(pdf);

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    ImageIO.write(img, "png", stream );
    FileUtils.writeByteArrayToFile(new File("test.png"),stream.toByteArray());
  }
}
