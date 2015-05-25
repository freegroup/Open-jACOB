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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
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
 * Class CastorGroup.
 * 
 * @version $Revision$ $Date$
 */
public class CastorGroup implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resizeMode
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode _resizeMode;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _alias
     */
    private java.lang.String _alias;

    /**
     * Field _browser
     */
    private java.lang.String _browser;

    /**
     * Field _hideEmptyBrowser
     */
    private boolean _hideEmptyBrowser = false;

    /**
     * keeps track of state for field: _hideEmptyBrowser
     */
    private boolean _has_hideEmptyBrowser;

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
     * Field _border
     */
    private boolean _border = true;

    /**
     * keeps track of state for field: _border
     */
    private boolean _has_border;

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
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

    /**
     * Field _selectionActionEventHandlerList
     */
    private java.util.Vector _selectionActionEventHandlerList;

    /**
     * Field _guiElementList
     */
    private java.util.Vector _guiElementList;

    /**
     * Field _contextMenuEntryList
     */
    private java.util.Vector _contextMenuEntryList;

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

    public CastorGroup() {
        super();
        _selectionActionEventHandlerList = new Vector();
        _guiElementList = new Vector();
        _contextMenuEntryList = new Vector();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroup()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addContextMenuEntry
     * 
     * @param vContextMenuEntry
     */
    public void addContextMenuEntry(de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry vContextMenuEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        _contextMenuEntryList.addElement(vContextMenuEntry);
    } //-- void addContextMenuEntry(de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry) 

    /**
     * Method addContextMenuEntry
     * 
     * @param index
     * @param vContextMenuEntry
     */
    public void addContextMenuEntry(int index, de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry vContextMenuEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        _contextMenuEntryList.insertElementAt(vContextMenuEntry, index);
    } //-- void addContextMenuEntry(int, de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry) 

    /**
     * Method addGuiElement
     * 
     * @param vGuiElement
     */
    public void addGuiElement(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement vGuiElement)
        throws java.lang.IndexOutOfBoundsException
    {
        _guiElementList.addElement(vGuiElement);
    } //-- void addGuiElement(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) 

    /**
     * Method addGuiElement
     * 
     * @param index
     * @param vGuiElement
     */
    public void addGuiElement(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement vGuiElement)
        throws java.lang.IndexOutOfBoundsException
    {
        _guiElementList.insertElementAt(vGuiElement, index);
    } //-- void addGuiElement(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) 

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
     * Method addSelectionActionEventHandler
     * 
     * @param vSelectionActionEventHandler
     */
    public void addSelectionActionEventHandler(de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler vSelectionActionEventHandler)
        throws java.lang.IndexOutOfBoundsException
    {
        _selectionActionEventHandlerList.addElement(vSelectionActionEventHandler);
    } //-- void addSelectionActionEventHandler(de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler) 

    /**
     * Method addSelectionActionEventHandler
     * 
     * @param index
     * @param vSelectionActionEventHandler
     */
    public void addSelectionActionEventHandler(int index, de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler vSelectionActionEventHandler)
        throws java.lang.IndexOutOfBoundsException
    {
        _selectionActionEventHandlerList.insertElementAt(vSelectionActionEventHandler, index);
    } //-- void addSelectionActionEventHandler(int, de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler) 

    /**
     * Method deleteBorder
     */
    public void deleteBorder()
    {
        this._has_border= false;
    } //-- void deleteBorder() 

    /**
     * Method deleteBorderWidth
     */
    public void deleteBorderWidth()
    {
        this._has_borderWidth= false;
    } //-- void deleteBorderWidth() 

    /**
     * Method deleteHideEmptyBrowser
     */
    public void deleteHideEmptyBrowser()
    {
        this._has_hideEmptyBrowser= false;
    } //-- void deleteHideEmptyBrowser() 

    /**
     * Method enumerateContextMenuEntry
     */
    public java.util.Enumeration enumerateContextMenuEntry()
    {
        return _contextMenuEntryList.elements();
    } //-- java.util.Enumeration enumerateContextMenuEntry() 

    /**
     * Method enumerateGuiElement
     */
    public java.util.Enumeration enumerateGuiElement()
    {
        return _guiElementList.elements();
    } //-- java.util.Enumeration enumerateGuiElement() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Method enumerateSelectionActionEventHandler
     */
    public java.util.Enumeration enumerateSelectionActionEventHandler()
    {
        return _selectionActionEventHandlerList.elements();
    } //-- java.util.Enumeration enumerateSelectionActionEventHandler() 

    /**
     * Returns the value of field 'alias'.
     * 
     * @return the value of field 'alias'.
     */
    public java.lang.String getAlias()
    {
        return this._alias;
    } //-- java.lang.String getAlias() 

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
     * Returns the value of field 'border'.
     * 
     * @return the value of field 'border'.
     */
    public boolean getBorder()
    {
        return this._border;
    } //-- boolean getBorder() 

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
     * Returns the value of field 'browser'.
     * 
     * @return the value of field 'browser'.
     */
    public java.lang.String getBrowser()
    {
        return this._browser;
    } //-- java.lang.String getBrowser() 

    /**
     * Method getContextMenuEntry
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry getContextMenuEntry(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contextMenuEntryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry) _contextMenuEntryList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry getContextMenuEntry(int) 

    /**
     * Method getContextMenuEntry
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry[] getContextMenuEntry()
    {
        int size = _contextMenuEntryList.size();
        de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry) _contextMenuEntryList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry[] getContextMenuEntry() 

    /**
     * Method getContextMenuEntryCount
     */
    public int getContextMenuEntryCount()
    {
        return _contextMenuEntryList.size();
    } //-- int getContextMenuEntryCount() 

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
     * Returns the value of field 'dimension'.
     * 
     * @return the value of field 'dimension'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension()
    {
        return this._dimension;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension() 

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
     * Method getGuiElement
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement getGuiElement(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _guiElementList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) _guiElementList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement getGuiElement(int) 

    /**
     * Method getGuiElement
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[] getGuiElement()
    {
        int size = _guiElementList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) _guiElementList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[] getGuiElement() 

    /**
     * Method getGuiElementCount
     */
    public int getGuiElementCount()
    {
        return _guiElementList.size();
    } //-- int getGuiElementCount() 

    /**
     * Returns the value of field 'hideEmptyBrowser'.
     * 
     * @return the value of field 'hideEmptyBrowser'.
     */
    public boolean getHideEmptyBrowser()
    {
        return this._hideEmptyBrowser;
    } //-- boolean getHideEmptyBrowser() 

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
     * Returns the value of field 'resizeMode'.
     * 
     * @return the value of field 'resizeMode'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode getResizeMode()
    {
        return this._resizeMode;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode getResizeMode() 

    /**
     * Method getSelectionActionEventHandler
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler getSelectionActionEventHandler(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _selectionActionEventHandlerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler) _selectionActionEventHandlerList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler getSelectionActionEventHandler(int) 

    /**
     * Method getSelectionActionEventHandler
     */
    public de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler[] getSelectionActionEventHandler()
    {
        int size = _selectionActionEventHandlerList.size();
        de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler) _selectionActionEventHandlerList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler[] getSelectionActionEventHandler() 

    /**
     * Method getSelectionActionEventHandlerCount
     */
    public int getSelectionActionEventHandlerCount()
    {
        return _selectionActionEventHandlerList.size();
    } //-- int getSelectionActionEventHandlerCount() 

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
     * Method hasBorder
     */
    public boolean hasBorder()
    {
        return this._has_border;
    } //-- boolean hasBorder() 

    /**
     * Method hasBorderWidth
     */
    public boolean hasBorderWidth()
    {
        return this._has_borderWidth;
    } //-- boolean hasBorderWidth() 

    /**
     * Method hasHideEmptyBrowser
     */
    public boolean hasHideEmptyBrowser()
    {
        return this._has_hideEmptyBrowser;
    } //-- boolean hasHideEmptyBrowser() 

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
     * Method removeAllContextMenuEntry
     */
    public void removeAllContextMenuEntry()
    {
        _contextMenuEntryList.removeAllElements();
    } //-- void removeAllContextMenuEntry() 

    /**
     * Method removeAllGuiElement
     */
    public void removeAllGuiElement()
    {
        _guiElementList.removeAllElements();
    } //-- void removeAllGuiElement() 

    /**
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeAllSelectionActionEventHandler
     */
    public void removeAllSelectionActionEventHandler()
    {
        _selectionActionEventHandlerList.removeAllElements();
    } //-- void removeAllSelectionActionEventHandler() 

    /**
     * Method removeContextMenuEntry
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry removeContextMenuEntry(int index)
    {
        java.lang.Object obj = _contextMenuEntryList.elementAt(index);
        _contextMenuEntryList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry removeContextMenuEntry(int) 

    /**
     * Method removeGuiElement
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement removeGuiElement(int index)
    {
        java.lang.Object obj = _guiElementList.elementAt(index);
        _guiElementList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement removeGuiElement(int) 

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
     * Method removeSelectionActionEventHandler
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler removeSelectionActionEventHandler(int index)
    {
        java.lang.Object obj = _selectionActionEventHandlerList.elementAt(index);
        _selectionActionEventHandlerList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler removeSelectionActionEventHandler(int) 

    /**
     * Sets the value of field 'alias'.
     * 
     * @param alias the value of field 'alias'.
     */
    public void setAlias(java.lang.String alias)
    {
        this._alias = alias;
    } //-- void setAlias(java.lang.String) 

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
     * Sets the value of field 'border'.
     * 
     * @param border the value of field 'border'.
     */
    public void setBorder(boolean border)
    {
        this._border = border;
        this._has_border = true;
    } //-- void setBorder(boolean) 

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
     * Sets the value of field 'browser'.
     * 
     * @param browser the value of field 'browser'.
     */
    public void setBrowser(java.lang.String browser)
    {
        this._browser = browser;
    } //-- void setBrowser(java.lang.String) 

    /**
     * Method setContextMenuEntry
     * 
     * @param index
     * @param vContextMenuEntry
     */
    public void setContextMenuEntry(int index, de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry vContextMenuEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contextMenuEntryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _contextMenuEntryList.setElementAt(vContextMenuEntry, index);
    } //-- void setContextMenuEntry(int, de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry) 

    /**
     * Method setContextMenuEntry
     * 
     * @param contextMenuEntryArray
     */
    public void setContextMenuEntry(de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry[] contextMenuEntryArray)
    {
        //-- copy array
        _contextMenuEntryList.removeAllElements();
        for (int i = 0; i < contextMenuEntryArray.length; i++) {
            _contextMenuEntryList.addElement(contextMenuEntryArray[i]);
        }
    } //-- void setContextMenuEntry(de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry) 

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
     * Sets the value of field 'dimension'.
     * 
     * @param dimension the value of field 'dimension'.
     */
    public void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension dimension)
    {
        this._dimension = dimension;
    } //-- void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension) 

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
     * Method setGuiElement
     * 
     * @param index
     * @param vGuiElement
     */
    public void setGuiElement(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement vGuiElement)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _guiElementList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _guiElementList.setElementAt(vGuiElement, index);
    } //-- void setGuiElement(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) 

    /**
     * Method setGuiElement
     * 
     * @param guiElementArray
     */
    public void setGuiElement(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[] guiElementArray)
    {
        //-- copy array
        _guiElementList.removeAllElements();
        for (int i = 0; i < guiElementArray.length; i++) {
            _guiElementList.addElement(guiElementArray[i]);
        }
    } //-- void setGuiElement(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) 

    /**
     * Sets the value of field 'hideEmptyBrowser'.
     * 
     * @param hideEmptyBrowser the value of field 'hideEmptyBrowser'
     */
    public void setHideEmptyBrowser(boolean hideEmptyBrowser)
    {
        this._hideEmptyBrowser = hideEmptyBrowser;
        this._has_hideEmptyBrowser = true;
    } //-- void setHideEmptyBrowser(boolean) 

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
     * Sets the value of field 'resizeMode'.
     * 
     * @param resizeMode the value of field 'resizeMode'.
     */
    public void setResizeMode(de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode resizeMode)
    {
        this._resizeMode = resizeMode;
    } //-- void setResizeMode(de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode) 

    /**
     * Method setSelectionActionEventHandler
     * 
     * @param index
     * @param vSelectionActionEventHandler
     */
    public void setSelectionActionEventHandler(int index, de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler vSelectionActionEventHandler)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _selectionActionEventHandlerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _selectionActionEventHandlerList.setElementAt(vSelectionActionEventHandler, index);
    } //-- void setSelectionActionEventHandler(int, de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler) 

    /**
     * Method setSelectionActionEventHandler
     * 
     * @param selectionActionEventHandlerArray
     */
    public void setSelectionActionEventHandler(de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler[] selectionActionEventHandlerArray)
    {
        //-- copy array
        _selectionActionEventHandlerList.removeAllElements();
        for (int i = 0; i < selectionActionEventHandlerArray.length; i++) {
            _selectionActionEventHandlerList.addElement(selectionActionEventHandlerArray[i]);
        }
    } //-- void setSelectionActionEventHandler(de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler) 

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
     * Method unmarshalCastorGroup
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorGroup(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup.class, reader);
    } //-- java.lang.Object unmarshalCastorGroup(java.io.Reader) 

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
