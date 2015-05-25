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

package de.tif.jacob.core.definition.impl.jad;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.config.IConfig;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.AbstractApplicationProvider;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;
import de.tif.jacob.core.exception.MissingPropertyException;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadFileApplicationProvider extends AbstractApplicationProvider
{
  static public transient final String RCS_ID = "$Id: JadFileApplicationProvider.java,v 1.2 2008/10/01 10:23:05 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  static private final transient Log logger = LogFactory.getLog(JadFileApplicationProvider.class);
  
  private static final String PROPERTY_JAD_FILE = "jad.file";
  
  private final Jacob jacob;
  private final JadDefinition definition;
  
	/**
	 * 
	 */
  public JadFileApplicationProvider(IConfig config) throws Exception
  {
    this(getJADReader(config));
  }
  
  public JadFileApplicationProvider(Reader jadReader) throws Exception
	{
		if (logger.isTraceEnabled())
			logger.trace("Loading JAD application definitions ..");

		// load jad
		this.jacob = (Jacob) Jacob.unmarshalJacob(jadReader);
		this.definition = new JadDefinition(jacob);

		if (logger.isTraceEnabled())
			logger.trace("JAD application definitions loaded.");
	}
  
  private static Reader getJADReader(IConfig config) throws IOException
  {
    String jadFileName = config.getProperty(PROPERTY_JAD_FILE);
    if (null == jadFileName)
    {
      throw new MissingPropertyException("No JAD file name defined", PROPERTY_JAD_FILE);
    }
    URL url = config.getResource(jadFileName);
    if (null == url)
    {
      throw new RuntimeException("JAD resource '" + jadFileName + "' not found!");
    }
    logger.info("Reading JAD: " + url);
    return new InputStreamReader(url.openStream(),"ISO-8859-1");
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IApplicationProvider#getApplication(java.lang.String, de.tif.jacob.core.misc.Version)
	 */
	public IApplicationDefinition getApplication(String applicationName, Version version) throws RuntimeException
	{
    if (logger.isDebugEnabled())
      logger.debug("getApplication(): applicationName=" + applicationName + ", version=" + version);

    return new JadApplication(version, this.definition, applicationName);
  }

  public static void main(String[] args)
  {
    try
    {
      ConvertToJacobOptions options = new ConvertToJacobOptions();

      if (options.getOptions(args))
      {
        String inputFileName = args[0];
        System.out.println("Reading from: " + inputFileName);
        String outputFileName = args[1];
        System.out.println("Writing to: " + outputFileName);
        options.printOptions();
        
        Jacob jacob = (Jacob) Jacob.unmarshalJacob(new FileReader(inputFileName));
        JadDefinition definition = new JadDefinition(jacob);
        
        Jacob jacob2 = new Jacob();
        definition.toJacob(jacob2, new ConvertToJacobOptions());
				FileOutputStream fileOutputStream = new FileOutputStream(outputFileName);
        printFormated(jacob2, fileOutputStream, options.getEncoding());
        fileOutputStream.close();
        
        System.out.println("Converting successfully finished!");
        System.exit(0);
      }
      else
      {
        System.err.println("Usage: JadFileApplicationProvider.main inputfile outputfile [encoding]");
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
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IApplicationProvider#getDefaultApplication()
	 */
	public IApplicationDefinition getDefaultApplication()
	{
		if (this.definition.getDefaultApplication() == null)
			return null;
		
		return new JadApplication(this.definition.getVersion(), this.definition, this.definition.getDefaultApplication());
	}

  public Jacob getJacob()
  {
    return this.jacob;
  }
}
