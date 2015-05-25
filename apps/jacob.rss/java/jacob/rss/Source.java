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
 * The RSS channel that the item came from. The purpose of this
 * element is to propagate credit for links, to publicize the
 * sources of news items. It can be used in the Post command of an
 * aggregator. It should be generated automatically when forwarding
 * an item from an aggregator to a weblog authoring tool.
 * 
 * @version $Revision$ $Date$
 */
public class Source implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * links to the XMLization of the source
     */
    private java.lang.String _url;


      //----------------/
     //- Constructors -/
    //----------------/

    public Source() {
        super();
    } //-- jacob.rss.Source()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'url'. The field 'url' has the
     * following description: links to the XMLization of the source
     * 
     * @return the value of field 'url'.
     */
    public java.lang.String getUrl()
    {
        return this._url;
    } //-- java.lang.String getUrl() 

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
     * Sets the value of field 'url'. The field 'url' has the
     * following description: links to the XMLization of the source
     * 
     * @param url the value of field 'url'.
     */
    public void setUrl(java.lang.String url)
    {
        this._url = url;
    } //-- void setUrl(java.lang.String) 

    /**
     * Method unmarshalSource
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSource(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.rss.Source) Unmarshaller.unmarshal(jacob.rss.Source.class, reader);
    } //-- java.lang.Object unmarshalSource(java.io.Reader) 

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
