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
 * Class CastorApplicationDataScopeType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorApplicationDataScopeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The group type
     */
    public static final int GROUP_TYPE = 0;

    /**
     * The instance of the group type
     */
    public static final CastorApplicationDataScopeType GROUP = new CastorApplicationDataScopeType(GROUP_TYPE, "group");

    /**
     * The form type
     */
    public static final int FORM_TYPE = 1;

    /**
     * The instance of the form type
     */
    public static final CastorApplicationDataScopeType FORM = new CastorApplicationDataScopeType(FORM_TYPE, "form");

    /**
     * The domain type
     */
    public static final int DOMAIN_TYPE = 2;

    /**
     * The instance of the domain type
     */
    public static final CastorApplicationDataScopeType DOMAIN = new CastorApplicationDataScopeType(DOMAIN_TYPE, "domain");

    /**
     * The application type
     */
    public static final int APPLICATION_TYPE = 3;

    /**
     * The instance of the application type
     */
    public static final CastorApplicationDataScopeType APPLICATION = new CastorApplicationDataScopeType(APPLICATION_TYPE, "application");

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

    private CastorApplicationDataScopeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorApplicationDataScopeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * CastorApplicationDataScopeType
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
        members.put("group", GROUP);
        members.put("form", FORM);
        members.put("domain", DOMAIN);
        members.put("application", APPLICATION);
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
     * CastorApplicationDataScopeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorApplicationDataScopeType
     * based on the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorApplicationDataScopeType";
            throw new IllegalArgumentException(err);
        }
        return (CastorApplicationDataScopeType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationDataScopeType valueOf(java.lang.String) 

}
