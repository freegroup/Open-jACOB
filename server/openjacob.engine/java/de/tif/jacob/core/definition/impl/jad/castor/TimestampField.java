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

import de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType;
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
 * Class TimestampField.
 * 
 * @version $Revision$ $Date$
 */
public class TimestampField implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resolution
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType _resolution = de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType.valueOf("secbase");

    /**
     * Field _default
     */
    private java.lang.String _default;


      //----------------/
     //- Constructors -/
    //----------------/

    public TimestampField() {
        super();
        setResolution(de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType.valueOf("secbase"));
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TimestampField()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'resolution'.
     * 
     * @return the value of field 'resolution'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType getResolution()
    {
        return this._resolution;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType getResolution() 

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
     * Sets the value of field 'resolution'.
     * 
     * @param resolution the value of field 'resolution'.
     */
    public void setResolution(de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType resolution)
    {
        this._resolution = resolution;
    } //-- void setResolution(de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType) 

    /**
     * Method unmarshalTimestampField
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTimestampField(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.TimestampField) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.TimestampField.class, reader);
    } //-- java.lang.Object unmarshalTimestampField(java.io.Reader) 

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
