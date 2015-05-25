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
 * Class CastorResizeMode.
 * 
 * @version $Revision$ $Date$
 */
public class CastorResizeMode implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The none type
     */
    public static final int NONE_TYPE = 0;

    /**
     * The instance of the none type
     */
    public static final CastorResizeMode NONE = new CastorResizeMode(NONE_TYPE, "none");

    /**
     * The booth type
     */
    public static final int BOOTH_TYPE = 1;

    /**
     * The instance of the booth type
     */
    public static final CastorResizeMode BOOTH = new CastorResizeMode(BOOTH_TYPE, "booth");

    /**
     * The width type
     */
    public static final int WIDTH_TYPE = 2;

    /**
     * The instance of the width type
     */
    public static final CastorResizeMode WIDTH = new CastorResizeMode(WIDTH_TYPE, "width");

    /**
     * The height type
     */
    public static final int HEIGHT_TYPE = 3;

    /**
     * The instance of the height type
     */
    public static final CastorResizeMode HEIGHT = new CastorResizeMode(HEIGHT_TYPE, "height");

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

    private CastorResizeMode(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorResizeMode
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CastorResizeMode
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
        members.put("none", NONE);
        members.put("booth", BOOTH);
        members.put("width", WIDTH);
        members.put("height", HEIGHT);
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
     * CastorResizeMode
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorResizeMode based on the
     * given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorResizeMode";
            throw new IllegalArgumentException(err);
        }
        return (CastorResizeMode) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode valueOf(java.lang.String) 

}
