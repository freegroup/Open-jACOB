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
 * Class PriorityType.
 * 
 * @version $Revision$ $Date$
 */
public class PriorityType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The 1-Normal type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the 1-Normal type
     */
    public static final PriorityType VALUE_0 = new PriorityType(VALUE_0_TYPE, "1-Normal");

    /**
     * The 2-Kritisch type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the 2-Kritisch type
     */
    public static final PriorityType VALUE_1 = new PriorityType(VALUE_1_TYPE, "2-Kritisch");

    /**
     * The 3-Produktion type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the 3-Produktion type
     */
    public static final PriorityType VALUE_2 = new PriorityType(VALUE_2_TYPE, "3-Produktion");

    /**
     * The 4-Notfall type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the 4-Notfall type
     */
    public static final PriorityType VALUE_3 = new PriorityType(VALUE_3_TYPE, "4-Notfall");

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

    private PriorityType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of PriorityType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this PriorityType
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
        members.put("1-Normal", VALUE_0);
        members.put("2-Kritisch", VALUE_1);
        members.put("3-Produktion", VALUE_2);
        members.put("4-Notfall", VALUE_3);
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
     * PriorityType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new PriorityType based on the given
     * String value.
     * 
     * @param string
     */
    public static jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid PriorityType";
            throw new IllegalArgumentException(err);
        }
        return (PriorityType) obj;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType valueOf(java.lang.String) 

}
