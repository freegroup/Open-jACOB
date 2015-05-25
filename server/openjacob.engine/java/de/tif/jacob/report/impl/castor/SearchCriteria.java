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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class SearchCriteria.
 * 
 * @version $Revision$ $Date$
 */
public class SearchCriteria implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableAlias
     */
    private java.lang.String _tableAlias;

    /**
     * Field _field
     */
    private java.lang.String _field;

    /**
     * Field _value
     */
    private java.lang.String _value;

    /**
     * The full qualified name of the qui element.
     * app.domain.form.element
     * 
     * e.g.
     * qualitymaster.f_qualitymaster.request.request.requestMileston
     */
    private java.lang.String _guiElement;

    /**
     * Field _isKeyValue
     */
    private boolean _isKeyValue;

    /**
     * keeps track of state for field: _isKeyValue
     */
    private boolean _has_isKeyValue;


      //----------------/
     //- Constructors -/
    //----------------/

    public SearchCriteria() {
        super();
    } //-- de.tif.jacob.report.impl.castor.SearchCriteria()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsKeyValue
     */
    public void deleteIsKeyValue()
    {
        this._has_isKeyValue= false;
    } //-- void deleteIsKeyValue() 

    /**
     * Returns the value of field 'field'.
     * 
     * @return the value of field 'field'.
     */
    public java.lang.String getField()
    {
        return this._field;
    } //-- java.lang.String getField() 

    /**
     * Returns the value of field 'guiElement'. The field
     * 'guiElement' has the following description: The full
     * qualified name of the qui element.
     * app.domain.form.element
     * 
     * e.g.
     * qualitymaster.f_qualitymaster.request.request.requestMilestone
     * 
     * @return the value of field 'guiElement'.
     */
    public java.lang.String getGuiElement()
    {
        return this._guiElement;
    } //-- java.lang.String getGuiElement() 

    /**
     * Returns the value of field 'isKeyValue'.
     * 
     * @return the value of field 'isKeyValue'.
     */
    public boolean getIsKeyValue()
    {
        return this._isKeyValue;
    } //-- boolean getIsKeyValue() 

    /**
     * Returns the value of field 'tableAlias'.
     * 
     * @return the value of field 'tableAlias'.
     */
    public java.lang.String getTableAlias()
    {
        return this._tableAlias;
    } //-- java.lang.String getTableAlias() 

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public java.lang.String getValue()
    {
        return this._value;
    } //-- java.lang.String getValue() 

    /**
     * Method hasIsKeyValue
     */
    public boolean hasIsKeyValue()
    {
        return this._has_isKeyValue;
    } //-- boolean hasIsKeyValue() 

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
     * Sets the value of field 'field'.
     * 
     * @param field the value of field 'field'.
     */
    public void setField(java.lang.String field)
    {
        this._field = field;
    } //-- void setField(java.lang.String) 

    /**
     * Sets the value of field 'guiElement'. The field 'guiElement'
     * has the following description: The full qualified name of
     * the qui element.
     * app.domain.form.element
     * 
     * e.g.
     * qualitymaster.f_qualitymaster.request.request.requestMilestone
     * 
     * @param guiElement the value of field 'guiElement'.
     */
    public void setGuiElement(java.lang.String guiElement)
    {
        this._guiElement = guiElement;
    } //-- void setGuiElement(java.lang.String) 

    /**
     * Sets the value of field 'isKeyValue'.
     * 
     * @param isKeyValue the value of field 'isKeyValue'.
     */
    public void setIsKeyValue(boolean isKeyValue)
    {
        this._isKeyValue = isKeyValue;
        this._has_isKeyValue = true;
    } //-- void setIsKeyValue(boolean) 

    /**
     * Sets the value of field 'tableAlias'.
     * 
     * @param tableAlias the value of field 'tableAlias'.
     */
    public void setTableAlias(java.lang.String tableAlias)
    {
        this._tableAlias = tableAlias;
    } //-- void setTableAlias(java.lang.String) 

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value)
    {
        this._value = value;
    } //-- void setValue(java.lang.String) 

    /**
     * Method unmarshalSearchCriteria
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSearchCriteria(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.SearchCriteria) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.SearchCriteria.class, reader);
    } //-- java.lang.Object unmarshalSearchCriteria(java.io.Reader) 

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
