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
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IMessageDialogCallback;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPGroup;

/**
 *
 */
public abstract class HTTPMessageDialog  extends HTTPGenericDialog implements IMessageDialog
{
  protected final IMessageDialogCallback callback;
  protected String message;
  public abstract void println(String msg);
  
  /**
   * 
   */
  public HTTPMessageDialog(HTTPClientContext context, String msg,IMessageDialogCallback callback)
  {
    super((HTTPDomain)context.getDomain(), (HTTPGroup)context.getGroup());
    this.message = msg;
    this.callback = callback;
  }
  
  public HTTPMessageDialog(String msg,IMessageDialogCallback callback)
  {
    super(null,null);
    this.message = msg;
    this.callback = callback;
  }
  
  public final String getMessage()
  {
    return message;
  }
  
  
  public final void show()
  {
    // size dosn't matters in a alert box
    show(1,1); 
  }
  
  /*
   * The message dialog has nothing to proceed. Consume the event if this dialog has produce the event. 
   */
  
  public final void processEvent(IClientContext context, String event, String value) throws Exception
  {
    callback.onClose(context);
  }
}
