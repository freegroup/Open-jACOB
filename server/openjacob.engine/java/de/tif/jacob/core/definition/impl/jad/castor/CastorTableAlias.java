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
 * Class CastorTableAlias.
 * 
 * @version $Revision$ $Date$
 */
public class CastorTableAlias implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _table
     */
    private java.lang.String _table;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _condition
     */
    private java.lang.String _condition;

    /**
     * Field _sortFieldList
     */
    private java.util.Vector _sortFieldList;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorTableAlias() {
        super();
        _sortFieldList = new Vector();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias()


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
     * Method addSortField
     * 
     * @param vSortField
     */
    public void addSortField(de.tif.jacob.core.definition.impl.jad.castor.SortField vSortField)
        throws java.lang.IndexOutOfBoundsException
    {
        _sortFieldList.addElement(vSortField);
    } //-- void addSortField(de.tif.jacob.core.definition.impl.jad.castor.SortField) 

    /**
     * Method addSortField
     * 
     * @param index
     * @param vSortField
     */
    public void addSortField(int index, de.tif.jacob.core.definition.impl.jad.castor.SortField vSortField)
        throws java.lang.IndexOutOfBoundsException
    {
        _sortFieldList.insertElementAt(vSortField, index);
    } //-- void addSortField(int, de.tif.jacob.core.definition.impl.jad.castor.SortField) 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Method enumerateSortField
     */
    public java.util.Enumeration enumerateSortField()
    {
        return _sortFieldList.elements();
    } //-- java.util.Enumeration enumerateSortField() 

    /**
     * Returns the value of field 'condition'.
     * 
     * @return the value of field 'condition'.
     */
    public java.lang.String getCondition()
    {
        return this._condition;
    } //-- java.lang.String getCondition() 

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
     * Method getSortField
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.SortField getSortField(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _sortFieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.SortField) _sortFieldList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.SortField getSortField(int) 

    /**
     * Method getSortField
     */
    public de.tif.jacob.core.definition.impl.jad.castor.SortField[] getSortField()
    {
        int size = _sortFieldList.size();
        de.tif.jacob.core.definition.impl.jad.castor.SortField[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.SortField[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.SortField) _sortFieldList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.SortField[] getSortField() 

    /**
     * Method getSortFieldCount
     */
    public int getSortFieldCount()
    {
        return _sortFieldList.size();
    } //-- int getSortFieldCount() 

    /**
     * Returns the value of field 'table'.
     * 
     * @return the value of field 'table'.
     */
    public java.lang.String getTable()
    {
        return this._table;
    } //-- java.lang.String getTable() 

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
     * Method removeAllSortField
     */
    public void removeAllSortField()
    {
        _sortFieldList.removeAllElements();
    } //-- void removeAllSortField() 

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
     * Method removeSortField
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.SortField removeSortField(int index)
    {
        java.lang.Object obj = _sortFieldList.elementAt(index);
        _sortFieldList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.SortField) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.SortField removeSortField(int) 

    /**
     * Sets the value of field 'condition'.
     * 
     * @param condition the value of field 'condition'.
     */
    public void setCondition(java.lang.String condition)
    {
        this._condition = condition;
    } //-- void setCondition(java.lang.String) 

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
     * Method setSortField
     * 
     * @param index
     * @param vSortField
     */
    public void setSortField(int index, de.tif.jacob.core.definition.impl.jad.castor.SortField vSortField)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _sortFieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _sortFieldList.setElementAt(vSortField, index);
    } //-- void setSortField(int, de.tif.jacob.core.definition.impl.jad.castor.SortField) 

    /**
     * Method setSortField
     * 
     * @param sortFieldArray
     */
    public void setSortField(de.tif.jacob.core.definition.impl.jad.castor.SortField[] sortFieldArray)
    {
        //-- copy array
        _sortFieldList.removeAllElements();
        for (int i = 0; i < sortFieldArray.length; i++) {
            _sortFieldList.addElement(sortFieldArray[i]);
        }
    } //-- void setSortField(de.tif.jacob.core.definition.impl.jad.castor.SortField) 

    /**
     * Sets the value of field 'table'.
     * 
     * @param table the value of field 'table'.
     */
    public void setTable(java.lang.String table)
    {
        this._table = table;
    } //-- void setTable(java.lang.String) 

    /**
     * Method unmarshalCastorTableAlias
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorTableAlias(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias.class, reader);
    } //-- java.lang.Object unmarshalCastorTableAlias(java.io.Reader) 

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
