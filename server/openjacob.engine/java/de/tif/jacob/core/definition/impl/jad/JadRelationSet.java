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

package de.tif.jacob.core.definition.impl.jad;

import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.AbstractRelationSet;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadRelationSet extends AbstractRelationSet
{
  static public transient final String RCS_ID = "$Id: JadRelationSet.java,v 1.1 2007/01/19 09:50:38 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  private CastorRelationset tempRelationSet;

	/**
	 * @param name
	 */
	protected JadRelationSet(CastorRelationset relationSet)
	{
		super(relationSet.getName());
		this.tempRelationSet = relationSet;
    
    // handle properties
    if (relationSet.getPropertyCount() > 0)
      putCastorProperties(relationSet.getProperty());
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition,
	 *      de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
		for (int i = 0; i < this.tempRelationSet.getRelationCount(); i++)
		{
			String relationname = this.tempRelationSet.getRelation(i);
			addRelation(definition.getAbstractRelation(relationname));
		}

		// let garbage collector do its job
		this.tempRelationSet = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.IRelationSet#isLocal()
	 */
	public boolean isLocal()
	{
		return false;
	}

}
