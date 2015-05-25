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
 * Class CastorTable.
 * 
 * @version $Revision$ $Date$
 */
public class CastorTable implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _datasource
     */
    private java.lang.String _datasource;

    /**
     * Field _dbName
     */
    private java.lang.String _dbName;

    /**
     * Field _representativeField
     */
    private java.lang.String _representativeField;

    /**
     * Field _historyField
     */
    private java.lang.String _historyField;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _omitLocking
     */
    private boolean _omitLocking = false;

    /**
     * keeps track of state for field: _omitLocking
     */
    private boolean _has_omitLocking;

    /**
     * Field _ntoMTable
     */
    private boolean _ntoMTable = false;

    /**
     * keeps track of state for field: _ntoMTable
     */
    private boolean _has_ntoMTable;

    /**
     * Field _recordsAlwaysDeletable
     */
    private boolean _recordsAlwaysDeletable = false;

    /**
     * keeps track of state for field: _recordsAlwaysDeletable
     */
    private boolean _has_recordsAlwaysDeletable;

    /**
     * Field _fieldList
     */
    private java.util.Vector _fieldList;

    /**
     * TODO: Prüfen ob dieses Element nicht Pflicht sein kann!
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorKey _primaryKey;

    /**
     * Field _foreignKeyList
     */
    private java.util.Vector _foreignKeyList;

    /**
     * Field _uniqueIndexList
     */
    private java.util.Vector _uniqueIndexList;

    /**
     * Field _indexList
     */
    private java.util.Vector _indexList;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorTable() {
        super();
        _fieldList = new Vector();
        _foreignKeyList = new Vector();
        _uniqueIndexList = new Vector();
        _indexList = new Vector();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTable()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addField
     * 
     * @param vField
     */
    public void addField(de.tif.jacob.core.definition.impl.jad.castor.CastorTableField vField)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldList.addElement(vField);
    } //-- void addField(de.tif.jacob.core.definition.impl.jad.castor.CastorTableField) 

    /**
     * Method addField
     * 
     * @param index
     * @param vField
     */
    public void addField(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorTableField vField)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldList.insertElementAt(vField, index);
    } //-- void addField(int, de.tif.jacob.core.definition.impl.jad.castor.CastorTableField) 

    /**
     * Method addForeignKey
     * 
     * @param vForeignKey
     */
    public void addForeignKey(de.tif.jacob.core.definition.impl.jad.castor.CastorKey vForeignKey)
        throws java.lang.IndexOutOfBoundsException
    {
        _foreignKeyList.addElement(vForeignKey);
    } //-- void addForeignKey(de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method addForeignKey
     * 
     * @param index
     * @param vForeignKey
     */
    public void addForeignKey(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorKey vForeignKey)
        throws java.lang.IndexOutOfBoundsException
    {
        _foreignKeyList.insertElementAt(vForeignKey, index);
    } //-- void addForeignKey(int, de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method addIndex
     * 
     * @param vIndex
     */
    public void addIndex(de.tif.jacob.core.definition.impl.jad.castor.CastorKey vIndex)
        throws java.lang.IndexOutOfBoundsException
    {
        _indexList.addElement(vIndex);
    } //-- void addIndex(de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method addIndex
     * 
     * @param index
     * @param vIndex
     */
    public void addIndex(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorKey vIndex)
        throws java.lang.IndexOutOfBoundsException
    {
        _indexList.insertElementAt(vIndex, index);
    } //-- void addIndex(int, de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

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
     * Method addUniqueIndex
     * 
     * @param vUniqueIndex
     */
    public void addUniqueIndex(de.tif.jacob.core.definition.impl.jad.castor.CastorKey vUniqueIndex)
        throws java.lang.IndexOutOfBoundsException
    {
        _uniqueIndexList.addElement(vUniqueIndex);
    } //-- void addUniqueIndex(de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method addUniqueIndex
     * 
     * @param index
     * @param vUniqueIndex
     */
    public void addUniqueIndex(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorKey vUniqueIndex)
        throws java.lang.IndexOutOfBoundsException
    {
        _uniqueIndexList.insertElementAt(vUniqueIndex, index);
    } //-- void addUniqueIndex(int, de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method deleteNtoMTable
     */
    public void deleteNtoMTable()
    {
        this._has_ntoMTable= false;
    } //-- void deleteNtoMTable() 

    /**
     * Method deleteOmitLocking
     */
    public void deleteOmitLocking()
    {
        this._has_omitLocking= false;
    } //-- void deleteOmitLocking() 

    /**
     * Method deleteRecordsAlwaysDeletable
     */
    public void deleteRecordsAlwaysDeletable()
    {
        this._has_recordsAlwaysDeletable= false;
    } //-- void deleteRecordsAlwaysDeletable() 

    /**
     * Method enumerateField
     */
    public java.util.Enumeration enumerateField()
    {
        return _fieldList.elements();
    } //-- java.util.Enumeration enumerateField() 

    /**
     * Method enumerateForeignKey
     */
    public java.util.Enumeration enumerateForeignKey()
    {
        return _foreignKeyList.elements();
    } //-- java.util.Enumeration enumerateForeignKey() 

    /**
     * Method enumerateIndex
     */
    public java.util.Enumeration enumerateIndex()
    {
        return _indexList.elements();
    } //-- java.util.Enumeration enumerateIndex() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Method enumerateUniqueIndex
     */
    public java.util.Enumeration enumerateUniqueIndex()
    {
        return _uniqueIndexList.elements();
    } //-- java.util.Enumeration enumerateUniqueIndex() 

    /**
     * Returns the value of field 'datasource'.
     * 
     * @return the value of field 'datasource'.
     */
    public java.lang.String getDatasource()
    {
        return this._datasource;
    } //-- java.lang.String getDatasource() 

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
     * Method getField
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTableField getField(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTableField) _fieldList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableField getField(int) 

    /**
     * Method getField
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTableField[] getField()
    {
        int size = _fieldList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorTableField[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorTableField[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorTableField) _fieldList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableField[] getField() 

    /**
     * Method getFieldCount
     */
    public int getFieldCount()
    {
        return _fieldList.size();
    } //-- int getFieldCount() 

    /**
     * Method getForeignKey
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey getForeignKey(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _foreignKeyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) _foreignKeyList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey getForeignKey(int) 

    /**
     * Method getForeignKey
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] getForeignKey()
    {
        int size = _foreignKeyList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorKey[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) _foreignKeyList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] getForeignKey() 

    /**
     * Method getForeignKeyCount
     */
    public int getForeignKeyCount()
    {
        return _foreignKeyList.size();
    } //-- int getForeignKeyCount() 

    /**
     * Returns the value of field 'historyField'.
     * 
     * @return the value of field 'historyField'.
     */
    public java.lang.String getHistoryField()
    {
        return this._historyField;
    } //-- java.lang.String getHistoryField() 

    /**
     * Method getIndex
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey getIndex(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _indexList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) _indexList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey getIndex(int) 

    /**
     * Method getIndex
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] getIndex()
    {
        int size = _indexList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorKey[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) _indexList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] getIndex() 

    /**
     * Method getIndexCount
     */
    public int getIndexCount()
    {
        return _indexList.size();
    } //-- int getIndexCount() 

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
     * Returns the value of field 'ntoMTable'.
     * 
     * @return the value of field 'ntoMTable'.
     */
    public boolean getNtoMTable()
    {
        return this._ntoMTable;
    } //-- boolean getNtoMTable() 

    /**
     * Returns the value of field 'omitLocking'.
     * 
     * @return the value of field 'omitLocking'.
     */
    public boolean getOmitLocking()
    {
        return this._omitLocking;
    } //-- boolean getOmitLocking() 

    /**
     * Returns the value of field 'primaryKey'. The field
     * 'primaryKey' has the following description: TODO: Prüfen ob
     * dieses Element nicht Pflicht sein kann!
     * 
     * @return the value of field 'primaryKey'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey getPrimaryKey()
    {
        return this._primaryKey;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey getPrimaryKey() 

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
     * Returns the value of field 'recordsAlwaysDeletable'.
     * 
     * @return the value of field 'recordsAlwaysDeletable'.
     */
    public boolean getRecordsAlwaysDeletable()
    {
        return this._recordsAlwaysDeletable;
    } //-- boolean getRecordsAlwaysDeletable() 

    /**
     * Returns the value of field 'representativeField'.
     * 
     * @return the value of field 'representativeField'.
     */
    public java.lang.String getRepresentativeField()
    {
        return this._representativeField;
    } //-- java.lang.String getRepresentativeField() 

    /**
     * Method getUniqueIndex
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey getUniqueIndex(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _uniqueIndexList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) _uniqueIndexList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey getUniqueIndex(int) 

    /**
     * Method getUniqueIndex
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] getUniqueIndex()
    {
        int size = _uniqueIndexList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorKey[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) _uniqueIndexList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] getUniqueIndex() 

    /**
     * Method getUniqueIndexCount
     */
    public int getUniqueIndexCount()
    {
        return _uniqueIndexList.size();
    } //-- int getUniqueIndexCount() 

    /**
     * Method hasNtoMTable
     */
    public boolean hasNtoMTable()
    {
        return this._has_ntoMTable;
    } //-- boolean hasNtoMTable() 

    /**
     * Method hasOmitLocking
     */
    public boolean hasOmitLocking()
    {
        return this._has_omitLocking;
    } //-- boolean hasOmitLocking() 

    /**
     * Method hasRecordsAlwaysDeletable
     */
    public boolean hasRecordsAlwaysDeletable()
    {
        return this._has_recordsAlwaysDeletable;
    } //-- boolean hasRecordsAlwaysDeletable() 

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
     * Method removeAllField
     */
    public void removeAllField()
    {
        _fieldList.removeAllElements();
    } //-- void removeAllField() 

    /**
     * Method removeAllForeignKey
     */
    public void removeAllForeignKey()
    {
        _foreignKeyList.removeAllElements();
    } //-- void removeAllForeignKey() 

    /**
     * Method removeAllIndex
     */
    public void removeAllIndex()
    {
        _indexList.removeAllElements();
    } //-- void removeAllIndex() 

    /**
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeAllUniqueIndex
     */
    public void removeAllUniqueIndex()
    {
        _uniqueIndexList.removeAllElements();
    } //-- void removeAllUniqueIndex() 

    /**
     * Method removeField
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTableField removeField(int index)
    {
        java.lang.Object obj = _fieldList.elementAt(index);
        _fieldList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTableField) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableField removeField(int) 

    /**
     * Method removeForeignKey
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey removeForeignKey(int index)
    {
        java.lang.Object obj = _foreignKeyList.elementAt(index);
        _foreignKeyList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey removeForeignKey(int) 

    /**
     * Method removeIndex
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey removeIndex(int index)
    {
        java.lang.Object obj = _indexList.elementAt(index);
        _indexList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey removeIndex(int) 

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
     * Method removeUniqueIndex
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey removeUniqueIndex(int index)
    {
        java.lang.Object obj = _uniqueIndexList.elementAt(index);
        _uniqueIndexList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorKey) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey removeUniqueIndex(int) 

    /**
     * Sets the value of field 'datasource'.
     * 
     * @param datasource the value of field 'datasource'.
     */
    public void setDatasource(java.lang.String datasource)
    {
        this._datasource = datasource;
    } //-- void setDatasource(java.lang.String) 

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
     * Method setField
     * 
     * @param index
     * @param vField
     */
    public void setField(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorTableField vField)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _fieldList.setElementAt(vField, index);
    } //-- void setField(int, de.tif.jacob.core.definition.impl.jad.castor.CastorTableField) 

    /**
     * Method setField
     * 
     * @param fieldArray
     */
    public void setField(de.tif.jacob.core.definition.impl.jad.castor.CastorTableField[] fieldArray)
    {
        //-- copy array
        _fieldList.removeAllElements();
        for (int i = 0; i < fieldArray.length; i++) {
            _fieldList.addElement(fieldArray[i]);
        }
    } //-- void setField(de.tif.jacob.core.definition.impl.jad.castor.CastorTableField) 

    /**
     * Method setForeignKey
     * 
     * @param index
     * @param vForeignKey
     */
    public void setForeignKey(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorKey vForeignKey)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _foreignKeyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _foreignKeyList.setElementAt(vForeignKey, index);
    } //-- void setForeignKey(int, de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method setForeignKey
     * 
     * @param foreignKeyArray
     */
    public void setForeignKey(de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] foreignKeyArray)
    {
        //-- copy array
        _foreignKeyList.removeAllElements();
        for (int i = 0; i < foreignKeyArray.length; i++) {
            _foreignKeyList.addElement(foreignKeyArray[i]);
        }
    } //-- void setForeignKey(de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Sets the value of field 'historyField'.
     * 
     * @param historyField the value of field 'historyField'.
     */
    public void setHistoryField(java.lang.String historyField)
    {
        this._historyField = historyField;
    } //-- void setHistoryField(java.lang.String) 

    /**
     * Method setIndex
     * 
     * @param index
     * @param vIndex
     */
    public void setIndex(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorKey vIndex)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _indexList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _indexList.setElementAt(vIndex, index);
    } //-- void setIndex(int, de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method setIndex
     * 
     * @param indexArray
     */
    public void setIndex(de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] indexArray)
    {
        //-- copy array
        _indexList.removeAllElements();
        for (int i = 0; i < indexArray.length; i++) {
            _indexList.addElement(indexArray[i]);
        }
    } //-- void setIndex(de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

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
     * Sets the value of field 'ntoMTable'.
     * 
     * @param ntoMTable the value of field 'ntoMTable'.
     */
    public void setNtoMTable(boolean ntoMTable)
    {
        this._ntoMTable = ntoMTable;
        this._has_ntoMTable = true;
    } //-- void setNtoMTable(boolean) 

    /**
     * Sets the value of field 'omitLocking'.
     * 
     * @param omitLocking the value of field 'omitLocking'.
     */
    public void setOmitLocking(boolean omitLocking)
    {
        this._omitLocking = omitLocking;
        this._has_omitLocking = true;
    } //-- void setOmitLocking(boolean) 

    /**
     * Sets the value of field 'primaryKey'. The field 'primaryKey'
     * has the following description: TODO: Prüfen ob dieses
     * Element nicht Pflicht sein kann!
     * 
     * @param primaryKey the value of field 'primaryKey'.
     */
    public void setPrimaryKey(de.tif.jacob.core.definition.impl.jad.castor.CastorKey primaryKey)
    {
        this._primaryKey = primaryKey;
    } //-- void setPrimaryKey(de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

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
     * Sets the value of field 'recordsAlwaysDeletable'.
     * 
     * @param recordsAlwaysDeletable the value of field
     * 'recordsAlwaysDeletable'.
     */
    public void setRecordsAlwaysDeletable(boolean recordsAlwaysDeletable)
    {
        this._recordsAlwaysDeletable = recordsAlwaysDeletable;
        this._has_recordsAlwaysDeletable = true;
    } //-- void setRecordsAlwaysDeletable(boolean) 

    /**
     * Sets the value of field 'representativeField'.
     * 
     * @param representativeField the value of field
     * 'representativeField'.
     */
    public void setRepresentativeField(java.lang.String representativeField)
    {
        this._representativeField = representativeField;
    } //-- void setRepresentativeField(java.lang.String) 

    /**
     * Method setUniqueIndex
     * 
     * @param index
     * @param vUniqueIndex
     */
    public void setUniqueIndex(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorKey vUniqueIndex)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _uniqueIndexList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _uniqueIndexList.setElementAt(vUniqueIndex, index);
    } //-- void setUniqueIndex(int, de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method setUniqueIndex
     * 
     * @param uniqueIndexArray
     */
    public void setUniqueIndex(de.tif.jacob.core.definition.impl.jad.castor.CastorKey[] uniqueIndexArray)
    {
        //-- copy array
        _uniqueIndexList.removeAllElements();
        for (int i = 0; i < uniqueIndexArray.length; i++) {
            _uniqueIndexList.addElement(uniqueIndexArray[i]);
        }
    } //-- void setUniqueIndex(de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

    /**
     * Method unmarshalCastorTable
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorTable(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTable) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorTable.class, reader);
    } //-- java.lang.Object unmarshalCastorTable(java.io.Reader) 

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
