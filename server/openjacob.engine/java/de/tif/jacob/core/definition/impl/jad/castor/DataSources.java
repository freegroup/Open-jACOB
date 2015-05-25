/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.core.definition.impl.jad.castor;

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
 * Class DataSources.
 * 
 * @version $Revision$ $Date$
 */
public class DataSources implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dataSourceList
     */
    private java.util.Vector _dataSourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DataSources() {
        super();
        _dataSourceList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DataSources()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDataSource
     * 
     * @param vDataSource
     */
    public void addDataSource(de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource vDataSource)
        throws java.lang.IndexOutOfBoundsException
    {
        _dataSourceList.addElement(vDataSource);
    } //-- void addDataSource(de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource) 

    /**
     * Method addDataSource
     * 
     * @param index
     * @param vDataSource
     */
    public void addDataSource(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource vDataSource)
        throws java.lang.IndexOutOfBoundsException
    {
        _dataSourceList.insertElementAt(vDataSource, index);
    } //-- void addDataSource(int, de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource) 

    /**
     * Method enumerateDataSource
     */
    public java.util.Enumeration enumerateDataSource()
    {
        return _dataSourceList.elements();
    } //-- java.util.Enumeration enumerateDataSource() 

    /**
     * Method getDataSource
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource getDataSource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dataSourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource) _dataSourceList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource getDataSource(int) 

    /**
     * Method getDataSource
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource[] getDataSource()
    {
        int size = _dataSourceList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource) _dataSourceList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource[] getDataSource() 

    /**
     * Method getDataSourceCount
     */
    public int getDataSourceCount()
    {
        return _dataSourceList.size();
    } //-- int getDataSourceCount() 

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
     * Method removeAllDataSource
     */
    public void removeAllDataSource()
    {
        _dataSourceList.removeAllElements();
    } //-- void removeAllDataSource() 

    /**
     * Method removeDataSource
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource removeDataSource(int index)
    {
        java.lang.Object obj = _dataSourceList.elementAt(index);
        _dataSourceList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource removeDataSource(int) 

    /**
     * Method setDataSource
     * 
     * @param index
     * @param vDataSource
     */
    public void setDataSource(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource vDataSource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dataSourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _dataSourceList.setElementAt(vDataSource, index);
    } //-- void setDataSource(int, de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource) 

    /**
     * Method setDataSource
     * 
     * @param dataSourceArray
     */
    public void setDataSource(de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource[] dataSourceArray)
    {
        //-- copy array
        _dataSourceList.removeAllElements();
        for (int i = 0; i < dataSourceArray.length; i++) {
            _dataSourceList.addElement(dataSourceArray[i]);
        }
    } //-- void setDataSource(de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource) 

    /**
     * Method unmarshalDataSources
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDataSources(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.DataSources) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.DataSources.class, reader);
    } //-- java.lang.Object unmarshalDataSources(java.io.Reader) 

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
