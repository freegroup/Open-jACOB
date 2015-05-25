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
 * Created on 22.11.2004
 *
 */
package de.tif.jacob.designer.model;

import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.designer.preferences.I18NPreferences;

/**
 *
 */
public class BrowserFieldCalculatedModel extends BrowserFieldModel
{
	public BrowserFieldCalculatedModel(JacobModel jacob , BrowserModel browserModel,  CastorBrowserField field)
	{
	  super(jacob,browserModel,field);
	}

  @Override
	public void renameFieldReference(FieldModel field, String from, String to)
	{
	  // calcualted field -> nothing to do
	}
	
	
  @Override
  protected void resetI18N()
  {
    setLabel("%"+getBrowserModel().getName().toUpperCase()+ getJacobModel().getSeparator()+getName().toUpperCase());
    getJacobModel().addI18N(getLabel().substring(1),"",false);
  }

  @Override
  public String getError()
  {
    return null;
  }
  
  
  @Override
  public String getInfo()
  {
    return null;
  }
  
  @Override
  public boolean isInUse()
  {
    return true;
  }
  
  @Override
  public String getSortOrder()
  {
    return SORTORDER_NONE;
  }
  
  @Override
  public void setSortOrder(String sortORder)
  {
    // ignore
  }
}
