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
import java.math.BigDecimal;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class DecimalField.
 * 
 * @version $Revision$ $Date$
 */
public class DecimalField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _min
     */
    private java.math.BigDecimal _min;

    /**
     * Field _max
     */
    private java.math.BigDecimal _max;

    /**
     * Field _scale
     */
    private short _scale = 2;

    /**
     * keeps track of state for field: _scale
     */
    private boolean _has_scale;

    /**
     * Field _default
     */
    private java.math.BigDecimal _default;


      //----------------/
     //- Constructors -/
    //----------------/

    public DecimalField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DecimalField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteScale
     */
    public void deleteScale()
    {
        this._has_scale= false;
    } //-- void deleteScale() 

    /**
     * Returns the value of field 'default'.
     * 
     * @return the value of field 'default'.
     */
    public java.math.BigDecimal getDefault()
    {
        return this._default;
    } //-- java.math.BigDecimal getDefault() 

    /**
     * Returns the value of field 'max'.
     * 
     * @return the value of field 'max'.
     */
    public java.math.BigDecimal getMax()
    {
        return this._max;
    } //-- java.math.BigDecimal getMax() 

    /**
     * Returns the value of field 'min'.
     * 
     * @return the value of field 'min'.
     */
    public java.math.BigDecimal getMin()
    {
        return this._min;
    } //-- java.math.BigDecimal getMin() 

    /**
     * Returns the value of field 'scale'.
     * 
     * @return the value of field 'scale'.
     */
    public short getScale()
    {
        return this._scale;
    } //-- short getScale() 

    /**
     * Method hasScale
     */
    public boolean hasScale()
    {
        return this._has_scale;
    } //-- boolean hasScale() 

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
    public void setDefault(java.math.BigDecimal _default)
    {
        this._default = _default;
    } //-- void setDefault(java.math.BigDecimal) 

    /**
     * Sets the value of field 'max'.
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(java.math.BigDecimal max)
    {
        this._max = max;
    } //-- void setMax(java.math.BigDecimal) 

    /**
     * Sets the value of field 'min'.
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(java.math.BigDecimal min)
    {
        this._min = min;
    } //-- void setMin(java.math.BigDecimal) 

    /**
     * Sets the value of field 'scale'.
     * 
     * @param scale the value of field 'scale'.
     */
    public void setScale(short scale)
    {
        this._scale = scale;
        this._has_scale = true;
    } //-- void setScale(short) 

    /**
     * Method unmarshalDecimalField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDecimalField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.DecimalField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.DecimalField.class, reader);
    } //-- java.lang.Object unmarshalDecimalField(java.io.Reader) 

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
