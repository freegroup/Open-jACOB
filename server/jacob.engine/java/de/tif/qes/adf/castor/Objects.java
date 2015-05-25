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
 * Class Objects.
 * 
 * @version $Revision$ $Date$
 */
public class Objects implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _items
     */
    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public Objects() {
        super();
        _items = new Vector();
    } //-- de.tif.qes.adf.castor.Objects()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addObjectsItem
     * 
     * @param vObjectsItem
     */
    public void addObjectsItem(de.tif.qes.adf.castor.ObjectsItem vObjectsItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.addElement(vObjectsItem);
    } //-- void addObjectsItem(de.tif.qes.adf.castor.ObjectsItem) 

    /**
     * Method addObjectsItem
     * 
     * @param index
     * @param vObjectsItem
     */
    public void addObjectsItem(int index, de.tif.qes.adf.castor.ObjectsItem vObjectsItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.insertElementAt(vObjectsItem, index);
    } //-- void addObjectsItem(int, de.tif.qes.adf.castor.ObjectsItem) 

    /**
     * Method enumerateObjectsItem
     */
    public java.util.Enumeration enumerateObjectsItem()
    {
        return _items.elements();
    } //-- java.util.Enumeration enumerateObjectsItem() 

    /**
     * Method getObjectsItem
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.ObjectsItem getObjectsItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.qes.adf.castor.ObjectsItem) _items.elementAt(index);
    } //-- de.tif.qes.adf.castor.ObjectsItem getObjectsItem(int) 

    /**
     * Method getObjectsItem
     */
    public de.tif.qes.adf.castor.ObjectsItem[] getObjectsItem()
    {
        int size = _items.size();
        de.tif.qes.adf.castor.ObjectsItem[] mArray = new de.tif.qes.adf.castor.ObjectsItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.qes.adf.castor.ObjectsItem) _items.elementAt(index);
        }
        return mArray;
    } //-- de.tif.qes.adf.castor.ObjectsItem[] getObjectsItem() 

    /**
     * Method getObjectsItemCount
     */
    public int getObjectsItemCount()
    {
        return _items.size();
    } //-- int getObjectsItemCount() 

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
     * Method removeAllObjectsItem
     */
    public void removeAllObjectsItem()
    {
        _items.removeAllElements();
    } //-- void removeAllObjectsItem() 

    /**
     * Method removeObjectsItem
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.ObjectsItem removeObjectsItem(int index)
    {
        java.lang.Object obj = _items.elementAt(index);
        _items.removeElementAt(index);
        return (de.tif.qes.adf.castor.ObjectsItem) obj;
    } //-- de.tif.qes.adf.castor.ObjectsItem removeObjectsItem(int) 

    /**
     * Method setObjectsItem
     * 
     * @param index
     * @param vObjectsItem
     */
    public void setObjectsItem(int index, de.tif.qes.adf.castor.ObjectsItem vObjectsItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.setElementAt(vObjectsItem, index);
    } //-- void setObjectsItem(int, de.tif.qes.adf.castor.ObjectsItem) 

    /**
     * Method setObjectsItem
     * 
     * @param objectsItemArray
     */
    public void setObjectsItem(de.tif.qes.adf.castor.ObjectsItem[] objectsItemArray)
    {
        //-- copy array
        _items.removeAllElements();
        for (int i = 0; i < objectsItemArray.length; i++) {
            _items.addElement(objectsItemArray[i]);
        }
    } //-- void setObjectsItem(de.tif.qes.adf.castor.ObjectsItem) 

    /**
     * Method unmarshalObjects
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalObjects(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Objects) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Objects.class, reader);
    } //-- java.lang.Object unmarshalObjects(java.io.Reader) 

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
