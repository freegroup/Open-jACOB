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

package de.tif.jacob.screen.impl.dialogs;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPGroup;

/**
 *
 */
public abstract class HTTPAskDialog extends HTTPGenericDialog implements IAskDialog
{
  final IAskDialogCallback callback;
  protected final boolean            multiline;
  
  public HTTPAskDialog(HTTPClientContext context,IAskDialogCallback callback, boolean multiline)
  {
    super((HTTPDomain)context.getDomain(), (HTTPGroup)context.getGroup());
    this.callback  = callback;
    this.multiline = multiline;
  }


  public final void show()
  {
    // self calculation size
    show(1,1); 
  }
 
  public final void processEvent(IClientContext context, String event, String value) throws Exception
  {
    if(event.equals("ok"))
      callback.onOk(context,value);
    else if(event.equals("cancel"))
      callback.onCancel(context);
  }

}
