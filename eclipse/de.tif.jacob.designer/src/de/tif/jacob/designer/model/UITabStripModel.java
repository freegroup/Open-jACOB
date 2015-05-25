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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

public class UITabStripModel extends  ObjectModel
{
  UITabContainerModel parent;
  List<UITabModel> tabs =null;
  public UITabStripModel(UITabContainerModel parent)
  {
    super(parent.getJacobModel());
    this.parent = parent;
  }

  public UITabContainerModel getTabContainerModel()
  {
    return parent;
  }
  
  public List<UITabModel> getElements()
  {
    if(tabs==null)
    {
      tabs = new ArrayList<UITabModel>();
      Iterator iter = parent.getPanes().iterator();
      while(iter.hasNext())
      {
        UIGroupModel group = (UIGroupModel)iter.next();
        tabs.add(new UITabModel(parent, group));
      }
    }
    return tabs;
  }
  
  public void addElement(UITabModel tab, int index)
  {
    if(index!=-1)
      getElements().add(index,tab);
    else
      getElements().add(tab);
  }

  public void addElement(UIGroupModel group, int index)
  {
      addElement(new UITabModel(parent, group), index);
  }
  
  public UITabModel removeElement(UIGroupModel group)
  {
    // you mus call getElements() to init the tabs List!!! 
    Iterator iter = getElements().iterator();
    while(iter.hasNext())
    {
      UITabModel tab = (UITabModel)iter.next();
      if(tab.getUIGroupModel()==group)
      {
        iter.remove();
        return tab;
      }
    }
    return null;
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

  public void createMissingI18NKey()
  {
    for (UITabModel tab : getElements())
    {
      tab.createMissingI18NKey();
    }
  }

  /**
   * 
   * @param from the orignal key WITH the % at first character
   * @param to the new key WITH the % at first character
   */
  protected void renameI18NKey(String fromName, String toName)
  {
    for (UITabModel tab : getElements())
    {
      tab.renameI18NKey(fromName, toName);
    }
  }

  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  public boolean isI18NKeyInUse(String key)
  {
    for (UITabModel tab : getElements())
    {
      if(tab.isI18NKeyInUse(key))
        return true;
    }
    return false;
  }

  @Override
  public String getName()
  {
    return "<unused>";
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
