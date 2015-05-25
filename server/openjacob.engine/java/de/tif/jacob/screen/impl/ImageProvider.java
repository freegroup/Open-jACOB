/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.screen.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.net.www.MimeTable;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.screen.IImageProvider;


/**
 * @author Andreas Herz
 *
 */
public class ImageProvider extends HttpServlet
{
  static public final transient String RCS_ID = "$Id: ImageProvider.java,v 1.4 2009/07/02 14:18:51 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  static private final transient Log logger = LogFactory.getLog(ImageProvider.class);
  
  static private byte[] blank;
  static private byte[] noImageFrame;
  
  public void service(HttpServletRequest request, HttpServletResponse response)
  {
    String mimeType=MimeTable.getDefaultTable().findByFileName("test.gif").getType();
    
    response.setContentType(mimeType);
    String browserId   = request.getParameter("browser");
    String imageId     = request.getParameter("image");
    String dbImageProvider = request.getParameter("dbImage"); // its's not the image...it's the ID of the IImageProvider
    String dbImageId   = request.getParameter("dbImageId"); // the image id or NULL if the provider only provides one single image
    
    try
    {
      HTTPClientSession jacobSession=HTTPClientSession.get(request);

      // Falls sich der Client abmeldet, kann es passieren, das die Menuleiste 'schneller' oder noch nicht fertig war seine
      // Bilder für die Outlookbar zu laden. Bei dem logout wird nun die session gelöscht und die Outlookbar kann
      // diese somit nicht finden. Kann man ignorieren.
      //
      if(jacobSession!=null)
      {  
        if(imageId!=null)
        {
		      Object obj =ClassProvider.getInstance(jacobSession.getApplicationDefinition(),"jacob.resources.ResourceProvider");
		      if(obj!=null)
		      {
		        InputStream in=null;
		        if(imageId.endsWith(".gif")||imageId.endsWith(".png"))
		        {
			        in =obj.getClass().getResourceAsStream(imageId);
		        }
		        else
		        {
			        in =obj.getClass().getResourceAsStream(imageId+".gif");
			        if(in==null)
			          in =obj.getClass().getResourceAsStream(imageId+".png");
		        }
		        if(in==null)
		          in =getClass().getResourceAsStream("DefaultOutlookIcon.gif");
		        
		        if(in!=null)
		        {  
		          OutputStream out =response.getOutputStream();
			        CopyUtils.copy(in,out);
			        IOUtils.closeQuietly(in);
		        }
		      }
        }
        else if(dbImageProvider !=null)
        {
          response.setHeader("Cache-Control", "no-cache");
          response.setHeader("Pragma", "no-cache");
          response.setDateHeader("Expires", 0);

          HTTPApplication app = (HTTPApplication)jacobSession.getApplication(browserId);
          
          HTTPClientContext context = jacobSession.createContext(jacobSession.getUser(),app, browserId);
          Context.setCurrent(context);
          
          Object image = app.getActiveDomain().getCurrentForm(context).findByName(dbImageProvider);
          if (image instanceof IImageStreamProvider)
          {
            // neues Interface IImageStreamProvider
            IImageStreamProvider imageStream = (IImageStreamProvider) image;
            String contentType = imageStream.getImageContentType(context, dbImageId);
            if (contentType != null)
              response.setContentType(contentType);
            imageStream.writeImage(context, response.getOutputStream(), dbImageId);
          }
          else
          {
            // altes Interface IImageProvider
            OutputStream out = response.getOutputStream();
            byte[] data = ((IImageProvider) image).getImageData(context, dbImageId);
            if (data == null)
              data = getNoImageFrameImage();
            if (data == null)
              data = getBlankImage();

            CopyUtils.copy(data, out);
          }
        }
      }
    }
    catch (Exception e)
    {
      logger.warn("Could not provide image '"+imageId+"'", e);
    }
  }
  
  public synchronized static byte[] getBlankImage()
  {
    if(blank == null)
      blank = loadImage("blank.png");

    return blank;
  }
  
  public synchronized static byte[] getNoImageFrameImage()
  {
    if(noImageFrame == null)  //no_image_frame.png
      noImageFrame = loadImage("no.png");

    return noImageFrame;
  }
  

  private static byte[] loadImage(String fileName)
  {
    try
    {
      InputStream in =ImageProvider.class.getResourceAsStream(fileName);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      
      if(in!=null)
      {  
        CopyUtils.copy(in,out);
        IOUtils.closeQuietly(in);
        return out.toByteArray();
      }
    }
    catch (IOException e)
    {
      ExceptionHandler.handle(e);
    }
    return null;
  }
}

  