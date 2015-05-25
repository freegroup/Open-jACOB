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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType;
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
 * Class CastorDomain.
 * 
 * @version $Revision$ $Date$
 */
public class CastorDomain implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _title
     */
    private java.lang.String _title;

    /**
     * Field _visible
     */
    private boolean _visible = true;

    /**
     * keeps track of state for field: _visible
     */
    private boolean _has_visible;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _eventHandler
     */
    private java.lang.String _eventHandler;

    /**
     * Field _icon
     */
    private java.lang.String _icon;

    /**
     * Field _canCollapse
     */
    private boolean _canCollapse = true;

    /**
     * keeps track of state for field: _canCollapse
     */
    private boolean _has_canCollapse;

    /**
     * Field _dataScope
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType _dataScope;

    /**
     * Seit jACOB 2.7. Man hat nun die Möglichkeit Formen in einer
     * Gruppe zusammenzufassen. Dies hat nur eine Auswirkung auf
     * Ihre visuelle Representation. Nicht auf den eigentlichen
     * Kern der Engine.
     */
    private java.util.Vector _formGroupList;

    /**
     * Field _formList
     */
    private java.util.Vector _formList;

    /**
     * Field _roleList
     */
    private java.util.Vector _roleList;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;

    /**
     * Field _workFlow
     */
    private java.lang.String _workFlow;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorDomain() {
        super();
        _formGroupList = new Vector();
        _formList = new Vector();
        _roleList = new Vector();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDomain()


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
     * Method addFormGroup
     * 
     * @param vFormGroup
     */
    public void addFormGroup(de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup vFormGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _formGroupList.addElement(vFormGroup);
    } //-- void addFormGroup(de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup) 

    /**
     * Method addFormGroup
     * 
     * @param index
     * @param vFormGroup
     */
    public void addFormGroup(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup vFormGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _formGroupList.insertElementAt(vFormGroup, index);
    } //-- void addFormGroup(int, de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup) 

    /**
     * Method addProperty
     * 
     * @param vProperty
     */
    public void addProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.addElement(vProperty);
    } //-- void addProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method addProperty
     * 
     * @param index
     * @param vProperty
     */
    public void addProperty(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.insertElementAt(vProperty, index);
    } //-- void addProperty(int, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method addRole
     * 
     * @param vRole
     */
    public void addRole(java.lang.String vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.addElement(vRole);
    } //-- void addRole(java.lang.String) 

    /**
     * Method addRole
     * 
     * @param index
     * @param vRole
     */
    public void addRole(int index, java.lang.String vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.insertElementAt(vRole, index);
    } //-- void addRole(int, java.lang.String) 

    /**
     * Method deleteCanCollapse
     */
    public void deleteCanCollapse()
    {
        this._has_canCollapse= false;
    } //-- void deleteCanCollapse() 

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
     * Method enumerateFormGroup
     */
    public java.util.Enumeration enumerateFormGroup()
    {
        return _formGroupList.elements();
    } //-- java.util.Enumeration enumerateFormGroup() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Method enumerateRole
     */
    public java.util.Enumeration enumerateRole()
    {
        return _roleList.elements();
    } //-- java.util.Enumeration enumerateRole() 

    /**
     * Returns the value of field 'canCollapse'.
     * 
     * @return the value of field 'canCollapse'.
     */
    public boolean getCanCollapse()
    {
        return this._canCollapse;
    } //-- boolean getCanCollapse() 

    /**
     * Returns the value of field 'dataScope'.
     * 
     * @return the value of field 'dataScope'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType getDataScope()
    {
        return this._dataScope;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType getDataScope() 

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
     * Method getFormGroup
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup getFormGroup(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formGroupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup) _formGroupList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup getFormGroup(int) 

    /**
     * Method getFormGroup
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup[] getFormGroup()
    {
        int size = _formGroupList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup) _formGroupList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup[] getFormGroup() 

    /**
     * Method getFormGroupCount
     */
    public int getFormGroupCount()
    {
        return _formGroupList.size();
    } //-- int getFormGroupCount() 

    /**
     * Returns the value of field 'icon'.
     * 
     * @return the value of field 'icon'.
     */
    public java.lang.String getIcon()
    {
        return this._icon;
    } //-- java.lang.String getIcon() 

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
     * Method getProperty
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty getProperty(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) _propertyList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty getProperty(int) 

    /**
     * Method getProperty
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] getProperty()
    {
        int size = _propertyList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) _propertyList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] getProperty() 

    /**
     * Method getPropertyCount
     */
    public int getPropertyCount()
    {
        return _propertyList.size();
    } //-- int getPropertyCount() 

    /**
     * Method getRole
     * 
     * @param index
     */
    public java.lang.String getRole(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_roleList.elementAt(index);
    } //-- java.lang.String getRole(int) 

    /**
     * Method getRole
     */
    public java.lang.String[] getRole()
    {
        int size = _roleList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_roleList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getRole() 

    /**
     * Method getRoleCount
     */
    public int getRoleCount()
    {
        return _roleList.size();
    } //-- int getRoleCount() 

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
     * Method hasCanCollapse
     */
    public boolean hasCanCollapse()
    {
        return this._has_canCollapse;
    } //-- boolean hasCanCollapse() 

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
     * Method removeAllFormGroup
     */
    public void removeAllFormGroup()
    {
        _formGroupList.removeAllElements();
    } //-- void removeAllFormGroup() 

    /**
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeAllRole
     */
    public void removeAllRole()
    {
        _roleList.removeAllElements();
    } //-- void removeAllRole() 

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
     * Method removeFormGroup
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup removeFormGroup(int index)
    {
        java.lang.Object obj = _formGroupList.elementAt(index);
        _formGroupList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup removeFormGroup(int) 

    /**
     * Method removeProperty
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty removeProperty(int index)
    {
        java.lang.Object obj = _propertyList.elementAt(index);
        _propertyList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty removeProperty(int) 

    /**
     * Method removeRole
     * 
     * @param index
     */
    public java.lang.String removeRole(int index)
    {
        java.lang.Object obj = _roleList.elementAt(index);
        _roleList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeRole(int) 

    /**
     * Sets the value of field 'canCollapse'.
     * 
     * @param canCollapse the value of field 'canCollapse'.
     */
    public void setCanCollapse(boolean canCollapse)
    {
        this._canCollapse = canCollapse;
        this._has_canCollapse = true;
    } //-- void setCanCollapse(boolean) 

    /**
     * Sets the value of field 'dataScope'.
     * 
     * @param dataScope the value of field 'dataScope'.
     */
    public void setDataScope(de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType dataScope)
    {
        this._dataScope = dataScope;
    } //-- void setDataScope(de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType) 

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
     * Method setFormGroup
     * 
     * @param index
     * @param vFormGroup
     */
    public void setFormGroup(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup vFormGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formGroupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _formGroupList.setElementAt(vFormGroup, index);
    } //-- void setFormGroup(int, de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup) 

    /**
     * Method setFormGroup
     * 
     * @param formGroupArray
     */
    public void setFormGroup(de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup[] formGroupArray)
    {
        //-- copy array
        _formGroupList.removeAllElements();
        for (int i = 0; i < formGroupArray.length; i++) {
            _formGroupList.addElement(formGroupArray[i]);
        }
    } //-- void setFormGroup(de.tif.jacob.core.definition.impl.jad.castor.CastorFormGroup) 

    /**
     * Sets the value of field 'icon'.
     * 
     * @param icon the value of field 'icon'.
     */
    public void setIcon(java.lang.String icon)
    {
        this._icon = icon;
    } //-- void setIcon(java.lang.String) 

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
     * Method setProperty
     * 
     * @param index
     * @param vProperty
     */
    public void setProperty(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propertyList.setElementAt(vProperty, index);
    } //-- void setProperty(int, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method setProperty
     * 
     * @param propertyArray
     */
    public void setProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] propertyArray)
    {
        //-- copy array
        _propertyList.removeAllElements();
        for (int i = 0; i < propertyArray.length; i++) {
            _propertyList.addElement(propertyArray[i]);
        }
    } //-- void setProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method setRole
     * 
     * @param index
     * @param vRole
     */
    public void setRole(int index, java.lang.String vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _roleList.setElementAt(vRole, index);
    } //-- void setRole(int, java.lang.String) 

    /**
     * Method setRole
     * 
     * @param roleArray
     */
    public void setRole(java.lang.String[] roleArray)
    {
        //-- copy array
        _roleList.removeAllElements();
        for (int i = 0; i < roleArray.length; i++) {
            _roleList.addElement(roleArray[i]);
        }
    } //-- void setRole(java.lang.String) 

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
     * Method unmarshalCastorDomain
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorDomain(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorDomain) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorDomain.class, reader);
    } //-- java.lang.Object unmarshalCastorDomain(java.io.Reader) 

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
