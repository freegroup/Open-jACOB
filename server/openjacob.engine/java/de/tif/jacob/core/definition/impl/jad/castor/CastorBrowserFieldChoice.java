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
 * Class CastorBrowserFieldChoice.
 * 
 * @version $Revision$ $Date$
 */
public class CastorBrowserFieldChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableField
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType _tableField;

    /**
     * Field _runtime
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserRuntimeFieldType _runtime;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorBrowserFieldChoice() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserFieldChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'runtime'.
     * 
     * @return the value of field 'runtime'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserRuntimeFieldType getRuntime()
    {
        return this._runtime;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserRuntimeFieldType getRuntime() 

    /**
     * Returns the value of field 'tableField'.
     * 
     * @return the value of field 'tableField'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType getTableField()
    {
        return this._tableField;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType getTableField() 

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
     * Sets the value of field 'runtime'.
     * 
     * @param runtime the value of field 'runtime'.
     */
    public void setRuntime(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserRuntimeFieldType runtime)
    {
        this._runtime = runtime;
    } //-- void setRuntime(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserRuntimeFieldType) 

    /**
     * Sets the value of field 'tableField'.
     * 
     * @param tableField the value of field 'tableField'.
     */
    public void setTableField(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType tableField)
    {
        this._tableField = tableField;
    } //-- void setTableField(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType) 

    /**
     * Method unmarshalCastorBrowserFieldChoice
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorBrowserFieldChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserFieldChoice) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserFieldChoice.class, reader);
    } //-- java.lang.Object unmarshalCastorBrowserFieldChoice(java.io.Reader) 

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
