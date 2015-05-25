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
	protected ResourceBundle getResourceBundle()
	{
		return RESOURCE_BUNDLE;
	}
}
