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

package de.tif.jacob.core.definition.impl.admin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.config.IConfig;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IApplicationProvider;
import de.tif.jacob.core.definition.impl.jad.JadFileApplicationProvider;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class AdminApplicationProvider implements IApplicationProvider
{
  static public transient final String RCS_ID = "$Id: AdminApplicationProvider.java,v 1.2 2009/03/02 19:04:49 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  static private final transient Log logger = LogFactory.getLog(AdminApplicationProvider.class);
  
  public static final String ADMIN_JAD_FILE = "admin.jad";
  public static final String ADMIN_APPLICATION_NAME = "admin";
  
  private static IApplicationProvider embeddedProvider;
  private static IApplicationDefinition adminApplicationDefinition;
  private static Exception initialisationException;
  
  
  static
  {
    try
    {
    	embeddedProvider = new JadFileApplicationProvider(getReader());
      adminApplicationDefinition = embeddedProvider.getApplication(ADMIN_APPLICATION_NAME, Version.ADMIN);
    }
    catch (Exception ex)
    {
      logger.fatal("Initialisation failed", ex);
      initialisationException = ex;
    }
  }
  
  public static IApplicationDefinition getApplication()
  {
    if (null != initialisationException)
    {
      throw new RuntimeException("Initialisation failed", initialisationException);
    }
    return adminApplicationDefinition;
  }
  
  public static IDataAccessor newDataAccessor()
  {
    return new DataAccessor(getApplication());
  }
  
	/**
	 * @param config
	 * @throws Exception
	 */
	public AdminApplicationProvider(IConfig config) throws Exception
	{
		if (null != initialisationException)
		{
			throw new Exception("Initialisation failed", initialisationException);
		}
	}

	/**
	 * @param jadReader
	 * @throws Exception
	 */
  private static Reader getReader() throws IOException
	{
    URL url = AdminApplicationProvider.class.getResource(ADMIN_JAD_FILE);
    if (null == url)
    {
      throw new RuntimeException("JAD resource '" + ADMIN_JAD_FILE + "' not found!");
    }
    logger.info("Reading JAD: " + url);
    return new InputStreamReader(url.openStream());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IApplicationProvider#getApplication(java.lang.String, de.tif.jacob.core.Version)
	 */
	public IApplicationDefinition getApplication(String applicationName, Version version) throws RuntimeException
	{
		return embeddedProvider.getApplication(applicationName, version);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IApplicationProvider#getDefaultApplication()
	 */
	public IApplicationDefinition getDefaultApplication()
	{
		return getApplication();
	}

}
