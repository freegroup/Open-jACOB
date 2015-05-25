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
 * Sollte beim nächsten Majorrelease zu Pflicht gemacht werden!
 * 
 * @version $Revision$ $Date$
 */
public class Roles implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _roleList
     */
    private java.util.Vector _roleList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Roles() {
        super();
        _roleList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Roles()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRole
     * 
     * @param vRole
     */
    public void addRole(de.tif.jacob.core.definition.impl.jad.castor.CastorRole vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.addElement(vRole);
    } //-- void addRole(de.tif.jacob.core.definition.impl.jad.castor.CastorRole) 

    /**
     * Method addRole
     * 
     * @param index
     * @param vRole
     */
    public void addRole(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorRole vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.insertElementAt(vRole, index);
    } //-- void addRole(int, de.tif.jacob.core.definition.impl.jad.castor.CastorRole) 

    /**
     * Method enumerateRole
     */
    public java.util.Enumeration enumerateRole()
    {
        return _roleList.elements();
    } //-- java.util.Enumeration enumerateRole() 

    /**
     * Method getRole
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRole getRole(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorRole) _roleList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRole getRole(int) 

    /**
     * Method getRole
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRole[] getRole()
    {
        int size = _roleList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorRole[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorRole[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorRole) _roleList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRole[] getRole() 

    /**
     * Method getRoleCount
     */
    public int getRoleCount()
    {
        return _roleList.size();
    } //-- int getRoleCount() 

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
     * Method removeAllRole
     */
    public void removeAllRole()
    {
        _roleList.removeAllElements();
    } //-- void removeAllRole() 

    /**
     * Method removeRole
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorRole removeRole(int index)
    {
        java.lang.Object obj = _roleList.elementAt(index);
        _roleList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorRole) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRole removeRole(int) 

    /**
     * Method setRole
     * 
     * @param index
     * @param vRole
     */
    public void setRole(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorRole vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _roleList.setElementAt(vRole, index);
    } //-- void setRole(int, de.tif.jacob.core.definition.impl.jad.castor.CastorRole) 

    /**
     * Method setRole
     * 
     * @param roleArray
     */
    public void setRole(de.tif.jacob.core.definition.impl.jad.castor.CastorRole[] roleArray)
    {
        //-- copy array
        _roleList.removeAllElements();
        for (int i = 0; i < roleArray.length; i++) {
            _roleList.addElement(roleArray[i]);
        }
    } //-- void setRole(de.tif.jacob.core.definition.impl.jad.castor.CastorRole) 

    /**
     * Method unmarshalRoles
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalRoles(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Roles) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Roles.class, reader);
    } //-- java.lang.Object unmarshalRoles(java.io.Reader) 

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
