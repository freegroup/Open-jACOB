/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML
 * Schema.
 * $Id: Backup.java,v 1.1 2007/02/02 22:26:48 freegroup Exp $
 */

package jacob.backup.castor;

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
 * Class Backup.
 * 
 * @version $Revision: 1.1 $ $Date: 2007/02/02 22:26:48 $
 */
public class Backup implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _partList
     */
    private java.util.Vector _partList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Backup() {
        super();
        _partList = new Vector();
    } //-- jacob.backup.castor.Backup()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPart
     * 
     * 
     * 
     * @param vPart
     */
    public void addPart(jacob.backup.castor.Part vPart)
        throws java.lang.IndexOutOfBoundsException
    {
        _partList.addElement(vPart);
    } //-- void addPart(jacob.backup.castor.Part) 

    /**
     * Method addPart
     * 
     * 
     * 
     * @param index
     * @param vPart
     */
    public void addPart(int index, jacob.backup.castor.Part vPart)
        throws java.lang.IndexOutOfBoundsException
    {
        _partList.insertElementAt(vPart, index);
    } //-- void addPart(int, jacob.backup.castor.Part) 

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
     * Method getPart
     * 
     * 
     * 
     * @param index
     * @return Part
     */
    public jacob.backup.castor.Part getPart(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _partList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (jacob.backup.castor.Part) _partList.elementAt(index);
    } //-- jacob.backup.castor.Part getPart(int) 

    /**
     * Method getPart
     * 
     * 
     * 
     * @return Part
     */
    public jacob.backup.castor.Part[] getPart()
    {
        int size = _partList.size();
        jacob.backup.castor.Part[] mArray = new jacob.backup.castor.Part[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (jacob.backup.castor.Part) _partList.elementAt(index);
        }
        return mArray;
    } //-- jacob.backup.castor.Part[] getPart() 

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
     * Method removeAllPart
     * 
     */
    public void removeAllPart()
    {
        _partList.removeAllElements();
    } //-- void removeAllPart() 

    /**
     * Method removePart
     * 
     * 
     * 
     * @param index
     * @return Part
     */
    public jacob.backup.castor.Part removePart(int index)
    {
        java.lang.Object obj = _partList.elementAt(index);
        _partList.removeElementAt(index);
        return (jacob.backup.castor.Part) obj;
    } //-- jacob.backup.castor.Part removePart(int) 

    /**
     * Method setPart
     * 
     * 
     * 
     * @param index
     * @param vPart
     */
    public void setPart(int index, jacob.backup.castor.Part vPart)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _partList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _partList.setElementAt(vPart, index);
    } //-- void setPart(int, jacob.backup.castor.Part) 

    /**
     * Method setPart
     * 
     * 
     * 
     * @param partArray
     */
    public void setPart(jacob.backup.castor.Part[] partArray)
    {
        //-- copy array
        _partList.removeAllElements();
        for (int i = 0; i < partArray.length; i++) {
            _partList.addElement(partArray[i]);
        }
    } //-- void setPart(jacob.backup.castor.Part) 

    /**
     * Method unmarshalBackup
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalBackup(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.backup.castor.Backup) Unmarshaller.unmarshal(jacob.backup.castor.Backup.class, reader);
    } //-- java.lang.Object unmarshalBackup(java.io.Reader) 

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
