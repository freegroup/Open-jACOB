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
import java.math.BigDecimal;
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
     * Field _version
     */
    private java.math.BigDecimal _version;

    /**
     * Field _formList
     */
    private java.util.Vector _formList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Forms() {
        super();
        _formList = new Vector();
    } //-- de.tif.qes.adf.castor.Forms()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addForm
     * 
     * @param vForm
     */
    public void addForm(de.tif.qes.adf.castor.Form vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _formList.addElement(vForm);
    } //-- void addForm(de.tif.qes.adf.castor.Form) 

    /**
     * Method addForm
     * 
     * @param index
     * @param vForm
     */
    public void addForm(int index, de.tif.qes.adf.castor.Form vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _formList.insertElementAt(vForm, index);
    } //-- void addForm(int, de.tif.qes.adf.castor.Form) 

    /**
     * Method enumerateForm
     */
    public java.util.Enumeration enumerateForm()
    {
        return _formList.elements();
    } //-- java.util.Enumeration enumerateForm() 

    /**
     * Method getForm
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Form getForm(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.qes.adf.castor.Form) _formList.elementAt(index);
    } //-- de.tif.qes.adf.castor.Form getForm(int) 

    /**
     * Method getForm
     */
    public de.tif.qes.adf.castor.Form[] getForm()
    {
        int size = _formList.size();
        de.tif.qes.adf.castor.Form[] mArray = new de.tif.qes.adf.castor.Form[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.qes.adf.castor.Form) _formList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.qes.adf.castor.Form[] getForm() 

    /**
     * Method getFormCount
     */
    public int getFormCount()
    {
        return _formList.size();
    } //-- int getFormCount() 

    /**
     * Returns the value of field 'version'.
     * 
     * @return the value of field 'version'.
     */
    public java.math.BigDecimal getVersion()
    {
        return this._version;
    } //-- java.math.BigDecimal getVersion() 

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
     * Method removeAllForm
     */
    public void removeAllForm()
    {
        _formList.removeAllElements();
    } //-- void removeAllForm() 

    /**
     * Method removeForm
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Form removeForm(int index)
    {
        java.lang.Object obj = _formList.elementAt(index);
        _formList.removeElementAt(index);
        return (de.tif.qes.adf.castor.Form) obj;
    } //-- de.tif.qes.adf.castor.Form removeForm(int) 

    /**
     * Method setForm
     * 
     * @param index
     * @param vForm
     */
    public void setForm(int index, de.tif.qes.adf.castor.Form vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _formList.setElementAt(vForm, index);
    } //-- void setForm(int, de.tif.qes.adf.castor.Form) 

    /**
     * Method setForm
     * 
     * @param formArray
     */
    public void setForm(de.tif.qes.adf.castor.Form[] formArray)
    {
        //-- copy array
        _formList.removeAllElements();
        for (int i = 0; i < formArray.length; i++) {
            _formList.addElement(formArray[i]);
        }
    } //-- void setForm(de.tif.qes.adf.castor.Form) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.math.BigDecimal version)
    {
        this._version = version;
    } //-- void setVersion(java.math.BigDecimal) 

    /**
     * Method unmarshalForms
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalForms(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Forms) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Forms.class, reader);
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
