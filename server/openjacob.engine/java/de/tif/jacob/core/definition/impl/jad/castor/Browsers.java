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
 * Class Browsers.
 * 
 * @version $Revision$ $Date$
 */
public class Browsers implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _browserList
     */
    private java.util.Vector _browserList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Browsers() {
        super();
        _browserList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Browsers()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addBrowser
     * 
     * @param vBrowser
     */
    public void addBrowser(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser vBrowser)
        throws java.lang.IndexOutOfBoundsException
    {
        _browserList.addElement(vBrowser);
    } //-- void addBrowser(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser) 

    /**
     * Method addBrowser
     * 
     * @param index
     * @param vBrowser
     */
    public void addBrowser(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser vBrowser)
        throws java.lang.IndexOutOfBoundsException
    {
        _browserList.insertElementAt(vBrowser, index);
    } //-- void addBrowser(int, de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser) 

    /**
     * Method enumerateBrowser
     */
    public java.util.Enumeration enumerateBrowser()
    {
        return _browserList.elements();
    } //-- java.util.Enumeration enumerateBrowser() 

    /**
     * Method getBrowser
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser getBrowser(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _browserList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser) _browserList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser getBrowser(int) 

    /**
     * Method getBrowser
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser[] getBrowser()
    {
        int size = _browserList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser) _browserList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser[] getBrowser() 

    /**
     * Method getBrowserCount
     */
    public int getBrowserCount()
    {
        return _browserList.size();
    } //-- int getBrowserCount() 

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
     * Method removeAllBrowser
     */
    public void removeAllBrowser()
    {
        _browserList.removeAllElements();
    } //-- void removeAllBrowser() 

    /**
     * Method removeBrowser
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser removeBrowser(int index)
    {
        java.lang.Object obj = _browserList.elementAt(index);
        _browserList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser removeBrowser(int) 

    /**
     * Method setBrowser
     * 
     * @param index
     * @param vBrowser
     */
    public void setBrowser(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser vBrowser)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _browserList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _browserList.setElementAt(vBrowser, index);
    } //-- void setBrowser(int, de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser) 

    /**
     * Method setBrowser
     * 
     * @param browserArray
     */
    public void setBrowser(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser[] browserArray)
    {
        //-- copy array
        _browserList.removeAllElements();
        for (int i = 0; i < browserArray.length; i++) {
            _browserList.addElement(browserArray[i]);
        }
    } //-- void setBrowser(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser) 

    /**
     * Method unmarshalBrowsers
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalBrowsers(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Browsers) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Browsers.class, reader);
    } //-- java.lang.Object unmarshalBrowsers(java.io.Reader) 

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
