/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.qes.adf.castor;

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
 * Class MenuItem.
 * 
 * @version $Revision$ $Date$
 */
public class MenuItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _clazz
     */
    private java.lang.String _clazz;

    /**
     * Field _scriptName
     */
    private java.lang.String _scriptName;

    /**
     * Field _file
     */
    private java.lang.String _file;

    /**
     * Field _caption
     */
    private de.tif.qes.adf.castor.Caption _caption;

    /**
     * Field _ADL
     */
    private de.tif.qes.adf.castor.ADL _ADL;


      //----------------/
     //- Constructors -/
    //----------------/

    public MenuItem() {
        super();
    } //-- de.tif.qes.adf.castor.MenuItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ADL'.
     * 
     * @return the value of field 'ADL'.
     */
    public de.tif.qes.adf.castor.ADL getADL()
    {
        return this._ADL;
    } //-- de.tif.qes.adf.castor.ADL getADL() 

    /**
     * Returns the value of field 'caption'.
     * 
     * @return the value of field 'caption'.
     */
    public de.tif.qes.adf.castor.Caption getCaption()
    {
        return this._caption;
    } //-- de.tif.qes.adf.castor.Caption getCaption() 

    /**
     * Returns the value of field 'clazz'.
     * 
     * @return the value of field 'clazz'.
     */
    public java.lang.String getClazz()
    {
        return this._clazz;
    } //-- java.lang.String getClazz() 

    /**
     * Returns the value of field 'file'.
     * 
     * @return the value of field 'file'.
     */
    public java.lang.String getFile()
    {
        return this._file;
    } //-- java.lang.String getFile() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'scriptName'.
     * 
     * @return the value of field 'scriptName'.
     */
    public java.lang.String getScriptName()
    {
        return this._scriptName;
    } //-- java.lang.String getScriptName() 

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
     * Sets the value of field 'ADL'.
     * 
     * @param ADL the value of field 'ADL'.
     */
    public void setADL(de.tif.qes.adf.castor.ADL ADL)
    {
        this._ADL = ADL;
    } //-- void setADL(de.tif.qes.adf.castor.ADL) 

    /**
     * Sets the value of field 'caption'.
     * 
     * @param caption the value of field 'caption'.
     */
    public void setCaption(de.tif.qes.adf.castor.Caption caption)
    {
        this._caption = caption;
    } //-- void setCaption(de.tif.qes.adf.castor.Caption) 

    /**
     * Sets the value of field 'clazz'.
     * 
     * @param clazz the value of field 'clazz'.
     */
    public void setClazz(java.lang.String clazz)
    {
        this._clazz = clazz;
    } //-- void setClazz(java.lang.String) 

    /**
     * Sets the value of field 'file'.
     * 
     * @param file the value of field 'file'.
     */
    public void setFile(java.lang.String file)
    {
        this._file = file;
    } //-- void setFile(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'scriptName'.
     * 
     * @param scriptName the value of field 'scriptName'.
     */
    public void setScriptName(java.lang.String scriptName)
    {
        this._scriptName = scriptName;
    } //-- void setScriptName(java.lang.String) 

    /**
     * Method unmarshalMenuItem
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalMenuItem(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.MenuItem) Unmarshaller.unmarshal(de.tif.qes.adf.castor.MenuItem.class, reader);
    } //-- java.lang.Object unmarshalMenuItem(java.io.Reader) 

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
