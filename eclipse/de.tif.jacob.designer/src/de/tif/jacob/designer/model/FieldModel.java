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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.fieldtypes.DecimalFieldType;
import de.tif.jacob.core.definition.impl.jad.castor.BinaryField;
import de.tif.jacob.core.definition.impl.jad.castor.BooleanField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
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
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class FieldModel extends ObjectWithPropertyModel
{
  public final static String DBTYPE_TEXT      = "TEXT";
  public final static String DBTYPE_DOCUMENT  = "DOCUMENT";
  public final static String DBTYPE_INTEGER   = "INTEGER";
  public final static String DBTYPE_LONG      = "LONG";
  public final static String DBTYPE_FLOAT     = "FLOAT";
  public final static String DBTYPE_DOUBLE    = "DOUBLE";
  public final static String DBTYPE_DECIMAL   = "DECIMAL";
  public final static String DBTYPE_DATE      = "DATE";
  public final static String DBTYPE_TIME      = "TIME";
  public final static String DBTYPE_TIMESTAMP = "TIMESTAMP";
  public final static String DBTYPE_LONGTEXT  = "LONGTEXT";
  public final static String DBTYPE_BINARY    = "BINARY";
  public final static String DBTYPE_ENUM      = "ENUM";
  public final static String DBTYPE_BOOLEAN   = "BOOLEAN";
  
  public final static String[] DBTYPES = {
    DBTYPE_TEXT, DBTYPE_INTEGER, DBTYPE_LONG, 
    DBTYPE_FLOAT, DBTYPE_DOUBLE, DBTYPE_DECIMAL, DBTYPE_ENUM,
    DBTYPE_DATE, DBTYPE_TIME, DBTYPE_TIMESTAMP, 
    DBTYPE_LONGTEXT, DBTYPE_BINARY, DBTYPE_BOOLEAN, DBTYPE_DOCUMENT };
  
  private final CastorTableField castor;
  private final TableModel table;
  
  private FieldModelType typeModel;
	private IPropertyDescriptor[] descriptors;
  
  public FieldModel(JacobModel jacob, TableModel table)
  {
    super(jacob);
    this.table = table;
    this.castor =  new CastorTableField();
    
    this.castor.setCastorTableFieldChoice( new CastorTableFieldChoice());
    this.castor.setRequired(false);
    this.castor.setHistory(false);
    this.castor.setReadonly(false);
 
    TextField typeField = new TextField();
    typeField.setMaxLength(40);
    typeField.setSearchMode(TextFieldSearchModeType.UNBOUND);
    typeField.setCaseSensitive(false);
    this.castor.getCastorTableFieldChoice().setText(typeField);
  }
  
  protected FieldModel(JacobModel jacob, TableModel table, CastorTableField field)
  {
    super(jacob);
    this.table = table;
    this.castor = field;
  }
  
  public boolean getHistory()
  {
    return castor.getHistory();
  }
  
  public String getLabel()
  {
    return StringUtil.toSaveString(castor.getLabel());
  }
  
  public String getName()
  {
    return StringUtil.toSaveString(castor.getName());
  }
  
  /**
   * 
   * @return label like "name [VARCHAR(20)]"
   */
  public String getExtendedDescriptionLabel()
  {
	  if(getType()== FieldModel.DBTYPE_TEXT)
	    return getName()+" "+getType()+"<"+getLengthAsString()+">";
    return getName()+" "+getType();
  }
  
  public String getDbName()
  {
    return StringUtil.toSaveString(castor.getDbName());
  }
  
  public boolean getReadonly()
  {
    return castor.getReadonly();
  }
  
  public boolean getRequired()
  {
    return castor.getRequired();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.ObjectModel#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   */
  public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
  {
    super.firePropertyChange(propertyName, oldValue, newValue);
    
    // additionally inform parent that any child property has changed
    if(propertyName != PROPERTY_FIELD_DELETED && this.table!=null)
      this.table.firePropertyChange(PROPERTY_FIELD_CHANGED, oldValue, newValue);
  }
  
  public void setHistory(boolean history)
  {
    boolean save = getHistory();
    if (history != save)
    {
      castor.setHistory(history);
      firePropertyChange(PROPERTY_HISTORY_CHANGED, new Boolean(save),new Boolean(history));
      getTableModel().firePropertyChange(PROPERTY_FIELD_CHANGED, null, this);
    }
  }
  
  public void setLabel(String label)
  {
    String save = getLabel();
    if (StringUtil.saveEquals(label,save))
      return;
    
    castor.setLabel(label);
    firePropertyChange(PROPERTY_LABEL_CHANGED, save, label);
  }
  
  public void setName(String name)
  {
    String saveLabel = getLabel();
    String saveName = getName();
    
    if (StringUtil.saveEquals(name, saveName))
      return;

    // Falls der Name bereits vergeben ist, dann wird der NAme wieder auf
    // den Wert vorher zurückgesetzt.
    // "firePropertyChange" muß aber trotzdem gefeuert werden, damit
    // der Anwender ein feedback bekommt, dass sich der Wert nicht geändert
    // hat.
    if(this.getTableModel().getFieldModel(name)!=null)
    {
      firePropertyChange(PROPERTY_NAME_CHANGED, "", name);
      return;
    }
    
    castor.setName(name);    
    getJacobModel().renameFieldReference(this, saveName,name);
    
    
    // keep dbname synchron with name
    if (StringUtil.saveEquals(getDbName(), saveName))
      castor.setDbName(name);    
    
    // keep label synchron with name if the user doesn't changed them
    // manually
    if(getJacobModel().useI18N())
    {
	    String calculatedLabel = "%"+getTableModel().getName().toUpperCase()+getJacobModel().getSeparator()+saveName.toUpperCase();
	    if (saveLabel.length()==0 || calculatedLabel.equals(saveLabel))
	      castor.setLabel(getDefaultI18NLabel());
    }
    else
    {
	    String calculatedLabel = StringUtils.capitalise(saveName);
	    if (saveLabel.length()==0 || calculatedLabel.equals(saveLabel))
	      castor.setLabel(StringUtils.capitalise(name));
    }

    firePropertyChange(PROPERTY_NAME_CHANGED, saveName, name);
  }
  
  /**
   * Required for Velocity template.
   * Aus unerfindlichen Gründen wird sonst die Methode "getDescription" von Velocity
   * nicht gefunden. ?!
   */
  public String getDescription()
  {
    return super.getDescription();
  }

  protected void resetI18N()
  {
    String oldLabel = castor.getLabel();
    castor.setLabel(getDefaultI18NLabel());
    
    // Es wird wenn möglich das alte Label wiederverwendet...
    //
    if(oldLabel!=null && oldLabel.length()>0 && !oldLabel.startsWith("%"))
      getJacobModel().addI18N(castor.getLabel().substring(1),oldLabel,false);
    else
      getJacobModel().addI18N(castor.getLabel().substring(1),getName(),false);
  }
  
  private String getDefaultI18NLabel()
  {
    String sep=getJacobModel().getSeparator();
    return "%"+getTableModel().getName().toUpperCase()+sep+getName().toUpperCase();
  }
  
  public void setDbName(String name)
  {
    String save = getDbName();
    if (StringUtil.saveEquals(name, save))
      return;
    
    castor.setDbName(name);    
    firePropertyChange(PROPERTY_DBNAME_CHANGED, save, name);
  }
  
  public void setReadonly(boolean readonly)
  {
    boolean save = getReadonly();
    if (readonly != save)
    {
      castor.setReadonly(readonly);
      firePropertyChange(PROPERTY_READONLY_CHANGED, new Boolean(save),new Boolean(readonly));
    }
  }
  
  public void setRequired(boolean required)
  {
    boolean save = getRequired();
    if (required != save)
    {
      castor.setRequired(required);
      firePropertyChange(PROPERTY_REQUIRED_CHANGED, new Boolean(save),new Boolean(required));
      // es muss jetzt geprï¿½ft werden ob das Feld Teil eines keys ist und ob dieser Key teil einer Relation ist.
      // Falls dies der Fall ist muss ja eventuell im RelationsetEditor die Relation neu gezeichnet werden
      // ( dekoration )
      //
      Iterator iter = getTableModel().getKeyModels().iterator();
      while (iter.hasNext())
      {
        KeyModel key = (KeyModel) iter.next();
        if(key.contains(this))
        {
          // jetzt alle Relationen finden in den dieser key vorhanden ist
          //
          Iterator relIter = getJacobModel().getRelationModels().iterator();
          while (relIter.hasNext())
          {
            RelationModel relation = (RelationModel) relIter.next();
            if(relation.getToKey()== key)
              relation.firePropertyChange(PROPERTY_KEY_CHANGED,null, key);
          }
        }
      }
    }
  }
  

  public String getType()
  {
    if (castor.getCastorTableFieldChoice() != null)
    {
      if (castor.getCastorTableFieldChoice().getBinary() != null)
        return DBTYPE_BINARY;
      if (castor.getCastorTableFieldChoice().getBoolean() != null)
        return DBTYPE_BOOLEAN;
      if (castor.getCastorTableFieldChoice().getDate() != null)
        return DBTYPE_DATE;
      if (castor.getCastorTableFieldChoice().getDecimal() != null)
        return DBTYPE_DECIMAL;
      if (castor.getCastorTableFieldChoice().getDouble() != null)
        return DBTYPE_DOUBLE;
      if (castor.getCastorTableFieldChoice().getEnumeration() != null)
        return DBTYPE_ENUM;
      if (castor.getCastorTableFieldChoice().getFloat() != null)
        return DBTYPE_FLOAT;
      if (castor.getCastorTableFieldChoice().getInteger() != null)
        return DBTYPE_INTEGER;
      if (castor.getCastorTableFieldChoice().getLong() != null)
        return DBTYPE_LONG;
      if (castor.getCastorTableFieldChoice().getLongText() != null)
        return DBTYPE_LONGTEXT;
      if (castor.getCastorTableFieldChoice().getText() != null)
        return DBTYPE_TEXT;
      if (castor.getCastorTableFieldChoice().getDocument() != null)
        return DBTYPE_DOCUMENT;
      if (castor.getCastorTableFieldChoice().getTime() != null)
        return DBTYPE_TIME;
      if (castor.getCastorTableFieldChoice().getTimestamp() != null)
        return DBTYPE_TIMESTAMP;
    }
    return null;
  }
  
  public void setType(String type)
  {
    String save = getType();
    
    if (StringUtil.saveEquals(type,save))
      return;
    
    CastorTableFieldChoice choice = new CastorTableFieldChoice();
    
    if(DBTYPE_BINARY.equals(type))
      choice.setBinary(new BinaryField());
    else if(DBTYPE_DATE.equals(type))
      choice.setDate(new DateField());
    else if(DBTYPE_DECIMAL.equals(type))
      choice.setDecimal(new DecimalField());
    else if(DBTYPE_DOUBLE.equals(type))
      choice.setDouble(new DoubleField());
    else if(DBTYPE_BOOLEAN.equals(type))
      choice.setBoolean(new BooleanField());
    else if(DBTYPE_ENUM.equals(type))
    {
      EnumerationField typeField = new EnumerationField();
      choice.setEnumeration(typeField);
    }
    else if(DBTYPE_FLOAT.equals(type))
      choice.setFloat(new FloatField());
    else if(DBTYPE_INTEGER.equals(type))
    {
      choice.setInteger(new IntegerField());
      choice.getInteger().setAutoincrement(false);
    }
    else if(DBTYPE_LONG.equals(type))
    {
      choice.setLong(new LongField());
      choice.getLong().setAutoincrement(false);
    }
    else if(DBTYPE_LONGTEXT.equals(type))
    {
      choice.setLongText(new LongTextField());
      choice.getLongText().setEditMode(LongTextEditModeType.FULLEDIT);
    }
    else if(DBTYPE_DOCUMENT.equals(type))
      choice.setDocument(new DocumentField());
    else if(DBTYPE_TEXT.equals(type))
    {
      TextField typeField = new TextField();
      typeField.setMaxLength(64);
      typeField.setSearchMode(TextFieldSearchModeType.UNBOUND);
      typeField.setCaseSensitive(false);
      choice.setText(typeField);
    }
    else if(DBTYPE_TIME.equals(type))
      choice.setTime(new TimeField());
    else if(DBTYPE_TIMESTAMP.equals(type))
    {
      TimestampField typeField = new TimestampField();
      typeField.setResolution(TimestampFieldResolutionType.SECBASE);
      choice.setTimestamp(typeField);
    }
    else
      throw new IllegalArgumentException(type);
    
    castor.setCastorTableFieldChoice(choice);
    this.typeModel = null;
    
    firePropertyChange(PROPERTY_FIELD_TYPE_CHANGED, save, type);
    
    // additionally inform parent thatthe field has changed
    table.firePropertyChange(PROPERTY_FIELD_CHANGED, this, this);
  }
  
  /**
   * returns true if the fields matches with all attributes.
   * @return
   */
  public boolean match(FieldModel other)
  {
    if(other.getType()!=getType())
      return false;
    // TODO: VIEL(!!!) mehr Prüfungen einarbeiten
    
    return true;
  }
  
  public FieldModelTypeLongtext getLongtextFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeLongtext(this, this.castor.getCastorTableFieldChoice().getLongText());
    return (FieldModelTypeLongtext) this.typeModel;
  }
  
  public FieldModelTypeText getTextFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeText(this, this.castor.getCastorTableFieldChoice().getText());
    return (FieldModelTypeText) this.typeModel;
  }
  
  public FieldModelTypeTimestamp getTimestampFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeTimestamp(this, this.castor.getCastorTableFieldChoice().getTimestamp());
    return (FieldModelTypeTimestamp) this.typeModel;
  }
  
  public FieldModelTypeEnum getEnumFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeEnum(getJacobModel(), this, this.castor.getCastorTableFieldChoice().getEnumeration());
    return (FieldModelTypeEnum) this.typeModel;
  }
  
  public FieldModelTypeBoolean getBooleanFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeBoolean(getJacobModel(), this, this.castor.getCastorTableFieldChoice().getBoolean());
    return (FieldModelTypeBoolean) this.typeModel;
  }

  public FieldModelTypeLong getLongFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeLong(this, this.castor.getCastorTableFieldChoice().getLong());
    return (FieldModelTypeLong) this.typeModel;
  }
 
  public FieldModelTypeInteger getIntegerFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeInteger(this, this.castor.getCastorTableFieldChoice().getInteger());
    return (FieldModelTypeInteger) this.typeModel;
  }
 
  public FieldModelTypeFloat getFloatFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeFloat(this, this.castor.getCastorTableFieldChoice().getFloat());
    return (FieldModelTypeFloat) this.typeModel;
  }
 
  public FieldModelTypeDouble getDoubleFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeDouble(this, this.castor.getCastorTableFieldChoice().getDouble());
    return (FieldModelTypeDouble) this.typeModel;
  }
 
  public FieldModelTypeDecimal getDecimalFieldType()
  {
    if (this.typeModel == null)
      this.typeModel = new FieldModelTypeDecimal(this, this.castor.getCastorTableFieldChoice().getDecimal());
    return (FieldModelTypeDecimal) this.typeModel;
  }
 
  /**
   * @return
   */
  public String getLengthAsString()
  {
    String length = "";
    if (DBTYPE_TEXT.equals(getType()))
    {
      length = Integer.toString(this.castor.getCastorTableFieldChoice().getText().getMaxLength());
      if(getTextFieldType().getFixeLength())
        length = "["+length+"]";
    }
    return length;
  }

	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		if (descriptors == null)
		{
			IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
			descriptors = new IPropertyDescriptor[superDescriptors.length + 3];
			for (int i = 0; i < superDescriptors.length; i++)
				descriptors[i] = superDescriptors[i];
			descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,  "Name", PROPERTYGROUP_DB);
			descriptors[superDescriptors.length+1] = new TextPropertyGroupingDescriptor(ID_PROPERTY_DBNAME,"DB-Name", PROPERTYGROUP_DB);
			descriptors[superDescriptors.length+2] = new TextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
		}
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		try 
		{
			if (propName ==ID_PROPERTY_NAME)
				setName((String) val);
			else if (propName ==ID_PROPERTY_DBNAME)
				setDbName((String) val);
			else if (propName ==ID_PROPERTY_LABEL)
				setLabel((String) val);
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
		if (propName ==ID_PROPERTY_NAME)
			return getName();
		if (propName ==ID_PROPERTY_DBNAME)
			return getDbName();
		if (propName ==ID_PROPERTY_LABEL)
			return getLabel();
		return super.getPropertyValue(propName);
	}
	

  public TableModel getTableModel()
  {
    return table;
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

  public CastorTableField getCastor()
  {
    return castor;
  }

  public String getError()
  {
    // check whether autoincrement is set for key fields
    // 
    boolean canBePartOfForeignKey = true;
    if(getType()==FieldModel.DBTYPE_INTEGER && getIntegerFieldType().getAutoincrement()==true)
      canBePartOfForeignKey=false;
    else if(getType()==FieldModel.DBTYPE_LONG && getLongFieldType().getAutoincrement()==true)
      canBePartOfForeignKey=false;
    
    if(canBePartOfForeignKey==false)
    {
      Iterator keyIter = table.getKeyModels().iterator();
      while (keyIter.hasNext())
      {
        KeyModel key = (KeyModel) keyIter.next();
        if(key.getType()==KeyModel.DBTYPE_FOREIGN && key.contains(this))
            return "Column ["+getName()+"] is part of foreign key. Disable [autoincrement] for this column";
      }
    }
    
    // Falls das Feld Mitglied des PrimaryKey ist, dann MUSS das Feld auf 'required' gesetzt
    // sein
    //
    KeyModel primaryKey = getTableModel().getPrimaryKeyModel();
    if(primaryKey!=null && primaryKey.contains(this) && this.getRequired()==false)
      return "Field ["+getName()+"] must be set to required because it is a member of the primary key";
    
    
    if (getType() == FieldModel.DBTYPE_INTEGER)
    {
      FieldModelTypeInteger myType  = getIntegerFieldType();
      
      // check whether min value is always less or equal max value
      if ( myType.hasMinValue() && myType.hasMaxValue() && myType.getMinValue() > myType.getMaxValue())
        return "Column [" + getName() + "] has a min value greater than max value";
      
      if ( myType.hasMinValue() && myType.hasDefaultValue() && myType.getMinValue() > myType.getDefaultValue())
        return "Column [" + getName() + "] has a min value greater than default value";
      
      if ( myType.hasMaxValue() && myType.hasDefaultValue() && myType.getMaxValue() < myType.getDefaultValue())
        return "Column [" + getName() + "] has a max value less than default value";
    }
    else if (getType() == FieldModel.DBTYPE_LONG)
    {
      FieldModelTypeLong myType  = getLongFieldType();
      
      // check whether min value is always less or equal max value
      if ( myType.hasMinValue() && myType.hasMaxValue() && myType.getMinValue() > myType.getMaxValue())
        return "Column [" + getName() + "] has a min value greater than max value";
      
      if ( myType.hasMinValue() && myType.hasDefaultValue() && myType.getMinValue() > myType.getDefaultValue())
        return "Column [" + getName() + "] has a min value greater than default value";
      
      if ( myType.hasMaxValue() && myType.hasDefaultValue() && myType.getMaxValue() < myType.getDefaultValue())
        return "Column [" + getName() + "] has a max value less than default value";
    }
    else if (getType() == FieldModel.DBTYPE_FLOAT)
    {
      FieldModelTypeFloat myType  = getFloatFieldType();
      
      // check whether min value is always less or equal max value
      if ( myType.hasMinValue() && myType.hasMaxValue() && myType.getMinValue() > myType.getMaxValue())
        return "Column [" + getName() + "] has a min value greater than max value";
      
      if ( myType.hasMinValue() && myType.hasDefaultValue() && myType.getMinValue() > myType.getDefaultValue())
        return "Column [" + getName() + "] has a min value greater than default value";
      
      if ( myType.hasMaxValue() && myType.hasDefaultValue() && myType.getMaxValue() < myType.getDefaultValue())
        return "Column [" + getName() + "] has a max value less than default value";
    }
    else if (getType() == FieldModel.DBTYPE_DOUBLE)
    {
      FieldModelTypeDouble myType  = getDoubleFieldType();
      
      // check whether min value is always less or equal max value
      if ( myType.hasMinValue() && myType.hasMaxValue() && myType.getMinValue() > myType.getMaxValue())
        return "Column [" + getName() + "] has a min value greater than max value";
      
      if ( myType.hasMinValue() && myType.hasDefaultValue() && myType.getMinValue() > myType.getDefaultValue())
        return "Column [" + getName() + "] has a min value greater than default value";
      
      if ( myType.hasMaxValue() && myType.hasDefaultValue() && myType.getMaxValue() < myType.getDefaultValue())
        return "Column [" + getName() + "] has a max value less than default value";
    }
    else if (getType() == FieldModel.DBTYPE_DECIMAL)
    {
      FieldModelTypeDecimal myType  = getDecimalFieldType();
      
      // check whether min value is always less or equal max value
      if (myType.getMinValue() != null && myType.getMaxValue() != null && myType.getMinValue().compareTo(myType.getMaxValue()) > 0)
        return "Column [" + getName() + "] has a min value greater than max value";

      if (myType.getMinValue() != null && myType.getDefaultValue() != null && myType.getMinValue().compareTo(myType.getDefaultValue()) > 0)
        return "Column [" + getName() + "] has a min value greater than default value";

      if (myType.getMaxValue() != null && myType.getDefaultValue() != null && myType.getMaxValue().compareTo(myType.getDefaultValue()) < 0)
        return "Column [" + getName() + "] has a max value less than default value";
    }
    else if (getType() == FieldModel.DBTYPE_ENUM)
    {
      FieldModelTypeEnum myType  = getEnumFieldType();
      String[] values = myType.getEnumValues();
      Set unique = new HashSet();
      for (int i = 0; i < values.length; i++)
      {
        unique.add(values[i]);
        // Ein leer Eintrag als enum wert ist nicht erlaubt. Das Feld kann einfach als optional markiert werden
        //
        if(values[i]==null || values[i].length()==0)
          return "Empty enum value is not allowed.";
      }
      if(unique.size()!=values.length)
        return "Remove duplicated enum values in the field definition.";
    }
    
    
    return null;
  }
  
  public String getWarning()
  {
    if (getType() == FieldModel.DBTYPE_DECIMAL)
    {
      FieldModelTypeDecimal myType = getDecimalFieldType();

      // check whether min value is always less or equal max value
      if (myType.getScale() < DecimalFieldType.MIN_SCALE || myType.getScale() > DecimalFieldType.MAX_SCALE)
        return "Column [" + getName() + "] has a scale value which is not between " + DecimalFieldType.MIN_SCALE + ".." + DecimalFieldType.MAX_SCALE;
    }

    String caption = getLabel();

    if(StringUtil.saveEquals("",caption))
      return "No default Label defined for column ["+getName()+"]";

    if (caption != null && caption.startsWith("%") && getJacobModel().hasI18NKey(caption.substring(1)) == false)
      return "No localization entry for [" + caption.substring(1) + "] found";
    
    return null;
  }
  
  public String getInfo()
  {
    return null;
  }
  
  public boolean isInUse()
  {
    // prüfen ob das Feld in einer Relation verwendet wird
    //
    for (RelationModel relation : getJacobModel().getRelationModels())
    {
      for (FieldModel field : relation.getToKey().getFieldModels())
      {
        if(field==this)
          return true;
      }
    }
    
    // prüfen ob das Element in einem Key verwendet wrid
    //
    for (KeyModel key : getTableModel().getKeyModels())
    {
      if(key.contains(this))
        return true;
    }
    return false;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
  
  @Override
  public ObjectModel getParent()
  {
    return getTableModel();
  }
}
