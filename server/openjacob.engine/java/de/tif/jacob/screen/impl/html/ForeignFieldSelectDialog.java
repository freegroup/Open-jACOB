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

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import de.tif.jacob.core.Property;
import de.tif.jacob.screen.impl.GuiElement;


/**
 *
 * @author Andreas Herz
 */
public class ForeignFieldSelectDialog
{
  static public final transient String RCS_ID = "$Id: ForeignFieldSelectDialog.java,v 1.3 2010/01/20 16:22:17 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private static final HashMap browsers = new HashMap();
  

  /**
   * Wird aus diversen JSP Seiten (./rpc/...) gerufen
   * 
   * @since 2.9.1
   * @param context
   * @param cacheId
   * @return
   * @throws Exception
   */
  public static ForeignFieldBrowser getBrowser(ClientContext context, String cacheId) throws Exception
  {
    return (ForeignFieldBrowser)browsers.get(cacheId);
  }

  /**
   * Write the HTML code to the writer object
   * 
   * @param out
   * @throws IOException
   */
  public static void renderBrowser(ClientContext context, String cacheId) throws Exception
  {
    ForeignFieldBrowser browser = (ForeignFieldBrowser)browsers.get(cacheId);
    if(browser!=null)
    {
      // free the resource. 
      browsers.remove(cacheId);
      
      // render the content of the ForeignFieldBrowser.
    	browser.calculateHTML(context);
    	browser.writeHTML(context, context.out);
    }
  }

  /**
   * 
   * 
   */
  public static void show( ClientContext context, ForeignFieldBrowser browser) throws Exception
  {
    String a = ((GuiElement)browser.getParent()).getEtrHashCode();
    // render the content of the ForeignFieldBrowser.
    if(!Property.FFBROWSER_INLINE.getBooleanValue())
    {
	    browsers.put(Integer.toString(browser.getId()), browser);
	    context.addAdditionalHTML("<script type=\"text/javascript\">");
	    context.addAdditionalHTML("function foreingField(){showForeignFieldDialog('"+a+"','"+context.clientBrowser+"','"+browser.getId()+"');}");
	    context.addAdditionalHTML("</script>\n");
    }
    else
    {
	    Writer out = new StringWriter();
	    out.write("<div name=foreign_field_inline id=foreign_field_inline style=\"border:2 solid black; background-color:white;position:absolute;top:651px;left:110px;height:120px;\">");
	  	browser.calculateHTML(context);
	  	browser.writeHTML(context, out);
	    out.write("</div>");
	    context.setForeignFieldHTML(out.toString());
	    
	    out = new StringWriter();
	    out.write("<script type=\"text/javascript\">\n");
	    out.write("showForeignFieldDialogInline('"+a+"','foreign_field_inline');\n");
	    out.write("</script>\n\n");

	    context.addAdditionalHTML(out.toString());
    }
  }
}


