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
 * Class CastorFontWeightType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorFontWeightType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The normal type
     */
    public static final int NORMAL_TYPE = 0;

    /**
     * The instance of the normal type
     */
    public static final CastorFontWeightType NORMAL = new CastorFontWeightType(NORMAL_TYPE, "normal");

    /**
     * The bold type
     */
    public static final int BOLD_TYPE = 1;

    /**
     * The instance of the bold type
     */
    public static final CastorFontWeightType BOLD = new CastorFontWeightType(BOLD_TYPE, "bold");

    /**
     * The bolder type
     */
    public static final int BOLDER_TYPE = 2;

    /**
     * The instance of the bolder type
     */
    public static final CastorFontWeightType BOLDER = new CastorFontWeightType(BOLDER_TYPE, "bolder");

    /**
     * The lighter type
     */
    public static final int LIGHTER_TYPE = 3;

    /**
     * The instance of the lighter type
     */
    public static final CastorFontWeightType LIGHTER = new CastorFontWeightType(LIGHTER_TYPE, "lighter");

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

    private CastorFontWeightType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.report.impl.castor.types.CastorFontWeightType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorFontWeightType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CastorFontWeightType
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
        members.put("normal", NORMAL);
        members.put("bold", BOLD);
        members.put("bolder", BOLDER);
        members.put("lighter", LIGHTER);
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
     * CastorFontWeightType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorFontWeightType based on
     * the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.report.impl.castor.types.CastorFontWeightType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorFontWeightType";
            throw new IllegalArgumentException(err);
        }
        return (CastorFontWeightType) obj;
    } //-- de.tif.jacob.report.impl.castor.types.CastorFontWeightType valueOf(java.lang.String) 

}
