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

import de.tif.jacob.core.Property;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.event.IHotkeyEventHandler;
import de.tif.jacob.screen.event.ITextFieldEventHandler;
import de.tif.jacob.screen.event.KeyEvent;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Text extends SingleDataGUIElement implements IText
{
  static public final transient String RCS_ID = "$Id: Text.java,v 1.18 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.18 $";

  TextInputFieldDefinition definition = null;
  private ICaption caption;
  private int keyMask =0;
  private Alignment.Horizontal halign;
  
  protected Text(IApplication app, TextInputFieldDefinition text)
  {
    super(app, text.getName(), null, text.isVisible(), text.isReadOnly(), text.getRectangle(), text.getLocalTableAlias(), text.getLocalTableField(), text.getFont(), text.getProperties());
    definition = text;
    this.halign = text.getHorizontalAlign();
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
   * The framework call this method if this event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId())
    {
      GuiEventHandler handler= getEventHandler(context);
      if(event.equals("autosuggest"))
      {
      	IAutosuggestProvider provider = (IAutosuggestProvider)handler;
      	IAutosuggestProvider.AutosuggestItem[] items = ((HTTPClientSession)context.getSession()).getAutosuggestValues(provider);
      	provider.suggestSelected(context,items[Integer.parseInt(value)]);
      }
      else if(event.equals("hotkey"))
      {
        int keycode = Integer.parseInt(value);
        switch(keycode)
        {
          case KEYCODE_ENTER:
            ((IHotkeyEventHandler)handler).keyPressed(context, new KeyEvent(KeyEvent.VK_ENTER,this));
            break;
          default:
            break;
        }
      }
      return true;
    }
    
    return super.processEvent(context, guid, event, value);
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
      GuiEventHandler handler= getEventHandler(context);
    	
      // Die Kinder eines Eingabefeldes (bis jetzt immer 'Caption') muessen nur dann
      // neu berechnet werden, wenn sich das Textelement selbst veraendert hat
      // oder noch nie gezeichnet worden ist.
      //
      super.calculateHTML(context);
      
      Writer w = newCache();
      if(handler instanceof IAutosuggestProvider)
      {
      	w.write("<div name=\"autosuggest_");
        w.write(getEtrHashCode());
        w.write("\" id=\"autosuggest_");
        w.write(getEtrHashCode());
        w.write("\" class=\"suggestions\" style=\"visibility:hidden;\" ></div>");
      }
      w.write("<input autocomplete=\"");
      w.write("on".equalsIgnoreCase(Property.TEXT_AUTOCOMPLETE.getValue()) ? "on" : "off");
      w.write("\" style=\"");
      getCSSStyle(context, w, boundingRect);
      if(this.halign!=null)
      {
        w.write(";text-align:");
        w.write(halign.toString());
      }
      w.write("\" type=\"text\"");
      if(handler instanceof IAutosuggestProvider && isEditable() && isEnabled())
        w.write(" autosuggest=\"true\" ");
      if(getTabIndex()>=0)
      {
      	w.write(" TABINDEX=\"");
      	w.write(Integer.toString(getTabIndex()));
      	w.write("\"");
      }
      w.write(" name=\"");
      w.write(getEtrHashCode());
      w.write("\" eventId=\"");
      w.write(Integer.toString(getId()));
      w.write("\" id=\"");
      w.write(getEtrHashCode());
      w.write("\"");
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
      {
        String onFocus="";
        String onBlur="";
        if(handler instanceof IHotkeyEventHandler)
        {
          String groupGuid = ((Group)context.getGroup()).getEtrHashCode();
          onBlur="stopKeyEventing('"+groupGuid+"',false);";
          onFocus="stopKeyEventing('"+groupGuid+"',true);";
        }
        w.write(" class=\"text_normal editable_inputfield\" ");
        if(definition.getInputHint()!=null && StringUtils.isEmpty(getValue()))
        {
          w.write(" onFocus=\""+onFocus+"onFocusElement(this,'"+getName()+"');getObj('hint_");
          w.write(getEtrHashCode());
          w.write("').style.display='none';\" onblur=\""+onBlur+"if(this.value=='')getObj('hint_");
          w.write(getEtrHashCode());
          w.write("').style.display='block';\">\n");
          w.write("<div class=\"input_hint\" style=\"");
          getCSSStyle(context, w, boundingRect);
          w.write("\" onClick=\"setFocus('");
          w.write(getEtrHashCode());
          w.write("',0)\" id=\"hint_");
          w.write(getEtrHashCode());
          w.write("\" >");
          w.write(I18N.localizeLabel(definition.getInputHint(),context));
          w.write("</div>");
        }
        else
        {
          w.write(" onBlur=\""+onBlur+"\" onFocus=\""+onFocus+"onFocusElement(this,'"+getName()+"')\" ");
          w.write(" >\n");
        }
      }
      else
      {
        if (isEnabled())
        	w.write(" readonly class=\"text_normal\" >\n");
        else
          w.write(" readonly class=\"text_disabled\" >\n");
      }
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
    // wenn das Textelemente IAutosuggestProvider implementiert, dann muss sich das Elemente
    // in der Applikation registrieren und den zus�tzlichen javascript Code einarbeiten
    //
    GuiEventHandler handler= getEventHandler(context);
    if(handler instanceof IAutosuggestProvider)
    {
      ((HTTPClientSession)context.getSession()).removeAutosuggestValues((IAutosuggestProvider)handler);
      ((HTTPClientSession)context.getSession()).addAutosuggestProvider(this, (IAutosuggestProvider)handler);
      context.addOnLoadJavascript("new AutoSuggestControl(getObj('"+getEtrHashCode()+"'));");
    }
    if(keyMask !=0)
    {
      context.addAdditionalHTML("\n<script  type=\"text/javascript\">\n");
      context.addAdditionalHTML("addKeyHandler(getObj('"+getEtrHashCode()+"'));\n");
      
      // process the VK_ENTER key if the user had add them to the key mask
      //
      if((keyMask&KeyEvent.VK_ENTER)!=0)
        context.addAdditionalHTML("getObj('"+getEtrHashCode()+"').addKeyDown("+KEYCODE_ENTER+",function(){FireEventData('"+getId()+"','hotkey',"+KEYCODE_ENTER+")});\n");
      
      context.addAdditionalHTML("</script>\n");
    }

    if(getDataField().isDiverse())
      context.addOnLoadJavascript("initBehaviourDiverse('"+getEtrHashCode()+"');");
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
    if(obj instanceof ITextFieldEventHandler)
      ((ITextFieldEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
    
    if(obj instanceof IHotkeyEventHandler)
      keyMask = ((IHotkeyEventHandler)obj).getKeyMask(context);
  }
  
  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
