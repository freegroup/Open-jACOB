/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.ruleengine.castor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class DecisionType.
 * 
 * @version $Revision$ $Date$
 */
public class DecisionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The boolean type
     */
    public static final int BOOLEAN_TYPE = 0;

    /**
     * The instance of the boolean type
     */
    public static final DecisionType BOOLEAN = new DecisionType(BOOLEAN_TYPE, "boolean");

    /**
     * The string type
     */
    public static final int STRING_TYPE = 1;

    /**
     * The instance of the string type
     */
    public static final DecisionType STRING = new DecisionType(STRING_TYPE, "string");

    /**
     * The enum type
     */
    public static final int ENUM_TYPE = 2;

    /**
     * The instance of the enum type
     */
    public static final DecisionType ENUM = new DecisionType(ENUM_TYPE, "enum");

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

    private DecisionType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.ruleengine.castor.types.DecisionType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * DecisionType
     * 
     * @return Enumeration
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getType
     * 
     * Returns the type of this DecisionType
     * 
     * @return int
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     * 
     * 
     * 
     * @return Hashtable
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("boolean", BOOLEAN);
        members.put("string", STRING);
        members.put("enum", ENUM);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve
     * 
     *  will be called during deserialization to replace the
     * deserialized object with the correct constant instance.
     * <br/>
     * 
     * @return Object
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

    /**
     * Method toString
     * 
     * Returns the String representation of this DecisionType
     * 
     * @return String
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOf
     * 
     * Returns a new DecisionType based on the given String value.
     * 
     * @param string
     * @return DecisionType
     */
    public static de.tif.jacob.ruleengine.castor.types.DecisionType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid DecisionType";
            throw new IllegalArgumentException(err);
        }
        return (DecisionType) obj;
    } //-- de.tif.jacob.ruleengine.castor.types.DecisionType valueOf(java.lang.String) 

}
