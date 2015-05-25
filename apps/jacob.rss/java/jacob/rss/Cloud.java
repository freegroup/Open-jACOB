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

import jacob.rss.types.CloudProtocolType;
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
 * Class Cloud.
 * 
 * @version $Revision$ $Date$
 */
public class Cloud implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _domain
     */
    private java.lang.Object _domain;

    /**
     * Field _port
     */
    private java.lang.Object _port;

    /**
     * Field _path
     */
    private java.lang.Object _path;

    /**
     * Field _registerProcedure
     */
    private java.lang.Object _registerProcedure;

    /**
     * Field _protocol
     */
    private jacob.rss.types.CloudProtocolType _protocol;


      //----------------/
     //- Constructors -/
    //----------------/

    public Cloud() {
        super();
    } //-- jacob.rss.Cloud()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'domain'.
     * 
     * @return the value of field 'domain'.
     */
    public java.lang.Object getDomain()
    {
        return this._domain;
    } //-- java.lang.Object getDomain() 

    /**
     * Returns the value of field 'path'.
     * 
     * @return the value of field 'path'.
     */
    public java.lang.Object getPath()
    {
        return this._path;
    } //-- java.lang.Object getPath() 

    /**
     * Returns the value of field 'port'.
     * 
     * @return the value of field 'port'.
     */
    public java.lang.Object getPort()
    {
        return this._port;
    } //-- java.lang.Object getPort() 

    /**
     * Returns the value of field 'protocol'.
     * 
     * @return the value of field 'protocol'.
     */
    public jacob.rss.types.CloudProtocolType getProtocol()
    {
        return this._protocol;
    } //-- jacob.rss.types.CloudProtocolType getProtocol() 

    /**
     * Returns the value of field 'registerProcedure'.
     * 
     * @return the value of field 'registerProcedure'.
     */
    public java.lang.Object getRegisterProcedure()
    {
        return this._registerProcedure;
    } //-- java.lang.Object getRegisterProcedure() 

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
     * Sets the value of field 'domain'.
     * 
     * @param domain the value of field 'domain'.
     */
    public void setDomain(java.lang.Object domain)
    {
        this._domain = domain;
    } //-- void setDomain(java.lang.Object) 

    /**
     * Sets the value of field 'path'.
     * 
     * @param path the value of field 'path'.
     */
    public void setPath(java.lang.Object path)
    {
        this._path = path;
    } //-- void setPath(java.lang.Object) 

    /**
     * Sets the value of field 'port'.
     * 
     * @param port the value of field 'port'.
     */
    public void setPort(java.lang.Object port)
    {
        this._port = port;
    } //-- void setPort(java.lang.Object) 

    /**
     * Sets the value of field 'protocol'.
     * 
     * @param protocol the value of field 'protocol'.
     */
    public void setProtocol(jacob.rss.types.CloudProtocolType protocol)
    {
        this._protocol = protocol;
    } //-- void setProtocol(jacob.rss.types.CloudProtocolType) 

    /**
     * Sets the value of field 'registerProcedure'.
     * 
     * @param registerProcedure the value of field
     * 'registerProcedure'.
     */
    public void setRegisterProcedure(java.lang.Object registerProcedure)
    {
        this._registerProcedure = registerProcedure;
    } //-- void setRegisterProcedure(java.lang.Object) 

    /**
     * Method unmarshalCloud
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCloud(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.rss.Cloud) Unmarshaller.unmarshal(jacob.rss.Cloud.class, reader);
    } //-- java.lang.Object unmarshalCloud(java.io.Reader) 

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
