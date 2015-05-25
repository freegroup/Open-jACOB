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

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.DocumentInputFieldDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.messaging.Message;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IDocumentFieldEventHandler;
import de.tif.jacob.screen.impl.DocumentAction;
import de.tif.jacob.screen.impl.DocumentActionDelete;
import de.tif.jacob.screen.impl.DocumentActionUpload;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.HTTPDocument;
import de.tif.jacob.util.StringUtil;

/**
 *
 *
 * @author Andreas Herz
 */
public class Document extends SingleDataGUIElement implements HTTPDocument
{
  static public final transient String RCS_ID = "$Id: Document.java,v 1.12 2010/10/15 11:40:30 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.12 $";

  // Don't make this elements static!! The upload action contains final variables for the upload
  //
  private final DocumentAction actionUpload = new DocumentActionUpload();
  private final DocumentAction actionDelete = new DocumentActionDelete();
  
  private final DocumentInputFieldDefinition definition;
  private ICaption caption;
  
  /**
   * 
   * @param parent
   * @param dateTime
   */
  protected Document(IApplication app,  DocumentInputFieldDefinition def)
  {
    super(app, def.getName(), null,def.isVisible(),def.isReadOnly(), def.getRectangle(), def.getLocalTableAlias(), def.getLocalTableField(), def.getFont(), def.getProperties());

    if (null != def.getCaption())
      addChild(caption=new Caption(app,  def.getCaption()));
    
    // Da um das Element eine HTML Tabelle ist, muss die Höhe korrigiert werden und an
    // der rechten Seite muss Platz für ein Button sein welcher 16 Pixel groß ist
    if(boundingRect!=null)
    {  
      boundingRect.width=boundingRect.width+16;
      boundingRect.height+=2;
    }

    definition   = def;
    getDataField().setValue(null);
  }
  
  
  public ICaption getCaption()
  {
    return caption;
  }

  
  public int getTabIndex()
  {
    return definition.getTabIndex();
  }
  

  /**
   * 
   */
  public void setValue(IClientContext context, IDataTableRecord record) throws Exception
  {
    resetCache();
    ITableField tableField = getDataField().getField();
    if (record != null && tableField != null)
    {
      Object value = record.getValue(tableField.getFieldIndex());
      getDataField().setValue(value);
    }
    else
    {
      getDataField().setValue(null);
    }
  }
  
  
  /**
   * 
   * @param value
   */
  public void setValue(String value)
  {
    // Don't invalidate the cache if the new data the same
    //
    Object save = getDataField().getValue();
    if (save instanceof String && StringUtils.equals(value, (String) save))
      return;
    
    resetCache();
  	getDataField().setValue(value);
  }
  
  public String getValue()
  {
    Object value = getDataField().getValue();
    if (value != null)
    {
      // value is either a DocumentValue or a String
      return value instanceof DataDocumentValue ? ((DataDocumentValue) value).getName(): (String) value;
    }
    return null; 
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#processParameter(int, java.lang.String)
   */
  public boolean processParameter(int guid, String data) throws IOException, NoSuchFieldException
  {
    // If a record has been backfilled or a record is created, setValue() (called by 
    // super.processParameter() has to be avoided!
    if (getDataStatus() == UPDATE || getDataStatus() == NEW || getDataStatus() == SELECTED)
      return guid==this.getId();
    return super.processParameter(guid, data);
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
      super.calculateHTML(context);
      
      String eventGUID = getEtrHashCode();
      String containerGUID="container_"+eventGUID;
      Writer w = newCache();
      w.write("<table  id=\""+containerGUID+"\" class=\"document\" cellspacing=\"0\" cellpadding=\"0\"  style=\"table-layout:fixed;");
    	getCSSStyle(context,w, boundingRect);
    	w.write("\" ><tr><td style=\"margin:0px\"><input onFocus=\"onFocusElement(this,'"+getName()+"')\" ");
    	
			w.write("style=\"width:100%;height:100%\" type=\"text\" ");
      if(getTabIndex()>=0)
      {
      	w.write(" TABINDEX=\"");
      	w.write(Integer.toString(getTabIndex()));
      	w.write("\" ");
      }
      w.write(" id=\"");
      w.write(eventGUID);
      w.write("\"");
      w.write(" name=\"");
      w.write(eventGUID);
      w.write("\" value=\"");
      Object value = getDataField().getValue();
      DataDocumentValue document = null;
      if (value != null)
      {
        // value is either a DocumentValue or a String
        if (value instanceof DataDocumentValue)
        {
          document = (DataDocumentValue) value;
          w.write(StringUtil.htmlEncode(document.getName()));
        }
        else
          w.write(StringUtil.htmlEncode((String) value));
      }
      w.write("\"");
      
      if(getDataStatus() == SEARCH)
      {
        // Make text field editable to allow document search by name
        w.write(" class=\"document_normal editable_inputfield\" ></td><td class=\"document_icon\" width=\"16\" elementId=\""+containerGUID+"\">");
        w.write("<img class=\"document_disabled\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("document_disabled.png"));
        w.write("\" border=\"0\"></td>");
        w.write("</tr></table>");
      }
      else
      {
        // do upload on input field click
        //
        w.write(" readonly class='document_normal'></td><td class=\"document_icon\" width=\"16\" >");
        w.write("<img id=\"docimg_"+getId()+"\" elementId=\""+containerGUID+"\"  class=\"document_normal\" src=\"");
        if(document!=null)
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("document.png"));
        else
          w.write(((ClientSession)context.getSession()).getTheme().getImageURL("document_disabled.png"));
        w.write("\" border=\"0\" alt=\"");
        if(document!=null)
          w.write(I18N.getCoreLocalized("DOCUMENT_ACTION_TOOLTIP_SHOW", context));
        w.write("\"/></td>\n");
        w.write("</tr></table>");
      }
    }
    // Kontextmenu muss ans Ende des Dokumentes
    //
    
    if(getDataStatus() != SEARCH)
    {
      Object value = getDataField().getValue();
      // Handling for IDataMultiUpdateTableRecord.DIVERSE
      DataDocumentValue document = value instanceof DataDocumentValue ? (DataDocumentValue) value : null;
      context.addContextmenuAdditionalHTML("<div style=\"border:1px solid gray;position:absolute;display:none;\" class=\"contextMenu\" id=\"document_menu_"+getId()+"\">");
      context.addContextmenuAdditionalHTML("<table  cellspacing=\"0\" cellpadding=\"0\" class=\"contextMenu\">");
      // Anzeigen ist bei vorhandenen Dokument immer möglich wenn das Element nicht disabled ist
      if(document!=null && this.isEnabled())
      {
        String showUrl=document==null?"#":getDataField().getDownloadURL((HTTPClientSession)context.getSession());
        context.addContextmenuAdditionalHTML("<tr><td class='contextMenuItem'><a class=\"contextMenuItem\" href=\""+showUrl+"\">"+I18N.getCoreLocalized("BUTTON_COMMON_SHOW", context)+"</a></td></tr>\n");
      }
      else
        context.addContextmenuAdditionalHTML("<tr><td class='contextMenuItem'><a class=\"contextMenuItemDisabled\" href=\"#\">"+I18N.getCoreLocalized("BUTTON_COMMON_SHOW", context)+"</a></td></tr>\n");
      
      // Bearbeiten ist möglich wenn document vorhanden und im UPDATE/NEW Modus
      // UND wenn es sich um einen InternetExplorer handelt!!!!
      if(isEditable() && document!=null && (getDataStatus() == UPDATE || getDataStatus() == NEW) && "ie".equals(((ClientSession)context.getSession()).getClientBrowserType()))
      {
        String davUrl=document==null?"#":getDataField().getWebdavURL((HTTPClientSession)context.getSession());
        String mimeType = Message.getMimeType(document.getName());
        context.addContextmenuAdditionalHTML("<tr><td class='contextMenuItem'><a class=\"contextMenuItem\" href=\"javascript:editDocument('"+davUrl+"','"+mimeType+"')\">"+I18N.getCoreLocalized("BUTTON_COMMON_EDIT", context)+"</a></td></tr>\n");
      }
      else
        context.addContextmenuAdditionalHTML("<tr><td class='contextMenuItem'><a class=\"contextMenuItemDisabled\">"+I18N.getCoreLocalized("BUTTON_COMMON_EDIT", context)+"</a></td></tr>\n");
  
      // Hochladen geht im UPDATE oder NEW Modus
      //
      if(isEditable() && (getDataStatus() == UPDATE || getDataStatus() == NEW))
        context.addContextmenuAdditionalHTML("<tr><td class='contextMenuItem'><a class=\"contextMenuItem\" href=\"#\" onclick=\"FireEvent('"+Integer.toString(getId())+"','" + actionUpload.getId() + "')\">"+actionUpload.getI18NLabel(context)+"</a></td></tr>\n");
      else
        context.addContextmenuAdditionalHTML("<tr><td class='contextMenuItem'><a class=\"contextMenuItemDisabled\">"+actionUpload.getLabel(context)+"</a><br>\n");
      
      // Löschen geht wenn Dokument vorhanden und der Modus UPDATE oder NEW ist
      //
      if(isEditable() && document!=null && (getDataStatus() == UPDATE || getDataStatus() == NEW))
        context.addContextmenuAdditionalHTML("<tr><td class='contextMenuItem'><a class=\"contextMenuItem\" href=\"#\"  onclick=\"FireEvent('"+Integer.toString(getId())+"','" + actionDelete.getId() + "')\">"+actionDelete.getI18NLabel(context)+"</a></td></tr>\n");
      else
        context.addContextmenuAdditionalHTML("<tr><td class='contextMenuItem'><a class=\"contextMenuItemDisabled\">"+actionDelete.getLabel(context)+"</a></td></tr>\n");
      context.addContextmenuAdditionalHTML("</table>");
      context.addContextmenuAdditionalHTML("</div>");
    }
    
  }
  

  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid!=this.getId())
      return super.processEvent(context, guid, event, value);

    if(actionUpload.getId().equals(event))
      actionUpload.execute(context,this,value);
    else if(actionDelete.getId().equals(event))
      actionDelete.execute(context,this,value);
    
    return true;
  }
  
  
  public void onDelete(IClientContext context) throws Exception
  {
    Object obj = getEventHandler(context);
    if(obj!=null)
    {
      if(obj instanceof IDocumentFieldEventHandler)
        ((IDocumentFieldEventHandler)obj).afterDelete(context, this);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IDocumentFieldEventHandler.class.getName()+"]");
    }
  }

  
  public void onUpload(IClientContext context, String docName, byte[] doc) throws Exception
  {
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the button
    //
    Object obj = getEventHandler(context);
    if(obj!=null)
    {
      if(obj instanceof IDocumentFieldEventHandler)
        ((IDocumentFieldEventHandler)obj).afterUpload(context, this, docName, doc);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IDocumentFieldEventHandler.class.getName()+"]");
    }
  }



  /* 
   * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context,GroupState groupStatus)  throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the button
    //
    Object obj = getEventHandler(context);
    if(obj!=null)
    {
      if(obj instanceof IDocumentFieldEventHandler)
        ((IDocumentFieldEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IDocumentFieldEventHandler.class.getName()+"]");
    }
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);
    context.addOnLoadJavascript("initBehaviourDocument('container_"+getEtrHashCode()+"');");
  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
