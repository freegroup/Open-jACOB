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
 * Class IntegerField.
 * 
 * @version $Revision$ $Date$
 */
public class IntegerField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _autoincrement
     */
    private boolean _autoincrement;

    /**
     * keeps track of state for field: _autoincrement
     */
    private boolean _has_autoincrement;

    /**
     * Field _min
     */
    private int _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * Field _max
     */
    private int _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;

    /**
     * Field _dbincrement
     */
    private boolean _dbincrement = false;

    /**
     * keeps track of state for field: _dbincrement
     */
    private boolean _has_dbincrement;

    /**
     * Field _default
     */
    private int _default;

    /**
     * keeps track of state for field: _default
     */
    private boolean _has_default;


      //----------------/
     //- Constructors -/
    //----------------/

    public IntegerField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.IntegerField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteAutoincrement
     */
    public void deleteAutoincrement()
    {
        this._has_autoincrement= false;
    } //-- void deleteAutoincrement() 

    /**
     * Method deleteDbincrement
     */
    public void deleteDbincrement()
    {
        this._has_dbincrement= false;
    } //-- void deleteDbincrement() 

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
     * Returns the value of field 'autoincrement'.
     * 
     * @return the value of field 'autoincrement'.
     */
    public boolean getAutoincrement()
    {
        return this._autoincrement;
    } //-- boolean getAutoincrement() 

    /**
     * Returns the value of field 'dbincrement'.
     * 
     * @return the value of field 'dbincrement'.
     */
    public boolean getDbincrement()
    {
        return this._dbincrement;
    } //-- boolean getDbincrement() 

    /**
     * Returns the value of field 'default'.
     * 
     * @return the value of field 'default'.
     */
    public int getDefault()
    {
        return this._default;
    } //-- int getDefault() 

    /**
     * Returns the value of field 'max'.
     * 
     * @return the value of field 'max'.
     */
    public int getMax()
    {
        return this._max;
    } //-- int getMax() 

    /**
     * Returns the value of field 'min'.
     * 
     * @return the value of field 'min'.
     */
    public int getMin()
    {
        return this._min;
    } //-- int getMin() 

    /**
     * Method hasAutoincrement
     */
    public boolean hasAutoincrement()
    {
        return this._has_autoincrement;
    } //-- boolean hasAutoincrement() 

    /**
     * Method hasDbincrement
     */
    public boolean hasDbincrement()
    {
        return this._has_dbincrement;
    } //-- boolean hasDbincrement() 

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
     * Sets the value of field 'autoincrement'.
     * 
     * @param autoincrement the value of field 'autoincrement'.
     */
    public void setAutoincrement(boolean autoincrement)
    {
        this._autoincrement = autoincrement;
        this._has_autoincrement = true;
    } //-- void setAutoincrement(boolean) 

    /**
     * Sets the value of field 'dbincrement'.
     * 
     * @param dbincrement the value of field 'dbincrement'.
     */
    public void setDbincrement(boolean dbincrement)
    {
        this._dbincrement = dbincrement;
        this._has_dbincrement = true;
    } //-- void setDbincrement(boolean) 

    /**
     * Sets the value of field 'default'.
     * 
     * @param _default
     * @param default the value of field 'default'.
     */
    public void setDefault(int _default)
    {
        this._default = _default;
        this._has_default = true;
    } //-- void setDefault(int) 

    /**
     * Sets the value of field 'max'.
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(int max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(int) 

    /**
     * Sets the value of field 'min'.
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(int min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(int) 

    /**
     * Method unmarshalIntegerField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalIntegerField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.IntegerField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.IntegerField.class, reader);
    } //-- java.lang.Object unmarshalIntegerField(java.io.Reader) 

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
