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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CastorAction.
 * 
 * @version $Revision$ $Date$
 */
public class CastorAction implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _generic
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Generic _generic;

    /**
     * Field _newRecord
     */
    private de.tif.jacob.core.definition.impl.jad.castor.NewRecord _newRecord;

    /**
     * Field _updateRecord
     */
    private de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord _updateRecord;

    /**
     * Field _deleteRecord
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord _deleteRecord;

    /**
     * Field _clearGroup
     */
    private de.tif.jacob.core.definition.impl.jad.castor.ClearGroup _clearGroup;

    /**
     * Field _search
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Search _search;

    /**
     * Field _searchUpdateRecord
     */
    private de.tif.jacob.core.definition.impl.jad.castor.SearchUpdateRecord _searchUpdateRecord;

    /**
     * Field _recordSelected
     */
    private de.tif.jacob.core.definition.impl.jad.castor.RecordSelected _recordSelected;

    /**
     * Field _navigateToForm
     */
    private de.tif.jacob.core.definition.impl.jad.castor.NavigateToForm _navigateToForm;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorAction() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorAction()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'clearGroup'.
     * 
     * @return the value of field 'clearGroup'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ClearGroup getClearGroup()
    {
        return this._clearGroup;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ClearGroup getClearGroup() 

    /**
     * Returns the value of field 'deleteRecord'.
     * 
     * @return the value of field 'deleteRecord'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord getDeleteRecord()
    {
        return this._deleteRecord;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord getDeleteRecord() 

    /**
     * Returns the value of field 'generic'.
     * 
     * @return the value of field 'generic'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Generic getGeneric()
    {
        return this._generic;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Generic getGeneric() 

    /**
     * Returns the value of field 'navigateToForm'.
     * 
     * @return the value of field 'navigateToForm'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.NavigateToForm getNavigateToForm()
    {
        return this._navigateToForm;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.NavigateToForm getNavigateToForm() 

    /**
     * Returns the value of field 'newRecord'.
     * 
     * @return the value of field 'newRecord'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.NewRecord getNewRecord()
    {
        return this._newRecord;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.NewRecord getNewRecord() 

    /**
     * Returns the value of field 'recordSelected'.
     * 
     * @return the value of field 'recordSelected'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.RecordSelected getRecordSelected()
    {
        return this._recordSelected;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.RecordSelected getRecordSelected() 

    /**
     * Returns the value of field 'search'.
     * 
     * @return the value of field 'search'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Search getSearch()
    {
        return this._search;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Search getSearch() 

    /**
     * Returns the value of field 'searchUpdateRecord'.
     * 
     * @return the value of field 'searchUpdateRecord'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.SearchUpdateRecord getSearchUpdateRecord()
    {
        return this._searchUpdateRecord;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.SearchUpdateRecord getSearchUpdateRecord() 

    /**
     * Returns the value of field 'updateRecord'.
     * 
     * @return the value of field 'updateRecord'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord getUpdateRecord()
    {
        return this._updateRecord;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord getUpdateRecord() 

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
     * Sets the value of field 'clearGroup'.
     * 
     * @param clearGroup the value of field 'clearGroup'.
     */
    public void setClearGroup(de.tif.jacob.core.definition.impl.jad.castor.ClearGroup clearGroup)
    {
        this._clearGroup = clearGroup;
    } //-- void setClearGroup(de.tif.jacob.core.definition.impl.jad.castor.ClearGroup) 

    /**
     * Sets the value of field 'deleteRecord'.
     * 
     * @param deleteRecord the value of field 'deleteRecord'.
     */
    public void setDeleteRecord(de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord deleteRecord)
    {
        this._deleteRecord = deleteRecord;
    } //-- void setDeleteRecord(de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord) 

    /**
     * Sets the value of field 'generic'.
     * 
     * @param generic the value of field 'generic'.
     */
    public void setGeneric(de.tif.jacob.core.definition.impl.jad.castor.Generic generic)
    {
        this._generic = generic;
    } //-- void setGeneric(de.tif.jacob.core.definition.impl.jad.castor.Generic) 

    /**
     * Sets the value of field 'navigateToForm'.
     * 
     * @param navigateToForm the value of field 'navigateToForm'.
     */
    public void setNavigateToForm(de.tif.jacob.core.definition.impl.jad.castor.NavigateToForm navigateToForm)
    {
        this._navigateToForm = navigateToForm;
    } //-- void setNavigateToForm(de.tif.jacob.core.definition.impl.jad.castor.NavigateToForm) 

    /**
     * Sets the value of field 'newRecord'.
     * 
     * @param newRecord the value of field 'newRecord'.
     */
    public void setNewRecord(de.tif.jacob.core.definition.impl.jad.castor.NewRecord newRecord)
    {
        this._newRecord = newRecord;
    } //-- void setNewRecord(de.tif.jacob.core.definition.impl.jad.castor.NewRecord) 

    /**
     * Sets the value of field 'recordSelected'.
     * 
     * @param recordSelected the value of field 'recordSelected'.
     */
    public void setRecordSelected(de.tif.jacob.core.definition.impl.jad.castor.RecordSelected recordSelected)
    {
        this._recordSelected = recordSelected;
    } //-- void setRecordSelected(de.tif.jacob.core.definition.impl.jad.castor.RecordSelected) 

    /**
     * Sets the value of field 'search'.
     * 
     * @param search the value of field 'search'.
     */
    public void setSearch(de.tif.jacob.core.definition.impl.jad.castor.Search search)
    {
        this._search = search;
    } //-- void setSearch(de.tif.jacob.core.definition.impl.jad.castor.Search) 

    /**
     * Sets the value of field 'searchUpdateRecord'.
     * 
     * @param searchUpdateRecord the value of field
     * 'searchUpdateRecord'.
     */
    public void setSearchUpdateRecord(de.tif.jacob.core.definition.impl.jad.castor.SearchUpdateRecord searchUpdateRecord)
    {
        this._searchUpdateRecord = searchUpdateRecord;
    } //-- void setSearchUpdateRecord(de.tif.jacob.core.definition.impl.jad.castor.SearchUpdateRecord) 

    /**
     * Sets the value of field 'updateRecord'.
     * 
     * @param updateRecord the value of field 'updateRecord'.
     */
    public void setUpdateRecord(de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord updateRecord)
    {
        this._updateRecord = updateRecord;
    } //-- void setUpdateRecord(de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord) 

    /**
     * Method unmarshalCastorAction
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorAction(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorAction) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorAction.class, reader);
    } //-- java.lang.Object unmarshalCastorAction(java.io.Reader) 

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
