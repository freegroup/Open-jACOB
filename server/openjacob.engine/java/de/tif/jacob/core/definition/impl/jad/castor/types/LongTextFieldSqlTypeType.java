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
 * Class LongTextFieldSqlTypeType.
 * 
 * @version $Revision$ $Date$
 */
public class LongTextFieldSqlTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The LONGVARCHAR type
     */
    public static final int LONGVARCHAR_TYPE = 0;

    /**
     * The instance of the LONGVARCHAR type
     */
    public static final LongTextFieldSqlTypeType LONGVARCHAR = new LongTextFieldSqlTypeType(LONGVARCHAR_TYPE, "LONGVARCHAR");

    /**
     * The CLOB type
     */
    public static final int CLOB_TYPE = 1;

    /**
     * The instance of the CLOB type
     */
    public static final LongTextFieldSqlTypeType CLOB = new LongTextFieldSqlTypeType(CLOB_TYPE, "CLOB");

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

    private LongTextFieldSqlTypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of LongTextFieldSqlTypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * LongTextFieldSqlTypeType
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
        members.put("LONGVARCHAR", LONGVARCHAR);
        members.put("CLOB", CLOB);
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
     * LongTextFieldSqlTypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new LongTextFieldSqlTypeType based
     * on the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LongTextFieldSqlTypeType";
            throw new IllegalArgumentException(err);
        }
        return (LongTextFieldSqlTypeType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType valueOf(java.lang.String) 

}
