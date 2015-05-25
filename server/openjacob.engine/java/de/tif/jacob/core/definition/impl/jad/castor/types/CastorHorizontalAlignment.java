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
 * Class CastorHorizontalAlignment.
 * 
 * @version $Revision$ $Date$
 */
public class CastorHorizontalAlignment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The left type
     */
    public static final int LEFT_TYPE = 0;

    /**
     * The instance of the left type
     */
    public static final CastorHorizontalAlignment LEFT = new CastorHorizontalAlignment(LEFT_TYPE, "left");

    /**
     * The center type
     */
    public static final int CENTER_TYPE = 1;

    /**
     * The instance of the center type
     */
    public static final CastorHorizontalAlignment CENTER = new CastorHorizontalAlignment(CENTER_TYPE, "center");

    /**
     * The right type
     */
    public static final int RIGHT_TYPE = 2;

    /**
     * The instance of the right type
     */
    public static final CastorHorizontalAlignment RIGHT = new CastorHorizontalAlignment(RIGHT_TYPE, "right");

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

    private CastorHorizontalAlignment(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorHorizontalAlignment
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * CastorHorizontalAlignment
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
        members.put("left", LEFT);
        members.put("center", CENTER);
        members.put("right", RIGHT);
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
     * CastorHorizontalAlignment
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorHorizontalAlignment based
     * on the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorHorizontalAlignment";
            throw new IllegalArgumentException(err);
        }
        return (CastorHorizontalAlignment) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment valueOf(java.lang.String) 

}
