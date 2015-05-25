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
 * Class CastorFormGroup.
 * 
 * @version $Revision$ $Date$
 */
public class CastorFormGroup implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _visible
     */
    private boolean _visible = true;

    /**
     * keeps track of state for field: _visible
     */
    private boolean _has_visible;

    /**
     * Field _title
     */
    private java.lang.String _title;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _eventHandler
     */
    private java.lang.String _eventHandler;

    /**
     * Field _formList
     */
    private java.util.Vector _formList;

    /**
     * Field _workFlow
     */
    private java.lang.String _workFlow;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorFormGroup() {
        super();
        _formList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addForm
     * 
     * @param vForm
     */
    public void addForm(java.lang.String vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _formList.addElement(vForm);
    } //-- void addForm(java.lang.String) 

    /**
     * Method addForm
     * 
     * @param index
     * @param vForm
     */
    public void addForm(int index, java.lang.String vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        _formList.insertElementAt(vForm, index);
    } //-- void addForm(int, java.lang.String) 

    /**
     * Method deleteVisible
     */
    public void deleteVisible()
    {
        this._has_visible= false;
    } //-- void deleteVisible() 

    /**
     * Method enumerateForm
     */
    public java.util.Enumeration enumerateForm()
    {
        return _formList.elements();
    } //-- java.util.Enumeration enumerateForm() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'eventHandler'.
     * 
     * @return the value of field 'eventHandler'.
     */
    public java.lang.String getEventHandler()
    {
        return this._eventHandler;
    } //-- java.lang.String getEventHandler() 

    /**
     * Method getForm
     * 
     * @param index
     */
    public java.lang.String getForm(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_formList.elementAt(index);
    } //-- java.lang.String getForm(int) 

    /**
     * Method getForm
     */
    public java.lang.String[] getForm()
    {
        int size = _formList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_formList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getForm() 

    /**
     * Method getFormCount
     */
    public int getFormCount()
    {
        return _formList.size();
    } //-- int getFormCount() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'title'.
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
     * Returns the value of field 'visible'.
     * 
     * @return the value of field 'visible'.
     */
    public boolean getVisible()
    {
        return this._visible;
    } //-- boolean getVisible() 

    /**
     * Returns the value of field 'workFlow'.
     * 
     * @return the value of field 'workFlow'.
     */
    public java.lang.String getWorkFlow()
    {
        return this._workFlow;
    } //-- java.lang.String getWorkFlow() 

    /**
     * Method hasVisible
     */
    public boolean hasVisible()
    {
        return this._has_visible;
    } //-- boolean hasVisible() 

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
    public java.lang.String removeForm(int index)
    {
        java.lang.Object obj = _formList.elementAt(index);
        _formList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeForm(int) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'eventHandler'.
     * 
     * @param eventHandler the value of field 'eventHandler'.
     */
    public void setEventHandler(java.lang.String eventHandler)
    {
        this._eventHandler = eventHandler;
    } //-- void setEventHandler(java.lang.String) 

    /**
     * Method setForm
     * 
     * @param index
     * @param vForm
     */
    public void setForm(int index, java.lang.String vForm)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _formList.setElementAt(vForm, index);
    } //-- void setForm(int, java.lang.String) 

    /**
     * Method setForm
     * 
     * @param formArray
     */
    public void setForm(java.lang.String[] formArray)
    {
        //-- copy array
        _formList.removeAllElements();
        for (int i = 0; i < formArray.length; i++) {
            _formList.addElement(formArray[i]);
        }
    } //-- void setForm(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'title'.
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Sets the value of field 'visible'.
     * 
     * @param visible the value of field 'visible'.
     */
    public void setVisible(boolean visible)
    {
        this._visible = visible;
        this._has_visible = true;
    } //-- void setVisible(boolean) 

    /**
     * Sets the value of field 'workFlow'.
     * 
     * @param workFlow the value of field 'workFlow'.
     */
    public void setWorkFlow(java.lang.String workFlow)
    {
        this._workFlow = workFlow;
    } //-- void setWorkFlow(java.lang.String) 

    /**
     * Method unmarshalCastorFormGroup
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorFormGroup(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup.class, reader);
    } //-- java.lang.Object unmarshalCastorFormGroup(java.io.Reader) 

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
