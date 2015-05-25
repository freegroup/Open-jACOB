/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.screen.impl.theme.castor;

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
 * Class CastorTheme.
 * 
 * @version $Revision$ $Date$
 */
public class CastorTheme implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _displayName
     */
    private java.lang.String _displayName;

    /**
     * Field _engineVersion
     */
    private java.lang.String _engineVersion;

    /**
     * Field _castorNavigation
     */
    private CastorNavigation _castorNavigation;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorTheme() {
        super();
    } //-- de.tif.jacob.screen.impl.theme.castor.CastorTheme()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'castorNavigation'.
     * 
     * @return the value of field 'castorNavigation'.
     */
    public CastorNavigation getCastorNavigation()
    {
        return this._castorNavigation;
    } //-- CastorNavigation getCastorNavigation() 

    /**
     * Returns the value of field 'displayName'.
     * 
     * @return the value of field 'displayName'.
     */
    public java.lang.String getDisplayName()
    {
        return this._displayName;
    } //-- java.lang.String getDisplayName() 

    /**
     * Returns the value of field 'engineVersion'.
     * 
     * @return the value of field 'engineVersion'.
     */
    public java.lang.String getEngineVersion()
    {
        return this._engineVersion;
    } //-- java.lang.String getEngineVersion() 

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
     * Sets the value of field 'castorNavigation'.
     * 
     * @param castorNavigation the value of field 'castorNavigation'
     */
    public void setCastorNavigation(CastorNavigation castorNavigation)
    {
        this._castorNavigation = castorNavigation;
    } //-- void setCastorNavigation(CastorNavigation) 

    /**
     * Sets the value of field 'displayName'.
     * 
     * @param displayName the value of field 'displayName'.
     */
    public void setDisplayName(java.lang.String displayName)
    {
        this._displayName = displayName;
    } //-- void setDisplayName(java.lang.String) 

    /**
     * Sets the value of field 'engineVersion'.
     * 
     * @param engineVersion the value of field 'engineVersion'.
     */
    public void setEngineVersion(java.lang.String engineVersion)
    {
        this._engineVersion = engineVersion;
    } //-- void setEngineVersion(java.lang.String) 

    /**
     * Method unmarshalCastorTheme
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorTheme(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.screen.impl.theme.castor.CastorTheme) Unmarshaller.unmarshal(de.tif.jacob.screen.impl.theme.castor.CastorTheme.class, reader);
    } //-- java.lang.Object unmarshalCastorTheme(java.io.Reader) 

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
