/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.rss;

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
 * Class Category.
 * 
 * @version $Revision$ $Date$
 */
public class Category implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The value of the element is a forward-slash-separated string
     * that identifies a hierarchic location in the indicated
     * taxonomy. Processors may establish conventions for the
     * interpretation of categories. 
     */
    private java.lang.String _domain;


      //----------------/
     //- Constructors -/
    //----------------/

    public Category() {
        super();
    } //-- jacob.rss.Category()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'domain'. The field 'domain' has
     * the following description: The value of the element is a
     * forward-slash-separated string that identifies a hierarchic
     * location in the indicated taxonomy. Processors may establish
     * conventions for the interpretation of categories. 
     * 
     * @return the value of field 'domain'.
     */
    public java.lang.String getDomain()
    {
        return this._domain;
    } //-- java.lang.String getDomain() 

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
     * Sets the value of field 'domain'. The field 'domain' has the
     * following description: The value of the element is a
     * forward-slash-separated string that identifies a hierarchic
     * location in the indicated taxonomy. Processors may establish
     * conventions for the interpretation of categories. 
     * 
     * @param domain the value of field 'domain'.
     */
    public void setDomain(java.lang.String domain)
    {
        this._domain = domain;
    } //-- void setDomain(java.lang.String) 

    /**
     * Method unmarshalCategory
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCategory(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.rss.Category) Unmarshaller.unmarshal(jacob.rss.Category.class, reader);
    } //-- java.lang.Object unmarshalCategory(java.io.Reader) 

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
