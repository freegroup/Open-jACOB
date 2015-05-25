/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.scheduler.system.filescan.castor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class EventTypeEnum.
 * 
 * @version $Revision$ $Date$
 */
public class EventTypeEnum implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The come type
     */
    public static final int COME_TYPE = 0;

    /**
     * The instance of the come type
     */
    public static final EventTypeEnum COME = new EventTypeEnum(COME_TYPE, "come");

    /**
     * The gone type
     */
    public static final int GONE_TYPE = 1;

    /**
     * The instance of the gone type
     */
    public static final EventTypeEnum GONE = new EventTypeEnum(GONE_TYPE, "gone");

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

    private EventTypeEnum(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.scheduler.system.filescan.castor.types.EventTypeEnum(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of EventTypeEnum
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this EventTypeEnum
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
        members.put("come", COME);
        members.put("gone", GONE);
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
     * EventTypeEnum
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new EventTypeEnum based on the given
     * String value.
     * 
     * @param string
     */
    public static jacob.scheduler.system.filescan.castor.types.EventTypeEnum valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid EventTypeEnum";
            throw new IllegalArgumentException(err);
        }
        return (EventTypeEnum) obj;
    } //-- jacob.scheduler.system.filescan.castor.types.EventTypeEnum valueOf(java.lang.String) 

}
