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
 * Class PluginComponent.
 * 
 * @version $Revision$ $Date$
 */
public class PluginComponent implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _writeContainer
     */
    private boolean _writeContainer = true;

    /**
     * keeps track of state for field: _writeContainer
     */
    private boolean _has_writeContainer;

    /**
     * Field _pluginImplClass
     */
    private java.lang.String _pluginImplClass;

    /**
     * Field _pluginId
     */
    private java.lang.String _pluginId;

    /**
     * Field _pluginVersion
     */
    private java.lang.String _pluginVersion;

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public PluginComponent() {
        super();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.PluginComponent()


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
     * Method deleteWriteContainer
     */
    public void deleteWriteContainer()
    {
        this._has_writeContainer= false;
    } //-- void deleteWriteContainer() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Returns the value of field 'dimension'.
     * 
     * @return the value of field 'dimension'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension()
    {
        return this._dimension;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension() 

    /**
     * Returns the value of field 'pluginId'.
     * 
     * @return the value of field 'pluginId'.
     */
    public java.lang.String getPluginId()
    {
        return this._pluginId;
    } //-- java.lang.String getPluginId() 

    /**
     * Returns the value of field 'pluginImplClass'.
     * 
     * @return the value of field 'pluginImplClass'.
     */
    public java.lang.String getPluginImplClass()
    {
        return this._pluginImplClass;
    } //-- java.lang.String getPluginImplClass() 

    /**
     * Returns the value of field 'pluginVersion'.
     * 
     * @return the value of field 'pluginVersion'.
     */
    public java.lang.String getPluginVersion()
    {
        return this._pluginVersion;
    } //-- java.lang.String getPluginVersion() 

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
     * Returns the value of field 'writeContainer'.
     * 
     * @return the value of field 'writeContainer'.
     */
    public boolean getWriteContainer()
    {
        return this._writeContainer;
    } //-- boolean getWriteContainer() 

    /**
     * Method hasWriteContainer
     */
    public boolean hasWriteContainer()
    {
        return this._has_writeContainer;
    } //-- boolean hasWriteContainer() 

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
     * Sets the value of field 'dimension'.
     * 
     * @param dimension the value of field 'dimension'.
     */
    public void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension dimension)
    {
        this._dimension = dimension;
    } //-- void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension) 

    /**
     * Sets the value of field 'pluginId'.
     * 
     * @param pluginId the value of field 'pluginId'.
     */
    public void setPluginId(java.lang.String pluginId)
    {
        this._pluginId = pluginId;
    } //-- void setPluginId(java.lang.String) 

    /**
     * Sets the value of field 'pluginImplClass'.
     * 
     * @param pluginImplClass the value of field 'pluginImplClass'.
     */
    public void setPluginImplClass(java.lang.String pluginImplClass)
    {
        this._pluginImplClass = pluginImplClass;
    } //-- void setPluginImplClass(java.lang.String) 

    /**
     * Sets the value of field 'pluginVersion'.
     * 
     * @param pluginVersion the value of field 'pluginVersion'.
     */
    public void setPluginVersion(java.lang.String pluginVersion)
    {
        this._pluginVersion = pluginVersion;
    } //-- void setPluginVersion(java.lang.String) 

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
     * Sets the value of field 'writeContainer'.
     * 
     * @param writeContainer the value of field 'writeContainer'.
     */
    public void setWriteContainer(boolean writeContainer)
    {
        this._writeContainer = writeContainer;
        this._has_writeContainer = true;
    } //-- void setWriteContainer(boolean) 

    /**
     * Method unmarshalPluginComponent
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalPluginComponent(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.PluginComponent) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.PluginComponent.class, reader);
    } //-- java.lang.Object unmarshalPluginComponent(java.io.Reader) 

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
