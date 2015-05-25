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
 * Class CastorGuiElement.
 * 
 * @version $Revision$ $Date$
 */
public class CastorGuiElement implements java.io.Serializable {


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
    private boolean _visible;

    /**
     * keeps track of state for field: _visible
     */
    private boolean _has_visible;

    /**
     * Field _tabIndex
     */
    private int _tabIndex = 0;

    /**
     * keeps track of state for field: _tabIndex
     */
    private boolean _has_tabIndex;

    /**
     * Field _paneIndex
     */
    private int _paneIndex = 0;

    /**
     * keeps track of state for field: _paneIndex
     */
    private boolean _has_paneIndex;

    /**
     * Field _eventHandler
     */
    private java.lang.String _eventHandler;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _inputHint
     */
    private java.lang.String _inputHint;

    /**
     * Field _borderWidth
     */
    private int _borderWidth = -1;

    /**
     * keeps track of state for field: _borderWidth
     */
    private boolean _has_borderWidth;

    /**
     * Field _borderColor
     */
    private java.lang.String _borderColor;

    /**
     * Field _backgroundColor
     */
    private java.lang.String _backgroundColor;

    /**
     * Field _castorGuiElementChoice
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice _castorGuiElementChoice;

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

    public CastorGuiElement() {
        super();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Method deleteBorderWidth
     */
    public void deleteBorderWidth()
    {
        this._has_borderWidth= false;
    } //-- void deleteBorderWidth() 

    /**
     * Method deletePaneIndex
     */
    public void deletePaneIndex()
    {
        this._has_paneIndex= false;
    } //-- void deletePaneIndex() 

    /**
     * Method deleteTabIndex
     */
    public void deleteTabIndex()
    {
        this._has_tabIndex= false;
    } //-- void deleteTabIndex() 

    /**
     * Method deleteVisible
     */
    public void deleteVisible()
    {
        this._has_visible= false;
    } //-- void deleteVisible() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Returns the value of field 'backgroundColor'.
     * 
     * @return the value of field 'backgroundColor'.
     */
    public java.lang.String getBackgroundColor()
    {
        return this._backgroundColor;
    } //-- java.lang.String getBackgroundColor() 

    /**
     * Returns the value of field 'borderColor'.
     * 
     * @return the value of field 'borderColor'.
     */
    public java.lang.String getBorderColor()
    {
        return this._borderColor;
    } //-- java.lang.String getBorderColor() 

    /**
     * Returns the value of field 'borderWidth'.
     * 
     * @return the value of field 'borderWidth'.
     */
    public int getBorderWidth()
    {
        return this._borderWidth;
    } //-- int getBorderWidth() 

    /**
     * Returns the value of field 'castorGuiElementChoice'.
     * 
     * @return the value of field 'castorGuiElementChoice'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice getCastorGuiElementChoice()
    {
        return this._castorGuiElementChoice;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice getCastorGuiElementChoice() 

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
     * Returns the value of field 'inputHint'.
     * 
     * @return the value of field 'inputHint'.
     */
    public java.lang.String getInputHint()
    {
        return this._inputHint;
    } //-- java.lang.String getInputHint() 

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
     * Returns the value of field 'paneIndex'.
     * 
     * @return the value of field 'paneIndex'.
     */
    public int getPaneIndex()
    {
        return this._paneIndex;
    } //-- int getPaneIndex() 

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
     * Returns the value of field 'tabIndex'.
     * 
     * @return the value of field 'tabIndex'.
     */
    public int getTabIndex()
    {
        return this._tabIndex;
    } //-- int getTabIndex() 

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
     * Method hasBorderWidth
     */
    public boolean hasBorderWidth()
    {
        return this._has_borderWidth;
    } //-- boolean hasBorderWidth() 

    /**
     * Method hasPaneIndex
     */
    public boolean hasPaneIndex()
    {
        return this._has_paneIndex;
    } //-- boolean hasPaneIndex() 

    /**
     * Method hasTabIndex
     */
    public boolean hasTabIndex()
    {
        return this._has_tabIndex;
    } //-- boolean hasTabIndex() 

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
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

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
     * Sets the value of field 'backgroundColor'.
     * 
     * @param backgroundColor the value of field 'backgroundColor'.
     */
    public void setBackgroundColor(java.lang.String backgroundColor)
    {
        this._backgroundColor = backgroundColor;
    } //-- void setBackgroundColor(java.lang.String) 

    /**
     * Sets the value of field 'borderColor'.
     * 
     * @param borderColor the value of field 'borderColor'.
     */
    public void setBorderColor(java.lang.String borderColor)
    {
        this._borderColor = borderColor;
    } //-- void setBorderColor(java.lang.String) 

    /**
     * Sets the value of field 'borderWidth'.
     * 
     * @param borderWidth the value of field 'borderWidth'.
     */
    public void setBorderWidth(int borderWidth)
    {
        this._borderWidth = borderWidth;
        this._has_borderWidth = true;
    } //-- void setBorderWidth(int) 

    /**
     * Sets the value of field 'castorGuiElementChoice'.
     * 
     * @param castorGuiElementChoice the value of field
     * 'castorGuiElementChoice'.
     */
    public void setCastorGuiElementChoice(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice castorGuiElementChoice)
    {
        this._castorGuiElementChoice = castorGuiElementChoice;
    } //-- void setCastorGuiElementChoice(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice) 

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
     * Sets the value of field 'inputHint'.
     * 
     * @param inputHint the value of field 'inputHint'.
     */
    public void setInputHint(java.lang.String inputHint)
    {
        this._inputHint = inputHint;
    } //-- void setInputHint(java.lang.String) 

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
     * Sets the value of field 'paneIndex'.
     * 
     * @param paneIndex the value of field 'paneIndex'.
     */
    public void setPaneIndex(int paneIndex)
    {
        this._paneIndex = paneIndex;
        this._has_paneIndex = true;
    } //-- void setPaneIndex(int) 

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
     * Sets the value of field 'tabIndex'.
     * 
     * @param tabIndex the value of field 'tabIndex'.
     */
    public void setTabIndex(int tabIndex)
    {
        this._tabIndex = tabIndex;
        this._has_tabIndex = true;
    } //-- void setTabIndex(int) 

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
     * Method unmarshalCastorGuiElement
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorGuiElement(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement.class, reader);
    } //-- java.lang.Object unmarshalCastorGuiElement(java.io.Reader) 

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
