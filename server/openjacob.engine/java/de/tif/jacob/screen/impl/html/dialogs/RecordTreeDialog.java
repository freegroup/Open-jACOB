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

package de.tif.jacob.screen.impl.html.dialogs;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IRecordTreeDialogCallback;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.dialogs.HTTPRecordTreeDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.screen.impl.html.GuiHtmlElement;

/**
 *
 */
public class RecordTreeDialog extends HTTPRecordTreeDialog
{
  
  public RecordTreeDialog(HTTPClientContext context, IGuiElement anchor, IDataTableRecord record, IRelationSet relationSet, Filldirection filldirection, IRecordTreeDialogCallback callback) throws Exception
  {
    super(context, anchor, record, relationSet, filldirection, callback);
  }
  
  public void show(int width, int height)
  {
    ClientContext context = (ClientContext)IClientContext.getCurrent();
    
    context.addAdditionalHTML("<script type=\"text/javascript\">\n");
    context.addAdditionalHTML("new jACOBAutocloseWindow('"+((GuiHtmlElement)anchor).getEtrHashCode()+"','"+generateUrl()+"',"+width+","+height+");\n");
    context.addAdditionalHTML("</script>\n");

    ((ClientSession)context.getApplication().getSession()).addDialog(this);
  }
  
  public String generateUrl()
  {
    ClientContext c = (ClientContext)context;
    return "dialogs/RecordTreeDialog.jsp?browser="+c.clientBrowser+"&guid="+getId();
  }
}
