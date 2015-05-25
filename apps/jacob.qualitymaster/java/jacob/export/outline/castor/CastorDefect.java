/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: CastorDefect.java,v 1.2 2009/01/26 15:37:22 herz Exp $
 */

package jacob.export.outline.castor;

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
 * Class CastorDefect.
 * 
 * @version $Revision: 1.2 $ $Date: 2009/01/26 15:37:22 $
 */
public class CastorDefect implements java.io.Serializable {

 

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pkey
     */
    private java.lang.String _pkey;

    /**
     * Field _priority
     */
    private java.lang.String _priority;

    /**
     * Field _state
     */
    private java.lang.String _state;

    /**
     * Field _createDate
     */
    private java.lang.String _createDate;

    /**
     * Field _subject
     */
    private java.lang.String _subject;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorDefect() {
        super();
    } //-- jacob.export.outline.castor.CastorDefect()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'createDate'.
     * 
     * @return the value of field 'createDate'.
     */
    public java.lang.String getCreateDate()
    {
        return this._createDate;
    } //-- java.lang.String getCreateDate() 

    /**
     * Returns the value of field 'pkey'.
     * 
     * @return the value of field 'pkey'.
     */
    public java.lang.String getPkey()
    {
        return this._pkey;
    } //-- java.lang.String getPkey() 

    /**
     * Returns the value of field 'priority'.
     * 
     * @return the value of field 'priority'.
     */
    public java.lang.String getPriority()
    {
        return this._priority;
    } //-- java.lang.String getPriority() 

    /**
     * Returns the value of field 'state'.
     * 
     * @return the value of field 'state'.
     */
    public java.lang.String getState()
    {
        return this._state;
    } //-- java.lang.String getState() 

    /**
     * Returns the value of field 'subject'.
     * 
     * @return the value of field 'subject'.
     */
    public java.lang.String getSubject()
    {
        return this._subject;
    } //-- java.lang.String getSubject() 

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
     * Sets the value of field 'createDate'.
     * 
     * @param createDate the value of field 'createDate'.
     */
    public void setCreateDate(java.lang.String createDate)
    {
        this._createDate = createDate;
    } //-- void setCreateDate(java.lang.String) 

    /**
     * Sets the value of field 'pkey'.
     * 
     * @param pkey the value of field 'pkey'.
     */
    public void setPkey(java.lang.String pkey)
    {
        this._pkey = pkey;
    } //-- void setPkey(java.lang.String) 

    /**
     * Sets the value of field 'priority'.
     * 
     * @param priority the value of field 'priority'.
     */
    public void setPriority(java.lang.String priority)
    {
        this._priority = priority;
    } //-- void setPriority(java.lang.String) 

    /**
     * Sets the value of field 'state'.
     * 
     * @param state the value of field 'state'.
     */
    public void setState(java.lang.String state)
    {
        this._state = state;
    } //-- void setState(java.lang.String) 

    /**
     * Sets the value of field 'subject'.
     * 
     * @param subject the value of field 'subject'.
     */
    public void setSubject(java.lang.String subject)
    {
        this._subject = subject;
    } //-- void setSubject(java.lang.String) 

    /**
     * Method unmarshalCastorDefect
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorDefect(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.export.outline.castor.CastorDefect) Unmarshaller.unmarshal(jacob.export.outline.castor.CastorDefect.class, reader);
    } //-- java.lang.Object unmarshalCastorDefect(java.io.Reader) 

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
