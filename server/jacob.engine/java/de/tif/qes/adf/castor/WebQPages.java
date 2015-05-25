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
 * Class WebQPages.
 * 
 * @version $Revision$ $Date$
 */
public class WebQPages implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _select
     */
    private java.lang.String _select;

    /**
     * Field _query
     */
    private java.lang.String _query;

    /**
     * Field _new
     */
    private java.lang.String _new;

    /**
     * Field _alias
     */
    private java.lang.String _alias;

    /**
     * Field _update
     */
    private java.lang.String _update;


      //----------------/
     //- Constructors -/
    //----------------/

    public WebQPages() {
        super();
    } //-- de.tif.qes.adf.castor.WebQPages()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'alias'.
     * 
     * @return the value of field 'alias'.
     */
    public java.lang.String getAlias()
    {
        return this._alias;
    } //-- java.lang.String getAlias() 

    /**
     * Returns the value of field 'new'.
     * 
     * @return the value of field 'new'.
     */
    public java.lang.String getNew()
    {
        return this._new;
    } //-- java.lang.String getNew() 

    /**
     * Returns the value of field 'query'.
     * 
     * @return the value of field 'query'.
     */
    public java.lang.String getQuery()
    {
        return this._query;
    } //-- java.lang.String getQuery() 

    /**
     * Returns the value of field 'select'.
     * 
     * @return the value of field 'select'.
     */
    public java.lang.String getSelect()
    {
        return this._select;
    } //-- java.lang.String getSelect() 

    /**
     * Returns the value of field 'update'.
     * 
     * @return the value of field 'update'.
     */
    public java.lang.String getUpdate()
    {
        return this._update;
    } //-- java.lang.String getUpdate() 

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
     * Sets the value of field 'alias'.
     * 
     * @param alias the value of field 'alias'.
     */
    public void setAlias(java.lang.String alias)
    {
        this._alias = alias;
    } //-- void setAlias(java.lang.String) 

    /**
     * Sets the value of field 'new'.
     * 
     * @param _new
     * @param new the value of field 'new'.
     */
    public void setNew(java.lang.String _new)
    {
        this._new = _new;
    } //-- void setNew(java.lang.String) 

    /**
     * Sets the value of field 'query'.
     * 
     * @param query the value of field 'query'.
     */
    public void setQuery(java.lang.String query)
    {
        this._query = query;
    } //-- void setQuery(java.lang.String) 

    /**
     * Sets the value of field 'select'.
     * 
     * @param select the value of field 'select'.
     */
    public void setSelect(java.lang.String select)
    {
        this._select = select;
    } //-- void setSelect(java.lang.String) 

    /**
     * Sets the value of field 'update'.
     * 
     * @param update the value of field 'update'.
     */
    public void setUpdate(java.lang.String update)
    {
        this._update = update;
    } //-- void setUpdate(java.lang.String) 

    /**
     * Method unmarshalWebQPages
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalWebQPages(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.WebQPages) Unmarshaller.unmarshal(de.tif.qes.adf.castor.WebQPages.class, reader);
    } //-- java.lang.Object unmarshalWebQPages(java.io.Reader) 

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
