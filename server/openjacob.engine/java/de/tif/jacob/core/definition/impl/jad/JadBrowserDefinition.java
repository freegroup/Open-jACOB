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

import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.KeyType;
import de.tif.jacob.core.definition.impl.AbstractBrowserDefinition;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JadBrowserDefinition extends AbstractBrowserDefinition
{
	static public transient final String RCS_ID = "$Id: JadBrowserDefinition.java,v 1.5 2009/08/17 23:47:01 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.5 $";

	private final BrowserType type;
	private final JadKey      connectByKey;

  /**
	 *  
	 */
	public JadBrowserDefinition(CastorBrowser browser)
	{
		super(browser.getName(), browser.getDescription(), browser.getAlias());

    if(browser.getConnectByKey()!=null)
      connectByKey = new JadKey(browser.getConnectByKey(),KeyType.FOREIGN);
    else
      connectByKey = null;
    
    // handle properties
    if (browser.getPropertyCount() > 0)
      putCastorProperties(browser.getProperty());
    
    this.type = browser.getType();
		for (int i = 0; i < browser.getFieldCount(); i++)
		{
			CastorBrowserField field = browser.getField(i);
			if (field.getCastorBrowserFieldChoice().getTableField() != null)
			{
				if (field.getCastorBrowserFieldChoice().getTableField().getForeign() != null)
					this.addField(new JadBrowserForeignTableField(field));
				else
					this.addField(new JadBrowserTableField(field));
			}
			else if (field.getCastorBrowserFieldChoice().getRuntime() != null)
			{
				this.addField(new JadBrowserRuntimeField(field));
			}
			else
			{
				throw new RuntimeException("Unknown browser field type");
			}
		}
	}

  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
    super.postProcessing(definition, parent);
    
    JadTableDefinition table = (JadTableDefinition) this.getTableAlias().getTableDefinition();
    if(connectByKey!=null)
      connectByKey.postProcessing(definition,table);
  }

  public IKey getConnectByKey()
  {
    return connectByKey;
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.impl.AbstractBrowser#getType()
	 */
	protected BrowserType getType()
	{
		return this.type;
	}

}
