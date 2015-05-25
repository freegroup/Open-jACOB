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
 * Class CastorManyToMany.
 * 
 * @version $Revision$ $Date$
 */
public class CastorManyToMany implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fromRelation
     */
    private java.lang.String _fromRelation;

    /**
     * Field _toRelation
     */
    private java.lang.String _toRelation;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorManyToMany() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'fromRelation'.
     * 
     * @return the value of field 'fromRelation'.
     */
    public java.lang.String getFromRelation()
    {
        return this._fromRelation;
    } //-- java.lang.String getFromRelation() 

    /**
     * Returns the value of field 'toRelation'.
     * 
     * @return the value of field 'toRelation'.
     */
    public java.lang.String getToRelation()
    {
        return this._toRelation;
    } //-- java.lang.String getToRelation() 

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
     * Sets the value of field 'fromRelation'.
     * 
     * @param fromRelation the value of field 'fromRelation'.
     */
    public void setFromRelation(java.lang.String fromRelation)
    {
        this._fromRelation = fromRelation;
    } //-- void setFromRelation(java.lang.String) 

    /**
     * Sets the value of field 'toRelation'.
     * 
     * @param toRelation the value of field 'toRelation'.
     */
    public void setToRelation(java.lang.String toRelation)
    {
        this._toRelation = toRelation;
    } //-- void setToRelation(java.lang.String) 

    /**
     * Method unmarshalCastorManyToMany
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorManyToMany(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany.class, reader);
    } //-- java.lang.Object unmarshalCastorManyToMany(java.io.Reader) 

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
