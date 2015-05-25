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
 * Class CastorSubstring.
 * 
 * @version $Revision$ $Date$
 */
public class CastorSubstring implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _beginIndex
     */
    private int _beginIndex;

    /**
     * keeps track of state for field: _beginIndex
     */
    private boolean _has_beginIndex;

    /**
     * Field _endIndex
     */
    private int _endIndex;

    /**
     * keeps track of state for field: _endIndex
     */
    private boolean _has_endIndex;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorSubstring() {
        super();
    } //-- de.tif.jacob.report.impl.castor.CastorSubstring()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteBeginIndex
     */
    public void deleteBeginIndex()
    {
        this._has_beginIndex= false;
    } //-- void deleteBeginIndex() 

    /**
     * Method deleteEndIndex
     */
    public void deleteEndIndex()
    {
        this._has_endIndex= false;
    } //-- void deleteEndIndex() 

    /**
     * Returns the value of field 'beginIndex'.
     * 
     * @return the value of field 'beginIndex'.
     */
    public int getBeginIndex()
    {
        return this._beginIndex;
    } //-- int getBeginIndex() 

    /**
     * Returns the value of field 'endIndex'.
     * 
     * @return the value of field 'endIndex'.
     */
    public int getEndIndex()
    {
        return this._endIndex;
    } //-- int getEndIndex() 

    /**
     * Method hasBeginIndex
     */
    public boolean hasBeginIndex()
    {
        return this._has_beginIndex;
    } //-- boolean hasBeginIndex() 

    /**
     * Method hasEndIndex
     */
    public boolean hasEndIndex()
    {
        return this._has_endIndex;
    } //-- boolean hasEndIndex() 

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
     * Sets the value of field 'beginIndex'.
     * 
     * @param beginIndex the value of field 'beginIndex'.
     */
    public void setBeginIndex(int beginIndex)
    {
        this._beginIndex = beginIndex;
        this._has_beginIndex = true;
    } //-- void setBeginIndex(int) 

    /**
     * Sets the value of field 'endIndex'.
     * 
     * @param endIndex the value of field 'endIndex'.
     */
    public void setEndIndex(int endIndex)
    {
        this._endIndex = endIndex;
        this._has_endIndex = true;
    } //-- void setEndIndex(int) 

    /**
     * Method unmarshalCastorSubstring
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorSubstring(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorSubstring) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorSubstring.class, reader);
    } //-- java.lang.Object unmarshalCastorSubstring(java.io.Reader) 

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
