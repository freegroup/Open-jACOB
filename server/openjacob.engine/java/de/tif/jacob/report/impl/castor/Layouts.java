/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.report.impl.castor;

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
 * Class Layouts.
 * 
 * @version $Revision$ $Date$
 */
public class Layouts implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _layoutList
     */
    private java.util.Vector _layoutList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Layouts() {
        super();
        _layoutList = new Vector();
    } //-- de.tif.jacob.report.impl.castor.Layouts()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addLayout
     * 
     * @param vLayout
     */
    public void addLayout(de.tif.jacob.report.impl.castor.CastorLayout vLayout)
        throws java.lang.IndexOutOfBoundsException
    {
        _layoutList.addElement(vLayout);
    } //-- void addLayout(de.tif.jacob.report.impl.castor.CastorLayout) 

    /**
     * Method addLayout
     * 
     * @param index
     * @param vLayout
     */
    public void addLayout(int index, de.tif.jacob.report.impl.castor.CastorLayout vLayout)
        throws java.lang.IndexOutOfBoundsException
    {
        _layoutList.insertElementAt(vLayout, index);
    } //-- void addLayout(int, de.tif.jacob.report.impl.castor.CastorLayout) 

    /**
     * Method enumerateLayout
     */
    public java.util.Enumeration enumerateLayout()
    {
        return _layoutList.elements();
    } //-- java.util.Enumeration enumerateLayout() 

    /**
     * Method getLayout
     * 
     * @param index
     */
    public de.tif.jacob.report.impl.castor.CastorLayout getLayout(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _layoutList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.report.impl.castor.CastorLayout) _layoutList.elementAt(index);
    } //-- de.tif.jacob.report.impl.castor.CastorLayout getLayout(int) 

    /**
     * Method getLayout
     */
    public de.tif.jacob.report.impl.castor.CastorLayout[] getLayout()
    {
        int size = _layoutList.size();
        de.tif.jacob.report.impl.castor.CastorLayout[] mArray = new de.tif.jacob.report.impl.castor.CastorLayout[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.report.impl.castor.CastorLayout) _layoutList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.report.impl.castor.CastorLayout[] getLayout() 

    /**
     * Method getLayoutCount
     */
    public int getLayoutCount()
    {
        return _layoutList.size();
    } //-- int getLayoutCount() 

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
     * Method removeAllLayout
     */
    public void removeAllLayout()
    {
        _layoutList.removeAllElements();
    } //-- void removeAllLayout() 

    /**
     * Method removeLayout
     * 
     * @param index
     */
    public de.tif.jacob.report.impl.castor.CastorLayout removeLayout(int index)
    {
        java.lang.Object obj = _layoutList.elementAt(index);
        _layoutList.removeElementAt(index);
        return (de.tif.jacob.report.impl.castor.CastorLayout) obj;
    } //-- de.tif.jacob.report.impl.castor.CastorLayout removeLayout(int) 

    /**
     * Method setLayout
     * 
     * @param index
     * @param vLayout
     */
    public void setLayout(int index, de.tif.jacob.report.impl.castor.CastorLayout vLayout)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _layoutList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _layoutList.setElementAt(vLayout, index);
    } //-- void setLayout(int, de.tif.jacob.report.impl.castor.CastorLayout) 

    /**
     * Method setLayout
     * 
     * @param layoutArray
     */
    public void setLayout(de.tif.jacob.report.impl.castor.CastorLayout[] layoutArray)
    {
        //-- copy array
        _layoutList.removeAllElements();
        for (int i = 0; i < layoutArray.length; i++) {
            _layoutList.addElement(layoutArray[i]);
        }
    } //-- void setLayout(de.tif.jacob.report.impl.castor.CastorLayout) 

    /**
     * Method unmarshalLayouts
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLayouts(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.Layouts) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.Layouts.class, reader);
    } //-- java.lang.Object unmarshalLayouts(java.io.Reader) 

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
