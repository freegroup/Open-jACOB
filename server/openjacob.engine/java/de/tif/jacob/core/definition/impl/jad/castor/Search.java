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
 * Class Search.
 * 
 * @version $Revision$ $Date$
 */
public class Search implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _relationset
     */
    private java.lang.String _relationset;

    /**
     * Field _filldirection
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection _filldirection;

    /**
     * Field _safeMode
     */
    private boolean _safeMode;

    /**
     * keeps track of state for field: _safeMode
     */
    private boolean _has_safeMode;


      //----------------/
     //- Constructors -/
    //----------------/

    public Search() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Search()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteSafeMode
     */
    public void deleteSafeMode()
    {
        this._has_safeMode= false;
    } //-- void deleteSafeMode() 

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
     * Returns the value of field 'relationset'.
     * 
     * @return the value of field 'relationset'.
     */
    public java.lang.String getRelationset()
    {
        return this._relationset;
    } //-- java.lang.String getRelationset() 

    /**
     * Returns the value of field 'safeMode'.
     * 
     * @return the value of field 'safeMode'.
     */
    public boolean getSafeMode()
    {
        return this._safeMode;
    } //-- boolean getSafeMode() 

    /**
     * Method hasSafeMode
     */
    public boolean hasSafeMode()
    {
        return this._has_safeMode;
    } //-- boolean hasSafeMode() 

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
     * Sets the value of field 'filldirection'.
     * 
     * @param filldirection the value of field 'filldirection'.
     */
    public void setFilldirection(de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection filldirection)
    {
        this._filldirection = filldirection;
    } //-- void setFilldirection(de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection) 

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
     * Sets the value of field 'safeMode'.
     * 
     * @param safeMode the value of field 'safeMode'.
     */
    public void setSafeMode(boolean safeMode)
    {
        this._safeMode = safeMode;
        this._has_safeMode = true;
    } //-- void setSafeMode(boolean) 

    /**
     * Method unmarshalSearch
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSearch(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Search) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Search.class, reader);
    } //-- java.lang.Object unmarshalSearch(java.io.Reader) 

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
