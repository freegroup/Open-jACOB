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
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment;
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
 * Class CastorCaption.
 * 
 * @version $Revision$ $Date$
 */
public class CastorCaption implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _color
     */
    private java.lang.String _color;

    /**
     * Field _ellipsis
     */
    private boolean _ellipsis = false;

    /**
     * keeps track of state for field: _ellipsis
     */
    private boolean _has_ellipsis;

    /**
     * Field _label
     */
    private java.lang.String _label;

    /**
     * Field _halign
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment _halign = de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment.valueOf("right");

    /**
     * Field _valign
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment _valign = de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment.valueOf("top");

    /**
     * Field _eventHandler
     */
    private java.lang.String _eventHandler;

    /**
     * Some GUI Elements such like InFormBrowser do not need a
     * dimension for caption!
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

    /**
     * A caption might be implemented as a link to execute an action
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorAction _action;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;

    /**
     * Field _font
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorFont _font;

    /**
     * Field _workFlow
     */
    private java.lang.String _workFlow;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorCaption() {
        super();
        setHalign(de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment.valueOf("right"));
        setValign(de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment.valueOf("top"));
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorCaption()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addProperty
     * 
     * @param vProperty
     */
    public void addProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.addElement(vProperty);
    } //-- void addProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method addProperty
     * 
     * @param index
     * @param vProperty
     */
    public void addProperty(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.insertElementAt(vProperty, index);
    } //-- void addProperty(int, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method deleteEllipsis
     */
    public void deleteEllipsis()
    {
        this._has_ellipsis= false;
    } //-- void deleteEllipsis() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Returns the value of field 'action'. The field 'action' has
     * the following description: A caption might be implemented as
     * a link to execute an action
     * 
     * @return the value of field 'action'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorAction getAction()
    {
        return this._action;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorAction getAction() 

    /**
     * Returns the value of field 'color'.
     * 
     * @return the value of field 'color'.
     */
    public java.lang.String getColor()
    {
        return this._color;
    } //-- java.lang.String getColor() 

    /**
     * Returns the value of field 'dimension'. The field
     * 'dimension' has the following description: Some GUI Elements
     * such like InFormBrowser do not need a dimension for caption!
     * 
     * @return the value of field 'dimension'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension()
    {
        return this._dimension;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorDimension getDimension() 

    /**
     * Returns the value of field 'ellipsis'.
     * 
     * @return the value of field 'ellipsis'.
     */
    public boolean getEllipsis()
    {
        return this._ellipsis;
    } //-- boolean getEllipsis() 

    /**
     * Returns the value of field 'eventHandler'.
     * 
     * @return the value of field 'eventHandler'.
     */
    public java.lang.String getEventHandler()
    {
        return this._eventHandler;
    } //-- java.lang.String getEventHandler() 

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
     * Returns the value of field 'label'.
     * 
     * @return the value of field 'label'.
     */
    public java.lang.String getLabel()
    {
        return this._label;
    } //-- java.lang.String getLabel() 

    /**
     * Method getProperty
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty getProperty(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) _propertyList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty getProperty(int) 

    /**
     * Method getProperty
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] getProperty()
    {
        int size = _propertyList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) _propertyList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] getProperty() 

    /**
     * Method getPropertyCount
     */
    public int getPropertyCount()
    {
        return _propertyList.size();
    } //-- int getPropertyCount() 

    /**
     * Returns the value of field 'valign'.
     * 
     * @return the value of field 'valign'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment getValign()
    {
        return this._valign;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment getValign() 

    /**
     * Returns the value of field 'workFlow'.
     * 
     * @return the value of field 'workFlow'.
     */
    public java.lang.String getWorkFlow()
    {
        return this._workFlow;
    } //-- java.lang.String getWorkFlow() 

    /**
     * Method hasEllipsis
     */
    public boolean hasEllipsis()
    {
        return this._has_ellipsis;
    } //-- boolean hasEllipsis() 

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
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeProperty
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty removeProperty(int index)
    {
        java.lang.Object obj = _propertyList.elementAt(index);
        _propertyList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty removeProperty(int) 

    /**
     * Sets the value of field 'action'. The field 'action' has the
     * following description: A caption might be implemented as a
     * link to execute an action
     * 
     * @param action the value of field 'action'.
     */
    public void setAction(de.tif.jacob.core.definition.impl.jad.castor.CastorAction action)
    {
        this._action = action;
    } //-- void setAction(de.tif.jacob.core.definition.impl.jad.castor.CastorAction) 

    /**
     * Sets the value of field 'color'.
     * 
     * @param color the value of field 'color'.
     */
    public void setColor(java.lang.String color)
    {
        this._color = color;
    } //-- void setColor(java.lang.String) 

    /**
     * Sets the value of field 'dimension'. The field 'dimension'
     * has the following description: Some GUI Elements such like
     * InFormBrowser do not need a dimension for caption!
     * 
     * @param dimension the value of field 'dimension'.
     */
    public void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension dimension)
    {
        this._dimension = dimension;
    } //-- void setDimension(de.tif.jacob.core.definition.impl.jad.castor.CastorDimension) 

    /**
     * Sets the value of field 'ellipsis'.
     * 
     * @param ellipsis the value of field 'ellipsis'.
     */
    public void setEllipsis(boolean ellipsis)
    {
        this._ellipsis = ellipsis;
        this._has_ellipsis = true;
    } //-- void setEllipsis(boolean) 

    /**
     * Sets the value of field 'eventHandler'.
     * 
     * @param eventHandler the value of field 'eventHandler'.
     */
    public void setEventHandler(java.lang.String eventHandler)
    {
        this._eventHandler = eventHandler;
    } //-- void setEventHandler(java.lang.String) 

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
     * Sets the value of field 'label'.
     * 
     * @param label the value of field 'label'.
     */
    public void setLabel(java.lang.String label)
    {
        this._label = label;
    } //-- void setLabel(java.lang.String) 

    /**
     * Method setProperty
     * 
     * @param index
     * @param vProperty
     */
    public void setProperty(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propertyList.setElementAt(vProperty, index);
    } //-- void setProperty(int, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method setProperty
     * 
     * @param propertyArray
     */
    public void setProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] propertyArray)
    {
        //-- copy array
        _propertyList.removeAllElements();
        for (int i = 0; i < propertyArray.length; i++) {
            _propertyList.addElement(propertyArray[i]);
        }
    } //-- void setProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Sets the value of field 'valign'.
     * 
     * @param valign the value of field 'valign'.
     */
    public void setValign(de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment valign)
    {
        this._valign = valign;
    } //-- void setValign(de.tif.jacob.core.definition.impl.jad.castor.types.CastorVerticalAlignment) 

    /**
     * Sets the value of field 'workFlow'.
     * 
     * @param workFlow the value of field 'workFlow'.
     */
    public void setWorkFlow(java.lang.String workFlow)
    {
        this._workFlow = workFlow;
    } //-- void setWorkFlow(java.lang.String) 

    /**
     * Method unmarshalCastorCaption
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorCaption(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorCaption) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorCaption.class, reader);
    } //-- java.lang.Object unmarshalCastorCaption(java.io.Reader) 

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
