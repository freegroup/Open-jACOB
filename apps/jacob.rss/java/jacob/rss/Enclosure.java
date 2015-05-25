/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.rss;

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
 * Describes a media object that is attached to the item. 
 * 
 * @version $Revision$ $Date$
 */
public class Enclosure implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Where the enclosure is located
     */
    private java.lang.String _url;

    /**
     * How big it is in bytes.
     */
    private double _length;

    /**
     * keeps track of state for field: _length
     */
    private boolean _has_length;

    /**
     * What its type is, a standard MIME type.
     */
    private java.lang.String _type;


      //----------------/
     //- Constructors -/
    //----------------/

    public Enclosure() {
        super();
    } //-- jacob.rss.Enclosure()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteLength
     */
    public void deleteLength()
    {
        this._has_length= false;
    } //-- void deleteLength() 

    /**
     * Returns the value of field 'length'. The field 'length' has
     * the following description: How big it is in bytes.
     * 
     * @return the value of field 'length'.
     */
    public double getLength()
    {
        return this._length;
    } //-- double getLength() 

    /**
     * Returns the value of field 'type'. The field 'type' has the
     * following description: What its type is, a standard MIME
     * type.
     * 
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Returns the value of field 'url'. The field 'url' has the
     * following description: Where the enclosure is located
     * 
     * @return the value of field 'url'.
     */
    public java.lang.String getUrl()
    {
        return this._url;
    } //-- java.lang.String getUrl() 

    /**
     * Method hasLength
     */
    public boolean hasLength()
    {
        return this._has_length;
    } //-- boolean hasLength() 

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
     * Sets the value of field 'length'. The field 'length' has the
     * following description: How big it is in bytes.
     * 
     * @param length the value of field 'length'.
     */
    public void setLength(double length)
    {
        this._length = length;
        this._has_length = true;
    } //-- void setLength(double) 

    /**
     * Sets the value of field 'type'. The field 'type' has the
     * following description: What its type is, a standard MIME
     * type.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * Sets the value of field 'url'. The field 'url' has the
     * following description: Where the enclosure is located
     * 
     * @param url the value of field 'url'.
     */
    public void setUrl(java.lang.String url)
    {
        this._url = url;
    } //-- void setUrl(java.lang.String) 

    /**
     * Method unmarshalEnclosure
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEnclosure(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.rss.Enclosure) Unmarshaller.unmarshal(jacob.rss.Enclosure.class, reader);
    } //-- java.lang.Object unmarshalEnclosure(java.io.Reader) 

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
