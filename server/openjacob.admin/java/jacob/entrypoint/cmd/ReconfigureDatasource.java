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
package jacob.entrypoint.cmd;



import jacob.common.AppLogger;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.reconfigure.CommandList;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;

/**
 * This is a entry point for the 'admin' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * 
 * This type of entry point is useful, if your client is unable to implement/call
 * a SOAP request.
 * 
 * You can return any type of document (XML, plain text, gif images,.....)
 * 
 * You can access this entry point with an WebBrowser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=ReconfigureDatasource&app=admin&user=USERNAME&pwd=PASSWORD&param1=abc
 *  
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and password of the application.
 *       2. Replace localhost:8080 with the real servername and port.
 *       3. You can add any additional parameters to the url. The jACOB application servers will provide them
 *          for you via the properties.getProperty("...") method.
 */
public class ReconfigureDatasource implements ICmdEntryPoint
{
	static public transient final String RCS_ID = "$Id: ReconfigureDatasource.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
	static public transient final String RCS_REV = "$Revision: 1.1 $";

	static final Log logger = AppLogger.getLogger();

	/*
	 * The main method for the entry point
	 * 
	 */
	public void enter(CmdEntryPointContext context, Properties properties) throws IOException
	{
		StringBuffer sb = new StringBuffer(1024);
		
    String applicationVersion = properties.getProperty("applicationVersion");
    String applicationName    = properties.getProperty("applicationName");
    String dataSource         = properties.getProperty("dataSource");
    
		try
		{
		  if(applicationName==null || applicationVersion==null || dataSource==null)
		  {
		    sb.append("Required parameter missing:\n")
		    .append("\tapplicationVersion:")
		    .append(applicationVersion)
		    .append("\n\tapplicationName:")
		    .append(applicationName)
		    .append("\n\tdataSource:")
		    .append(dataSource)
		    .append("\n\nNO reconfigure done!");
		    return;
		  }
		  SQLDataSource sqlDataSource = (SQLDataSource)DataSource.get(dataSource);

		  ISchemaDefinition desiredSchemaDefinition = DeployMain.getApplication(applicationName,applicationVersion).getSchemaDefinition(sqlDataSource.getName());
      
      Reconfigure reconfigure = sqlDataSource.getReconfigureImpl();
      
      ISchemaDefinition currentSchemaDefinition = reconfigure.fetchSchemaInformation();
      
  		// setup datasource first
      //
  	  sqlDataSource.setup(currentSchemaDefinition);
  		
  	  // execute reconfigure check then
  	  //
      CommandList commands = reconfigure.reconfigure(desiredSchemaDefinition, currentSchemaDefinition, true);
      
      if(commands.size()==0)
      {
        sb.append("Database already up to date. Reconfigure passed.");
        return;
      }
      if(commands.execute(sqlDataSource,false))
      {
        sb.append(commands.getExecutionSummary());
      }
      else
      {
        sb.append(commands.getLastErrorStatement());        
      }
      sb.append("reconfigure database done.");
    }
		catch (Exception e)
		{
		  sb.append(e.getClass().getName()+":"+e.getMessage());
		}
		finally
		{
      context.getStream().write(sb.toString().getBytes());
		}
	}

	/**
	 * Returns the mime type for this entry point.
	 * 
	 * The Web client need this information for the proper display of the returned content.
	 */
	public String getMimeType(CmdEntryPointContext context, Properties properties)
	{
		return "text/plain";
	}
}
