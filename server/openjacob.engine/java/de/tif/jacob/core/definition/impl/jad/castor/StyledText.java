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
 * Class StyledText.
 * 
 * @version $Revision$ $Date$
 */
public class StyledText implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _i18nkey
     */
    private java.lang.String _i18nkey;

    /**
     * Field _postProcessWithLetterEngine
     */
    private boolean _postProcessWithLetterEngine = false;

    /**
     * keeps track of state for field: _postProcessWithLetterEngine
     */
    private boolean _has_postProcessWithLetterEngine;

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;


      //----------------/
     //- Constructors -/
    //----------------/

    public StyledText() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.StyledText()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deletePostProcessWithLetterEngine
     */
    public void deletePostProcessWithLetterEngine()
    {
        this._has_postProcessWithLetterEngine= false;
    } //-- void deletePostProcessWithLetterEngine() 

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
     * Returns the value of field 'i18nkey'.
     * 
     * @return the value of field 'i18nkey'.
     */
    public java.lang.String getI18nkey()
    {
        return this._i18nkey;
    } //-- java.lang.String getI18nkey() 

    /**
     * Returns the value of field 'postProcessWithLetterEngine'.
     * 
     * @return the value of field 'postProcessWithLetterEngine'.
     */
    public boolean getPostProcessWithLetterEngine()
    {
        return this._postProcessWithLetterEngine;
    } //-- boolean getPostProcessWithLetterEngine() 

    /**
     * Method hasPostProcessWithLetterEngine
     */
    public boolean hasPostProcessWithLetterEngine()
    {
        return this._has_postProcessWithLetterEngine;
    } //-- boolean hasPostProcessWithLetterEngine() 

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
     * Sets the value of field 'dimension'.
     * 
     * @param dimension the value of field 'dimension'.
     */
    public void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension dimension)
    {
        this._dimension = dimension;
    } //-- void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension) 

    /**
     * Sets the value of field 'i18nkey'.
     * 
     * @param i18nkey the value of field 'i18nkey'.
     */
    public void setI18nkey(java.lang.String i18nkey)
    {
        this._i18nkey = i18nkey;
    } //-- void setI18nkey(java.lang.String) 

    /**
     * Sets the value of field 'postProcessWithLetterEngine'.
     * 
     * @param postProcessWithLetterEngine the value of field
     * 'postProcessWithLetterEngine'.
     */
    public void setPostProcessWithLetterEngine(boolean postProcessWithLetterEngine)
    {
        this._postProcessWithLetterEngine = postProcessWithLetterEngine;
        this._has_postProcessWithLetterEngine = true;
    } //-- void setPostProcessWithLetterEngine(boolean) 

    /**
     * Method unmarshalStyledText
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalStyledText(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.StyledText) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.StyledText.class, reader);
    } //-- java.lang.Object unmarshalStyledText(java.io.Reader) 

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
