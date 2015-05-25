/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.scheduler.system.callTaskSynchronizer.castor;

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
 * Class Job.
 * 
 * @version $Revision$ $Date$
 */
public class Job implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _call
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.Call _call;

    /**
     * Field _task
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.Task _task;


      //----------------/
     //- Constructors -/
    //----------------/

    public Job() {
        super();
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.Job()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'call'.
     * 
     * @return the value of field 'call'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.Call getCall()
    {
        return this._call;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.Call getCall() 

    /**
     * Returns the value of field 'task'.
     * 
     * @return the value of field 'task'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.Task getTask()
    {
        return this._task;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.Task getTask() 

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
     * Sets the value of field 'call'.
     * 
     * @param call the value of field 'call'.
     */
    public void setCall(jacob.scheduler.system.callTaskSynchronizer.castor.Call call)
    {
        this._call = call;
    } //-- void setCall(jacob.scheduler.system.callTaskSynchronizer.castor.Call) 

    /**
     * Sets the value of field 'task'.
     * 
     * @param task the value of field 'task'.
     */
    public void setTask(jacob.scheduler.system.callTaskSynchronizer.castor.Task task)
    {
        this._task = task;
    } //-- void setTask(jacob.scheduler.system.callTaskSynchronizer.castor.Task) 

    /**
     * Method unmarshalJob
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalJob(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.callTaskSynchronizer.castor.Job) Unmarshaller.unmarshal(jacob.scheduler.system.callTaskSynchronizer.castor.Job.class, reader);
    } //-- java.lang.Object unmarshalJob(java.io.Reader) 

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
