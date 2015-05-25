/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.qes.adf.castor;

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
 * Class Masked.
 * 
 * @version $Revision$ $Date$
 */
public class Masked implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _mask
     */
    private java.lang.String _mask;

    /**
     * Field _tabIndex
     */
    private int _tabIndex;

    /**
     * keeps track of state for field: _tabIndex
     */
    private boolean _has_tabIndex;

    /**
     * Field _invisible
     */
    private java.lang.String _invisible;

    /**
     * Field _displayType
     */
    private java.lang.String _displayType;

    /**
     * Field _position
     */
    private de.tif.qes.adf.castor.Position _position;

    /**
     * Field _caption
     */
    private de.tif.qes.adf.castor.Caption _caption;

    /**
     * Field _ADL
     */
    private de.tif.qes.adf.castor.ADL _ADL;

    /**
     * Field _modes
     */
    private de.tif.qes.adf.castor.Modes _modes;


      //----------------/
     //- Constructors -/
    //----------------/

    public Masked() {
        super();
    } //-- de.tif.qes.adf.castor.Masked()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteTabIndex
     */
    public void deleteTabIndex()
    {
        this._has_tabIndex= false;
    } //-- void deleteTabIndex() 

    /**
     * Returns the value of field 'ADL'.
     * 
     * @return the value of field 'ADL'.
     */
    public de.tif.qes.adf.castor.ADL getADL()
    {
        return this._ADL;
    } //-- de.tif.qes.adf.castor.ADL getADL() 

    /**
     * Returns the value of field 'caption'.
     * 
     * @return the value of field 'caption'.
     */
    public de.tif.qes.adf.castor.Caption getCaption()
    {
        return this._caption;
    } //-- de.tif.qes.adf.castor.Caption getCaption() 

    /**
     * Returns the value of field 'displayType'.
     * 
     * @return the value of field 'displayType'.
     */
    public java.lang.String getDisplayType()
    {
        return this._displayType;
    } //-- java.lang.String getDisplayType() 

    /**
     * Returns the value of field 'invisible'.
     * 
     * @return the value of field 'invisible'.
     */
    public java.lang.String getInvisible()
    {
        return this._invisible;
    } //-- java.lang.String getInvisible() 

    /**
     * Returns the value of field 'mask'.
     * 
     * @return the value of field 'mask'.
     */
    public java.lang.String getMask()
    {
        return this._mask;
    } //-- java.lang.String getMask() 

    /**
     * Returns the value of field 'modes'.
     * 
     * @return the value of field 'modes'.
     */
    public de.tif.qes.adf.castor.Modes getModes()
    {
        return this._modes;
    } //-- de.tif.qes.adf.castor.Modes getModes() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'position'.
     * 
     * @return the value of field 'position'.
     */
    public de.tif.qes.adf.castor.Position getPosition()
    {
        return this._position;
    } //-- de.tif.qes.adf.castor.Position getPosition() 

    /**
     * Returns the value of field 'tabIndex'.
     * 
     * @return the value of field 'tabIndex'.
     */
    public int getTabIndex()
    {
        return this._tabIndex;
    } //-- int getTabIndex() 

    /**
     * Method hasTabIndex
     */
    public boolean hasTabIndex()
    {
        return this._has_tabIndex;
    } //-- boolean hasTabIndex() 

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
     * Sets the value of field 'ADL'.
     * 
     * @param ADL the value of field 'ADL'.
     */
    public void setADL(de.tif.qes.adf.castor.ADL ADL)
    {
        this._ADL = ADL;
    } //-- void setADL(de.tif.qes.adf.castor.ADL) 

    /**
     * Sets the value of field 'caption'.
     * 
     * @param caption the value of field 'caption'.
     */
    public void setCaption(de.tif.qes.adf.castor.Caption caption)
    {
        this._caption = caption;
    } //-- void setCaption(de.tif.qes.adf.castor.Caption) 

    /**
     * Sets the value of field 'displayType'.
     * 
     * @param displayType the value of field 'displayType'.
     */
    public void setDisplayType(java.lang.String displayType)
    {
        this._displayType = displayType;
    } //-- void setDisplayType(java.lang.String) 

    /**
     * Sets the value of field 'invisible'.
     * 
     * @param invisible the value of field 'invisible'.
     */
    public void setInvisible(java.lang.String invisible)
    {
        this._invisible = invisible;
    } //-- void setInvisible(java.lang.String) 

    /**
     * Sets the value of field 'mask'.
     * 
     * @param mask the value of field 'mask'.
     */
    public void setMask(java.lang.String mask)
    {
        this._mask = mask;
    } //-- void setMask(java.lang.String) 

    /**
     * Sets the value of field 'modes'.
     * 
     * @param modes the value of field 'modes'.
     */
    public void setModes(de.tif.qes.adf.castor.Modes modes)
    {
        this._modes = modes;
    } //-- void setModes(de.tif.qes.adf.castor.Modes) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'position'.
     * 
     * @param position the value of field 'position'.
     */
    public void setPosition(de.tif.qes.adf.castor.Position position)
    {
        this._position = position;
    } //-- void setPosition(de.tif.qes.adf.castor.Position) 

    /**
     * Sets the value of field 'tabIndex'.
     * 
     * @param tabIndex the value of field 'tabIndex'.
     */
    public void setTabIndex(int tabIndex)
    {
        this._tabIndex = tabIndex;
        this._has_tabIndex = true;
    } //-- void setTabIndex(int) 

    /**
     * Method unmarshalMasked
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMasked(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Masked) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Masked.class, reader);
    } //-- java.lang.Object unmarshalMasked(java.io.Reader) 

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
