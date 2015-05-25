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
 * Class TextFieldSearchModeType.
 * 
 * @version $Revision$ $Date$
 */
public class TextFieldSearchModeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The unbound type
     */
    public static final int UNBOUND_TYPE = 0;

    /**
     * The instance of the unbound type
     */
    public static final TextFieldSearchModeType UNBOUND = new TextFieldSearchModeType(UNBOUND_TYPE, "unbound");

    /**
     * The leftbound type
     */
    public static final int LEFTBOUND_TYPE = 1;

    /**
     * The instance of the leftbound type
     */
    public static final TextFieldSearchModeType LEFTBOUND = new TextFieldSearchModeType(LEFTBOUND_TYPE, "leftbound");

    /**
     * The rightbound type
     */
    public static final int RIGHTBOUND_TYPE = 2;

    /**
     * The instance of the rightbound type
     */
    public static final TextFieldSearchModeType RIGHTBOUND = new TextFieldSearchModeType(RIGHTBOUND_TYPE, "rightbound");

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

    private TextFieldSearchModeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of TextFieldSearchModeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this TextFieldSearchModeTyp
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
        members.put("unbound", UNBOUND);
        members.put("leftbound", LEFTBOUND);
        members.put("rightbound", RIGHTBOUND);
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
     * TextFieldSearchModeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new TextFieldSearchModeType based on
     * the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TextFieldSearchModeType";
            throw new IllegalArgumentException(err);
        }
        return (TextFieldSearchModeType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType valueOf(java.lang.String) 

}
