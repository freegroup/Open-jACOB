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
 * Class CastorVerticalAlignment.
 * 
 * @version $Revision$ $Date$
 */
public class CastorVerticalAlignment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The top type
     */
    public static final int TOP_TYPE = 0;

    /**
     * The instance of the top type
     */
    public static final CastorVerticalAlignment TOP = new CastorVerticalAlignment(TOP_TYPE, "top");

    /**
     * The middle type
     */
    public static final int MIDDLE_TYPE = 1;

    /**
     * The instance of the middle type
     */
    public static final CastorVerticalAlignment MIDDLE = new CastorVerticalAlignment(MIDDLE_TYPE, "middle");

    /**
     * The bottom type
     */
    public static final int BOTTOM_TYPE = 2;

    /**
     * The instance of the bottom type
     */
    public static final CastorVerticalAlignment BOTTOM = new CastorVerticalAlignment(BOTTOM_TYPE, "bottom");

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

    private CastorVerticalAlignment(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorVerticalAlignment
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CastorVerticalAlignmen
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
        members.put("top", TOP);
        members.put("middle", MIDDLE);
        members.put("bottom", BOTTOM);
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
     * CastorVerticalAlignment
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorVerticalAlignment based on
     * the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorVerticalAlignment";
            throw new IllegalArgumentException(err);
        }
        return (CastorVerticalAlignment) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment valueOf(java.lang.String) 

}
