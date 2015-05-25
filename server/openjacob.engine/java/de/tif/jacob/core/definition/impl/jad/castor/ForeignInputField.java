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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
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
 * Class ForeignInputField.
 * 
 * @version $Revision$ $Date$
 */
public class ForeignInputField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _halign
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment _halign;

    /**
     * Field _asComboBox
     */
    private boolean _asComboBox = false;

    /**
     * keeps track of state for field: _asComboBox
     */
    private boolean _has_asComboBox;

    /**
     * Field _foreignAlias
     */
    private java.lang.String _foreignAlias;

    /**
     * Field _foreignTableField
     */
    private java.lang.String _foreignTableField;

    /**
     * Field _relationToUse
     */
    private java.lang.String _relationToUse;

    /**
     * Field _browserToUse
     */
    private java.lang.String _browserToUse;

    /**
     * Field _relationset
     */
    private java.lang.String _relationset;

    /**
     * Field _filldirection
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection _filldirection;

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

    public ForeignInputField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteAsComboBox
     */
    public void deleteAsComboBox()
    {
        this._has_asComboBox= false;
    } //-- void deleteAsComboBox() 

    /**
     * Returns the value of field 'asComboBox'.
     * 
     * @return the value of field 'asComboBox'.
     */
    public boolean getAsComboBox()
    {
        return this._asComboBox;
    } //-- boolean getAsComboBox() 

    /**
     * Returns the value of field 'browserToUse'.
     * 
     * @return the value of field 'browserToUse'.
     */
    public java.lang.String getBrowserToUse()
    {
        return this._browserToUse;
    } //-- java.lang.String getBrowserToUse() 

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
     * Returns the value of field 'filldirection'.
     * 
     * @return the value of field 'filldirection'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection getFilldirection()
    {
        return this._filldirection;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection getFilldirection() 

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
     * Returns the value of field 'foreignAlias'.
     * 
     * @return the value of field 'foreignAlias'.
     */
    public java.lang.String getForeignAlias()
    {
        return this._foreignAlias;
    } //-- java.lang.String getForeignAlias() 

    /**
     * Returns the value of field 'foreignTableField'.
     * 
     * @return the value of field 'foreignTableField'.
     */
    public java.lang.String getForeignTableField()
    {
        return this._foreignTableField;
    } //-- java.lang.String getForeignTableField() 

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
     * Returns the value of field 'relationToUse'.
     * 
     * @return the value of field 'relationToUse'.
     */
    public java.lang.String getRelationToUse()
    {
        return this._relationToUse;
    } //-- java.lang.String getRelationToUse() 

    /**
     * Returns the value of field 'relationset'.
     * 
     * @return the value of field 'relationset'.
     */
    public java.lang.String getRelationset()
    {
        return this._relationset;
    } //-- java.lang.String getRelationset() 

    /**
     * Method hasAsComboBox
     */
    public boolean hasAsComboBox()
    {
        return this._has_asComboBox;
    } //-- boolean hasAsComboBox() 

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
     * Sets the value of field 'asComboBox'.
     * 
     * @param asComboBox the value of field 'asComboBox'.
     */
    public void setAsComboBox(boolean asComboBox)
    {
        this._asComboBox = asComboBox;
        this._has_asComboBox = true;
    } //-- void setAsComboBox(boolean) 

    /**
     * Sets the value of field 'browserToUse'.
     * 
     * @param browserToUse the value of field 'browserToUse'.
     */
    public void setBrowserToUse(java.lang.String browserToUse)
    {
        this._browserToUse = browserToUse;
    } //-- void setBrowserToUse(java.lang.String) 

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
     * Sets the value of field 'filldirection'.
     * 
     * @param filldirection the value of field 'filldirection'.
     */
    public void setFilldirection(de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection filldirection)
    {
        this._filldirection = filldirection;
    } //-- void setFilldirection(de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection) 

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
     * Sets the value of field 'foreignAlias'.
     * 
     * @param foreignAlias the value of field 'foreignAlias'.
     */
    public void setForeignAlias(java.lang.String foreignAlias)
    {
        this._foreignAlias = foreignAlias;
    } //-- void setForeignAlias(java.lang.String) 

    /**
     * Sets the value of field 'foreignTableField'.
     * 
     * @param foreignTableField the value of field
     * 'foreignTableField'.
     */
    public void setForeignTableField(java.lang.String foreignTableField)
    {
        this._foreignTableField = foreignTableField;
    } //-- void setForeignTableField(java.lang.String) 

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
     * Sets the value of field 'relationToUse'.
     * 
     * @param relationToUse the value of field 'relationToUse'.
     */
    public void setRelationToUse(java.lang.String relationToUse)
    {
        this._relationToUse = relationToUse;
    } //-- void setRelationToUse(java.lang.String) 

    /**
     * Sets the value of field 'relationset'.
     * 
     * @param relationset the value of field 'relationset'.
     */
    public void setRelationset(java.lang.String relationset)
    {
        this._relationset = relationset;
    } //-- void setRelationset(java.lang.String) 

    /**
     * Method unmarshalForeignInputField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalForeignInputField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField.class, reader);
    } //-- java.lang.Object unmarshalForeignInputField(java.io.Reader) 

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
