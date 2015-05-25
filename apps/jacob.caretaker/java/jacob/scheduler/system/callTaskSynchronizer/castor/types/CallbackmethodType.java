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
 * Class CallbackmethodType.
 * 
 * @version $Revision$ $Date$
 */
public class CallbackmethodType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Keine type
     */
    public static final int KEINE_TYPE = 0;

    /**
     * The instance of the Keine type
     */
    public static final CallbackmethodType KEINE = new CallbackmethodType(KEINE_TYPE, "Keine");

    /**
     * The SMS type
     */
    public static final int SMS_TYPE = 1;

    /**
     * The instance of the SMS type
     */
    public static final CallbackmethodType SMS = new CallbackmethodType(SMS_TYPE, "SMS");

    /**
     * The Telefon type
     */
    public static final int TELEFON_TYPE = 2;

    /**
     * The instance of the Telefon type
     */
    public static final CallbackmethodType TELEFON = new CallbackmethodType(TELEFON_TYPE, "Telefon");

    /**
     * The Email type
     */
    public static final int EMAIL_TYPE = 3;

    /**
     * The instance of the Email type
     */
    public static final CallbackmethodType EMAIL = new CallbackmethodType(EMAIL_TYPE, "Email");

    /**
     * The FAX type
     */
    public static final int FAX_TYPE = 4;

    /**
     * The instance of the FAX type
     */
    public static final CallbackmethodType FAX = new CallbackmethodType(FAX_TYPE, "FAX");

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

    private CallbackmethodType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CallbackmethodType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CallbackmethodType
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
        members.put("Keine", KEINE);
        members.put("SMS", SMS);
        members.put("Telefon", TELEFON);
        members.put("Email", EMAIL);
        members.put("FAX", FAX);
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
     * CallbackmethodType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CallbackmethodType based on the
     * given String value.
     * 
     * @param string
     */
    public static jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CallbackmethodType";
            throw new IllegalArgumentException(err);
        }
        return (CallbackmethodType) obj;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType valueOf(java.lang.String) 

}
