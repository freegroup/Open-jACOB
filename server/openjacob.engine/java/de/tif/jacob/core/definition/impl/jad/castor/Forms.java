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
 * Class Forms.
 * 
 * @version $Revision$ $Date$
 */
public class Forms implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _formList
     */
    private java.util.Vector _formList;

    /**
     * Field _externalFormList
     */
    private java.util.Vector _externalFormList;

    /**
     * Field _mutableFormList
     */
    private java.util.Vector _mutableFormList;

    /**
     * Field _htmlFormList
     */
    private java.util.Vector _htmlFormList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Forms() {
        super();
        _formList = new Vector();
        _externalFormList = new Vector();
        _mutableFormList = new Vector();
        _htmlFormList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Forms()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addExternalForm
     * 
     * @param vExternalForm
     */
    public void addExternalForm(de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm vExternalForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _externalFormList.addElement(vExternalForm);
    } //-- void addExternalForm(de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm) 

    /**
     * Method addExternalForm
     * 
     * @param index
     * @param vExternalForm
     */
    public void addExternalForm(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm vExternalForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _externalFormList.insertElementAt(vExternalForm, index);
    } //-- void addExternalForm(int, de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm) 

    /**
     * Method addForm
     * 
     * @param vForm
     */
    public void addForm(de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _formList.addElement(vForm);
    } //-- void addForm(de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm) 

    /**
     * Method addForm
     * 
     * @param index
     * @param vForm
     */
    public void addForm(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _formList.insertElementAt(vForm, index);
    } //-- void addForm(int, de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm) 

    /**
     * Method addHtmlForm
     * 
     * @param vHtmlForm
     */
    public void addHtmlForm(de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm vHtmlForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _htmlFormList.addElement(vHtmlForm);
    } //-- void addHtmlForm(de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm) 

    /**
     * Method addHtmlForm
     * 
     * @param index
     * @param vHtmlForm
     */
    public void addHtmlForm(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm vHtmlForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _htmlFormList.insertElementAt(vHtmlForm, index);
    } //-- void addHtmlForm(int, de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm) 

    /**
     * Method addMutableForm
     * 
     * @param vMutableForm
     */
    public void addMutableForm(de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm vMutableForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _mutableFormList.addElement(vMutableForm);
    } //-- void addMutableForm(de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm) 

    /**
     * Method addMutableForm
     * 
     * @param index
     * @param vMutableForm
     */
    public void addMutableForm(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm vMutableForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _mutableFormList.insertElementAt(vMutableForm, index);
    } //-- void addMutableForm(int, de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm) 

    /**
     * Method enumerateExternalForm
     */
    public java.util.Enumeration enumerateExternalForm()
    {
        return _externalFormList.elements();
    } //-- java.util.Enumeration enumerateExternalForm() 

    /**
     * Method enumerateForm
     */
    public java.util.Enumeration enumerateForm()
    {
        return _formList.elements();
    } //-- java.util.Enumeration enumerateForm() 

    /**
     * Method enumerateHtmlForm
     */
    public java.util.Enumeration enumerateHtmlForm()
    {
        return _htmlFormList.elements();
    } //-- java.util.Enumeration enumerateHtmlForm() 

    /**
     * Method enumerateMutableForm
     */
    public java.util.Enumeration enumerateMutableForm()
    {
        return _mutableFormList.elements();
    } //-- java.util.Enumeration enumerateMutableForm() 

    /**
     * Method getExternalForm
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm getExternalForm(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _externalFormList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm) _externalFormList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm getExternalForm(int) 

    /**
     * Method getExternalForm
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm[] getExternalForm()
    {
        int size = _externalFormList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm) _externalFormList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm[] getExternalForm() 

    /**
     * Method getExternalFormCount
     */
    public int getExternalFormCount()
    {
        return _externalFormList.size();
    } //-- int getExternalFormCount() 

    /**
     * Method getForm
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm getForm(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm) _formList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm getForm(int) 

    /**
     * Method getForm
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm[] getForm()
    {
        int size = _formList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm) _formList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm[] getForm() 

    /**
     * Method getFormCount
     */
    public int getFormCount()
    {
        return _formList.size();
    } //-- int getFormCount() 

    /**
     * Method getHtmlForm
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm getHtmlForm(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _htmlFormList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm) _htmlFormList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm getHtmlForm(int) 

    /**
     * Method getHtmlForm
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm[] getHtmlForm()
    {
        int size = _htmlFormList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm) _htmlFormList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm[] getHtmlForm() 

    /**
     * Method getHtmlFormCount
     */
    public int getHtmlFormCount()
    {
        return _htmlFormList.size();
    } //-- int getHtmlFormCount() 

    /**
     * Method getMutableForm
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm getMutableForm(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mutableFormList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm) _mutableFormList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm getMutableForm(int) 

    /**
     * Method getMutableForm
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm[] getMutableForm()
    {
        int size = _mutableFormList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm) _mutableFormList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm[] getMutableForm() 

    /**
     * Method getMutableFormCount
     */
    public int getMutableFormCount()
    {
        return _mutableFormList.size();
    } //-- int getMutableFormCount() 

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
     * Method removeAllExternalForm
     */
    public void removeAllExternalForm()
    {
        _externalFormList.removeAllElements();
    } //-- void removeAllExternalForm() 

    /**
     * Method removeAllForm
     */
    public void removeAllForm()
    {
        _formList.removeAllElements();
    } //-- void removeAllForm() 

    /**
     * Method removeAllHtmlForm
     */
    public void removeAllHtmlForm()
    {
        _htmlFormList.removeAllElements();
    } //-- void removeAllHtmlForm() 

    /**
     * Method removeAllMutableForm
     */
    public void removeAllMutableForm()
    {
        _mutableFormList.removeAllElements();
    } //-- void removeAllMutableForm() 

    /**
     * Method removeExternalForm
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm removeExternalForm(int index)
    {
        java.lang.Object obj = _externalFormList.elementAt(index);
        _externalFormList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm removeExternalForm(int) 

    /**
     * Method removeForm
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm removeForm(int index)
    {
        java.lang.Object obj = _formList.elementAt(index);
        _formList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm removeForm(int) 

    /**
     * Method removeHtmlForm
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm removeHtmlForm(int index)
    {
        java.lang.Object obj = _htmlFormList.elementAt(index);
        _htmlFormList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm removeHtmlForm(int) 

    /**
     * Method removeMutableForm
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm removeMutableForm(int index)
    {
        java.lang.Object obj = _mutableFormList.elementAt(index);
        _mutableFormList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm removeMutableForm(int) 

    /**
     * Method setExternalForm
     * 
     * @param index
     * @param vExternalForm
     */
    public void setExternalForm(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm vExternalForm)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _externalFormList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _externalFormList.setElementAt(vExternalForm, index);
    } //-- void setExternalForm(int, de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm) 

    /**
     * Method setExternalForm
     * 
     * @param externalFormArray
     */
    public void setExternalForm(de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm[] externalFormArray)
    {
        //-- copy array
        _externalFormList.removeAllElements();
        for (int i = 0; i < externalFormArray.length; i++) {
            _externalFormList.addElement(externalFormArray[i]);
        }
    } //-- void setExternalForm(de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm) 

    /**
     * Method setForm
     * 
     * @param index
     * @param vForm
     */
    public void setForm(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _formList.setElementAt(vForm, index);
    } //-- void setForm(int, de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm) 

    /**
     * Method setForm
     * 
     * @param formArray
     */
    public void setForm(de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm[] formArray)
    {
        //-- copy array
        _formList.removeAllElements();
        for (int i = 0; i < formArray.length; i++) {
            _formList.addElement(formArray[i]);
        }
    } //-- void setForm(de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm) 

    /**
     * Method setHtmlForm
     * 
     * @param index
     * @param vHtmlForm
     */
    public void setHtmlForm(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm vHtmlForm)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _htmlFormList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _htmlFormList.setElementAt(vHtmlForm, index);
    } //-- void setHtmlForm(int, de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm) 

    /**
     * Method setHtmlForm
     * 
     * @param htmlFormArray
     */
    public void setHtmlForm(de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm[] htmlFormArray)
    {
        //-- copy array
        _htmlFormList.removeAllElements();
        for (int i = 0; i < htmlFormArray.length; i++) {
            _htmlFormList.addElement(htmlFormArray[i]);
        }
    } //-- void setHtmlForm(de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm) 

    /**
     * Method setMutableForm
     * 
     * @param index
     * @param vMutableForm
     */
    public void setMutableForm(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm vMutableForm)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mutableFormList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mutableFormList.setElementAt(vMutableForm, index);
    } //-- void setMutableForm(int, de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm) 

    /**
     * Method setMutableForm
     * 
     * @param mutableFormArray
     */
    public void setMutableForm(de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm[] mutableFormArray)
    {
        //-- copy array
        _mutableFormList.removeAllElements();
        for (int i = 0; i < mutableFormArray.length; i++) {
            _mutableFormList.addElement(mutableFormArray[i]);
        }
    } //-- void setMutableForm(de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm) 

    /**
     * Method unmarshalForms
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalForms(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Forms) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Forms.class, reader);
    } //-- java.lang.Object unmarshalForms(java.io.Reader) 

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
