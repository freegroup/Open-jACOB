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
 * Class LongTextEditModeType.
 * 
 * @version $Revision$ $Date$
 */
public class LongTextEditModeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The append type
     */
    public static final int APPEND_TYPE = 0;

    /**
     * The instance of the append type
     */
    public static final LongTextEditModeType APPEND = new LongTextEditModeType(APPEND_TYPE, "append");

    /**
     * The prepend type
     */
    public static final int PREPEND_TYPE = 1;

    /**
     * The instance of the prepend type
     */
    public static final LongTextEditModeType PREPEND = new LongTextEditModeType(PREPEND_TYPE, "prepend");

    /**
     * The fulledit type
     */
    public static final int FULLEDIT_TYPE = 2;

    /**
     * The instance of the fulledit type
     */
    public static final LongTextEditModeType FULLEDIT = new LongTextEditModeType(FULLEDIT_TYPE, "fulledit");

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

    private LongTextEditModeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of LongTextEditModeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this LongTextEditModeType
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
        members.put("append", APPEND);
        members.put("prepend", PREPEND);
        members.put("fulledit", FULLEDIT);
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
     * LongTextEditModeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new LongTextEditModeType based on
     * the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LongTextEditModeType";
            throw new IllegalArgumentException(err);
        }
        return (LongTextEditModeType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType valueOf(java.lang.String) 

}
