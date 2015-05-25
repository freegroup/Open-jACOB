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
 * Class SortField.
 * 
 * @version $Revision$ $Date$
 */
public class SortField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fieldName
     */
    private java.lang.String _fieldName;

    /**
     * Field _sortOrder
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder _sortOrder;


      //----------------/
     //- Constructors -/
    //----------------/

    public SortField() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.SortField()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'fieldName'.
     * 
     * @return the value of field 'fieldName'.
     */
    public java.lang.String getFieldName()
    {
        return this._fieldName;
    } //-- java.lang.String getFieldName() 

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
     * Sets the value of field 'fieldName'.
     * 
     * @param fieldName the value of field 'fieldName'.
     */
    public void setFieldName(java.lang.String fieldName)
    {
        this._fieldName = fieldName;
    } //-- void setFieldName(java.lang.String) 

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
     * Method unmarshalSortField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSortField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.SortField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.SortField.class, reader);
    } //-- java.lang.Object unmarshalSortField(java.io.Reader) 

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
