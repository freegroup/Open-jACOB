/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.report.impl.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import de.tif.jacob.report.impl.castor.types.CastorFontStyleType;
import de.tif.jacob.report.impl.castor.types.CastorFontWeightType;
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
 * Class CastorFont.
 * 
 * @version $Revision$ $Date$
 */
public class CastorFont implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _family
     */
    private java.lang.String _family;

    /**
     * Field _style
     */
    private de.tif.jacob.report.impl.castor.types.CastorFontStyleType _style = de.tif.jacob.report.impl.castor.types.CastorFontStyleType.valueOf("normal");

    /**
     * Field _weight
     */
    private de.tif.jacob.report.impl.castor.types.CastorFontWeightType _weight = de.tif.jacob.report.impl.castor.types.CastorFontWeightType.valueOf("normal");

    /**
     * Field _size
     */
    private int _size;

    /**
     * keeps track of state for field: _size
     */
    private boolean _has_size;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorFont() {
        super();
        setStyle(de.tif.jacob.report.impl.castor.types.CastorFontStyleType.valueOf("normal"));
        setWeight(de.tif.jacob.report.impl.castor.types.CastorFontWeightType.valueOf("normal"));
    } //-- de.tif.jacob.report.impl.castor.CastorFont()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteSize
     */
    public void deleteSize()
    {
        this._has_size= false;
    } //-- void deleteSize() 

    /**
     * Returns the value of field 'family'.
     * 
     * @return the value of field 'family'.
     */
    public java.lang.String getFamily()
    {
        return this._family;
    } //-- java.lang.String getFamily() 

    /**
     * Returns the value of field 'size'.
     * 
     * @return the value of field 'size'.
     */
    public int getSize()
    {
        return this._size;
    } //-- int getSize() 

    /**
     * Returns the value of field 'style'.
     * 
     * @return the value of field 'style'.
     */
    public de.tif.jacob.report.impl.castor.types.CastorFontStyleType getStyle()
    {
        return this._style;
    } //-- de.tif.jacob.report.impl.castor.types.CastorFontStyleType getStyle() 

    /**
     * Returns the value of field 'weight'.
     * 
     * @return the value of field 'weight'.
     */
    public de.tif.jacob.report.impl.castor.types.CastorFontWeightType getWeight()
    {
        return this._weight;
    } //-- de.tif.jacob.report.impl.castor.types.CastorFontWeightType getWeight() 

    /**
     * Method hasSize
     */
    public boolean hasSize()
    {
        return this._has_size;
    } //-- boolean hasSize() 

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
     * Sets the value of field 'family'.
     * 
     * @param family the value of field 'family'.
     */
    public void setFamily(java.lang.String family)
    {
        this._family = family;
    } //-- void setFamily(java.lang.String) 

    /**
     * Sets the value of field 'size'.
     * 
     * @param size the value of field 'size'.
     */
    public void setSize(int size)
    {
        this._size = size;
        this._has_size = true;
    } //-- void setSize(int) 

    /**
     * Sets the value of field 'style'.
     * 
     * @param style the value of field 'style'.
     */
    public void setStyle(de.tif.jacob.report.impl.castor.types.CastorFontStyleType style)
    {
        this._style = style;
    } //-- void setStyle(de.tif.jacob.report.impl.castor.types.CastorFontStyleType) 

    /**
     * Sets the value of field 'weight'.
     * 
     * @param weight the value of field 'weight'.
     */
    public void setWeight(de.tif.jacob.report.impl.castor.types.CastorFontWeightType weight)
    {
        this._weight = weight;
    } //-- void setWeight(de.tif.jacob.report.impl.castor.types.CastorFontWeightType) 

    /**
     * Method unmarshalCastorFont
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorFont(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorFont) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorFont.class, reader);
    } //-- java.lang.Object unmarshalCastorFont(java.io.Reader) 

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
