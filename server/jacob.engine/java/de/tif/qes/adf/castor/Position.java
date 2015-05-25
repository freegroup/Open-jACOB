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
 * Class Position.
 * 
 * @version $Revision$ $Date$
 */
public class Position implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _left
     */
    private long _left;

    /**
     * keeps track of state for field: _left
     */
    private boolean _has_left;

    /**
     * Field _top
     */
    private long _top;

    /**
     * keeps track of state for field: _top
     */
    private boolean _has_top;

    /**
     * Field _width
     */
    private long _width;

    /**
     * keeps track of state for field: _width
     */
    private boolean _has_width;

    /**
     * Field _height
     */
    private long _height;

    /**
     * keeps track of state for field: _height
     */
    private boolean _has_height;


      //----------------/
     //- Constructors -/
    //----------------/

    public Position() {
        super();
    } //-- de.tif.qes.adf.castor.Position()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteHeight
     */
    public void deleteHeight()
    {
        this._has_height= false;
    } //-- void deleteHeight() 

    /**
     * Method deleteLeft
     */
    public void deleteLeft()
    {
        this._has_left= false;
    } //-- void deleteLeft() 

    /**
     * Method deleteTop
     */
    public void deleteTop()
    {
        this._has_top= false;
    } //-- void deleteTop() 

    /**
     * Method deleteWidth
     */
    public void deleteWidth()
    {
        this._has_width= false;
    } //-- void deleteWidth() 

    /**
     * Returns the value of field 'height'.
     * 
     * @return the value of field 'height'.
     */
    public long getHeight()
    {
        return this._height;
    } //-- long getHeight() 

    /**
     * Returns the value of field 'left'.
     * 
     * @return the value of field 'left'.
     */
    public long getLeft()
    {
        return this._left;
    } //-- long getLeft() 

    /**
     * Returns the value of field 'top'.
     * 
     * @return the value of field 'top'.
     */
    public long getTop()
    {
        return this._top;
    } //-- long getTop() 

    /**
     * Returns the value of field 'width'.
     * 
     * @return the value of field 'width'.
     */
    public long getWidth()
    {
        return this._width;
    } //-- long getWidth() 

    /**
     * Method hasHeight
     */
    public boolean hasHeight()
    {
        return this._has_height;
    } //-- boolean hasHeight() 

    /**
     * Method hasLeft
     */
    public boolean hasLeft()
    {
        return this._has_left;
    } //-- boolean hasLeft() 

    /**
     * Method hasTop
     */
    public boolean hasTop()
    {
        return this._has_top;
    } //-- boolean hasTop() 

    /**
     * Method hasWidth
     */
    public boolean hasWidth()
    {
        return this._has_width;
    } //-- boolean hasWidth() 

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
     * Sets the value of field 'height'.
     * 
     * @param height the value of field 'height'.
     */
    public void setHeight(long height)
    {
        this._height = height;
        this._has_height = true;
    } //-- void setHeight(long) 

    /**
     * Sets the value of field 'left'.
     * 
     * @param left the value of field 'left'.
     */
    public void setLeft(long left)
    {
        this._left = left;
        this._has_left = true;
    } //-- void setLeft(long) 

    /**
     * Sets the value of field 'top'.
     * 
     * @param top the value of field 'top'.
     */
    public void setTop(long top)
    {
        this._top = top;
        this._has_top = true;
    } //-- void setTop(long) 

    /**
     * Sets the value of field 'width'.
     * 
     * @param width the value of field 'width'.
     */
    public void setWidth(long width)
    {
        this._width = width;
        this._has_width = true;
    } //-- void setWidth(long) 

    /**
     * Method unmarshalPosition
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalPosition(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Position) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Position.class, reader);
    } //-- java.lang.Object unmarshalPosition(java.io.Reader) 

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
