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
 * Class LocalInputField.
 * 
 * @version $Revision$ $Date$
 */
public class LocalInputField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableField
     */
    private java.lang.String _tableField;

    /**
     * Field _passwordInput
     */
    private de.tif.jacob.core.definition.impl.jad.castor.PasswordInput _passwordInput;

    /**
     * Field _textInput
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TextInput _textInput;

    /**
     * Seit jACOB 2.6
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Gauge _gauge;

    /**
     * Field _checkBox
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CheckBox _checkBox;

    /**
     * Seit jACOB 2.6
     */
    private de.tif.jacob.core.definition.impl.jad.castor.ListBox _listBox;

    /**
     * Field _comboBox
     */
    private de.tif.jacob.core.definition.impl.jad.castor.ComboBox _comboBox;

    /**
     * Seit jACOB 2.7
     */
    private de.tif.jacob.core.definition.impl.jad.castor.RadioButtonGroup _radioButtonGroup;

    /**
     * Field _dateInput
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DateInput _dateInput;

    /**
     * Field _timeInput
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TimeInput _timeInput;

    /**
     * Field _timestampInput
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TimestampInput _timestampInput;

    /**
     * Field _longTextInput
     */
    private de.tif.jacob.core.definition.impl.jad.castor.LongTextInput _longTextInput;

    /**
     * Field _image
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Image _image;

    /**
     * Field _documentInput
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DocumentInput _documentInput;


      //----------------/
     //- Constructors -/
    //----------------/

    public LocalInputField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LocalInputField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'checkBox'.
     * 
     * @return the value of field 'checkBox'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CheckBox getCheckBox()
    {
        return this._checkBox;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CheckBox getCheckBox() 

    /**
     * Returns the value of field 'comboBox'.
     * 
     * @return the value of field 'comboBox'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ComboBox getComboBox()
    {
        return this._comboBox;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ComboBox getComboBox() 

    /**
     * Returns the value of field 'dateInput'.
     * 
     * @return the value of field 'dateInput'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DateInput getDateInput()
    {
        return this._dateInput;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DateInput getDateInput() 

    /**
     * Returns the value of field 'documentInput'.
     * 
     * @return the value of field 'documentInput'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DocumentInput getDocumentInput()
    {
        return this._documentInput;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DocumentInput getDocumentInput() 

    /**
     * Returns the value of field 'gauge'. The field 'gauge' has
     * the following description: Seit jACOB 2.6
     * 
     * @return the value of field 'gauge'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Gauge getGauge()
    {
        return this._gauge;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Gauge getGauge() 

    /**
     * Returns the value of field 'image'.
     * 
     * @return the value of field 'image'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Image getImage()
    {
        return this._image;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Image getImage() 

    /**
     * Returns the value of field 'listBox'. The field 'listBox'
     * has the following description: Seit jACOB 2.6
     * 
     * @return the value of field 'listBox'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ListBox getListBox()
    {
        return this._listBox;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ListBox getListBox() 

    /**
     * Returns the value of field 'longTextInput'.
     * 
     * @return the value of field 'longTextInput'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.LongTextInput getLongTextInput()
    {
        return this._longTextInput;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LongTextInput getLongTextInput() 

    /**
     * Returns the value of field 'passwordInput'.
     * 
     * @return the value of field 'passwordInput'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.PasswordInput getPasswordInput()
    {
        return this._passwordInput;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.PasswordInput getPasswordInput() 

    /**
     * Returns the value of field 'radioButtonGroup'. The field
     * 'radioButtonGroup' has the following description: Seit jACOB
     * 2.7
     * 
     * @return the value of field 'radioButtonGroup'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.RadioButtonGroup getRadioButtonGroup()
    {
        return this._radioButtonGroup;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.RadioButtonGroup getRadioButtonGroup() 

    /**
     * Returns the value of field 'tableField'.
     * 
     * @return the value of field 'tableField'.
     */
    public java.lang.String getTableField()
    {
        return this._tableField;
    } //-- java.lang.String getTableField() 

    /**
     * Returns the value of field 'textInput'.
     * 
     * @return the value of field 'textInput'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TextInput getTextInput()
    {
        return this._textInput;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TextInput getTextInput() 

    /**
     * Returns the value of field 'timeInput'.
     * 
     * @return the value of field 'timeInput'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TimeInput getTimeInput()
    {
        return this._timeInput;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TimeInput getTimeInput() 

    /**
     * Returns the value of field 'timestampInput'.
     * 
     * @return the value of field 'timestampInput'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TimestampInput getTimestampInput()
    {
        return this._timestampInput;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TimestampInput getTimestampInput() 

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
     * Sets the value of field 'checkBox'.
     * 
     * @param checkBox the value of field 'checkBox'.
     */
    public void setCheckBox(de.tif.jacob.core.definition.impl.jad.castor.CheckBox checkBox)
    {
        this._checkBox = checkBox;
    } //-- void setCheckBox(de.tif.jacob.core.definition.impl.jad.castor.CheckBox) 

    /**
     * Sets the value of field 'comboBox'.
     * 
     * @param comboBox the value of field 'comboBox'.
     */
    public void setComboBox(de.tif.jacob.core.definition.impl.jad.castor.ComboBox comboBox)
    {
        this._comboBox = comboBox;
    } //-- void setComboBox(de.tif.jacob.core.definition.impl.jad.castor.ComboBox) 

    /**
     * Sets the value of field 'dateInput'.
     * 
     * @param dateInput the value of field 'dateInput'.
     */
    public void setDateInput(de.tif.jacob.core.definition.impl.jad.castor.DateInput dateInput)
    {
        this._dateInput = dateInput;
    } //-- void setDateInput(de.tif.jacob.core.definition.impl.jad.castor.DateInput) 

    /**
     * Sets the value of field 'documentInput'.
     * 
     * @param documentInput the value of field 'documentInput'.
     */
    public void setDocumentInput(de.tif.jacob.core.definition.impl.jad.castor.DocumentInput documentInput)
    {
        this._documentInput = documentInput;
    } //-- void setDocumentInput(de.tif.jacob.core.definition.impl.jad.castor.DocumentInput) 

    /**
     * Sets the value of field 'gauge'. The field 'gauge' has the
     * following description: Seit jACOB 2.6
     * 
     * @param gauge the value of field 'gauge'.
     */
    public void setGauge(de.tif.jacob.core.definition.impl.jad.castor.Gauge gauge)
    {
        this._gauge = gauge;
    } //-- void setGauge(de.tif.jacob.core.definition.impl.jad.castor.Gauge) 

    /**
     * Sets the value of field 'image'.
     * 
     * @param image the value of field 'image'.
     */
    public void setImage(de.tif.jacob.core.definition.impl.jad.castor.Image image)
    {
        this._image = image;
    } //-- void setImage(de.tif.jacob.core.definition.impl.jad.castor.Image) 

    /**
     * Sets the value of field 'listBox'. The field 'listBox' has
     * the following description: Seit jACOB 2.6
     * 
     * @param listBox the value of field 'listBox'.
     */
    public void setListBox(de.tif.jacob.core.definition.impl.jad.castor.ListBox listBox)
    {
        this._listBox = listBox;
    } //-- void setListBox(de.tif.jacob.core.definition.impl.jad.castor.ListBox) 

    /**
     * Sets the value of field 'longTextInput'.
     * 
     * @param longTextInput the value of field 'longTextInput'.
     */
    public void setLongTextInput(de.tif.jacob.core.definition.impl.jad.castor.LongTextInput longTextInput)
    {
        this._longTextInput = longTextInput;
    } //-- void setLongTextInput(de.tif.jacob.core.definition.impl.jad.castor.LongTextInput) 

    /**
     * Sets the value of field 'passwordInput'.
     * 
     * @param passwordInput the value of field 'passwordInput'.
     */
    public void setPasswordInput(de.tif.jacob.core.definition.impl.jad.castor.PasswordInput passwordInput)
    {
        this._passwordInput = passwordInput;
    } //-- void setPasswordInput(de.tif.jacob.core.definition.impl.jad.castor.PasswordInput) 

    /**
     * Sets the value of field 'radioButtonGroup'. The field
     * 'radioButtonGroup' has the following description: Seit jACOB
     * 2.7
     * 
     * @param radioButtonGroup the value of field 'radioButtonGroup'
     */
    public void setRadioButtonGroup(de.tif.jacob.core.definition.impl.jad.castor.RadioButtonGroup radioButtonGroup)
    {
        this._radioButtonGroup = radioButtonGroup;
    } //-- void setRadioButtonGroup(de.tif.jacob.core.definition.impl.jad.castor.RadioButtonGroup) 

    /**
     * Sets the value of field 'tableField'.
     * 
     * @param tableField the value of field 'tableField'.
     */
    public void setTableField(java.lang.String tableField)
    {
        this._tableField = tableField;
    } //-- void setTableField(java.lang.String) 

    /**
     * Sets the value of field 'textInput'.
     * 
     * @param textInput the value of field 'textInput'.
     */
    public void setTextInput(de.tif.jacob.core.definition.impl.jad.castor.TextInput textInput)
    {
        this._textInput = textInput;
    } //-- void setTextInput(de.tif.jacob.core.definition.impl.jad.castor.TextInput) 

    /**
     * Sets the value of field 'timeInput'.
     * 
     * @param timeInput the value of field 'timeInput'.
     */
    public void setTimeInput(de.tif.jacob.core.definition.impl.jad.castor.TimeInput timeInput)
    {
        this._timeInput = timeInput;
    } //-- void setTimeInput(de.tif.jacob.core.definition.impl.jad.castor.TimeInput) 

    /**
     * Sets the value of field 'timestampInput'.
     * 
     * @param timestampInput the value of field 'timestampInput'.
     */
    public void setTimestampInput(de.tif.jacob.core.definition.impl.jad.castor.TimestampInput timestampInput)
    {
        this._timestampInput = timestampInput;
    } //-- void setTimestampInput(de.tif.jacob.core.definition.impl.jad.castor.TimestampInput) 

    /**
     * Method unmarshalLocalInputField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLocalInputField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.LocalInputField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.LocalInputField.class, reader);
    } //-- java.lang.Object unmarshalLocalInputField(java.io.Reader) 

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
