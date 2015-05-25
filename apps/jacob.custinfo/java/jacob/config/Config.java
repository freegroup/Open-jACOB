/*
 * Created on 23.04.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jacob.config;

import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.tif.jacob.core.config.AbstractConfig;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Config extends AbstractConfig
{
  private static final String BUNDLE_NAME = "jacob.config.config";
//  private static final String BUNDLE_NAME = Config.class.getPackage().getName()+".config";
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  /**
   *  
   */
  public Config()
  {
    // nothing more to do
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.config.AbstractConfig#getResourceBundle()
	 */
	protected ResourceBundle getResourceBundle()
	{
		return RESOURCE_BUNDLE;
	}

}