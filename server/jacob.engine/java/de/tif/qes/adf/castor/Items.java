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
 * Anmerkung: Mußte optional gemacht werden, da QDesigner beim
 * Konvertieren von ADF5.0 Mist macht!
 * 
 * @version $Revision$ $Date$
 */
public class Items implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _itemList
     */
    private java.util.Vector _itemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Items() {
        super();
        _itemList = new Vector();
    } //-- de.tif.qes.adf.castor.Items()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addItem
     * 
     * @param vItem
     */
    public void addItem(de.tif.qes.adf.castor.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.addElement(vItem);
    } //-- void addItem(de.tif.qes.adf.castor.Item) 

    /**
     * Method addItem
     * 
     * @param index
     * @param vItem
     */
    public void addItem(int index, de.tif.qes.adf.castor.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.insertElementAt(vItem, index);
    } //-- void addItem(int, de.tif.qes.adf.castor.Item) 

    /**
     * Method enumerateItem
     */
    public java.util.Enumeration enumerateItem()
    {
        return _itemList.elements();
    } //-- java.util.Enumeration enumerateItem() 

    /**
     * Method getItem
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Item getItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.qes.adf.castor.Item) _itemList.elementAt(index);
    } //-- de.tif.qes.adf.castor.Item getItem(int) 

    /**
     * Method getItem
     */
    public de.tif.qes.adf.castor.Item[] getItem()
    {
        int size = _itemList.size();
        de.tif.qes.adf.castor.Item[] mArray = new de.tif.qes.adf.castor.Item[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.qes.adf.castor.Item) _itemList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.qes.adf.castor.Item[] getItem() 

    /**
     * Method getItemCount
     */
    public int getItemCount()
    {
        return _itemList.size();
    } //-- int getItemCount() 

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
     * Method removeAllItem
     */
    public void removeAllItem()
    {
        _itemList.removeAllElements();
    } //-- void removeAllItem() 

    /**
     * Method removeItem
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Item removeItem(int index)
    {
        java.lang.Object obj = _itemList.elementAt(index);
        _itemList.removeElementAt(index);
        return (de.tif.qes.adf.castor.Item) obj;
    } //-- de.tif.qes.adf.castor.Item removeItem(int) 

    /**
     * Method setItem
     * 
     * @param index
     * @param vItem
     */
    public void setItem(int index, de.tif.qes.adf.castor.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _itemList.setElementAt(vItem, index);
    } //-- void setItem(int, de.tif.qes.adf.castor.Item) 

    /**
     * Method setItem
     * 
     * @param itemArray
     */
    public void setItem(de.tif.qes.adf.castor.Item[] itemArray)
    {
        //-- copy array
        _itemList.removeAllElements();
        for (int i = 0; i < itemArray.length; i++) {
            _itemList.addElement(itemArray[i]);
        }
    } //-- void setItem(de.tif.qes.adf.castor.Item) 

    /**
     * Method unmarshalItems
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalItems(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Items) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Items.class, reader);
    } //-- java.lang.Object unmarshalItems(java.io.Reader) 

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
