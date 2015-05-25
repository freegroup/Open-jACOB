/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: Block.java,v 1.3 2011/07/01 21:23:55 freegroup Exp $
 */

package de.tif.jacob.util.flow.castor;

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
 * Class Block.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/07/01 21:23:55 $
 */
public class Block implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _implementation
     */
    private java.lang.String _implementation;

    /**
     * Field _type
     */
    private java.lang.String _type;

    /**
     * Field _x
     */
    private int _x;

    /**
     * keeps track of state for field: _x
     */
    private boolean _has_x;

    /**
     * Field _y
     */
    private int _y;

    /**
     * keeps track of state for field: _y
     */
    private boolean _has_y;

    /**
     * Field _property
     */
    private java.lang.String _property;

    /**
     * Field _static_param
     */
    private de.tif.jacob.util.flow.castor.Static_param _static_param;

    /**
     * Field _dynamic_param
     */
    private de.tif.jacob.util.flow.castor.Dynamic_param _dynamic_param;

    /**
     * Field _innerblocks
     */
    private de.tif.jacob.util.flow.castor.Innerblocks _innerblocks;

    /**
     * Field _block
     */
    private Block _block;


      //----------------/
     //- Constructors -/
    //----------------/

    public Block() {
        super();
    } //-- de.tif.jacob.util.flow.castor.Block()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteX
     */
    public void deleteX()
    {
        this._has_x= false;
    } //-- void deleteX() 

    /**
     * Method deleteY
     */
    public void deleteY()
    {
        this._has_y= false;
    } //-- void deleteY() 

    /**
     * Returns the value of field 'block'.
     * 
     * @return the value of field 'block'.
     */
    public de.tif.jacob.util.flow.castor.Block getBlock()
    {
        return this._block;
    } //-- de.tif.jacob.util.flow.castor.Block getBlock() 

    /**
     * Returns the value of field 'dynamic_param'.
     * 
     * @return the value of field 'dynamic_param'.
     */
    public de.tif.jacob.util.flow.castor.Dynamic_param getDynamic_param()
    {
        return this._dynamic_param;
    } //-- de.tif.jacob.util.flow.castor.Dynamic_param getDynamic_param() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
     */
    public java.lang.String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Returns the value of field 'implementation'.
     * 
     * @return the value of field 'implementation'.
     */
    public java.lang.String getImplementation()
    {
        return this._implementation;
    } //-- java.lang.String getImplementation() 

    /**
     * Returns the value of field 'innerblocks'.
     * 
     * @return the value of field 'innerblocks'.
     */
    public de.tif.jacob.util.flow.castor.Innerblocks getInnerblocks()
    {
        return this._innerblocks;
    } //-- de.tif.jacob.util.flow.castor.Innerblocks getInnerblocks() 

    /**
     * Returns the value of field 'property'.
     * 
     * @return the value of field 'property'.
     */
    public java.lang.String getProperty()
    {
        return this._property;
    } //-- java.lang.String getProperty() 

    /**
     * Returns the value of field 'static_param'.
     * 
     * @return the value of field 'static_param'.
     */
    public de.tif.jacob.util.flow.castor.Static_param getStatic_param()
    {
        return this._static_param;
    } //-- de.tif.jacob.util.flow.castor.Static_param getStatic_param() 

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
     * Returns the value of field 'x'.
     * 
     * @return the value of field 'x'.
     */
    public int getX()
    {
        return this._x;
    } //-- int getX() 

    /**
     * Returns the value of field 'y'.
     * 
     * @return the value of field 'y'.
     */
    public int getY()
    {
        return this._y;
    } //-- int getY() 

    /**
     * Method hasX
     */
    public boolean hasX()
    {
        return this._has_x;
    } //-- boolean hasX() 

    /**
     * Method hasY
     */
    public boolean hasY()
    {
        return this._has_y;
    } //-- boolean hasY() 

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
     * Sets the value of field 'block'.
     * 
     * @param block the value of field 'block'.
     */
    public void setBlock(de.tif.jacob.util.flow.castor.Block block)
    {
        this._block = block;
    } //-- void setBlock(de.tif.jacob.util.flow.castor.Block) 

    /**
     * Sets the value of field 'dynamic_param'.
     * 
     * @param dynamic_param the value of field 'dynamic_param'.
     */
    public void setDynamic_param(de.tif.jacob.util.flow.castor.Dynamic_param dynamic_param)
    {
        this._dynamic_param = dynamic_param;
    } //-- void setDynamic_param(de.tif.jacob.util.flow.castor.Dynamic_param) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id)
    {
        this._id = id;
    } //-- void setId(java.lang.String) 

    /**
     * Sets the value of field 'implementation'.
     * 
     * @param implementation the value of field 'implementation'.
     */
    public void setImplementation(java.lang.String implementation)
    {
        this._implementation = implementation;
    } //-- void setImplementation(java.lang.String) 

    /**
     * Sets the value of field 'innerblocks'.
     * 
     * @param innerblocks the value of field 'innerblocks'.
     */
    public void setInnerblocks(de.tif.jacob.util.flow.castor.Innerblocks innerblocks)
    {
        this._innerblocks = innerblocks;
    } //-- void setInnerblocks(de.tif.jacob.util.flow.castor.Innerblocks) 

    /**
     * Sets the value of field 'property'.
     * 
     * @param property the value of field 'property'.
     */
    public void setProperty(java.lang.String property)
    {
        this._property = property;
    } //-- void setProperty(java.lang.String) 

    /**
     * Sets the value of field 'static_param'.
     * 
     * @param static_param the value of field 'static_param'.
     */
    public void setStatic_param(de.tif.jacob.util.flow.castor.Static_param static_param)
    {
        this._static_param = static_param;
    } //-- void setStatic_param(de.tif.jacob.util.flow.castor.Static_param) 

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
     * Sets the value of field 'x'.
     * 
     * @param x the value of field 'x'.
     */
    public void setX(int x)
    {
        this._x = x;
        this._has_x = true;
    } //-- void setX(int) 

    /**
     * Sets the value of field 'y'.
     * 
     * @param y the value of field 'y'.
     */
    public void setY(int y)
    {
        this._y = y;
        this._has_y = true;
    } //-- void setY(int) 

    /**
     * Method unmarshalBlock
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalBlock(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.util.flow.castor.Block) Unmarshaller.unmarshal(de.tif.jacob.util.flow.castor.Block.class, reader);
    } //-- java.lang.Object unmarshalBlock(java.io.Reader) 

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
