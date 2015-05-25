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

import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.definition.guielements.PasswordInputFieldDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IPassword;
import de.tif.jacob.screen.event.IPasswordFieldEventHandler;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Password extends SingleDataGUIElement implements IPassword
{
  static public final transient String RCS_ID = "$Id: Password.java,v 1.8 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";

  private final PasswordInputFieldDefinition definition;
  private ICaption caption;
  
  protected Password(IApplication app, PasswordInputFieldDefinition text)
  {
    super(app,  text.getName(), null, text.isVisible(), text.isReadOnly(), text.getRectangle(), text.getLocalTableAlias(), text.getLocalTableField(), text.getFont(), text.getProperties());
    definition = text;
    
    dataField = new DataField(this, text.getLocalTableAlias(), text.getLocalTableField(),""){
    
      // Ist nicht besonders klug wenn man nach einem Passwort suchen kann. Ein passwortField
      // tr�gt somit nichts zur Suche bei.
      public String getQBEValue()
      {
        return "";
      }
    };

    if (null != text.getCaption())
      addChild(caption=new Caption(app,  text.getCaption()));
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
   * Return HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    
    if(getCache()==null)
    {
      // Die Kinder eines Eingabefeldes (bis jetzt immer 'Caption') m�ssen nur dann
      // neu berechnet werden, wenn sich das Textelement selbst ver�ndert hat
      // oder noch nie gezeichnet worden ist.
      //
      super.calculateHTML(context);
      
      Writer w = newCache();
      w.write("<input autocomplete=\"off\" style=\"");
      getCSSStyle(context, w, boundingRect);
      w.write("\" type=\"password\"");
      w.write(" onFocus=\"onFocusElement(this,'"+getName()+"')\" ");
      if(getTabIndex()>=0)
      {
      	w.write("TABINDEX=\"");
      	w.write(Integer.toString(getTabIndex()));
      	w.write("\"");
      }
      w.write(" name=\"");
      w.write(getEtrHashCode());
      w.write("\" id=\"");
      w.write(getEtrHashCode());
      w.write("\" ");
      w.write(" value=\"");
      w.write(StringUtil.htmlEncode( getValue()));
      w.write("\"");
      if(getDataStatus() == UPDATE || getDataStatus()==NEW)
      {
        ITableField tableField = definition.getLocalTableField();
        if (tableField!=null && tableField.getType() instanceof TextFieldType)
        {
          int maxLength = ((TextFieldType) tableField.getType()).getMaxLength();
          if (maxLength != 0)
          {
            w.write(" maxCharSize=\""+maxLength+"\" onKeyUp=\"checkMaxLength(this);\" ");
          }
        }
      }
      if(isEditable() && isEnabled())
        w.write(" class=\"text_normal editable_inputfield\" ");
      else if(!isEditable() && !isEnabled()) 
        w.write(" readonly class=\"text_disabled\" ");
      else if(!isEditable() || !isEnabled())
      	w.write(" readonly class=\"text_normal\" ");
      else
        w.write(" class=\"text_normal\" ");
      w.write(">\n");
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
      if(obj instanceof IPasswordFieldEventHandler)
        ((IPasswordFieldEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IPasswordFieldEventHandler.class.getName()+"]");
    }
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
