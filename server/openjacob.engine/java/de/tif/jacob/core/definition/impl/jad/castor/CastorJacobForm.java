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
 * Class CastorJacobForm.
 * 
 * @version $Revision$ $Date$
 */
public class CastorJacobForm implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _label
     */
    private java.lang.String _label;

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
     * Field _visible
     */
    private boolean _visible = true;

    /**
     * keeps track of state for field: _visible
     */
    private boolean _has_visible;

    /**
     * Field _hideSearchBrowserTabStrip
     */
    private boolean _hideSearchBrowserTabStrip = false;

    /**
     * keeps track of state for field: _hideSearchBrowserTabStrip
     */
    private boolean _has_hideSearchBrowserTabStrip;

    /**
     * Field _groupList
     */
    private java.util.Vector _groupList;

    /**
     * Field _groupContainerList
     */
    private java.util.Vector _groupContainerList;

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

    public CastorJacobForm() {
        super();
        _groupList = new Vector();
        _groupContainerList = new Vector();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addGroup
     * 
     * @param vGroup
     */
    public void addGroup(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupList.addElement(vGroup);
    } //-- void addGroup(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) 

    /**
     * Method addGroup
     * 
     * @param index
     * @param vGroup
     */
    public void addGroup(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGroup vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupList.insertElementAt(vGroup, index);
    } //-- void addGroup(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) 

    /**
     * Method addGroupContainer
     * 
     * @param vGroupContainer
     */
    public void addGroupContainer(de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer vGroupContainer)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupContainerList.addElement(vGroupContainer);
    } //-- void addGroupContainer(de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer) 

    /**
     * Method addGroupContainer
     * 
     * @param index
     * @param vGroupContainer
     */
    public void addGroupContainer(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer vGroupContainer)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupContainerList.insertElementAt(vGroupContainer, index);
    } //-- void addGroupContainer(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer) 

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
     * Method deleteHideSearchBrowserTabStrip
     */
    public void deleteHideSearchBrowserTabStrip()
    {
        this._has_hideSearchBrowserTabStrip= false;
    } //-- void deleteHideSearchBrowserTabStrip() 

    /**
     * Method deleteVisible
     */
    public void deleteVisible()
    {
        this._has_visible= false;
    } //-- void deleteVisible() 

    /**
     * Method enumerateGroup
     */
    public java.util.Enumeration enumerateGroup()
    {
        return _groupList.elements();
    } //-- java.util.Enumeration enumerateGroup() 

    /**
     * Method enumerateGroupContainer
     */
    public java.util.Enumeration enumerateGroupContainer()
    {
        return _groupContainerList.elements();
    } //-- java.util.Enumeration enumerateGroupContainer() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

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
     * Method getGroup
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroup getGroup(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) _groupList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroup getGroup(int) 

    /**
     * Method getGroup
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[] getGroup()
    {
        int size = _groupList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) _groupList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[] getGroup() 

    /**
     * Method getGroupContainer
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer getGroupContainer(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupContainerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer) _groupContainerList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer getGroupContainer(int) 

    /**
     * Method getGroupContainer
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer[] getGroupContainer()
    {
        int size = _groupContainerList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer) _groupContainerList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer[] getGroupContainer() 

    /**
     * Method getGroupContainerCount
     */
    public int getGroupContainerCount()
    {
        return _groupContainerList.size();
    } //-- int getGroupContainerCount() 

    /**
     * Method getGroupCount
     */
    public int getGroupCount()
    {
        return _groupList.size();
    } //-- int getGroupCount() 

    /**
     * Returns the value of field 'hideSearchBrowserTabStrip'.
     * 
     * @return the value of field 'hideSearchBrowserTabStrip'.
     */
    public boolean getHideSearchBrowserTabStrip()
    {
        return this._hideSearchBrowserTabStrip;
    } //-- boolean getHideSearchBrowserTabStrip() 

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
     * Returns the value of field 'label'.
     * 
     * @return the value of field 'label'.
     */
    public java.lang.String getLabel()
    {
        return this._label;
    } //-- java.lang.String getLabel() 

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
     * Method hasHideSearchBrowserTabStrip
     */
    public boolean hasHideSearchBrowserTabStrip()
    {
        return this._has_hideSearchBrowserTabStrip;
    } //-- boolean hasHideSearchBrowserTabStrip() 

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
     * Method removeAllGroup
     */
    public void removeAllGroup()
    {
        _groupList.removeAllElements();
    } //-- void removeAllGroup() 

    /**
     * Method removeAllGroupContainer
     */
    public void removeAllGroupContainer()
    {
        _groupContainerList.removeAllElements();
    } //-- void removeAllGroupContainer() 

    /**
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeGroup
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroup removeGroup(int index)
    {
        java.lang.Object obj = _groupList.elementAt(index);
        _groupList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroup removeGroup(int) 

    /**
     * Method removeGroupContainer
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer removeGroupContainer(int index)
    {
        java.lang.Object obj = _groupContainerList.elementAt(index);
        _groupContainerList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer removeGroupContainer(int) 

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
     * Method setGroup
     * 
     * @param index
     * @param vGroup
     */
    public void setGroup(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGroup vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupList.setElementAt(vGroup, index);
    } //-- void setGroup(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) 

    /**
     * Method setGroup
     * 
     * @param groupArray
     */
    public void setGroup(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[] groupArray)
    {
        //-- copy array
        _groupList.removeAllElements();
        for (int i = 0; i < groupArray.length; i++) {
            _groupList.addElement(groupArray[i]);
        }
    } //-- void setGroup(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) 

    /**
     * Method setGroupContainer
     * 
     * @param index
     * @param vGroupContainer
     */
    public void setGroupContainer(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer vGroupContainer)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupContainerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupContainerList.setElementAt(vGroupContainer, index);
    } //-- void setGroupContainer(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer) 

    /**
     * Method setGroupContainer
     * 
     * @param groupContainerArray
     */
    public void setGroupContainer(de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer[] groupContainerArray)
    {
        //-- copy array
        _groupContainerList.removeAllElements();
        for (int i = 0; i < groupContainerArray.length; i++) {
            _groupContainerList.addElement(groupContainerArray[i]);
        }
    } //-- void setGroupContainer(de.tif.jacob.core.definition.impl.jad.castor.CastorGroupContainer) 

    /**
     * Sets the value of field 'hideSearchBrowserTabStrip'.
     * 
     * @param hideSearchBrowserTabStrip the value of field
     * 'hideSearchBrowserTabStrip'.
     */
    public void setHideSearchBrowserTabStrip(boolean hideSearchBrowserTabStrip)
    {
        this._hideSearchBrowserTabStrip = hideSearchBrowserTabStrip;
        this._has_hideSearchBrowserTabStrip = true;
    } //-- void setHideSearchBrowserTabStrip(boolean) 

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
     * Sets the value of field 'label'.
     * 
     * @param label the value of field 'label'.
     */
    public void setLabel(java.lang.String label)
    {
        this._label = label;
    } //-- void setLabel(java.lang.String) 

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
     * Method unmarshalCastorJacobForm
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorJacobForm(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm.class, reader);
    } //-- java.lang.Object unmarshalCastorJacobForm(java.io.Reader) 

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