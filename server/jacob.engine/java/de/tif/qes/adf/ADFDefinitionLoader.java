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

package de.tif.qes.adf;

import java.io.Reader;

import de.tif.qes.QesLayoutAdjustment;
import de.tif.qes.adf.castor.Forms;
import de.tif.qes.adl.element.ADLDefinition;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADFDefinitionLoader
{
	public ADFDefinition load(Reader reader, ADLDefinition definition, QesLayoutAdjustment layout) throws Exception
	{
		try
		{
			Forms castorForms = (Forms) Forms.unmarshalForms(reader);
			return new ADFDefinition(castorForms, definition, layout);
		}
		finally
		{
			reader.close();
		}
	}

}
