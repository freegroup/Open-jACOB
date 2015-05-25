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
 * Class DeleteRecord.
 * 
 * @version $Revision$ $Date$
 */
public class DeleteRecord implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _needUserConfirmation
     */
    private boolean _needUserConfirmation = true;

    /**
     * keeps track of state for field: _needUserConfirmation
     */
    private boolean _has_needUserConfirmation;


      //----------------/
     //- Constructors -/
    //----------------/

    public DeleteRecord() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteNeedUserConfirmation
     */
    public void deleteNeedUserConfirmation()
    {
        this._has_needUserConfirmation= false;
    } //-- void deleteNeedUserConfirmation() 

    /**
     * Returns the value of field 'needUserConfirmation'.
     * 
     * @return the value of field 'needUserConfirmation'.
     */
    public boolean getNeedUserConfirmation()
    {
        return this._needUserConfirmation;
    } //-- boolean getNeedUserConfirmation() 

    /**
     * Method hasNeedUserConfirmation
     */
    public boolean hasNeedUserConfirmation()
    {
        return this._has_needUserConfirmation;
    } //-- boolean hasNeedUserConfirmation() 

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
     * Sets the value of field 'needUserConfirmation'.
     * 
     * @param needUserConfirmation the value of field
     * 'needUserConfirmation'.
     */
    public void setNeedUserConfirmation(boolean needUserConfirmation)
    {
        this._needUserConfirmation = needUserConfirmation;
        this._has_needUserConfirmation = true;
    } //-- void setNeedUserConfirmation(boolean) 

    /**
     * Method unmarshalDeleteRecord
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDeleteRecord(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord.class, reader);
    } //-- java.lang.Object unmarshalDeleteRecord(java.io.Reader) 

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
