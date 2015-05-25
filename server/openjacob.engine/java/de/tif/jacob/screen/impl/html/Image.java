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

import java.awt.Rectangle;
import java.io.IOException;
import java.io.Writer;

import javax.swing.ImageIcon;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.ImageDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.impl.HTTPImage;
import de.tif.jacob.screen.impl.ImageAction;
import de.tif.jacob.screen.impl.ImageActionDelete;
import de.tif.jacob.screen.impl.ImageActionUpload;
import de.tif.jacob.screen.impl.ImageProvider;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Image extends SingleDataGUIElement implements HTTPImage
{
  static public final transient String RCS_ID = "$Id: Image.java,v 1.9 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

  static final String EVENT_UPLOAD = "upload";
  
  private final ImageDefinition definition;

  private int realWidth  = -1;
  private int realHeight = -1;
  
  private final ImageAction deleteAction = new ImageActionDelete();
  private final ImageAction uploadAction = new ImageActionUpload();
  
  protected Image(IApplication app, ImageDefinition definition)
  {
    super(app,  definition.getName(), null, definition.isVisible(), definition.isReadOnly(), definition.getRectangle(), definition.getLocalTableAlias(), definition.getLocalTableField(), null, definition.getProperties());
    this.definition = definition;
    
    if (null != definition.getCaption())
      addChild(new Caption(app,  definition.getCaption()));
  }
  
  /**
   * 
   */
  public void setValue(IClientContext context, IDataTableRecord record) throws Exception
  {
    resetCache();
    realHeight = -1;
    realWidth = -1;
    ITableField tableField = getDataField().getField();
    if (record != null && tableField != null)
    {
      Object value = record.getValue(tableField.getFieldIndex());
      if (value instanceof DataDocumentValue)
      {
        DataDocumentValue docvalue = (DataDocumentValue) value;
        if (docvalue.getContent().length != 0)
        {
          ImageIcon image = new ImageIcon(docvalue.getContent());
          realWidth = image.getIconWidth();
          realHeight = image.getIconHeight();
        }
      }
      getDataField().setValue(value);
    }
    else
    {
      getDataField().setValue(null);
    }
  }
  
  /**
   * 
   */
  public int getTabIndex()
  {
    return definition.getTabIndex();
  }
  
  
  /**
   * Return HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    if(getCache()==null)
    {
      // Die Kinder eines Image (bis jetzt immer 'Caption') m�ssen nur dann
      // neu berechnet werden, wenn sich das Textelement selbst ver�ndert hat
      // oder noch nie gezeichnet worden ist.
      //
      super.calculateHTML(context);
      Rectangle realBounding =(Rectangle) boundingRect.clone();
      if(realHeight!=-1)
      {
	      double zoomWidth  = boundingRect.width / (double)realWidth;
	      double zoomHeight = boundingRect.height/ (double)realHeight;
	      
	      if(zoomWidth<1.0 || zoomHeight<1.0)
	      {
	        if(zoomWidth<zoomHeight)
	        {
	          realBounding.height= (int)( zoomWidth*realHeight);
	          realBounding.y     = realBounding.y+(boundingRect.height-realBounding.height)/2;
	        }
	        else
	        {
	          realBounding.width = (int) (zoomHeight*realWidth);
	          realBounding.x     = realBounding.x+(boundingRect.width-realBounding.width)/2;
	        }
	      }
	      else
	      {
          realBounding.width=realWidth;  
          realBounding.height=realHeight;  
          realBounding.y     = realBounding.y+(boundingRect.height-realBounding.height)/2;
          realBounding.x     = realBounding.x+(boundingRect.width-realBounding.width)/2;
	      }
      }
      Writer w = newCache();
      
      w.write("<img style=\"");
      getCSSStyle(context, w, realBounding);
      w.write("\" ");
      w.write(" name=\"");
      w.write(getEtrHashCode());
      w.write("\" id=\"");
      w.write(Integer.toString(getId()));
      w.write("\"");
      
      // Handling for IDataMultiUpdateTableRecord.DIVERSE
      Object value = getDataField().getValue();
      DataDocumentValue document = value instanceof DataDocumentValue ? (DataDocumentValue) value : null;
      
      if(getDataStatus() == UPDATE || getDataStatus()==NEW)
      {
	      w.write(" class='image_update' onClick=\"FireEvent('");
	      w.write(Integer.toString(getId()));
	      w.write("','"+EVENT_UPLOAD+"')\" ");
      }
      else
      {
        if (document != null && document.getContent().length != 0)
          w.write(" class='image_selected' ");
        else
          w.write(" class='image_empty' ");
      }
      
      w.write(" src=\"image?browser=");
      w.write(context.clientBrowser);
      w.write("&dbImage=");
      w.write(getName());
      w.write("&dummy=");
      w.write(Long.toString(System.currentTimeMillis()));
      w.write("\">\n");
      if((getDataStatus()==UPDATE || getDataStatus()==NEW) && !context.isInReportMode() && isEditable())
      {
        cssStyleCache=null;
        w.write("<span id=\"fade_");
        w.write(Integer.toString(getId()));
        w.write("\" class=\"art_img\" style=\"");
        getCSSStyle(context, w, realBounding);
        w.write("\">");
        w.write("<table cellspacing=\"0\" cellpadding=\"0\" height=\"100%\" width=\"100%\">");
        w.write("<tr><td align=\"right\" valign=\"bottom\">");
        // "upload" ist im update und new modus immer erlaubt
        //
        renderAction(context, w, uploadAction.getId(),"",uploadAction.getIcon(),uploadAction.getIcon(context), uploadAction.getTooltip(context),true);      
        // wenn ein image vorhanden ist, dann kommt die "delete" Aktion hinzu
        //
        if (document != null && document.getContent().length != 0)
          renderAction(context, w, deleteAction.getId(),"",deleteAction.getIcon(),deleteAction.getIcon(context), deleteAction.getTooltip(context),true);
        w.write("</td></tr>");
        w.write("</table>");
        w.write("</span>");
      }
    }
  }
  
  
  public void resetCache()
  {
    cssStyleCache=null;
    super.resetCache();
  }
  
  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);
    context.addOnLoadJavascript("initBehaviourImage('"+getId()+"');");
  }


  /* 
   * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context,GroupState groupStatus)  throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the textfield
    // or it can calculate the new value if the element a non DB bounded element.
    //
    Object obj = getEventHandler(context);
    if(obj!=null)
    {
      /*
      if(obj instanceof ITextFieldEventHandler)
        ((ITextFieldEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+ITextFieldEventHandler.class.getName()+"]");
        */
    }
  }
  
  public byte[] getImageData(IClientContext context, String imageId)
  {
    Object value = getDataField().getValue();
    if (value instanceof DataDocumentValue)
    {
      DataDocumentValue document = (DataDocumentValue) value;
      if (document.getContent().length != 0)
        return document.getContent();
    }

    return ImageProvider.getNoImageFrameImage();
  }
  
  /**
   * The default implementation transfer the data from the event to the GUI guid.
   * (this is not usefull for check boxes!)
   * 
   */
  public boolean processParameter( int guid, String data) throws IOException, NoSuchFieldException
  {
    if(guid==this.getId())
      return true;
    
    return false;    
  }
   
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid!=this.getId())
      return super.processEvent(context, guid, event, value);

    if(event.equals(deleteAction.getId()))
      deleteAction.execute(context,this,value);
    else if(event.equals(uploadAction.getId()))
      uploadAction.execute(context,this,value);
    
    return true;
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
  private final void renderAction(IClientContext context, Writer w, String actionName, String value,String oldStyleIcon, Icon newStyleIcon,String tooltip, boolean reloadPage) throws IOException
  {
    if(reloadPage)
      w.write("<img class=\"image_action\" onClick=\"FireEventData('");
    else
      w.write("<img onClick=\"reloadPage=false;FireEventData('");
    w.write(Integer.toString(getId()));
    w.write("','");
    w.write(actionName);
    w.write("','"+value+"');\" align=\"top\" src=\"");
    if(newStyleIcon!=null)
      w.write(newStyleIcon.getPath(true));
    else
      w.write(((ClientSession)context.getSession()).getTheme().getImageURL(oldStyleIcon));
    w.write("\" title=\"");
    w.write(tooltip);
    w.write("\">");
  }

  public int getRealHeight()
  {
    return realHeight;
  }

  public void setRealHeight(int realHeight)
  {
    this.realHeight = realHeight;
  }

  public int getRealWidth()
  {
    return realWidth;
  }

  public void setRealWidth(int realWidth)
  {
    this.realWidth = realWidth;
  }
}
