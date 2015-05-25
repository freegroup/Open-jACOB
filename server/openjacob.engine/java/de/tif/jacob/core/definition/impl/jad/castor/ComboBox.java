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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
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
 * Class ComboBox.
 * 
 * @version $Revision$ $Date$
 */
public class ComboBox implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _allowUserDefinedValue
     */
    private boolean _allowUserDefinedValue = false;

    /**
     * keeps track of state for field: _allowUserDefinedValue
     */
    private boolean _has_allowUserDefinedValue;

    /**
     * Field _allowNullSearch
     */
    private boolean _allowNullSearch = true;

    /**
     * keeps track of state for field: _allowNullSearch
     */
    private boolean _has_allowNullSearch;

    /**
     * Field _allowNotNullSearch
     */
    private boolean _allowNotNullSearch = true;

    /**
     * keeps track of state for field: _allowNotNullSearch
     */
    private boolean _has_allowNotNullSearch;

    /**
     * Field _callHookOnSelect
     */
    private boolean _callHookOnSelect = true;

    /**
     * keeps track of state for field: _callHookOnSelect
     */
    private boolean _has_callHookOnSelect;

    /**
     * Field _halign
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment _halign;

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

    /**
     * Field _valueList
     */
    private java.util.Vector _valueList;

    /**
     * Field _caption
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorCaption _caption;

    /**
     * Field _font
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorFont _font;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComboBox() {
        super();
        _valueList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ComboBox()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addValue
     * 
     * @param vValue
     */
    public void addValue(java.lang.String vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        _valueList.addElement(vValue);
    } //-- void addValue(java.lang.String) 

    /**
     * Method addValue
     * 
     * @param index
     * @param vValue
     */
    public void addValue(int index, java.lang.String vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        _valueList.insertElementAt(vValue, index);
    } //-- void addValue(int, java.lang.String) 

    /**
     * Method deleteAllowNotNullSearch
     */
    public void deleteAllowNotNullSearch()
    {
        this._has_allowNotNullSearch= false;
    } //-- void deleteAllowNotNullSearch() 

    /**
     * Method deleteAllowNullSearch
     */
    public void deleteAllowNullSearch()
    {
        this._has_allowNullSearch= false;
    } //-- void deleteAllowNullSearch() 

    /**
     * Method deleteAllowUserDefinedValue
     */
    public void deleteAllowUserDefinedValue()
    {
        this._has_allowUserDefinedValue= false;
    } //-- void deleteAllowUserDefinedValue() 

    /**
     * Method deleteCallHookOnSelect
     */
    public void deleteCallHookOnSelect()
    {
        this._has_callHookOnSelect= false;
    } //-- void deleteCallHookOnSelect() 

    /**
     * Method enumerateValue
     */
    public java.util.Enumeration enumerateValue()
    {
        return _valueList.elements();
    } //-- java.util.Enumeration enumerateValue() 

    /**
     * Returns the value of field 'allowNotNullSearch'.
     * 
     * @return the value of field 'allowNotNullSearch'.
     */
    public boolean getAllowNotNullSearch()
    {
        return this._allowNotNullSearch;
    } //-- boolean getAllowNotNullSearch() 

    /**
     * Returns the value of field 'allowNullSearch'.
     * 
     * @return the value of field 'allowNullSearch'.
     */
    public boolean getAllowNullSearch()
    {
        return this._allowNullSearch;
    } //-- boolean getAllowNullSearch() 

    /**
     * Returns the value of field 'allowUserDefinedValue'.
     * 
     * @return the value of field 'allowUserDefinedValue'.
     */
    public boolean getAllowUserDefinedValue()
    {
        return this._allowUserDefinedValue;
    } //-- boolean getAllowUserDefinedValue() 

    /**
     * Returns the value of field 'callHookOnSelect'.
     * 
     * @return the value of field 'callHookOnSelect'.
     */
    public boolean getCallHookOnSelect()
    {
        return this._callHookOnSelect;
    } //-- boolean getCallHookOnSelect() 

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
     * Returns the value of field 'dimension'.
     * 
     * @return the value of field 'dimension'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension()
    {
        return this._dimension;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension() 

    /**
     * Returns the value of field 'font'.
     * 
     * @return the value of field 'font'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorFont getFont()
    {
        return this._font;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorFont getFont() 

    /**
     * Returns the value of field 'halign'.
     * 
     * @return the value of field 'halign'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment getHalign()
    {
        return this._halign;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment getHalign() 

    /**
     * Method getValue
     * 
     * @param index
     */
    public java.lang.String getValue(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _valueList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_valueList.elementAt(index);
    } //-- java.lang.String getValue(int) 

    /**
     * Method getValue
     */
    public java.lang.String[] getValue()
    {
        int size = _valueList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_valueList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getValue() 

    /**
     * Method getValueCount
     */
    public int getValueCount()
    {
        return _valueList.size();
    } //-- int getValueCount() 

    /**
     * Method hasAllowNotNullSearch
     */
    public boolean hasAllowNotNullSearch()
    {
        return this._has_allowNotNullSearch;
    } //-- boolean hasAllowNotNullSearch() 

    /**
     * Method hasAllowNullSearch
     */
    public boolean hasAllowNullSearch()
    {
        return this._has_allowNullSearch;
    } //-- boolean hasAllowNullSearch() 

    /**
     * Method hasAllowUserDefinedValue
     */
    public boolean hasAllowUserDefinedValue()
    {
        return this._has_allowUserDefinedValue;
    } //-- boolean hasAllowUserDefinedValue() 

    /**
     * Method hasCallHookOnSelect
     */
    public boolean hasCallHookOnSelect()
    {
        return this._has_callHookOnSelect;
    } //-- boolean hasCallHookOnSelect() 

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
     * Method removeAllValue
     */
    public void removeAllValue()
    {
        _valueList.removeAllElements();
    } //-- void removeAllValue() 

    /**
     * Method removeValue
     * 
     * @param index
     */
    public java.lang.String removeValue(int index)
    {
        java.lang.Object obj = _valueList.elementAt(index);
        _valueList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeValue(int) 

    /**
     * Sets the value of field 'allowNotNullSearch'.
     * 
     * @param allowNotNullSearch the value of field
     * 'allowNotNullSearch'.
     */
    public void setAllowNotNullSearch(boolean allowNotNullSearch)
    {
        this._allowNotNullSearch = allowNotNullSearch;
        this._has_allowNotNullSearch = true;
    } //-- void setAllowNotNullSearch(boolean) 

    /**
     * Sets the value of field 'allowNullSearch'.
     * 
     * @param allowNullSearch the value of field 'allowNullSearch'.
     */
    public void setAllowNullSearch(boolean allowNullSearch)
    {
        this._allowNullSearch = allowNullSearch;
        this._has_allowNullSearch = true;
    } //-- void setAllowNullSearch(boolean) 

    /**
     * Sets the value of field 'allowUserDefinedValue'.
     * 
     * @param allowUserDefinedValue the value of field
     * 'allowUserDefinedValue'.
     */
    public void setAllowUserDefinedValue(boolean allowUserDefinedValue)
    {
        this._allowUserDefinedValue = allowUserDefinedValue;
        this._has_allowUserDefinedValue = true;
    } //-- void setAllowUserDefinedValue(boolean) 

    /**
     * Sets the value of field 'callHookOnSelect'.
     * 
     * @param callHookOnSelect the value of field 'callHookOnSelect'
     */
    public void setCallHookOnSelect(boolean callHookOnSelect)
    {
        this._callHookOnSelect = callHookOnSelect;
        this._has_callHookOnSelect = true;
    } //-- void setCallHookOnSelect(boolean) 

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
     * Sets the value of field 'dimension'.
     * 
     * @param dimension the value of field 'dimension'.
     */
    public void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension dimension)
    {
        this._dimension = dimension;
    } //-- void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension) 

    /**
     * Sets the value of field 'font'.
     * 
     * @param font the value of field 'font'.
     */
    public void setFont(de.tif.jacob.core.definition.impl.jad.castor.CastorFont font)
    {
        this._font = font;
    } //-- void setFont(de.tif.jacob.core.definition.impl.jad.castor.CastorFont) 

    /**
     * Sets the value of field 'halign'.
     * 
     * @param halign the value of field 'halign'.
     */
    public void setHalign(de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment halign)
    {
        this._halign = halign;
    } //-- void setHalign(de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment) 

    /**
     * Method setValue
     * 
     * @param index
     * @param vValue
     */
    public void setValue(int index, java.lang.String vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _valueList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _valueList.setElementAt(vValue, index);
    } //-- void setValue(int, java.lang.String) 

    /**
     * Method setValue
     * 
     * @param valueArray
     */
    public void setValue(java.lang.String[] valueArray)
    {
        //-- copy array
        _valueList.removeAllElements();
        for (int i = 0; i < valueArray.length; i++) {
            _valueList.addElement(valueArray[i]);
        }
    } //-- void setValue(java.lang.String) 

    /**
     * Method unmarshalComboBox
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalComboBox(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.ComboBox) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.ComboBox.class, reader);
    } //-- java.lang.Object unmarshalComboBox(java.io.Reader) 

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
