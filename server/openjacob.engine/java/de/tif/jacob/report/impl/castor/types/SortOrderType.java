/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.report.impl.castor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SortOrderType.
 * 
 * @version $Revision$ $Date$
 */
public class SortOrderType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The NONE type
     */
    public static final int NONE_TYPE = 0;

    /**
     * The instance of the NONE type
     */
    public static final SortOrderType NONE = new SortOrderType(NONE_TYPE, "NONE");

    /**
     * The DESCENDING type
     */
    public static final int DESCENDING_TYPE = 1;

    /**
     * The instance of the DESCENDING type
     */
    public static final SortOrderType DESCENDING = new SortOrderType(DESCENDING_TYPE, "DESCENDING");

    /**
     * The ASCENDING type
     */
    public static final int ASCENDING_TYPE = 2;

    /**
     * The instance of the ASCENDING type
     */
    public static final SortOrderType ASCENDING = new SortOrderType(ASCENDING_TYPE, "ASCENDING");

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

    private SortOrderType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.report.impl.castor.types.SortOrderType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of SortOrderType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this SortOrderType
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
        members.put("NONE", NONE);
        members.put("DESCENDING", DESCENDING);
        members.put("ASCENDING", ASCENDING);
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
     * SortOrderType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new SortOrderType based on the given
     * String value.
     * 
     * @param string
     */
    public static de.tif.jacob.report.impl.castor.types.SortOrderType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid SortOrderType";
            throw new IllegalArgumentException(err);
        }
        return (SortOrderType) obj;
    } //-- de.tif.jacob.report.impl.castor.types.SortOrderType valueOf(java.lang.String) 

}
