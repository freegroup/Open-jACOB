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

import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType;
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
 * Class LongTextInput.
 * 
 * @version $Revision$ $Date$
 */
public class LongTextInput implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mode
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType _mode;

    /**
     * Field _inForm
     */
    private boolean _inForm;

    /**
     * keeps track of state for field: _inForm
     */
    private boolean _has_inForm;

    /**
     * Field _htmlInput
     */
    private boolean _htmlInput = false;

    /**
     * keeps track of state for field: _htmlInput
     */
    private boolean _has_htmlInput;

    /**
     * Field _contentType
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType _contentType = de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType.valueOf("text/plain");

    /**
     * Field _wordWrap
     */
    private boolean _wordWrap = false;

    /**
     * keeps track of state for field: _wordWrap
     */
    private boolean _has_wordWrap;

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

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

    public LongTextInput() {
        super();
        setContentType(de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType.valueOf("text/plain"));
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LongTextInput()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteHtmlInput
     */
    public void deleteHtmlInput()
    {
        this._has_htmlInput= false;
    } //-- void deleteHtmlInput() 

    /**
     * Method deleteInForm
     */
    public void deleteInForm()
    {
        this._has_inForm= false;
    } //-- void deleteInForm() 

    /**
     * Method deleteWordWrap
     */
    public void deleteWordWrap()
    {
        this._has_wordWrap= false;
    } //-- void deleteWordWrap() 

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
     * Returns the value of field 'contentType'.
     * 
     * @return the value of field 'contentType'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType getContentType()
    {
        return this._contentType;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType getContentType() 

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
     * Returns the value of field 'htmlInput'.
     * 
     * @return the value of field 'htmlInput'.
     */
    public boolean getHtmlInput()
    {
        return this._htmlInput;
    } //-- boolean getHtmlInput() 

    /**
     * Returns the value of field 'inForm'.
     * 
     * @return the value of field 'inForm'.
     */
    public boolean getInForm()
    {
        return this._inForm;
    } //-- boolean getInForm() 

    /**
     * Returns the value of field 'mode'.
     * 
     * @return the value of field 'mode'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType getMode()
    {
        return this._mode;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType getMode() 

    /**
     * Returns the value of field 'wordWrap'.
     * 
     * @return the value of field 'wordWrap'.
     */
    public boolean getWordWrap()
    {
        return this._wordWrap;
    } //-- boolean getWordWrap() 

    /**
     * Method hasHtmlInput
     */
    public boolean hasHtmlInput()
    {
        return this._has_htmlInput;
    } //-- boolean hasHtmlInput() 

    /**
     * Method hasInForm
     */
    public boolean hasInForm()
    {
        return this._has_inForm;
    } //-- boolean hasInForm() 

    /**
     * Method hasWordWrap
     */
    public boolean hasWordWrap()
    {
        return this._has_wordWrap;
    } //-- boolean hasWordWrap() 

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
     * Sets the value of field 'caption'.
     * 
     * @param caption the value of field 'caption'.
     */
    public void setCaption(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption caption)
    {
        this._caption = caption;
    } //-- void setCaption(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption) 

    /**
     * Sets the value of field 'contentType'.
     * 
     * @param contentType the value of field 'contentType'.
     */
    public void setContentType(de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType contentType)
    {
        this._contentType = contentType;
    } //-- void setContentType(de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType) 

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
     * Sets the value of field 'htmlInput'.
     * 
     * @param htmlInput the value of field 'htmlInput'.
     */
    public void setHtmlInput(boolean htmlInput)
    {
        this._htmlInput = htmlInput;
        this._has_htmlInput = true;
    } //-- void setHtmlInput(boolean) 

    /**
     * Sets the value of field 'inForm'.
     * 
     * @param inForm the value of field 'inForm'.
     */
    public void setInForm(boolean inForm)
    {
        this._inForm = inForm;
        this._has_inForm = true;
    } //-- void setInForm(boolean) 

    /**
     * Sets the value of field 'mode'.
     * 
     * @param mode the value of field 'mode'.
     */
    public void setMode(de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType mode)
    {
        this._mode = mode;
    } //-- void setMode(de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType) 

    /**
     * Sets the value of field 'wordWrap'.
     * 
     * @param wordWrap the value of field 'wordWrap'.
     */
    public void setWordWrap(boolean wordWrap)
    {
        this._wordWrap = wordWrap;
        this._has_wordWrap = true;
    } //-- void setWordWrap(boolean) 

    /**
     * Method unmarshalLongTextInput
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLongTextInput(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.LongTextInput) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.LongTextInput.class, reader);
    } //-- java.lang.Object unmarshalLongTextInput(java.io.Reader) 

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
