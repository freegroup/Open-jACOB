/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.report.impl.castor;

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
 * Class CastorCaption.
 * 
 * @version $Revision$ $Date$
 */
public class CastorCaption implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _visible
     */
    private boolean _visible = true;

    /**
     * keeps track of state for field: _visible
     */
    private boolean _has_visible;

    /**
     * Field _expanded
     */
    private boolean _expanded = true;

    /**
     * keeps track of state for field: _expanded
     */
    private boolean _has_expanded;

    /**
     * Field _color
     */
    private java.lang.String _color;

    /**
     * Field _text
     */
    private java.lang.String _text;

    /**
     * Field _font
     */
    private de.tif.jacob.report.impl.castor.CastorFont _font;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorCaption() {
        super();
    } //-- de.tif.jacob.report.impl.castor.CastorCaption()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteExpanded
     */
    public void deleteExpanded()
    {
        this._has_expanded= false;
    } //-- void deleteExpanded() 

    /**
     * Method deleteVisible
     */
    public void deleteVisible()
    {
        this._has_visible= false;
    } //-- void deleteVisible() 

    /**
     * Returns the value of field 'color'.
     * 
     * @return the value of field 'color'.
     */
    public java.lang.String getColor()
    {
        return this._color;
    } //-- java.lang.String getColor() 

    /**
     * Returns the value of field 'expanded'.
     * 
     * @return the value of field 'expanded'.
     */
    public boolean getExpanded()
    {
        return this._expanded;
    } //-- boolean getExpanded() 

    /**
     * Returns the value of field 'font'.
     * 
     * @return the value of field 'font'.
     */
    public de.tif.jacob.report.impl.castor.CastorFont getFont()
    {
        return this._font;
    } //-- de.tif.jacob.report.impl.castor.CastorFont getFont() 

    /**
     * Returns the value of field 'text'.
     * 
     * @return the value of field 'text'.
     */
    public java.lang.String getText()
    {
        return this._text;
    } //-- java.lang.String getText() 

    /**
     * Returns the value of field 'visible'.
     * 
     * @return the value of field 'visible'.
     */
    public boolean getVisible()
    {
        return this._visible;
    } //-- boolean getVisible() 

    /**
     * Method hasExpanded
     */
    public boolean hasExpanded()
    {
        return this._has_expanded;
    } //-- boolean hasExpanded() 

    /**
     * Method hasVisible
     */
    public boolean hasVisible()
    {
        return this._has_visible;
    } //-- boolean hasVisible() 

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
     * Sets the value of field 'color'.
     * 
     * @param color the value of field 'color'.
     */
    public void setColor(java.lang.String color)
    {
        this._color = color;
    } //-- void setColor(java.lang.String) 

    /**
     * Sets the value of field 'expanded'.
     * 
     * @param expanded the value of field 'expanded'.
     */
    public void setExpanded(boolean expanded)
    {
        this._expanded = expanded;
        this._has_expanded = true;
    } //-- void setExpanded(boolean) 

    /**
     * Sets the value of field 'font'.
     * 
     * @param font the value of field 'font'.
     */
    public void setFont(de.tif.jacob.report.impl.castor.CastorFont font)
    {
        this._font = font;
    } //-- void setFont(de.tif.jacob.report.impl.castor.CastorFont) 

    /**
     * Sets the value of field 'text'.
     * 
     * @param text the value of field 'text'.
     */
    public void setText(java.lang.String text)
    {
        this._text = text;
    } //-- void setText(java.lang.String) 

    /**
     * Sets the value of field 'visible'.
     * 
     * @param visible the value of field 'visible'.
     */
    public void setVisible(boolean visible)
    {
        this._visible = visible;
        this._has_visible = true;
    } //-- void setVisible(boolean) 

    /**
     * Method unmarshalCastorCaption
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorCaption(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorCaption) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorCaption.class, reader);
    } //-- java.lang.Object unmarshalCastorCaption(java.io.Reader) 

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
