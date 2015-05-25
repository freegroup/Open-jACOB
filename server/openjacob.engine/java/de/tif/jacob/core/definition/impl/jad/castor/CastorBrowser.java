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

import de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType;
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
 * Class CastorBrowser.
 * 
 * @version $Revision$ $Date$
 */
public class CastorBrowser implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _alias
     */
    private java.lang.String _alias;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _type
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType _type;

    /**
     * Field _fieldList
     */
    private java.util.Vector _fieldList;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;

    /**
     * Field _connectByKey
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorKey _connectByKey;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorBrowser() {
        super();
        _fieldList = new Vector();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addField
     * 
     * @param vField
     */
    public void addField(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField vField)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldList.addElement(vField);
    } //-- void addField(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField) 

    /**
     * Method addField
     * 
     * @param index
     * @param vField
     */
    public void addField(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField vField)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldList.insertElementAt(vField, index);
    } //-- void addField(int, de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField) 

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
     * Method enumerateField
     */
    public java.util.Enumeration enumerateField()
    {
        return _fieldList.elements();
    } //-- java.util.Enumeration enumerateField() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Returns the value of field 'alias'.
     * 
     * @return the value of field 'alias'.
     */
    public java.lang.String getAlias()
    {
        return this._alias;
    } //-- java.lang.String getAlias() 

    /**
     * Returns the value of field 'connectByKey'.
     * 
     * @return the value of field 'connectByKey'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorKey getConnectByKey()
    {
        return this._connectByKey;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorKey getConnectByKey() 

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
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField getField(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField) _fieldList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField getField(int) 

    /**
     * Method getField
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField[] getField()
    {
        int size = _fieldList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField) _fieldList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField[] getField() 

    /**
     * Method getFieldCount
     */
    public int getFieldCount()
    {
        return _fieldList.size();
    } //-- int getFieldCount() 

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
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType getType()
    {
        return this._type;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType getType() 

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
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeField
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField removeField(int index)
    {
        java.lang.Object obj = _fieldList.elementAt(index);
        _fieldList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField removeField(int) 

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
     * Sets the value of field 'alias'.
     * 
     * @param alias the value of field 'alias'.
     */
    public void setAlias(java.lang.String alias)
    {
        this._alias = alias;
    } //-- void setAlias(java.lang.String) 

    /**
     * Sets the value of field 'connectByKey'.
     * 
     * @param connectByKey the value of field 'connectByKey'.
     */
    public void setConnectByKey(de.tif.jacob.core.definition.impl.jad.castor.CastorKey connectByKey)
    {
        this._connectByKey = connectByKey;
    } //-- void setConnectByKey(de.tif.jacob.core.definition.impl.jad.castor.CastorKey) 

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
    public void setField(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField vField)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _fieldList.setElementAt(vField, index);
    } //-- void setField(int, de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField) 

    /**
     * Method setField
     * 
     * @param fieldArray
     */
    public void setField(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField[] fieldArray)
    {
        //-- copy array
        _fieldList.removeAllElements();
        for (int i = 0; i < fieldArray.length; i++) {
            _fieldList.addElement(fieldArray[i]);
        }
    } //-- void setField(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField) 

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
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType type)
    {
        this._type = type;
    } //-- void setType(de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType) 

    /**
     * Method unmarshalCastorBrowser
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorBrowser(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser.class, reader);
    } //-- java.lang.Object unmarshalCastorBrowser(java.io.Reader) 

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
