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
import java.util.Collections;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.Container;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

public class UITabContainerModel extends UIGroupElementModel
{
  List<UIGroupModel> groups =null;
  
  UITabPanesModel pane;
  UITabStripModel strip;
  
  public UITabContainerModel()
  {
    super(null, null,null, new CastorGuiElement());
    CastorGuiElementChoice choice = new CastorGuiElementChoice();
    Container container = new Container();
    choice.setContainer(container);
    getCastor().setCastorGuiElementChoice(choice);
    getCastor().setVisible(true);
    container.setDimension(new CastorDimension());
    setSize(new Dimension(50, 50));
    setLocation(new Point(100, 100));
    
    pane  = new UITabPanesModel(this);
    strip = new UITabStripModel(this);
  }
  
  public UITabContainerModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group,guiElement);

    pane  = new UITabPanesModel(this);
    strip = new UITabStripModel(this);
    
    // call them initial to avoid Thread conflict in Reference Search
    //
    getPanes();
  }

  public void setJacobModel(JacobModel jacobModel)
  {
    super.setJacobModel(jacobModel);
    pane.setJacobModel(jacobModel);
    strip.setJacobModel(jacobModel);

    // call them initial to avoid Thread conflict in Reference Search
    //
    getPanes();
  }

  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorContainer().getDimension();
  }
  
  public void setTabPosition(UITabModel tab, int newIndex)
  {
    getPanes(); // to init the groups List!!!!
    
    int oldIndex = groups.indexOf(tab.getUIGroupModel());
    
    // Falls der alte Index kleiner als der neue ist, dann muss der neue
    // Index um Eins vermidert werden, da bei dem Entfernen des elementes
    // sich die Indexposition verschiebt.
    //
    if(oldIndex<newIndex)
      newIndex--;
    
    if(newIndex==oldIndex)
      return;
    
    
    strip.removeElement(tab.getUIGroupModel());
    groups.remove(oldIndex);
    CastorGroup castorGroup  =getCastorContainer().removePane(oldIndex);
    
    strip.addElement(tab, newIndex);
    groups.add(newIndex, tab.getUIGroupModel());
    getCastorContainer().addPane(newIndex,castorGroup);

    firePropertyChange(PROPERTY_ELEMENT_CHANGED,  null, this);
    strip.firePropertyChange(PROPERTY_ELEMENT_CHANGED,  null, this);
  }
  
  public List getLinkedDomainModels()
  {
    return Collections.EMPTY_LIST;
  }

  public List<ObjectModel> getElements()
  {
    List<ObjectModel> result = new ArrayList<ObjectModel>();
    result.add(strip);
    result.add(pane);
    
    return result;
  }
  
  public List<UIGroupModel> getPanes()
  {
    if(groups==null)
    {
      groups = new ArrayList<UIGroupModel>();
      for(int i=0;i<getCastorContainer().getPaneCount();i++)
      {
        CastorGroup group=getCastorContainer().getPane(i);
        // Die Gruppe muss sich dem TabContainer anpassen
        //
        UIGroupModel groupModel=new UIGroupModel(getJacobModel(),pane, group);
        groupModel.getCastorDimension().setHeight(0);
        groupModel.getCastorDimension().setWidth(0);
        groupModel.getCastorDimension().setX(0);
        groupModel.getCastorDimension().setY(0);
        groups.add(groupModel);
      }
    }
    return groups;
  }
  
 
  public void addPane(UIGroupModel group, int index)
  {
  	// Die Gruppe muss sich der Dimension des TabContainers anpassen
  	//
    group.setSize(new Dimension(0,0));
    group.setLocation(new Point(0,0));
    
    group.setGroupContainerModel(pane);
    if(index!=-1)
    {
      getCastorContainer().addPane(index, group.getCastor());
      getPanes().add(index,group);
    }
    else
    {
      getCastorContainer().addPane( group.getCastor());
      getPanes().add( group);
    }
    strip.addElement(group, index);
    
    pane.firePropertyChange(PROPERTY_ELEMENT_ADDED, null, group);
    strip.firePropertyChange(PROPERTY_ELEMENT_ADDED, null, group);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, null, getSize());
  }

  public void addPane(UIGroupModel group)
  {
    addPane(group, -1);
  }

  public void removePane(UIGroupModel group)
  {
    for(int i=0;i<getCastorContainer().getPaneCount();i++)
    {
      if(getCastorContainer().getPane(i)==group.getCastor())
      {
        getCastorContainer().removePane(i);
        getPanes().remove(group);
        strip.removeElement(group);
        pane.firePropertyChange(PROPERTY_ELEMENT_REMOVED, group, null);
        strip.firePropertyChange(PROPERTY_ELEMENT_REMOVED, group, null);
        // Dem Browser und alle die diesen anzeigen mitteilen, das sich der (Error/Warning) Status 
        // möglicherweise geändert hat. Der Browser feuert ein Event und alle Listener aktualisieren sich dann
        // Es gibt eventuell keine Gruppe mehr die diesen Browser benötigt)
        group.getBrowserModel().firePropertyChange(PROPERTY_BROWSER_CHANGED, null,group.getBrowserModel());
        firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, null, getSize());
        break;
      }
    }
  }

  
  public void setSize(Dimension size)
  {
    Dimension save = getSize();
    getCastorContainer().getDimension().setHeight(size.height);
    getCastorContainer().getDimension().setWidth(size.width);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }

  public Point getLocation()
  {
    return new Point(getCastorContainer().getDimension().getX(), getCastorContainer().getDimension().getY());
  }
  
  public Dimension getSize()
  {
    return new Dimension(getCastorContainer().getDimension().getWidth(), getCastorContainer().getDimension().getHeight());
  }

  
  private Container getCastorContainer()
  {
    return getCastor().getCastorGuiElementChoice().getContainer();
  }
  
  public String getDefaultNameSuffix()
  {
    return "Container";
  }
  

  @Override
  public String getTemplateFileName()
  {
    return "ITabContainerEventHandler.java";
  }

  @Override
  public int getDefaultWidth()
  {
    return DEFAULT_ELEMENT_WIDTH*2;
  }
  
  @Override
  public int getDefaultHeight()
  {
    // eine Tabbox geht per default über den Platz von 3 eingabeelemente
    //
    return DEFAULT_ELEMENT_HEIGHT*3+DEFAULT_ELEMENT_SPACING*2;
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

  public UITabPanesModel getPane()
  {
    return pane;
  }
  
  @Override
  public void renameAliasReference(String from, String to)
  {
    for (UIGroupModel group : getPanes())
    {
      group.renameAliasReference(from, to);
    }
  }
  
  @Override
  public void renameBrowserReference(String from, String to)
  {
    for (UIGroupModel group : getPanes())
    {
      group.renameBrowserReference(from, to);
    }
  }
  
  @Override
  public void renameEventHandler(String fromClass, String toClass)
  {
    for (UIGroupModel group : getPanes())
    {
      group.renameEventHandler(fromClass, toClass);
    }
  }
  
  @Override
  public void renameFieldReference(FieldModel field, String fromName, String toName)
  {
    for (UIGroupModel group : getPanes())
    {
      group.renameFieldReference(field, fromName, toName);
    }
  }

  /**
   * 
   * @param from the orignal key WITH the % at first character
   * @param to the new key WITH the % at first character
   */
  public void renameI18NKey(String fromName, String toName)
  {
    for (UIGroupModel group : getPanes())
    {
      group.renameI18NKey(fromName, toName);
    }
    strip.renameI18NKey(fromName, toName);
  }

  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    for (UIGroupModel group : getPanes())
    {
      if(group.isI18NKeyInUse(key))
        return true;
    }
    return strip.isI18NKeyInUse(key);
  }
    
  @Override
  public void createMissingI18NKey()
  {
    for (UIGroupModel group : getPanes())
    {
      group.createMissingI18NKey();
    }
    strip.createMissingI18NKey();
  }
  
  @Override
  public void renameRelationReference(String from, String to)
  {
    for (UIGroupModel group : getPanes())
    {
      group.renameRelationReference(from, to);
    }
  }
  
  @Override
  public void renameRelationsetReference(String from, String to)
  {
    // rename the relationset of foreign fields
    //
    for (UIGroupModel group : getPanes())
    {
      group.renameRelationsetReference(from, to);
    }
  }

  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    for (UIGroupModel group : getPanes())
    {
      group.addReferrerObject(result, model);
    }
  }
}
