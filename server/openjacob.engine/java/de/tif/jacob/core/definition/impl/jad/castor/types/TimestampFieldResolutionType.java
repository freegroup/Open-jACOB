/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.core.definition.impl.jad.castor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class TimestampFieldResolutionType.
 * 
 * @version $Revision$ $Date$
 */
public class TimestampFieldResolutionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The minbase type
     */
    public static final int MINBASE_TYPE = 0;

    /**
     * The instance of the minbase type
     */
    public static final TimestampFieldResolutionType MINBASE = new TimestampFieldResolutionType(MINBASE_TYPE, "minbase");

    /**
     * The secbase type
     */
    public static final int SECBASE_TYPE = 1;

    /**
     * The instance of the secbase type
     */
    public static final TimestampFieldResolutionType SECBASE = new TimestampFieldResolutionType(SECBASE_TYPE, "secbase");

    /**
     * The msecbase type
     */
    public static final int MSECBASE_TYPE = 2;

    /**
     * The instance of the msecbase type
     */
    public static final TimestampFieldResolutionType MSECBASE = new TimestampFieldResolutionType(MSECBASE_TYPE, "msecbase");

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

    private TimestampFieldResolutionType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of TimestampFieldResolutionType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * TimestampFieldResolutionType
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
        members.put("minbase", MINBASE);
        members.put("secbase", SECBASE);
        members.put("msecbase", MSECBASE);
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
     * TimestampFieldResolutionType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new TimestampFieldResolutionType
     * based on the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TimestampFieldResolutionType";
            throw new IllegalArgumentException(err);
        }
        return (TimestampFieldResolutionType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType valueOf(java.lang.String) 

}