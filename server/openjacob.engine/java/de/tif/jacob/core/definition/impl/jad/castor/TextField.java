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

import de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType;
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
 * Class TextField.
 * 
 * @version $Revision$ $Date$
 */
public class TextField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _maxLength
     */
    private int _maxLength;

    /**
     * keeps track of state for field: _maxLength
     */
    private boolean _has_maxLength;

    /**
     * Field _caseSensitive
     */
    private boolean _caseSensitive;

    /**
     * keeps track of state for field: _caseSensitive
     */
    private boolean _has_caseSensitive;

    /**
     * Field _searchMode
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType _searchMode;

    /**
     * Field _fixedLength
     */
    private boolean _fixedLength = false;

    /**
     * keeps track of state for field: _fixedLength
     */
    private boolean _has_fixedLength;

    /**
     * Field _default
     */
    private java.lang.String _default;


      //----------------/
     //- Constructors -/
    //----------------/

    public TextField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TextField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteCaseSensitive
     */
    public void deleteCaseSensitive()
    {
        this._has_caseSensitive= false;
    } //-- void deleteCaseSensitive() 

    /**
     * Method deleteFixedLength
     */
    public void deleteFixedLength()
    {
        this._has_fixedLength= false;
    } //-- void deleteFixedLength() 

    /**
     * Method deleteMaxLength
     */
    public void deleteMaxLength()
    {
        this._has_maxLength= false;
    } //-- void deleteMaxLength() 

    /**
     * Returns the value of field 'caseSensitive'.
     * 
     * @return the value of field 'caseSensitive'.
     */
    public boolean getCaseSensitive()
    {
        return this._caseSensitive;
    } //-- boolean getCaseSensitive() 

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
     * Returns the value of field 'fixedLength'.
     * 
     * @return the value of field 'fixedLength'.
     */
    public boolean getFixedLength()
    {
        return this._fixedLength;
    } //-- boolean getFixedLength() 

    /**
     * Returns the value of field 'maxLength'.
     * 
     * @return the value of field 'maxLength'.
     */
    public int getMaxLength()
    {
        return this._maxLength;
    } //-- int getMaxLength() 

    /**
     * Returns the value of field 'searchMode'.
     * 
     * @return the value of field 'searchMode'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType getSearchMode()
    {
        return this._searchMode;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType getSearchMode() 

    /**
     * Method hasCaseSensitive
     */
    public boolean hasCaseSensitive()
    {
        return this._has_caseSensitive;
    } //-- boolean hasCaseSensitive() 

    /**
     * Method hasFixedLength
     */
    public boolean hasFixedLength()
    {
        return this._has_fixedLength;
    } //-- boolean hasFixedLength() 

    /**
     * Method hasMaxLength
     */
    public boolean hasMaxLength()
    {
        return this._has_maxLength;
    } //-- boolean hasMaxLength() 

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
     * Sets the value of field 'caseSensitive'.
     * 
     * @param caseSensitive the value of field 'caseSensitive'.
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this._caseSensitive = caseSensitive;
        this._has_caseSensitive = true;
    } //-- void setCaseSensitive(boolean) 

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
     * Sets the value of field 'fixedLength'.
     * 
     * @param fixedLength the value of field 'fixedLength'.
     */
    public void setFixedLength(boolean fixedLength)
    {
        this._fixedLength = fixedLength;
        this._has_fixedLength = true;
    } //-- void setFixedLength(boolean) 

    /**
     * Sets the value of field 'maxLength'.
     * 
     * @param maxLength the value of field 'maxLength'.
     */
    public void setMaxLength(int maxLength)
    {
        this._maxLength = maxLength;
        this._has_maxLength = true;
    } //-- void setMaxLength(int) 

    /**
     * Sets the value of field 'searchMode'.
     * 
     * @param searchMode the value of field 'searchMode'.
     */
    public void setSearchMode(de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType searchMode)
    {
        this._searchMode = searchMode;
    } //-- void setSearchMode(de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType) 

    /**
     * Method unmarshalTextField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTextField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.TextField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.TextField.class, reader);
    } //-- java.lang.Object unmarshalTextField(java.io.Reader) 

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
