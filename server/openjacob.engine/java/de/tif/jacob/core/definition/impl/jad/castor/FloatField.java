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
 * Class FloatField.
 * 
 * @version $Revision$ $Date$
 */
public class FloatField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _min
     */
    private float _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * Field _max
     */
    private float _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;

    /**
     * Field _default
     */
    private float _default;

    /**
     * keeps track of state for field: _default
     */
    private boolean _has_default;


      //----------------/
     //- Constructors -/
    //----------------/

    public FloatField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.FloatField()


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
     * Method deleteMax
     */
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
     * Method deleteMin
     */
    public void deleteMin()
    {
        this._has_min= false;
    } //-- void deleteMin() 

    /**
     * Returns the value of field 'default'.
     * 
     * @return the value of field 'default'.
     */
    public float getDefault()
    {
        return this._default;
    } //-- float getDefault() 

    /**
     * Returns the value of field 'max'.
     * 
     * @return the value of field 'max'.
     */
    public float getMax()
    {
        return this._max;
    } //-- float getMax() 

    /**
     * Returns the value of field 'min'.
     * 
     * @return the value of field 'min'.
     */
    public float getMin()
    {
        return this._min;
    } //-- float getMin() 

    /**
     * Method hasDefault
     */
    public boolean hasDefault()
    {
        return this._has_default;
    } //-- boolean hasDefault() 

    /**
     * Method hasMax
     */
    public boolean hasMax()
    {
        return this._has_max;
    } //-- boolean hasMax() 

    /**
     * Method hasMin
     */
    public boolean hasMin()
    {
        return this._has_min;
    } //-- boolean hasMin() 

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
    public void setDefault(float _default)
    {
        this._default = _default;
        this._has_default = true;
    } //-- void setDefault(float) 

    /**
     * Sets the value of field 'max'.
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(float max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(float) 

    /**
     * Sets the value of field 'min'.
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(float min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(float) 

    /**
     * Method unmarshalFloatField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalFloatField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.FloatField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.FloatField.class, reader);
    } //-- java.lang.Object unmarshalFloatField(java.io.Reader) 

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
