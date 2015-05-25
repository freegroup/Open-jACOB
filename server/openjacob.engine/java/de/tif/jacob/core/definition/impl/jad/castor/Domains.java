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
 * Class Domains.
 * 
 * @version $Revision$ $Date$
 */
public class Domains implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _domainList
     */
    private java.util.Vector _domainList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Domains() {
        super();
        _domainList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Domains()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDomain
     * 
     * @param vDomain
     */
    public void addDomain(de.tif.jacob.core.definition.impl.jad.castor.CastorDomain vDomain)
        throws java.lang.IndexOutOfBoundsException
    {
        _domainList.addElement(vDomain);
    } //-- void addDomain(de.tif.jacob.core.definition.impl.jad.castor.CastorDomain) 

    /**
     * Method addDomain
     * 
     * @param index
     * @param vDomain
     */
    public void addDomain(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorDomain vDomain)
        throws java.lang.IndexOutOfBoundsException
    {
        _domainList.insertElementAt(vDomain, index);
    } //-- void addDomain(int, de.tif.jacob.core.definition.impl.jad.castor.CastorDomain) 

    /**
     * Method enumerateDomain
     */
    public java.util.Enumeration enumerateDomain()
    {
        return _domainList.elements();
    } //-- java.util.Enumeration enumerateDomain() 

    /**
     * Method getDomain
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDomain getDomain(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _domainList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorDomain) _domainList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDomain getDomain(int) 

    /**
     * Method getDomain
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDomain[] getDomain()
    {
        int size = _domainList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorDomain[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorDomain[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorDomain) _domainList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDomain[] getDomain() 

    /**
     * Method getDomainCount
     */
    public int getDomainCount()
    {
        return _domainList.size();
    } //-- int getDomainCount() 

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
     * Method removeDomain
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDomain removeDomain(int index)
    {
        java.lang.Object obj = _domainList.elementAt(index);
        _domainList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorDomain) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDomain removeDomain(int) 

    /**
     * Method setDomain
     * 
     * @param index
     * @param vDomain
     */
    public void setDomain(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorDomain vDomain)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _domainList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _domainList.setElementAt(vDomain, index);
    } //-- void setDomain(int, de.tif.jacob.core.definition.impl.jad.castor.CastorDomain) 

    /**
     * Method setDomain
     * 
     * @param domainArray
     */
    public void setDomain(de.tif.jacob.core.definition.impl.jad.castor.CastorDomain[] domainArray)
    {
        //-- copy array
        _domainList.removeAllElements();
        for (int i = 0; i < domainArray.length; i++) {
            _domainList.addElement(domainArray[i]);
        }
    } //-- void setDomain(de.tif.jacob.core.definition.impl.jad.castor.CastorDomain) 

    /**
     * Method unmarshalDomains
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDomains(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Domains) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Domains.class, reader);
    } //-- java.lang.Object unmarshalDomains(java.io.Reader) 

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
