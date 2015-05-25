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
 * Class DoubleField.
 * 
 * @version $Revision$ $Date$
 */
public class DoubleField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _min
     */
    private double _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * Field _max
     */
    private double _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;

    /**
     * Field _default
     */
    private double _default;

    /**
     * keeps track of state for field: _default
     */
    private boolean _has_default;


      //----------------/
     //- Constructors -/
    //----------------/

    public DoubleField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DoubleField()


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
    public double getDefault()
    {
        return this._default;
    } //-- double getDefault() 

    /**
     * Returns the value of field 'max'.
     * 
     * @return the value of field 'max'.
     */
    public double getMax()
    {
        return this._max;
    } //-- double getMax() 

    /**
     * Returns the value of field 'min'.
     * 
     * @return the value of field 'min'.
     */
    public double getMin()
    {
        return this._min;
    } //-- double getMin() 

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
    public void setDefault(double _default)
    {
        this._default = _default;
        this._has_default = true;
    } //-- void setDefault(double) 

    /**
     * Sets the value of field 'max'.
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(double max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(double) 

    /**
     * Sets the value of field 'min'.
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(double min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(double) 

    /**
     * Method unmarshalDoubleField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDoubleField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.DoubleField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.DoubleField.class, reader);
    } //-- java.lang.Object unmarshalDoubleField(java.io.Reader) 

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
