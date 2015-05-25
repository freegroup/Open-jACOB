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

import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
import de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType;
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
 * Class Container.
 * 
 * @version $Revision$ $Date$
 */
public class Container implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resizeMode
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode _resizeMode;

    /**
     * Field _layout
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType _layout = de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType.valueOf("tab_strip");

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;

    /**
     * Field _paneList
     */
    private java.util.Vector _paneList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Container() {
        super();
        setLayout(de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType.valueOf("tab_strip"));
        _paneList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Container()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPane
     * 
     * @param vPane
     */
    public void addPane(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup vPane)
        throws java.lang.IndexOutOfBoundsException
    {
        _paneList.addElement(vPane);
    } //-- void addPane(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) 

    /**
     * Method addPane
     * 
     * @param index
     * @param vPane
     */
    public void addPane(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGroup vPane)
        throws java.lang.IndexOutOfBoundsException
    {
        _paneList.insertElementAt(vPane, index);
    } //-- void addPane(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) 

    /**
     * Method enumeratePane
     */
    public java.util.Enumeration enumeratePane()
    {
        return _paneList.elements();
    } //-- java.util.Enumeration enumeratePane() 

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
     * Returns the value of field 'layout'.
     * 
     * @return the value of field 'layout'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType getLayout()
    {
        return this._layout;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType getLayout() 

    /**
     * Method getPane
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroup getPane(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paneList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) _paneList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroup getPane(int) 

    /**
     * Method getPane
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[] getPane()
    {
        int size = _paneList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) _paneList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[] getPane() 

    /**
     * Method getPaneCount
     */
    public int getPaneCount()
    {
        return _paneList.size();
    } //-- int getPaneCount() 

    /**
     * Returns the value of field 'resizeMode'.
     * 
     * @return the value of field 'resizeMode'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode getResizeMode()
    {
        return this._resizeMode;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode getResizeMode() 

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
     * Method removeAllPane
     */
    public void removeAllPane()
    {
        _paneList.removeAllElements();
    } //-- void removeAllPane() 

    /**
     * Method removePane
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorGroup removePane(int index)
    {
        java.lang.Object obj = _paneList.elementAt(index);
        _paneList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGroup removePane(int) 

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
     * Sets the value of field 'layout'.
     * 
     * @param layout the value of field 'layout'.
     */
    public void setLayout(de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType layout)
    {
        this._layout = layout;
    } //-- void setLayout(de.tif.jacob.core.definition.impl.jad.castor.types.ContainerLayoutType) 

    /**
     * Method setPane
     * 
     * @param index
     * @param vPane
     */
    public void setPane(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorGroup vPane)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paneList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _paneList.setElementAt(vPane, index);
    } //-- void setPane(int, de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) 

    /**
     * Method setPane
     * 
     * @param paneArray
     */
    public void setPane(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup[] paneArray)
    {
        //-- copy array
        _paneList.removeAllElements();
        for (int i = 0; i < paneArray.length; i++) {
            _paneList.addElement(paneArray[i]);
        }
    } //-- void setPane(de.tif.jacob.core.definition.impl.jad.castor.CastorGroup) 

    /**
     * Sets the value of field 'resizeMode'.
     * 
     * @param resizeMode the value of field 'resizeMode'.
     */
    public void setResizeMode(de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode resizeMode)
    {
        this._resizeMode = resizeMode;
    } //-- void setResizeMode(de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode) 

    /**
     * Method unmarshalContainer
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalContainer(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Container) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Container.class, reader);
    } //-- java.lang.Object unmarshalContainer(java.io.Reader) 

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
