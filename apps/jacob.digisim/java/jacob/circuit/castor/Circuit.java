/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML
 * Schema.
 * $Id: Circuit.java,v 1.1 2007/02/02 22:26:47 freegroup Exp $
 */

package jacob.circuit.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Circuit.
 * 
 * @version $Revision: 1.1 $ $Date: 2007/02/02 22:26:47 $
 */
public class Circuit implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _partList
     */
    private java.util.Vector _partList;

    /**
     * Field _connectionList
     */
    private java.util.Vector _connectionList;

    /**
     * Field _annotationList
     */
    private java.util.Vector _annotationList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Circuit() {
        super();
        _partList = new Vector();
        _connectionList = new Vector();
        _annotationList = new Vector();
    } //-- jacob.circuit.castor.Circuit()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAnnotation
     * 
     * 
     * 
     * @param vAnnotation
     */
    public void addAnnotation(jacob.circuit.castor.Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        _annotationList.addElement(vAnnotation);
    } //-- void addAnnotation(jacob.circuit.castor.Annotation) 

    /**
     * Method addAnnotation
     * 
     * 
     * 
     * @param index
     * @param vAnnotation
     */
    public void addAnnotation(int index, jacob.circuit.castor.Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        _annotationList.insertElementAt(vAnnotation, index);
    } //-- void addAnnotation(int, jacob.circuit.castor.Annotation) 

    /**
     * Method addConnection
     * 
     * 
     * 
     * @param vConnection
     */
    public void addConnection(jacob.circuit.castor.Connection vConnection)
        throws java.lang.IndexOutOfBoundsException
    {
        _connectionList.addElement(vConnection);
    } //-- void addConnection(jacob.circuit.castor.Connection) 

    /**
     * Method addConnection
     * 
     * 
     * 
     * @param index
     * @param vConnection
     */
    public void addConnection(int index, jacob.circuit.castor.Connection vConnection)
        throws java.lang.IndexOutOfBoundsException
    {
        _connectionList.insertElementAt(vConnection, index);
    } //-- void addConnection(int, jacob.circuit.castor.Connection) 

    /**
     * Method addPart
     * 
     * 
     * 
     * @param vPart
     */
    public void addPart(jacob.circuit.castor.Part vPart)
        throws java.lang.IndexOutOfBoundsException
    {
        _partList.addElement(vPart);
    } //-- void addPart(jacob.circuit.castor.Part) 

    /**
     * Method addPart
     * 
     * 
     * 
     * @param index
     * @param vPart
     */
    public void addPart(int index, jacob.circuit.castor.Part vPart)
        throws java.lang.IndexOutOfBoundsException
    {
        _partList.insertElementAt(vPart, index);
    } //-- void addPart(int, jacob.circuit.castor.Part) 

    /**
     * Method enumerateAnnotation
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateAnnotation()
    {
        return _annotationList.elements();
    } //-- java.util.Enumeration enumerateAnnotation() 

    /**
     * Method enumerateConnection
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateConnection()
    {
        return _connectionList.elements();
    } //-- java.util.Enumeration enumerateConnection() 

    /**
     * Method enumeratePart
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumeratePart()
    {
        return _partList.elements();
    } //-- java.util.Enumeration enumeratePart() 

    /**
     * Method getAnnotation
     * 
     * 
     * 
     * @param index
     * @return Annotation
     */
    public jacob.circuit.castor.Annotation getAnnotation(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _annotationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (jacob.circuit.castor.Annotation) _annotationList.elementAt(index);
    } //-- jacob.circuit.castor.Annotation getAnnotation(int) 

    /**
     * Method getAnnotation
     * 
     * 
     * 
     * @return Annotation
     */
    public jacob.circuit.castor.Annotation[] getAnnotation()
    {
        int size = _annotationList.size();
        jacob.circuit.castor.Annotation[] mArray = new jacob.circuit.castor.Annotation[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (jacob.circuit.castor.Annotation) _annotationList.elementAt(index);
        }
        return mArray;
    } //-- jacob.circuit.castor.Annotation[] getAnnotation() 

    /**
     * Method getAnnotationCount
     * 
     * 
     * 
     * @return int
     */
    public int getAnnotationCount()
    {
        return _annotationList.size();
    } //-- int getAnnotationCount() 

    /**
     * Method getConnection
     * 
     * 
     * 
     * @param index
     * @return Connection
     */
    public jacob.circuit.castor.Connection getConnection(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _connectionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (jacob.circuit.castor.Connection) _connectionList.elementAt(index);
    } //-- jacob.circuit.castor.Connection getConnection(int) 

    /**
     * Method getConnection
     * 
     * 
     * 
     * @return Connection
     */
    public jacob.circuit.castor.Connection[] getConnection()
    {
        int size = _connectionList.size();
        jacob.circuit.castor.Connection[] mArray = new jacob.circuit.castor.Connection[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (jacob.circuit.castor.Connection) _connectionList.elementAt(index);
        }
        return mArray;
    } //-- jacob.circuit.castor.Connection[] getConnection() 

    /**
     * Method getConnectionCount
     * 
     * 
     * 
     * @return int
     */
    public int getConnectionCount()
    {
        return _connectionList.size();
    } //-- int getConnectionCount() 

    /**
     * Method getPart
     * 
     * 
     * 
     * @param index
     * @return Part
     */
    public jacob.circuit.castor.Part getPart(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _partList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (jacob.circuit.castor.Part) _partList.elementAt(index);
    } //-- jacob.circuit.castor.Part getPart(int) 

    /**
     * Method getPart
     * 
     * 
     * 
     * @return Part
     */
    public jacob.circuit.castor.Part[] getPart()
    {
        int size = _partList.size();
        jacob.circuit.castor.Part[] mArray = new jacob.circuit.castor.Part[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (jacob.circuit.castor.Part) _partList.elementAt(index);
        }
        return mArray;
    } //-- jacob.circuit.castor.Part[] getPart() 

    /**
     * Method getPartCount
     * 
     * 
     * 
     * @return int
     */
    public int getPartCount()
    {
        return _partList.size();
    } //-- int getPartCount() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllAnnotation
     * 
     */
    public void removeAllAnnotation()
    {
        _annotationList.removeAllElements();
    } //-- void removeAllAnnotation() 

    /**
     * Method removeAllConnection
     * 
     */
    public void removeAllConnection()
    {
        _connectionList.removeAllElements();
    } //-- void removeAllConnection() 

    /**
     * Method removeAllPart
     * 
     */
    public void removeAllPart()
    {
        _partList.removeAllElements();
    } //-- void removeAllPart() 

    /**
     * Method removeAnnotation
     * 
     * 
     * 
     * @param index
     * @return Annotation
     */
    public jacob.circuit.castor.Annotation removeAnnotation(int index)
    {
        java.lang.Object obj = _annotationList.elementAt(index);
        _annotationList.removeElementAt(index);
        return (jacob.circuit.castor.Annotation) obj;
    } //-- jacob.circuit.castor.Annotation removeAnnotation(int) 

    /**
     * Method removeConnection
     * 
     * 
     * 
     * @param index
     * @return Connection
     */
    public jacob.circuit.castor.Connection removeConnection(int index)
    {
        java.lang.Object obj = _connectionList.elementAt(index);
        _connectionList.removeElementAt(index);
        return (jacob.circuit.castor.Connection) obj;
    } //-- jacob.circuit.castor.Connection removeConnection(int) 

    /**
     * Method removePart
     * 
     * 
     * 
     * @param index
     * @return Part
     */
    public jacob.circuit.castor.Part removePart(int index)
    {
        java.lang.Object obj = _partList.elementAt(index);
        _partList.removeElementAt(index);
        return (jacob.circuit.castor.Part) obj;
    } //-- jacob.circuit.castor.Part removePart(int) 

    /**
     * Method setAnnotation
     * 
     * 
     * 
     * @param index
     * @param vAnnotation
     */
    public void setAnnotation(int index, jacob.circuit.castor.Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _annotationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _annotationList.setElementAt(vAnnotation, index);
    } //-- void setAnnotation(int, jacob.circuit.castor.Annotation) 

    /**
     * Method setAnnotation
     * 
     * 
     * 
     * @param annotationArray
     */
    public void setAnnotation(jacob.circuit.castor.Annotation[] annotationArray)
    {
        //-- copy array
        _annotationList.removeAllElements();
        for (int i = 0; i < annotationArray.length; i++) {
            _annotationList.addElement(annotationArray[i]);
        }
    } //-- void setAnnotation(jacob.circuit.castor.Annotation) 

    /**
     * Method setConnection
     * 
     * 
     * 
     * @param index
     * @param vConnection
     */
    public void setConnection(int index, jacob.circuit.castor.Connection vConnection)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _connectionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _connectionList.setElementAt(vConnection, index);
    } //-- void setConnection(int, jacob.circuit.castor.Connection) 

    /**
     * Method setConnection
     * 
     * 
     * 
     * @param connectionArray
     */
    public void setConnection(jacob.circuit.castor.Connection[] connectionArray)
    {
        //-- copy array
        _connectionList.removeAllElements();
        for (int i = 0; i < connectionArray.length; i++) {
            _connectionList.addElement(connectionArray[i]);
        }
    } //-- void setConnection(jacob.circuit.castor.Connection) 

    /**
     * Method setPart
     * 
     * 
     * 
     * @param index
     * @param vPart
     */
    public void setPart(int index, jacob.circuit.castor.Part vPart)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _partList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _partList.setElementAt(vPart, index);
    } //-- void setPart(int, jacob.circuit.castor.Part) 

    /**
     * Method setPart
     * 
     * 
     * 
     * @param partArray
     */
    public void setPart(jacob.circuit.castor.Part[] partArray)
    {
        //-- copy array
        _partList.removeAllElements();
        for (int i = 0; i < partArray.length; i++) {
            _partList.addElement(partArray[i]);
        }
    } //-- void setPart(jacob.circuit.castor.Part) 

    /**
     * Method unmarshalCircuit
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalCircuit(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.circuit.castor.Circuit) Unmarshaller.unmarshal(jacob.circuit.castor.Circuit.class, reader);
    } //-- java.lang.Object unmarshalCircuit(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
