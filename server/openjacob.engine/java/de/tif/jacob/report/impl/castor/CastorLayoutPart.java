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
 * Class CastorLayoutPart.
 * 
 * @version $Revision$ $Date$
 */
public class CastorLayoutPart implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ident
     */
    private int _ident;

    /**
     * keeps track of state for field: _ident
     */
    private boolean _has_ident;

    /**
     * Field _group
     */
    private de.tif.jacob.report.impl.castor.CastorGroup _group;

    /**
     * Field _columns
     */
    private de.tif.jacob.report.impl.castor.CastorLayoutColumns _columns;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorLayoutPart() {
        super();
    } //-- de.tif.jacob.report.impl.castor.CastorLayoutPart()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIdent
     */
    public void deleteIdent()
    {
        this._has_ident= false;
    } //-- void deleteIdent() 

    /**
     * Returns the value of field 'columns'.
     * 
     * @return the value of field 'columns'.
     */
    public de.tif.jacob.report.impl.castor.CastorLayoutColumns getColumns()
    {
        return this._columns;
    } //-- de.tif.jacob.report.impl.castor.CastorLayoutColumns getColumns() 

    /**
     * Returns the value of field 'group'.
     * 
     * @return the value of field 'group'.
     */
    public de.tif.jacob.report.impl.castor.CastorGroup getGroup()
    {
        return this._group;
    } //-- de.tif.jacob.report.impl.castor.CastorGroup getGroup() 

    /**
     * Returns the value of field 'ident'.
     * 
     * @return the value of field 'ident'.
     */
    public int getIdent()
    {
        return this._ident;
    } //-- int getIdent() 

    /**
     * Method hasIdent
     */
    public boolean hasIdent()
    {
        return this._has_ident;
    } //-- boolean hasIdent() 

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
     * Sets the value of field 'columns'.
     * 
     * @param columns the value of field 'columns'.
     */
    public void setColumns(de.tif.jacob.report.impl.castor.CastorLayoutColumns columns)
    {
        this._columns = columns;
    } //-- void setColumns(de.tif.jacob.report.impl.castor.CastorLayoutColumns) 

    /**
     * Sets the value of field 'group'.
     * 
     * @param group the value of field 'group'.
     */
    public void setGroup(de.tif.jacob.report.impl.castor.CastorGroup group)
    {
        this._group = group;
    } //-- void setGroup(de.tif.jacob.report.impl.castor.CastorGroup) 

    /**
     * Sets the value of field 'ident'.
     * 
     * @param ident the value of field 'ident'.
     */
    public void setIdent(int ident)
    {
        this._ident = ident;
        this._has_ident = true;
    } //-- void setIdent(int) 

    /**
     * Method unmarshalCastorLayoutPart
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorLayoutPart(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorLayoutPart) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorLayoutPart.class, reader);
    } //-- java.lang.Object unmarshalCastorLayoutPart(java.io.Reader) 

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
