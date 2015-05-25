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
 * Class WarrentystatusType.
 * 
 * @version $Revision$ $Date$
 */
public class WarrentystatusType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The �berpr�fen type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the �berpr�fen type
     */
    public static final WarrentystatusType VALUE_0 = new WarrentystatusType(VALUE_0_TYPE, "�berpr�fen");

    /**
     * The verfolgen type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the verfolgen type
     */
    public static final WarrentystatusType VALUE_1 = new WarrentystatusType(VALUE_1_TYPE, "verfolgen");

    /**
     * The nicht verfolgen type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the nicht verfolgen type
     */
    public static final WarrentystatusType VALUE_2 = new WarrentystatusType(VALUE_2_TYPE, "nicht verfolgen");

    /**
     * The wird verfolgt type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the wird verfolgt type
     */
    public static final WarrentystatusType VALUE_3 = new WarrentystatusType(VALUE_3_TYPE, "wird verfolgt");

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

    private WarrentystatusType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of WarrentystatusType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this WarrentystatusType
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
        members.put("�berpr�fen", VALUE_0);
        members.put("verfolgen", VALUE_1);
        members.put("nicht verfolgen", VALUE_2);
        members.put("wird verfolgt", VALUE_3);
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
     * WarrentystatusType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new WarrentystatusType based on the
     * given String value.
     * 
     * @param string
     */
    public static jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid WarrentystatusType";
            throw new IllegalArgumentException(err);
        }
        return (WarrentystatusType) obj;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType valueOf(java.lang.String) 

}
