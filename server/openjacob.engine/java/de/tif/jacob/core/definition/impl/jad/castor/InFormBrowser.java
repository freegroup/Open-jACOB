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
 * Class InFormBrowser.
 * 
 * @version $Revision$ $Date$
 */
public class InFormBrowser implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _relationToUse
     */
    private java.lang.String _relationToUse;

    /**
     * Field _browserToUse
     */
    private java.lang.String _browserToUse;

    /**
     * Field _newMode
     */
    private boolean _newMode;

    /**
     * keeps track of state for field: _newMode
     */
    private boolean _has_newMode;

    /**
     * Field _updateMode
     */
    private boolean _updateMode;

    /**
     * keeps track of state for field: _updateMode
     */
    private boolean _has_updateMode;

    /**
     * Field _searchMode
     */
    private boolean _searchMode = true;

    /**
     * keeps track of state for field: _searchMode
     */
    private boolean _has_searchMode;

    /**
     * Field _deleteMode
     */
    private boolean _deleteMode = false;

    /**
     * keeps track of state for field: _deleteMode
     */
    private boolean _has_deleteMode;

    /**
     * Field _deleteModeSelected
     */
    private boolean _deleteModeSelected = false;

    /**
     * keeps track of state for field: _deleteModeSelected
     */
    private boolean _has_deleteModeSelected;

    /**
     * Field _deleteModeUpdateNew
     */
    private boolean _deleteModeUpdateNew = false;

    /**
     * keeps track of state for field: _deleteModeUpdateNew
     */
    private boolean _has_deleteModeUpdateNew;

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

    /**
     * Field _caption
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorCaption _caption;

    /**
     * Field _selectionActionEventHandlerList
     */
    private java.util.Vector _selectionActionEventHandlerList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InFormBrowser() {
        super();
        _selectionActionEventHandlerList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Method deleteDeleteMode
     */
    public void deleteDeleteMode()
    {
        this._has_deleteMode= false;
    } //-- void deleteDeleteMode() 

    /**
     * Method deleteDeleteModeSelected
     */
    public void deleteDeleteModeSelected()
    {
        this._has_deleteModeSelected= false;
    } //-- void deleteDeleteModeSelected() 

    /**
     * Method deleteDeleteModeUpdateNew
     */
    public void deleteDeleteModeUpdateNew()
    {
        this._has_deleteModeUpdateNew= false;
    } //-- void deleteDeleteModeUpdateNew() 

    /**
     * Method deleteNewMode
     */
    public void deleteNewMode()
    {
        this._has_newMode= false;
    } //-- void deleteNewMode() 

    /**
     * Method deleteSearchMode
     */
    public void deleteSearchMode()
    {
        this._has_searchMode= false;
    } //-- void deleteSearchMode() 

    /**
     * Method deleteUpdateMode
     */
    public void deleteUpdateMode()
    {
        this._has_updateMode= false;
    } //-- void deleteUpdateMode() 

    /**
     * Method enumerateSelectionActionEventHandler
     */
    public java.util.Enumeration enumerateSelectionActionEventHandler()
    {
        return _selectionActionEventHandlerList.elements();
    } //-- java.util.Enumeration enumerateSelectionActionEventHandler() 

    /**
     * Returns the value of field 'browserToUse'.
     * 
     * @return the value of field 'browserToUse'.
     */
    public java.lang.String getBrowserToUse()
    {
        return this._browserToUse;
    } //-- java.lang.String getBrowserToUse() 

    /**
     * Returns the value of field 'caption'.
     * 
     * @return the value of field 'caption'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorCaption getCaption()
    {
        return this._caption;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorCaption getCaption() 

    /**
     * Returns the value of field 'deleteMode'.
     * 
     * @return the value of field 'deleteMode'.
     */
    public boolean getDeleteMode()
    {
        return this._deleteMode;
    } //-- boolean getDeleteMode() 

    /**
     * Returns the value of field 'deleteModeSelected'.
     * 
     * @return the value of field 'deleteModeSelected'.
     */
    public boolean getDeleteModeSelected()
    {
        return this._deleteModeSelected;
    } //-- boolean getDeleteModeSelected() 

    /**
     * Returns the value of field 'deleteModeUpdateNew'.
     * 
     * @return the value of field 'deleteModeUpdateNew'.
     */
    public boolean getDeleteModeUpdateNew()
    {
        return this._deleteModeUpdateNew;
    } //-- boolean getDeleteModeUpdateNew() 

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
     * Returns the value of field 'newMode'.
     * 
     * @return the value of field 'newMode'.
     */
    public boolean getNewMode()
    {
        return this._newMode;
    } //-- boolean getNewMode() 

    /**
     * Returns the value of field 'relationToUse'.
     * 
     * @return the value of field 'relationToUse'.
     */
    public java.lang.String getRelationToUse()
    {
        return this._relationToUse;
    } //-- java.lang.String getRelationToUse() 

    /**
     * Returns the value of field 'searchMode'.
     * 
     * @return the value of field 'searchMode'.
     */
    public boolean getSearchMode()
    {
        return this._searchMode;
    } //-- boolean getSearchMode() 

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
     * Returns the value of field 'updateMode'.
     * 
     * @return the value of field 'updateMode'.
     */
    public boolean getUpdateMode()
    {
        return this._updateMode;
    } //-- boolean getUpdateMode() 

    /**
     * Method hasDeleteMode
     */
    public boolean hasDeleteMode()
    {
        return this._has_deleteMode;
    } //-- boolean hasDeleteMode() 

    /**
     * Method hasDeleteModeSelected
     */
    public boolean hasDeleteModeSelected()
    {
        return this._has_deleteModeSelected;
    } //-- boolean hasDeleteModeSelected() 

    /**
     * Method hasDeleteModeUpdateNew
     */
    public boolean hasDeleteModeUpdateNew()
    {
        return this._has_deleteModeUpdateNew;
    } //-- boolean hasDeleteModeUpdateNew() 

    /**
     * Method hasNewMode
     */
    public boolean hasNewMode()
    {
        return this._has_newMode;
    } //-- boolean hasNewMode() 

    /**
     * Method hasSearchMode
     */
    public boolean hasSearchMode()
    {
        return this._has_searchMode;
    } //-- boolean hasSearchMode() 

    /**
     * Method hasUpdateMode
     */
    public boolean hasUpdateMode()
    {
        return this._has_updateMode;
    } //-- boolean hasUpdateMode() 

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
     * Method removeAllSelectionActionEventHandler
     */
    public void removeAllSelectionActionEventHandler()
    {
        _selectionActionEventHandlerList.removeAllElements();
    } //-- void removeAllSelectionActionEventHandler() 

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
     * Sets the value of field 'browserToUse'.
     * 
     * @param browserToUse the value of field 'browserToUse'.
     */
    public void setBrowserToUse(java.lang.String browserToUse)
    {
        this._browserToUse = browserToUse;
    } //-- void setBrowserToUse(java.lang.String) 

    /**
     * Sets the value of field 'caption'.
     * 
     * @param caption the value of field 'caption'.
     */
    public void setCaption(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption caption)
    {
        this._caption = caption;
    } //-- void setCaption(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption) 

    /**
     * Sets the value of field 'deleteMode'.
     * 
     * @param deleteMode the value of field 'deleteMode'.
     */
    public void setDeleteMode(boolean deleteMode)
    {
        this._deleteMode = deleteMode;
        this._has_deleteMode = true;
    } //-- void setDeleteMode(boolean) 

    /**
     * Sets the value of field 'deleteModeSelected'.
     * 
     * @param deleteModeSelected the value of field
     * 'deleteModeSelected'.
     */
    public void setDeleteModeSelected(boolean deleteModeSelected)
    {
        this._deleteModeSelected = deleteModeSelected;
        this._has_deleteModeSelected = true;
    } //-- void setDeleteModeSelected(boolean) 

    /**
     * Sets the value of field 'deleteModeUpdateNew'.
     * 
     * @param deleteModeUpdateNew the value of field
     * 'deleteModeUpdateNew'.
     */
    public void setDeleteModeUpdateNew(boolean deleteModeUpdateNew)
    {
        this._deleteModeUpdateNew = deleteModeUpdateNew;
        this._has_deleteModeUpdateNew = true;
    } //-- void setDeleteModeUpdateNew(boolean) 

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
     * Sets the value of field 'newMode'.
     * 
     * @param newMode the value of field 'newMode'.
     */
    public void setNewMode(boolean newMode)
    {
        this._newMode = newMode;
        this._has_newMode = true;
    } //-- void setNewMode(boolean) 

    /**
     * Sets the value of field 'relationToUse'.
     * 
     * @param relationToUse the value of field 'relationToUse'.
     */
    public void setRelationToUse(java.lang.String relationToUse)
    {
        this._relationToUse = relationToUse;
    } //-- void setRelationToUse(java.lang.String) 

    /**
     * Sets the value of field 'searchMode'.
     * 
     * @param searchMode the value of field 'searchMode'.
     */
    public void setSearchMode(boolean searchMode)
    {
        this._searchMode = searchMode;
        this._has_searchMode = true;
    } //-- void setSearchMode(boolean) 

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
     * Sets the value of field 'updateMode'.
     * 
     * @param updateMode the value of field 'updateMode'.
     */
    public void setUpdateMode(boolean updateMode)
    {
        this._updateMode = updateMode;
        this._has_updateMode = true;
    } //-- void setUpdateMode(boolean) 

    /**
     * Method unmarshalInFormBrowser
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInFormBrowser(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser.class, reader);
    } //-- java.lang.Object unmarshalInFormBrowser(java.io.Reader) 

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
