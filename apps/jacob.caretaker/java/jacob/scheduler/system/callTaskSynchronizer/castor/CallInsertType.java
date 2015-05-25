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

import jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType;
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
 * Class CallInsertType.
 * 
 * @version $Revision$ $Date$
 */
public class CallInsertType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Zeitstempel des gewuenschten Inserts 
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
     * Staus der Gewaehrleistungsueberpruefung
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType _warrentystatus;

    /**
     * Prioritaet der Meldung
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType _priority;

    /**
     * Schluessel zum Gewerk
     */
    private long _categorycall;

    /**
     * keeps track of state for field: _categorycall
     */
    private boolean _has_categorycall;

    /**
     * Schluessel der T�tigkeit
     */
    private long _process_key;

    /**
     * keeps track of state for field: _process_key
     */
    private boolean _has_process_key;

    /**
     * Schluessel des Objektes in der EDVIN Instanz
     */
    private java.lang.String _objectID;

    /**
     * Name der EDVIN instanz ENDVIN/APA
     */
    private java.lang.String _instance;

    /**
     * Schluessel des AK
     */
    private long _workgroupcall;

    /**
     * keeps track of state for field: _workgroupcall
     */
    private boolean _has_workgroupcall;

    /**
     * Name des AKs
     */
    private java.lang.String _workgroup_name;

    /**
     * Schl�ssel zum Object
     */
    private long _object_key;

    /**
     * keeps track of state for field: _object_key
     */
    private boolean _has_object_key;

    /**
     * Schl�ssel zu Agent
     */
    private long _agentcall;

    /**
     * keeps track of state for field: _agentcall
     */
    private boolean _has_agentcall;

    /**
     * Schl�ssel zum Melder
     */
    private long _employeecall;

    /**
     * keeps track of state for field: _employeecall
     */
    private boolean _has_employeecall;

    /**
     * R�ckrufart an den Melder
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType _callbackmethod;

    /**
     * Langtextbeschreibung f�r Doku/Lsg
     */
    private java.lang.String _problemtext;

    /**
     * Ort nach Faplisnotation
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.Location _location;

    /**
     * Field _notslrelevant
     */
    private long _notslrelevant;

    /**
     * keeps track of state for field: _notslrelevant
     */
    private boolean _has_notslrelevant;


      //----------------/
     //- Constructors -/
    //----------------/

    public CallInsertType() {
        super();
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteAgentcall
     */
    public void deleteAgentcall()
    {
        this._has_agentcall= false;
    } //-- void deleteAgentcall() 

    /**
     * Method deleteCategorycall
     */
    public void deleteCategorycall()
    {
        this._has_categorycall= false;
    } //-- void deleteCategorycall() 

    /**
     * Method deleteEmployeecall
     */
    public void deleteEmployeecall()
    {
        this._has_employeecall= false;
    } //-- void deleteEmployeecall() 

    /**
     * Method deleteNotslrelevant
     */
    public void deleteNotslrelevant()
    {
        this._has_notslrelevant= false;
    } //-- void deleteNotslrelevant() 

    /**
     * Method deleteObject_key
     */
    public void deleteObject_key()
    {
        this._has_object_key= false;
    } //-- void deleteObject_key() 

    /**
     * Method deleteProcess_key
     */
    public void deleteProcess_key()
    {
        this._has_process_key= false;
    } //-- void deleteProcess_key() 

    /**
     * Method deleteWorkgroupcall
     */
    public void deleteWorkgroupcall()
    {
        this._has_workgroupcall= false;
    } //-- void deleteWorkgroupcall() 

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
     * Returns the value of field 'agentcall'. The field
     * 'agentcall' has the following description: Schl�ssel zu
     * Agent
     * 
     * @return the value of field 'agentcall'.
     */
    public long getAgentcall()
    {
        return this._agentcall;
    } //-- long getAgentcall() 

    /**
     * Returns the value of field 'callbackmethod'. The field
     * 'callbackmethod' has the following description: R�ckrufart
     * an den Melder
     * 
     * @return the value of field 'callbackmethod'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType getCallbackmethod()
    {
        return this._callbackmethod;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType getCallbackmethod() 

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
     * Returns the value of field 'categorycall'. The field
     * 'categorycall' has the following description: Schluessel zum
     * Gewerk
     * 
     * @return the value of field 'categorycall'.
     */
    public long getCategorycall()
    {
        return this._categorycall;
    } //-- long getCategorycall() 

    /**
     * Returns the value of field 'datemodified'. The field
     * 'datemodified' has the following description: Zeitstempel
     * des gewuenschten Inserts 
     * 
     * @return the value of field 'datemodified'.
     */
    public java.util.Date getDatemodified()
    {
        return this._datemodified;
    } //-- java.util.Date getDatemodified() 

    /**
     * Returns the value of field 'employeecall'. The field
     * 'employeecall' has the following description: Schl�ssel zum
     * Melder
     * 
     * @return the value of field 'employeecall'.
     */
    public long getEmployeecall()
    {
        return this._employeecall;
    } //-- long getEmployeecall() 

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
     * Returns the value of field 'instance'. The field 'instance'
     * has the following description: Name der EDVIN instanz
     * ENDVIN/APA
     * 
     * @return the value of field 'instance'.
     */
    public java.lang.String getInstance()
    {
        return this._instance;
    } //-- java.lang.String getInstance() 

    /**
     * Returns the value of field 'location'. The field 'location'
     * has the following description: Ort nach Faplisnotation
     * 
     * @return the value of field 'location'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.Location getLocation()
    {
        return this._location;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.Location getLocation() 

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
     * Returns the value of field 'objectID'. The field 'objectID'
     * has the following description: Schluessel des Objektes in
     * der EDVIN Instanz
     * 
     * @return the value of field 'objectID'.
     */
    public java.lang.String getObjectID()
    {
        return this._objectID;
    } //-- java.lang.String getObjectID() 

    /**
     * Returns the value of field 'object_key'. The field
     * 'object_key' has the following description: Schl�ssel zum
     * Object
     * 
     * @return the value of field 'object_key'.
     */
    public long getObject_key()
    {
        return this._object_key;
    } //-- long getObject_key() 

    /**
     * Returns the value of field 'priority'. The field 'priority'
     * has the following description: Prioritaet der Meldung
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
     * Returns the value of field 'problemtext'. The field
     * 'problemtext' has the following description:
     * Langtextbeschreibung f�r Doku/Lsg
     * 
     * @return the value of field 'problemtext'.
     */
    public java.lang.String getProblemtext()
    {
        return this._problemtext;
    } //-- java.lang.String getProblemtext() 

    /**
     * Returns the value of field 'process_key'. The field
     * 'process_key' has the following description: Schluessel der
     * T�tigkeit
     * 
     * @return the value of field 'process_key'.
     */
    public long getProcess_key()
    {
        return this._process_key;
    } //-- long getProcess_key() 

    /**
     * Returns the value of field 'warrentystatus'. The field
     * 'warrentystatus' has the following description: Staus der
     * Gewaehrleistungsueberpruefung
     * 
     * @return the value of field 'warrentystatus'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType getWarrentystatus()
    {
        return this._warrentystatus;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType getWarrentystatus() 

    /**
     * Returns the value of field 'workgroup_name'. The field
     * 'workgroup_name' has the following description: Name des AKs
     * 
     * @return the value of field 'workgroup_name'.
     */
    public java.lang.String getWorkgroup_name()
    {
        return this._workgroup_name;
    } //-- java.lang.String getWorkgroup_name() 

    /**
     * Returns the value of field 'workgroupcall'. The field
     * 'workgroupcall' has the following description: Schluessel
     * des AK
     * 
     * @return the value of field 'workgroupcall'.
     */
    public long getWorkgroupcall()
    {
        return this._workgroupcall;
    } //-- long getWorkgroupcall() 

    /**
     * Method hasAgentcall
     */
    public boolean hasAgentcall()
    {
        return this._has_agentcall;
    } //-- boolean hasAgentcall() 

    /**
     * Method hasCategorycall
     */
    public boolean hasCategorycall()
    {
        return this._has_categorycall;
    } //-- boolean hasCategorycall() 

    /**
     * Method hasEmployeecall
     */
    public boolean hasEmployeecall()
    {
        return this._has_employeecall;
    } //-- boolean hasEmployeecall() 

    /**
     * Method hasNotslrelevant
     */
    public boolean hasNotslrelevant()
    {
        return this._has_notslrelevant;
    } //-- boolean hasNotslrelevant() 

    /**
     * Method hasObject_key
     */
    public boolean hasObject_key()
    {
        return this._has_object_key;
    } //-- boolean hasObject_key() 

    /**
     * Method hasProcess_key
     */
    public boolean hasProcess_key()
    {
        return this._has_process_key;
    } //-- boolean hasProcess_key() 

    /**
     * Method hasWorkgroupcall
     */
    public boolean hasWorkgroupcall()
    {
        return this._has_workgroupcall;
    } //-- boolean hasWorkgroupcall() 

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
     * Sets the value of field 'agentcall'. The field 'agentcall'
     * has the following description: Schl�ssel zu Agent
     * 
     * @param agentcall the value of field 'agentcall'.
     */
    public void setAgentcall(long agentcall)
    {
        this._agentcall = agentcall;
        this._has_agentcall = true;
    } //-- void setAgentcall(long) 

    /**
     * Sets the value of field 'callbackmethod'. The field
     * 'callbackmethod' has the following description: R�ckrufart
     * an den Melder
     * 
     * @param callbackmethod the value of field 'callbackmethod'.
     */
    public void setCallbackmethod(jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType callbackmethod)
    {
        this._callbackmethod = callbackmethod;
    } //-- void setCallbackmethod(jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType) 

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
     * Sets the value of field 'categorycall'. The field
     * 'categorycall' has the following description: Schluessel zum
     * Gewerk
     * 
     * @param categorycall the value of field 'categorycall'.
     */
    public void setCategorycall(long categorycall)
    {
        this._categorycall = categorycall;
        this._has_categorycall = true;
    } //-- void setCategorycall(long) 

    /**
     * Sets the value of field 'datemodified'. The field
     * 'datemodified' has the following description: Zeitstempel
     * des gewuenschten Inserts 
     * 
     * @param datemodified the value of field 'datemodified'.
     */
    public void setDatemodified(java.util.Date datemodified)
    {
        this._datemodified = datemodified;
    } //-- void setDatemodified(java.util.Date) 

    /**
     * Sets the value of field 'employeecall'. The field
     * 'employeecall' has the following description: Schl�ssel zum
     * Melder
     * 
     * @param employeecall the value of field 'employeecall'.
     */
    public void setEmployeecall(long employeecall)
    {
        this._employeecall = employeecall;
        this._has_employeecall = true;
    } //-- void setEmployeecall(long) 

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
     * Sets the value of field 'instance'. The field 'instance' has
     * the following description: Name der EDVIN instanz ENDVIN/APA
     * 
     * @param instance the value of field 'instance'.
     */
    public void setInstance(java.lang.String instance)
    {
        this._instance = instance;
    } //-- void setInstance(java.lang.String) 

    /**
     * Sets the value of field 'location'. The field 'location' has
     * the following description: Ort nach Faplisnotation
     * 
     * @param location the value of field 'location'.
     */
    public void setLocation(jacob.scheduler.system.callTaskSynchronizer.castor.Location location)
    {
        this._location = location;
    } //-- void setLocation(jacob.scheduler.system.callTaskSynchronizer.castor.Location) 

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
     * Sets the value of field 'objectID'. The field 'objectID' has
     * the following description: Schluessel des Objektes in der
     * EDVIN Instanz
     * 
     * @param objectID the value of field 'objectID'.
     */
    public void setObjectID(java.lang.String objectID)
    {
        this._objectID = objectID;
    } //-- void setObjectID(java.lang.String) 

    /**
     * Sets the value of field 'object_key'. The field 'object_key'
     * has the following description: Schl�ssel zum Object
     * 
     * @param object_key the value of field 'object_key'.
     */
    public void setObject_key(long object_key)
    {
        this._object_key = object_key;
        this._has_object_key = true;
    } //-- void setObject_key(long) 

    /**
     * Sets the value of field 'priority'. The field 'priority' has
     * the following description: Prioritaet der Meldung
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
     * Sets the value of field 'problemtext'. The field
     * 'problemtext' has the following description:
     * Langtextbeschreibung f�r Doku/Lsg
     * 
     * @param problemtext the value of field 'problemtext'.
     */
    public void setProblemtext(java.lang.String problemtext)
    {
        this._problemtext = problemtext;
    } //-- void setProblemtext(java.lang.String) 

    /**
     * Sets the value of field 'process_key'. The field
     * 'process_key' has the following description: Schluessel der
     * T�tigkeit
     * 
     * @param process_key the value of field 'process_key'.
     */
    public void setProcess_key(long process_key)
    {
        this._process_key = process_key;
        this._has_process_key = true;
    } //-- void setProcess_key(long) 

    /**
     * Sets the value of field 'warrentystatus'. The field
     * 'warrentystatus' has the following description: Staus der
     * Gewaehrleistungsueberpruefung
     * 
     * @param warrentystatus the value of field 'warrentystatus'.
     */
    public void setWarrentystatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType warrentystatus)
    {
        this._warrentystatus = warrentystatus;
    } //-- void setWarrentystatus(jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType) 

    /**
     * Sets the value of field 'workgroup_name'. The field
     * 'workgroup_name' has the following description: Name des AKs
     * 
     * @param workgroup_name the value of field 'workgroup_name'.
     */
    public void setWorkgroup_name(java.lang.String workgroup_name)
    {
        this._workgroup_name = workgroup_name;
    } //-- void setWorkgroup_name(java.lang.String) 

    /**
     * Sets the value of field 'workgroupcall'. The field
     * 'workgroupcall' has the following description: Schluessel
     * des AK
     * 
     * @param workgroupcall the value of field 'workgroupcall'.
     */
    public void setWorkgroupcall(long workgroupcall)
    {
        this._workgroupcall = workgroupcall;
        this._has_workgroupcall = true;
    } //-- void setWorkgroupcall(long) 

    /**
     * Method unmarshalCallInsertType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCallInsertType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType) Unmarshaller.unmarshal(jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType.class, reader);
    } //-- java.lang.Object unmarshalCallInsertType(java.io.Reader) 

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
