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

import java.util.List;

import de.tif.jacob.core.definition.impl.AbstractBrowserDefinition;
import de.tif.jacob.core.definition.impl.AbstractBrowserField;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class ADLBrowser extends AbstractBrowserDefinition
{
  static public final transient String RCS_ID = "$Id: ADLBrowser.java,v 1.1 2006-12-21 11:32:19 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
//	private final Map fieldNameToIndexMap;
  
	public ADLBrowser(String name, String tableAliasName, List fields)
	{
		super(name, null, tableAliasName);
//		this.fieldNameToIndexMap = new HashMap();
		for (int i = 0; i < fields.size(); i++)
		{
      AbstractBrowserField browserField = (AbstractBrowserField) fields.get(i);
      
      // IBIS: Temporär unsichtbare Browserfelder verwerfen. Klären, ob diese wirklich benötigt werden.
      if (!browserField.isVisible())
        continue;
      
      // do adjustment, since during initialisation of ADLBrowserAnchorField, we
      // do not know whether the browser field is a foreign browser field
      if (browserField instanceof ADLBrowserAnchorField)
        browserField = ((ADLBrowserAnchorField) browserField).adjustIfNecessary(tableAliasName);
      
      addField(browserField);
//			this.fieldNameToIndexMap.put(browserField.getName(), new Integer(i));
		}
	}
  
//	public int getFieldIndexByFieldName(String fieldName) throws NoSuchFieldException
//	{
//		Integer index = (Integer) this.fieldNameToIndexMap.get(fieldName);
//		if (null == index)
//			throw new NoSuchFieldException(fieldName + " is no field of browser " + this.name);
//		return index.intValue();
//	}

}
