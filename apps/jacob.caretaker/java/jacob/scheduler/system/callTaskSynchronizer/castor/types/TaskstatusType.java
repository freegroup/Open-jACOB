/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.scheduler.system.callTaskSynchronizer.castor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class TaskstatusType.
 * 
 * @version $Revision$ $Date$
 */
public class TaskstatusType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Neu type
     */
    public static final int NEU_TYPE = 0;

    /**
     * The instance of the Neu type
     */
    public static final TaskstatusType NEU = new TaskstatusType(NEU_TYPE, "Neu");

    /**
     * The Angelegt type
     */
    public static final int ANGELEGT_TYPE = 1;

    /**
     * The instance of the Angelegt type
     */
    public static final TaskstatusType ANGELEGT = new TaskstatusType(ANGELEGT_TYPE, "Angelegt");

    /**
     * The Freigegeben type
     */
    public static final int FREIGEGEBEN_TYPE = 2;

    /**
     * The instance of the Freigegeben type
     */
    public static final TaskstatusType FREIGEGEBEN = new TaskstatusType(FREIGEGEBEN_TYPE, "Freigegeben");

    /**
     * The Storniert type
     */
    public static final int STORNIERT_TYPE = 3;

    /**
     * The instance of the Storniert type
     */
    public static final TaskstatusType STORNIERT = new TaskstatusType(STORNIERT_TYPE, "Storniert");

    /**
     * The In Arbeit type
     */
    public static final int IN_ARBEIT_TYPE = 4;

    /**
     * The instance of the In Arbeit type
     */
    public static final TaskstatusType IN_ARBEIT = new TaskstatusType(IN_ARBEIT_TYPE, "In Arbeit");

    /**
     * The Fertig gemeldet type
     */
    public static final int FERTIG_GEMELDET_TYPE = 5;

    /**
     * The instance of the Fertig gemeldet type
     */
    public static final TaskstatusType FERTIG_GEMELDET = new TaskstatusType(FERTIG_GEMELDET_TYPE, "Fertig gemeldet");

    /**
     * The Dokumentiert type
     */
    public static final int DOKUMENTIERT_TYPE = 6;

    /**
     * The instance of the Dokumentiert type
     */
    public static final TaskstatusType DOKUMENTIERT = new TaskstatusType(DOKUMENTIERT_TYPE, "Dokumentiert");

    /**
     * The Abgerechnet type
     */
    public static final int ABGERECHNET_TYPE = 7;

    /**
     * The instance of the Abgerechnet type
     */
    public static final TaskstatusType ABGERECHNET = new TaskstatusType(ABGERECHNET_TYPE, "Abgerechnet");

    /**
     * The Abgeschlossen type
     */
    public static final int ABGESCHLOSSEN_TYPE = 8;

    /**
     * The instance of the Abgeschlossen type
     */
    public static final TaskstatusType ABGESCHLOSSEN = new TaskstatusType(ABGESCHLOSSEN_TYPE, "Abgeschlossen");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private TaskstatusType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of TaskstatusType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this TaskstatusType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("Neu", NEU);
        members.put("Angelegt", ANGELEGT);
        members.put("Freigegeben", FREIGEGEBEN);
        members.put("Storniert", STORNIERT);
        members.put("In Arbeit", IN_ARBEIT);
        members.put("Fertig gemeldet", FERTIG_GEMELDET);
        members.put("Dokumentiert", DOKUMENTIERT);
        members.put("Abgerechnet", ABGERECHNET);
        members.put("Abgeschlossen", ABGESCHLOSSEN);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance. <br/>
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

    /**
     * Method toStringReturns the String representation of this
     * TaskstatusType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new TaskstatusType based on the
     * given String value.
     * 
     * @param string
     */
    public static jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TaskstatusType";
            throw new IllegalArgumentException(err);
        }
        return (TaskstatusType) obj;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.TaskstatusType valueOf(java.lang.String) 

}
