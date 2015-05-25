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
 * Class Menus.
 * 
 * @version $Revision$ $Date$
 */
public class Menus implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _menuItemList
     */
    private java.util.Vector _menuItemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Menus() {
        super();
        _menuItemList = new Vector();
    } //-- de.tif.qes.adf.castor.Menus()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMenuItem
     * 
     * @param vMenuItem
     */
    public void addMenuItem(de.tif.qes.adf.castor.MenuItem vMenuItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _menuItemList.addElement(vMenuItem);
    } //-- void addMenuItem(de.tif.qes.adf.castor.MenuItem) 

    /**
     * Method addMenuItem
     * 
     * @param index
     * @param vMenuItem
     */
    public void addMenuItem(int index, de.tif.qes.adf.castor.MenuItem vMenuItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _menuItemList.insertElementAt(vMenuItem, index);
    } //-- void addMenuItem(int, de.tif.qes.adf.castor.MenuItem) 

    /**
     * Method enumerateMenuItem
     */
    public java.util.Enumeration enumerateMenuItem()
    {
        return _menuItemList.elements();
    } //-- java.util.Enumeration enumerateMenuItem() 

    /**
     * Method getMenuItem
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.MenuItem getMenuItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _menuItemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.qes.adf.castor.MenuItem) _menuItemList.elementAt(index);
    } //-- de.tif.qes.adf.castor.MenuItem getMenuItem(int) 

    /**
     * Method getMenuItem
     */
    public de.tif.qes.adf.castor.MenuItem[] getMenuItem()
    {
        int size = _menuItemList.size();
        de.tif.qes.adf.castor.MenuItem[] mArray = new de.tif.qes.adf.castor.MenuItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.qes.adf.castor.MenuItem) _menuItemList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.qes.adf.castor.MenuItem[] getMenuItem() 

    /**
     * Method getMenuItemCount
     */
    public int getMenuItemCount()
    {
        return _menuItemList.size();
    } //-- int getMenuItemCount() 

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
     * Method removeAllMenuItem
     */
    public void removeAllMenuItem()
    {
        _menuItemList.removeAllElements();
    } //-- void removeAllMenuItem() 

    /**
     * Method removeMenuItem
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.MenuItem removeMenuItem(int index)
    {
        java.lang.Object obj = _menuItemList.elementAt(index);
        _menuItemList.removeElementAt(index);
        return (de.tif.qes.adf.castor.MenuItem) obj;
    } //-- de.tif.qes.adf.castor.MenuItem removeMenuItem(int) 

    /**
     * Method setMenuItem
     * 
     * @param index
     * @param vMenuItem
     */
    public void setMenuItem(int index, de.tif.qes.adf.castor.MenuItem vMenuItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _menuItemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _menuItemList.setElementAt(vMenuItem, index);
    } //-- void setMenuItem(int, de.tif.qes.adf.castor.MenuItem) 

    /**
     * Method setMenuItem
     * 
     * @param menuItemArray
     */
    public void setMenuItem(de.tif.qes.adf.castor.MenuItem[] menuItemArray)
    {
        //-- copy array
        _menuItemList.removeAllElements();
        for (int i = 0; i < menuItemArray.length; i++) {
            _menuItemList.addElement(menuItemArray[i]);
        }
    } //-- void setMenuItem(de.tif.qes.adf.castor.MenuItem) 

    /**
     * Method unmarshalMenus
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMenus(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Menus) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Menus.class, reader);
    } //-- java.lang.Object unmarshalMenus(java.io.Reader) 

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
