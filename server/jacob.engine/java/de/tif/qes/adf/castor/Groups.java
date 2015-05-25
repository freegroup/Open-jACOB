/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.qes.adf.castor;

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
 * Class Groups.
 * 
 * @version $Revision$ $Date$
 */
public class Groups implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _groupList
     */
    private java.util.Vector _groupList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Groups() {
        super();
        _groupList = new Vector();
    } //-- de.tif.qes.adf.castor.Groups()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addGroup
     * 
     * @param vGroup
     */
    public void addGroup(de.tif.qes.adf.castor.Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupList.addElement(vGroup);
    } //-- void addGroup(de.tif.qes.adf.castor.Group) 

    /**
     * Method addGroup
     * 
     * @param index
     * @param vGroup
     */
    public void addGroup(int index, de.tif.qes.adf.castor.Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupList.insertElementAt(vGroup, index);
    } //-- void addGroup(int, de.tif.qes.adf.castor.Group) 

    /**
     * Method enumerateGroup
     */
    public java.util.Enumeration enumerateGroup()
    {
        return _groupList.elements();
    } //-- java.util.Enumeration enumerateGroup() 

    /**
     * Method getGroup
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Group getGroup(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.qes.adf.castor.Group) _groupList.elementAt(index);
    } //-- de.tif.qes.adf.castor.Group getGroup(int) 

    /**
     * Method getGroup
     */
    public de.tif.qes.adf.castor.Group[] getGroup()
    {
        int size = _groupList.size();
        de.tif.qes.adf.castor.Group[] mArray = new de.tif.qes.adf.castor.Group[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.qes.adf.castor.Group) _groupList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.qes.adf.castor.Group[] getGroup() 

    /**
     * Method getGroupCount
     */
    public int getGroupCount()
    {
        return _groupList.size();
    } //-- int getGroupCount() 

    /**
     * Method isValid
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
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllGroup
     */
    public void removeAllGroup()
    {
        _groupList.removeAllElements();
    } //-- void removeAllGroup() 

    /**
     * Method removeGroup
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Group removeGroup(int index)
    {
        java.lang.Object obj = _groupList.elementAt(index);
        _groupList.removeElementAt(index);
        return (de.tif.qes.adf.castor.Group) obj;
    } //-- de.tif.qes.adf.castor.Group removeGroup(int) 

    /**
     * Method setGroup
     * 
     * @param index
     * @param vGroup
     */
    public void setGroup(int index, de.tif.qes.adf.castor.Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupList.setElementAt(vGroup, index);
    } //-- void setGroup(int, de.tif.qes.adf.castor.Group) 

    /**
     * Method setGroup
     * 
     * @param groupArray
     */
    public void setGroup(de.tif.qes.adf.castor.Group[] groupArray)
    {
        //-- copy array
        _groupList.removeAllElements();
        for (int i = 0; i < groupArray.length; i++) {
            _groupList.addElement(groupArray[i]);
        }
    } //-- void setGroup(de.tif.qes.adf.castor.Group) 

    /**
     * Method unmarshalGroups
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalGroups(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Groups) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Groups.class, reader);
    } //-- java.lang.Object unmarshalGroups(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
