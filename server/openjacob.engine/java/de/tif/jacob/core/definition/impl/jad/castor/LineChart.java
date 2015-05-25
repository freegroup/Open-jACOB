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
 * Class LineChart.
 * 
 * @version $Revision$ $Date$
 */
public class LineChart implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _title
     */
    private boolean _title = true;

    /**
     * keeps track of state for field: _title
     */
    private boolean _has_title;

    /**
     * Field _legendX
     */
    private boolean _legendX = true;

    /**
     * keeps track of state for field: _legendX
     */
    private boolean _has_legendX;

    /**
     * Field _legendY
     */
    private boolean _legendY = true;

    /**
     * keeps track of state for field: _legendY
     */
    private boolean _has_legendY;

    /**
     * Field _background
     */
    private boolean _background = true;

    /**
     * keeps track of state for field: _background
     */
    private boolean _has_background;

    /**
     * Field _grid
     */
    private boolean _grid = true;

    /**
     * keeps track of state for field: _grid
     */
    private boolean _has_grid;

    /**
     * Field _dimension
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorDimension _dimension;


      //----------------/
     //- Constructors -/
    //----------------/

    public LineChart() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LineChart()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteBackground
     */
    public void deleteBackground()
    {
        this._has_background= false;
    } //-- void deleteBackground() 

    /**
     * Method deleteGrid
     */
    public void deleteGrid()
    {
        this._has_grid= false;
    } //-- void deleteGrid() 

    /**
     * Method deleteLegendX
     */
    public void deleteLegendX()
    {
        this._has_legendX= false;
    } //-- void deleteLegendX() 

    /**
     * Method deleteLegendY
     */
    public void deleteLegendY()
    {
        this._has_legendY= false;
    } //-- void deleteLegendY() 

    /**
     * Method deleteTitle
     */
    public void deleteTitle()
    {
        this._has_title= false;
    } //-- void deleteTitle() 

    /**
     * Returns the value of field 'background'.
     * 
     * @return the value of field 'background'.
     */
    public boolean getBackground()
    {
        return this._background;
    } //-- boolean getBackground() 

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
     * Returns the value of field 'grid'.
     * 
     * @return the value of field 'grid'.
     */
    public boolean getGrid()
    {
        return this._grid;
    } //-- boolean getGrid() 

    /**
     * Returns the value of field 'legendX'.
     * 
     * @return the value of field 'legendX'.
     */
    public boolean getLegendX()
    {
        return this._legendX;
    } //-- boolean getLegendX() 

    /**
     * Returns the value of field 'legendY'.
     * 
     * @return the value of field 'legendY'.
     */
    public boolean getLegendY()
    {
        return this._legendY;
    } //-- boolean getLegendY() 

    /**
     * Returns the value of field 'title'.
     * 
     * @return the value of field 'title'.
     */
    public boolean getTitle()
    {
        return this._title;
    } //-- boolean getTitle() 

    /**
     * Method hasBackground
     */
    public boolean hasBackground()
    {
        return this._has_background;
    } //-- boolean hasBackground() 

    /**
     * Method hasGrid
     */
    public boolean hasGrid()
    {
        return this._has_grid;
    } //-- boolean hasGrid() 

    /**
     * Method hasLegendX
     */
    public boolean hasLegendX()
    {
        return this._has_legendX;
    } //-- boolean hasLegendX() 

    /**
     * Method hasLegendY
     */
    public boolean hasLegendY()
    {
        return this._has_legendY;
    } //-- boolean hasLegendY() 

    /**
     * Method hasTitle
     */
    public boolean hasTitle()
    {
        return this._has_title;
    } //-- boolean hasTitle() 

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
     * Sets the value of field 'background'.
     * 
     * @param background the value of field 'background'.
     */
    public void setBackground(boolean background)
    {
        this._background = background;
        this._has_background = true;
    } //-- void setBackground(boolean) 

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
     * Sets the value of field 'grid'.
     * 
     * @param grid the value of field 'grid'.
     */
    public void setGrid(boolean grid)
    {
        this._grid = grid;
        this._has_grid = true;
    } //-- void setGrid(boolean) 

    /**
     * Sets the value of field 'legendX'.
     * 
     * @param legendX the value of field 'legendX'.
     */
    public void setLegendX(boolean legendX)
    {
        this._legendX = legendX;
        this._has_legendX = true;
    } //-- void setLegendX(boolean) 

    /**
     * Sets the value of field 'legendY'.
     * 
     * @param legendY the value of field 'legendY'.
     */
    public void setLegendY(boolean legendY)
    {
        this._legendY = legendY;
        this._has_legendY = true;
    } //-- void setLegendY(boolean) 

    /**
     * Sets the value of field 'title'.
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(boolean title)
    {
        this._title = title;
        this._has_title = true;
    } //-- void setTitle(boolean) 

    /**
     * Method unmarshalLineChart
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLineChart(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.LineChart) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.LineChart.class, reader);
    } //-- java.lang.Object unmarshalLineChart(java.io.Reader) 

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
