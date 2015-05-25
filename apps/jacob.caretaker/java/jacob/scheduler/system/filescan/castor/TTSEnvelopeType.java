/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.scheduler.system.filescan.castor;

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
 * Class TTSEnvelopeType.
 * 
 * @version $Revision$ $Date$
 */
public class TTSEnvelopeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ttl
     */
    private byte _ttl;

    /**
     * keeps track of state for field: _ttl
     */
    private boolean _has_ttl;

    /**
     * Field _header
     */
    private jacob.scheduler.system.filescan.castor.HeaderType _header;

    /**
     * Field _body
     */
    private jacob.scheduler.system.filescan.castor.BodyType _body;


      //----------------/
     //- Constructors -/
    //----------------/

    public TTSEnvelopeType() {
        super();
    } //-- jacob.scheduler.system.filescan.castor.TTSEnvelopeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteTtl
     */
    public void deleteTtl()
    {
        this._has_ttl= false;
    } //-- void deleteTtl() 

    /**
     * Returns the value of field 'body'.
     * 
     * @return the value of field 'body'.
     */
    public jacob.scheduler.system.filescan.castor.BodyType getBody()
    {
        return this._body;
    } //-- jacob.scheduler.system.filescan.castor.BodyType getBody() 

    /**
     * Returns the value of field 'header'.
     * 
     * @return the value of field 'header'.
     */
    public jacob.scheduler.system.filescan.castor.HeaderType getHeader()
    {
        return this._header;
    } //-- jacob.scheduler.system.filescan.castor.HeaderType getHeader() 

    /**
     * Returns the value of field 'ttl'.
     * 
     * @return the value of field 'ttl'.
     */
    public byte getTtl()
    {
        return this._ttl;
    } //-- byte getTtl() 

    /**
     * Method hasTtl
     */
    public boolean hasTtl()
    {
        return this._has_ttl;
    } //-- boolean hasTtl() 

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
     * Sets the value of field 'body'.
     * 
     * @param body the value of field 'body'.
     */
    public void setBody(jacob.scheduler.system.filescan.castor.BodyType body)
    {
        this._body = body;
    } //-- void setBody(jacob.scheduler.system.filescan.castor.BodyType) 

    /**
     * Sets the value of field 'header'.
     * 
     * @param header the value of field 'header'.
     */
    public void setHeader(jacob.scheduler.system.filescan.castor.HeaderType header)
    {
        this._header = header;
    } //-- void setHeader(jacob.scheduler.system.filescan.castor.HeaderType) 

    /**
     * Sets the value of field 'ttl'.
     * 
     * @param ttl the value of field 'ttl'.
     */
    public void setTtl(byte ttl)
    {
        this._ttl = ttl;
        this._has_ttl = true;
    } //-- void setTtl(byte) 

    /**
     * Method unmarshalTTSEnvelopeType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTTSEnvelopeType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.filescan.castor.TTSEnvelopeType) Unmarshaller.unmarshal(jacob.scheduler.system.filescan.castor.TTSEnvelopeType.class, reader);
    } //-- java.lang.Object unmarshalTTSEnvelopeType(java.io.Reader) 

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
