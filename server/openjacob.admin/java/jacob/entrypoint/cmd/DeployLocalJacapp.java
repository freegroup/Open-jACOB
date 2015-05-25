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

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;

/**
 * This is a entry point for the 'admin' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * 
 * This type of entry point is usefull if your client are unable to implement/call
 * a SOAP request.
 * 
 * You can return any type of dokument (XML, plain text, gif images,.....)
 * 
 * You can access this entry point with an WebBrowser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=DeployLocalJacapp&app=admin&user=USERNAME&pwd=PASSWORD&param1=abc
 *  
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and password of the application.
 *       2. Replace localhost:8080 with the real servername and port.
 *       3. You can add any additional parameters to the url. The jACOB application servers will provide them
 *          for you via the properties.getProperty("...") method.
 */
public class DeployLocalJacapp implements ICmdEntryPoint
{
	static Log logger = AppLogger.getLogger();

	/*
	 * The main method for the entry point
	 * 
	 */
	public void enter(CmdEntryPointContext context, Properties properties) throws IOException
	{
    StringBuffer sb =new StringBuffer(1024);

    try
    {
	    // it is possible to hands over some optional parameters for advance configuration.
	    //
	    String filePath = properties.getProperty("jacappPath");
	    if(filePath==null)
	    {
	  	  sb.append("No .jacapp file in entrypoint parameters found\n");
	  	  return;
	    }
	
  
	    if(!new File(filePath).exists())
	    {
	  	  sb.append("jacapp file ["+filePath+"] not found\n");
	  	  return;
	    }
	    
	    synchronized (DeployManager.MUTEX)
	    {
	  	  sb.append("\tDeploy to database...");
	      DeployManager.deploy(filePath);
	  	  sb.append("\tdone\n");
	    }
	    sb.append("Deploy successfull\n");
    }
    catch(Exception e)
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
	 * The Web client need this information for the propper display of the returned content.
	 */
	public String getMimeType(CmdEntryPointContext context, Properties properties)
	{
		return "text/plain";
	}
}
