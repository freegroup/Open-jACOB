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
 * Class FlowLayoutContainerOrientationType.
 * 
 * @version $Revision$ $Date$
 */
public class FlowLayoutContainerOrientationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The horizontal type
     */
    public static final int HORIZONTAL_TYPE = 0;

    /**
     * The instance of the horizontal type
     */
    public static final FlowLayoutContainerOrientationType HORIZONTAL = new FlowLayoutContainerOrientationType(HORIZONTAL_TYPE, "horizontal");

    /**
     * The vertical type
     */
    public static final int VERTICAL_TYPE = 1;

    /**
     * The instance of the vertical type
     */
    public static final FlowLayoutContainerOrientationType VERTICAL = new FlowLayoutContainerOrientationType(VERTICAL_TYPE, "vertical");

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

    private FlowLayoutContainerOrientationType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of FlowLayoutContainerOrientationType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * FlowLayoutContainerOrientationType
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
        members.put("horizontal", HORIZONTAL);
        members.put("vertical", VERTICAL);
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
     * FlowLayoutContainerOrientationType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new
     * FlowLayoutContainerOrientationType based on the given String
     * value.
     * 
     * @param string
     */
    public static de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid FlowLayoutContainerOrientationType";
            throw new IllegalArgumentException(err);
        }
        return (FlowLayoutContainerOrientationType) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType valueOf(java.lang.String) 

}
