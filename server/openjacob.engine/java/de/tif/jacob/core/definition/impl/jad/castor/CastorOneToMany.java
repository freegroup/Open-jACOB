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
 * Class CastorOneToMany.
 * 
 * @version $Revision$ $Date$
 */
public class CastorOneToMany implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fromAlias
     */
    private java.lang.String _fromAlias;

    /**
     * Field _toAlias
     */
    private java.lang.String _toAlias;

    /**
     * Field _toKey
     */
    private java.lang.String _toKey;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorOneToMany() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'fromAlias'.
     * 
     * @return the value of field 'fromAlias'.
     */
    public java.lang.String getFromAlias()
    {
        return this._fromAlias;
    } //-- java.lang.String getFromAlias() 

    /**
     * Returns the value of field 'toAlias'.
     * 
     * @return the value of field 'toAlias'.
     */
    public java.lang.String getToAlias()
    {
        return this._toAlias;
    } //-- java.lang.String getToAlias() 

    /**
     * Returns the value of field 'toKey'.
     * 
     * @return the value of field 'toKey'.
     */
    public java.lang.String getToKey()
    {
        return this._toKey;
    } //-- java.lang.String getToKey() 

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
     * Sets the value of field 'fromAlias'.
     * 
     * @param fromAlias the value of field 'fromAlias'.
     */
    public void setFromAlias(java.lang.String fromAlias)
    {
        this._fromAlias = fromAlias;
    } //-- void setFromAlias(java.lang.String) 

    /**
     * Sets the value of field 'toAlias'.
     * 
     * @param toAlias the value of field 'toAlias'.
     */
    public void setToAlias(java.lang.String toAlias)
    {
        this._toAlias = toAlias;
    } //-- void setToAlias(java.lang.String) 

    /**
     * Sets the value of field 'toKey'.
     * 
     * @param toKey the value of field 'toKey'.
     */
    public void setToKey(java.lang.String toKey)
    {
        this._toKey = toKey;
    } //-- void setToKey(java.lang.String) 

    /**
     * Method unmarshalCastorOneToMany
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorOneToMany(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany.class, reader);
    } //-- java.lang.Object unmarshalCastorOneToMany(java.io.Reader) 

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
