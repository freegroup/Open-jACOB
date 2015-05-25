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

package de.tif.jacob.core.definition.impl.aliascondition;

import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.definition.IDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAlias.ITableAliasConditionAdjuster;
import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AliasCondition extends AliasElement
{
	static public transient final String RCS_ID = "$Id: AliasCondition.java,v 1.2 2007/10/16 00:24:14 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.2 $";

	static private final transient Log logger = LogFactory.getLog(AliasCondition.class);

	private AliasConditionResult staticCondition = null;
	
	/**
	 *  
	 */
	public AliasCondition()
	{
		// nothing more to do
	}

	/**
	 * @param definition
	 * @param alias
	 */
	public final void postProcessing(IDefinition definition, ITableAlias alias)
	{
		childrenPostProcessing(definition, alias);
		
		if (!this.isDynamic())
		{
			this.staticCondition = new AliasConditionResult(null);
			print(this.staticCondition);
		}
	}
	
	public final AliasConditionResult getResult(ITableAliasConditionAdjuster adjuster)
	{
		if (this.staticCondition != null && adjuster == null)
			return this.staticCondition;
		
		AliasConditionResult result = new AliasConditionResult(adjuster);
		print(result);
		return result.isEmpty() ? null : result;
	} 
	
	public static AliasCondition parse(String str) throws Exception
  {
    if (null == str)
      return null;

    if (logger.isDebugEnabled())
    {
      logger.debug("Parsing alias condition: " + str);
    }

    AliasConditionScanner scanner = new AliasConditionScanner(new StringReader(str));
    AliasConditionParser parser = new AliasConditionParser(scanner);
    return (AliasCondition) parser.parse().value;
  }

	public static AliasCondition parseWithPostProcessing(String str, IDefinition definition, ITableAlias alias) throws InvalidExpressionException
	{
		try
		{
			AliasCondition aliasCondition = parse(str);
			if (aliasCondition != null)
			{
				aliasCondition.postProcessing(definition, alias);
			}
			return aliasCondition;
		}
		catch (Exception ex)
		{
			throw new InvalidExpressionException(str, ex);
		}
	}

}
