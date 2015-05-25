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
 * Class CastorGroup.
 * 
 * @version $Revision$ $Date$
 */
public class CastorGroup implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableAlias
     */
    private java.lang.String _tableAlias;

    /**
     * Field _field
     */
    private java.lang.String _field;

    /**
     * Field _header
     */
    private de.tif.jacob.report.impl.castor.CastorCaption _header;

    /**
     * Field _part
     */
    private de.tif.jacob.report.impl.castor.CastorLayoutPart _part;

    /**
     * Field _footer
     */
    private de.tif.jacob.report.impl.castor.CastorCaption _footer;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorGroup() {
        super();
    } //-- de.tif.jacob.report.impl.castor.CastorGroup()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'field'.
     * 
     * @return the value of field 'field'.
     */
    public java.lang.String getField()
    {
        return this._field;
    } //-- java.lang.String getField() 

    /**
     * Returns the value of field 'footer'.
     * 
     * @return the value of field 'footer'.
     */
    public de.tif.jacob.report.impl.castor.CastorCaption getFooter()
    {
        return this._footer;
    } //-- de.tif.jacob.report.impl.castor.CastorCaption getFooter() 

    /**
     * Returns the value of field 'header'.
     * 
     * @return the value of field 'header'.
     */
    public de.tif.jacob.report.impl.castor.CastorCaption getHeader()
    {
        return this._header;
    } //-- de.tif.jacob.report.impl.castor.CastorCaption getHeader() 

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
     * Returns the value of field 'tableAlias'.
     * 
     * @return the value of field 'tableAlias'.
     */
    public java.lang.String getTableAlias()
    {
        return this._tableAlias;
    } //-- java.lang.String getTableAlias() 

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
     * Sets the value of field 'field'.
     * 
     * @param field the value of field 'field'.
     */
    public void setField(java.lang.String field)
    {
        this._field = field;
    } //-- void setField(java.lang.String) 

    /**
     * Sets the value of field 'footer'.
     * 
     * @param footer the value of field 'footer'.
     */
    public void setFooter(de.tif.jacob.report.impl.castor.CastorCaption footer)
    {
        this._footer = footer;
    } //-- void setFooter(de.tif.jacob.report.impl.castor.CastorCaption) 

    /**
     * Sets the value of field 'header'.
     * 
     * @param header the value of field 'header'.
     */
    public void setHeader(de.tif.jacob.report.impl.castor.CastorCaption header)
    {
        this._header = header;
    } //-- void setHeader(de.tif.jacob.report.impl.castor.CastorCaption) 

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
     * Sets the value of field 'tableAlias'.
     * 
     * @param tableAlias the value of field 'tableAlias'.
     */
    public void setTableAlias(java.lang.String tableAlias)
    {
        this._tableAlias = tableAlias;
    } //-- void setTableAlias(java.lang.String) 

    /**
     * Method unmarshalCastorGroup
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorGroup(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorGroup) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorGroup.class, reader);
    } //-- java.lang.Object unmarshalCastorGroup(java.io.Reader) 

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
