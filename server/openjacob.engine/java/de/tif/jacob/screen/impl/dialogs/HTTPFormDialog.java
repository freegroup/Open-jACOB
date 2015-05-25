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

import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPGroup;

/**
 *
 */
public abstract class HTTPFormDialog  extends HTTPGenericDialog implements IFormDialog
{
  protected final IFormDialogCallback callback;
  private Map            responseValues  = new HashMap();

  /**
   * 
   */
  public HTTPFormDialog(HTTPClientContext context, IFormDialogCallback callback)
  {
    super((HTTPDomain)context.getDomain(), (HTTPGroup)context.getGroup());
   
    this.callback = callback;
  }
  
  public HTTPFormDialog(IFormDialogCallback callback)
  {
    super(null,null);
   
    this.callback = callback;
  }
  
	/* 
	 * @see de.tif.jacob.screen.dialogs.IMultipleDataDialog#setValue(java.lang.String, java.lang.String)
	 */
	public final void setResponseValue(String key, Object value)
	{
		responseValues.put(key, value);
	}

	/**
	 * Depends on the FormDialog the values in the map can be Strings or byte[] if you have a
	 * file upload element in the dialog.<br>
	 *  
	 * @return Returns the response values from the dialog.
	 */
	public final Map getResponseValues()
	{
		return responseValues;
	}


	public final void processEvent(IClientContext context, String event, String value) throws Exception
  {
   
    // do callback object defined. This is possible in a simple MessageDialog
    //
    if(callback==null)
      return;
    
    try
		{
      callback.onSubmit(context,event, responseValues);
		}
		finally 
		{
		  ((HTTPClientSession)((HTTPClientContext)context).getApplication().getSession()).removeDialog(this);
    }
  }
  }
