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

import de.tif.jacob.core.definition.impl.AbstractDomainDefinition;
import de.tif.qes.adf.ADFDefinition;
import de.tif.qes.adl.element.ADLModule;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class QeSFocusDefinition extends AbstractDomainDefinition
{
	static public final transient String RCS_ID = "$Id: QeSFocusDefinition.java,v 1.3 2009-08-17 23:37:01 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	public QeSFocusDefinition(ADLModule module, ADFDefinition adfDefinition)
	{
	  // use default event handler class name
		super(module.getName(), module.getLabel(), null, true, module.isVisible(), null);
		
		for (int i = 0; i < module.getFormNames().size(); i++)
		{
			addForm(adfDefinition.getForm((String) module.getFormNames().get(i)));
		}
		for (int i = 0; i < module.getRoleNames().size(); i++)
		{
			addRoleName((String) module.getRoleNames().get(i));
		}
	}

}
