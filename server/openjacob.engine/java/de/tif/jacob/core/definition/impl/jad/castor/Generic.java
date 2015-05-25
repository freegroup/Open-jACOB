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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Generic.
 * 
 * @version $Revision$ $Date$
 */
public class Generic implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dummy
     */
    private boolean _dummy;

    /**
     * keeps track of state for field: _dummy
     */
    private boolean _has_dummy;


      //----------------/
     //- Constructors -/
    //----------------/

    public Generic() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Generic()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteDummy
     */
    public void deleteDummy()
    {
        this._has_dummy= false;
    } //-- void deleteDummy() 

    /**
     * Returns the value of field 'dummy'.
     * 
     * @return the value of field 'dummy'.
     */
    public boolean getDummy()
    {
        return this._dummy;
    } //-- boolean getDummy() 

    /**
     * Method hasDummy
     */
    public boolean hasDummy()
    {
        return this._has_dummy;
    } //-- boolean hasDummy() 

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
     * Sets the value of field 'dummy'.
     * 
     * @param dummy the value of field 'dummy'.
     */
    public void setDummy(boolean dummy)
    {
        this._dummy = dummy;
        this._has_dummy = true;
    } //-- void setDummy(boolean) 

    /**
     * Method unmarshalGeneric
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalGeneric(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Generic) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Generic.class, reader);
    } //-- java.lang.Object unmarshalGeneric(java.io.Reader) 

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
