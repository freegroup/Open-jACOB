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
package de.tif.jacob.designer.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

public class UITabPanesModel extends ObjectModel implements UIGroupContainer
{
  UITabContainerModel parent;
  
  public UITabPanesModel(UITabContainerModel parent)
  {
    super(parent.getJacobModel());
    this.parent = parent;
  }

  public List<UIGroupModel> getElements()
  {
    return parent.getPanes();
  }

  public UIGroupContainer getGroupContainerModel()
  {
    return parent.getGroupContainerModel();
  }
  
  public void addElement(UIGroupModel group)
  {
    parent.addPane(group);
  }
  
  public void removeElement(UIGroupModel group)
  {
    parent.removePane(group);
  }

  
  public boolean isUIElementNameFree(String name)
  {
    return true;
  }


  public List getLinkedDomainModels()
  {
    return Collections.EMPTY_LIST;
  }


  @Override
  public String getName()
  {
    return parent.getName();
  }


  @Override
  public String getError()
  {
    Iterator<UIGroupModel> iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupModel obj = iter.next();
      String error = obj.getError();
      if (error != null)
        return error;
    }
    return null;
  }

  @Override
  public String getWarning()
  {
    Iterator<UIGroupModel> iter = getElements().iterator();
    while (iter.hasNext())
    {
      UIGroupModel obj = iter.next();
      String warning = obj.getWarning();
      if (warning != null)
        return warning;
    }
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
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }

  @Override
  public ObjectModel getParent()
  {
    return parent;
  }
}
