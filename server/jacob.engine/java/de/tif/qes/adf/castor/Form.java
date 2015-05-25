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
 * Class Form.
 * 
 * @version $Revision$ $Date$
 */
public class Form implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _tableset
     */
    private java.lang.String _tableset;

    /**
     * Field _width
     */
    private long _width;

    /**
     * keeps track of state for field: _width
     */
    private boolean _has_width;

    /**
     * Field _height
     */
    private long _height;

    /**
     * keeps track of state for field: _height
     */
    private boolean _has_height;

    /**
     * Field _caption
     */
    private de.tif.qes.adf.castor.Caption _caption;

    /**
     * Field _webQPages
     */
    private de.tif.qes.adf.castor.WebQPages _webQPages;

    /**
     * Field _scripts
     */
    private de.tif.qes.adf.castor.Scripts _scripts;

    /**
     * Field _groups
     */
    private de.tif.qes.adf.castor.Groups _groups;


      //----------------/
     //- Constructors -/
    //----------------/

    public Form() {
        super();
    } //-- de.tif.qes.adf.castor.Form()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteHeight
     */
    public void deleteHeight()
    {
        this._has_height= false;
    } //-- void deleteHeight() 

    /**
     * Method deleteWidth
     */
    public void deleteWidth()
    {
        this._has_width= false;
    } //-- void deleteWidth() 

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
     * Returns the value of field 'groups'.
     * 
     * @return the value of field 'groups'.
     */
    public de.tif.qes.adf.castor.Groups getGroups()
    {
        return this._groups;
    } //-- de.tif.qes.adf.castor.Groups getGroups() 

    /**
     * Returns the value of field 'height'.
     * 
     * @return the value of field 'height'.
     */
    public long getHeight()
    {
        return this._height;
    } //-- long getHeight() 

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
     * Returns the value of field 'scripts'.
     * 
     * @return the value of field 'scripts'.
     */
    public de.tif.qes.adf.castor.Scripts getScripts()
    {
        return this._scripts;
    } //-- de.tif.qes.adf.castor.Scripts getScripts() 

    /**
     * Returns the value of field 'tableset'.
     * 
     * @return the value of field 'tableset'.
     */
    public java.lang.String getTableset()
    {
        return this._tableset;
    } //-- java.lang.String getTableset() 

    /**
     * Returns the value of field 'webQPages'.
     * 
     * @return the value of field 'webQPages'.
     */
    public de.tif.qes.adf.castor.WebQPages getWebQPages()
    {
        return this._webQPages;
    } //-- de.tif.qes.adf.castor.WebQPages getWebQPages() 

    /**
     * Returns the value of field 'width'.
     * 
     * @return the value of field 'width'.
     */
    public long getWidth()
    {
        return this._width;
    } //-- long getWidth() 

    /**
     * Method hasHeight
     */
    public boolean hasHeight()
    {
        return this._has_height;
    } //-- boolean hasHeight() 

    /**
     * Method hasWidth
     */
    public boolean hasWidth()
    {
        return this._has_width;
    } //-- boolean hasWidth() 

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
    public void setCaption(de.tif.qes.adf.castor.Caption caption)
    {
        this._caption = caption;
    } //-- void setCaption(de.tif.qes.adf.castor.Caption) 

    /**
     * Sets the value of field 'groups'.
     * 
     * @param groups the value of field 'groups'.
     */
    public void setGroups(de.tif.qes.adf.castor.Groups groups)
    {
        this._groups = groups;
    } //-- void setGroups(de.tif.qes.adf.castor.Groups) 

    /**
     * Sets the value of field 'height'.
     * 
     * @param height the value of field 'height'.
     */
    public void setHeight(long height)
    {
        this._height = height;
        this._has_height = true;
    } //-- void setHeight(long) 

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
     * Sets the value of field 'scripts'.
     * 
     * @param scripts the value of field 'scripts'.
     */
    public void setScripts(de.tif.qes.adf.castor.Scripts scripts)
    {
        this._scripts = scripts;
    } //-- void setScripts(de.tif.qes.adf.castor.Scripts) 

    /**
     * Sets the value of field 'tableset'.
     * 
     * @param tableset the value of field 'tableset'.
     */
    public void setTableset(java.lang.String tableset)
    {
        this._tableset = tableset;
    } //-- void setTableset(java.lang.String) 

    /**
     * Sets the value of field 'webQPages'.
     * 
     * @param webQPages the value of field 'webQPages'.
     */
    public void setWebQPages(de.tif.qes.adf.castor.WebQPages webQPages)
    {
        this._webQPages = webQPages;
    } //-- void setWebQPages(de.tif.qes.adf.castor.WebQPages) 

    /**
     * Sets the value of field 'width'.
     * 
     * @param width the value of field 'width'.
     */
    public void setWidth(long width)
    {
        this._width = width;
        this._has_width = true;
    } //-- void setWidth(long) 

    /**
     * Method unmarshalForm
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalForm(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Form) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Form.class, reader);
    } //-- java.lang.Object unmarshalForm(java.io.Reader) 

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
