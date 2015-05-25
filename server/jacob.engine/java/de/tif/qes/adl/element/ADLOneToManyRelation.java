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

import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.AbstractOneToManyRelation;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLOneToManyRelation extends AbstractOneToManyRelation
{
  static public final transient String RCS_ID = "$Id: ADLOneToManyRelation.java,v 1.1 2006-12-21 11:32:20 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private final String fromKeyName;
  private final String toKeyName;
  private final String desc;
  
	public ADLOneToManyRelation(String name, String desc, String fromTableAliasName, String fromKeyName, String toTableAliasName, String toKeyName, String sameas)
	{
	  // IBIS: How to handle sameas?
		super(name, fromTableAliasName, toTableAliasName);

		this.desc = desc;
		this.fromKeyName = fromKeyName;
		this.toKeyName = toKeyName;
	}

  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    super.postProcessing(definition, parent);
    setFromKey(getFromTableAlias().getTableDefinition().getKey(this.fromKeyName));
    setToKey(getToTableAlias().getTableDefinition().getKey(this.toKeyName));
  }
  
	/**
	 * @return Returns the fromField.
	 */
	public String getFromKeyName()
	{
		return fromKeyName;
	}

	/**
	 * @return Returns the toField.
	 */
	public String getToKeyName()
	{
		return toKeyName;
	}

}
