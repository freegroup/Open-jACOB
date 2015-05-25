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

package de.tif.jacob.core.definition.impl;

import java.util.Collections;
import java.util.List;

import de.tif.jacob.core.definition.IExternalFormDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorExternalFormTargetType;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractExternalFormDefinition extends AbstractGuiElement implements IExternalFormDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractExternalFormDefinition.java,v 1.4 2009/12/14 16:32:46 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";
  
  private final String label;
  private final String url;
  private final String target;
  private final boolean visible;
  
	/**
	 * @param name
	 */
	public AbstractExternalFormDefinition(String name, String label, String description, String url, String target, boolean visible)
	{
		super(name, description, null);
		this.visible = visible;
    this.label = label;
    this.url = url;
    this.target= target;
  }


	public boolean hideSearchBrowserTabStrip()
  {
    // irrelevant for ExternalFormDefinition
    return false;
  }


  /* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IFormDefinition#getLabel()
	 */
	public final String getLabel()
	{
		return label;
	}



  public String getURL()
  {
    return url;
  }
  

  public String getTarget()
  {
    return target;
  }
  

  public List getGroupDefinitions()
  {
  	// external forms do not have groups
    return Collections.EMPTY_LIST;
  }
  
  
  public boolean isVisible()
  {
    return this.visible;
  }


  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
   */
  public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
  }

  
  public void toJacob(CastorExternalForm externalForm, ConvertToJacobOptions options)
  {
    externalForm.setName(getName());
    externalForm.setLabel(getLabel());
    externalForm.setDescription(getDescription());
    externalForm.setUrl(this.url);
    externalForm.setTarget(CastorExternalFormTargetType.valueOf(this.target));
    
    // handle properties
    externalForm.setProperty(getCastorProperties());
  }
}
