/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.qes.adf.castor;

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
 * Class IfbModes.
 * 
 * @version $Revision$ $Date$
 */
public class IfbModes implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _new
     */
    private java.lang.String _new;

    /**
     * Field _delete
     */
    private java.lang.String _delete;

    /**
     * Field _update
     */
    private java.lang.String _update;


      //----------------/
     //- Constructors -/
    //----------------/

    public IfbModes() {
        super();
    } //-- de.tif.qes.adf.castor.IfbModes()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'delete'.
     * 
     * @return the value of field 'delete'.
     */
    public java.lang.String getDelete()
    {
        return this._delete;
    } //-- java.lang.String getDelete() 

    /**
     * Returns the value of field 'new'.
     * 
     * @return the value of field 'new'.
     */
    public java.lang.String getNew()
    {
        return this._new;
    } //-- java.lang.String getNew() 

    /**
     * Returns the value of field 'update'.
     * 
     * @return the value of field 'update'.
     */
    public java.lang.String getUpdate()
    {
        return this._update;
    } //-- java.lang.String getUpdate() 

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
     * Sets the value of field 'delete'.
     * 
     * @param delete the value of field 'delete'.
     */
    public void setDelete(java.lang.String delete)
    {
        this._delete = delete;
    } //-- void setDelete(java.lang.String) 

    /**
     * Sets the value of field 'new'.
     * 
     * @param _new
     * @param new the value of field 'new'.
     */
    public void setNew(java.lang.String _new)
    {
        this._new = _new;
    } //-- void setNew(java.lang.String) 

    /**
     * Sets the value of field 'update'.
     * 
     * @param update the value of field 'update'.
     */
    public void setUpdate(java.lang.String update)
    {
        this._update = update;
    } //-- void setUpdate(java.lang.String) 

    /**
     * Method unmarshalIfbModes
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalIfbModes(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.IfbModes) Unmarshaller.unmarshal(de.tif.qes.adf.castor.IfbModes.class, reader);
    } //-- java.lang.Object unmarshalIfbModes(java.io.Reader) 

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
