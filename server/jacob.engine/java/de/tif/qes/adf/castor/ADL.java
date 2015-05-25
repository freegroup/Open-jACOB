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
 * Class ADL.
 * 
 * @version $Revision$ $Date$
 */
public class ADL implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _relationSet
     */
    private java.lang.String _relationSet;

    /**
     * Field _fillDirection
     */
    private java.lang.String _fillDirection;

    /**
     * Field _table
     */
    private java.lang.String _table;

    /**
     * Field _field
     */
    private java.lang.String _field;

    /**
     * Field _default
     */
    private java.lang.String _default;

    /**
     * Field _browser
     */
    private java.lang.String _browser;

    /**
     * Field _relation
     */
    private java.lang.String _relation;


      //----------------/
     //- Constructors -/
    //----------------/

    public ADL() {
        super();
    } //-- de.tif.qes.adf.castor.ADL()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'browser'.
     * 
     * @return the value of field 'browser'.
     */
    public java.lang.String getBrowser()
    {
        return this._browser;
    } //-- java.lang.String getBrowser() 

    /**
     * Returns the value of field 'default'.
     * 
     * @return the value of field 'default'.
     */
    public java.lang.String getDefault()
    {
        return this._default;
    } //-- java.lang.String getDefault() 

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
     * Returns the value of field 'fillDirection'.
     * 
     * @return the value of field 'fillDirection'.
     */
    public java.lang.String getFillDirection()
    {
        return this._fillDirection;
    } //-- java.lang.String getFillDirection() 

    /**
     * Returns the value of field 'relation'.
     * 
     * @return the value of field 'relation'.
     */
    public java.lang.String getRelation()
    {
        return this._relation;
    } //-- java.lang.String getRelation() 

    /**
     * Returns the value of field 'relationSet'.
     * 
     * @return the value of field 'relationSet'.
     */
    public java.lang.String getRelationSet()
    {
        return this._relationSet;
    } //-- java.lang.String getRelationSet() 

    /**
     * Returns the value of field 'table'.
     * 
     * @return the value of field 'table'.
     */
    public java.lang.String getTable()
    {
        return this._table;
    } //-- java.lang.String getTable() 

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
     * Sets the value of field 'browser'.
     * 
     * @param browser the value of field 'browser'.
     */
    public void setBrowser(java.lang.String browser)
    {
        this._browser = browser;
    } //-- void setBrowser(java.lang.String) 

    /**
     * Sets the value of field 'default'.
     * 
     * @param _default
     * @param default the value of field 'default'.
     */
    public void setDefault(java.lang.String _default)
    {
        this._default = _default;
    } //-- void setDefault(java.lang.String) 

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
     * Sets the value of field 'fillDirection'.
     * 
     * @param fillDirection the value of field 'fillDirection'.
     */
    public void setFillDirection(java.lang.String fillDirection)
    {
        this._fillDirection = fillDirection;
    } //-- void setFillDirection(java.lang.String) 

    /**
     * Sets the value of field 'relation'.
     * 
     * @param relation the value of field 'relation'.
     */
    public void setRelation(java.lang.String relation)
    {
        this._relation = relation;
    } //-- void setRelation(java.lang.String) 

    /**
     * Sets the value of field 'relationSet'.
     * 
     * @param relationSet the value of field 'relationSet'.
     */
    public void setRelationSet(java.lang.String relationSet)
    {
        this._relationSet = relationSet;
    } //-- void setRelationSet(java.lang.String) 

    /**
     * Sets the value of field 'table'.
     * 
     * @param table the value of field 'table'.
     */
    public void setTable(java.lang.String table)
    {
        this._table = table;
    } //-- void setTable(java.lang.String) 

    /**
     * Method unmarshalADL
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalADL(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.ADL) Unmarshaller.unmarshal(de.tif.qes.adf.castor.ADL.class, reader);
    } //-- java.lang.Object unmarshalADL(java.io.Reader) 

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
