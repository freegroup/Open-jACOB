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

import jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CallUpdateType.
 * 
 * @version $Revision$ $Date$
 */
public class CallUpdateType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Primary Key der Tabelle call 
     */
    private long _pkey;

    /**
     * keeps track of state for field: _pkey
     */
    private boolean _has_pkey;

    /**
     * Zeitstempel des gewuenschten Updates 
     */
    private java.util.Date _datemodified;

    /**
     * Schluessel im Fremdsystem
     */
    private java.lang.String _externalId;

    /**
     * Problembeschreibung der Meldung
     */
    private java.lang.String _problem;

    /**
     * Aktionstext in der Meldung 
     */
    private java.lang.String _action;

    /**
     * Staus der Meldung im TTS
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType _callstatus;

    /**
     * Staus der Gew�hrleistungs�berpr�fung
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType _warrentystatus;

    /**
     * Field _notslrelevant
     */
    private long _notslrelevant;

    /**
     * keeps track of state for field: _notslrelevant
     */
    private boolean _has_notslrelevant;

    /**
     * Priorität der Meldung
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType _priority;


      //----------------/
     //- Constructors -/
    //----------------/

    public CallUpdateType() {
        super();
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.CallUpdateType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteNotslrelevant
     */
    public void deleteNotslrelevant()
    {
        this._has_notslrelevant= false;
    } //-- void deleteNotslrelevant() 

    /**
     * Method deletePkey
     */
    public void deletePkey()
    {
        this._has_pkey= false;
    } //-- void deletePkey() 

    /**
     * Returns the value of field 'action'. The field 'action' has
     * the following description: Aktionstext in der Meldung 
     * 
     * @return the value of field 'action'.
     */
    public java.lang.String getAction()
    {
        return this._action;
    } //-- java.lang.String getAction() 

    /**
     * Returns the value of field 'callstatus'. The field
     * 'callstatus' has the following description: Staus der
     * Meldung im TTS
     * 
     * @return the value of field 'callstatus'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType getCallstatus()
    {
        return this._callstatus;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType getCallstatus() 

    /**
     * Returns the value of field 'datemodified'. The field
     * 'datemodified' has the following description: Zeitstempel
     * des gewuenschten Updates 
     * 
     * @return the value of field 'datemodified'.
     */
    public java.util.Date getDatemodified()
    {
        return this._datemodified;
    } //-- java.util.Date getDatemodified() 

    /**
     * Returns the value of field 'externalId'. The field
     * 'externalId' has the following description: Schluessel im
     * Fremdsystem
     * 
     * @return the value of field 'externalId'.
     */
    public java.lang.String getExternalId()
    {
        return this._externalId;
    } //-- java.lang.String getExternalId() 

    /**
     * Returns the value of field 'notslrelevant'.
     * 
     * @return the value of field 'notslrelevant'.
     */
    public long getNotslrelevant()
    {
        return this._notslrelevant;
    } //-- long getNotslrelevant() 

    /**
     * Returns the value of field 'pkey'. The field 'pkey' has the
     * following description: Primary Key der Tabelle call 
     * 
     * @return the value of field 'pkey'.
     */
    public long getPkey()
    {
        return this._pkey;
    } //-- long getPkey() 

    /**
     * Returns the value of field 'priority'. The field 'priority'
     * has the following description: Priorität der Meldung
     * 
     * @return the value of field 'priority'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType getPriority()
    {
        return this._priority;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType getPriority() 

    /**
     * Returns the value of field 'problem'. The field 'problem'
     * has the following description: Problembeschreibung der
     * Meldung
     * 
     * @return the value of field 'problem'.
     */
    public java.lang.String getProblem()
    {
        return this._problem;
    } //-- java.lang.String getProblem() 

    /**
     * Returns the value of field 'warrentystatus'. The field
     * 'warrentystatus' has the following description: Staus der
     * Gew�hrleistungs�berpr�fung
     * 
     * @return the value of field 'warrentystatus'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType getWarrentystatus()
    {
        return this._warrentystatus;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType getWarrentystatus() 

    /**
     * Method hasNotslrelevant
     */
    public boolean hasNotslrelevant()
    {
        return this._has_notslrelevant;
    } //-- boolean hasNotslrelevant() 

    /**
     * Method hasPkey
     */
    public boolean hasPkey()
    {
        return this._has_pkey;
    } //-- boolean hasPkey() 

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
     * Sets the value of field 'action'. The field 'action' has the
     * following description: Aktionstext in der Meldung 
     * 
     * @param action the value of field 'action'.
     */
    public void setAction(java.lang.String action)
    {
        this._action = action;
    } //-- void setAction(java.lang.String) 

    /**
     * Sets the value of field 'callstatus'. The field 'callstatus'
     * has the following description: Staus der Meldung im TTS
     * 
     * @param callstatus the value of field 'callstatus'.
     */
    public void setCallstatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType callstatus)
    {
        this._callstatus = callstatus;
    } //-- void setCallstatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType) 

    /**
     * Sets the value of field 'datemodified'. The field
     * 'datemodified' has the following description: Zeitstempel
     * des gewuenschten Updates 
     * 
     * @param datemodified the value of field 'datemodified'.
     */
    public void setDatemodified(java.util.Date datemodified)
    {
        this._datemodified = datemodified;
    } //-- void setDatemodified(java.util.Date) 

    /**
     * Sets the value of field 'externalId'. The field 'externalId'
     * has the following description: Schluessel im Fremdsystem
     * 
     * @param externalId the value of field 'externalId'.
     */
    public void setExternalId(java.lang.String externalId)
    {
        this._externalId = externalId;
    } //-- void setExternalId(java.lang.String) 

    /**
     * Sets the value of field 'notslrelevant'.
     * 
     * @param notslrelevant the value of field 'notslrelevant'.
     */
    public void setNotslrelevant(long notslrelevant)
    {
        this._notslrelevant = notslrelevant;
        this._has_notslrelevant = true;
    } //-- void setNotslrelevant(long) 

    /**
     * Sets the value of field 'pkey'. The field 'pkey' has the
     * following description: Primary Key der Tabelle call 
     * 
     * @param pkey the value of field 'pkey'.
     */
    public void setPkey(long pkey)
    {
        this._pkey = pkey;
        this._has_pkey = true;
    } //-- void setPkey(long) 

    /**
     * Sets the value of field 'priority'. The field 'priority' has
     * the following description: Priorität der Meldung
     * 
     * @param priority the value of field 'priority'.
     */
    public void setPriority(jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType priority)
    {
        this._priority = priority;
    } //-- void setPriority(jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType) 

    /**
     * Sets the value of field 'problem'. The field 'problem' has
     * the following description: Problembeschreibung der Meldung
     * 
     * @param problem the value of field 'problem'.
     */
    public void setProblem(java.lang.String problem)
    {
        this._problem = problem;
    } //-- void setProblem(java.lang.String) 

    /**
     * Sets the value of field 'warrentystatus'. The field
     * 'warrentystatus' has the following description: Staus der
     * Gew�hrleistungs�berpr�fung
     * 
     * @param warrentystatus the value of field 'warrentystatus'.
     */
    public void setWarrentystatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType warrentystatus)
    {
        this._warrentystatus = warrentystatus;
    } //-- void setWarrentystatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType) 

    /**
     * Method unmarshalCallUpdateType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCallUpdateType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.callTaskSynchronizer.castor.CallUpdateType) Unmarshaller.unmarshal(jacob.scheduler.system.callTaskSynchronizer.castor.CallUpdateType.class, reader);
    } //-- java.lang.Object unmarshalCallUpdateType(java.io.Reader) 

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
