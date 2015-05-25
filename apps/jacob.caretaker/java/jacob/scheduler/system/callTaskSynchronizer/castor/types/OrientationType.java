/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.scheduler.system.callTaskSynchronizer.castor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class OrientationType.
 * 
 * @version $Revision$ $Date$
 */
public class OrientationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The N type
     */
    public static final int N_TYPE = 0;

    /**
     * The instance of the N type
     */
    public static final OrientationType N = new OrientationType(N_TYPE, "N");

    /**
     * The NO type
     */
    public static final int NO_TYPE = 1;

    /**
     * The instance of the NO type
     */
    public static final OrientationType NO = new OrientationType(NO_TYPE, "NO");

    /**
     * The O type
     */
    public static final int O_TYPE = 2;

    /**
     * The instance of the O type
     */
    public static final OrientationType O = new OrientationType(O_TYPE, "O");

    /**
     * The SO type
     */
    public static final int SO_TYPE = 3;

    /**
     * The instance of the SO type
     */
    public static final OrientationType SO = new OrientationType(SO_TYPE, "SO");

    /**
     * The S type
     */
    public static final int S_TYPE = 4;

    /**
     * The instance of the S type
     */
    public static final OrientationType S = new OrientationType(S_TYPE, "S");

    /**
     * The SW type
     */
    public static final int SW_TYPE = 5;

    /**
     * The instance of the SW type
     */
    public static final OrientationType SW = new OrientationType(SW_TYPE, "SW");

    /**
     * The W type
     */
    public static final int W_TYPE = 6;

    /**
     * The instance of the W type
     */
    public static final OrientationType W = new OrientationType(W_TYPE, "W");

    /**
     * The NW type
     */
    public static final int NW_TYPE = 7;

    /**
     * The instance of the NW type
     */
    public static final OrientationType NW = new OrientationType(NW_TYPE, "NW");

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

    private OrientationType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of OrientationType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this OrientationType
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
        members.put("N", N);
        members.put("NO", NO);
        members.put("O", O);
        members.put("SO", SO);
        members.put("S", S);
        members.put("SW", SW);
        members.put("W", W);
        members.put("NW", NW);
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
     * OrientationType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new OrientationType based on the
     * given String value.
     * 
     * @param string
     */
    public static jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid OrientationType";
            throw new IllegalArgumentException(err);
        }
        return (OrientationType) obj;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType valueOf(java.lang.String) 

}
