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
 * Class CastorFontFamilyType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorFontFamilyType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The serif type
     */
    public static final int SERIF_TYPE = 0;

    /**
     * The instance of the serif type
     */
    public static final CastorFontFamilyType SERIF = new CastorFontFamilyType(SERIF_TYPE, "serif");

    /**
     * The sans-serif type
     */
    public static final int SANS_SERIF_TYPE = 1;

    /**
     * The instance of the sans-serif type
     */
    public static final CastorFontFamilyType SANS_SERIF = new CastorFontFamilyType(SANS_SERIF_TYPE, "sans-serif");

    /**
     * The cursive type
     */
    public static final int CURSIVE_TYPE = 2;

    /**
     * The instance of the cursive type
     */
    public static final CastorFontFamilyType CURSIVE = new CastorFontFamilyType(CURSIVE_TYPE, "cursive");

    /**
     * The fantasy type
     */
    public static final int FANTASY_TYPE = 3;

    /**
     * The instance of the fantasy type
     */
    public static final CastorFontFamilyType FANTASY = new CastorFontFamilyType(FANTASY_TYPE, "fantasy");

    /**
     * The monospace type
     */
    public static final int MONOSPACE_TYPE = 4;

    /**
     * The instance of the monospace type
     */
    public static final CastorFontFamilyType MONOSPACE = new CastorFontFamilyType(MONOSPACE_TYPE, "monospace");

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

    private CastorFontFamilyType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorFontFamilyType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CastorFontFamilyType
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
        members.put("serif", SERIF);
        members.put("sans-serif", SANS_SERIF);
        members.put("cursive", CURSIVE);
        members.put("fantasy", FANTASY);
        members.put("monospace", MONOSPACE);
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
     * CastorFontFamilyType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorFontFamilyType based on
     * the given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorFontFamilyType";
            throw new IllegalArgumentException(err);
        }
        return (CastorFontFamilyType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType valueOf(java.lang.String) 

}
