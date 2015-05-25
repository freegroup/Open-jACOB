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
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.IFormDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomain;
import de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractDomainDefinition extends AbstractElement implements IDomainDefinition
{
  static public transient final String RCS_ID = "$Id: AbstractDomainDefinition.java,v 1.6 2008/06/27 19:39:31 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.6 $";
  
  private final String title;
  private final List roles;
  private final List forms;
  private final List formGroups;
  private final List unmodifiableRoles;
  private final List unmodifiableForms;
  private final List unmodifiableFormGroups;
  private final boolean visible;
  private final String eventHandler;
  private boolean canCollapse;
  
	/**
	 * 
	 */
	public AbstractDomainDefinition(String name, String title, String description,boolean canCollapse, boolean visible, String eventHandler)
	{
    super(name, description);
    this.title = title;
    this.visible = visible;
    this.canCollapse = canCollapse;
    this.forms = new ArrayList();
    this.formGroups = new ArrayList();
    this.roles = new ArrayList();
    this.unmodifiableForms = Collections.unmodifiableList(this.forms);
    this.unmodifiableFormGroups = Collections.unmodifiableList(this.formGroups);
    this.unmodifiableRoles = Collections.unmodifiableList(this.roles);
    this.eventHandler = eventHandler;
  }
  
  protected void addForm(IFormDefinition form)
  {
    this.forms.add(form);
  }
  
  protected void addFormGroup(AbstractFormGroupDefinition formGroup)
  {
    this.formGroups.add(formGroup);
  }

  protected void addRoleName(String role)
  {
    this.roles.add(role);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IDomainDefinition#getTitle()
   */
  public final String getTitle()
  {
    return title;
  }

  public boolean canCollapse()
  {
    return canCollapse;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IDomainDefinition#isVisible()
   */
  public final boolean isVisible()
  {
    return this.visible;
  }
  
  /**
   * Returns the class name of the event handler of this domain.
   * 
   * @return event handler class name or <code>null</code> if default event
   *         handler should be used.
   */
  public final String getEventHandler()
  {
    return this.eventHandler;
  }

  public DataScope getDataScope()
  {
    // by default a domain does not overwrite data scope
    return null;
  }

  protected final void toJacob(CastorDomain jacobDomain)
  {
    jacobDomain.setName(getName());
    jacobDomain.setTitle(getTitle());
    jacobDomain.setDescription(getDescription());
    jacobDomain.setVisible(isVisible());
    jacobDomain.setCanCollapse(canCollapse());
    jacobDomain.setEventHandler(getEventHandler());
    jacobDomain.setDataScope(getDataScope() == null ? null : getDataScope().toDomainJad());
    
    List forms = this.getFormDefinitions();
    for (int i=0; i < forms.size(); i++)
    {
      IFormDefinition form = (IFormDefinition) forms.get(i);
      jacobDomain.addForm(form.getName());
    }
    
    List formgroups = this.getFormGroupDefinitions();
    for (int i = 0; i < formgroups.size(); i++)
    {
      CastorFormGroup jacobFormGroup = new CastorFormGroup();
      ((AbstractFormGroupDefinition) formgroups.get(i)).toJacob(jacobFormGroup);
      jacobDomain.addFormGroup(jacobFormGroup);
    }

    Iterator iter = getRoleNames().iterator();
    while (iter.hasNext())
    {
      jacobDomain.addRole((String) iter.next());
    }
    
    // handle properties
    jacobDomain.setProperty(getCastorProperties());
  }
  
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IDomainDefinition#getFormDefinitions()
	 */
	public final List getFormDefinitions()
	{
		return this.unmodifiableForms;
	}

  public final List getFormGroupDefinitions()
  {
    return this.unmodifiableFormGroups;
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IDomainDefinition#getRoleNames()
	 */
	public final List getRoleNames()
	{
		return this.unmodifiableRoles;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    // nothing more to do
	}

}
