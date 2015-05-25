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

import jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType;
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
 * Class Ttsjob.
 * 
 * @version $Revision$ $Date$
 */
public class Ttsjob implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _actorSystem
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType _actorSystem;

    /**
     * Field _job
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.Job _job;


      //----------------/
     //- Constructors -/
    //----------------/

    public Ttsjob() {
        super();
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.Ttsjob()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'actorSystem'.
     * 
     * @return the value of field 'actorSystem'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType getActorSystem()
    {
        return this._actorSystem;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType getActorSystem() 

    /**
     * Returns the value of field 'job'.
     * 
     * @return the value of field 'job'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.Job getJob()
    {
        return this._job;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.Job getJob() 

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
     * Sets the value of field 'actorSystem'.
     * 
     * @param actorSystem the value of field 'actorSystem'.
     */
    public void setActorSystem(jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType actorSystem)
    {
        this._actorSystem = actorSystem;
    } //-- void setActorSystem(jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType) 

    /**
     * Sets the value of field 'job'.
     * 
     * @param job the value of field 'job'.
     */
    public void setJob(jacob.scheduler.system.callTaskSynchronizer.castor.Job job)
    {
        this._job = job;
    } //-- void setJob(jacob.scheduler.system.callTaskSynchronizer.castor.Job) 

    /**
     * Method unmarshalTtsjob
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTtsjob(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.callTaskSynchronizer.castor.Ttsjob) Unmarshaller.unmarshal(jacob.scheduler.system.callTaskSynchronizer.castor.Ttsjob.class, reader);
    } //-- java.lang.Object unmarshalTtsjob(java.io.Reader) 

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
