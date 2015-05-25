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
 * Class CastorDomainDataScopeType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorDomainDataScopeType implements java.io.Serializable {


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
    public static final CastorDomainDataScopeType GROUP = new CastorDomainDataScopeType(GROUP_TYPE, "group");

    /**
     * The form type
     */
    public static final int FORM_TYPE = 1;

    /**
     * The instance of the form type
     */
    public static final CastorDomainDataScopeType FORM = new CastorDomainDataScopeType(FORM_TYPE, "form");

    /**
     * The domain type
     */
    public static final int DOMAIN_TYPE = 2;

    /**
     * The instance of the domain type
     */
    public static final CastorDomainDataScopeType DOMAIN = new CastorDomainDataScopeType(DOMAIN_TYPE, "domain");

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

    private CastorDomainDataScopeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorDomainDataScopeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * CastorDomainDataScopeType
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
     * CastorDomainDataScopeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorDomainDataScopeType based
     * on the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorDomainDataScopeType";
            throw new IllegalArgumentException(err);
        }
        return (CastorDomainDataScopeType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType valueOf(java.lang.String) 

}
