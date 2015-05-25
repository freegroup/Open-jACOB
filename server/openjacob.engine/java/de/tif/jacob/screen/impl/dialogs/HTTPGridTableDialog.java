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

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPGroup;

/**
 *
 */
public abstract class HTTPGridTableDialog  extends HTTPGenericDialog implements IGridTableDialog
{
  static class Connection
  {
   int column;
   ISingleDataGuiElement element;
  }
  String[]                 header;
  String[][]               data;
  Vector                   connections = new Vector();
  final IGridTableDialogCallback callback; // an optional callback object which will be called if the user clicks on a row

  /**
   * 
   */
  public HTTPGridTableDialog(HTTPClientContext context, IGridTableDialogCallback callback)
  {
    super((HTTPDomain)context.getDomain(), (HTTPGroup)context.getGroup());
    this.callback = callback;
  }

  
  
  /**
   * @param context
   * @param guid
   * @param event
   * @param value
   * @throws IOException
   * @throws NoSuchFieldException
   * @author Andreas Herz
   */
  public final void processEvent(IClientContext context, String event, String value) throws Exception
  {
    int row = Integer.parseInt(value);
    // make the backfill of the connected gui elements
    //
    Iterator iter = connections.iterator();
    while(iter.hasNext())
    {
    	Connection con = (Connection)iter.next();
      con.element.setValue(data[row][con.column].toString());
    }

    // call the callback object if the user has hands over one
    //
    if(callback!=null)
    {
      Properties props=new Properties();
      for (int i = 0; i < header.length; i++)
      {
        props.put(header[i], data[row][i]);
      }
      callback.onSelect(context,row,props);
    }
  }
  
  public final void show()
  {
    show(300,200);
  }

  /**
   * Connect a GUI element with a column in the Dialog. The column data
   * will be transfer to the GUI element if the user clicks in a row.
   * 
   * @param column
   * @param element
   * @author Andreas Herz
   */
  public final void connect(int column, ISingleDataGuiElement element)
  {
  	Connection connection = new Connection();
    connection.column  = column;
    connection.element = element;
    
    connections.add(connection);
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
 
}
