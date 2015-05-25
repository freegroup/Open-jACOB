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
import de.tif.jacob.screen.dialogs.IExcelDialog;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPGroup;

/**
 *
 */
public abstract class HTTPExcelDialog  extends HTTPGenericDialog implements IExcelDialog
{
  private String[]      header;
  private String[][]    data;
 
  public HTTPExcelDialog(HTTPClientContext context)
  {
    super((HTTPDomain)context.getDomain(), (HTTPGroup)context.getGroup());
  }
  
  public final void show()
  {
   show(500,300); 
  }
  
  public final void setHeader(String[] header)
  {
    this.header = header;
  }
  
  public final void setData(String[][] data)
  {
    this.data = data;
  }
  
  public final String[] getHeader()
  {
   return header; 
  }
  
  public final String[][] getData()
  {
   return data; 
  }
  
  /* 
   * @see de.tif.jacob.screen.dialogs.ICallbackDialog#processEvent(de.tif.jacob.screen.IClientContext, int, java.lang.String, java.lang.String)
   */
  public final void processEvent(IClientContext context, String event, String value) throws Exception
  {
  }

}
