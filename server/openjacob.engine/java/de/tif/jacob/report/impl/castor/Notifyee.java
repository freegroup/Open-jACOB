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
 * the user and the cron rule for an report 'news letter'. The
 * report will be send to the url (via YAN) with the cronRule time
 * intervall
 * 
 * @version $Revision$ $Date$
 */
public class Notifyee implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _omitEmpty
     */
    private boolean _omitEmpty = false;

    /**
     * keeps track of state for field: _omitEmpty
     */
    private boolean _has_omitEmpty;

    /**
     * The url for the report receiver. The url must start with the
     * protocol for the message channel
     */
    private java.lang.String _address;

    /**
     * The channel via the report has to bee send. The channel and
     * the address of the receiver must match. e.g.
     * email://a.herz@freegroup.de
     */
    private java.lang.String _protocol;

    /**
     * The internal id (key) of the notifyee.
     */
    private java.lang.String _userId;

    /**
     * The login id of the notifyee. If this element is missing,
     * the user is the owner.
     */
    private java.lang.String _userLoginId;

    /**
     * default mimeType is text/plain
     */
    private java.lang.String _mimeType = "text/plain";

    /**
     * Field _cronRule
     */
    private de.tif.jacob.report.impl.castor.CronRule _cronRule;


      //----------------/
     //- Constructors -/
    //----------------/

    public Notifyee() {
        super();
        setMimeType("text/plain");
    } //-- de.tif.jacob.report.impl.castor.Notifyee()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteOmitEmpty
     */
    public void deleteOmitEmpty()
    {
        this._has_omitEmpty= false;
    } //-- void deleteOmitEmpty() 

    /**
     * Returns the value of field 'address'. The field 'address'
     * has the following description: The url for the report
     * receiver. The url must start with the protocol for the
     * message channel
     * 
     * @return the value of field 'address'.
     */
    public java.lang.String getAddress()
    {
        return this._address;
    } //-- java.lang.String getAddress() 

    /**
     * Returns the value of field 'cronRule'.
     * 
     * @return the value of field 'cronRule'.
     */
    public de.tif.jacob.report.impl.castor.CronRule getCronRule()
    {
        return this._cronRule;
    } //-- de.tif.jacob.report.impl.castor.CronRule getCronRule() 

    /**
     * Returns the value of field 'mimeType'. The field 'mimeType'
     * has the following description: default mimeType is
     * text/plain
     * 
     * @return the value of field 'mimeType'.
     */
    public java.lang.String getMimeType()
    {
        return this._mimeType;
    } //-- java.lang.String getMimeType() 

    /**
     * Returns the value of field 'omitEmpty'.
     * 
     * @return the value of field 'omitEmpty'.
     */
    public boolean getOmitEmpty()
    {
        return this._omitEmpty;
    } //-- boolean getOmitEmpty() 

    /**
     * Returns the value of field 'protocol'. The field 'protocol'
     * has the following description: The channel via the report
     * has to bee send. The channel and the address of the receiver
     * must match. e.g. email://a.herz@freegroup.de
     * 
     * @return the value of field 'protocol'.
     */
    public java.lang.String getProtocol()
    {
        return this._protocol;
    } //-- java.lang.String getProtocol() 

    /**
     * Returns the value of field 'userId'. The field 'userId' has
     * the following description: The internal id (key) of the
     * notifyee.
     * 
     * @return the value of field 'userId'.
     */
    public java.lang.String getUserId()
    {
        return this._userId;
    } //-- java.lang.String getUserId() 

    /**
     * Returns the value of field 'userLoginId'. The field
     * 'userLoginId' has the following description: The login id of
     * the notifyee. If this element is missing, the user is the
     * owner.
     * 
     * @return the value of field 'userLoginId'.
     */
    public java.lang.String getUserLoginId()
    {
        return this._userLoginId;
    } //-- java.lang.String getUserLoginId() 

    /**
     * Method hasOmitEmpty
     */
    public boolean hasOmitEmpty()
    {
        return this._has_omitEmpty;
    } //-- boolean hasOmitEmpty() 

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
     * Sets the value of field 'address'. The field 'address' has
     * the following description: The url for the report receiver.
     * The url must start with the protocol for the message channel
     * 
     * @param address the value of field 'address'.
     */
    public void setAddress(java.lang.String address)
    {
        this._address = address;
    } //-- void setAddress(java.lang.String) 

    /**
     * Sets the value of field 'cronRule'.
     * 
     * @param cronRule the value of field 'cronRule'.
     */
    public void setCronRule(de.tif.jacob.report.impl.castor.CronRule cronRule)
    {
        this._cronRule = cronRule;
    } //-- void setCronRule(de.tif.jacob.report.impl.castor.CronRule) 

    /**
     * Sets the value of field 'mimeType'. The field 'mimeType' has
     * the following description: default mimeType is text/plain
     * 
     * @param mimeType the value of field 'mimeType'.
     */
    public void setMimeType(java.lang.String mimeType)
    {
        this._mimeType = mimeType;
    } //-- void setMimeType(java.lang.String) 

    /**
     * Sets the value of field 'omitEmpty'.
     * 
     * @param omitEmpty the value of field 'omitEmpty'.
     */
    public void setOmitEmpty(boolean omitEmpty)
    {
        this._omitEmpty = omitEmpty;
        this._has_omitEmpty = true;
    } //-- void setOmitEmpty(boolean) 

    /**
     * Sets the value of field 'protocol'. The field 'protocol' has
     * the following description: The channel via the report has to
     * bee send. The channel and the address of the receiver must
     * match. e.g. email://a.herz@freegroup.de
     * 
     * @param protocol the value of field 'protocol'.
     */
    public void setProtocol(java.lang.String protocol)
    {
        this._protocol = protocol;
    } //-- void setProtocol(java.lang.String) 

    /**
     * Sets the value of field 'userId'. The field 'userId' has the
     * following description: The internal id (key) of the
     * notifyee.
     * 
     * @param userId the value of field 'userId'.
     */
    public void setUserId(java.lang.String userId)
    {
        this._userId = userId;
    } //-- void setUserId(java.lang.String) 

    /**
     * Sets the value of field 'userLoginId'. The field
     * 'userLoginId' has the following description: The login id of
     * the notifyee. If this element is missing, the user is the
     * owner.
     * 
     * @param userLoginId the value of field 'userLoginId'.
     */
    public void setUserLoginId(java.lang.String userLoginId)
    {
        this._userLoginId = userLoginId;
    } //-- void setUserLoginId(java.lang.String) 

    /**
     * Method unmarshalNotifyee
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalNotifyee(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.Notifyee) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.Notifyee.class, reader);
    } //-- java.lang.Object unmarshalNotifyee(java.io.Reader) 

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
