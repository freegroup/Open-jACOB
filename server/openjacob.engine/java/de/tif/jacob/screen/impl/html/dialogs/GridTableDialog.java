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

import de.tif.jacob.core.Context;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.impl.dialogs.HTTPGridTableDialog;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.ClientSession;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GridTableDialog  extends HTTPGridTableDialog
{
  final String anchor;
  public GridTableDialog(ClientContext context, String anchor)
  {
    super(context,null);
    this.anchor  = anchor;
  }
  
  public GridTableDialog(ClientContext context, String anchor, IGridTableDialogCallback callback)
  {
    super(context,callback);
    this.anchor  = anchor;
  }
  
  /**
   * Shows an HTML information dialog on the client. 
   * 
   * @param title    The title of the dialog
   * @param question The question of the dialog.
   */
  public void show(int width, int height)
  {
    ClientContext context = (ClientContext) Context.getCurrent();
    ClientSession session = (ClientSession) context.getSession();
    
    context.addAdditionalHTML("<script type=\"text/javascript\">\n");
    context.addAdditionalHTML("new jACOBAutocloseWindow('"+anchor+"','"+session.getBaseUrl()+"dialogs/GridTableDialog.jsp?browser="+context.clientBrowser+"&guid="+getId()+"',"+width+","+height+");\n");
    context.addAdditionalHTML("</script>\n");

    ((ClientSession)context.getApplication().getSession()).addDialog(this);
  }
}
