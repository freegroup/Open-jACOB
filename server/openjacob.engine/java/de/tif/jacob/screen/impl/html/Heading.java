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
import java.util.Vector;

import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.HeadingDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IHeading;
import de.tif.jacob.util.StringUtil;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Heading extends GuiHtmlElement implements IHeading
{
  static public final transient String RCS_ID = "$Id: Heading.java,v 1.6 2010/10/15 08:04:46 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";

  private Alignment.Horizontal halign;
  
  private final HeadingDefinition definition;
  
  protected Heading(IApplication app, HeadingDefinition definition)
  {
    super(app, definition.getName(), definition.getCaption().getLabel(), definition.isVisible(), definition.getRectangle(), definition.getProperties());
    this.definition = definition;
    this.halign = definition.getHorizontalAlign();
    this.setFont(definition.getCaption().getFont());
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
  
    if(getCache()==null)
    {
      Writer w = newCache();
      if(halign ==Alignment.LEFT)
      {
	      w.write("<table cellspacing=\"0\" cellpadding=\"0\" class=\"heading\" style=\"");
	      getCSSStyle(context, w,boundingRect);
	      w.write("\" id=\"");
	      w.write(getEtrHashCode());
	      w.write("\"><tr><td width=\"1\"><span class=\"heading\" style=\"font-family:inherit;font-weight:inherit;font-style:inherit;color:inherit;background-color:inherit;font-size:inherit;white-space:nowrap;text-align:");
	      w.write(halign.toString());
	      w.write("\">");
	      w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
	      w.write("</span></td><td><hr class=\"heading\" ></td></tr></table>");
      }
      else
      {
	      w.write("<table cellspacing=\"0\" cellpadding=\"0\" class=\"heading\" style=\"");
	      getCSSStyle(context, w,boundingRect);
	      w.write("\" id=\"");
	      w.write(getEtrHashCode());
	      w.write("\"><tr><td><hr class=\"heading\" ></td><td width=\"1\"><span class=\"heading\" style=\"white-space:nowrap;text-align:");
	      w.write(halign.toString());
	      w.write("\">");
	      w.write(StringUtil.htmlEncode(getI18NLabel(context.getLocale())));
	      w.write("</span></td></tr></table>");
      }
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
   * a catiopn has no data fields......
   */
  protected void addDataFields(Vector fields)
  {
  }
  
	/**
	 * @return Returns the align.
	 */
	public String getAlign()
	{
		return halign.toString();
	}

	/**
	 * @param align The align to set.
	 */
	public void setAlign(String align)
	{
	  resetCache();
    // TODO: API �ndern
	//	this.halign = Alignment.Horizontal.. halign;
	}

	public String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
}
