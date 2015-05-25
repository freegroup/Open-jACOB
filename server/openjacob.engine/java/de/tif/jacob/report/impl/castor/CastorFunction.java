/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.report.impl.castor;

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
 * Class CastorFunction.
 * 
 * @version $Revision$ $Date$
 */
public class CastorFunction implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _substring
     */
    private de.tif.jacob.report.impl.castor.CastorSubstring _substring;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorFunction() {
        super();
    } //-- de.tif.jacob.report.impl.castor.CastorFunction()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'substring'.
     * 
     * @return the value of field 'substring'.
     */
    public de.tif.jacob.report.impl.castor.CastorSubstring getSubstring()
    {
        return this._substring;
    } //-- de.tif.jacob.report.impl.castor.CastorSubstring getSubstring() 

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
     * Sets the value of field 'substring'.
     * 
     * @param substring the value of field 'substring'.
     */
    public void setSubstring(de.tif.jacob.report.impl.castor.CastorSubstring substring)
    {
        this._substring = substring;
    } //-- void setSubstring(de.tif.jacob.report.impl.castor.CastorSubstring) 

    /**
     * Method unmarshalCastorFunction
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorFunction(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorFunction) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorFunction.class, reader);
    } //-- java.lang.Object unmarshalCastorFunction(java.io.Reader) 

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
