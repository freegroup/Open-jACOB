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

import jacob.scheduler.system.filescan.castor.types.ActivityTypeEnum;
import jacob.scheduler.system.filescan.castor.types.EventTypeEnum;
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
 * Class JobType.
 * 
 * @version $Revision$ $Date$
 */
public class JobType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _event
     */
    private jacob.scheduler.system.filescan.castor.types.EventTypeEnum _event;

    /**
     * Field _objectID
     */
    private java.lang.String _objectID;

    /**
     * Field _instance
     */
    private java.lang.String _instance;

    /**
     * Field _maintenanceGroup
     */
    private java.lang.String _maintenanceGroup;

    /**
     * Field _timeStamp
     */
    private java.util.Date _timeStamp;

    /**
     * Field _messageText
     */
    private java.lang.String _messageText;

    /**
     * Field _activity
     */
    private jacob.scheduler.system.filescan.castor.types.ActivityTypeEnum _activity;


      //----------------/
     //- Constructors -/
    //----------------/

    public JobType() {
        super();
    } //-- jacob.scheduler.system.filescan.castor.JobType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'activity'.
     * 
     * @return the value of field 'activity'.
     */
    public jacob.scheduler.system.filescan.castor.types.ActivityTypeEnum getActivity()
    {
        return this._activity;
    } //-- jacob.scheduler.system.filescan.castor.types.ActivityTypeEnum getActivity() 

    /**
     * Returns the value of field 'event'.
     * 
     * @return the value of field 'event'.
     */
    public jacob.scheduler.system.filescan.castor.types.EventTypeEnum getEvent()
    {
        return this._event;
    } //-- jacob.scheduler.system.filescan.castor.types.EventTypeEnum getEvent() 

    /**
     * Returns the value of field 'instance'.
     * 
     * @return the value of field 'instance'.
     */
    public java.lang.String getInstance()
    {
        return this._instance;
    } //-- java.lang.String getInstance() 

    /**
     * Returns the value of field 'maintenanceGroup'.
     * 
     * @return the value of field 'maintenanceGroup'.
     */
    public java.lang.String getMaintenanceGroup()
    {
        return this._maintenanceGroup;
    } //-- java.lang.String getMaintenanceGroup() 

    /**
     * Returns the value of field 'messageText'.
     * 
     * @return the value of field 'messageText'.
     */
    public java.lang.String getMessageText()
    {
        return this._messageText;
    } //-- java.lang.String getMessageText() 

    /**
     * Returns the value of field 'objectID'.
     * 
     * @return the value of field 'objectID'.
     */
    public java.lang.String getObjectID()
    {
        return this._objectID;
    } //-- java.lang.String getObjectID() 

    /**
     * Returns the value of field 'timeStamp'.
     * 
     * @return the value of field 'timeStamp'.
     */
    public java.util.Date getTimeStamp()
    {
        return this._timeStamp;
    } //-- java.util.Date getTimeStamp() 

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
     * Sets the value of field 'activity'.
     * 
     * @param activity the value of field 'activity'.
     */
    public void setActivity(jacob.scheduler.system.filescan.castor.types.ActivityTypeEnum activity)
    {
        this._activity = activity;
    } //-- void setActivity(jacob.scheduler.system.filescan.castor.types.ActivityTypeEnum) 

    /**
     * Sets the value of field 'event'.
     * 
     * @param event the value of field 'event'.
     */
    public void setEvent(jacob.scheduler.system.filescan.castor.types.EventTypeEnum event)
    {
        this._event = event;
    } //-- void setEvent(jacob.scheduler.system.filescan.castor.types.EventTypeEnum) 

    /**
     * Sets the value of field 'instance'.
     * 
     * @param instance the value of field 'instance'.
     */
    public void setInstance(java.lang.String instance)
    {
        this._instance = instance;
    } //-- void setInstance(java.lang.String) 

    /**
     * Sets the value of field 'maintenanceGroup'.
     * 
     * @param maintenanceGroup the value of field 'maintenanceGroup'
     */
    public void setMaintenanceGroup(java.lang.String maintenanceGroup)
    {
        this._maintenanceGroup = maintenanceGroup;
    } //-- void setMaintenanceGroup(java.lang.String) 

    /**
     * Sets the value of field 'messageText'.
     * 
     * @param messageText the value of field 'messageText'.
     */
    public void setMessageText(java.lang.String messageText)
    {
        this._messageText = messageText;
    } //-- void setMessageText(java.lang.String) 

    /**
     * Sets the value of field 'objectID'.
     * 
     * @param objectID the value of field 'objectID'.
     */
    public void setObjectID(java.lang.String objectID)
    {
        this._objectID = objectID;
    } //-- void setObjectID(java.lang.String) 

    /**
     * Sets the value of field 'timeStamp'.
     * 
     * @param timeStamp the value of field 'timeStamp'.
     */
    public void setTimeStamp(java.util.Date timeStamp)
    {
        this._timeStamp = timeStamp;
    } //-- void setTimeStamp(java.util.Date) 

    /**
     * Method unmarshalJobType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalJobType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.filescan.castor.JobType) Unmarshaller.unmarshal(jacob.scheduler.system.filescan.castor.JobType.class, reader);
    } //-- java.lang.Object unmarshalJobType(java.io.Reader) 

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
