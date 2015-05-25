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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection;
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
 * Class CastorBrowserForeignType.
 * 
 * @version $Revision$ $Date$
 */
public class CastorBrowserForeignType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _foreignAlias
     */
    private java.lang.String _foreignAlias;

    /**
     * Field _relationToUse
     */
    private java.lang.String _relationToUse;

    /**
     * Field _browserToUse
     */
    private java.lang.String _browserToUse;

    /**
     * Field _relationset
     */
    private java.lang.String _relationset;

    /**
     * Field _filldirection
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection _filldirection;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorBrowserForeignType() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'browserToUse'.
     * 
     * @return the value of field 'browserToUse'.
     */
    public java.lang.String getBrowserToUse()
    {
        return this._browserToUse;
    } //-- java.lang.String getBrowserToUse() 

    /**
     * Returns the value of field 'filldirection'.
     * 
     * @return the value of field 'filldirection'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection getFilldirection()
    {
        return this._filldirection;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection getFilldirection() 

    /**
     * Returns the value of field 'foreignAlias'.
     * 
     * @return the value of field 'foreignAlias'.
     */
    public java.lang.String getForeignAlias()
    {
        return this._foreignAlias;
    } //-- java.lang.String getForeignAlias() 

    /**
     * Returns the value of field 'relationToUse'.
     * 
     * @return the value of field 'relationToUse'.
     */
    public java.lang.String getRelationToUse()
    {
        return this._relationToUse;
    } //-- java.lang.String getRelationToUse() 

    /**
     * Returns the value of field 'relationset'.
     * 
     * @return the value of field 'relationset'.
     */
    public java.lang.String getRelationset()
    {
        return this._relationset;
    } //-- java.lang.String getRelationset() 

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
     * Sets the value of field 'browserToUse'.
     * 
     * @param browserToUse the value of field 'browserToUse'.
     */
    public void setBrowserToUse(java.lang.String browserToUse)
    {
        this._browserToUse = browserToUse;
    } //-- void setBrowserToUse(java.lang.String) 

    /**
     * Sets the value of field 'filldirection'.
     * 
     * @param filldirection the value of field 'filldirection'.
     */
    public void setFilldirection(de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection filldirection)
    {
        this._filldirection = filldirection;
    } //-- void setFilldirection(de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection) 

    /**
     * Sets the value of field 'foreignAlias'.
     * 
     * @param foreignAlias the value of field 'foreignAlias'.
     */
    public void setForeignAlias(java.lang.String foreignAlias)
    {
        this._foreignAlias = foreignAlias;
    } //-- void setForeignAlias(java.lang.String) 

    /**
     * Sets the value of field 'relationToUse'.
     * 
     * @param relationToUse the value of field 'relationToUse'.
     */
    public void setRelationToUse(java.lang.String relationToUse)
    {
        this._relationToUse = relationToUse;
    } //-- void setRelationToUse(java.lang.String) 

    /**
     * Sets the value of field 'relationset'.
     * 
     * @param relationset the value of field 'relationset'.
     */
    public void setRelationset(java.lang.String relationset)
    {
        this._relationset = relationset;
    } //-- void setRelationset(java.lang.String) 

    /**
     * Method unmarshalCastorBrowserForeignType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorBrowserForeignType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType.class, reader);
    } //-- java.lang.Object unmarshalCastorBrowserForeignType(java.io.Reader) 

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
