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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType;
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
 * Class CastorApplication.
 * 
 * @version $Revision$ $Date$
 */
public class CastorApplication implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _title
     */
    private java.lang.String _title;

    /**
     * Field _eventHandler
     */
    private java.lang.String _eventHandler;

    /**
     * Field _dataScope
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType _dataScope;

    /**
     * Field _eventHandlerLookupMethod
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType _eventHandlerLookupMethod;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _domainList
     */
    private java.util.Vector _domainList;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;

    /**
     * Field _workFlow
     */
    private java.lang.String _workFlow;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorApplication() {
        super();
        _domainList = new Vector();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorApplication()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDomain
     * 
     * @param vDomain
     */
    public void addDomain(de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef vDomain)
        throws java.lang.IndexOutOfBoundsException
    {
        _domainList.addElement(vDomain);
    } //-- void addDomain(de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef) 

    /**
     * Method addDomain
     * 
     * @param index
     * @param vDomain
     */
    public void addDomain(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef vDomain)
        throws java.lang.IndexOutOfBoundsException
    {
        _domainList.insertElementAt(vDomain, index);
    } //-- void addDomain(int, de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef) 

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
     * Method enumerateDomain
     */
    public java.util.Enumeration enumerateDomain()
    {
        return _domainList.elements();
    } //-- java.util.Enumeration enumerateDomain() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Returns the value of field 'dataScope'.
     * 
     * @return the value of field 'dataScope'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType getDataScope()
    {
        return this._dataScope;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType getDataScope() 

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
     * Method getDomain
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef getDomain(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _domainList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef) _domainList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef getDomain(int) 

    /**
     * Method getDomain
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef[] getDomain()
    {
        int size = _domainList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef) _domainList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef[] getDomain() 

    /**
     * Method getDomainCount
     */
    public int getDomainCount()
    {
        return _domainList.size();
    } //-- int getDomainCount() 

    /**
     * Returns the value of field 'eventHandler'.
     * 
     * @return the value of field 'eventHandler'.
     */
    public java.lang.String getEventHandler()
    {
        return this._eventHandler;
    } //-- java.lang.String getEventHandler() 

    /**
     * Returns the value of field 'eventHandlerLookupMethod'.
     * 
     * @return the value of field 'eventHandlerLookupMethod'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType getEventHandlerLookupMethod()
    {
        return this._eventHandlerLookupMethod;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType getEventHandlerLookupMethod() 

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
     * Returns the value of field 'title'.
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
     * Returns the value of field 'workFlow'.
     * 
     * @return the value of field 'workFlow'.
     */
    public java.lang.String getWorkFlow()
    {
        return this._workFlow;
    } //-- java.lang.String getWorkFlow() 

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
     * Method removeAllDomain
     */
    public void removeAllDomain()
    {
        _domainList.removeAllElements();
    } //-- void removeAllDomain() 

    /**
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeDomain
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef removeDomain(int index)
    {
        java.lang.Object obj = _domainList.elementAt(index);
        _domainList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef removeDomain(int) 

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
     * Sets the value of field 'dataScope'.
     * 
     * @param dataScope the value of field 'dataScope'.
     */
    public void setDataScope(de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType dataScope)
    {
        this._dataScope = dataScope;
    } //-- void setDataScope(de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType) 

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
     * Method setDomain
     * 
     * @param index
     * @param vDomain
     */
    public void setDomain(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef vDomain)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _domainList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _domainList.setElementAt(vDomain, index);
    } //-- void setDomain(int, de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef) 

    /**
     * Method setDomain
     * 
     * @param domainArray
     */
    public void setDomain(de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef[] domainArray)
    {
        //-- copy array
        _domainList.removeAllElements();
        for (int i = 0; i < domainArray.length; i++) {
            _domainList.addElement(domainArray[i]);
        }
    } //-- void setDomain(de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef) 

    /**
     * Sets the value of field 'eventHandler'.
     * 
     * @param eventHandler the value of field 'eventHandler'.
     */
    public void setEventHandler(java.lang.String eventHandler)
    {
        this._eventHandler = eventHandler;
    } //-- void setEventHandler(java.lang.String) 

    /**
     * Sets the value of field 'eventHandlerLookupMethod'.
     * 
     * @param eventHandlerLookupMethod the value of field
     * 'eventHandlerLookupMethod'.
     */
    public void setEventHandlerLookupMethod(de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType eventHandlerLookupMethod)
    {
        this._eventHandlerLookupMethod = eventHandlerLookupMethod;
    } //-- void setEventHandlerLookupMethod(de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType) 

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
     * Sets the value of field 'title'.
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Sets the value of field 'workFlow'.
     * 
     * @param workFlow the value of field 'workFlow'.
     */
    public void setWorkFlow(java.lang.String workFlow)
    {
        this._workFlow = workFlow;
    } //-- void setWorkFlow(java.lang.String) 

    /**
     * Method unmarshalCastorApplication
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorApplication(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorApplication) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorApplication.class, reader);
    } //-- java.lang.Object unmarshalCastorApplication(java.io.Reader) 

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
