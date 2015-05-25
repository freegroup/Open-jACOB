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

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.LongTextEditMode;
import de.tif.jacob.core.definition.guielements.LongTextInputFieldDefinition;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.HTTPLongText;
import de.tif.jacob.util.StringUtil;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LongText extends SingleDataGUIElement implements HTTPLongText
{
  static public final transient String RCS_ID = "$Id: LongText.java,v 1.13 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.13 $";

  private final LongTextInputFieldDefinition definition;
  private boolean hasRecordValue;
  
  private LongTextEditMode editMode;
  private ICaption caption;
  
  protected LongText(IApplication app, LongTextInputFieldDefinition text)
  {
    super(app, text.getName(), null, text.isVisible(), text.isReadOnly(), text.getRectangle(), text.getLocalTableAlias(), text.getLocalTableField(), null, text.getProperties());
    definition = text;
    if (null != text.getCaption())
      addChild(caption = new Caption(app, text.getCaption()));

    this.editMode = text.getEditMode();
  }
  
  public int getTabIndex()
  {
    return definition.getTabIndex();
  }

  
  public ICaption getCaption()
  {
    return caption;
  }


  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid != this.getId())
      return super.processEvent(context,guid,event,value);

    return Command.processEvent(this,context,guid,event,value);
  }

  public void setEditMode(LongTextEditMode editMode)
  {
    if (editMode == null)
      this.editMode = this.definition.getEditMode();
    else
      this.editMode = editMode;
    invalidate();
  }

  public LongTextEditMode getEditMode()
  {
    return editMode;
  }


  public boolean hasWordwrap()
  {
    return definition.isWordwrap();
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
      Writer w = newCache();
      if(getDataStatus()==SELECTED && this.hasRecordValue)
      {
        w.write("\t<a class=\"longtext_normal\" href=\"javascript:FireEvent('");
        w.write(Integer.toString(getId()));
        w.write("', 'show')\" ");
        w.write(">\n<img id=\""+this.getEtrHashCode() +"\" title=\"");
        w.write(StringUtil.htmlEncode(I18N.getCoreLocalized("TOOLTIP_SHOW_CONTENT", context)));
        w.write("\" style=\"");
        getCSSStyle(context, w, boundingRect);
        w.write("\" class=\"longtext_normal\" border=\"0\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("longtext.png"));
        w.write("\">\n</a>\n");
        if(this.caption!=null)
          this.caption.setLink("javascript:FireEvent('"+Integer.toString(getId())+"', 'show')");
      }
      else if((getDataStatus()==UPDATE) || (getDataStatus()==NEW))
      {
        w.write("\t<a class=\"longtext_normal\" href=\"javascript:FireEvent('");
        w.write(Integer.toString(getId()));
        w.write("', 'edit')\" ");
        w.write(">\n<img id=\""+this.getEtrHashCode() +"\" title=\"");
        w.write(StringUtil.htmlEncode(I18N.getCoreLocalized("TOOLTIP_EDIT_CONTENT", context)));
        w.write("\" style=\"");
        getCSSStyle(context, w, boundingRect);
        w.write("\" class=\"longtext_normal\" border=\"0\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("longtext.png"));
        w.write("\">\n</a>\n");
        if(this.caption!=null)
          this.caption.setLink("javascript:FireEvent('"+Integer.toString(getId())+"', 'edit')");
      }
      else
      {
        w.write("\t<img id=\""+this.getEtrHashCode() +"\" alt=\"\" class=\"longtext_disabled\" src=\"");
        w.write(((ClientSession)context.getSession()).getTheme().getImageURL("longtext_disabled.png"));
        w.write("\" style=\"");
        getCSSStyle(context, w, boundingRect);
        w.write("\" >");
        if (this.caption != null)
          this.caption.setLink((String) null);
      }
    }
    // render all childs e.g The label/caption of a text field.
    //
    super.calculateHTML(context);
  }
  
  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    super.writeHTML(context, w);
    writeCache(w);
  }
  
  public ITableField getTableField()
  {
    return getDataField().getField();
  }

  /**
   *
   */
  public void setValue(IClientContext context, IDataTableRecord record) throws NoSuchFieldException
  {
    ITableField tableField = getDataField().getField();
    if (tableField != null)
    {
      boolean hasRecordValue = record != null && !record.hasNullValue(tableField.getFieldIndex());
      if (this.hasRecordValue ^ hasRecordValue)
      {
        resetCache();
        this.hasRecordValue = hasRecordValue;
      }

      if (this.hasRecordValue)
      {
        // Im append oder prepend mode muss das Datafield leer initialisiert
        // werden.
        if (getEditMode() == LongTextEditMode.FULLEDIT)
        {
          setValue(record.getStringValue(tableField.getFieldIndex()));
          return;
        }
      }
    }
    setValue("");
  }
  
  /**
   * @return Returns the isEditable.
   */
//  public boolean isEditable()
//  {
//    return super.isEditable() && (definition.getMode()!=LongTextInputMode.READONLY);
//  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
}
