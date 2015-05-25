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
 * Class Group.
 * 
 * @version $Revision$ $Date$
 */
public class Group implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _alias
     */
    private java.lang.String _alias;

    /**
     * Field _type
     */
    private java.lang.String _type;

    /**
     * Field _activeBrowser
     */
    private java.lang.String _activeBrowser;

    /**
     * Field _caption
     */
    private de.tif.qes.adf.castor.Caption _caption;

    /**
     * Field _position
     */
    private de.tif.qes.adf.castor.Position _position;

    /**
     * Field _browsers
     */
    private de.tif.qes.adf.castor.Browsers _browsers;

    /**
     * Field _scripts
     */
    private de.tif.qes.adf.castor.Scripts _scripts;

    /**
     * Field _menus
     */
    private de.tif.qes.adf.castor.Menus _menus;

    /**
     * Field _objects
     */
    private de.tif.qes.adf.castor.Objects _objects;


      //----------------/
     //- Constructors -/
    //----------------/

    public Group() {
        super();
    } //-- de.tif.qes.adf.castor.Group()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'activeBrowser'.
     * 
     * @return the value of field 'activeBrowser'.
     */
    public java.lang.String getActiveBrowser()
    {
        return this._activeBrowser;
    } //-- java.lang.String getActiveBrowser() 

    /**
     * Returns the value of field 'alias'.
     * 
     * @return the value of field 'alias'.
     */
    public java.lang.String getAlias()
    {
        return this._alias;
    } //-- java.lang.String getAlias() 

    /**
     * Returns the value of field 'browsers'.
     * 
     * @return the value of field 'browsers'.
     */
    public de.tif.qes.adf.castor.Browsers getBrowsers()
    {
        return this._browsers;
    } //-- de.tif.qes.adf.castor.Browsers getBrowsers() 

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
     * Returns the value of field 'menus'.
     * 
     * @return the value of field 'menus'.
     */
    public de.tif.qes.adf.castor.Menus getMenus()
    {
        return this._menus;
    } //-- de.tif.qes.adf.castor.Menus getMenus() 

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
     * Returns the value of field 'objects'.
     * 
     * @return the value of field 'objects'.
     */
    public de.tif.qes.adf.castor.Objects getObjects()
    {
        return this._objects;
    } //-- de.tif.qes.adf.castor.Objects getObjects() 

    /**
     * Returns the value of field 'position'.
     * 
     * @return the value of field 'position'.
     */
    public de.tif.qes.adf.castor.Position getPosition()
    {
        return this._position;
    } //-- de.tif.qes.adf.castor.Position getPosition() 

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
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

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
     * Sets the value of field 'activeBrowser'.
     * 
     * @param activeBrowser the value of field 'activeBrowser'.
     */
    public void setActiveBrowser(java.lang.String activeBrowser)
    {
        this._activeBrowser = activeBrowser;
    } //-- void setActiveBrowser(java.lang.String) 

    /**
     * Sets the value of field 'alias'.
     * 
     * @param alias the value of field 'alias'.
     */
    public void setAlias(java.lang.String alias)
    {
        this._alias = alias;
    } //-- void setAlias(java.lang.String) 

    /**
     * Sets the value of field 'browsers'.
     * 
     * @param browsers the value of field 'browsers'.
     */
    public void setBrowsers(de.tif.qes.adf.castor.Browsers browsers)
    {
        this._browsers = browsers;
    } //-- void setBrowsers(de.tif.qes.adf.castor.Browsers) 

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
     * Sets the value of field 'menus'.
     * 
     * @param menus the value of field 'menus'.
     */
    public void setMenus(de.tif.qes.adf.castor.Menus menus)
    {
        this._menus = menus;
    } //-- void setMenus(de.tif.qes.adf.castor.Menus) 

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
     * Sets the value of field 'objects'.
     * 
     * @param objects the value of field 'objects'.
     */
    public void setObjects(de.tif.qes.adf.castor.Objects objects)
    {
        this._objects = objects;
    } //-- void setObjects(de.tif.qes.adf.castor.Objects) 

    /**
     * Sets the value of field 'position'.
     * 
     * @param position the value of field 'position'.
     */
    public void setPosition(de.tif.qes.adf.castor.Position position)
    {
        this._position = position;
    } //-- void setPosition(de.tif.qes.adf.castor.Position) 

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
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * Method unmarshalGroup
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalGroup(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Group) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Group.class, reader);
    } //-- java.lang.Object unmarshalGroup(java.io.Reader) 

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
