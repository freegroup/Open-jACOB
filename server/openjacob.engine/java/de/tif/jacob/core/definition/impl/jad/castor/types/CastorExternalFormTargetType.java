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
 * Class CastorExternalFormTargetType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorExternalFormTargetType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Content Area type
     */
    public static final int CONTENT_AREA_TYPE = 0;

    /**
     * The instance of the Content Area type
     */
    public static final CastorExternalFormTargetType CONTENT_AREA = new CastorExternalFormTargetType(CONTENT_AREA_TYPE, "Content Area");

    /**
     * The New Window type
     */
    public static final int NEW_WINDOW_TYPE = 1;

    /**
     * The instance of the New Window type
     */
    public static final CastorExternalFormTargetType NEW_WINDOW = new CastorExternalFormTargetType(NEW_WINDOW_TYPE, "New Window");

    /**
     * The Current Window type
     */
    public static final int CURRENT_WINDOW_TYPE = 2;

    /**
     * The instance of the Current Window type
     */
    public static final CastorExternalFormTargetType CURRENT_WINDOW = new CastorExternalFormTargetType(CURRENT_WINDOW_TYPE, "Current Window");

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

    private CastorExternalFormTargetType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorExternalFormTargetType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorExternalFormTargetType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * CastorExternalFormTargetType
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
        members.put("Content Area", CONTENT_AREA);
        members.put("New Window", NEW_WINDOW);
        members.put("Current Window", CURRENT_WINDOW);
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
     * CastorExternalFormTargetType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorExternalFormTargetType
     * based on the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorExternalFormTargetType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorExternalFormTargetType";
            throw new IllegalArgumentException(err);
        }
        return (CastorExternalFormTargetType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorExternalFormTargetType valueOf(java.lang.String) 

}
