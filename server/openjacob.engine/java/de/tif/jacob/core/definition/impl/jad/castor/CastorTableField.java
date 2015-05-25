/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.core.definition.impl.jad.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CastorTableField.
 * 
 * @version $Revision$ $Date$
 */
public class CastorTableField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _dbName
     */
    private java.lang.String _dbName;

    /**
     * Field _required
     */
    private boolean _required;

    /**
     * keeps track of state for field: _required
     */
    private boolean _has_required;

    /**
     * Field _readonly
     */
    private boolean _readonly;

    /**
     * keeps track of state for field: _readonly
     */
    private boolean _has_readonly;

    /**
     * Field _customFieldType
     */
    private java.lang.String _customFieldType;

    /**
     * Field _history
     */
    private boolean _history = false;

    /**
     * keeps track of state for field: _history
     */
    private boolean _has_history;

    /**
     * Field _label
     */
    private java.lang.String _label;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _castorTableFieldChoice
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice _castorTableFieldChoice;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorTableField() {
        super();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addProperty
     * 
     * @param vProperty
     */
    public void addProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.addElement(vProperty);
    } //-- void addProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method addProperty
     * 
     * @param index
     * @param vProperty
     */
    public void addProperty(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.insertElementAt(vProperty, index);
    } //-- void addProperty(int, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method deleteHistory
     */
    public void deleteHistory()
    {
        this._has_history= false;
    } //-- void deleteHistory() 

    /**
     * Method deleteReadonly
     */
    public void deleteReadonly()
    {
        this._has_readonly= false;
    } //-- void deleteReadonly() 

    /**
     * Method deleteRequired
     */
    public void deleteRequired()
    {
        this._has_required= false;
    } //-- void deleteRequired() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Returns the value of field 'castorTableFieldChoice'.
     * 
     * @return the value of field 'castorTableFieldChoice'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice getCastorTableFieldChoice()
    {
        return this._castorTableFieldChoice;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice getCastorTableFieldChoice() 

    /**
     * Returns the value of field 'customFieldType'.
     * 
     * @return the value of field 'customFieldType'.
     */
    public java.lang.String getCustomFieldType()
    {
        return this._customFieldType;
    } //-- java.lang.String getCustomFieldType() 

    /**
     * Returns the value of field 'dbName'.
     * 
     * @return the value of field 'dbName'.
     */
    public java.lang.String getDbName()
    {
        return this._dbName;
    } //-- java.lang.String getDbName() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'history'.
     * 
     * @return the value of field 'history'.
     */
    public boolean getHistory()
    {
        return this._history;
    } //-- boolean getHistory() 

    /**
     * Returns the value of field 'label'.
     * 
     * @return the value of field 'label'.
     */
    public java.lang.String getLabel()
    {
        return this._label;
    } //-- java.lang.String getLabel() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getProperty
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty getProperty(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) _propertyList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty getProperty(int) 

    /**
     * Method getProperty
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] getProperty()
    {
        int size = _propertyList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) _propertyList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] getProperty() 

    /**
     * Method getPropertyCount
     */
    public int getPropertyCount()
    {
        return _propertyList.size();
    } //-- int getPropertyCount() 

    /**
     * Returns the value of field 'readonly'.
     * 
     * @return the value of field 'readonly'.
     */
    public boolean getReadonly()
    {
        return this._readonly;
    } //-- boolean getReadonly() 

    /**
     * Returns the value of field 'required'.
     * 
     * @return the value of field 'required'.
     */
    public boolean getRequired()
    {
        return this._required;
    } //-- boolean getRequired() 

    /**
     * Method hasHistory
     */
    public boolean hasHistory()
    {
        return this._has_history;
    } //-- boolean hasHistory() 

    /**
     * Method hasReadonly
     */
    public boolean hasReadonly()
    {
        return this._has_readonly;
    } //-- boolean hasReadonly() 

    /**
     * Method hasRequired
     */
    public boolean hasRequired()
    {
        return this._has_required;
    } //-- boolean hasRequired() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeProperty
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty removeProperty(int index)
    {
        java.lang.Object obj = _propertyList.elementAt(index);
        _propertyList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty removeProperty(int) 

    /**
     * Sets the value of field 'castorTableFieldChoice'.
     * 
     * @param castorTableFieldChoice the value of field
     * 'castorTableFieldChoice'.
     */
    public void setCastorTableFieldChoice(de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice castorTableFieldChoice)
    {
        this._castorTableFieldChoice = castorTableFieldChoice;
    } //-- void setCastorTableFieldChoice(de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice) 

    /**
     * Sets the value of field 'customFieldType'.
     * 
     * @param customFieldType the value of field 'customFieldType'.
     */
    public void setCustomFieldType(java.lang.String customFieldType)
    {
        this._customFieldType = customFieldType;
    } //-- void setCustomFieldType(java.lang.String) 

    /**
     * Sets the value of field 'dbName'.
     * 
     * @param dbName the value of field 'dbName'.
     */
    public void setDbName(java.lang.String dbName)
    {
        this._dbName = dbName;
    } //-- void setDbName(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'history'.
     * 
     * @param history the value of field 'history'.
     */
    public void setHistory(boolean history)
    {
        this._history = history;
        this._has_history = true;
    } //-- void setHistory(boolean) 

    /**
     * Sets the value of field 'label'.
     * 
     * @param label the value of field 'label'.
     */
    public void setLabel(java.lang.String label)
    {
        this._label = label;
    } //-- void setLabel(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method setProperty
     * 
     * @param index
     * @param vProperty
     */
    public void setProperty(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propertyList.setElementAt(vProperty, index);
    } //-- void setProperty(int, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method setProperty
     * 
     * @param propertyArray
     */
    public void setProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] propertyArray)
    {
        //-- copy array
        _propertyList.removeAllElements();
        for (int i = 0; i < propertyArray.length; i++) {
            _propertyList.addElement(propertyArray[i]);
        }
    } //-- void setProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Sets the value of field 'readonly'.
     * 
     * @param readonly the value of field 'readonly'.
     */
    public void setReadonly(boolean readonly)
    {
        this._readonly = readonly;
        this._has_readonly = true;
    } //-- void setReadonly(boolean) 

    /**
     * Sets the value of field 'required'.
     * 
     * @param required the value of field 'required'.
     */
    public void setRequired(boolean required)
    {
        this._required = required;
        this._has_required = true;
    } //-- void setRequired(boolean) 

    /**
     * Method unmarshalCastorTableField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorTableField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTableField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorTableField.class, reader);
    } //-- java.lang.Object unmarshalCastorTableField(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
