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
import java.util.Collections;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.LongTextEditMode;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.definition.guielements.InFormLongTextInputFieldDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.ILinkParser;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.ILinkEventListener;
import de.tif.jacob.screen.event.ITextFieldEventHandler;
import de.tif.jacob.screen.impl.IDProvider;
import de.tif.jacob.util.StringUtil;
/**
 *
 */
public class InFormLongText extends SingleDataGUIElement implements IInFormLongText
{
  static public final transient String RCS_ID = "$Id: InFormLongText.java,v 1.29 2010/10/27 13:32:38 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.29 $";
  private final InFormLongTextInputFieldDefinition definition;
  private final int selectionStartId;
  private final int selectionEndId;
  private final int caretPositionId;
  private int selectionStart;
  private int selectionEnd;
  private int caretPosition;
  Rectangle editModeBoundingRect_view;
  Rectangle editModeBoundingRect_edit;
  private LongTextEditMode editMode;
  private ILinkParser linkParser = null;
  private ILinkEventListener linkListener=null;
  
  // only for test prupose
  private InFormLongText()
  {
    super(null,null,null, true,true,null,null,null,null, Collections.EMPTY_MAP);
    selectionStartId = IDProvider.next();
    selectionEndId = IDProvider.next();
    caretPositionId = IDProvider.next();
    definition=null;
   }
  
  protected InFormLongText(IApplication app, InFormLongTextInputFieldDefinition text)
  {
    super(app, text.getName(), null, text.isVisible(), text.isReadOnly(), text.getRectangle(), text.getLocalTableAlias(), text.getLocalTableField(), text.getFont(), text.getProperties());
    definition = text;
    if (null != text.getCaption())
      addChild(new Caption(app, text.getCaption()));
    setEditMode( text.getEditMode());

    selectionStartId = IDProvider.next();
    selectionEndId = IDProvider.next();
    caretPositionId = IDProvider.next();
  }

  public int getTabIndex()
  {
    return definition.getTabIndex();
  }

  public ICaption getCaption()
  {
    return null;
  }

  public void setEditMode(LongTextEditMode editMode)
  {
    if (editMode == null)
      this.editMode = this.definition.getEditMode();
    else
      this.editMode = editMode;
    
    // bounding box korrigieren wenn die Element in APPEND / PREPEND Mode sind.
    //
    if (editMode == LongTextFieldType.APPEND)
    {
      editModeBoundingRect_view = new Rectangle(this.boundingRect);
      editModeBoundingRect_edit = new Rectangle(this.boundingRect);
      editModeBoundingRect_view.height = editModeBoundingRect_view.height / 2;
      editModeBoundingRect_edit.height = this.boundingRect.height - editModeBoundingRect_view.height;
      editModeBoundingRect_edit.y = editModeBoundingRect_view.y + editModeBoundingRect_view.height;
    }
    else if (editMode == LongTextFieldType.PREPEND)
    {
      editModeBoundingRect_view = new Rectangle(this.boundingRect);
      editModeBoundingRect_edit = new Rectangle(this.boundingRect);
      editModeBoundingRect_edit.height = editModeBoundingRect_edit.height / 2;
      editModeBoundingRect_view.height = this.boundingRect.height - editModeBoundingRect_edit.height;
      editModeBoundingRect_view.y = editModeBoundingRect_edit.y + editModeBoundingRect_edit.height;
    }
    
    invalidate();
  }

  public LongTextEditMode getEditMode()
  {
    return editMode;
  }

  public boolean processParameter(int guid, String data) throws IOException, NoSuchFieldException
  {
    if (guid == caretPositionId)
    {
      caretPosition = Integer.parseInt(data);
      return true;
    }
    else if (guid == selectionStartId)
    {
      selectionStart = Integer.parseInt(data);
      return true;
    }
    else if (guid == selectionEndId)
    {
      selectionEnd = Integer.parseInt(data);
      return true;
    }
    // Blöder Fehler des nicEditor. Wenn dieser leer ist, dann wird als "value" eine LHTML Leerzeile zurückgeliefert.
    // Entweder die Implementierung vom nicEditor anpassen oder hier ein BugFix. 
    // Wenn implementierung des nixEditor angefasst wird, dann muss man bei BugFixes diesen jedesmal Patchen. :-(
    // => hier diesen gesonderten Fall abfangen.
    if(guid==this.getId() &&  isEditable() && "<br>".equals(data)&&definition.getContentType().equals("text/html"))
      data="";

    return super.processParameter(guid, data);
  }

  /**
   * Return HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if (!isVisible())
      return;
    // render all childs e.g The label/caption of a text field.
    //
    super.calculateHTML(context);
    if (getCache() == null)
    {
      String eventGUID = getEtrHashCode();
      Writer w = newCache();
      String groupGuid = ((Group) context.getGroup()).getEtrHashCode();
      Rectangle rect = this.boundingRect;
      if (isEditable() && isEnabled())
      {
        // Textblock mit dem bereits vorhandenem Inhalt des Textfeldes
        //
        if (getDataStatus()!=SEARCH && (editMode == LongTextFieldType.APPEND || editMode == LongTextFieldType.PREPEND))
        {
          cssStyleCache = null;
          rect = editModeBoundingRect_edit;
          IDataTableRecord record = context.getSelectedRecord();
          // render the current existing text in a read only textarea
          //
          if (definition.getContentType().equals("text/html"))
          {
            w.write("\t<div style=\"overflow:auto;");
            getCSSStyle(context, w, editModeBoundingRect_view);
            w.write("\"  class=\"longtext_normal\"");
            w.write(" >");
            if(this.linkParser!=null)
              w.write(this.linkParser.parse(context,this, record.getSaveStringValue(definition.getLocalTableField().getName())));
            else
              w.write(record.getSaveStringValue(definition.getLocalTableField().getName()));
            w.write("</div>");
          }
          else
          {
          w.write("\t<textarea readonly class=\"longtext_normal\" style=\"");
          getCSSStyle(context, w, editModeBoundingRect_view);
          w.write("\">");
          // Attention: use getSaveStringValue() instead of
          // getOldSaveStringValue() here, since otherwise the text of copied
          // records is not displayed
          if (record != null)
            w.write(StringUtil.htmlEncode(record.getSaveStringValue(definition.getLocalTableField().getName())));
          w.write("</textarea>");
          }
          cssStyleCache = null;
        }
        w.write("\t<textarea");
        // Falls das longtextfeld auf ein 'normales' varchar gemappt ist, dann
        // muss zusaetzlich die Laengenbegrenzung des Feldes beachtet werden.
        //
        ITableField tableField = definition.getLocalTableField();
        if (tableField != null && tableField.getType() instanceof TextFieldType)
        {
          int maxLength = ((TextFieldType) tableField.getType()).getMaxLength();
          if (maxLength != 0)
            w.write(" maxCharSize=\"" + maxLength + "\" onKeyUp=\"checkMaxLength(this);\"");
        }
        w.write(" onBlur=\"stopKeyEventing('" + groupGuid + "',false);\" onFocus=\"onFocusElement(this,'" + getName() + "');stopKeyEventing('" + groupGuid + "',true);\" style=\"");
        getCSSStyle(context, w, rect);
        
        if (!definition.isWordwrap())
          w.write("\" wrap=\"off");
        
        if (getTabIndex() >= 0)
        {
          w.write("\" TABINDEX=\"");
          w.write(Integer.toString(getTabIndex()));
        }
        
        // contentType nicht rausschreiben wenn das Element im Suchmodus ist. Es kann sonst
        // passieren, dass in der Suchmaske ein HTMLEditor erscheint. Würde bei der Suche keine
        // Treffer mehr bringen, da dann mit HTML TAGs wie <br> gesucht wird.
        if(getDataStatus()!=SEARCH)
        {
           w.write("\" contentType=\"");
           w.write(definition.getContentType());
        }
        
        w.write("\" name=\"");
        w.write(eventGUID);
        w.write("\"");
        w.write(" id=\"");
        w.write(eventGUID);
        w.write("\"");
        w.write(" class=\"longtext_normal editable_inputfield\"");
        w.write(" >");
        w.write(StringUtil.htmlEncode(getValue()));
        w.write("</textarea>");
      }
      else
      {
        if (definition.getContentType().equals("text/plain") && this.linkParser!=null)
        {
          w.write("\t<div style=\"overflow:auto;");
          getCSSStyle(context, w, rect);
          w.write("\"");
          w.write(" id=\"");
          w.write(eventGUID);
          w.write("\"");
          
          if (isEnabled())
            w.write("  class=\"longtext_normal\"");
          else
            w.write("  class=\"longtext_disabled\"");
          
          w.write(" ><pre>");
          w.write(this.linkParser.parse(context,this, getValue()));
          w.write("</pre></div>");
        }
        else  if (definition.getContentType().equals("text/html"))
        {
          w.write("\t<div style=\"overflow:auto;");
          getCSSStyle(context, w, rect);
          w.write("\"");
          w.write(" id=\"");
          w.write(eventGUID);
          w.write("\"");
          if (isEnabled())
            w.write("  class=\"longtext_normal\"");
          else
            w.write("  class=\"longtext_disabled\"");
          w.write(" >");
          if(this.linkParser!=null)
            w.write(this.linkParser.parse(context,this, getValue()));
          else
            w.write(getValue());
          
          w.write("</div>");
        }
        else
        // fallback for unknown content type like text/sql, text/js,
        // text/wiki,.....application/ps
        {
          w.write("\t<textarea style=\"");
          getCSSStyle(context, w, rect);
          w.write("\"");
          if (!definition.isWordwrap())
            w.write(" wrap=\"off\" ");
          w.write(" id=\"");
          w.write(eventGUID);
          w.write("\"");
          if (isEnabled())
            w.write(" readonly class=\"longtext_normal\"");
          else
            w.write(" readonly class=\"longtext_disabled\"");
          w.write(" >");
          w.write(StringUtil.htmlEncode(getValue()));
          w.write("</textarea>");
        }
      }
      // write the print icon for the longtext field
      //
      if (getDataStatus() == NEW || getDataStatus() == UPDATE || getDataStatus() == SELECTED)
      {
        w.write("<img src=\"");
        w.write(Icon.printer.getPath(true));
        w.write("\" id=\"fade_");
        w.write(Integer.toString(getId()));
        w.write("\" style=\"");
        w.write("position:absolute;top:");
        w.write(Long.toString(boundingRect.y + 1));
        w.write("px;left:");
        w.write(Long.toString(rect.x + rect.width - 16 - 1));
        w.write("px;\"/>");
      }
    }
  }

  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId())
    {
      if(HtmlLinkRenderStrategy.EVENT_CLICK.equals(event)&& this.linkListener!=null)
      {
        try
        {
          this.linkListener.onClick(context,this, value);
        }
        catch (Exception e)
        {
          ExceptionHandler.handle(e);
        }
      }
      return true;
    }

    return super.processEvent(context, guid, event, value);
  }

  /*
   * @see de.tif.jacob.screen.IInFormLongText#getCaretPosition()
   */
  public int getCaretPosition()
  {
    return caretPosition;
  }

  /*
   * 
   * @see
   * de.tif.jacob.screen.IInFormLongText#setCaretPosition(de.tif.jacob.screen
   * .IClientContext, int)
   */
  public void setCaretPosition(int caretPosition)
  {
    this.getForm().setFocus(this, caretPosition);
  }

  /*
   * 
   * @see de.tif.jacob.screen.IInFormLongText#getSelectionEnd()
   */
  public int getSelectionEnd()
  {
    return selectionEnd;
  }

  /*
   * 
   * @see de.tif.jacob.screen.IInFormLongText#getSelectionStart()
   */
  public int getSelectionStart()
  {
    return selectionStart;
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if (!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);
    if (isEditable() && isEnabled())
    {
      w.write("<input type=\"hidden\" name=\"" + ID_PREFIX + selectionStartId + "\" id=\"" + ID_PREFIX + selectionStartId + "\" value=\"0\">\n");
      w.write("<input type=\"hidden\" name=\"" + ID_PREFIX + selectionEndId + "\"   id=\"" + ID_PREFIX + selectionEndId + "\" value=\"0\">\n");
      w.write("<input type=\"hidden\" name=\"" + ID_PREFIX + caretPositionId + "\"  id=\"" + ID_PREFIX + caretPositionId + "\" value=\"0\">\n");
    }
    context.addOnLoadJavascript("initBehaviourLongText('" + getId() + "', '" + ID_PREFIX + caretPositionId + "', '" + ID_PREFIX + selectionStartId + "', '" + ID_PREFIX + selectionEndId + "');");
  }

  /**
   * Buttons können Ihre Position dadurch verändern, dass diese in einer
   * ButtonBar enthalten sind. aus diesem Grund müssen diese zusätzlich Ihr
   * CSS-Style resetten. Dies ist bei "normalen" Elementen noch nicht notwendig.
   */
  public void resetCache()
  {
    super.resetCache();
    cssStyleCache = null;
  }

  public boolean hasWordwrap()
  {
    return definition.isWordwrap();
  }

  /**
   * 
   * @param value
   */
  public void setValue(IClientContext context, IDataTableRecord record) throws NoSuchFieldException
  {
    ITableField tableField = getDataField().getField();
    if (record != null && tableField != null && !record.hasNullValue(tableField.getFieldIndex()))
    {
      if ((getDataStatus() == NEW || getDataStatus() == UPDATE) && (editMode == LongTextEditMode.APPEND || (editMode == LongTextEditMode.PREPEND)))
        setValue("");
      else
        setValue(record.getStringValue(tableField.getFieldIndex(), context.getLocale()));
      return;
    }
    setValue("");
  }

  /*
   * @see
   * de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de
   * .tif.jacob.screen.IClientContext,
   * de.tif.jacob.screen.IGuiElement.GroupState)
   */
  public void onGroupDataStatusChanged(IClientContext context, GroupState groupStatus) throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the
    // textfield
    // or it can calculate the new value if the element a non DB bounded
    // element.
    //
    Object obj = getEventHandler(context);
    if (obj != null)
    {
      // if(obj instanceof IInformLongTextEventHandler) call
      // ITextFieldEventHandler instead of IInformLongTextEventHandler (be
      // backward compatible)
      if (obj instanceof ITextFieldEventHandler)
        ((ITextFieldEventHandler) obj).onGroupStatusChanged(context, groupStatus, this);
      else
        throw new UserException("Class [" + obj.getClass().getName() + "] does not implement required interface/class [" + ITextFieldEventHandler.class.getName() + "]");
    }
  }

  public void setLinkHandling(ILinkParser parser, ILinkEventListener listener) throws Exception
  {
    this.linkParser = parser;
    this.linkListener = listener;
    
    this.resetCache();
  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
