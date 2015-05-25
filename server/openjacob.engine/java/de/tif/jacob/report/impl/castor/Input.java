/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.report.impl.castor;

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
 * Class Input.
 * 
 * @version $Revision$ $Date$
 */
public class Input implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _relationSet
     */
    private java.lang.String _relationSet;

    /**
     * Field _mainTableAlias
     */
    private java.lang.String _mainTableAlias;

    /**
     * Field _searchCriteriaList
     */
    private java.util.Vector _searchCriteriaList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Input() {
        super();
        _searchCriteriaList = new Vector();
    } //-- de.tif.jacob.report.impl.castor.Input()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addSearchCriteria
     * 
     * @param vSearchCriteria
     */
    public void addSearchCriteria(de.tif.jacob.report.impl.castor.SearchCriteria vSearchCriteria)
        throws java.lang.IndexOutOfBoundsException
    {
        _searchCriteriaList.addElement(vSearchCriteria);
    } //-- void addSearchCriteria(de.tif.jacob.report.impl.castor.SearchCriteria) 

    /**
     * Method addSearchCriteria
     * 
     * @param index
     * @param vSearchCriteria
     */
    public void addSearchCriteria(int index, de.tif.jacob.report.impl.castor.SearchCriteria vSearchCriteria)
        throws java.lang.IndexOutOfBoundsException
    {
        _searchCriteriaList.insertElementAt(vSearchCriteria, index);
    } //-- void addSearchCriteria(int, de.tif.jacob.report.impl.castor.SearchCriteria) 

    /**
     * Method enumerateSearchCriteria
     */
    public java.util.Enumeration enumerateSearchCriteria()
    {
        return _searchCriteriaList.elements();
    } //-- java.util.Enumeration enumerateSearchCriteria() 

    /**
     * Returns the value of field 'mainTableAlias'.
     * 
     * @return the value of field 'mainTableAlias'.
     */
    public java.lang.String getMainTableAlias()
    {
        return this._mainTableAlias;
    } //-- java.lang.String getMainTableAlias() 

    /**
     * Returns the value of field 'relationSet'.
     * 
     * @return the value of field 'relationSet'.
     */
    public java.lang.String getRelationSet()
    {
        return this._relationSet;
    } //-- java.lang.String getRelationSet() 

    /**
     * Method getSearchCriteria
     * 
     * @param index
     */
    public de.tif.jacob.report.impl.castor.SearchCriteria getSearchCriteria(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _searchCriteriaList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.report.impl.castor.SearchCriteria) _searchCriteriaList.elementAt(index);
    } //-- de.tif.jacob.report.impl.castor.SearchCriteria getSearchCriteria(int) 

    /**
     * Method getSearchCriteria
     */
    public de.tif.jacob.report.impl.castor.SearchCriteria[] getSearchCriteria()
    {
        int size = _searchCriteriaList.size();
        de.tif.jacob.report.impl.castor.SearchCriteria[] mArray = new de.tif.jacob.report.impl.castor.SearchCriteria[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.report.impl.castor.SearchCriteria) _searchCriteriaList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.report.impl.castor.SearchCriteria[] getSearchCriteria() 

    /**
     * Method getSearchCriteriaCount
     */
    public int getSearchCriteriaCount()
    {
        return _searchCriteriaList.size();
    } //-- int getSearchCriteriaCount() 

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
     * Method removeAllSearchCriteria
     */
    public void removeAllSearchCriteria()
    {
        _searchCriteriaList.removeAllElements();
    } //-- void removeAllSearchCriteria() 

    /**
     * Method removeSearchCriteria
     * 
     * @param index
     */
    public de.tif.jacob.report.impl.castor.SearchCriteria removeSearchCriteria(int index)
    {
        java.lang.Object obj = _searchCriteriaList.elementAt(index);
        _searchCriteriaList.removeElementAt(index);
        return (de.tif.jacob.report.impl.castor.SearchCriteria) obj;
    } //-- de.tif.jacob.report.impl.castor.SearchCriteria removeSearchCriteria(int) 

    /**
     * Sets the value of field 'mainTableAlias'.
     * 
     * @param mainTableAlias the value of field 'mainTableAlias'.
     */
    public void setMainTableAlias(java.lang.String mainTableAlias)
    {
        this._mainTableAlias = mainTableAlias;
    } //-- void setMainTableAlias(java.lang.String) 

    /**
     * Sets the value of field 'relationSet'.
     * 
     * @param relationSet the value of field 'relationSet'.
     */
    public void setRelationSet(java.lang.String relationSet)
    {
        this._relationSet = relationSet;
    } //-- void setRelationSet(java.lang.String) 

    /**
     * Method setSearchCriteria
     * 
     * @param index
     * @param vSearchCriteria
     */
    public void setSearchCriteria(int index, de.tif.jacob.report.impl.castor.SearchCriteria vSearchCriteria)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _searchCriteriaList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _searchCriteriaList.setElementAt(vSearchCriteria, index);
    } //-- void setSearchCriteria(int, de.tif.jacob.report.impl.castor.SearchCriteria) 

    /**
     * Method setSearchCriteria
     * 
     * @param searchCriteriaArray
     */
    public void setSearchCriteria(de.tif.jacob.report.impl.castor.SearchCriteria[] searchCriteriaArray)
    {
        //-- copy array
        _searchCriteriaList.removeAllElements();
        for (int i = 0; i < searchCriteriaArray.length; i++) {
            _searchCriteriaList.addElement(searchCriteriaArray[i]);
        }
    } //-- void setSearchCriteria(de.tif.jacob.report.impl.castor.SearchCriteria) 

    /**
     * Method unmarshalInput
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalInput(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.Input) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.Input.class, reader);
    } //-- java.lang.Object unmarshalInput(java.io.Reader) 

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
