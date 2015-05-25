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
 * Class EndpointTypeSrc.
 * 
 * @version $Revision$ $Date$
 */
public class EndpointTypeSrc implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _host
     */
    private java.lang.String _host;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _agent
     */
    private java.lang.String _agent;


      //----------------/
     //- Constructors -/
    //----------------/

    public EndpointTypeSrc() {
        super();
    } //-- jacob.scheduler.system.filescan.castor.EndpointTypeSrc()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'agent'.
     * 
     * @return the value of field 'agent'.
     */
    public java.lang.String getAgent()
    {
        return this._agent;
    } //-- java.lang.String getAgent() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'host'.
     * 
     * @return the value of field 'host'.
     */
    public java.lang.String getHost()
    {
        return this._host;
    } //-- java.lang.String getHost() 

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
     * Sets the value of field 'agent'.
     * 
     * @param agent the value of field 'agent'.
     */
    public void setAgent(java.lang.String agent)
    {
        this._agent = agent;
    } //-- void setAgent(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'host'.
     * 
     * @param host the value of field 'host'.
     */
    public void setHost(java.lang.String host)
    {
        this._host = host;
    } //-- void setHost(java.lang.String) 

    /**
     * Method unmarshalEndpointTypeSrc
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEndpointTypeSrc(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.filescan.castor.EndpointTypeSrc) Unmarshaller.unmarshal(jacob.scheduler.system.filescan.castor.EndpointTypeSrc.class, reader);
    } //-- java.lang.Object unmarshalEndpointTypeSrc(java.io.Reader) 

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
