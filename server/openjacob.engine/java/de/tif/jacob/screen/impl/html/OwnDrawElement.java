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

package de.tif.jacob.screen.impl.html;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.tif.jacob.core.definition.guielements.OwnDrawElementDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IOwnDrawElement;
import de.tif.jacob.screen.event.IOwnDrawElementEventHandler;
import de.tif.jacob.util.gif.GifEncoder;
import de.tif.jacob.util.gif.Quantize;
import de.tif.jacob.util.gif.TransparentFilter;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OwnDrawElement extends GuiHtmlElement implements IOwnDrawElement
{
  static public final transient String RCS_ID = "$Id: OwnDrawElement.java,v 1.7 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  byte[] imageData;
  private final OwnDrawElementDefinition definition;
  protected OwnDrawElement(IApplication app, OwnDrawElementDefinition def)
  {
  	super(app, def.getName(), "label",def.isVisible(),def.getRectangle(), def.getProperties());
  	this.definition=def;
  }

  
  private List imageMapEntries = new ArrayList();
  class ImageMapEntry
  {
    final String id;
    final String title;
    final Rectangle rectangle;
    ImageMapEntry(String id,String tooltip, Rectangle rect)
    {
      this.id = id;
      this.title = tooltip;
      this.rectangle = rect;
    }
  }
  
  /*
   * 
   */
  public final void refresh()
  {
    resetCache();
  }
  
  public final void resetCache()
  {
    super.resetCache();
    imageData = null;
    imageMapEntries=new ArrayList();
  }
  
  
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid != getId())
      return false;
    
    IOwnDrawElementEventHandler hook =getHook(context);
    hook.onClick(context,this,value);
    
    return true;
  }

  public void addClickArea(String id,String tooltip, Rectangle area)
  {
    imageMapEntries.add(new ImageMapEntry(id,tooltip,area));
  }

  /**
   * return the HTML representation of this object
   */
  public final void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
  
    if(getCache()==null)
    {
      Writer w = newCache();
    	
      w.write("<img style=\"");
      getCSSStyle(context, w, boundingRect);
      w.write("\" name=\"");
      w.write(getEtrHashCode());
      w.write("\" id=\"");
      w.write(getEtrHashCode());
      w.write("\"");
      if(getDataStatus() == UPDATE || getDataStatus()==NEW)
	      w.write(" class='image_update' ");
      else if(getDataStatus() == SELECTED)
        w.write(" class='image_selected' ");
      else
        w.write(" class='image_empty' ");
      
      w.write(" src=\"image?browser=");
      w.write(context.clientBrowser);//+"&image="+button.getName()+"');
      w.write("&dbImage=");
      w.write(getName());
      w.write("&dummy=");
      w.write(Long.toString(System.currentTimeMillis()));

      // calculate the image 
      //
      IOwnDrawElementEventHandler hook =getHook(context);
      Image image = new BufferedImage(boundingRect.width, boundingRect.height,BufferedImage.TYPE_INT_ARGB);
      
      // avoid JavaScript error and incorrect HTML, if paint() method is errorneous
      //
      try
      {
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Get a graphics object to draw on the image
        hook.paint(context,this, graphics, boundingRect.getSize());
        
        // Farben für GIF auf 256 reduzieren
        image = Quantize.quantizeImage(image,255);
      }
      catch (RuntimeException ex)
      {
        ExceptionHandler.handle(context, ex);
      }
      catch (Exception ex)
      {
        ExceptionHandler.handle(context, ex);
      }

      if(imageMapEntries.size()>0)
      {
        w.write("\" border=0 usemap=\"#imageMap_");
        w.write(getEtrHashCode());
        w.write("\">\n");
        w.write("<map name='imageMap_"+getEtrHashCode()+"'>\n");
        for (int i = 0; i < imageMapEntries.size(); i++)
        {
            ImageMapEntry entry = (ImageMapEntry)imageMapEntries.get(i);
            w.write("<area href=\"javascript:FireEventData('");
            w.write(Integer.toString(getId()));
            w.write("','click','");
            w.write(entry.id);
            w.write( "');\" coords=\"");
            w.write(Integer.toString(entry.rectangle.x));
            w.write(",");
            w.write(Integer.toString(entry.rectangle.y));
            w.write(",");
            w.write(Integer.toString(entry.rectangle.x+entry.rectangle.width));
            w.write(",");
            w.write(Integer.toString(entry.rectangle.y+entry.rectangle.height));
            w.write("\" title=\"");
            w.write(entry.title);
            w.write("\" shape=\"rect\"/>\n");
        } 
        w.write("</map>\n");      
      }
      else
      {
        w.write("\">\n");
      }
      

      // Send the GIF to the browser
      // Encode the image as a GIF (Thanks to Jef Poskanzer! www.acme.com)
      GifEncoder encoder;
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      
      Color transparentColor = hook.getTransparentColor(); 
      if (transparentColor != null)
          encoder = new GifEncoder(new FilteredImageSource(image.getSource(), new TransparentFilter(transparentColor)), out);
      else
          encoder = new GifEncoder(image, out);

      encoder.encode();
      out.flush();
      
      imageData= out.toByteArray();
    }
  }

  public final byte[] getImageData(IClientContext context, String imageId)
  {
    return imageData;
  }

  protected final void addDataFields(Vector fields)
  {
  }

  /** 
   */
  public final void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    writeCache(w);
  }

  public final GroupState getDataStatus()
	{
	  if(parent!=null)
	    return parent.getDataStatus();
	  return super.getDataStatus(); 
	}
  
  private IOwnDrawElementEventHandler getHook(IClientContext context) throws UserException
  {
	  Object obj = getEventHandler(context);
    if(obj instanceof IOwnDrawElementEventHandler)
      return (IOwnDrawElementEventHandler)obj;
    throw new UserException("Object ["+getPathName()+"] must implement a hook of type ["+IOwnDrawElementEventHandler.class.getName()+"]");
  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }

	public void onGroupDataStatusChanged(IClientContext context, GroupState newGroupDataStatus) throws Exception 
	{
		refresh();
		super.onGroupDataStatusChanged(context, newGroupDataStatus);
	}
  
}
