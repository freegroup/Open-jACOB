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
 * Created on 05.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import de.tif.jacob.core.definition.impl.jad.castor.CastorKey;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class KeyModel extends ObjectModel
{
  public final static String DBTYPE_FOREIGN = "FOREIGN";
  public final static String DBTYPE_PRIMARY = "PRIMARY";
  public final static String DBTYPE_UNIQUE  = "UNIQUE";
  public final static String DBTYPE_INDEX   = "INDEX";

  public final static String[] DBTYPES = 
  {
      DBTYPE_PRIMARY,
      DBTYPE_UNIQUE, 
      DBTYPE_INDEX,
      DBTYPE_FOREIGN 
  };

  private final CastorKey castor;
  private final TableModel tableModel;
  private String type;
  private List<FieldModel> fields;
  
  public KeyModel(TableModel table, CastorKey key, String type)
  {
    super(table.getJacobModel());

    this.castor     = key;
    this.tableModel = table;
    this.type       = type;
  }

  /**
   * @return Returns the parent.
   */
  public TableModel getTableModel()
  {
    return tableModel;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.ObjectModel#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   */
  public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
  {
    super.firePropertyChange(propertyName, oldValue, newValue);
    
    // additionally inform parent that a child property has changed
    this.tableModel.firePropertyChange(TableModel.PROPERTY_KEY_CHANGED, null, this);
  }
  
  public String getName()
  {
    return StringUtil.toSaveString(castor.getName());
  }

  /**
   * @param name
   */
  public void setName(String name)
  {
    String save =castor.getName();
    
    if (StringUtil.saveEquals(name, save))
      return;
    
    getJacobModel().renameKeyReference(this, save, name);
    
    this.castor.setName(name);
    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }

  /**
   * @return Returns the type.
   */
  public String getType()
  {
    return type;
  }


  /**
   * @return
   */
  public String[] getFields()
  {
    return this.castor.getField();
  }
 
  /**
   * 
   * @return List[FieldModel]
   */
  public List<FieldModel> getFieldModels()
  {
    if(fields!=null)
      return fields;
    
    fields = new ArrayList<FieldModel>();
    for(int i =0;i<castor.getFieldCount(); i++)
    {
      FieldModel field = getTableModel().getFieldModel(castor.getField(i));
      if(field==null)
        throw new RuntimeException("Unable to find field: ["+castor.getField(i)+"] in table ["+this.getTableModel().getName()+"]");
      fields.add(field);
    }
    return fields;
  }
  
  private int indexOf(String field)
  {
    for (int i=0; i < this.castor.getFieldCount(); i++)
    {
      if (this.castor.getField(i).equals(field))
      {
        return i;
      }
    }
    return -1;
  }

  public void addElement(FieldModel field)
  {
    // plausibility check
    if (!this.tableModel.equals(field.getTableModel()))
      throw new IllegalArgumentException("field is not element of this table. Unable to add field to this table.");

    // field already contained?
    if (-1 == indexOf(field.getName()))
    {
      this.castor.addField(field.getName());
      fields = null; // remove chached references
      firePropertyChange(PROPERTY_FIELDS_CHANGED, null, field);
    }
  }

  public boolean removeElement(FieldModel field)
  {
    // plausibility check
    if (tableModel != field.getTableModel())
      return false;

    int index = indexOf(field.getName());
    if (-1 != index)
    {
      removeField(index);
      firePropertyChange(PROPERTY_FIELDS_CHANGED, field, null);
      return true;
    }
    return false;
  }

  /**
   * 
   * @param field
   * @return returns true if the hands over field member of this key object.
   */
  public boolean contains(FieldModel field)
  {
    return indexOf(field.getName())>=0;
  }
  
  public void removeField(int index)
  {
    String value = this.castor.removeField(index);
    fields = null; // delete the cached objects
    if(value!=null)
      firePropertyChange(PROPERTY_FIELDS_CHANGED, value, null);
  }
  
  /**
   * TODO: choose a better function name.....
   * @return
   */
  public boolean existsUniqueKeyWithSameFields()
  {
    // Falls es in der ToTable einen UniqueKey gibt, welcher alle Felder des toKey enthält,
    // dann handelt es sich um eine 1-1 Relation
    //
    
    for (KeyModel key : getTableModel().getKeyModels())
    {
      if(key!=this && DBTYPE_UNIQUE.equals(key.getType()))
      {
        // der key muss mindestens ein Feld enthalten...
        boolean hit = key.getFieldModels().size()>0;
        for (FieldModel field : key.getFieldModels())
        {
          // ....und "this" muss alle Felder des Unique Key enthalten 
          hit = hit && this.contains(field);
        }
        if(hit)
          return true;
      }
    }
    
    return false;    
  }
  
  /**
   * @param selectionIndex
   * @return
   */
  public String upField(int selectionIndex)
  {
    String field = this.castor.removeField(selectionIndex);
    this.castor.addField(selectionIndex-1, field);

    firePropertyChange(PROPERTY_FIELDS_CHANGED, null, field);
    return field;
  }

  /**
   * @param selectionIndex
   * @return
   */
  public String downField(int selectionIndex)
  {
    String field = this.castor.removeField(selectionIndex);
    this.castor.addField(selectionIndex+1, field);
    
    firePropertyChange(PROPERTY_FIELDS_CHANGED, null, field);
    return field;
  }

  /**
   * Returns true if it possible to make a relation between thes keys.
   * 
   * @param other
   * @return
   */
  public boolean match(KeyModel other)
  {
    try
    {
      Iterator thisIter  = getFieldModels().iterator();
      Iterator otherIter = other.getFieldModels().iterator();
      while(thisIter.hasNext())
      {
        FieldModel thisField  =(FieldModel) thisIter.next();
        FieldModel otherField =(FieldModel) otherIter.next();
        if(!thisField.match(otherField))
          return false;
      }
    }
    catch( Exception e)
    {
      return false;
    }
    return true;
  }
  
  
  protected CastorKey getCastor()
  {
    return castor;
  }

  protected void renameFieldReference(FieldModel field,String from , String to)
  {
    for(int i=0; i<getCastor().getFieldCount();i++)
    {
      if(getCastor().getField(i).equals(from))
      {
        getCastor().setField(i,to);
        fields = null; // remove the cached fields
      }
    }
  }
  
  public String getError()
  {
    if(getFieldModels().size()==0)
      return "Key definition contains no columns";
    
    if(StringUtil.saveEquals("",getName()))
      return "Name of the key definition is required";
    
    if(getTableModel().getKeyModel(getName())!=this)
      return "Name of the key definition already exists";
    
    // Falls der Key ein nicht mehr vorhandenes Feld enthält ist dies ein Fehler
    //
    for (FieldModel field : getFieldModels())
    {
      if(field == field.getTableModel().NULL_FIELD)
        return "Key contains invalid table field";
    }
    return null;
  }
  
  public String getWarning()
  {
    return isInUse()?null:"Key definition is not used in any relation";
  }
  
  public String getInfo()
  {
    return null;
  }
  
  public boolean isInUse()
  {
    boolean inUse=true;
    if(getType() == KeyModel.DBTYPE_FOREIGN)
    {
    	inUse= false;
      Iterator relIter =getJacobModel().getRelationModels().iterator();
      while (relIter.hasNext())
      {
        RelationModel relation = (RelationModel) relIter.next();
        if(relation.getToKey()==this)
        {
          inUse=true;
          break;
        }
      }
    }
    return inUse;
  }
  
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }

  public String getImageBaseName()
  {
    return "KeyModel"+StringUtils.capitalise(getType().toLowerCase());
  }

  @Override
  public ObjectModel getParent()
  {
    return getTableModel();
  }
}
