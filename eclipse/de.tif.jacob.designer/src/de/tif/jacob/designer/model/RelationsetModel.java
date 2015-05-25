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
 * Created on Oct 20, 2004
 *
 */
package de.tif.jacob.designer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowRelationsetEditorAction;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.exception.InvalidNameException;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 * 
 */
public class RelationsetModel extends ObjectWithPropertyModel implements IOpenable
{
	private final CastorRelationset castor;

	private List notes = new ArrayList();
	
	// TableAliasModels welche mit DragDrop in den GUI Editor geworfen worden sind.
	// 
	private List nonPersistentTableAliases = new ArrayList();
	
  public RelationsetModel()
  {
    castor = new CastorRelationset();
    castor.setName("relationset_"+System.currentTimeMillis());
  }

  public RelationsetModel(JacobModel jacobModel, String name)
  {
    super(jacobModel);
    castor = new CastorRelationset();
    castor.setName(name);
  }

  
  public RelationsetModel(JacobModel jacob, CastorRelationset relationset)
  {
    super(jacob);
    this.castor = relationset;
    
    // collect all sticky notes of the relationset
    //
    for(int i=0 ; i< getCastorPropertyCount();i++)
    {
      String propName = getCastorProperty(i).getName();
      if(propName.startsWith(RelationsetStickyNoteModel.STICKY_NOTE_PREFIX) && propName.endsWith(".text"))
      {
        // sticky note found
        String guid = StringUtils.replace(propName,".text", "");
        notes.add(new RelationsetStickyNoteModel(this,guid));
      }
    }
  }

  /**
   * 
   */
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
	  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
	  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,"Name", PROPERTYGROUP_COMMON);
		
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
	  try
	  {
			if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
				setName((String) val);
			else
				super.setPropertyValue(propName, val);
		} 
		catch (Exception e) 
		{
			JacobDesigner.showException(e);
		}
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
			return getName();

		return super.getPropertyValue(propName);
	}
   
  public String getName()
  {
    return castor.getName();
  }

  
  
  @Override
  public String getExtendedDescriptionLabel()
  {
    if(getJacobModel().getTestRelationset()==this)
      return ">"+getName();
    return getName();
  }

  public void setName(String name) throws Exception
  {
		String save = getName();
		
		if(StringUtil.saveEquals(name,save))
		  return;
		
		// pr�fen ob der Name bereits vergeben ist. Der Relationsetname muss eindeutig sein.
		//
		if(getJacobModel().getRelationsetModel(name)!=null)
			throw new InvalidNameException("Name is already in use. Please use another one.");

		// Es muss durch alle Elemente welche die Relation als Referenz halten gegangen werden
    // und der Name angepasst
    //
    getJacobModel().renameRelationsetReference(save,name);
  
    castor.setName(name);
		firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }
  
 
  public boolean contains(RelationModel relation)
  {
    for (int i = 0; i < this.castor.getRelationCount(); i++)
    {
      if (relation.getName().equals(this.castor.getRelation(i)))
      {
        return true;
      }
    }
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.editor.datamodel.model.IDataModel#addRelation(de.tif.jacob.designer.editor.datamodel.model.IRelation)
   */
  public void addElement(RelationModel relation)
  {
    if (!contains(relation))
    {
      // add relation physically to the set
      castor.addRelation(relation.getName());
      firePropertyChange(PROPERTY_ELEMENT_ADDED, null, relation);
      relation.getToTableAlias().firePropertyChange(ObjectModel.PROPERTY_RELATIONSET_CHANGED,null,null);
      relation.getFromTableAlias().firePropertyChange(ObjectModel.PROPERTY_RELATIONSET_CHANGED,null,null);
      relation.firePropertyChange(PROPERTY_ELEMENT_ASSIGNED, null, this);
      // n-side
      // foreignTableKy
      // 
    }
   }
  
  /**
   * 
   * @param tablealias The table alias to add to the relation set
   * @return List[RelationModel] all added relations related to the added table
   */
  public List addElement(TableAliasModel table)
  {
    if (contains(table))
      return Collections.EMPTY_LIST;

    List relations = getJacobModel().getRelationModels();
    List result    = new ArrayList();
    
    for (int i = 0; i < relations.size(); i++)
    {
      RelationModel relation = (RelationModel) relations.get(i);

      //
      // check whether the relation's primary table is the table to add and the foreign table
      // is already within this data model or also vice verca
      if ((relation.getToTableAlias()==table   && contains(relation.getFromTableAlias())) 
       || (relation.getFromTableAlias()==table && contains(relation.getToTableAlias())))
      {
        addElement(relation);
        result.add(relation);
      }
    }
    
    // Falls keine Relation hinzugef�gt wurde, muss die Tabelle in einer tempo. Liste
    // gehalten werden, damit diese in dem GUI Designer angezeigt werden kann.
    // Die Tabelle kann dan mit DragDrop mit einer anderen Tabelle verbunden werden.
    //
    if(result.size()==0)
    {
      nonPersistentTableAliases.add(table);
      firePropertyChange(PROPERTY_ELEMENT_ADDED, null, table);
    }
    return result;
  }

  public void addElement(RelationsetStickyNoteModel note)
  {
    notes.add(note);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null,note);
   }
  
  public void removeElement(RelationsetStickyNoteModel note)
  {
    notes.remove(note);
    List toRemove = new ArrayList();
    for(int i=0; i<getCastorPropertyCount();i++)
    {
      CastorProperty prop=getCastorProperty(i);
      if(prop.getName().startsWith(note.getGuid()))
        toRemove.add(prop);
    }
    Iterator iter = toRemove.iterator();
    while (iter.hasNext())
    {
      CastorProperty obj = (CastorProperty) iter.next();
      removeCastorProperty(obj);
    }
    
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, note, null);
   }

  /**
   * Returns als RelationModel related with the TableAliasModel (primary or foreign)
   * 
   * @param alias
   * @return
   */
  public List getRelationModels(TableAliasModel alias)
  {
    List result = new ArrayList();
    
    for (int i=0; i < castor.getRelationCount(); i++)
    {
      String relName = castor.getRelation(i);
      RelationModel relation =  getJacobModel().getRelationModel(relName);

      if(relation!=null)
      {
        if(relation.getFromTableAlias()==alias)
          result.add(relation);
  
        if(relation.getToTableAlias()==alias)
          result.add(relation);
      }
      else
      {
        System.out.println("Relationset ["+relName+"] not found.");
      }
    }
    
    return result;
  }
  
  public List<RelationModel> getRelationModels()
  {
    List<RelationModel> result = new ArrayList<RelationModel>();
    
    for (int i=0; i < castor.getRelationCount(); i++)
    {
      String relName = castor.getRelation(i);
      RelationModel model = getJacobModel().getRelationModel(relName);
      if(model!=null)
        result.add(model);
    }
    
    return result;
  }
 
  public boolean contains(TableAliasModel alias)
  {
    return getTableAliasModels().contains(alias);
  }
  
  /**
   * 
   * @return List[TableAliasModel]
   */
  
  public List getTableAliasModels()
  {
    List tables = new ArrayList();
    
    for (int i=0; i < castor.getRelationCount(); i++)
    {
      String relName = castor.getRelation(i);
      RelationModel relation = getJacobModel().getRelationModel(relName);
      
      TableAliasModel table = relation.getFromTableAlias();
      if (!tables.contains(table))
        tables.add(table);

      table = relation.getToTableAlias();
      if (!tables.contains(table))
        tables.add(table);
    }
    Iterator iter = nonPersistentTableAliases.iterator();
    while (iter.hasNext())
    {
      TableAliasModel alias = (TableAliasModel) iter.next();
      if(!tables.contains(alias))
        tables.add(alias);
    }
    return tables;
  }
  

	/* (non-Javadoc)
   * @see de.tif.jacob.designer.model.IDataModel#isLayoutManualAllowed()
   */
  public boolean isLayoutManualAllowed()
  {
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.IDataModel#isLayoutManualDesired()
   */
  public boolean isLayoutManualDesired()
  {
    return true;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.editor.datamodel.model.IDataModel#removeRelation(de.tif.jacob.designer.editor.datamodel.model.IRelation)
   */
  public void removeElement(RelationModel relation)
  {
    // remove relation physically from the set
    for (int i=0; i < this.castor.getRelationCount(); i++)
    {
      if (this.castor.getRelation(i).equals(relation.getName()))
      {
        this.castor.removeRelation(i);
        firePropertyChange(PROPERTY_ELEMENT_REMOVED, relation, null);
        relation.getToTableAlias().firePropertyChange(ObjectModel.PROPERTY_RELATIONSET_CHANGED,null,null);
        relation.getFromTableAlias().firePropertyChange(ObjectModel.PROPERTY_RELATIONSET_CHANGED,null,null);
        relation.firePropertyChange(PROPERTY_ELEMENT_UNASSIGNED, this, null);
        return;
      }
    }
  }
  
  public void removeTmpElement(TableAliasModel alias)
  {
    nonPersistentTableAliases.remove(alias);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, alias, null);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void removeCastorProperty(CastorProperty property)
  {
    for(int i=0;i<castor.getPropertyCount();i++)
    {
      if(castor.getProperty(i)==property)
      {
        castor.removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property ["+property.getName()+"] is not part of "+getClass().getName());
  }
  
  public Rectangle getTableAliasBounds(TableAliasModel alias)
	{
	  Rectangle rect = new Rectangle();
	  rect.x      = getCastorIntProperty(alias.getName()+".x",-1);
	  rect.y      = getCastorIntProperty(alias.getName()+".y",-1);
	  rect.width  = -1;//getIntProperty(alias.getName()+".width",-1);
	  rect.height = -1;//getIntProperty(alias.getName()+".height",-1);
	  if(rect.x==-1)
	    return null;
    if(rect.height==0 && rect.width==0 && rect.x==0 && rect.y==0)
      return null;
    
	  return rect;
	}
	

	public void setTableAliasBounds(TableAliasModel alias, Rectangle rect)
	{
	  Rectangle save = getTableAliasBounds(alias);
	  if(save!=null && save.equals(rect))
	    return;
	  
	  setCastorProperty(alias.getName()+".x", ""+rect.x);
	  setCastorProperty(alias.getName()+".y", ""+rect.y);
		alias.firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, null, this);
	}
	
	public void setTableAliasExpanded(TableAliasModel alias, boolean expanded)
	{
	  setCastorProperty(alias.getName()+".expanded", ""+expanded);
	  alias.firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, null,this);
	}
	
	public boolean getTableAliasExpanded(TableAliasModel alias)
	{
	    return getCastorBooleanProperty(alias.getName()+".expanded",false);
	}

	public String getInfo()
  {
    return getJacobModel().getTestRelationset()==this?"This is the current 'test relationset'":null;
  }
  
  public String getError()
  {
    return hasCircle()?"Relationset definition contains a loop":null;
  }
  
  public String getWarning()
  {
    return castor.getRelationCount()==0?"Relationset contains no relation":null;
  }
  
  public boolean isInUse()
  {
    // Es darf an keinem Button in einer Form verwendet werden 
    //
    for (UIJacobFormModel form : getJacobModel().getJacobFormModels())
    {
      for (UIGroupModel group : form.getGroupModels())
      {
        for (UIGroupElementModel element : group.getElements())
        {
          if(element instanceof AbstractActionEmitterModel)
          {
            AbstractActionEmitterModel action = (AbstractActionEmitterModel)element;
            if(action.getRelationsetModel()==this)
              return true;
          }
        }
      }
    }
    return false;
  }

  public boolean hasCircle()
  {
    List relations = getRelationModels();
    if(relations.size()<2)
      return false;
    RelationModel startRelation = (RelationModel)relations.get(0);

    return  hasCircle(startRelation.getToTableAlias(),new ArrayList(), null);
  }
  
  private boolean hasCircle(TableAliasModel alias, List visited, RelationModel relationComesFrom)
  {
    if(visited.contains(alias))
      return true;
    
    visited.add(alias);
    Iterator iter = getRelationModels(alias).iterator();
    while (iter.hasNext())
    {
      RelationModel relation = (RelationModel) iter.next();
      if(relation != relationComesFrom)
      {
	      boolean result=false;
	      if(relation.getToTableAlias()==alias)
	        result = hasCircle(relation.getFromTableAlias(),visited, relation);
	      else
	        result = hasCircle(relation.getToTableAlias(),visited, relation);
	      
	      if(result==true)
	        return true;
      }
    }
    return false;
  }
  
  public CastorRelationset getCastor()
  {
    return castor;
  }
  
  public List getNotes()
  {
    return notes;
  }

  
  public void renameAliasReference(String from, String to)
  {
    String x   = getCastorStringProperty(from+".x");
    String y   = getCastorStringProperty(from+".y");
    String exp = getCastorStringProperty(from+".expanded");
    if(x!=null)
    {
      setCastorProperty(from+".x",null);
      setCastorProperty(to+".x",x);
    }
    if(y!=null)
    {
      setCastorProperty(from+".y",null);
      setCastorProperty(to+".y",y);
    }
    if(exp!=null)
    {
      setCastorProperty(from+".expanded",null);
      setCastorProperty(to+".expanded",exp);
    }
  }
  
  
  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(getTableAliasModels().contains(model))
      result.addReferences(this);
  }
  
  public void openEditor()
  {
    new ShowRelationsetEditorAction()
    {
      @Override
      public RelationsetModel getRelationsetModel()
      {
        return RelationsetModel.this;
      }
    }.run(null);
  } 
  
  @Override
  public ObjectModel getParent()
  {
    return getJacobModel().getApplicationModel();
  }
}
