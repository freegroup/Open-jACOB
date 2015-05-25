package jacob.config;

import java.util.ResourceBundle;

import de.tif.jacob.core.config.AbstractConfig;

/**
 * The Config.java is a generated file! 
 *
 * !!! DO NOT EDIT NOR DELETE THIS FILE !!!
 * 
 * This is the anchor class to easily locate configuration properties
 * for the jACOB application server.
 */
public class Config extends AbstractConfig
{
	private static final String BUNDLE_NAME = "jacob.config.config";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public Config()
	{
		// nothing more to do
	}

	/*
	 * @see de.tif.jacob.core.config.AbstractConfig#getResourceBundle()
	 */
	@Override
	protected ResourceBundle getResourceBundle()
	{
		return RESOURCE_BUNDLE;
	}
}
