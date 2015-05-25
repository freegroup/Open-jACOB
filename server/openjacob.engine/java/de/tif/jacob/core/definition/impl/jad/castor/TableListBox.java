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
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
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
 * Class TableListBox.
 * 
 * @version $Revision$ $Date$
 */
public class TableListBox implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _halign
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment _halign;

    /**
     * Field _tableAlias
     */
    private java.lang.String _tableAlias;

    /**
     * Field _displayField
     */
    private java.lang.String _displayField;

    /**
     * Field _deleteModeSelected
     */
    private boolean _deleteModeSelected = false;

    /**
     * keeps track of state for field: _deleteModeSelected
     */
    private boolean _has_deleteModeSelected;

    /**
     * Field _deleteModeUpdateNew
     */
    private boolean _deleteModeUpdateNew = false;

    /**
     * keeps track of state for field: _deleteModeUpdateNew
     */
    private boolean _has_deleteModeUpdateNew;

    /**
     * Field _browserToUse
     */
    private java.lang.String _browserToUse;

    /**
     * Field _resizeMode
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode _resizeMode;

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

    public TableListBox() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TableListBox()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteDeleteModeSelected
     */
    public void deleteDeleteModeSelected()
    {
        this._has_deleteModeSelected= false;
    } //-- void deleteDeleteModeSelected() 

    /**
     * Method deleteDeleteModeUpdateNew
     */
    public void deleteDeleteModeUpdateNew()
    {
        this._has_deleteModeUpdateNew= false;
    } //-- void deleteDeleteModeUpdateNew() 

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
     * Returns the value of field 'deleteModeSelected'.
     * 
     * @return the value of field 'deleteModeSelected'.
     */
    public boolean getDeleteModeSelected()
    {
        return this._deleteModeSelected;
    } //-- boolean getDeleteModeSelected() 

    /**
     * Returns the value of field 'deleteModeUpdateNew'.
     * 
     * @return the value of field 'deleteModeUpdateNew'.
     */
    public boolean getDeleteModeUpdateNew()
    {
        return this._deleteModeUpdateNew;
    } //-- boolean getDeleteModeUpdateNew() 

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
     * Returns the value of field 'displayField'.
     * 
     * @return the value of field 'displayField'.
     */
    public java.lang.String getDisplayField()
    {
        return this._displayField;
    } //-- java.lang.String getDisplayField() 

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
     * Returns the value of field 'resizeMode'.
     * 
     * @return the value of field 'resizeMode'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode getResizeMode()
    {
        return this._resizeMode;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode getResizeMode() 

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
     * Method hasDeleteModeSelected
     */
    public boolean hasDeleteModeSelected()
    {
        return this._has_deleteModeSelected;
    } //-- boolean hasDeleteModeSelected() 

    /**
     * Method hasDeleteModeUpdateNew
     */
    public boolean hasDeleteModeUpdateNew()
    {
        return this._has_deleteModeUpdateNew;
    } //-- boolean hasDeleteModeUpdateNew() 

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
     * Sets the value of field 'deleteModeSelected'.
     * 
     * @param deleteModeSelected the value of field
     * 'deleteModeSelected'.
     */
    public void setDeleteModeSelected(boolean deleteModeSelected)
    {
        this._deleteModeSelected = deleteModeSelected;
        this._has_deleteModeSelected = true;
    } //-- void setDeleteModeSelected(boolean) 

    /**
     * Sets the value of field 'deleteModeUpdateNew'.
     * 
     * @param deleteModeUpdateNew the value of field
     * 'deleteModeUpdateNew'.
     */
    public void setDeleteModeUpdateNew(boolean deleteModeUpdateNew)
    {
        this._deleteModeUpdateNew = deleteModeUpdateNew;
        this._has_deleteModeUpdateNew = true;
    } //-- void setDeleteModeUpdateNew(boolean) 

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
     * Sets the value of field 'displayField'.
     * 
     * @param displayField the value of field 'displayField'.
     */
    public void setDisplayField(java.lang.String displayField)
    {
        this._displayField = displayField;
    } //-- void setDisplayField(java.lang.String) 

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
     * Sets the value of field 'resizeMode'.
     * 
     * @param resizeMode the value of field 'resizeMode'.
     */
    public void setResizeMode(de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode resizeMode)
    {
        this._resizeMode = resizeMode;
    } //-- void setResizeMode(de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode) 

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
     * Method unmarshalTableListBox
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTableListBox(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.TableListBox) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.TableListBox.class, reader);
    } //-- java.lang.Object unmarshalTableListBox(java.io.Reader) 

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
