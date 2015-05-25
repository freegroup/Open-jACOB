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

package de.tif.qes.adl.element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLModule
{
  static public final transient String RCS_ID = "$Id: ADLModule.java,v 1.1 2006-12-21 11:32:20 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private final String name;
  private final String label;
  private final boolean visible;
  private final String desc;
  private final List roles;
//  private final List browserNames;
//  private final List formNames;
  private final List formorderNames;

	/**
	 * 
	 */
	public ADLModule(String name, String label, Boolean invisible, String desc, List roles, List browserNames, List formNames, List formorderNames)
	{
    this.name = name;
    this.label = label;
    this.visible = !invisible.booleanValue();
    this.desc =desc;
    this.roles = roles == null ? new ArrayList() : roles;
//    this.browserNames = browserNames == null ? new ArrayList() : browserNames;
//    this.formNames = formNames == null ? new ArrayList() : formNames;
    this.formorderNames = formorderNames == null ? new ArrayList() : formorderNames;
  }

	/**
	 * @return Returns the desc.
	 */
	public String getDescription()
	{
		return desc;
	}

	/**
	 * @return Returns the formorderNames.
	 */
	public List getFormNames()
	{
		return formorderNames;
	}

	/**
	 * @return Returns the invisible.
	 */
	public boolean isVisible()
	{
		return visible;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return Returns the roles.
	 */
	public List getRoleNames()
	{
		return roles;
	}
  
  public boolean isFocus()
  {
    return isFocusName(this.name);
  }

  public static boolean isFocusName(String moduleName)
  {
    return moduleName.startsWith("f_");
  }

	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("\nADLModule[");
		buffer.append("name = ").append(name);
		buffer.append(", label = ").append(label);
		buffer.append(", visible = ").append(visible);
		buffer.append(", desc = ").append(desc);
		buffer.append(", roles = ").append(roles);
		buffer.append(", formNames = ").append(formorderNames);
		buffer.append("]");
		return buffer.toString();
	}
}
