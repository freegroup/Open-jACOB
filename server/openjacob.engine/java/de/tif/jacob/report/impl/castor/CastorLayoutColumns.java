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
 * Class CastorLayoutColumns.
 * 
 * @version $Revision$ $Date$
 */
public class CastorLayoutColumns implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _columnList
     */
    private java.util.Vector _columnList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorLayoutColumns() {
        super();
        _columnList = new Vector();
    } //-- de.tif.jacob.report.impl.castor.CastorLayoutColumns()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addColumn
     * 
     * @param vColumn
     */
    public void addColumn(de.tif.jacob.report.impl.castor.CastorLayoutColumn vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _columnList.addElement(vColumn);
    } //-- void addColumn(de.tif.jacob.report.impl.castor.CastorLayoutColumn) 

    /**
     * Method addColumn
     * 
     * @param index
     * @param vColumn
     */
    public void addColumn(int index, de.tif.jacob.report.impl.castor.CastorLayoutColumn vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _columnList.insertElementAt(vColumn, index);
    } //-- void addColumn(int, de.tif.jacob.report.impl.castor.CastorLayoutColumn) 

    /**
     * Method enumerateColumn
     */
    public java.util.Enumeration enumerateColumn()
    {
        return _columnList.elements();
    } //-- java.util.Enumeration enumerateColumn() 

    /**
     * Method getColumn
     * 
     * @param index
     */
    public de.tif.jacob.report.impl.castor.CastorLayoutColumn getColumn(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _columnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.report.impl.castor.CastorLayoutColumn) _columnList.elementAt(index);
    } //-- de.tif.jacob.report.impl.castor.CastorLayoutColumn getColumn(int) 

    /**
     * Method getColumn
     */
    public de.tif.jacob.report.impl.castor.CastorLayoutColumn[] getColumn()
    {
        int size = _columnList.size();
        de.tif.jacob.report.impl.castor.CastorLayoutColumn[] mArray = new de.tif.jacob.report.impl.castor.CastorLayoutColumn[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.report.impl.castor.CastorLayoutColumn) _columnList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.report.impl.castor.CastorLayoutColumn[] getColumn() 

    /**
     * Method getColumnCount
     */
    public int getColumnCount()
    {
        return _columnList.size();
    } //-- int getColumnCount() 

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
     * Method removeAllColumn
     */
    public void removeAllColumn()
    {
        _columnList.removeAllElements();
    } //-- void removeAllColumn() 

    /**
     * Method removeColumn
     * 
     * @param index
     */
    public de.tif.jacob.report.impl.castor.CastorLayoutColumn removeColumn(int index)
    {
        java.lang.Object obj = _columnList.elementAt(index);
        _columnList.removeElementAt(index);
        return (de.tif.jacob.report.impl.castor.CastorLayoutColumn) obj;
    } //-- de.tif.jacob.report.impl.castor.CastorLayoutColumn removeColumn(int) 

    /**
     * Method setColumn
     * 
     * @param index
     * @param vColumn
     */
    public void setColumn(int index, de.tif.jacob.report.impl.castor.CastorLayoutColumn vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _columnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _columnList.setElementAt(vColumn, index);
    } //-- void setColumn(int, de.tif.jacob.report.impl.castor.CastorLayoutColumn) 

    /**
     * Method setColumn
     * 
     * @param columnArray
     */
    public void setColumn(de.tif.jacob.report.impl.castor.CastorLayoutColumn[] columnArray)
    {
        //-- copy array
        _columnList.removeAllElements();
        for (int i = 0; i < columnArray.length; i++) {
            _columnList.addElement(columnArray[i]);
        }
    } //-- void setColumn(de.tif.jacob.report.impl.castor.CastorLayoutColumn) 

    /**
     * Method unmarshalCastorLayoutColumns
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorLayoutColumns(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorLayoutColumns) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorLayoutColumns.class, reader);
    } //-- java.lang.Object unmarshalCastorLayoutColumns(java.io.Reader) 

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
