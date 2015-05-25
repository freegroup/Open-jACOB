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
 * Created on Oct 26, 2004
 *
 */
package de.tif.jacob.designer.model;

import java.util.Iterator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelation;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelationChoice;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;


/**
 *
 */
public  class RelationModel extends ObjectModel
{
  private final CastorRelation castor;

  
  public RelationModel( TableAliasModel fromTable, TableAliasModel toTable, KeyModel toKey)
  {
    super(fromTable.getJacobModel());
    
    this.castor   = new CastorRelation();
    this.castor.setName(fromTable.getName()+"_"+toTable.getName());
    this.castor.setCastorRelationChoice(new CastorRelationChoice());
    this.castor.getCastorRelationChoice().setOneToMany(new CastorOneToMany());
    this.castor.getCastorRelationChoice().getOneToMany().setFromAlias(fromTable.getName());
    this.castor.getCastorRelationChoice().getOneToMany().setToAlias(toTable.getName());
    this.castor.getCastorRelationChoice().getOneToMany().setToKey(toKey.getName());

    fromTable.primaryKeyRelationships.add(this);
    toTable.foreignKeyRelationships.add(this);
  }

	/**
   * @param relation
   * @param model
   * @param toTable
   * @param fromTable
   * 
   */
  public RelationModel(JacobModel jacob, CastorRelation relation, TableAliasModel fromTable, TableAliasModel toTable)
  {
    super(jacob);
    this.castor = relation;

    fromTable.primaryKeyRelationships.add(this);
    toTable.foreignKeyRelationships.add(this);
  }

  /**
   * rename the internal references to a table alias if a alias has been renamed.
   * 
   * @param from
   * @param to
   */
  protected void renameAliasReference(String from, String to)
  {
    if(castor.getCastorRelationChoice().getOneToMany().getFromAlias().equals(from))
      castor.getCastorRelationChoice().getOneToMany().setFromAlias(to);
    
    if(castor.getCastorRelationChoice().getOneToMany().getToAlias().equals(from))
      castor.getCastorRelationChoice().getOneToMany().setToAlias(to);
  }

  protected void renameKeyReference(KeyModel key, String from, String to)
  {
    TableModel table=getToTableAlias().getTableModel();

    if(table != key.getTableModel())
      return;
    
    if(castor.getCastorRelationChoice().getOneToMany()!=null && castor.getCastorRelationChoice().getOneToMany().getToKey().equals(from))
      castor.getCastorRelationChoice().getOneToMany().setToKey(to);
  }

  public String getName()
  {
    return this.castor.getName();
  }
  
  public void setName(String name) throws Exception
  {
    String save =this.castor.getName();
    if (StringUtil.saveEquals(name, save))
      return;

  
    this.castor.setName(name);

    firePropertyChange(PROPERTY_NAME_CHANGED, save,name);
  }

	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 4];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,       "Name", PROPERTYGROUP_DB);
		descriptors[superDescriptors.length+1] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_FROM_TABLE, "From Table", PROPERTYGROUP_DB);
		descriptors[superDescriptors.length+2] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_TO_TABLE,   "To Table",   PROPERTYGROUP_DB);
		descriptors[superDescriptors.length+3] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_KEY,      "Foreign key",PROPERTYGROUP_DB);

		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		try 
		{
			if (propName ==ID_PROPERTY_NAME)
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
		if(propName ==ID_PROPERTY_NAME)
			return getName();
		if(propName ==ID_PROPERTY_TO_TABLE)
			return getToTableAlias().getName();
		if(propName ==ID_PROPERTY_FROM_TABLE)
			return getFromTableAlias().getName();
		if(propName ==ID_PROPERTY_KEY)
			return getToKey().getName();
		else
			return super.getPropertyValue(propName);
	}

  public boolean isInUse()
  {
    // prüfen ob das Teil in irgendeinem Relationset verwendet wird
    //
    Iterator iter = getJacobModel().getRelationsetModels().iterator();
    while (iter.hasNext())
    {
      RelationsetModel relationset = (RelationsetModel) iter.next();
      if(relationset.contains(this))
        return true;
    }
    
    // Prüfen ob der alias in einem GUI Element verwendet wird
    // 1. ForeignField
    // 2. BrowserForeignField
    // 3. Informbrowser
    //
    iter = getJacobModel().getBrowserModels().iterator();
    while (iter.hasNext())
    {
      BrowserModel browser = (BrowserModel) iter.next();
      for (int i=0;i< browser.getCastor().getFieldCount();i++)
      {
        CastorBrowserField cfield = browser.getCastor().getField(i);
        if((cfield.getCastorBrowserFieldChoice()!=null) &&
           (cfield.getCastorBrowserFieldChoice().getTableField()!=null) &&
           (cfield.getCastorBrowserFieldChoice().getTableField().getForeign()!=null) &&
           (cfield.getCastorBrowserFieldChoice().getTableField().getForeign().getRelationToUse()!=null) &&
           (cfield.getCastorBrowserFieldChoice().getTableField().getForeign().getRelationToUse().equals(getName())))
       	  	return true;
      }
    }
    
    iter = getJacobModel().getJacobFormModels().iterator();
    while (iter.hasNext())
    {
      UIJacobFormModel form = (UIJacobFormModel) iter.next();
      Iterator groupIter = form.getGroupModels().iterator();
      while (groupIter.hasNext())
      {
        UIGroupModel group = (UIGroupModel) groupIter.next();
        Iterator elementIter = group.getElements().iterator();
        while (elementIter.hasNext())
        {
          ObjectModel element = (ObjectModel) elementIter.next();
          if(element instanceof UIDBForeignFieldModel)
          {
            UIDBForeignFieldModel ff = (UIDBForeignFieldModel)element;
            if(ff.getRelationToUse()==this)
              return true;
          }
          if(element instanceof UIDBInformBrowserModel)
          {
            UIDBInformBrowserModel ifb = (UIDBInformBrowserModel)element;
            if(ifb.getRelationToUse()==this)
              return true;
          }
        }
      }
    }
    return false;
  }
  
  public String getError()
  {
    return null;
  }
  
  public String getWarning()
  {
    return isInUse()?null:"Relation is not used in any Object";
  }

  public String getInfo()
  {
    return null;
  }
  
  
	public TableAliasModel getFromTableAlias()
  {
	  // primary table
	  return getJacobModel().getTableAliasModel(castor.getCastorRelationChoice().getOneToMany().getFromAlias());

  }
  
  public TableAliasModel getToTableAlias()
  {
    // foreign table
    return getJacobModel().getTableAliasModel(castor.getCastorRelationChoice().getOneToMany().getToAlias());
  }

  public KeyModel getToKey()
  {
    return getToTableAlias().getKeyModel(castor.getCastorRelationChoice().getOneToMany().getToKey());
  }
  

  /**
   * use getToTableAlias()
   * @return
   * @deprecated 
   */
  public TableAliasModel getForeignKeyTable()
  {
    return getToTableAlias();
  }


  public CastorRelation getCastor()
  {
    return castor;
  }
  
  /* 
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return getName()+"  ["+getFromTableAlias().getName()+"->"+getToTableAlias().getName()+"]";
  }
  
  
  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    // Wenn das model ein PrimaryKey ist wird geprüft ob dies ein Teil von dieser Relation
    // ist. Wenn ja, dann wird der TableAlias der anderen Seite eingetragen
    //
    if(getFromTableAlias().getPrimaryKeyModel() == model)
      result.addReferences(getToTableAlias());
  }

  @Override
  public ObjectModel getParent()
  {
    return getJacobModel().getApplicationModel();
  }
  
}
