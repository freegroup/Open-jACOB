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
 * Class EnumerationField.
 * 
 * @version $Revision$ $Date$
 */
public class EnumerationField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _default
     */
    private java.lang.String _default;

    /**
     * Field _valueList
     */
    private java.util.Vector _valueList;

    /**
     * Field _labelList
     */
    private java.util.Vector _labelList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EnumerationField() {
        super();
        _valueList = new Vector();
        _labelList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.EnumerationField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addLabel
     * 
     * @param vLabel
     */
    public void addLabel(java.lang.String vLabel)
        throws java.lang.IndexOutOfBoundsException
    {
        _labelList.addElement(vLabel);
    } //-- void addLabel(java.lang.String) 

    /**
     * Method addLabel
     * 
     * @param index
     * @param vLabel
     */
    public void addLabel(int index, java.lang.String vLabel)
        throws java.lang.IndexOutOfBoundsException
    {
        _labelList.insertElementAt(vLabel, index);
    } //-- void addLabel(int, java.lang.String) 

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
     * Method enumerateLabel
     */
    public java.util.Enumeration enumerateLabel()
    {
        return _labelList.elements();
    } //-- java.util.Enumeration enumerateLabel() 

    /**
     * Method enumerateValue
     */
    public java.util.Enumeration enumerateValue()
    {
        return _valueList.elements();
    } //-- java.util.Enumeration enumerateValue() 

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
     * Method getLabel
     * 
     * @param index
     */
    public java.lang.String getLabel(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _labelList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_labelList.elementAt(index);
    } //-- java.lang.String getLabel(int) 

    /**
     * Method getLabel
     */
    public java.lang.String[] getLabel()
    {
        int size = _labelList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_labelList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getLabel() 

    /**
     * Method getLabelCount
     */
    public int getLabelCount()
    {
        return _labelList.size();
    } //-- int getLabelCount() 

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
     * Method removeAllLabel
     */
    public void removeAllLabel()
    {
        _labelList.removeAllElements();
    } //-- void removeAllLabel() 

    /**
     * Method removeAllValue
     */
    public void removeAllValue()
    {
        _valueList.removeAllElements();
    } //-- void removeAllValue() 

    /**
     * Method removeLabel
     * 
     * @param index
     */
    public java.lang.String removeLabel(int index)
    {
        java.lang.Object obj = _labelList.elementAt(index);
        _labelList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeLabel(int) 

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
     * Method setLabel
     * 
     * @param index
     * @param vLabel
     */
    public void setLabel(int index, java.lang.String vLabel)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _labelList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _labelList.setElementAt(vLabel, index);
    } //-- void setLabel(int, java.lang.String) 

    /**
     * Method setLabel
     * 
     * @param labelArray
     */
    public void setLabel(java.lang.String[] labelArray)
    {
        //-- copy array
        _labelList.removeAllElements();
        for (int i = 0; i < labelArray.length; i++) {
            _labelList.addElement(labelArray[i]);
        }
    } //-- void setLabel(java.lang.String) 

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
     * Method unmarshalEnumerationField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalEnumerationField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.EnumerationField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.EnumerationField.class, reader);
    } //-- java.lang.Object unmarshalEnumerationField(java.io.Reader) 

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
