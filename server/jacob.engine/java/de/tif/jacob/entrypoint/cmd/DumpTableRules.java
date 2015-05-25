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
/*
 * Created on 12.07.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package de.tif.jacob.entrypoint.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.transformer.ITransformer;
import de.tif.jacob.transformer.TransformerFactory;
import de.tif.qes.IQeScriptContainer;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class DumpTableRules implements ICmdEntryPoint
{
	static public final transient String RCS_ID = "$Id: DumpTableRules.java,v 1.1 2006-12-21 11:25:22 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.entrypoint.ICmdEntryPoint#getMimeType(de.tif.jacob.entrypoint.CmdEntryPointContext,
	 *      java.util.Properties)
	 */
	public String getMimeType(CmdEntryPointContext context, Properties properties)
	{
		return "application/excel";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.entrypoint.ICmdEntryPoint#enter(de.tif.jacob.entrypoint.CmdEntryPointContext,
	 *      java.util.Properties)
	 */
	public void enter(CmdEntryPointContext context, Properties properties) throws Exception
	{
		String[] header = new String[] { "Application", "Table Alias", "is implemented", "Handler", "QScript file", "QScript name", "QScript type", "all QScriptinfo" };
		Collection data = new ArrayList();

		IApplicationDefinition def = context.getApplicationDefinition();
		String app = def.getTitle();
		Iterator iter = def.getTableAliases().iterator();
		while (iter.hasNext())
		{
			ITableAlias alias = (ITableAlias) iter.next();
			String handlerClassName = "jacob.event.data." + StringUtils.capitalise(alias.getName()) + "TableRecord";
			Object handler = ClassProvider.getInstance(def, handlerClassName);

			String qscripts = alias.getProperty(IQeScriptContainer.SCRIPTS_PROPERTY);
			String qfile = alias.getProperty(IQeScriptContainer.SCRIPT_FILE_PROPERTY);
			String qname = alias.getProperty(IQeScriptContainer.SCRIPT_NAME_PROPERTY);
			String qtype = alias.getProperty(IQeScriptContainer.SCRIPT_TYPE_PROPERTY);
			data.add(new String[] { app, alias.getName(), "" + (handler != null), handlerClassName, qfile, qname, qtype, qscripts });
		}
		String[][] d = (String[][]) data.toArray(new String[data.size()][]);
		ITransformer trans = TransformerFactory.get(getMimeType(context, properties));
		trans.transform(context.getStream(), header, d);
	}

}
