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
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.Container;
import de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType;
import de.tif.jacob.designer.editor.Constants;
import de.tif.jacob.designer.editor.util.ColorUtil;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

public class UIStackContainerModel extends UIGroupElementModel implements UIGroupContainer
{
  List<UIGroupModel> groups =null;
  
  public UIStackContainerModel()
  {
    super(null, null,null, new CastorGuiElement());
    CastorGuiElementChoice choice = new CastorGuiElementChoice();
    Container container = new Container();
    choice.setContainer(container);
    getCastor().setCastorGuiElementChoice(choice);
    getCastor().setVisible(true);
    container.setLayout(ContainerLayoutType.STACK);
    container.setDimension(new CastorDimension());
    setSize(new Dimension(50, 50));
    setLocation(new Point(100, 100));
  }
  
  public UIStackContainerModel(JacobModel jacob, UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
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

  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorContainer().getDimension();
  }
  

  public List getLinkedDomainModels()
  {
    return Collections.EMPTY_LIST;
  }
  
  public List<UIGroupModel> getElements()
  {
    if(groups==null)
    {
      groups = new ArrayList<UIGroupModel>();
      for(int i=0;i<getCastorContainer().getPaneCount();i++)
      {
        CastorGroup group=getCastorContainer().getPane(i);
        // Die Gruppe muss sich dem TabContainer anpassen
        //
        UIGroupModel groupModel=new UIGroupModel(getJacobModel(),this, group);
        groupModel.setSize(new Dimension(0,0));
        groupModel.setLocation(new Point(0,0));
        groupModel.setBorder(false);
        groups.add(groupModel);
      }
    }
    return groups;
  }
  
 
  public void addElement(UIGroupModel group, int index)
  {
  	// Die Gruppe muss sich der Dimension des TabContainers anpassen
  	//
    group.setSize(new Dimension(0,0));
    group.setLocation(new Point(0,0));
    group.setBorder(false);
    group.setGroupContainerModel(this);
    if(index!=-1)
    {
      getCastorContainer().addPane(index, group.getCastor());
      getElements().add(index,group);
    }
    else
    {
      getCastorContainer().addPane( group.getCastor());
      getElements().add( group);
    }
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, null, getSize());
  }

  public void addElement(UIGroupModel group)
  {
    addElement(group, -1);
  }

  public void removeElement(UIGroupModel group)
  {
    for(int i=0;i<getCastorContainer().getPaneCount();i++)
    {
      if(getCastorContainer().getPane(i)==group.getCastor())
      {
        getCastorContainer().removePane(i);
        getElements().remove(group);
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

  public int getBorderWidth()
  {
    return getCastor().getBorderWidth();
  }
  

  public void setBorderWith(int width)
  {
    width = Math.max(-1,width);
    
    int save = getBorderWidth();
    if(save == width)
      return;
    
    getCastor().setBorderWidth(width);
    firePropertyChange(PROPERTY_BORDER_CHANGED, save, width);
  }

  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public Color getBorderColor()
  {
    if(getCastor().getBorderColor()==null)
      return ColorUtil.toColor(Constants.DEFAULT_COLOR);
    return ColorUtil.toColor(getCastor().getBorderColor());
  }
  
  public void setBorderColor(Color color)
  {
    Color save = getBorderColor();
    String saveCSS = getCastor().getBorderColor();
    String css  = ColorUtil.toCSS(color);
    
    if(StringUtil.saveEquals(saveCSS,css))
      return;
    
    
    if(Constants.DEFAULT_COLOR.equals(css))
      getCastor().setBorderColor(null);
    else
      getCastor().setBorderColor(css);
    
    firePropertyChange(PROPERTY_COLOR_CHANGED, save, color);
  }
  
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getFontSize()
   */
  public Color getBackgroundColor()
  {
    if(getCastor().getBackgroundColor()==null)
      return ColorUtil.toColor(Constants.DEFAULT_COLOR);
    return ColorUtil.toColor(getCastor().getBackgroundColor());
  }
  
  public void setBackgroundColor(Color color)
  {
    Color save = getBackgroundColor();
    String saveCSS = getCastor().getBackgroundColor();
    String css  = ColorUtil.toCSS(color);
    
    if(StringUtil.saveEquals(saveCSS,css))
      return;
    
    
    if(Constants.DEFAULT_COLOR.equals(css))
      getCastor().setBackgroundColor(null);
    else
      getCastor().setBackgroundColor(css);
    
    firePropertyChange(PROPERTY_COLOR_CHANGED, save, color);
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
    return "IStackContainerEventHandler.java";
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
  public void renameAliasReference(String from, String to)
  {
    for (UIGroupModel group : getElements())
    {
      group.renameAliasReference(from, to);
    }
  }
  
  @Override
  public void renameBrowserReference(String from, String to)
  {
    for (UIGroupModel group : getElements())
    {
      group.renameBrowserReference(from, to);
    }
  }
  
  @Override
  public void renameEventHandler(String fromClass, String toClass)
  {
    for (UIGroupModel group : getElements())
    {
      group.renameEventHandler(fromClass, toClass);
    }
  }
  
  @Override
  public void renameFieldReference(FieldModel field, String fromName, String toName)
  {
    for (UIGroupModel group : getElements())
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
    for (UIGroupModel group : getElements())
    {
      group.renameI18NKey(fromName, toName);
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
    for (UIGroupModel group : getElements())
    {
      if(group.isI18NKeyInUse(key))
        return true;
    }
    return false;
  }
    
  @Override
  public void createMissingI18NKey()
  {
    for (UIGroupModel group : getElements())
    {
      group.createMissingI18NKey();
    }
  }
  
  @Override
  public void renameRelationReference(String from, String to)
  {
    for (UIGroupModel group : getElements())
    {
      group.renameRelationReference(from, to);
    }
  }
  
  @Override
  public void renameRelationsetReference(String from, String to)
  {
    // rename the relationset of foreign fields
    //
    for (UIGroupModel group : getElements())
    {
      group.renameRelationsetReference(from, to);
    }
  }

  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    for (UIGroupModel group : getElements())
    {
      group.addReferrerObject(result, model);
    }
  }

  public boolean isUIElementNameFree(String name)
  {
    for (UIGroupModel group : getElements())
    {
      if(!group.isUIElementNameFree(name))
        return false;
    }
    return !this.getName().equals(name);
  }
  
  /**
   * The UI element is part of the 2.8.3 engine.
   */
  @Override
  public Version getRequiredJacobVersion()
  {
    return new Version(2,8,3);
  }
}
