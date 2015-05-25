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
 * Class Relations.
 * 
 * @version $Revision$ $Date$
 */
public class Relations implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _relationList
     */
    private java.util.Vector _relationList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Relations() {
        super();
        _relationList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Relations()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRelation
     * 
     * @param vRelation
     */
    public void addRelation(de.tif.jacob.core.definition.impl.jad.castor.CastorRelation vRelation)
        throws java.lang.IndexOutOfBoundsException
    {
        _relationList.addElement(vRelation);
    } //-- void addRelation(de.tif.jacob.core.definition.impl.jad.castor.CastorRelation) 

    /**
     * Method addRelation
     * 
     * @param index
     * @param vRelation
     */
    public void addRelation(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorRelation vRelation)
        throws java.lang.IndexOutOfBoundsException
    {
        _relationList.insertElementAt(vRelation, index);
    } //-- void addRelation(int, de.tif.jacob.core.definition.impl.jad.castor.CastorRelation) 

    /**
     * Method enumerateRelation
     */
    public java.util.Enumeration enumerateRelation()
    {
        return _relationList.elements();
    } //-- java.util.Enumeration enumerateRelation() 

    /**
     * Method getRelation
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRelation getRelation(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorRelation) _relationList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRelation getRelation(int) 

    /**
     * Method getRelation
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRelation[] getRelation()
    {
        int size = _relationList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorRelation[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorRelation[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorRelation) _relationList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRelation[] getRelation() 

    /**
     * Method getRelationCount
     */
    public int getRelationCount()
    {
        return _relationList.size();
    } //-- int getRelationCount() 

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
     * Method removeAllRelation
     */
    public void removeAllRelation()
    {
        _relationList.removeAllElements();
    } //-- void removeAllRelation() 

    /**
     * Method removeRelation
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRelation removeRelation(int index)
    {
        java.lang.Object obj = _relationList.elementAt(index);
        _relationList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorRelation) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRelation removeRelation(int) 

    /**
     * Method setRelation
     * 
     * @param index
     * @param vRelation
     */
    public void setRelation(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorRelation vRelation)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _relationList.setElementAt(vRelation, index);
    } //-- void setRelation(int, de.tif.jacob.core.definition.impl.jad.castor.CastorRelation) 

    /**
     * Method setRelation
     * 
     * @param relationArray
     */
    public void setRelation(de.tif.jacob.core.definition.impl.jad.castor.CastorRelation[] relationArray)
    {
        //-- copy array
        _relationList.removeAllElements();
        for (int i = 0; i < relationArray.length; i++) {
            _relationList.addElement(relationArray[i]);
        }
    } //-- void setRelation(de.tif.jacob.core.definition.impl.jad.castor.CastorRelation) 

    /**
     * Method unmarshalRelations
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalRelations(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Relations) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Relations.class, reader);
    } //-- java.lang.Object unmarshalRelations(java.io.Reader) 

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
