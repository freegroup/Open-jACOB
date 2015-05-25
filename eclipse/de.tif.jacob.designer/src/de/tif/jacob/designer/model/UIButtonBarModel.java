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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

public abstract class UIButtonBarModel extends UIGroupElementModel
{
  private List<IButtonBarElementModel> children =null;
  
  
  public UIButtonBarModel()
  {
    super(null, null,null, new CastorGuiElement());
    CastorGuiElementChoice choice = new CastorGuiElementChoice();
    FlowLayoutContainer bar = new FlowLayoutContainer();
    choice.setFlowLayoutContainer(bar);
    getCastor().setCastorGuiElementChoice(choice);
    getCastor().setVisible(true);
    bar.setDimension(new CastorDimension());
    setSize(new Dimension(50, 50));
    setLocation(new Point(100, 100));
    
  }
  
  public UIButtonBarModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group,guiElement);

    // call them initial to avoid Thread conflict in Reference Search
    //
    getElements();
  }

  public void setJacobModel(JacobModel jacobModel)
  {
    super.setJacobModel(jacobModel);

    // call them initial to avoid Thread conflict in Reference Search
    //
    getElements();
  }

  public CastorDimension getCastorDimension()
  {
    return getCastorContainer().getDimension();
  }
  
  public void setButtonPosition(IButtonBarElementModel button, int newIndex)
  {
    getElements(); // to init the groups List!!!!
    
    int oldIndex = children.indexOf(button);
    
    // Falls der alte Index kleiner als der neue ist, dann muss der neue
    // Index um Eins vermindert werden, da bei dem Entfernen des elementes
    // sich die Indexposition verschiebt.
    //
    if(oldIndex<newIndex)
      newIndex--;
    
    if(newIndex==oldIndex)
      return;
    
    
    children.remove(oldIndex);
    CastorGuiElement castorButton =getCastorContainer().removeGuiElement(oldIndex);
    
    children.add(newIndex, button);
    getCastorContainer().addGuiElement(newIndex,castorButton);

    firePropertyChange(PROPERTY_ELEMENT_CHANGED,  null, this);
  }
  

  public List<IButtonBarElementModel> getElements()
  {
    if(children==null)
    {
      children = new ArrayList<IButtonBarElementModel>();
      for(int i=0;i<getCastorContainer().getGuiElementCount();i++)
      {
        CastorGuiElement element=getCastorContainer().getGuiElement(i);

        if(element.getCastorGuiElementChoice().getButton()!=null)
        {
          UIButtonModel buttonModel=new UIButtonModel(getJacobModel(),getGroupContainerModel(),getGroupModel(), element);
          buttonModel.setSize(new Dimension(0,0));
          buttonModel.setLocation(new Point(0,0));
          buttonModel.setButtonBarModel(this);
          children.add(buttonModel);
        }
        else if(element.getCastorGuiElementChoice().getPluginComponent()!=null)
        {
          UIPluginComponentModel model=new UIPluginComponentModel(getJacobModel(),getGroupContainerModel(),getGroupModel(), element);
          model.setSize(new Dimension(0,0));
          model.setLocation(new Point(0,0));
          model.setButtonBarModel(this);
          children.add(model);
        }
      }
    }
    return children;
  }
  
 
  public void addElement(IButtonBarElementModel button, int index)
  {
  	// Die Gruppe muss sich der Dimension der ButtonBar anpassen
  	//
    button.setSize(new Dimension(0,0));
    button.setLocation(new Point(0,0));
    
    button.setGroup(this.getGroupModel());
    button.setButtonBarModel(this);
    if(index!=-1)
    {
      getCastorContainer().addGuiElement(index, button.getCastor());
      getElements().add(index,button);
    }
    else
    {
      getCastorContainer().addGuiElement( button.getCastor());
      getElements().add( button);
    }
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, button);
  }

  public void addElement(IButtonBarElementModel group)
  {
    addElement(group, -1);
  }

  public void removeElement(IButtonBarElementModel button)
  {
    for(int i=0;i<getCastorContainer().getGuiElementCount();i++)
    {
      if(getCastorContainer().getGuiElement(i)==button.getCastor())
      {
        getCastorContainer().removeGuiElement(i);
        getElements().remove(button);
        button.setButtonBarModel(null);
        firePropertyChange(PROPERTY_ELEMENT_REMOVED, button, null);
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

  
  protected FlowLayoutContainer getCastorContainer()
  {
    return getCastor().getCastorGuiElementChoice().getFlowLayoutContainer();
  }
  
  public String getDefaultNameSuffix()
  {
    return "ButtonBar";
  }

  public int getDefaultWidth()
  {
    return DEFAULT_ELEMENT_WIDTH*2;
  }
  
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

  public void renameAliasReference(String from, String to)
  {
  }
  
  public void renameBrowserReference(String from, String to)
  {
  }
  
  public void renameEventHandler(String fromClass, String toClass)
  {
    for (IButtonBarElementModel button : getElements())
    {
      button.renameEventHandler(fromClass, toClass);
    }
  }
  
  public void renameFieldReference(FieldModel field, String fromName, String toName)
  {
    for (IButtonBarElementModel button : getElements())
    {
      button.renameFieldReference(field, fromName, toName);
    }
  }

  /**
   * 
   * @param from the orignal key WITH the % at first character
   * @param to the new key WITH the % at first character
   */
  public void renameI18NKey(String fromName, String toName)
  {
    for (IButtonBarElementModel button : getElements())
    {
      button.renameI18NKey(fromName, toName);
    }
  }

  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    for (IButtonBarElementModel button : getElements())
    {
      if(button.isI18NKeyInUse(key))
        return true;
    }
    return false;
  }
    
  public void createMissingI18NKey()
  {
    for (IButtonBarElementModel button : getElements())
    {
      button.createMissingI18NKey();
    }
  }
  
  public void renameRelationReference(String from, String to)
  {
    for (IButtonBarElementModel button : getElements())
    {
      button.renameRelationReference(from, to);
    }
  }
  
  public void renameRelationsetReference(String from, String to)
  {
    // rename the relationset of foreign fields
    //
    for (IButtonBarElementModel button : getElements())
    {
      button.renameRelationsetReference(from, to);
    }
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    for (IButtonBarElementModel button : getElements())
    {
      button.addReferrerObject(result, model);
    }
  }

  public UIGroupElementModel getElement(String name)
  {
    if (name == null)
      throw new NullPointerException("you must hands over a valid element name. <null> is not alowed.");
    Iterator iter = getElements().iterator();
    while (iter.hasNext())
    {
      Object obj = iter.next();
      UIGroupElementModel ui = (UIGroupElementModel) obj;
      if (name.equals(ui.getName()))
        return ui;
    }
    return null;
  }

}
