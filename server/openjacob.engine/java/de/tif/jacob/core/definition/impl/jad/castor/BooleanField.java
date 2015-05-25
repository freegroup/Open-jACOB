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
 * Class BooleanField.
 * 
 * @version $Revision$ $Date$
 */
public class BooleanField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _default
     */
    private boolean _default;

    /**
     * keeps track of state for field: _default
     */
    private boolean _has_default;


      //----------------/
     //- Constructors -/
    //----------------/

    public BooleanField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.BooleanField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteDefault
     */
    public void deleteDefault()
    {
        this._has_default= false;
    } //-- void deleteDefault() 

    /**
     * Returns the value of field 'default'.
     * 
     * @return the value of field 'default'.
     */
    public boolean getDefault()
    {
        return this._default;
    } //-- boolean getDefault() 

    /**
     * Method hasDefault
     */
    public boolean hasDefault()
    {
        return this._has_default;
    } //-- boolean hasDefault() 

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
     * Sets the value of field 'default'.
     * 
     * @param _default
     * @param default the value of field 'default'.
     */
    public void setDefault(boolean _default)
    {
        this._default = _default;
        this._has_default = true;
    } //-- void setDefault(boolean) 

    /**
     * Method unmarshalBooleanField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalBooleanField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.BooleanField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.BooleanField.class, reader);
    } //-- java.lang.Object unmarshalBooleanField(java.io.Reader) 

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
