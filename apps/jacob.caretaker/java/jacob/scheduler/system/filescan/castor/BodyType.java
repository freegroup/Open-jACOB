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
 * Class BodyType.
 * 
 * @version $Revision$ $Date$
 */
public class BodyType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _job
     */
    private jacob.scheduler.system.filescan.castor.JobType _job;


      //----------------/
     //- Constructors -/
    //----------------/

    public BodyType() {
        super();
    } //-- jacob.scheduler.system.filescan.castor.BodyType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'job'.
     * 
     * @return the value of field 'job'.
     */
    public jacob.scheduler.system.filescan.castor.JobType getJob()
    {
        return this._job;
    } //-- jacob.scheduler.system.filescan.castor.JobType getJob() 

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
     * Sets the value of field 'job'.
     * 
     * @param job the value of field 'job'.
     */
    public void setJob(jacob.scheduler.system.filescan.castor.JobType job)
    {
        this._job = job;
    } //-- void setJob(jacob.scheduler.system.filescan.castor.JobType) 

    /**
     * Method unmarshalBodyType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalBodyType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.filescan.castor.BodyType) Unmarshaller.unmarshal(jacob.scheduler.system.filescan.castor.BodyType.class, reader);
    } //-- java.lang.Object unmarshalBodyType(java.io.Reader) 

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
