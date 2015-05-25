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
 * Kann beim nächsten Majorrelease gelöscht werden. Wird nicht mehr
 * benötigt!
 * 
 * @version $Revision$ $Date$
 */
public class ExternalModules implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _externalModuleList
     */
    private java.util.Vector _externalModuleList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExternalModules() {
        super();
        _externalModuleList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ExternalModules()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addExternalModule
     * 
     * @param vExternalModule
     */
    public void addExternalModule(de.tif.jacob.core.definition.impl.jad.castor.ExternalModule vExternalModule)
        throws java.lang.IndexOutOfBoundsException
    {
        _externalModuleList.addElement(vExternalModule);
    } //-- void addExternalModule(de.tif.jacob.core.definition.impl.jad.castor.ExternalModule) 

    /**
     * Method addExternalModule
     * 
     * @param index
     * @param vExternalModule
     */
    public void addExternalModule(int index, de.tif.jacob.core.definition.impl.jad.castor.ExternalModule vExternalModule)
        throws java.lang.IndexOutOfBoundsException
    {
        _externalModuleList.insertElementAt(vExternalModule, index);
    } //-- void addExternalModule(int, de.tif.jacob.core.definition.impl.jad.castor.ExternalModule) 

    /**
     * Method enumerateExternalModule
     */
    public java.util.Enumeration enumerateExternalModule()
    {
        return _externalModuleList.elements();
    } //-- java.util.Enumeration enumerateExternalModule() 

    /**
     * Method getExternalModule
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ExternalModule getExternalModule(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _externalModuleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.ExternalModule) _externalModuleList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ExternalModule getExternalModule(int) 

    /**
     * Method getExternalModule
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ExternalModule[] getExternalModule()
    {
        int size = _externalModuleList.size();
        de.tif.jacob.core.definition.impl.jad.castor.ExternalModule[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.ExternalModule[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.ExternalModule) _externalModuleList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ExternalModule[] getExternalModule() 

    /**
     * Method getExternalModuleCount
     */
    public int getExternalModuleCount()
    {
        return _externalModuleList.size();
    } //-- int getExternalModuleCount() 

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
     * Method removeAllExternalModule
     */
    public void removeAllExternalModule()
    {
        _externalModuleList.removeAllElements();
    } //-- void removeAllExternalModule() 

    /**
     * Method removeExternalModule
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ExternalModule removeExternalModule(int index)
    {
        java.lang.Object obj = _externalModuleList.elementAt(index);
        _externalModuleList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.ExternalModule) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ExternalModule removeExternalModule(int) 

    /**
     * Method setExternalModule
     * 
     * @param index
     * @param vExternalModule
     */
    public void setExternalModule(int index, de.tif.jacob.core.definition.impl.jad.castor.ExternalModule vExternalModule)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _externalModuleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _externalModuleList.setElementAt(vExternalModule, index);
    } //-- void setExternalModule(int, de.tif.jacob.core.definition.impl.jad.castor.ExternalModule) 

    /**
     * Method setExternalModule
     * 
     * @param externalModuleArray
     */
    public void setExternalModule(de.tif.jacob.core.definition.impl.jad.castor.ExternalModule[] externalModuleArray)
    {
        //-- copy array
        _externalModuleList.removeAllElements();
        for (int i = 0; i < externalModuleArray.length; i++) {
            _externalModuleList.addElement(externalModuleArray[i]);
        }
    } //-- void setExternalModule(de.tif.jacob.core.definition.impl.jad.castor.ExternalModule) 

    /**
     * Method unmarshalExternalModules
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalExternalModules(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.ExternalModules) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.ExternalModules.class, reader);
    } //-- java.lang.Object unmarshalExternalModules(java.io.Reader) 

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
