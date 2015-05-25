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
import java.math.BigDecimal;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Specifies a GIF, JPEG or PNG image that can be displayed with
 * the channel. More info
 * http://blogs.law.harvard.edu/tech/rss#ltimagegtSubelementOfLtchannelgt
 * here. 
 * 
 * @version $Revision$ $Date$
 */
public class Image implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The URL of a GIF, JPEG or PNG image that represents the
     * channel. 
     */
    private java.lang.String _url;

    /**
     * Describes the image, it's used in the ALT attribute of the
     * HTML img tag when the channel is rendered in HTML. 
     */
    private java.lang.String _title;

    /**
     * The URL of the site, when the channel is rendered, the image
     * is a link to the site. (Note, in practice the image title
     * and link should have the same value as the channel's title
     * and link.) 
     */
    private java.lang.String _link;

    /**
     * Field _width
     */
    private java.math.BigDecimal _width = new java.math.BigDecimal("88");

    /**
     * Field _height
     */
    private java.math.BigDecimal _height = new java.math.BigDecimal("31");

    /**
     * Contains text that is included in the TITLE attribute of the
     * link formed around the image in the HTML rendering.
     */
    private java.lang.String _description;


      //----------------/
     //- Constructors -/
    //----------------/

    public Image() {
        super();
        setWidth(new java.math.BigDecimal("88"));
        setHeight(new java.math.BigDecimal("31"));
    } //-- jacob.rss.Image()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: Contains text
     * that is included in the TITLE attribute of the link formed
     * around the image in the HTML rendering.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'height'.
     * 
     * @return the value of field 'height'.
     */
    public java.math.BigDecimal getHeight()
    {
        return this._height;
    } //-- java.math.BigDecimal getHeight() 

    /**
     * Returns the value of field 'link'. The field 'link' has the
     * following description: The URL of the site, when the channel
     * is rendered, the image is a link to the site. (Note, in
     * practice the image title and link should have the same value
     * as the channel's title and link.) 
     * 
     * @return the value of field 'link'.
     */
    public java.lang.String getLink()
    {
        return this._link;
    } //-- java.lang.String getLink() 

    /**
     * Returns the value of field 'title'. The field 'title' has
     * the following description: Describes the image, it's used in
     * the ALT attribute of the HTML img tag when the channel is
     * rendered in HTML. 
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
     * Returns the value of field 'url'. The field 'url' has the
     * following description: The URL of a GIF, JPEG or PNG image
     * that represents the channel. 
     * 
     * @return the value of field 'url'.
     */
    public java.lang.String getUrl()
    {
        return this._url;
    } //-- java.lang.String getUrl() 

    /**
     * Returns the value of field 'width'.
     * 
     * @return the value of field 'width'.
     */
    public java.math.BigDecimal getWidth()
    {
        return this._width;
    } //-- java.math.BigDecimal getWidth() 

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
     * Sets the value of field 'description'. The field
     * 'description' has the following description: Contains text
     * that is included in the TITLE attribute of the link formed
     * around the image in the HTML rendering.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'height'.
     * 
     * @param height the value of field 'height'.
     */
    public void setHeight(java.math.BigDecimal height)
    {
        this._height = height;
    } //-- void setHeight(java.math.BigDecimal) 

    /**
     * Sets the value of field 'link'. The field 'link' has the
     * following description: The URL of the site, when the channel
     * is rendered, the image is a link to the site. (Note, in
     * practice the image title and link should have the same value
     * as the channel's title and link.) 
     * 
     * @param link the value of field 'link'.
     */
    public void setLink(java.lang.String link)
    {
        this._link = link;
    } //-- void setLink(java.lang.String) 

    /**
     * Sets the value of field 'title'. The field 'title' has the
     * following description: Describes the image, it's used in the
     * ALT attribute of the HTML img tag when the channel is
     * rendered in HTML. 
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Sets the value of field 'url'. The field 'url' has the
     * following description: The URL of a GIF, JPEG or PNG image
     * that represents the channel. 
     * 
     * @param url the value of field 'url'.
     */
    public void setUrl(java.lang.String url)
    {
        this._url = url;
    } //-- void setUrl(java.lang.String) 

    /**
     * Sets the value of field 'width'.
     * 
     * @param width the value of field 'width'.
     */
    public void setWidth(java.math.BigDecimal width)
    {
        this._width = width;
    } //-- void setWidth(java.math.BigDecimal) 

    /**
     * Method unmarshalImage
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalImage(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.rss.Image) Unmarshaller.unmarshal(jacob.rss.Image.class, reader);
    } //-- java.lang.Object unmarshalImage(java.io.Reader) 

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
