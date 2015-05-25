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
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Vector;

import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.util.FastStringWriter;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class GuiHtmlElement extends GuiElement
{
  static public final transient String RCS_ID = "$Id: GuiHtmlElement.java,v 1.19 2010/10/22 11:10:11 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.19 $";

	protected static final int KEYCODE_NONE  = -1;
	protected static final int KEYCODE_ENTER = 13;
	protected static final int KEYCODE_DEL   = 46;
	protected static final int KEYCODE_DOWN  = 40;
	protected static final int KEYCODE_UP    = 38;

  private char[] cachedAppendHTML   = null;

  protected abstract void         addDataFields(Vector fields);
  

  /**
   * Constructor form the initial Castor object
   * 
   * @param parent
   * @param name
   * @param label
   * @param isInvisibleString
   * @author Andreas Herz
   */
  public GuiHtmlElement(IApplication app, String name, String label, boolean isVisible, Rectangle boundingRect, Map definitionProperties)
  {
    super(app, name,label, isVisible, boundingRect, definitionProperties);
  }
  
  /**
   * Calculates and add additional HTML include files
   * 
   */
  public void calculateIncludes(ClientContext context)
  {
    for (int i=0; i<getChildren().size();i++)
    {
      ((GuiHtmlElement)getChildren().get(i)).calculateIncludes(context);
    }
  }

  /**
   * Calculates the HTML representation of the application
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    for (int i=0; i<getChildren().size();i++)
    {
    	((GuiHtmlElement)getChildren().get(i)).calculateHTML(context);
    }
  }


  /** 
   * Writes the HTML content to the stream
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    for (int i=0; i<getChildren().size();i++)
    {
      ((GuiHtmlElement)getChildren().get(i)).writeHTML(context, w);
    }
    
    if(this.lastErrorMessage!=null)
      this.lastErrorMessage.writeTo(w);
  }
  
  /**
   * The serve send this message if a form has been submitted.
   * The GUI Element must check if the any data for this guid
   * The default UI element has no data. 
   * 
   */
  public boolean processParameter(int guid, String value) throws IOException, NoSuchFieldException
  {
    if(guid==this.getId())
      return true;
   
    for (int i=0; i<getChildren().size();i++)
    {
      if(((GuiHtmlElement)getChildren().get(i)).processParameter( guid, value))
        return true;
    }
    return false;
  }

  /**
   * The serve send this message if a form has been submitted.
   * The GUI Element must check if the any data for this guid
   * The default UI element has no data. 
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId())
      return true;
    
    for (int i=0; i<getChildren().size();i++)
    {
      if(((GuiHtmlElement)getChildren().get(i)).processEvent(context, guid, event, value))
        return true;
    }
    return false;
  }


  /**
   * map the position of  the object to an HTML style position
   * 
   * @param pos
   * @return
   */
  
  public void getCSSStyle(ClientContext context, Writer out, Rectangle boundingRect) throws IOException
  {
      if(cssStyleCache==null)
      {  
        StringBuffer tmp=new StringBuffer(1024);
        tmp.append(toCSSString(boundingRect));
        if(foregroundColor!=null)
        {
          tmp.append("color:");
          tmp.append(toCSSString(foregroundColor));
          tmp.append(";");
        }
        if(borderColor!=null && borderWidth>=0)
        {
          tmp.append("border:");
          tmp.append(Integer.toString(borderWidth));
          tmp.append("px solid");
          tmp.append(toCSSString(borderColor));
          tmp.append(";");
        }
        if(backgroundColor!=null)
        {
          tmp.append("background-color:");
          tmp.append(toCSSString(backgroundColor));
          tmp.append(";");
        }
        if(fontDef!=null)
        {
          if(!FontDefinition.DEFAULT_FONT_FAMILY.equals(fontDef.getFamily()))
          {
            tmp.append("font-family:");
            tmp.append(fontDef.getFamily());
            tmp.append(";");
          }
          if(!FontDefinition.DEFAULT_FONT_WEIGHT.equals(fontDef.getWeight()))
          {
            tmp.append("font-weight:");
            tmp.append(fontDef.getWeight());
            tmp.append(";");
          }
          if(!FontDefinition.DEFAULT_FONT_STYLE.equals(fontDef.getStyle()))
          {
            tmp.append("font-style:");
            tmp.append(fontDef.getStyle());
            tmp.append(";");
          }
          if(FontDefinition.DEFAULT_FONT_SIZE!=fontDef.getSize())
          {
            tmp.append("font-size:");
            tmp.append(fontDef.getSize());
            tmp.append("pt;");
          }
        }
        cssStyleCache=tmp.toString().toCharArray();
    }
    out.write(cssStyleCache);
  }

  
  public void setBackgroundColor(Color color)
  {
    this.backgroundColor=color;
    this.cssStyleCache=null;
    this.resetCache();
  }


  public void setColor(Color color)
  {
    this.foregroundColor=color;
    this.cssStyleCache=null;
    this.resetCache();
  }


  public void resetCache()
  {
    super.resetCache();
    cachedAppendHTML = null;
  }

  public void setErrorDecoration(IClientContext context,String message)
  {
    // TODO:Elemente welche in einem InformBrowser sind haben kein boundingRect.
    // An diesen kann im Moment keine Decoration gesetzt werden.
    //
    if(boundingRect!=null)
    {
      lastErrorMessage = new FastStringWriter();
      lastErrorMessage.write("<img title=\"");
      lastErrorMessage.write(StringUtil.htmlEncode(message,""));
      lastErrorMessage.write("\" src=\"");
      lastErrorMessage.write(((ClientSession)context.getSession()).getTheme().getImageURL("exclamation.png"));
      lastErrorMessage.write("\" style=\"");
      lastErrorMessage.write("position:absolute;top:");
      lastErrorMessage.write(Long.toString(boundingRect.y-8));
      lastErrorMessage.write("px;left:");
      lastErrorMessage.write(Long.toString(boundingRect.x+boundingRect.width-8));
      lastErrorMessage.write("px;");
      lastErrorMessage.write("z-index:1000;\"/>");
    }
    
    // Falls das Element in einer TabGroup ist, so muss diese auch invalidiert werden,
    // da der TabStrip den Fehler seiner Kinder anzeigen muss. Dazu muss der Cache des TabStrip
    // gel�scht werden.
    ((GuiElement)this.getParent()).firePropertyChange();
  }

  public void setBounding(Rectangle rect)
  {
    this.boundingRect = rect;
    resetCache();
  }

	/**
	 * @return Returns the cachedHTML.
	 */
  protected final char[] getCachedAppendHTML()
  {
  	return cachedAppendHTML;
  }

	protected final void setCachedAppendHTML(String cachedHTML)
	{
	  this.cachedAppendHTML=cachedHTML.toCharArray();
	}

  public Rectangle getBoundingRect()
  {
    return this.boundingRect;
  }


  public String getEventHandlerReference()
  {
    // TODO Auto-generated method stub
    return null;
  }


  public void marchingAnts(IClientContext context)
  {
    ((ClientContext)context).addOnLoadJavascript("marchingAnts('"+getEtrHashCode()+"')");
  }

}



