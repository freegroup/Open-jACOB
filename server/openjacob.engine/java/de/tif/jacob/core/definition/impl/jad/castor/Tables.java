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
 * Class Tables.
 * 
 * @version $Revision$ $Date$
 */
public class Tables implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableList
     */
    private java.util.Vector _tableList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Tables() {
        super();
        _tableList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Tables()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTable
     * 
     * @param vTable
     */
    public void addTable(de.tif.jacob.core.definition.impl.jad.castor.CastorTable vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.addElement(vTable);
    } //-- void addTable(de.tif.jacob.core.definition.impl.jad.castor.CastorTable) 

    /**
     * Method addTable
     * 
     * @param index
     * @param vTable
     */
    public void addTable(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorTable vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.insertElementAt(vTable, index);
    } //-- void addTable(int, de.tif.jacob.core.definition.impl.jad.castor.CastorTable) 

    /**
     * Method enumerateTable
     */
    public java.util.Enumeration enumerateTable()
    {
        return _tableList.elements();
    } //-- java.util.Enumeration enumerateTable() 

    /**
     * Method getTable
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTable getTable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTable) _tableList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTable getTable(int) 

    /**
     * Method getTable
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTable[] getTable()
    {
        int size = _tableList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorTable[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorTable[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorTable) _tableList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTable[] getTable() 

    /**
     * Method getTableCount
     */
    public int getTableCount()
    {
        return _tableList.size();
    } //-- int getTableCount() 

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
     * Method removeAllTable
     */
    public void removeAllTable()
    {
        _tableList.removeAllElements();
    } //-- void removeAllTable() 

    /**
     * Method removeTable
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTable removeTable(int index)
    {
        java.lang.Object obj = _tableList.elementAt(index);
        _tableList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTable) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTable removeTable(int) 

    /**
     * Method setTable
     * 
     * @param index
     * @param vTable
     */
    public void setTable(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorTable vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableList.setElementAt(vTable, index);
    } //-- void setTable(int, de.tif.jacob.core.definition.impl.jad.castor.CastorTable) 

    /**
     * Method setTable
     * 
     * @param tableArray
     */
    public void setTable(de.tif.jacob.core.definition.impl.jad.castor.CastorTable[] tableArray)
    {
        //-- copy array
        _tableList.removeAllElements();
        for (int i = 0; i < tableArray.length; i++) {
            _tableList.addElement(tableArray[i]);
        }
    } //-- void setTable(de.tif.jacob.core.definition.impl.jad.castor.CastorTable) 

    /**
     * Method unmarshalTables
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTables(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Tables) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Tables.class, reader);
    } //-- java.lang.Object unmarshalTables(java.io.Reader) 

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
