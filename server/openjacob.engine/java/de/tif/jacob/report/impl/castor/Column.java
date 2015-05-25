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

import de.tif.jacob.report.impl.castor.types.SortOrderType;
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
 * Class Column.
 * 
 * @version $Revision$ $Date$
 */
public class Column implements java.io.Serializable {


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
     * Field _label
     */
    private java.lang.String _label;

    /**
     * Field _sortOrder
     */
    private de.tif.jacob.report.impl.castor.types.SortOrderType _sortOrder;


      //----------------/
     //- Constructors -/
    //----------------/

    public Column() {
        super();
    } //-- de.tif.jacob.report.impl.castor.Column()


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
     * Returns the value of field 'label'.
     * 
     * @return the value of field 'label'.
     */
    public java.lang.String getLabel()
    {
        return this._label;
    } //-- java.lang.String getLabel() 

    /**
     * Returns the value of field 'sortOrder'.
     * 
     * @return the value of field 'sortOrder'.
     */
    public de.tif.jacob.report.impl.castor.types.SortOrderType getSortOrder()
    {
        return this._sortOrder;
    } //-- de.tif.jacob.report.impl.castor.types.SortOrderType getSortOrder() 

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
     * Sets the value of field 'label'.
     * 
     * @param label the value of field 'label'.
     */
    public void setLabel(java.lang.String label)
    {
        this._label = label;
    } //-- void setLabel(java.lang.String) 

    /**
     * Sets the value of field 'sortOrder'.
     * 
     * @param sortOrder the value of field 'sortOrder'.
     */
    public void setSortOrder(de.tif.jacob.report.impl.castor.types.SortOrderType sortOrder)
    {
        this._sortOrder = sortOrder;
    } //-- void setSortOrder(de.tif.jacob.report.impl.castor.types.SortOrderType) 

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
     * Method unmarshalColumn
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalColumn(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.Column) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.Column.class, reader);
    } //-- java.lang.Object unmarshalColumn(java.io.Reader) 

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
