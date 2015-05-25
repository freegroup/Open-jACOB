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
 * Class ActorSystemType.
 * 
 * @version $Revision$ $Date$
 */
public class ActorSystemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The KANA4 type
     */
    public static final int KANA4_TYPE = 0;

    /**
     * The instance of the KANA4 type
     */
    public static final ActorSystemType KANA4 = new ActorSystemType(KANA4_TYPE, "KANA4");

    /**
     * The EDVIN type
     */
    public static final int EDVIN_TYPE = 1;

    /**
     * The instance of the EDVIN type
     */
    public static final ActorSystemType EDVIN = new ActorSystemType(EDVIN_TYPE, "EDVIN");

    /**
     * The Warte type
     */
    public static final int WARTE_TYPE = 2;

    /**
     * The instance of the Warte type
     */
    public static final ActorSystemType WARTE = new ActorSystemType(WARTE_TYPE, "Warte");

    /**
     * The Generic type
     */
    public static final int GENERIC_TYPE = 3;

    /**
     * The instance of the Generic type
     */
    public static final ActorSystemType GENERIC = new ActorSystemType(GENERIC_TYPE, "Generic");

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

    private ActorSystemType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ActorSystemType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ActorSystemType
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
        members.put("KANA4", KANA4);
        members.put("EDVIN", EDVIN);
        members.put("Warte", WARTE);
        members.put("Generic", GENERIC);
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
     * ActorSystemType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ActorSystemType based on the
     * given String value.
     * 
     * @param string
     */
    public static jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ActorSystemType";
            throw new IllegalArgumentException(err);
        }
        return (ActorSystemType) obj;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType valueOf(java.lang.String) 

}
