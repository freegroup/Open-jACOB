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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.BinaryField;
import de.tif.jacob.core.definition.impl.jad.castor.BooleanField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorKey;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTable;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice;
import de.tif.jacob.core.definition.impl.jad.castor.DateField;
import de.tif.jacob.core.definition.impl.jad.castor.DecimalField;
import de.tif.jacob.core.definition.impl.jad.castor.DocumentField;
import de.tif.jacob.core.definition.impl.jad.castor.DoubleField;
import de.tif.jacob.core.definition.impl.jad.castor.EnumerationField;
import de.tif.jacob.core.definition.impl.jad.castor.FloatField;
import de.tif.jacob.core.definition.impl.jad.castor.IntegerField;
import de.tif.jacob.core.definition.impl.jad.castor.LongField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextField;
import de.tif.jacob.core.definition.impl.jad.castor.TextField;
import de.tif.jacob.core.definition.impl.jad.castor.TimeField;
import de.tif.jacob.core.definition.impl.jad.castor.TimestampField;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.util.Trace;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *  
 */
public class TableModel extends ObjectWithPropertyModel
{
  private final CastorTable castor;

  private List<FieldModel> fields = null;
  private List<KeyModel> keys   = null;
  
//  private String warningMessage;
  public final FieldModel NULL_FIELD;
  
	/**
	 * Create a new table from scatsch.
	 * 
	 * @param source
	 * @param name
	 */
	public TableModel(PhysicalDataModel source, String name)
	{
	  super(source.getJacobModel());
	  
    keys = new ArrayList<KeyModel>();
	  
	  this.castor = new CastorTable();
	  this.castor.setDatasource(source.getName());
	  this.castor.setDbName(name);
	  this.castor.setName(name);

	  // add per default the column "pkey" which is primary key of this table
	  //
	  CastorTableField fieldCastor =  new CastorTableField();
    
	  LongField typeField = new LongField();
	  typeField.setAutoincrement(true);

	  fieldCastor.setCastorTableFieldChoice( new CastorTableFieldChoice());
	  fieldCastor.setRequired(true);
	  fieldCastor.setHistory(false);
	  fieldCastor.setReadonly(true);
	  fieldCastor.setDbName("pkey");
	  fieldCastor.setName("pkey");
	  if(getJacobModel().useI18N())
	  	fieldCastor.setLabel("%"+name.toUpperCase()+getJacobModel().getSeparator()+"PKEY");
	  else
	  	fieldCastor.setLabel(StringUtils.capitalise(fieldCastor.getName()));
	  
    fieldCastor.getCastorTableFieldChoice().setLong(typeField);

	  this.castor.addField(fieldCastor);


    KeyModel key = createPrimaryKey();
    key.getCastor().setName("primaryKey");
    key.getCastor().addField(fieldCastor.getName());

    NULL_FIELD = createNullField();
	}
	
  protected TableModel(JacobModel jacob, CastorTable table)
  {
    super(jacob);
    this.castor = table;

    keys = new ArrayList<KeyModel>();
    if (castor.getPrimaryKey() != null)
      keys.add(new KeyModel(this, castor.getPrimaryKey(), KeyModel.DBTYPE_PRIMARY));

    for (int i = 0; i < castor.getForeignKeyCount(); i++)
    {
      keys.add(new KeyModel(this, castor.getForeignKey(i), KeyModel.DBTYPE_FOREIGN));
    }
    
    for (int i = 0; i < castor.getUniqueIndexCount(); i++)
    {
      keys.add(new KeyModel(this, castor.getUniqueIndex(i), KeyModel.DBTYPE_UNIQUE));
    }
    
    for (int i = 0; i < castor.getIndexCount(); i++)
    {
      keys.add(new KeyModel(this, castor.getIndex(i), KeyModel.DBTYPE_INDEX));
    }
    
	  NULL_FIELD = createNullField();
  }

  private FieldModel createNullField()
  {
    FieldModel field = new FieldModel(getJacobModel(),this);
	  field.getCastor().setDbName("null");
	  field.getCastor().setLabel("null");
	  field.getCastor().setName("null");

	  CastorTableFieldChoice choice = new CastorTableFieldChoice();
    
    choice.setBinary(new BinaryField());
    choice.setDate(new DateField());
    choice.setDecimal(new DecimalField());
    choice.setDouble(new DoubleField());
    choice.setEnumeration(new EnumerationField());
    choice.setFloat(new FloatField());
    choice.setInteger(new IntegerField());
    choice.getInteger().setAutoincrement(false);
    choice.setLong(new LongField());
    choice.getLong().setAutoincrement(false);
    choice.setLongText(new LongTextField());
    choice.setDocument(new DocumentField());
    choice.setText(new TextField());
    choice.getText().setMaxLength(40);
    choice.getText().setSearchMode(TextFieldSearchModeType.UNBOUND);
    choice.getText().setCaseSensitive(false);
    choice.setTime(new TimeField());
    choice.setTimestamp(new TimestampField());
    choice.setBoolean(new BooleanField());
    
    field.getCastor().setCastorTableFieldChoice(choice);
    
    return field;
  }
  
  public String getName()
  {
    return StringUtil.toSaveString(castor.getName());
  }

  /**
   * @return
   */
  public String getDbName()
  {
    return StringUtil.toSaveString(castor.getDbName());
  }

  public boolean hasUnusedKeys()
  {
    Iterator<KeyModel> iter = getKeyModels().iterator();
    while (iter.hasNext())
    {
      KeyModel key = iter.next();
      if(key.getType() == KeyModel.DBTYPE_FOREIGN)
      {
	      boolean inUse=false;
	      Iterator relIter =getJacobModel().getRelationModels().iterator();
	      while (relIter.hasNext())
	      {
	        RelationModel relation = (RelationModel) relIter.next();
	        if(relation.getToKey()==key)
	        {
	          inUse=true;
	          break;
	        }
	      }
	      if(inUse==false)
	        return true;
      }
    }
    return false;
  }
  
  
  public PhysicalDataModel getDatasourceModel()
  {
    return getJacobModel().getDatasourceModel(castor.getDatasource());
  }

  
  public String getRepresentativeField()
  {
    return StringUtil.toSaveString(castor.getRepresentativeField());
  }

  public FieldModel getRepresentativeFieldModel()
  {
    return getFieldModel(castor.getRepresentativeField());
  }

  public String getHistoryField()
  {
    return StringUtil.toSaveString(castor.getHistoryField());
  }
  
  public boolean removeElement(FieldModel field)
  {
    // Feld aus den Browsern entfernen
    //
    for (BrowserModel browser : getJacobModel().getBrowserModels())
      browser.removeElement(field);

    // Feld aus den Keys entfernen
    //
    for (KeyModel key : getKeyModels())
      key.removeElement(field);

    // Falls das Feld das historyfeld war, wird das history feld zurï¿½ck gesetzt
    //
    if(field.getName().equals(getHistoryField()))
        setHistoryField(null);
    
    // Falls das Feld das 'representativ' Feld war, wir dies dann auch gelöscht
    //
    if(field.getName().equals(getRepresentativeField()))
      setRepresentativeField(null);
    
    // redirect all bounded fields to the NULL_FIELD
    //
    getJacobModel().renameFieldReference(field,field.getName(),NULL_FIELD.getName());
    
    for (int i = 0; i < castor.getFieldCount(); i++)
    {
      if (castor.getField(i).equals(field.getCastor()))
      {
        this.castor.removeField(i);

        if (fields != null)
        {
          for (i = 0; i < fields.size(); i++)
          {
            if (field.equals(fields.get(i)))
            {
              fields.remove(i);
              break;
            }
          }
        }
        
        firePropertyChange(PROPERTY_FIELD_DELETED, field, null);
        field.firePropertyChange(PROPERTY_FIELD_DELETED,  field, null);

        // only required for the application outline objects
        field.getJacobModel().firePropertyChange(PROPERTY_FIELD_DELETED,  field, null);
        
        return true;
      }
    }
    return false;
  }

  public boolean removeElement(KeyModel key)
  {
    if (key.getCastor().equals(castor.getPrimaryKey()))
    {
      castor.setPrimaryKey(null);
      
      deleteAndNotify(key);
      return true;
    }
    for (int i = 0; i < castor.getForeignKeyCount(); i++)
    {
      if (castor.getForeignKey(i).equals(key.getCastor()))
      {
        this.castor.removeForeignKey(i);

        deleteAndNotify(key);
        return true;
      }
    }
    for (int i = 0; i < castor.getIndexCount(); i++)
    {
      if (castor.getIndex(i).equals(key.getCastor()))
      {
        this.castor.removeIndex(i);

        deleteAndNotify(key);
        return true;
      }
    }
    for (int i = 0; i < castor.getUniqueIndexCount(); i++)
    {
      if (castor.getUniqueIndex(i).equals(key.getCastor()))
      {
        this.castor.removeUniqueIndex(i);

        deleteAndNotify(key);
        return true;
      }
    }
    return false;
  }
  
  private void deleteAndNotify(KeyModel key)
  {
    if (this.keys != null)
    {
      for (int i = 0; i < keys.size(); i++)
      {
        if (key.equals(keys.get(i)))
        {
          keys.remove(i);
          break;
        }
      }
    }

    firePropertyChange(PROPERTY_KEY_DELETED, null, key);
    key.firePropertyChange(KeyModel.PROPERTY_KEY_DELETED, null, key);
  }

  /**
   * 
   * @param name
   */
  public void setName(String name)
  {
    String save = castor.getName();
    if (StringUtil.saveEquals(name, save))
      return;
    
    boolean fitDBName = false;
    
    // keep dbname synchron with name
    if (StringUtil.saveEquals(castor.getDbName(),save))
      fitDBName = true;

    castor.setName(name);
    
    getJacobModel().renameTableReference(save,name);
    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
    
    if(fitDBName)
      setDbName(name);
  }
  
  /**
   * 
   * @param name
   */
  public void setDbName(String name)
  {
    String save =castor.getDbName();
    if (StringUtil.saveEquals(name, save))
      return;
    
    castor.setDbName(name);
    firePropertyChange(PROPERTY_DBNAME_CHANGED, save, name);
  }

  public void setDatasource(String datasource)
  {
    String save =castor.getDatasource();
    if (StringUtil.saveEquals(datasource, save))
      return;
    PhysicalDataModel oldDS= getDatasourceModel();
    castor.setDatasource(datasource);
    PhysicalDataModel newDS= getDatasourceModel();
    
    firePropertyChange(PROPERTY_DATASOURCE_CHANGED, save, datasource);
    
    if(oldDS!=null)
      oldDS.firePropertyChange(PROPERTY_ELEMENT_REMOVED, this, null);
    newDS.firePropertyChange(PROPERTY_ELEMENT_ADDED, null, this);
  }

	public void setFieldPosition(FieldModel field, int newIndex)
	{
	  int oldIndex = fields.indexOf(field);
	  
	  fields.remove(field);
	  CastorTableField castorField = castor.removeField(oldIndex);
    
    fields.add(newIndex,field);
    castor.addField(newIndex,castorField);
    firePropertyChange(PROPERTY_TABLE_CHANGED,  null, this);
	}

  public void setRepresentativeField(String field)
  {
    String save =castor.getRepresentativeField();
    if (StringUtil.saveEquals(field, save))
      return;
    
    castor.setRepresentativeField(field);
    firePropertyChange(PROPERTY_REPRESENTATIVE_FIELD, save, field);
  }

  
  public void setHistoryField(String field)
  {
    // leerstring auf null zurï¿½cksetzten
    field = (field!=null && field.length()==0)?null:field;
    
    String save = castor.getHistoryField();
    if (StringUtil.saveEquals(field,save))
      return;
    
    castor.setHistoryField(field);
    firePropertyChange(PROPERTY_HISTORY_FIELD_CHANGED, save,field);
  }

  protected void renameFieldReference(FieldModel field,String from , String to)
  {
    if(field.getTableModel()!=this)
      return;
    
    if(castor.getRepresentativeField()!=null && castor.getRepresentativeField().equals(from))
      castor.setRepresentativeField(to);
    
    if(castor.getHistoryField()!=null && castor.getHistoryField().equals(from))
      castor.setHistoryField(to);
    
    for (KeyModel key : getKeyModels())
      key.renameFieldReference(field,from,to);
  }
  
  /**
   * 
   * @param from the orignal key WITH the % at first character
   * @param to the new key WITH the % at first character
   */
  protected void renameI18NKey(String from , String to)
  {
    for (int i = 0; i < castor.getFieldCount(); i++)
    {
      String label = castor.getField(i).getLabel();
      if(label !=null && label.equals(from))
        castor.getField(i).setLabel(to);
    }
  }

  /**
   * 
   */
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    for (FieldModel field : getFieldModels())
      field.addReferrerObject(result,model);

    for (KeyModel key : getKeyModels())
      key.addReferrerObject(result,model);
  }

  
  protected void resetI18N()
  {
    for (FieldModel field : getFieldModels())
      field.resetI18N();
  }
  
  /**
   * 
   * @param key the key to check WITH the % at first character
   * @return
   */
  protected boolean isI18NKeyInUse(String key)
  {
    for (int i = 0; i < castor.getFieldCount(); i++)
    {
      String label = castor.getField(i).getLabel();
      if(label !=null && label.equals(key))
        return true;
    }
    return false;
  }

  protected void createMissingI18NKey()
  {
    for (int i = 0; i < castor.getFieldCount(); i++)
    {
      String label = castor.getField(i).getLabel();
      if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
        getJacobModel().addI18N(label.substring(1),"",false);
    }
  }

  /**
   * 
   * @return List[FieldModel]
   */
  public List<FieldModel> getFieldModels()
  {
    if (fields == null)
    {
      fields = new ArrayList<FieldModel>();
      for (int i = 0; i < castor.getFieldCount(); i++)
      {
        fields.add(new FieldModel(getJacobModel(), this, castor.getField(i)));
      }
    }
    return fields;
  }

  /**
   * 
   * @return List[KeyModel]
   */
  public List<KeyModel> getKeyModels()
  {
    return keys;
  }
  

  /**
   * 
   * @return KeyModel
   */
  public KeyModel getKeyModel(String keyName)
  {
    Iterator<KeyModel> iter =getKeyModels().iterator();
    while(iter.hasNext())
    {
      KeyModel keyModel = iter.next();
      if(keyModel.getName().equals(keyName))
        return keyModel;
    }
    return null;
  }

  /**
   * returns the primary key of the table or null
   * 
   * @return the primary key of the table or null
   */
  public KeyModel getPrimaryKeyModel()
  {
    if(castor.getPrimaryKey()==null)
      return null;
    return getKeyModel(castor.getPrimaryKey().getName());
  }

  public List<KeyModel> getMatchedForeingKeyModels(KeyModel primKey)
  {
    List<KeyModel> result = new ArrayList<KeyModel>();
    Iterator<KeyModel> iter = getKeyModels().iterator();
    while (iter.hasNext())
    {
      KeyModel key = iter.next();
      if(key.match(primKey) && key.getType()==KeyModel.DBTYPE_FOREIGN)
        result.add(key);
    }
    return result;
  }
  
  public FieldModel getFieldModel(String name)
  {
    if("null".equals(name))
      return NULL_FIELD;
    
    Iterator<FieldModel> iter = getFieldModels().iterator();
    while (iter.hasNext())
    {
      FieldModel field = iter.next();
      if (field.getName().equals(name))
        return field;
    }
    return null;
  }

  /**
   * 
   * @return List[String]
   */
  public List<String> getDBFieldNames()
  {
    List<String> names = new ArrayList<String>();
    int i = 0;
    for (Iterator<FieldModel> iter = getFieldModels().iterator(); iter.hasNext(); i++)
    {
      names.add(iter.next().getDbName());
    }
    Collections.sort(names);
    return names;
  }

  /**
   * 
   * @return List[String]
   */
  public List<String> getFieldNames()
  {
    List<String> names = new ArrayList<String>();
    int i = 0;
    for (Iterator<FieldModel> iter = getFieldModels().iterator(); iter.hasNext(); i++)
    {
      names.add(iter.next().getName());
    }
    Collections.sort(names);
    return names;
  }

  /**
   * 
   * @return List[String]
   */
  
  public List<String> getFieldNames(UIDBLocalInputFieldModel inputElement)
  {
    List<FieldModel> fields = getFields(inputElement);
    List<String> result = new ArrayList<String>();
    
    for (FieldModel model : fields)
    {
      result.add(model.getName());
    }
    Collections.sort(result);
    return result;
  }
  
  public List<FieldModel> getFields(UIDBLocalInputFieldModel inputElement)
  {
    List<FieldModel> names = new ArrayList<FieldModel>();
    int i = 0;
    for (Iterator<FieldModel> iter = getFieldModels().iterator(); iter.hasNext(); i++)
    {
      FieldModel fieldModel = iter.next();
      // TEXT darf auf ein PasswordInput gemappt werden
      //
      if ((inputElement instanceof UIDBPasswordModel) && (fieldModel.getType() == FieldModel.DBTYPE_TEXT))
          names.add(fieldModel);

      // LONG darf auf ein TextInput gemappt werden
      //
      if ((inputElement instanceof UIDBTextModel) && (fieldModel.getType() == FieldModel.DBTYPE_LONG))
          names.add(fieldModel);

      // INTEGER darf auf ein TextInput gemappt werden
      //
      if ((inputElement instanceof UIDBTextModel) && (fieldModel.getType() == FieldModel.DBTYPE_INTEGER))
          names.add(fieldModel);

      // FLOAT darf auf ein TextInput gemappt werden
      //
      if ((inputElement instanceof UIDBTextModel) && (fieldModel.getType() == FieldModel.DBTYPE_FLOAT))
          names.add(fieldModel);

      // DECIMAL darf auf ein TextInput gemappt werden
      //
      if ((inputElement instanceof UIDBTextModel) && (fieldModel.getType() == FieldModel.DBTYPE_DECIMAL))
          names.add(fieldModel);

      // DOUBLE darf auf ein TextInput gemappt werden
      //
      if ((inputElement instanceof UIDBTextModel) && (fieldModel.getType() == FieldModel.DBTYPE_DOUBLE))
          names.add(fieldModel);

      // TEXT darf auf ein TextInput gemappt werden
      //
      if ((inputElement instanceof UIDBTextModel) && (fieldModel.getType() == FieldModel.DBTYPE_TEXT))
          names.add(fieldModel);

      // TEXT darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_TEXT))
          names.add(fieldModel);

      // LONG darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_LONG))
          names.add(fieldModel);

      // FLOAT darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_FLOAT))
          names.add(fieldModel);
      
      // ENUM darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_ENUM))
          names.add(fieldModel);
      
      // ENUM darf auf ein RadioButtonGroup gemappt werden
      //
      if ((inputElement instanceof UIDBRadioButtonGroupModel) && (fieldModel.getType() == FieldModel.DBTYPE_ENUM))
          names.add(fieldModel);
      
      // DECIMAL darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_DECIMAL))
          names.add(fieldModel);

      // DOUBLE darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_DOUBLE))
          names.add(fieldModel);

      // DATE darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_DATE))
          names.add(fieldModel);
      
      // INTEGER darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_INTEGER))
          names.add(fieldModel);

      // TIME darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_TIME))
          names.add(fieldModel);

      // TIMESTAMP darf auf ein LabelModel gemappt werden
      //
      if ((inputElement instanceof UIDBLabelModel) && (fieldModel.getType() == FieldModel.DBTYPE_TIMESTAMP))
          names.add(fieldModel);
      
      // TEXT darf auf InformLongText gemappt werden
      //
      else if ((inputElement instanceof UIDBInformLongTextModel) && fieldModel.getType() == FieldModel.DBTYPE_TEXT)
        names.add(fieldModel);

      // DOCUMENT darf auf ein Image gemappt werden
      //
      if ((inputElement instanceof UIDBImageModel) && (fieldModel.getType() == FieldModel.DBTYPE_DOCUMENT))
          names.add(fieldModel);

      // DOCUMENT darf auf ein Document gemappt werden
      //
      if ((inputElement instanceof UIDBDocumentModel) && (fieldModel.getType() == FieldModel.DBTYPE_DOCUMENT))
          names.add(fieldModel);

      // LONGTEXT darf auf ein TextInput gemappt werden
      //
      else if ((inputElement instanceof UIDBTextModel) && (fieldModel.getType() == FieldModel.DBTYPE_LONGTEXT))
          names.add(fieldModel);

      // INTEGER darf auf eine Checkbox gemappt werden
      //
      else if ((inputElement instanceof UIDBCheckboxModel) && fieldModel.getType() == FieldModel.DBTYPE_INTEGER)
        names.add(fieldModel);

      // LONG darf auf eine Checkbox gemappt werden
      //
      else if ((inputElement instanceof UIDBCheckboxModel) && fieldModel.getType() == FieldModel.DBTYPE_LONG)
        names.add(fieldModel);

      // BOOLEAN darf auf eine Checkbox gemappt werden
      //
      else if ((inputElement instanceof UIDBCheckboxModel) && fieldModel.getType() == FieldModel.DBTYPE_BOOLEAN)
        names.add(fieldModel);

      // BOOLEAN darf auf ein normales Label gemappt werden
      //
      else if ((inputElement instanceof UIDBLabelModel) && fieldModel.getType() == FieldModel.DBTYPE_BOOLEAN)
        names.add(fieldModel);

      // LONGTEXT darf auf InformLongText gemappt werden
      //
      else if ((inputElement instanceof UIDBInformLongTextModel) && fieldModel.getType() == FieldModel.DBTYPE_LONGTEXT)
        names.add(fieldModel);

      // LONGTEXT darf auf LongText gemappt werden
      //
      else if ((inputElement instanceof UIDBLongTextModel) && fieldModel.getType() == FieldModel.DBTYPE_LONGTEXT)
        names.add(fieldModel);

      // DATE darf auf Date gemappt werden
      //
      else if ((inputElement instanceof UIDBDateModel) && fieldModel.getType() == FieldModel.DBTYPE_DATE)
        names.add(fieldModel);

      // UIDBTimeModel darf auf Time gemappt werden
      //
      else if ((inputElement instanceof UIDBTimeModel) && fieldModel.getType() == FieldModel.DBTYPE_TIME)
        names.add(fieldModel);

      // DBTYPE_TIMESTAMP darf auf DBDateTimeModel gemappt werden
      //
      else if ((inputElement instanceof UIDBDateTimeModel) && fieldModel.getType() == FieldModel.DBTYPE_TIMESTAMP)
        names.add(fieldModel);
      
      // enum darf auf UIDBEnumComboboxModel gemappt werden
      //
      else if ((inputElement instanceof UIDBEnumComboboxModel) && fieldModel.getType() == FieldModel.DBTYPE_ENUM)
        names.add(fieldModel);
      
      // text darf auf UIDBTextfieldComboboxModel gemappt werden
      //
      else if ((inputElement instanceof UIDBTextfieldComboboxModel) && fieldModel.getType() == FieldModel.DBTYPE_TEXT)
        names.add(fieldModel);
      
      // enum darf auf UIDBListboxModel gemappt werden
      //
      else if ((inputElement instanceof UIDBListboxModel) && fieldModel.getType() == FieldModel.DBTYPE_ENUM)
        names.add(fieldModel);
    }
    return names;
  }

  /**
   *  
   */
  public void addElement(FieldModel fieldModel)
  {
    getFieldModels().add(fieldModel);
    this.castor.addField(fieldModel.getCastor());
    
    firePropertyChange(TableModel.PROPERTY_FIELD_ADDED, null, fieldModel);
  }
  
  /**
   *  
   */
  public FieldModel createField()
  {
    FieldModel fieldModel = new FieldModel(getJacobModel(), this);
    getFieldModels().add(fieldModel);
    this.castor.addField(fieldModel.getCastor());
    
    firePropertyChange(TableModel.PROPERTY_FIELD_ADDED, null, fieldModel);
    
    return fieldModel;
  }

  public FieldModel createMatchingField(FieldModel templateField)
  {
    FieldModel field = createField();
    field.setType(templateField.getType());
    if(field.getType()== FieldModel.DBTYPE_TEXT)
    {
      field.getTextFieldType().setCaseSensitive(templateField.getTextFieldType().getCaseSensitive());
      field.getTextFieldType().setDefault(templateField.getTextFieldType().getDefault());
      field.getTextFieldType().setFixLength(templateField.getTextFieldType().getFixeLength());
      field.getTextFieldType().setMaxLength(templateField.getTextFieldType().getMaxLength());
      field.getTextFieldType().setSearchMode(templateField.getTextFieldType().getSearchMode());
    }
    else if(field.getType()== FieldModel.DBTYPE_TIMESTAMP)
    {
      field.getTimestampFieldType().setDefault(templateField.getTimestampFieldType().getDefault());
      field.getTimestampFieldType().setResolution(templateField.getTimestampFieldType().getResolution());
    }
    else if(field.getType()== FieldModel.DBTYPE_ENUM)
    {
      field.getEnumFieldType().setDefault(templateField.getEnumFieldType().getDefault());
      field.getEnumFieldType().setEnumValues(templateField.getEnumFieldType().getEnumValues());
    }
    else if(field.getType()== FieldModel.DBTYPE_BOOLEAN)
    {
      field.getBooleanFieldType().setDefault(templateField.getBooleanFieldType().getDefault());
    }
    else if(field.getType()== FieldModel.DBTYPE_LONG)
    {
      FieldModelTypeLong myType = field.getLongFieldType();
      FieldModelTypeLong myTemplateType = templateField.getLongFieldType();
      
      myType.setAutoincrement(myTemplateType.getAutoincrement());
      
      if (myTemplateType.hasDefaultValue())
        myType.setDefaultValue(myTemplateType.getDefaultValue());
      else
        myType.deleteDefaultValue();
      
      if (myTemplateType.hasMinValue())
        myType.setMinValue(myTemplateType.getMinValue());
      else
        myType.deleteMinValue();
      
      if (myTemplateType.hasMaxValue())
        myType.setMaxValue(myTemplateType.getMaxValue());
      else
        myType.deleteMaxValue();
    }
    else if(field.getType()== FieldModel.DBTYPE_INTEGER)
    {
      FieldModelTypeInteger myType = field.getIntegerFieldType();
      FieldModelTypeInteger myTemplateType = templateField.getIntegerFieldType();
      
      myType.setAutoincrement(myTemplateType.getAutoincrement());
      
      if (myTemplateType.hasDefaultValue())
        myType.setDefaultValue(myTemplateType.getDefaultValue());
      else
        myType.deleteDefaultValue();
      
      if (myTemplateType.hasMinValue())
        myType.setMinValue(myTemplateType.getMinValue());
      else
        myType.deleteMinValue();
      
      if (myTemplateType.hasMaxValue())
        myType.setMaxValue(myTemplateType.getMaxValue());
      else
        myType.deleteMaxValue();
    }
    else if(field.getType()== FieldModel.DBTYPE_FLOAT)
    {
      FieldModelTypeFloat myType = field.getFloatFieldType();
      FieldModelTypeFloat myTemplateType = templateField.getFloatFieldType();
      
      if (myTemplateType.hasDefaultValue())
        myType.setDefaultValue(myTemplateType.getDefaultValue());
      else
        myType.deleteDefaultValue();
      
      if (myTemplateType.hasMinValue())
        myType.setMinValue(myTemplateType.getMinValue());
      else
        myType.deleteMinValue();
      
      if (myTemplateType.hasMaxValue())
        myType.setMaxValue(myTemplateType.getMaxValue());
      else
        myType.deleteMaxValue();
    }
    else if(field.getType()== FieldModel.DBTYPE_DOUBLE)
    {
      FieldModelTypeDouble myType = field.getDoubleFieldType();
      FieldModelTypeDouble myTemplateType = templateField.getDoubleFieldType();
      
      if (myTemplateType.hasDefaultValue())
        myType.setDefaultValue(myTemplateType.getDefaultValue());
      else
        myType.deleteDefaultValue();
      
      if (myTemplateType.hasMinValue())
        myType.setMinValue(myTemplateType.getMinValue());
      else
        myType.deleteMinValue();
      
      if (myTemplateType.hasMaxValue())
        myType.setMaxValue(myTemplateType.getMaxValue());
      else
        myType.deleteMaxValue();
    }
    else if (field.getType() == FieldModel.DBTYPE_DECIMAL)
    {
      FieldModelTypeDecimal myType = field.getDecimalFieldType();
      FieldModelTypeDecimal myTemplateType = templateField.getDecimalFieldType();

      myType.setScale(myTemplateType.getScale());

      myType.setDefaultValue(myTemplateType.getDefaultValue());

      myType.setMinValue(myTemplateType.getMinValue());

      myType.setMaxValue(myTemplateType.getMaxValue());
    }
    field.setName(templateField.getTableModel().getName()+"_"+templateField.getDbName()+"_key");
      
    return field;
  }
  
  private static final Set<String> representativeFieldExcludeTypeList = 
    new HashSet<String>(Arrays.asList(new String[] {FieldModel.DBTYPE_BINARY, FieldModel.DBTYPE_LONGTEXT}));

  private static final Set<String> historyFieldIncludeTypeList = 
    new HashSet<String>(Arrays.asList(new String[] {FieldModel.DBTYPE_LONGTEXT}));

  /**
   * @return
   */
  public String[] getRepresentativeFieldNames()
  {
    return getTypedFieldNames(null, representativeFieldExcludeTypeList);
  }

  /**
   * @return
   */
  public String[] getHistoryFieldNames()
  {
    return getTypedFieldNames(historyFieldIncludeTypeList, null);
  }
  
  private String[] getTypedFieldNames(Set<String> includeSet, Set<String> excludeSet)
  {
    List<String> list = new ArrayList<String>();
    for (FieldModel field : getFieldModels())
    {
      if (includeSet != null)
      {
        if (!includeSet.contains(field.getType()))
        {
          continue;
        }
      }
      else
      {
        if (excludeSet.contains(field.getType()))
        {
          continue;
        }
      }
      list.add(field.getName());
    }
    
    String[] result = new String[list.size()];
    for (int i=0; i < list.size(); i++)
    {
      result[i] = list.get(i);
    }
    return result;
  }

  public KeyModel createPrimaryKey()
  {
    if (hasPrimaryKey())
    {
      throw new RuntimeException("A primary key already exists");
    }
    
    CastorKey tableKey = new CastorKey();
    this.castor.setPrimaryKey(tableKey);
    KeyModel newKey =new KeyModel(this, tableKey, KeyModel.DBTYPE_PRIMARY);
    addKey(newKey);
    return newKey;
  }
  
  public boolean hasPrimaryKey()
  {
    return this.castor.getPrimaryKey()!=null;
  }
  
  public KeyModel createMatchingForeignKey(String nameSuggestion, KeyModel foreignPrimaryKey)
  {
    Trace.start("TabelModel.createMatchingForeignKey");
    try
    {
      Trace.mark();
      KeyModel key = createForeignKey();
      Trace.print("createForeignKey");
      
      // einen 'guten' namen fuer den fkey erzeugen
      //
      Trace.mark();
      //TableModel table = foreignPrimaryKey.getTableModel();
      int index = 2;
      String defaultName = nameSuggestion+"_FKey";
      while(getKeyModel(defaultName)!=null)
      {
        defaultName = nameSuggestion+"_FKey"+index++;
      }
      key.setName(defaultName);
      Trace.print("create name for key");
      
      // Alle Felder für den fkey erzeugen
      //
      // feldname = <alias>_key
      Trace.mark();
      if( foreignPrimaryKey.getFieldModels().size()==1)
      {
        FieldModel field = (FieldModel) foreignPrimaryKey.getFieldModels().get(0);
        FieldModel newField = createMatchingField(field);
        
        String column_name = nameSuggestion+"_key";
  
        int counter=2;
        while(getFieldModel(column_name)!=null)
          column_name=nameSuggestion+"_key"+counter++;
        
        newField.setName(column_name);
        if(newField.getType()== FieldModel.DBTYPE_INTEGER)
          newField.getIntegerFieldType().setAutoincrement(false);
        else if(newField.getType()== FieldModel.DBTYPE_LONG)
          newField.getLongFieldType().setAutoincrement(false);
        key.addElement(newField);
      }
      // feldname = <alias>_<field>_key
      else
      {
  	    Iterator iter = foreignPrimaryKey.getFieldModels().iterator();
  	    while (iter.hasNext())
  	    {
  	      FieldModel field = (FieldModel) iter.next();
  	      FieldModel newField = createMatchingField(field);
  	      if(newField.getType()== FieldModel.DBTYPE_INTEGER)
  	        newField.getIntegerFieldType().setAutoincrement(false);
  	      if(newField.getType()== FieldModel.DBTYPE_LONG)
  	        newField.getLongFieldType().setAutoincrement(false);
  	      key.addElement(newField);
  	    }
      }
      Trace.print("create all fields for the key");
      return key;
    }
    finally
    {
      Trace.stop("TabelModel.createMatchingForeignKey");
    }
  }
  
  public KeyModel createForeignKey()
  {
    CastorKey tableKey = new CastorKey();
    this.castor.addForeignKey(tableKey);
    KeyModel key = new KeyModel(this, tableKey, KeyModel.DBTYPE_FOREIGN);
    addKey(key);
    
    return key;
  }

  public void createUniqueKey()
  {
    CastorKey tableKey = new CastorKey();
    this.castor.addUniqueIndex(tableKey);
    addKey(new KeyModel(this, tableKey, KeyModel.DBTYPE_UNIQUE));
  }

  public void createIndexKey()
  {
    CastorKey tableKey = new CastorKey();
    this.castor.addIndex(tableKey);
    addKey(new KeyModel(this, tableKey, KeyModel.DBTYPE_INDEX));
  }

  /**
   *  
   */
  private void addKey(KeyModel keyModel)
  {
    keys.add(keyModel);
    firePropertyChange(TableModel.PROPERTY_KEY_ADDED, null, keyModel);
  }

  
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 5];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,    "Name", PROPERTYGROUP_DB);
		descriptors[superDescriptors.length+1] = new TextPropertyGroupingDescriptor(ID_PROPERTY_DBNAME,  "DB-Name", PROPERTYGROUP_DB);
    descriptors[superDescriptors.length+2] = new CheckboxPropertyDescriptor(ID_PROPERTY_OMIT_LOCK, "Omit Lock", PROPERTYGROUP_DB);
    descriptors[superDescriptors.length+3] = new CheckboxPropertyDescriptor(ID_PROPERTY_M_TO_N_TABLE, "is Link Table", PROPERTYGROUP_DB);
    descriptors[superDescriptors.length+4] = new CheckboxPropertyDescriptor(ID_PROPERTY_RECORDS_ALWAYS_DELETABLE, "Records always Deletable", PROPERTYGROUP_DB);
		
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		try 
		{
			if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
				setName((String) val);
			else if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_DBNAME))
				setDbName((String) val);
      else if (propName == ID_PROPERTY_OMIT_LOCK)
        setOmitLock(((Boolean) val).booleanValue());
      else if (propName == ID_PROPERTY_RECORDS_ALWAYS_DELETABLE)
        setRecordsAlwaysDeletable(((Boolean) val).booleanValue());
      else if (propName == ID_PROPERTY_M_TO_N_TABLE)
        setMtoNTable(((Boolean) val).booleanValue());
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
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_DBNAME))
			return getDbName();
    if (propName == ID_PROPERTY_OMIT_LOCK)
      return new Boolean(getOmitLock());
    if (propName == ID_PROPERTY_M_TO_N_TABLE)
      return new Boolean(getMtoNTable());
    if (propName == ID_PROPERTY_RECORDS_ALWAYS_DELETABLE)
      return new Boolean(getRecordsAlwaysDeletable());
		else
			return super.getPropertyValue(propName);
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
 
  public CastorTable getCastor()
  {
    return castor;
  }
  
  
  /**
   * 
   */
  public String getError()
  {
    if(getFieldModels().size()==0)
      return "Table definition contains no columns";
    
    // prï¿½fen ob ein Spaltennamen zwei mal vergeben ist
    //
    Set<String> columns = new HashSet<String>();
    Iterator<FieldModel> iter = getFieldModels().iterator();
    while (iter.hasNext())
    {
      FieldModel obj = iter.next();
      if(columns.contains(obj.getName()))
        return "column with name ["+obj.getName()+"] already exists";
      columns.add(obj.getName());
    }

    return null;
  }
  
  /**
   * 
   */
  public String getWarning()
  {
    // physikalischer Tabellennamen ist für einige Datenbanken  zu lang 
    // (z.B. Oracle)
    if(getDbName().length()>30)
      return "The physical database name is to long. Limit is 30 characters.";
    
    // Kein Alias für diese Tabelle. Die Tabelle ist somit nicht via
    // Script erreichbar.
    if(getJacobModel().getTableAliasModels(this).size()==0)
      return "No table alias defined for table ["+getName()+"]";

    // Historyfeld ist definert worden
    //
    if(!StringUtil.saveEquals("",getHistoryField()))
      return null;
    
    // Falls die Tabelle ein Feld per History protokollieren moechte aber
    // kein Historyfeld definiert ist, wird dies als Warning behandelt.
    //
    Iterator<FieldModel> iter = getFieldModels().iterator();
    while (iter.hasNext())
    {
      FieldModel field = iter.next();
      if(field.getHistory()==true)
        return "No history columns defined for table ["+getName()+"] but one column want's to write a history.";
    }
    return null;
  }
  
  public String getInfo()
  {
    return null;
  }

  public boolean getMtoNTable()
  {
    return castor.getNtoMTable();
  }


  public void setMtoNTable(boolean flag)
  {
    boolean save = getMtoNTable();
    if (flag==save)
      return;

    castor.setNtoMTable(flag);
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, new Boolean(save), new Boolean(flag));
  }
  
  public boolean getOmitLock()
  {
    return castor.getOmitLocking();
  }


  public void setOmitLock(boolean flag)
  {
    boolean save = getOmitLock();
    if (flag==save)
      return;

    castor.setOmitLocking(flag);
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, new Boolean(save), new Boolean(flag));
  }
  
  public boolean getRecordsAlwaysDeletable()
  {
    return castor.getRecordsAlwaysDeletable();
  }


  public void setRecordsAlwaysDeletable(boolean flag)
  {
    boolean save = getRecordsAlwaysDeletable();
    if (flag==save)
      return;

    castor.setRecordsAlwaysDeletable(flag);
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, new Boolean(save), new Boolean(flag));
  }
  
  /**
   * 
   * @return true if at least one table alias is defined for this table
   */
  public boolean isInUse()
  {
    if(getJacobModel().getTableAliasModels(this).size()>0)
      return true;
    
    return false;
  }

  @Override
  public ObjectModel getParent()
  {
    return getDatasourceModel();
  }


}
