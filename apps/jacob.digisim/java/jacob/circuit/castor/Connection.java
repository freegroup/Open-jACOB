/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML
 * Schema.
 * $Id: Connection.java,v 1.1 2007/02/02 22:26:47 freegroup Exp $
 */

package jacob.circuit.castor;

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
 * Class Connection.
 * 
 * @version $Revision: 1.1 $ $Date: 2007/02/02 22:26:47 $
 */
public class Connection implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sourcePartId
     */
    private java.lang.String _sourcePartId;

    /**
     * Field _sourcePortId
     */
    private java.lang.String _sourcePortId;

    /**
     * Field _targetPartId
     */
    private java.lang.String _targetPartId;

    /**
     * Field _targetPortId
     */
    private java.lang.String _targetPortId;


      //----------------/
     //- Constructors -/
    //----------------/

    public Connection() {
        super();
    } //-- jacob.circuit.castor.Connection()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'sourcePartId'.
     * 
     * @return String
     * @return the value of field 'sourcePartId'.
     */
    public java.lang.String getSourcePartId()
    {
        return this._sourcePartId;
    } //-- java.lang.String getSourcePartId() 

    /**
     * Returns the value of field 'sourcePortId'.
     * 
     * @return String
     * @return the value of field 'sourcePortId'.
     */
    public java.lang.String getSourcePortId()
    {
        return this._sourcePortId;
    } //-- java.lang.String getSourcePortId() 

    /**
     * Returns the value of field 'targetPartId'.
     * 
     * @return String
     * @return the value of field 'targetPartId'.
     */
    public java.lang.String getTargetPartId()
    {
        return this._targetPartId;
    } //-- java.lang.String getTargetPartId() 

    /**
     * Returns the value of field 'targetPortId'.
     * 
     * @return String
     * @return the value of field 'targetPortId'.
     */
    public java.lang.String getTargetPortId()
    {
        return this._targetPortId;
    } //-- java.lang.String getTargetPortId() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
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
     * 
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
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'sourcePartId'.
     * 
     * @param sourcePartId the value of field 'sourcePartId'.
     */
    public void setSourcePartId(java.lang.String sourcePartId)
    {
        this._sourcePartId = sourcePartId;
    } //-- void setSourcePartId(java.lang.String) 

    /**
     * Sets the value of field 'sourcePortId'.
     * 
     * @param sourcePortId the value of field 'sourcePortId'.
     */
    public void setSourcePortId(java.lang.String sourcePortId)
    {
        this._sourcePortId = sourcePortId;
    } //-- void setSourcePortId(java.lang.String) 

    /**
     * Sets the value of field 'targetPartId'.
     * 
     * @param targetPartId the value of field 'targetPartId'.
     */
    public void setTargetPartId(java.lang.String targetPartId)
    {
        this._targetPartId = targetPartId;
    } //-- void setTargetPartId(java.lang.String) 

    /**
     * Sets the value of field 'targetPortId'.
     * 
     * @param targetPortId the value of field 'targetPortId'.
     */
    public void setTargetPortId(java.lang.String targetPortId)
    {
        this._targetPortId = targetPortId;
    } //-- void setTargetPortId(java.lang.String) 

    /**
     * Method unmarshalConnection
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalConnection(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.circuit.castor.Connection) Unmarshaller.unmarshal(jacob.circuit.castor.Connection.class, reader);
    } //-- java.lang.Object unmarshalConnection(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
