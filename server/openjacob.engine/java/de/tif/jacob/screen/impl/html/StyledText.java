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
import java.net.URL;
import java.util.Locale;
import java.util.Vector;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.LabelDefinition;
import de.tif.jacob.core.definition.guielements.StyledTextDefinition;
import de.tif.jacob.letter.ILetter;
import de.tif.jacob.letter.LetterFactory;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IStyledText;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StyledText extends GuiHtmlElement implements IStyledText
{
  static public final transient String RCS_ID = "$Id: StyledText.java,v 1.2 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  
  private final StyledTextDefinition definition;
  
  protected StyledText(IApplication app, StyledTextDefinition label)
  {
    super(app, label.getName(),label.getContent(), label.isVisible(), label.getRectangle(), label.getProperties());
    this.definition = label;
  }

  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    return false;
  }

  /**
   * return the HTML representation of this object
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    if(!isVisible())
      return;
    // Jetzt wird es bitter. Da sich der DataAccessor immer ändern kann ohne
    // dass sich die zugehörige Gruppe geändert hat, muss der Cache bei jeder HTML Berechnung
    // gelöscht werden.
    if(definition.getPostProcessWithLetterEngine())
      resetCache();
    
    if(getCache()==null)
    {
      Writer w = newCache();
      w.write("\t<div style=\"");
      getCSSStyle(context, w,boundingRect);
      w.write("\">");
      if(definition.getPostProcessWithLetterEngine())
      {
        ILetter letter= LetterFactory.createInstance(LetterFactory.MIMETYPE_HTML);
        w.write(new String(letter.transform(context,context.getDataAccessor(),getI18NLabel(context.getLocale()).getBytes())));
      }
      else
      {
        w.write(getI18NLabel(context.getLocale()));
      }
      w.write("</div>\n");
    }
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
// FREEGROUP: Performanceumstellung - Label hat keine Kindelemente aufruf kann entfallen    
//    super.writeHTML(w);
    writeCache(w);
  }
  /**
   * a catiopn has no searchable data fields......
   */
  protected void addDataFields(Vector fields)
  {
  }
  

	public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
}
