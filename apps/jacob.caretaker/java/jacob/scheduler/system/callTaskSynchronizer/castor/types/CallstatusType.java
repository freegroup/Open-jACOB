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
 * Class CallstatusType.
 * 
 * @version $Revision$ $Date$
 */
public class CallstatusType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The R�ckruf type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the R�ckruf type
     */
    public static final CallstatusType VALUE_0 = new CallstatusType(VALUE_0_TYPE, "R�ckruf");

    /**
     * The Durchgestellt type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the Durchgestellt type
     */
    public static final CallstatusType VALUE_1 = new CallstatusType(VALUE_1_TYPE, "Durchgestellt");

    /**
     * The AK zugewiesen type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the AK zugewiesen type
     */
    public static final CallstatusType VALUE_2 = new CallstatusType(VALUE_2_TYPE, "AK zugewiesen");

    /**
     * The Fehlgeroutet type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the Fehlgeroutet type
     */
    public static final CallstatusType VALUE_3 = new CallstatusType(VALUE_3_TYPE, "Fehlgeroutet");

    /**
     * The Verworfen type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the Verworfen type
     */
    public static final CallstatusType VALUE_4 = new CallstatusType(VALUE_4_TYPE, "Verworfen");

    /**
     * The Angenommen type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the Angenommen type
     */
    public static final CallstatusType VALUE_5 = new CallstatusType(VALUE_5_TYPE, "Angenommen");

    /**
     * The Fertig gemeldet type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the Fertig gemeldet type
     */
    public static final CallstatusType VALUE_6 = new CallstatusType(VALUE_6_TYPE, "Fertig gemeldet");

    /**
     * The Fertig akzeptiert type
     */
    public static final int VALUE_7_TYPE = 7;

    /**
     * The instance of the Fertig akzeptiert type
     */
    public static final CallstatusType VALUE_7 = new CallstatusType(VALUE_7_TYPE, "Fertig akzeptiert");

    /**
     * The Dokumentiert type
     */
    public static final int VALUE_8_TYPE = 8;

    /**
     * The instance of the Dokumentiert type
     */
    public static final CallstatusType VALUE_8 = new CallstatusType(VALUE_8_TYPE, "Dokumentiert");

    /**
     * The Geschlossen type
     */
    public static final int VALUE_9_TYPE = 9;

    /**
     * The instance of the Geschlossen type
     */
    public static final CallstatusType VALUE_9 = new CallstatusType(VALUE_9_TYPE, "Geschlossen");

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

    private CallstatusType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CallstatusType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CallstatusType
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
        members.put("R�ckruf", VALUE_0);
        members.put("Durchgestellt", VALUE_1);
        members.put("AK zugewiesen", VALUE_2);
        members.put("Fehlgeroutet", VALUE_3);
        members.put("Verworfen", VALUE_4);
        members.put("Angenommen", VALUE_5);
        members.put("Fertig gemeldet", VALUE_6);
        members.put("Fertig akzeptiert", VALUE_7);
        members.put("Dokumentiert", VALUE_8);
        members.put("Geschlossen", VALUE_9);
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
     * CallstatusType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CallstatusType based on the
     * given String value.
     * 
     * @param string
     */
    public static jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CallstatusType";
            throw new IllegalArgumentException(err);
        }
        return (CallstatusType) obj;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType valueOf(java.lang.String) 

}
