/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML
 * Schema.
 * $Id: Part.java,v 1.1 2007/02/02 22:26:47 freegroup Exp $
 */

package jacob.circuit.castor;

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
 * Class Part.
 * 
 * @version $Revision: 1.1 $ $Date: 2007/02/02 22:26:47 $
 */
public class Part implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private java.lang.String _id;

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


      //----------------/
     //- Constructors -/
    //----------------/

    public Part() {
        super();
    } //-- jacob.circuit.castor.Part()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteX
     * 
     */
    public void deleteX()
    {
        this._has_x= false;
    } //-- void deleteX() 

    /**
     * Method deleteY
     * 
     */
    public void deleteY()
    {
        this._has_y= false;
    } //-- void deleteY() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return String
     * @return the value of field 'id'.
     */
    public java.lang.String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return String
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Returns the value of field 'x'.
     * 
     * @return int
     * @return the value of field 'x'.
     */
    public int getX()
    {
        return this._x;
    } //-- int getX() 

    /**
     * Returns the value of field 'y'.
     * 
     * @return int
     * @return the value of field 'y'.
     */
    public int getY()
    {
        return this._y;
    } //-- int getY() 

    /**
     * Method hasX
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasX()
    {
        return this._has_x;
    } //-- boolean hasX() 

    /**
     * Method hasY
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasY()
    {
        return this._has_y;
    } //-- boolean hasY() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
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
     * 
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
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

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
     * Method unmarshalPart
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalPart(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.circuit.castor.Part) Unmarshaller.unmarshal(jacob.circuit.castor.Part.class, reader);
    } //-- java.lang.Object unmarshalPart(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
