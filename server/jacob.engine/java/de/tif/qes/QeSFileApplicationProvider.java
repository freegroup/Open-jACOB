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

package de.tif.qes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.config.AbstractConfig;
import de.tif.jacob.core.config.IConfig;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.AbstractApplicationProvider;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;
import de.tif.jacob.core.exception.MissingPropertyException;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazzloader.ClassDirectory;
import de.tif.jacob.util.clazzloader.ExtendedClassLoader;
import de.tif.qes.adf.ADFDefinition;
import de.tif.qes.adf.ADFDefinitionLoader;
import de.tif.qes.adl.ADLDefinitionLoader;
import de.tif.qes.adl.element.ADLDefinition;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QeSFileApplicationProvider extends AbstractApplicationProvider
{
	static public final transient String RCS_ID = "$Id: QeSFileApplicationProvider.java,v 1.1 2006-12-21 11:31:23 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	static private final transient Log logger = LogFactory.getLog(QeSFileApplicationProvider.class);

	private static final String PROPERTY_ADL_FILE = "qes.file.adl";
	private static final String PROPERTY_ADF_FILE = "qes.file.adf";
	private static final String PROPERTY_DATASOURCE_MAPPING = "qes.datasource.mapping.";
	private static final String PROPERTY_DEFAULT_DATASOURCE = "qes.default.datasource";

	private final ADLDefinition adlDefinition;
	private final ADFDefinition adfDefinition;

	public QeSFileApplicationProvider(IConfig config) throws Exception
  {
    if (logger.isTraceEnabled())
      logger.trace("Loading QES application definitions ..");

    // extract mapping entries
    Map datasourceNameMap = new HashMap();
    Iterator iter = config.getKeysStartingWith(PROPERTY_DATASOURCE_MAPPING);
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      String qesDatasourceName = key.substring(PROPERTY_DATASOURCE_MAPPING.length());
      String mappedDatasourceName = config.getProperty(key);
      if ("".equals(qesDatasourceName) || "".equals(mappedDatasourceName))
      {
        throw new MissingPropertyException("Invalid mapping", key);
      }
      logger.info("Mapping datasource '" + qesDatasourceName + "' -> '" + mappedDatasourceName + "'");
      datasourceNameMap.put(qesDatasourceName, mappedDatasourceName);
    }

    // accessing default datasource
    String qesDefaultDatasourceName = config.getProperty(PROPERTY_DEFAULT_DATASOURCE);
    if (null == qesDefaultDatasourceName || "".equals(qesDefaultDatasourceName))
    {
      throw new MissingPropertyException(PROPERTY_DEFAULT_DATASOURCE);
    }

    // load adl
    ADLDefinitionLoader adlLoader = new ADLDefinitionLoader();
    this.adlDefinition = adlLoader.load(getADLReader(config));
    adlDefinition.postProcessing(qesDefaultDatasourceName, datasourceNameMap);

    // load adf
    ADFDefinitionLoader adfLoader = new ADFDefinitionLoader();
    this.adfDefinition = adfLoader.load(getADFReader(config), this.adlDefinition, new QesLayoutAdjustment(config, this.adlDefinition.getAdlVersion()));
    adlDefinition.postProcessing(this.adfDefinition);

    if (logger.isTraceEnabled())
      logger.trace("QES application definitions loaded.");
  }

	private static Reader getADLReader(IConfig config) throws IOException
	{
		String adlFileName = config.getProperty(PROPERTY_ADL_FILE);
		if (null == adlFileName)
		{
			throw new MissingPropertyException("No ADL file name defined", PROPERTY_ADL_FILE);
		}
		URL url = config.getResource(adlFileName);
		if (null == url)
		{
			throw new RuntimeException("ADL resource '" + adlFileName + "' not found!");
		}
    if (logger.isInfoEnabled())
    	logger.info("Reading ADL (ISO-8859-1): " + url);
		return new InputStreamReader(url.openStream(),"ISO-8859-1");
	}

	private static Reader getADFReader(IConfig config) throws IOException
	{
		String adfFileName = config.getProperty(PROPERTY_ADF_FILE);
		if (null == adfFileName)
		{
			throw new MissingPropertyException("No ADF file name defined", PROPERTY_ADF_FILE);
		}
		URL url = config.getResource(adfFileName);
		if (null == url)
		{
			throw new RuntimeException("ADF resource '" + adfFileName + "' not found!");
		}
    if (logger.isInfoEnabled())
      logger.info("Reading ADF (ISO-8859-1): " + url);
		Reader reader = new InputStreamReader(url.openStream(),"ISO-8859-1");
		try
		{
			StringBuffer file = new StringBuffer(1000000);
			int read = 0;
			char[] buffer = new char[4096];
			while (0 < (read = reader.read(buffer)))
			{
				file.append(buffer, 0, read);
			}

			// castor cannot map a 'Class' attribute of an xml object. Rename it to
			// 'Clazz'
			//
			return new StringReader(StringUtil.replace(file.toString(), "Class=\"", "Clazz=\""));
		}
		finally
		{
			reader.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.IApplicationProvider#getApplication(java.lang.String,
	 *      int)
	 */
	public IApplicationDefinition getApplication(String applicationName, Version version)
	{
		if (logger.isDebugEnabled())
			logger.debug("getApplication(): applicationName=" + applicationName + ", version=" + version);

		return new QeSApplicationDefinition(version, adlDefinition, applicationName);
	}

	private Jacob toJacob(ConvertToJacobOptions options)
	{
		Jacob jacob = new Jacob();
		jacob.setVersion(options.getVersion().toString());
    jacob.setEngineVersion(Version.ENGINE.toString());
		
		// handle default application if defined
		String defaultApplication = options.getDefaultApplication();
		if (null != defaultApplication)
		{
			// check if valid application name (Note: Throws an exception if not valid)
			this.adlDefinition.getApplicationInfo(defaultApplication);
			jacob.setDefaultApplication(defaultApplication);
		}
		
		this.adlDefinition.toJacob(jacob, options);
		this.adfDefinition.toJacob(jacob, options);
		return jacob;
	}

	public static void main(String[] args)
	{
		try
		{
			ConvertToJacobOptions options = new ConvertToJacobOptions();

			if (options.getOptions(args))
			{
				String configFileName = args[0];
				System.out.println("Reading config file: " + configFileName);
				String outputFileName = args[1];
				System.out.println("Writing to: " + outputFileName);
        options.printOptions();
				
				IConfig config = new Config(configFileName);
				QeSFileApplicationProvider provider = new QeSFileApplicationProvider(config);
				
				FileOutputStream fileOutputStream = new FileOutputStream(outputFileName);
        printFormated(provider.toJacob(options), fileOutputStream, options.getEncoding());
        fileOutputStream.close();
		
				System.out.println("Converting successfully finished!");
				System.exit(0);
			}
			else
			{
				System.err.println("Usage: QeSFileApplicationProvider.main configfile outputfile [options]");
				System.exit(1);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.err.println("Converting failed: " + ex.toString());
			System.exit(1);
		}
	}
  
	private static class Config extends AbstractConfig
	{
		final ResourceBundle resourceBundle;
		final File directory;

		/**
		 *  
		 */
		Config(String configFileName) throws Exception
		{
			File configFile = new File(configFileName);
			this.directory = configFile.getParentFile();
			String simpleFileName = configFile.getName().substring(0, configFile.getName().indexOf(".properties"));
			ExtendedClassLoader cl = new ExtendedClassLoader(null);
			cl.appendClassSource(new ClassDirectory(directory));
			this.resourceBundle = ResourceBundle.getBundle(simpleFileName, Locale.getDefault(), cl);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.config.AbstractConfig#getResourceBundle()
		 */
		protected ResourceBundle getResourceBundle()
		{
			return resourceBundle;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.config.AbstractConfig#getResource(java.lang.String)
		 */
		public URL getResource(String name)
		{
			try
			{
				File resourceFile = new File(this.directory, name);
				return resourceFile.toURL();
			}
			catch (Exception e)
			{
				logger.error("Getting resource " + name + " failed", e);
				return null;
			}
		}

	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IApplicationProvider#getDefaultApplication()
	 */
	public IApplicationDefinition getDefaultApplication()
	{
		// no default application definied
		return null;
	}

}
