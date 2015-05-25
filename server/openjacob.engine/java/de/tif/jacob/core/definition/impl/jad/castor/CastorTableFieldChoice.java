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
 * Class CastorTableFieldChoice.
 * 
 * @version $Revision$ $Date$
 */
public class CastorTableFieldChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _text
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TextField _text;

    /**
     * Seit jACOB 2.6
     */
    private de.tif.jacob.core.definition.impl.jad.castor.BooleanField _boolean;

    /**
     * Field _integer
     */
    private de.tif.jacob.core.definition.impl.jad.castor.IntegerField _integer;

    /**
     * Field _long
     */
    private de.tif.jacob.core.definition.impl.jad.castor.LongField _long;

    /**
     * Field _float
     */
    private de.tif.jacob.core.definition.impl.jad.castor.FloatField _float;

    /**
     * Field _double
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DoubleField _double;

    /**
     * Field _decimal
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DecimalField _decimal;

    /**
     * Field _date
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DateField _date;

    /**
     * Field _time
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TimeField _time;

    /**
     * Field _timestamp
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TimestampField _timestamp;

    /**
     * Field _longText
     */
    private de.tif.jacob.core.definition.impl.jad.castor.LongTextField _longText;

    /**
     * Field _binary
     */
    private de.tif.jacob.core.definition.impl.jad.castor.BinaryField _binary;

    /**
     * Field _enumeration
     */
    private de.tif.jacob.core.definition.impl.jad.castor.EnumerationField _enumeration;

    /**
     * Field _document
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DocumentField _document;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorTableFieldChoice() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'binary'.
     * 
     * @return the value of field 'binary'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.BinaryField getBinary()
    {
        return this._binary;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.BinaryField getBinary() 

    /**
     * Returns the value of field 'boolean'. The field 'boolean'
     * has the following description: Seit jACOB 2.6
     * 
     * @return the value of field 'boolean'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.BooleanField getBoolean()
    {
        return this._boolean;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.BooleanField getBoolean() 

    /**
     * Returns the value of field 'date'.
     * 
     * @return the value of field 'date'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DateField getDate()
    {
        return this._date;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DateField getDate() 

    /**
     * Returns the value of field 'decimal'.
     * 
     * @return the value of field 'decimal'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DecimalField getDecimal()
    {
        return this._decimal;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DecimalField getDecimal() 

    /**
     * Returns the value of field 'document'.
     * 
     * @return the value of field 'document'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DocumentField getDocument()
    {
        return this._document;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DocumentField getDocument() 

    /**
     * Returns the value of field 'double'.
     * 
     * @return the value of field 'double'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DoubleField getDouble()
    {
        return this._double;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DoubleField getDouble() 

    /**
     * Returns the value of field 'enumeration'.
     * 
     * @return the value of field 'enumeration'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.EnumerationField getEnumeration()
    {
        return this._enumeration;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.EnumerationField getEnumeration() 

    /**
     * Returns the value of field 'float'.
     * 
     * @return the value of field 'float'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.FloatField getFloat()
    {
        return this._float;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.FloatField getFloat() 

    /**
     * Returns the value of field 'integer'.
     * 
     * @return the value of field 'integer'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.IntegerField getInteger()
    {
        return this._integer;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.IntegerField getInteger() 

    /**
     * Returns the value of field 'long'.
     * 
     * @return the value of field 'long'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.LongField getLong()
    {
        return this._long;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LongField getLong() 

    /**
     * Returns the value of field 'longText'.
     * 
     * @return the value of field 'longText'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.LongTextField getLongText()
    {
        return this._longText;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LongTextField getLongText() 

    /**
     * Returns the value of field 'text'.
     * 
     * @return the value of field 'text'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TextField getText()
    {
        return this._text;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TextField getText() 

    /**
     * Returns the value of field 'time'.
     * 
     * @return the value of field 'time'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TimeField getTime()
    {
        return this._time;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TimeField getTime() 

    /**
     * Returns the value of field 'timestamp'.
     * 
     * @return the value of field 'timestamp'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TimestampField getTimestamp()
    {
        return this._timestamp;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TimestampField getTimestamp() 

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
     * Sets the value of field 'binary'.
     * 
     * @param binary the value of field 'binary'.
     */
    public void setBinary(de.tif.jacob.core.definition.impl.jad.castor.BinaryField binary)
    {
        this._binary = binary;
    } //-- void setBinary(de.tif.jacob.core.definition.impl.jad.castor.BinaryField) 

    /**
     * Sets the value of field 'boolean'. The field 'boolean' has
     * the following description: Seit jACOB 2.6
     * 
     * @param _boolean
     * @param boolean the value of field 'boolean'.
     */
    public void setBoolean(de.tif.jacob.core.definition.impl.jad.castor.BooleanField _boolean)
    {
        this._boolean = _boolean;
    } //-- void setBoolean(de.tif.jacob.core.definition.impl.jad.castor.BooleanField) 

    /**
     * Sets the value of field 'date'.
     * 
     * @param date the value of field 'date'.
     */
    public void setDate(de.tif.jacob.core.definition.impl.jad.castor.DateField date)
    {
        this._date = date;
    } //-- void setDate(de.tif.jacob.core.definition.impl.jad.castor.DateField) 

    /**
     * Sets the value of field 'decimal'.
     * 
     * @param decimal the value of field 'decimal'.
     */
    public void setDecimal(de.tif.jacob.core.definition.impl.jad.castor.DecimalField decimal)
    {
        this._decimal = decimal;
    } //-- void setDecimal(de.tif.jacob.core.definition.impl.jad.castor.DecimalField) 

    /**
     * Sets the value of field 'document'.
     * 
     * @param document the value of field 'document'.
     */
    public void setDocument(de.tif.jacob.core.definition.impl.jad.castor.DocumentField document)
    {
        this._document = document;
    } //-- void setDocument(de.tif.jacob.core.definition.impl.jad.castor.DocumentField) 

    /**
     * Sets the value of field 'double'.
     * 
     * @param _double
     * @param double the value of field 'double'.
     */
    public void setDouble(de.tif.jacob.core.definition.impl.jad.castor.DoubleField _double)
    {
        this._double = _double;
    } //-- void setDouble(de.tif.jacob.core.definition.impl.jad.castor.DoubleField) 

    /**
     * Sets the value of field 'enumeration'.
     * 
     * @param enumeration the value of field 'enumeration'.
     */
    public void setEnumeration(de.tif.jacob.core.definition.impl.jad.castor.EnumerationField enumeration)
    {
        this._enumeration = enumeration;
    } //-- void setEnumeration(de.tif.jacob.core.definition.impl.jad.castor.EnumerationField) 

    /**
     * Sets the value of field 'float'.
     * 
     * @param _float
     * @param float the value of field 'float'.
     */
    public void setFloat(de.tif.jacob.core.definition.impl.jad.castor.FloatField _float)
    {
        this._float = _float;
    } //-- void setFloat(de.tif.jacob.core.definition.impl.jad.castor.FloatField) 

    /**
     * Sets the value of field 'integer'.
     * 
     * @param integer the value of field 'integer'.
     */
    public void setInteger(de.tif.jacob.core.definition.impl.jad.castor.IntegerField integer)
    {
        this._integer = integer;
    } //-- void setInteger(de.tif.jacob.core.definition.impl.jad.castor.IntegerField) 

    /**
     * Sets the value of field 'long'.
     * 
     * @param _long
     * @param long the value of field 'long'.
     */
    public void setLong(de.tif.jacob.core.definition.impl.jad.castor.LongField _long)
    {
        this._long = _long;
    } //-- void setLong(de.tif.jacob.core.definition.impl.jad.castor.LongField) 

    /**
     * Sets the value of field 'longText'.
     * 
     * @param longText the value of field 'longText'.
     */
    public void setLongText(de.tif.jacob.core.definition.impl.jad.castor.LongTextField longText)
    {
        this._longText = longText;
    } //-- void setLongText(de.tif.jacob.core.definition.impl.jad.castor.LongTextField) 

    /**
     * Sets the value of field 'text'.
     * 
     * @param text the value of field 'text'.
     */
    public void setText(de.tif.jacob.core.definition.impl.jad.castor.TextField text)
    {
        this._text = text;
    } //-- void setText(de.tif.jacob.core.definition.impl.jad.castor.TextField) 

    /**
     * Sets the value of field 'time'.
     * 
     * @param time the value of field 'time'.
     */
    public void setTime(de.tif.jacob.core.definition.impl.jad.castor.TimeField time)
    {
        this._time = time;
    } //-- void setTime(de.tif.jacob.core.definition.impl.jad.castor.TimeField) 

    /**
     * Sets the value of field 'timestamp'.
     * 
     * @param timestamp the value of field 'timestamp'.
     */
    public void setTimestamp(de.tif.jacob.core.definition.impl.jad.castor.TimestampField timestamp)
    {
        this._timestamp = timestamp;
    } //-- void setTimestamp(de.tif.jacob.core.definition.impl.jad.castor.TimestampField) 

    /**
     * Method unmarshalCastorTableFieldChoice
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorTableFieldChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice.class, reader);
    } //-- java.lang.Object unmarshalCastorTableFieldChoice(java.io.Reader) 

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
