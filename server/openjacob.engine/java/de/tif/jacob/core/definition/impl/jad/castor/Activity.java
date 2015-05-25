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
 * Class Activity.
 * 
 * @version $Revision$ $Date$
 */
public class Activity implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _relatedForm
     */
    private java.lang.String _relatedForm;


      //----------------/
     //- Constructors -/
    //----------------/

    public Activity() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Activity()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'relatedForm'.
     * 
     * @return the value of field 'relatedForm'.
     */
    public java.lang.String getRelatedForm()
    {
        return this._relatedForm;
    } //-- java.lang.String getRelatedForm() 

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
     * Sets the value of field 'relatedForm'.
     * 
     * @param relatedForm the value of field 'relatedForm'.
     */
    public void setRelatedForm(java.lang.String relatedForm)
    {
        this._relatedForm = relatedForm;
    } //-- void setRelatedForm(java.lang.String) 

    /**
     * Method unmarshalActivity
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalActivity(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Activity) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Activity.class, reader);
    } //-- java.lang.Object unmarshalActivity(java.io.Reader) 

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
