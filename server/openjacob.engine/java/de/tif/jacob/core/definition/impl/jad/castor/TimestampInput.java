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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
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
 * Class TimestampInput.
 * 
 * @version $Revision$ $Date$
 */
public class TimestampInput implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _halign
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment _halign;

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

    /**
     * Field _caption
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorCaption _caption;

    /**
     * Field _font
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorFont _font;


      //----------------/
     //- Constructors -/
    //----------------/

    public TimestampInput() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TimestampInput()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'caption'.
     * 
     * @return the value of field 'caption'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorCaption getCaption()
    {
        return this._caption;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorCaption getCaption() 

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
     * Returns the value of field 'font'.
     * 
     * @return the value of field 'font'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorFont getFont()
    {
        return this._font;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorFont getFont() 

    /**
     * Returns the value of field 'halign'.
     * 
     * @return the value of field 'halign'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment getHalign()
    {
        return this._halign;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment getHalign() 

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
     * Sets the value of field 'caption'.
     * 
     * @param caption the value of field 'caption'.
     */
    public void setCaption(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption caption)
    {
        this._caption = caption;
    } //-- void setCaption(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption) 

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
     * Sets the value of field 'font'.
     * 
     * @param font the value of field 'font'.
     */
    public void setFont(de.tif.jacob.core.definition.impl.jad.castor.CastorFont font)
    {
        this._font = font;
    } //-- void setFont(de.tif.jacob.core.definition.impl.jad.castor.CastorFont) 

    /**
     * Sets the value of field 'halign'.
     * 
     * @param halign the value of field 'halign'.
     */
    public void setHalign(de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment halign)
    {
        this._halign = halign;
    } //-- void setHalign(de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment) 

    /**
     * Method unmarshalTimestampInput
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTimestampInput(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.TimestampInput) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.TimestampInput.class, reader);
    } //-- java.lang.Object unmarshalTimestampInput(java.io.Reader) 

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
