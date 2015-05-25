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
package de.tif.jacob.core.data.event;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.event.EventHandler;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class DataEventHandler extends EventHandler
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: DataEventHandler.java,v 1.3 2010/01/08 13:50:37 ibissw Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.3 $";

	protected static final DataEventHandler getEventHandler(IApplicationDefinition app, IDataRecordSet parent, String eventType) throws Exception
	{
		StringBuffer eventClassName = new StringBuffer(200);

		eventClassName.append("jacob.event.data.");
		if(parent!=null)
		{
		  eventClassName.append(StringUtils.capitalize(parent.getName()));
		}
		eventClassName.append(eventType);
		return (DataEventHandler) ClassProvider.getInstance(app, eventClassName.toString());
	}

}
