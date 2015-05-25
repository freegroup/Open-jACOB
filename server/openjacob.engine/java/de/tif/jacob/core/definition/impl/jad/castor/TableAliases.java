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
 * Class TableAliases.
 * 
 * @version $Revision$ $Date$
 */
public class TableAliases implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableAliasList
     */
    private java.util.Vector _tableAliasList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableAliases() {
        super();
        _tableAliasList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TableAliases()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTableAlias
     * 
     * @param vTableAlias
     */
    public void addTableAlias(de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias vTableAlias)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableAliasList.addElement(vTableAlias);
    } //-- void addTableAlias(de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias) 

    /**
     * Method addTableAlias
     * 
     * @param index
     * @param vTableAlias
     */
    public void addTableAlias(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias vTableAlias)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableAliasList.insertElementAt(vTableAlias, index);
    } //-- void addTableAlias(int, de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias) 

    /**
     * Method enumerateTableAlias
     */
    public java.util.Enumeration enumerateTableAlias()
    {
        return _tableAliasList.elements();
    } //-- java.util.Enumeration enumerateTableAlias() 

    /**
     * Method getTableAlias
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias getTableAlias(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableAliasList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias) _tableAliasList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias getTableAlias(int) 

    /**
     * Method getTableAlias
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias[] getTableAlias()
    {
        int size = _tableAliasList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias) _tableAliasList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias[] getTableAlias() 

    /**
     * Method getTableAliasCount
     */
    public int getTableAliasCount()
    {
        return _tableAliasList.size();
    } //-- int getTableAliasCount() 

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
     * Method removeAllTableAlias
     */
    public void removeAllTableAlias()
    {
        _tableAliasList.removeAllElements();
    } //-- void removeAllTableAlias() 

    /**
     * Method removeTableAlias
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias removeTableAlias(int index)
    {
        java.lang.Object obj = _tableAliasList.elementAt(index);
        _tableAliasList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias removeTableAlias(int) 

    /**
     * Method setTableAlias
     * 
     * @param index
     * @param vTableAlias
     */
    public void setTableAlias(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias vTableAlias)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableAliasList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableAliasList.setElementAt(vTableAlias, index);
    } //-- void setTableAlias(int, de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias) 

    /**
     * Method setTableAlias
     * 
     * @param tableAliasArray
     */
    public void setTableAlias(de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias[] tableAliasArray)
    {
        //-- copy array
        _tableAliasList.removeAllElements();
        for (int i = 0; i < tableAliasArray.length; i++) {
            _tableAliasList.addElement(tableAliasArray[i]);
        }
    } //-- void setTableAlias(de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias) 

    /**
     * Method unmarshalTableAliases
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTableAliases(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.TableAliases) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.TableAliases.class, reader);
    } //-- java.lang.Object unmarshalTableAliases(java.io.Reader) 

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
