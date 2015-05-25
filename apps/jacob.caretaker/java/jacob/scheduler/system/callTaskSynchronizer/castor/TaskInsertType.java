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
 * Class TaskInsertType.
 * 
 * @version $Revision$ $Date$
 */
public class TaskInsertType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Prioritaet des Auftrages
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType _priority;

    /**
     * Beschreibung
     */
    private java.lang.String _summary;

    /**
     * Status des Auftrags
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType _taskstatus;

    /**
     * notwendige Meldung fuer den Auftrag
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType _callinsert;


      //----------------/
     //- Constructors -/
    //----------------/

    public TaskInsertType() {
        super();
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'callinsert'. The field
     * 'callinsert' has the following description: notwendige
     * Meldung fuer den Auftrag
     * 
     * @return the value of field 'callinsert'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType getCallinsert()
    {
        return this._callinsert;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType getCallinsert() 

    /**
     * Returns the value of field 'priority'. The field 'priority'
     * has the following description: Prioritaet des Auftrages
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
     * Returns the value of field 'taskstatus'. The field
     * 'taskstatus' has the following description: Status des
     * Auftrags
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
     * Sets the value of field 'callinsert'. The field 'callinsert'
     * has the following description: notwendige Meldung fuer den
     * Auftrag
     * 
     * @param callinsert the value of field 'callinsert'.
     */
    public void setCallinsert(jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType callinsert)
    {
        this._callinsert = callinsert;
    } //-- void setCallinsert(jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType) 

    /**
     * Sets the value of field 'priority'. The field 'priority' has
     * the following description: Prioritaet des Auftrages
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
     * Sets the value of field 'taskstatus'. The field 'taskstatus'
     * has the following description: Status des Auftrags
     * 
     * @param taskstatus the value of field 'taskstatus'.
     */
    public void setTaskstatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType taskstatus)
    {
        this._taskstatus = taskstatus;
    } //-- void setTaskstatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType) 

    /**
     * Method unmarshalTaskInsertType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTaskInsertType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType) Unmarshaller.unmarshal(jacob.scheduler.system.callTaskSynchronizer.castor.TaskInsertType.class, reader);
    } //-- java.lang.Object unmarshalTaskInsertType(java.io.Reader) 

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
