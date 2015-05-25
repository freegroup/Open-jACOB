/*
 * Created on 08.03.2007
 *
 */
package de.tif.jacob.screen;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import de.tif.jacob.util.file.Directory;

public final class CreateIconClassFile
{
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    ArrayList files = Directory.getAll(new File("webapp/icons"),false);
    for (Iterator iter = files.iterator(); iter.hasNext();)
    {
      File element = (File) iter.next();
      String name = element.getName();
      name = name.substring(0,name.length()-4);
      System.out.println("\tpublic final static Icon "+name+" = new Icon(\""+name+"\");");
      BufferedImage orig = ImageIO.read(element);
      BufferedImage gray = convertToGrayscale(orig);
      String path = element.getParent();
      ImageIO.write(gray,"png",new File(path+"/"+name+"_disabled.png"));
      
    }
    // TODO Auto-generated method stub
  }
  
  public static BufferedImage convertToGrayscale(BufferedImage source) 
  {
    
    BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null); 
    return op.filter(source, null);
}      
}
