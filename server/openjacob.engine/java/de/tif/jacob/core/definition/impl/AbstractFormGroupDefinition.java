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

import de.tif.jacob.core.definition.IFormDefinition;
import de.tif.jacob.core.definition.IFormGroupDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractFormGroupDefinition extends AbstractElement implements IFormGroupDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractFormGroupDefinition.java,v 1.3 2007/06/19 06:55:10 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";
  
  private final String title;
  private final List forms;
  private final List unmodifiableForms;
  
	/**
	 * 
	 */
	public AbstractFormGroupDefinition(String name, String title, String description, String eventHandler)
	{
    super(name, description);
    this.title = title;
    this.forms = new ArrayList();
    this.unmodifiableForms = Collections.unmodifiableList(this.forms);
  }
  
  protected void addForm(IFormDefinition form)
  {
    this.forms.add(form);
  }
  
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IDomainDefinition#getTitle()
   */
  public final String getTitle()
  {
    return title;
  }


  protected final void toJacob(CastorFormGroup jacobFormGroup)
  {
    jacobFormGroup.setName(getName());
    jacobFormGroup.setTitle(getTitle());
    jacobFormGroup.setDescription(getDescription());
    jacobFormGroup.setEventHandler(null);
    
    List forms = this.getFormDefinitions();
    for (int i=0; i < forms.size(); i++)
    {
      IFormDefinition form = (IFormDefinition) forms.get(i);
      jacobFormGroup.addForm(form.getName());
    }
  }
  
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IDomainDefinition#getFormDefinitions()
	 */
	public final List getFormDefinitions()
	{
		return this.unmodifiableForms;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    // nothing more to do
	}

}
