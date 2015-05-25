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

import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType;
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
 * Class LongTextField.
 * 
 * @version $Revision$ $Date$
 */
public class LongTextField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _changeHeader
     */
    private boolean _changeHeader = false;

    /**
     * keeps track of state for field: _changeHeader
     */
    private boolean _has_changeHeader;

    /**
     * Field _disableMapping
     */
    private boolean _disableMapping = false;

    /**
     * keeps track of state for field: _disableMapping
     */
    private boolean _has_disableMapping;

    /**
     * Field _editMode
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType _editMode;

    /**
     * Field _sqlType
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType _sqlType;

    /**
     * Field _default
     */
    private java.lang.String _default;


      //----------------/
     //- Constructors -/
    //----------------/

    public LongTextField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LongTextField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteChangeHeader
     */
    public void deleteChangeHeader()
    {
        this._has_changeHeader= false;
    } //-- void deleteChangeHeader() 

    /**
     * Method deleteDisableMapping
     */
    public void deleteDisableMapping()
    {
        this._has_disableMapping= false;
    } //-- void deleteDisableMapping() 

    /**
     * Returns the value of field 'changeHeader'.
     * 
     * @return the value of field 'changeHeader'.
     */
    public boolean getChangeHeader()
    {
        return this._changeHeader;
    } //-- boolean getChangeHeader() 

    /**
     * Returns the value of field 'default'.
     * 
     * @return the value of field 'default'.
     */
    public java.lang.String getDefault()
    {
        return this._default;
    } //-- java.lang.String getDefault() 

    /**
     * Returns the value of field 'disableMapping'.
     * 
     * @return the value of field 'disableMapping'.
     */
    public boolean getDisableMapping()
    {
        return this._disableMapping;
    } //-- boolean getDisableMapping() 

    /**
     * Returns the value of field 'editMode'.
     * 
     * @return the value of field 'editMode'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType getEditMode()
    {
        return this._editMode;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType getEditMode() 

    /**
     * Returns the value of field 'sqlType'.
     * 
     * @return the value of field 'sqlType'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType getSqlType()
    {
        return this._sqlType;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType getSqlType() 

    /**
     * Method hasChangeHeader
     */
    public boolean hasChangeHeader()
    {
        return this._has_changeHeader;
    } //-- boolean hasChangeHeader() 

    /**
     * Method hasDisableMapping
     */
    public boolean hasDisableMapping()
    {
        return this._has_disableMapping;
    } //-- boolean hasDisableMapping() 

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
     * Sets the value of field 'changeHeader'.
     * 
     * @param changeHeader the value of field 'changeHeader'.
     */
    public void setChangeHeader(boolean changeHeader)
    {
        this._changeHeader = changeHeader;
        this._has_changeHeader = true;
    } //-- void setChangeHeader(boolean) 

    /**
     * Sets the value of field 'default'.
     * 
     * @param _default
     * @param default the value of field 'default'.
     */
    public void setDefault(java.lang.String _default)
    {
        this._default = _default;
    } //-- void setDefault(java.lang.String) 

    /**
     * Sets the value of field 'disableMapping'.
     * 
     * @param disableMapping the value of field 'disableMapping'.
     */
    public void setDisableMapping(boolean disableMapping)
    {
        this._disableMapping = disableMapping;
        this._has_disableMapping = true;
    } //-- void setDisableMapping(boolean) 

    /**
     * Sets the value of field 'editMode'.
     * 
     * @param editMode the value of field 'editMode'.
     */
    public void setEditMode(de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType editMode)
    {
        this._editMode = editMode;
    } //-- void setEditMode(de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType) 

    /**
     * Sets the value of field 'sqlType'.
     * 
     * @param sqlType the value of field 'sqlType'.
     */
    public void setSqlType(de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType sqlType)
    {
        this._sqlType = sqlType;
    } //-- void setSqlType(de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType) 

    /**
     * Method unmarshalLongTextField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLongTextField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.LongTextField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.LongTextField.class, reader);
    } //-- java.lang.Object unmarshalLongTextField(java.io.Reader) 

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
