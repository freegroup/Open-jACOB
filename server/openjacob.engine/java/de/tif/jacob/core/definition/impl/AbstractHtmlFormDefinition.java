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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tif.jacob.core.definition.IHtmlFormDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm;

/**
 * @author Andreas Herz
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractHtmlFormDefinition extends AbstractGuiElement implements IHtmlFormDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractHtmlFormDefinition.java,v 1.4 2009/12/14 16:32:46 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";
  
  private final String label;
  private final List groups;
  private final List unmodifiableGroupList;
  private final boolean visible;

  /**
	 * @param name
	 */
	public AbstractHtmlFormDefinition(String name, String label, String description, String eventHandler, boolean visible)
	{
		super(name, description, eventHandler);
    this.label = label;
    this.groups = new ArrayList();
    this.visible = visible;
    this.unmodifiableGroupList = Collections.unmodifiableList(this.groups);
  }


  public boolean isVisible()
  {
    return this.visible;
  }

  public boolean hideSearchBrowserTabStrip()
  {
    // irrelevant for HtmlFormDefinition
    return false;
  }

  protected void addGroup(AbstractHtmlGroupDefinition group)
  {
    this.groups.add(group);
  }
  
  /* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IFormDefinition#getLabel()
	 */
	public final String getLabel()
	{
		return label;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IFormDefinition#getGroupDefinitions()
   */
  public final List getGroupDefinitions()
  {
    return this.unmodifiableGroupList;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
   */
  public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
  }

  public void toJacob(CastorMutableForm jacobForm, ConvertToJacobOptions options)
  {
    jacobForm.setName(getName());
    jacobForm.setLabel(getLabel());
    jacobForm.setDescription(getDescription());
    jacobForm.setEventHandler(getEventHandler());
    
    
    // handle properties
    jacobForm.setProperty(getCastorProperties());
  }
}
