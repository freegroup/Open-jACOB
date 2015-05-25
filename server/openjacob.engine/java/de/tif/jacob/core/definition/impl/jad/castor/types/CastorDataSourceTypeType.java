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
 * Class CastorDataSourceTypeType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorDataSourceTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The sql type
     */
    public static final int SQL_TYPE = 0;

    /**
     * The instance of the sql type
     */
    public static final CastorDataSourceTypeType SQL = new CastorDataSourceTypeType(SQL_TYPE, "sql");

    /**
     * The index type
     */
    public static final int INDEX_TYPE = 1;

    /**
     * The instance of the index type
     */
    public static final CastorDataSourceTypeType INDEX = new CastorDataSourceTypeType(INDEX_TYPE, "index");

    /**
     * The directory type
     */
    public static final int DIRECTORY_TYPE = 2;

    /**
     * The instance of the directory type
     */
    public static final CastorDataSourceTypeType DIRECTORY = new CastorDataSourceTypeType(DIRECTORY_TYPE, "directory");

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

    private CastorDataSourceTypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceTypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorDataSourceTypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * CastorDataSourceTypeType
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
        members.put("sql", SQL);
        members.put("index", INDEX);
        members.put("directory", DIRECTORY);
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
     * CastorDataSourceTypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorDataSourceTypeType based
     * on the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceTypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorDataSourceTypeType";
            throw new IllegalArgumentException(err);
        }
        return (CastorDataSourceTypeType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceTypeType valueOf(java.lang.String) 

}
