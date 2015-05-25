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

package de.tif.jacob.entrypoint.cmd;

import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Properties;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.xml.Serializer;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;

/**
 *
 */
public class GenericSearch implements ICmdEntryPoint
{
  /* 
   * @see de.tif.jacob.entrypoint.ICmdEntryPoint#getMimeType()
   */
  public String getMimeType(CmdEntryPointContext context, Properties properties)
  {
    return "text/xml";
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.entrypoint.ICmdEntryPoint#enter(de.tif.jacob.entrypoint.CmdEntryPointContext, java.util.Properties)
   */
  public void enter(CmdEntryPointContext context, Properties properties) throws Exception
  {
    // it is possible to hands over some optional parameters for advance configuration.
    //
    String relationset   = properties.getProperty("relationset",IRelationSet.LOCAL_NAME);
    String filldirection = properties.getProperty("filldirection", Filldirection.BACKWARD.toString()); 
    String encoding      = properties.getProperty("encoding","ISO-8859-1");
    
    OutputStreamWriter writer =new OutputStreamWriter(context.getStream(), encoding);
    writer.write("<?xml version=\"1.0\" encoding=\"");
    writer.write(encoding);
    writer.write("\"?>\n");
    writer.write("<data>\n");

    IDataTable anchorTableAlias = context.getDataTable(properties.getProperty("anchorTableAlias"));
    Enumeration e = properties.keys();
    while(e.hasMoreElements())
    {
      String key =(String) e.nextElement();
      String [] parts=key.split("[.]");
      if(parts.length==2)
        context.getDataTable(parts[0]).qbeSetValue(parts[1],properties.getProperty(key));
    }

    for(int i=anchorTableAlias.search()-1;i>=0;i--)
    {
      IDataTableRecord record = anchorTableAlias.getRecord(i);
      // Serialize the current record (and all related tables) to an XML document.
      //
      Serializer.appendXml(writer,record,context.getApplicationDefinition().getRelationSet(relationset),Filldirection.parseFilldirection(filldirection));
    }
    writer.write("</data>");
    writer.flush();
  }
}
