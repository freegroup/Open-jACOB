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
 * Class CastorApplicationEventHandlerLookupMethodType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorApplicationEventHandlerLookupMethodType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The name type
     */
    public static final int NAME_TYPE = 0;

    /**
     * The instance of the name type
     */
    public static final CastorApplicationEventHandlerLookupMethodType NAME = new CastorApplicationEventHandlerLookupMethodType(NAME_TYPE, "name");

    /**
     * The reference type
     */
    public static final int REFERENCE_TYPE = 1;

    /**
     * The instance of the reference type
     */
    public static final CastorApplicationEventHandlerLookupMethodType REFERENCE = new CastorApplicationEventHandlerLookupMethodType(REFERENCE_TYPE, "reference");

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

    private CastorApplicationEventHandlerLookupMethodType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorApplicationEventHandlerLookupMethodType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * CastorApplicationEventHandlerLookupMethodType
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
        members.put("name", NAME);
        members.put("reference", REFERENCE);
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
     * CastorApplicationEventHandlerLookupMethodType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new
     * CastorApplicationEventHandlerLookupMethodType based on the
     * given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorApplicationEventHandlerLookupMethodType";
            throw new IllegalArgumentException(err);
        }
        return (CastorApplicationEventHandlerLookupMethodType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType valueOf(java.lang.String) 

}
