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

import jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType;
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
 * Class TaskUpdateType.
 * 
 * @version $Revision$ $Date$
 */
public class TaskUpdateType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Prioritaet des Auftrags
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType _priority;

    /**
     * Beschreibung
     */
    private java.lang.String _summary;

    /**
     * Auftragsnummer
     */
    private java.lang.String _taskno;

    /**
     * Auftragsstatus
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType _taskstatus;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaskUpdateType() {
        super();
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'priority'. The field 'priority'
     * has the following description: Prioritaet des Auftrags
     * 
     * @return the value of field 'priority'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType getPriority()
    {
        return this._priority;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType getPriority() 

    /**
     * Returns the value of field 'summary'. The field 'summary'
     * has the following description: Beschreibung
     * 
     * @return the value of field 'summary'.
     */
    public java.lang.String getSummary()
    {
        return this._summary;
    } //-- java.lang.String getSummary() 

    /**
     * Returns the value of field 'taskno'. The field 'taskno' has
     * the following description: Auftragsnummer
     * 
     * @return the value of field 'taskno'.
     */
    public java.lang.String getTaskno()
    {
        return this._taskno;
    } //-- java.lang.String getTaskno() 

    /**
     * Returns the value of field 'taskstatus'. The field
     * 'taskstatus' has the following description: Auftragsstatus
     * 
     * @return the value of field 'taskstatus'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType getTaskstatus()
    {
        return this._taskstatus;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType getTaskstatus() 

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
     * Sets the value of field 'priority'. The field 'priority' has
     * the following description: Prioritaet des Auftrags
     * 
     * @param priority the value of field 'priority'.
     */
    public void setPriority(jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType priority)
    {
        this._priority = priority;
    } //-- void setPriority(jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType) 

    /**
     * Sets the value of field 'summary'. The field 'summary' has
     * the following description: Beschreibung
     * 
     * @param summary the value of field 'summary'.
     */
    public void setSummary(java.lang.String summary)
    {
        this._summary = summary;
    } //-- void setSummary(java.lang.String) 

    /**
     * Sets the value of field 'taskno'. The field 'taskno' has the
     * following description: Auftragsnummer
     * 
     * @param taskno the value of field 'taskno'.
     */
    public void setTaskno(java.lang.String taskno)
    {
        this._taskno = taskno;
    } //-- void setTaskno(java.lang.String) 

    /**
     * Sets the value of field 'taskstatus'. The field 'taskstatus'
     * has the following description: Auftragsstatus
     * 
     * @param taskstatus the value of field 'taskstatus'.
     */
    public void setTaskstatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType taskstatus)
    {
        this._taskstatus = taskstatus;
    } //-- void setTaskstatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType) 

    /**
     * Method unmarshalTaskUpdateType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTaskUpdateType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType) Unmarshaller.unmarshal(jacob.scheduler.system.callTaskSynchronizer.castor.TaskUpdateType.class, reader);
    } //-- java.lang.Object unmarshalTaskUpdateType(java.io.Reader) 

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
