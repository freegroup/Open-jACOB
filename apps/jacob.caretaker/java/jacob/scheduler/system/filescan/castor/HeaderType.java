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
 * Class HeaderType.
 * 
 * @version $Revision$ $Date$
 */
public class HeaderType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _src
     */
    private jacob.scheduler.system.filescan.castor.EndpointTypeSrc _src;

    /**
     * Field _dest
     */
    private jacob.scheduler.system.filescan.castor.EndpointTypeDest _dest;


      //----------------/
     //- Constructors -/
    //----------------/

    public HeaderType() {
        super();
    } //-- jacob.scheduler.system.filescan.castor.HeaderType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dest'.
     * 
     * @return the value of field 'dest'.
     */
    public jacob.scheduler.system.filescan.castor.EndpointTypeDest getDest()
    {
        return this._dest;
    } //-- jacob.scheduler.system.filescan.castor.EndpointTypeDest getDest() 

    /**
     * Returns the value of field 'src'.
     * 
     * @return the value of field 'src'.
     */
    public jacob.scheduler.system.filescan.castor.EndpointTypeSrc getSrc()
    {
        return this._src;
    } //-- jacob.scheduler.system.filescan.castor.EndpointTypeSrc getSrc() 

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
     * Sets the value of field 'dest'.
     * 
     * @param dest the value of field 'dest'.
     */
    public void setDest(jacob.scheduler.system.filescan.castor.EndpointTypeDest dest)
    {
        this._dest = dest;
    } //-- void setDest(jacob.scheduler.system.filescan.castor.EndpointTypeDest) 

    /**
     * Sets the value of field 'src'.
     * 
     * @param src the value of field 'src'.
     */
    public void setSrc(jacob.scheduler.system.filescan.castor.EndpointTypeSrc src)
    {
        this._src = src;
    } //-- void setSrc(jacob.scheduler.system.filescan.castor.EndpointTypeSrc) 

    /**
     * Method unmarshalHeaderType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalHeaderType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.filescan.castor.HeaderType) Unmarshaller.unmarshal(jacob.scheduler.system.filescan.castor.HeaderType.class, reader);
    } //-- java.lang.Object unmarshalHeaderType(java.io.Reader) 

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
