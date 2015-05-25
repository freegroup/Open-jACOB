/*
 * Created on 22.11.2008
 *
 */
package de.tif.jacob.designer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

public class UIJacobGroupContainer extends UIFormElementModel implements UIGroupContainer
{
  private CastorGroupContainer castor;
  ArrayList<UIGroupModel> groups = null;
  
  public UIJacobGroupContainer()
  {
    super(null, null);
    castor = new CastorGroupContainer();
    castor.setDimension(new CastorDimension());
    setSize(new Dimension(50, 50));
    setLocation(new Point(100, 100));
  }

  public UIJacobGroupContainer(JacobModel jacob, UIGroupContainer container, CastorGroupContainer castorContainer)
  {
    super(jacob, null);
    this.castor = castorContainer;
  }
  

  @Override
  public CastorDimension getCastorDimension()
  {
    return castor.getDimension();
  }


  @Override
  public String getCastorName()
  {
    return castor.getName();
  }



  @Override
  public Point getLocation()
  {
    return new Point(castor.getDimension().getX(), castor.getDimension().getY());
  }

  @Override
  public Dimension getSize()
  {
    return new Dimension(getCastorContainer().getDimension().getWidth(), getCastorContainer().getDimension().getHeight());
  }


  private CastorGroupContainer getCastorContainer()
  {
    return castor;
  }

  @Override
  public void setCastorName(String name)
  {
    castor.setName(name);
  }

  @Override
  public void setSize(Dimension size)
  {
    Dimension save = getSize();
    getCastorContainer().getDimension().setHeight(size.height);
    getCastorContainer().getDimension().setWidth(size.width);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void removeCastorProperty(CastorProperty property)
  {
    for (int i = 0; i < castor.getPropertyCount(); i++)
    {
      if (castor.getProperty(i) == property)
      {
        castor.removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property [" + property.getName() + "] is not part of " + getClass().getName());
  }


  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    for (UIGroupModel element : getElements())
    {
      element.addReferrerObject(result,model);
    }
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
  public ObjectModel getParent()
  {
    return getJacobFormModel();
  }

  @Override
  public boolean isInUse()
  {
    return true;
  }

  public void addElement(UIGroupModel group)
  {
    castor.addGroup(group.getCastor());
    getElements().add(group);
    group.setGroupContainerModel(this);
    
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, group);
  }
  
  /**
   * 
   * @return List[UIGroupModel]
   */
//  public List<UIGroupModel> getElements()
//  {
//    if(elements==null)
//    {
//      elements = new ArrayList<UIGroupModel>();
//      for(int i=0;i<castor.getGroupCount();i++)
//      {
//        CastorGroup group=castor.getGroup(i);
//        UIGroupModel groupModel=new UIGroupModel(getJacobModel(),this, group);
//        elements.add(groupModel);
//      }
//    }
//    return elements;
//  }
  
  public List<UIGroupModel> getElements()
  {
    if(groups==null)
    {
      groups = new ArrayList<UIGroupModel>();
      for(int i=0;i<getCastorContainer().getGroupCount();i++)
      {
        CastorGroup group=getCastorContainer().getGroup(i);
        // Die Gruppe muss sich dem TabContainer anpassen
        //
//        UIGroupModel groupModel=new UIGroupModel(getJacobModel(),pane, group);
        UIGroupModel groupModel=new UIGroupModel(getJacobModel(),null, group);
        groupModel.setSize(new Dimension(0,0));
        groupModel.setLocation(new Point(0,0));
        groups.add(groupModel);
      }
    }
    return groups;
  }
   

  public List getLinkedDomainModels()
  {
    return Collections.EMPTY_LIST;
  }


  public boolean isUIElementNameFree(String name)
  {
    for (UIGroupModel group : getElements())
    {
      if(group.isUIElementNameFree(name)==false)
        return false;
    }
    return true;
  }


  public void removeElement(UIGroupModel group)
  {
    for(int i=0;i<castor.getGroupCount();i++)
    {
      if(castor.getGroup(i)==group.getCastor())
      {
        castor.removeGroup(i);
        groups.remove(group);
        firePropertyChange(PROPERTY_ELEMENT_REMOVED, group, null);
        // Dem Browser und alle die diesen anzeigen mitteilen, das sich der (Error/Warning) Status 
        // moeglicherweise geaendert hat. Der Browser feuert ein Event und alle Listener aktualisieren sich dann
        // /Es gibt eventuell keine Gruppe mehr die diesen Browser benoetigt)
        group.getBrowserModel().firePropertyChange(PROPERTY_BROWSER_CHANGED, null,group.getBrowserModel());
        break;
      }
    }
  }
  
  protected void renameAliasReference(String from, String to)
  {
    for (UIGroupModel group : getElements())
    {
      group.renameAliasReference(from, to);
    }
  }
  
  public void renameBrowserReference(String from, String to)
  {
    for (UIGroupModel group : getElements())
    {
      group.renameBrowserReference(from, to);
    }
  }
  
  public void renameEventHandler(String fromClass, String toClass)
  {
    for (UIGroupModel group : getElements())
    {
      group.renameEventHandler(fromClass, toClass);
    }
  }
  
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
  public boolean isI18NKeyInUse(String key)
  {
    for (UIGroupModel group : getElements())
    {
      if(group.isI18NKeyInUse(key))
        return true;
    }
    return false;
  }
    
  protected void createMissingI18NKey()
  {
    for (UIGroupModel group : getElements())
    {
      group.createMissingI18NKey();
    }
  }
  
  protected void renameRelationReference(String from, String to)
  {
    for (UIGroupModel group : getElements())
    {
      group.renameRelationReference(from, to);
    }
  }
  
  protected void renameRelationsetReference(String from, String to)
  {
    // rename the relationset of foreign fields
    //
    for (UIGroupModel group : getElements())
    {
      group.renameRelationsetReference(from, to);
    }
  }

  @Override
  public void setName(String name) throws Exception
  {
    
  }

}
