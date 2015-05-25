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
 * Class Relationsets.
 * 
 * @version $Revision$ $Date$
 */
public class Relationsets implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _relationsetList
     */
    private java.util.Vector _relationsetList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Relationsets() {
        super();
        _relationsetList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Relationsets()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRelationset
     * 
     * @param vRelationset
     */
    public void addRelationset(de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset vRelationset)
        throws java.lang.IndexOutOfBoundsException
    {
        _relationsetList.addElement(vRelationset);
    } //-- void addRelationset(de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset) 

    /**
     * Method addRelationset
     * 
     * @param index
     * @param vRelationset
     */
    public void addRelationset(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset vRelationset)
        throws java.lang.IndexOutOfBoundsException
    {
        _relationsetList.insertElementAt(vRelationset, index);
    } //-- void addRelationset(int, de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset) 

    /**
     * Method enumerateRelationset
     */
    public java.util.Enumeration enumerateRelationset()
    {
        return _relationsetList.elements();
    } //-- java.util.Enumeration enumerateRelationset() 

    /**
     * Method getRelationset
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset getRelationset(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relationsetList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset) _relationsetList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset getRelationset(int) 

    /**
     * Method getRelationset
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset[] getRelationset()
    {
        int size = _relationsetList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset) _relationsetList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset[] getRelationset() 

    /**
     * Method getRelationsetCount
     */
    public int getRelationsetCount()
    {
        return _relationsetList.size();
    } //-- int getRelationsetCount() 

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
     * Method removeAllRelationset
     */
    public void removeAllRelationset()
    {
        _relationsetList.removeAllElements();
    } //-- void removeAllRelationset() 

    /**
     * Method removeRelationset
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset removeRelationset(int index)
    {
        java.lang.Object obj = _relationsetList.elementAt(index);
        _relationsetList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset removeRelationset(int) 

    /**
     * Method setRelationset
     * 
     * @param index
     * @param vRelationset
     */
    public void setRelationset(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset vRelationset)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relationsetList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _relationsetList.setElementAt(vRelationset, index);
    } //-- void setRelationset(int, de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset) 

    /**
     * Method setRelationset
     * 
     * @param relationsetArray
     */
    public void setRelationset(de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset[] relationsetArray)
    {
        //-- copy array
        _relationsetList.removeAllElements();
        for (int i = 0; i < relationsetArray.length; i++) {
            _relationsetList.addElement(relationsetArray[i]);
        }
    } //-- void setRelationset(de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset) 

    /**
     * Method unmarshalRelationsets
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalRelationsets(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Relationsets) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Relationsets.class, reader);
    } //-- java.lang.Object unmarshalRelationsets(java.io.Reader) 

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
