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

import de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class FlowLayoutContainer.
 * 
 * @version $Revision$ $Date$
 */
public class FlowLayoutContainer implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _orientation
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType _orientation = de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType.valueOf("horizontal");

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

    /**
     * Field _guiElementList
     */
    private java.util.Vector _guiElementList;


      //----------------/
     //- Constructors -/
    //----------------/

    public FlowLayoutContainer() {
        super();
        setOrientation(de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType.valueOf("horizontal"));
        _guiElementList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addGuiElement
     * 
     * @param vGuiElement
     */
    public void addGuiElement(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement vGuiElement)
        throws java.lang.IndexOutOfBoundsException
    {
        _guiElementList.addElement(vGuiElement);
    } //-- void addGuiElement(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) 

    /**
     * Method addGuiElement
     * 
     * @param index
     * @param vGuiElement
     */
    public void addGuiElement(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement vGuiElement)
        throws java.lang.IndexOutOfBoundsException
    {
        _guiElementList.insertElementAt(vGuiElement, index);
    } //-- void addGuiElement(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) 

    /**
     * Method enumerateGuiElement
     */
    public java.util.Enumeration enumerateGuiElement()
    {
        return _guiElementList.elements();
    } //-- java.util.Enumeration enumerateGuiElement() 

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
     * Method getGuiElement
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement getGuiElement(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _guiElementList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) _guiElementList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement getGuiElement(int) 

    /**
     * Method getGuiElement
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[] getGuiElement()
    {
        int size = _guiElementList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) _guiElementList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[] getGuiElement() 

    /**
     * Method getGuiElementCount
     */
    public int getGuiElementCount()
    {
        return _guiElementList.size();
    } //-- int getGuiElementCount() 

    /**
     * Returns the value of field 'orientation'.
     * 
     * @return the value of field 'orientation'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType getOrientation()
    {
        return this._orientation;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType getOrientation() 

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
     * Method removeAllGuiElement
     */
    public void removeAllGuiElement()
    {
        _guiElementList.removeAllElements();
    } //-- void removeAllGuiElement() 

    /**
     * Method removeGuiElement
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement removeGuiElement(int index)
    {
        java.lang.Object obj = _guiElementList.elementAt(index);
        _guiElementList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement removeGuiElement(int) 

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
     * Method setGuiElement
     * 
     * @param index
     * @param vGuiElement
     */
    public void setGuiElement(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement vGuiElement)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _guiElementList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _guiElementList.setElementAt(vGuiElement, index);
    } //-- void setGuiElement(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) 

    /**
     * Method setGuiElement
     * 
     * @param guiElementArray
     */
    public void setGuiElement(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement[] guiElementArray)
    {
        //-- copy array
        _guiElementList.removeAllElements();
        for (int i = 0; i < guiElementArray.length; i++) {
            _guiElementList.addElement(guiElementArray[i]);
        }
    } //-- void setGuiElement(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement) 

    /**
     * Sets the value of field 'orientation'.
     * 
     * @param orientation the value of field 'orientation'.
     */
    public void setOrientation(de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType orientation)
    {
        this._orientation = orientation;
    } //-- void setOrientation(de.tif.jacob.core.definition.impl.jad.castor.types.FlowLayoutContainerOrientationType) 

    /**
     * Method unmarshalFlowLayoutContainer
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalFlowLayoutContainer(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer.class, reader);
    } //-- java.lang.Object unmarshalFlowLayoutContainer(java.io.Reader) 

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
