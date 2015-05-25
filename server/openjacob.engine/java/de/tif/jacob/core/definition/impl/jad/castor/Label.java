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
 * Class Label.
 * 
 * @version $Revision$ $Date$
 */
public class Label implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableField
     */
    private java.lang.String _tableField;

    /**
     * Field _nullDefaultValue
     */
    private java.lang.String _nullDefaultValue;

    /**
     * Field _caption
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorCaption _caption;


      //----------------/
     //- Constructors -/
    //----------------/

    public Label() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Label()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'caption'.
     * 
     * @return the value of field 'caption'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorCaption getCaption()
    {
        return this._caption;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorCaption getCaption() 

    /**
     * Returns the value of field 'nullDefaultValue'.
     * 
     * @return the value of field 'nullDefaultValue'.
     */
    public java.lang.String getNullDefaultValue()
    {
        return this._nullDefaultValue;
    } //-- java.lang.String getNullDefaultValue() 

    /**
     * Returns the value of field 'tableField'.
     * 
     * @return the value of field 'tableField'.
     */
    public java.lang.String getTableField()
    {
        return this._tableField;
    } //-- java.lang.String getTableField() 

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
     * Sets the value of field 'caption'.
     * 
     * @param caption the value of field 'caption'.
     */
    public void setCaption(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption caption)
    {
        this._caption = caption;
    } //-- void setCaption(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption) 

    /**
     * Sets the value of field 'nullDefaultValue'.
     * 
     * @param nullDefaultValue the value of field 'nullDefaultValue'
     */
    public void setNullDefaultValue(java.lang.String nullDefaultValue)
    {
        this._nullDefaultValue = nullDefaultValue;
    } //-- void setNullDefaultValue(java.lang.String) 

    /**
     * Sets the value of field 'tableField'.
     * 
     * @param tableField the value of field 'tableField'.
     */
    public void setTableField(java.lang.String tableField)
    {
        this._tableField = tableField;
    } //-- void setTableField(java.lang.String) 

    /**
     * Method unmarshalLabel
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLabel(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Label) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Label.class, reader);
    } //-- java.lang.Object unmarshalLabel(java.io.Reader) 

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
