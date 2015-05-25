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
 * Class StaticImage.
 * 
 * @version $Revision$ $Date$
 */
public class StaticImage implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _src
     */
    private java.lang.String _src;

    /**
     * Field _tooltip
     */
    private java.lang.String _tooltip;

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;


      //----------------/
     //- Constructors -/
    //----------------/

    public StaticImage() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.StaticImage()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dimension'.
     * 
     * @return the value of field 'dimension'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension()
    {
        return this._dimension;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension() 

    /**
     * Returns the value of field 'src'.
     * 
     * @return the value of field 'src'.
     */
    public java.lang.String getSrc()
    {
        return this._src;
    } //-- java.lang.String getSrc() 

    /**
     * Returns the value of field 'tooltip'.
     * 
     * @return the value of field 'tooltip'.
     */
    public java.lang.String getTooltip()
    {
        return this._tooltip;
    } //-- java.lang.String getTooltip() 

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
     * Sets the value of field 'dimension'.
     * 
     * @param dimension the value of field 'dimension'.
     */
    public void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension dimension)
    {
        this._dimension = dimension;
    } //-- void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension) 

    /**
     * Sets the value of field 'src'.
     * 
     * @param src the value of field 'src'.
     */
    public void setSrc(java.lang.String src)
    {
        this._src = src;
    } //-- void setSrc(java.lang.String) 

    /**
     * Sets the value of field 'tooltip'.
     * 
     * @param tooltip the value of field 'tooltip'.
     */
    public void setTooltip(java.lang.String tooltip)
    {
        this._tooltip = tooltip;
    } //-- void setTooltip(java.lang.String) 

    /**
     * Method unmarshalStaticImage
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalStaticImage(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.StaticImage) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.StaticImage.class, reader);
    } //-- java.lang.Object unmarshalStaticImage(java.io.Reader) 

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
