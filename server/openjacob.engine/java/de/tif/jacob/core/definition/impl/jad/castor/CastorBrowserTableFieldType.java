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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder;
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
 * Class CastorBrowserTableFieldType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorBrowserTableFieldType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableField
     */
    private java.lang.String _tableField;

    /**
     * Field _sortOrder
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder _sortOrder;

    /**
     * Field _foreign
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType _foreign;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorBrowserTableFieldType() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'foreign'.
     * 
     * @return the value of field 'foreign'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType getForeign()
    {
        return this._foreign;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType getForeign() 

    /**
     * Returns the value of field 'sortOrder'.
     * 
     * @return the value of field 'sortOrder'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder getSortOrder()
    {
        return this._sortOrder;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder getSortOrder() 

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
     * Sets the value of field 'foreign'.
     * 
     * @param foreign the value of field 'foreign'.
     */
    public void setForeign(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType foreign)
    {
        this._foreign = foreign;
    } //-- void setForeign(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType) 

    /**
     * Sets the value of field 'sortOrder'.
     * 
     * @param sortOrder the value of field 'sortOrder'.
     */
    public void setSortOrder(de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder sortOrder)
    {
        this._sortOrder = sortOrder;
    } //-- void setSortOrder(de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder) 

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
     * Method unmarshalCastorBrowserTableFieldType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorBrowserTableFieldType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType.class, reader);
    } //-- java.lang.Object unmarshalCastorBrowserTableFieldType(java.io.Reader) 

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
