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

package de.tif.jacob.core.definition.impl;

import de.tif.jacob.core.Version;


/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ConvertToJacobOptions
{
	static public transient final String RCS_ID = "$Id: ConvertToJacobOptions.java,v 1.2 2009/03/18 11:34:57 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.2 $";

//	private boolean suppressCustomFieldTypes;
	private boolean suppressSystemTables;
	private String encoding = "ISO-8859-1";
	private boolean fetchTableRules;
	private boolean fetchScripts;
	private boolean lookupByReference;
	private Version version;
	private String defaultApplication;

	public ConvertToJacobOptions()
	{
//		this.suppressCustomFieldTypes = false;
		this.suppressSystemTables = false;
		this.lookupByReference = false;
	}

	public final boolean getOptions(String[] args)
	{
		if (args.length < 2)
			return false;

		for (int i = 2; i < args.length; i++)
		{
			if ("-nocustomfieldtypes".equalsIgnoreCase(args[i]))
			{
//				this.suppressCustomFieldTypes = true;
			}
			else if ("-lookupbyreference".equalsIgnoreCase(args[i]))
			{
				this.lookupByReference = true;
			}
			else if ("-nosystemtables".equalsIgnoreCase(args[i]))
			{
				this.suppressSystemTables = true;
			}
			else if ("-encoding".equalsIgnoreCase(args[i]))
			{
				i++;
				if (i == args.length)
					return false;
				this.encoding = args[i];
			}
			else if ("-version".equalsIgnoreCase(args[i]))
			{
				i++;
				if (i == args.length)
					return false;
				this.version = Version.parseVersion(args[i]);
			}
			else if ("-default".equalsIgnoreCase(args[i]))
			{
				i++;
				if (i == args.length)
					return false;
				this.defaultApplication = args[i];
			}
			else if ("-tablerules".equalsIgnoreCase(args[i]))
			{
				this.fetchTableRules = true;
			}
			else if ("-scripts".equalsIgnoreCase(args[i]))
			{
				this.fetchScripts = true;
			}
			else
			{
				System.out.println("Unknown option: " + args[i]);
				return false;
			}
		}
		
		if (this.version == null)
			throw new RuntimeException("-version is a required option");
		
		return true;
	}

	public final void printOptions()
	{
		System.out.println("Version: " + getVersion());

		if (null != this.defaultApplication)
			System.out.println("Default: " + this.defaultApplication);

		System.out.println("Encoding: " + getEncoding());

		System.out.println("Table rules: " + this.fetchTableRules);

		System.out.println("Scripts: " + this.fetchScripts);
	}

	/**
	 * @return Returns the suppressCustomFieldTypes.
	 */
	public final boolean isSuppressCustomFieldTypes()
	{
	  // IBIS : remove option?
	  return false;
//		return suppressCustomFieldTypes;
	}

	/**
	 * @return Returns the encoding.
	 */
	public final String getEncoding()
	{
		return encoding;
	}

	/**
	 * @return Returns the suppressSystemTables.
	 */
	public final boolean isSuppressSystemTables()
	{
		return suppressSystemTables;
	}

  /**
   * @return Returns the lookupByReference.
   */
  public boolean isLookupByReference()
  {
    return lookupByReference;
  }
  
	/**
	 * @return Returns the fetchScripts.
	 */
	public boolean isFetchScripts()
	{
		return fetchScripts;
	}

	/**
	 * @return Returns the fetchTableRules.
	 */
	public boolean isFetchTableRules()
	{
		return fetchTableRules;
	}

	/**
	 * @return Returns the version.
	 */
	public Version getVersion()
	{
		return version;
	}

	/**
	 * @return Returns the defaultApplication.
	 */
	public String getDefaultApplication()
	{
		return defaultApplication;
	}

}
