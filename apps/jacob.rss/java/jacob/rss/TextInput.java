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
 * Specifies a text input box that can be displayed with the
 * channel. More info
 * http://blogs.law.harvard.edu/tech/rss#lttextinputgtSubelementOfLtchannelgt
 * here.
 * The purpose of the textInput element is something of a mystery.
 * You can use it to specify a search engine box. Or to allow a
 * reader to provide feedback. Most aggregators ignore it.
 * 
 * @version $Revision$ $Date$
 */
public class TextInput implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The label of the Submit button in the text input area. 
     */
    private java.lang.Object _title;

    /**
     * Explains the text input area. 
     */
    private java.lang.Object _description;

    /**
     * The name of the text object in the text input area.
     */
    private java.lang.Object _name;

    /**
     * The URL of the CGI script that processes text input requests.
     */
    private java.lang.Object _link;


      //----------------/
     //- Constructors -/
    //----------------/

    public TextInput() {
        super();
    } //-- jacob.rss.TextInput()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: Explains the
     * text input area. 
     * 
     * @return the value of field 'description'.
     */
    public java.lang.Object getDescription()
    {
        return this._description;
    } //-- java.lang.Object getDescription() 

    /**
     * Returns the value of field 'link'. The field 'link' has the
     * following description: The URL of the CGI script that
     * processes text input requests.
     * 
     * @return the value of field 'link'.
     */
    public java.lang.Object getLink()
    {
        return this._link;
    } //-- java.lang.Object getLink() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: The name of the text object in the
     * text input area.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.Object getName()
    {
        return this._name;
    } //-- java.lang.Object getName() 

    /**
     * Returns the value of field 'title'. The field 'title' has
     * the following description: The label of the Submit button in
     * the text input area. 
     * 
     * @return the value of field 'title'.
     */
    public java.lang.Object getTitle()
    {
        return this._title;
    } //-- java.lang.Object getTitle() 

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
     * 'description' has the following description: Explains the
     * text input area. 
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.Object description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.Object) 

    /**
     * Sets the value of field 'link'. The field 'link' has the
     * following description: The URL of the CGI script that
     * processes text input requests.
     * 
     * @param link the value of field 'link'.
     */
    public void setLink(java.lang.Object link)
    {
        this._link = link;
    } //-- void setLink(java.lang.Object) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: The name of the text object in the
     * text input area.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.Object name)
    {
        this._name = name;
    } //-- void setName(java.lang.Object) 

    /**
     * Sets the value of field 'title'. The field 'title' has the
     * following description: The label of the Submit button in the
     * text input area. 
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.Object title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.Object) 

    /**
     * Method unmarshalTextInput
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTextInput(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.rss.TextInput) Unmarshaller.unmarshal(jacob.rss.TextInput.class, reader);
    } //-- java.lang.Object unmarshalTextInput(java.io.Reader) 

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
