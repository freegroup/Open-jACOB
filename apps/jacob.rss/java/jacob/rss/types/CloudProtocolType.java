/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.rss.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class CloudProtocolType.
 * 
 * @version $Revision$ $Date$
 */
public class CloudProtocolType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The xml-rpc type
     */
    public static final int XML_RPC_TYPE = 0;

    /**
     * The instance of the xml-rpc type
     */
    public static final CloudProtocolType XML_RPC = new CloudProtocolType(XML_RPC_TYPE, "xml-rpc");

    /**
     * The soap type
     */
    public static final int SOAP_TYPE = 1;

    /**
     * The instance of the soap type
     */
    public static final CloudProtocolType SOAP = new CloudProtocolType(SOAP_TYPE, "soap");

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

    private CloudProtocolType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.rss.types.CloudProtocolType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CloudProtocolType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CloudProtocolType
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
        members.put("xml-rpc", XML_RPC);
        members.put("soap", SOAP);
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
     * CloudProtocolType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CloudProtocolType based on the
     * given String value.
     * 
     * @param string
     */
    public static jacob.rss.types.CloudProtocolType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CloudProtocolType";
            throw new IllegalArgumentException(err);
        }
        return (CloudProtocolType) obj;
    } //-- jacob.rss.types.CloudProtocolType valueOf(java.lang.String) 

}
