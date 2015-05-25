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
 * Class CastorFilldirection.
 * 
 * @version $Revision$ $Date$
 */
public class CastorFilldirection implements java.io.Serializable {


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
    public static final CastorFilldirection NONE = new CastorFilldirection(NONE_TYPE, "none");

    /**
     * The both type
     */
    public static final int BOTH_TYPE = 1;

    /**
     * The instance of the both type
     */
    public static final CastorFilldirection BOTH = new CastorFilldirection(BOTH_TYPE, "both");

    /**
     * The backward type
     */
    public static final int BACKWARD_TYPE = 2;

    /**
     * The instance of the backward type
     */
    public static final CastorFilldirection BACKWARD = new CastorFilldirection(BACKWARD_TYPE, "backward");

    /**
     * The forward type
     */
    public static final int FORWARD_TYPE = 3;

    /**
     * The instance of the forward type
     */
    public static final CastorFilldirection FORWARD = new CastorFilldirection(FORWARD_TYPE, "forward");

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

    private CastorFilldirection(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CastorFilldirection
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CastorFilldirection
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
        members.put("both", BOTH);
        members.put("backward", BACKWARD);
        members.put("forward", FORWARD);
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
     * CastorFilldirection
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CastorFilldirection based on the
     * given String value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CastorFilldirection";
            throw new IllegalArgumentException(err);
        }
        return (CastorFilldirection) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection valueOf(java.lang.String) 

}
