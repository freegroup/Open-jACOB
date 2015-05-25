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
 * Class CastorLayout.
 * 
 * @version $Revision$ $Date$
 */
public class CastorLayout implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mimeType
     */
    private java.lang.String _mimeType;

    /**
     * Field _width
     */
    private int _width;

    /**
     * keeps track of state for field: _width
     */
    private boolean _has_width;

    /**
     * Field _height
     */
    private int _height;

    /**
     * keeps track of state for field: _height
     */
    private boolean _has_height;

    /**
     * Field _spacing
     */
    private int _spacing;

    /**
     * keeps track of state for field: _spacing
     */
    private boolean _has_spacing;

    /**
     * Field _prologue
     */
    private de.tif.jacob.report.impl.castor.CastorCaption _prologue;

    /**
     * Field _pageHeader
     */
    private de.tif.jacob.report.impl.castor.CastorCaption _pageHeader;

    /**
     * Field _recordHeader
     */
    private de.tif.jacob.report.impl.castor.CastorCaption _recordHeader;

    /**
     * Field _part
     */
    private de.tif.jacob.report.impl.castor.CastorLayoutPart _part;

    /**
     * Field _recordFooter
     */
    private de.tif.jacob.report.impl.castor.CastorCaption _recordFooter;

    /**
     * Field _pageFooter
     */
    private de.tif.jacob.report.impl.castor.CastorCaption _pageFooter;

    /**
     * Field _epilogue
     */
    private de.tif.jacob.report.impl.castor.CastorCaption _epilogue;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorLayout() {
        super();
    } //-- de.tif.jacob.report.impl.castor.CastorLayout()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteHeight
     */
    public void deleteHeight()
    {
        this._has_height= false;
    } //-- void deleteHeight() 

    /**
     * Method deleteSpacing
     */
    public void deleteSpacing()
    {
        this._has_spacing= false;
    } //-- void deleteSpacing() 

    /**
     * Method deleteWidth
     */
    public void deleteWidth()
    {
        this._has_width= false;
    } //-- void deleteWidth() 

    /**
     * Returns the value of field 'epilogue'.
     * 
     * @return the value of field 'epilogue'.
     */
    public de.tif.jacob.report.impl.castor.CastorCaption getEpilogue()
    {
        return this._epilogue;
    } //-- de.tif.jacob.report.impl.castor.CastorCaption getEpilogue() 

    /**
     * Returns the value of field 'height'.
     * 
     * @return the value of field 'height'.
     */
    public int getHeight()
    {
        return this._height;
    } //-- int getHeight() 

    /**
     * Returns the value of field 'mimeType'.
     * 
     * @return the value of field 'mimeType'.
     */
    public java.lang.String getMimeType()
    {
        return this._mimeType;
    } //-- java.lang.String getMimeType() 

    /**
     * Returns the value of field 'pageFooter'.
     * 
     * @return the value of field 'pageFooter'.
     */
    public de.tif.jacob.report.impl.castor.CastorCaption getPageFooter()
    {
        return this._pageFooter;
    } //-- de.tif.jacob.report.impl.castor.CastorCaption getPageFooter() 

    /**
     * Returns the value of field 'pageHeader'.
     * 
     * @return the value of field 'pageHeader'.
     */
    public de.tif.jacob.report.impl.castor.CastorCaption getPageHeader()
    {
        return this._pageHeader;
    } //-- de.tif.jacob.report.impl.castor.CastorCaption getPageHeader() 

    /**
     * Returns the value of field 'part'.
     * 
     * @return the value of field 'part'.
     */
    public de.tif.jacob.report.impl.castor.CastorLayoutPart getPart()
    {
        return this._part;
    } //-- de.tif.jacob.report.impl.castor.CastorLayoutPart getPart() 

    /**
     * Returns the value of field 'prologue'.
     * 
     * @return the value of field 'prologue'.
     */
    public de.tif.jacob.report.impl.castor.CastorCaption getPrologue()
    {
        return this._prologue;
    } //-- de.tif.jacob.report.impl.castor.CastorCaption getPrologue() 

    /**
     * Returns the value of field 'recordFooter'.
     * 
     * @return the value of field 'recordFooter'.
     */
    public de.tif.jacob.report.impl.castor.CastorCaption getRecordFooter()
    {
        return this._recordFooter;
    } //-- de.tif.jacob.report.impl.castor.CastorCaption getRecordFooter() 

    /**
     * Returns the value of field 'recordHeader'.
     * 
     * @return the value of field 'recordHeader'.
     */
    public de.tif.jacob.report.impl.castor.CastorCaption getRecordHeader()
    {
        return this._recordHeader;
    } //-- de.tif.jacob.report.impl.castor.CastorCaption getRecordHeader() 

    /**
     * Returns the value of field 'spacing'.
     * 
     * @return the value of field 'spacing'.
     */
    public int getSpacing()
    {
        return this._spacing;
    } //-- int getSpacing() 

    /**
     * Returns the value of field 'width'.
     * 
     * @return the value of field 'width'.
     */
    public int getWidth()
    {
        return this._width;
    } //-- int getWidth() 

    /**
     * Method hasHeight
     */
    public boolean hasHeight()
    {
        return this._has_height;
    } //-- boolean hasHeight() 

    /**
     * Method hasSpacing
     */
    public boolean hasSpacing()
    {
        return this._has_spacing;
    } //-- boolean hasSpacing() 

    /**
     * Method hasWidth
     */
    public boolean hasWidth()
    {
        return this._has_width;
    } //-- boolean hasWidth() 

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
     * Sets the value of field 'epilogue'.
     * 
     * @param epilogue the value of field 'epilogue'.
     */
    public void setEpilogue(de.tif.jacob.report.impl.castor.CastorCaption epilogue)
    {
        this._epilogue = epilogue;
    } //-- void setEpilogue(de.tif.jacob.report.impl.castor.CastorCaption) 

    /**
     * Sets the value of field 'height'.
     * 
     * @param height the value of field 'height'.
     */
    public void setHeight(int height)
    {
        this._height = height;
        this._has_height = true;
    } //-- void setHeight(int) 

    /**
     * Sets the value of field 'mimeType'.
     * 
     * @param mimeType the value of field 'mimeType'.
     */
    public void setMimeType(java.lang.String mimeType)
    {
        this._mimeType = mimeType;
    } //-- void setMimeType(java.lang.String) 

    /**
     * Sets the value of field 'pageFooter'.
     * 
     * @param pageFooter the value of field 'pageFooter'.
     */
    public void setPageFooter(de.tif.jacob.report.impl.castor.CastorCaption pageFooter)
    {
        this._pageFooter = pageFooter;
    } //-- void setPageFooter(de.tif.jacob.report.impl.castor.CastorCaption) 

    /**
     * Sets the value of field 'pageHeader'.
     * 
     * @param pageHeader the value of field 'pageHeader'.
     */
    public void setPageHeader(de.tif.jacob.report.impl.castor.CastorCaption pageHeader)
    {
        this._pageHeader = pageHeader;
    } //-- void setPageHeader(de.tif.jacob.report.impl.castor.CastorCaption) 

    /**
     * Sets the value of field 'part'.
     * 
     * @param part the value of field 'part'.
     */
    public void setPart(de.tif.jacob.report.impl.castor.CastorLayoutPart part)
    {
        this._part = part;
    } //-- void setPart(de.tif.jacob.report.impl.castor.CastorLayoutPart) 

    /**
     * Sets the value of field 'prologue'.
     * 
     * @param prologue the value of field 'prologue'.
     */
    public void setPrologue(de.tif.jacob.report.impl.castor.CastorCaption prologue)
    {
        this._prologue = prologue;
    } //-- void setPrologue(de.tif.jacob.report.impl.castor.CastorCaption) 

    /**
     * Sets the value of field 'recordFooter'.
     * 
     * @param recordFooter the value of field 'recordFooter'.
     */
    public void setRecordFooter(de.tif.jacob.report.impl.castor.CastorCaption recordFooter)
    {
        this._recordFooter = recordFooter;
    } //-- void setRecordFooter(de.tif.jacob.report.impl.castor.CastorCaption) 

    /**
     * Sets the value of field 'recordHeader'.
     * 
     * @param recordHeader the value of field 'recordHeader'.
     */
    public void setRecordHeader(de.tif.jacob.report.impl.castor.CastorCaption recordHeader)
    {
        this._recordHeader = recordHeader;
    } //-- void setRecordHeader(de.tif.jacob.report.impl.castor.CastorCaption) 

    /**
     * Sets the value of field 'spacing'.
     * 
     * @param spacing the value of field 'spacing'.
     */
    public void setSpacing(int spacing)
    {
        this._spacing = spacing;
        this._has_spacing = true;
    } //-- void setSpacing(int) 

    /**
     * Sets the value of field 'width'.
     * 
     * @param width the value of field 'width'.
     */
    public void setWidth(int width)
    {
        this._width = width;
        this._has_width = true;
    } //-- void setWidth(int) 

    /**
     * Method unmarshalCastorLayout
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorLayout(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorLayout) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorLayout.class, reader);
    } //-- java.lang.Object unmarshalCastorLayout(java.io.Reader) 

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
