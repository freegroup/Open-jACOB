/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.report.impl.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import de.tif.jacob.report.impl.castor.types.CastorLayoutColumnJustificationType;
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
 * Class CastorLayoutColumn.
 * 
 * @version $Revision$ $Date$
 */
public class CastorLayoutColumn implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _color
     */
    private java.lang.String _color;

    /**
     * Field _justification
     */
    private de.tif.jacob.report.impl.castor.types.CastorLayoutColumnJustificationType _justification;

    /**
     * Field _width
     */
    private int _width;

    /**
     * keeps track of state for field: _width
     */
    private boolean _has_width;

    /**
     * Field _ident
     */
    private int _ident;

    /**
     * keeps track of state for field: _ident
     */
    private boolean _has_ident;

    /**
     * Field _linebreak
     */
    private boolean _linebreak = false;

    /**
     * keeps track of state for field: _linebreak
     */
    private boolean _has_linebreak;

    /**
     * Field _truncationMark
     */
    private java.lang.String _truncationMark;

    /**
     * Field _tableAlias
     */
    private java.lang.String _tableAlias;

    /**
     * Field _field
     */
    private java.lang.String _field;

    /**
     * Field _label
     */
    private java.lang.String _label;

    /**
     * Currently just substring as function is provided
     */
    private de.tif.jacob.report.impl.castor.CastorFunction _function;

    /**
     * Field _font
     */
    private de.tif.jacob.report.impl.castor.CastorFont _font;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorLayoutColumn() {
        super();
    } //-- de.tif.jacob.report.impl.castor.CastorLayoutColumn()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIdent
     */
    public void deleteIdent()
    {
        this._has_ident= false;
    } //-- void deleteIdent() 

    /**
     * Method deleteLinebreak
     */
    public void deleteLinebreak()
    {
        this._has_linebreak= false;
    } //-- void deleteLinebreak() 

    /**
     * Method deleteWidth
     */
    public void deleteWidth()
    {
        this._has_width= false;
    } //-- void deleteWidth() 

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
     * Returns the value of field 'field'.
     * 
     * @return the value of field 'field'.
     */
    public java.lang.String getField()
    {
        return this._field;
    } //-- java.lang.String getField() 

    /**
     * Returns the value of field 'font'.
     * 
     * @return the value of field 'font'.
     */
    public de.tif.jacob.report.impl.castor.CastorFont getFont()
    {
        return this._font;
    } //-- de.tif.jacob.report.impl.castor.CastorFont getFont() 

    /**
     * Returns the value of field 'function'. The field 'function'
     * has the following description: Currently just substring as
     * function is provided
     * 
     * @return the value of field 'function'.
     */
    public de.tif.jacob.report.impl.castor.CastorFunction getFunction()
    {
        return this._function;
    } //-- de.tif.jacob.report.impl.castor.CastorFunction getFunction() 

    /**
     * Returns the value of field 'ident'.
     * 
     * @return the value of field 'ident'.
     */
    public int getIdent()
    {
        return this._ident;
    } //-- int getIdent() 

    /**
     * Returns the value of field 'justification'.
     * 
     * @return the value of field 'justification'.
     */
    public de.tif.jacob.report.impl.castor.types.CastorLayoutColumnJustificationType getJustification()
    {
        return this._justification;
    } //-- de.tif.jacob.report.impl.castor.types.CastorLayoutColumnJustificationType getJustification() 

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
     * Returns the value of field 'linebreak'.
     * 
     * @return the value of field 'linebreak'.
     */
    public boolean getLinebreak()
    {
        return this._linebreak;
    } //-- boolean getLinebreak() 

    /**
     * Returns the value of field 'tableAlias'.
     * 
     * @return the value of field 'tableAlias'.
     */
    public java.lang.String getTableAlias()
    {
        return this._tableAlias;
    } //-- java.lang.String getTableAlias() 

    /**
     * Returns the value of field 'truncationMark'.
     * 
     * @return the value of field 'truncationMark'.
     */
    public java.lang.String getTruncationMark()
    {
        return this._truncationMark;
    } //-- java.lang.String getTruncationMark() 

    /**
     * Returns the value of field 'width'.
     * 
     * @return the value of field 'width'.
     */
    public int getWidth()
    {
        return this._width;
    } //-- int getWidth() 

    /**
     * Method hasIdent
     */
    public boolean hasIdent()
    {
        return this._has_ident;
    } //-- boolean hasIdent() 

    /**
     * Method hasLinebreak
     */
    public boolean hasLinebreak()
    {
        return this._has_linebreak;
    } //-- boolean hasLinebreak() 

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
     * Sets the value of field 'color'.
     * 
     * @param color the value of field 'color'.
     */
    public void setColor(java.lang.String color)
    {
        this._color = color;
    } //-- void setColor(java.lang.String) 

    /**
     * Sets the value of field 'field'.
     * 
     * @param field the value of field 'field'.
     */
    public void setField(java.lang.String field)
    {
        this._field = field;
    } //-- void setField(java.lang.String) 

    /**
     * Sets the value of field 'font'.
     * 
     * @param font the value of field 'font'.
     */
    public void setFont(de.tif.jacob.report.impl.castor.CastorFont font)
    {
        this._font = font;
    } //-- void setFont(de.tif.jacob.report.impl.castor.CastorFont) 

    /**
     * Sets the value of field 'function'. The field 'function' has
     * the following description: Currently just substring as
     * function is provided
     * 
     * @param function the value of field 'function'.
     */
    public void setFunction(de.tif.jacob.report.impl.castor.CastorFunction function)
    {
        this._function = function;
    } //-- void setFunction(de.tif.jacob.report.impl.castor.CastorFunction) 

    /**
     * Sets the value of field 'ident'.
     * 
     * @param ident the value of field 'ident'.
     */
    public void setIdent(int ident)
    {
        this._ident = ident;
        this._has_ident = true;
    } //-- void setIdent(int) 

    /**
     * Sets the value of field 'justification'.
     * 
     * @param justification the value of field 'justification'.
     */
    public void setJustification(de.tif.jacob.report.impl.castor.types.CastorLayoutColumnJustificationType justification)
    {
        this._justification = justification;
    } //-- void setJustification(de.tif.jacob.report.impl.castor.types.CastorLayoutColumnJustificationType) 

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
     * Sets the value of field 'linebreak'.
     * 
     * @param linebreak the value of field 'linebreak'.
     */
    public void setLinebreak(boolean linebreak)
    {
        this._linebreak = linebreak;
        this._has_linebreak = true;
    } //-- void setLinebreak(boolean) 

    /**
     * Sets the value of field 'tableAlias'.
     * 
     * @param tableAlias the value of field 'tableAlias'.
     */
    public void setTableAlias(java.lang.String tableAlias)
    {
        this._tableAlias = tableAlias;
    } //-- void setTableAlias(java.lang.String) 

    /**
     * Sets the value of field 'truncationMark'.
     * 
     * @param truncationMark the value of field 'truncationMark'.
     */
    public void setTruncationMark(java.lang.String truncationMark)
    {
        this._truncationMark = truncationMark;
    } //-- void setTruncationMark(java.lang.String) 

    /**
     * Sets the value of field 'width'.
     * 
     * @param width the value of field 'width'.
     */
    public void setWidth(int width)
    {
        this._width = width;
        this._has_width = true;
    } //-- void setWidth(int) 

    /**
     * Method unmarshalCastorLayoutColumn
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorLayoutColumn(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CastorLayoutColumn) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CastorLayoutColumn.class, reader);
    } //-- java.lang.Object unmarshalCastorLayoutColumn(java.io.Reader) 

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
