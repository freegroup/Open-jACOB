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
 * Class Task.
 * 
 * @version $Revision$ $Date$
 */
public class Task implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _insert
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType _insert;

    /**
     * Field _update
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType _update;


      //----------------/
     //- Constructors -/
    //----------------/

    public Task() {
        super();
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.Task()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'insert'.
     * 
     * @return the value of field 'insert'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType getInsert()
    {
        return this._insert;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType getInsert() 

    /**
     * Returns the value of field 'update'.
     * 
     * @return the value of field 'update'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType getUpdate()
    {
        return this._update;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType getUpdate() 

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
     * Sets the value of field 'insert'.
     * 
     * @param insert the value of field 'insert'.
     */
    public void setInsert(jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType insert)
    {
        this._insert = insert;
    } //-- void setInsert(jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType) 

    /**
     * Sets the value of field 'update'.
     * 
     * @param update the value of field 'update'.
     */
    public void setUpdate(jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType update)
    {
        this._update = update;
    } //-- void setUpdate(jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType) 

    /**
     * Method unmarshalTask
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTask(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.callTaskSynchronizer.castor.Task) Unmarshaller.unmarshal(jacob.scheduler.system.callTaskSynchronizer.castor.Task.class, reader);
    } //-- java.lang.Object unmarshalTask(java.io.Reader) 

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
