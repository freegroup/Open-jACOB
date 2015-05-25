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

import de.tif.jacob.core.definition.guielements.TimeInputFieldDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.ITime;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Time extends SingleDataGUIElement implements ITime
{
  static public final transient String RCS_ID = "$Id: Time.java,v 1.8 2010/10/15 11:40:30 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";

  private final TimeInputFieldDefinition definition;
  private ICaption caption;

  /**
   * 
   * @param parent
   * @param dateTime
   */
  protected Time(IApplication app,  TimeInputFieldDefinition date)
  {
    super(app, date.getName(), null,date.isVisible(),date.isReadOnly(), date.getRectangle(), date.getLocalTableAlias(), date.getLocalTableField(), date.getFont(), date.getProperties());

    if (null != date.getCaption())
      addChild(caption=new Caption(app,  date.getCaption()));
    
    // Da um das Element eine HTML Tabelle ist, muss die Höhe korrigiert werden und an
    // der rechten Seite muss Platz für ein Button sein welcher 16 Pixel groß ist
    if(boundingRect!=null)
    {  
      boundingRect.width+=16;
      
      // TODO: Hack, weil scheinbar Tabelle Pixels klaut
      boundingRect.height+=2;
    }
    definition = date;
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
    if (!isVisible())
      return;
    if (getCache() == null)
    {
      // render all childs e.g The label/caption of a text field.
      // FREEGROUP: PERFORMANCE UMSTELLUNG
      // Die Kinder eines Eingabefeldes (bis jetzt immer 'Caption') müssen nur
      // dann
      // neu berechnet werden, wenn sich das Textelement selbst verändert hat
      // oder noch nie gezeichnet worden ist.
      //
      super.calculateHTML(context);
      String eventGUID = getEtrHashCode();
      Writer w = newCache();
      w.write("<table cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;position:absolute;");
      getCSSStyle(context, w, boundingRect);
      w.write(" id=\"container_");
      w.write(eventGUID);
      w.write("\"><tr><td><input onFocus=\"onFocusElement(this,'");
      w.write(getName());
      w.write("');\" style=\"width:100%;height:97%\" type=\"text\"");
      if (getTabIndex() >= 0)
      {
        w.write(" TABINDEX=\"");
        w.write(Integer.toString(getTabIndex()));
        w.write("\"");
      }
      w.write(" id=\"");
      w.write(eventGUID);
      w.write("\"");
      if (context.isDebug())
      {
        w.write(" title=\"");
        w.write(getDataField().toString());
        w.write("\"");
        w.write(" guidForTestFramework=\"");
        w.write(getPathName());
        w.write("\"");
      }
      w.write(" name=\"");
      w.write(eventGUID);
      w.write("\" value=\"");
      w.write(StringUtil.htmlEncode(getValue()));
      w.write("\"");
      if (isEnabled() && isEditable())
      {
        w.write(" class=\"datetime_normal editable_inputfield\">");
      }
      else
      {
        w.write(" readonly class=\"datetime_disabled\">");
      }
      // FREEGROUP: Hier wird erst einmal nur das Time-Icon angehängt, ohne
      // spezielle Funktion, damit man zumindest mal erkennt, daß
      // es sich überhaupt um ein Timefeld handelt. Wünschenswert
      // wäre beim Klicken auf das Icon eine Auswahlliste mit Uhrzeiten
      // "00:30", "01:00", etc. Achtung: Muß I18N fähig sein!!!
      w.write("</td><td width=\"16\">");
      w.write("<img src=\"");
      w.write(((ClientSession) context.getSession()).getTheme().getImageURL("time.png"));
      w.write("\" border=\"0\" >\n");
      w.write("</td></tr></table>");
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

    if(getDataField().isDiverse())
      context.addOnLoadJavascript("initBehaviourDiverse('"+getId()+"');");
  }

  public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
