/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
 * Created on 15.06.2005
 *
 */
package de.tif.jacob.designer.model;

import de.tif.jacob.core.definition.impl.jad.castor.CastorRole;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UserRoleModel extends ObjectModel
{
  private final CastorRole castor;

  public UserRoleModel( JacobModel jacobModel ,CastorRole userRole)
  {
    super(jacobModel);
    this.castor = userRole;
  }
  
  public UserRoleModel( JacobModel jacobModel, String defaultName)
  {
    super(jacobModel);
    this.castor = new CastorRole();
    this.castor.setName(defaultName);
    this.castor.setDescription("");
  }

  public String getError()
  {
    return null;
  }

  public String getWarning()
  {
    return null;
  }

  public String getInfo()
  {
    return null;
  }

  public boolean isInUse()
  {
    return true;
  }
  
  protected CastorRole getCastor()
  {
    return castor;
  }

  public String getDescription()
  {
    return castor.getDescription();
  }
  
  public String getName()
  {
    return castor.getName();
  }
  
  public void setDescription(String desc)
  {
    String save = getDescription();
    if(StringUtil.saveEquals(save,desc))
      return;

    castor.setDescription(desc);
		firePropertyChange(PROPERTY_DESCRIPTION_CHANGED, save, desc);
  }
  
  public void setName(String name)
  {
    String save = getName();
    if(StringUtil.saveEquals(save,name))
      return;

    getJacobModel().renameUserRole(save,name);
    
    castor.setName(name);
		firePropertyChange(PROPERTY_USERROLE_CHANGED, save, name);
		getJacobModel().firePropertyChange(PROPERTY_USERROLE_CHANGED, save,name);
  }

  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
  
  @Override
  public ObjectModel getParent()
  {
    return getJacobModel().getApplicationModel();
  }
}
