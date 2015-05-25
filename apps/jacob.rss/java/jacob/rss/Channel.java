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
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Channel.
 * 
 * @version $Revision$ $Date$
 */
public class Channel implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _version
     */
    private double _version = 2.0;

    /**
     * keeps track of state for field: _version
     */
    private boolean _has_version;

    /**
     * The name of the channel. It's how people refer to your
     * service. If you have an HTML website that contains the same
     * information as your RSS file, the title of your channel
     * should be the same as the title of your website. 
     */
    private java.lang.String _title;

    /**
     * The URL to the HTML website corresponding to the channel.
     */
    private java.lang.String _link;

    /**
     * Phrase or sentence describing the channel.
     */
    private java.lang.String _description;

    /**
     * The language the channel is written in. This allows
     * aggregators to group all Italian language sites, for
     * example, on a single page. A list of allowable values for
     * this element, as provided by Netscape, is
     * http://blogs.law.harvard.edu/tech/stories/storyReader$15
     * here. You may also use
     * http://www.w3.org/TR/REC-html40/struct/dirlang.html#langcodes
     * values defined by the W3C.
     */
    private java.lang.String _language = "en-us";

    /**
     * Copyright notice for content in the channel.
     */
    private java.lang.String _copyright;

    /**
     * Email address for person responsible for editorial content.
     */
    private java.lang.String _managingEditor;

    /**
     * Email address for person responsible for technical issues
     * relating to channel.
     */
    private java.lang.String _webMaster;

    /**
     * The publication date for the content in the channel. For
     * example, the New York Times publishes on a daily basis, the
     * publication date flips once every 24 hours. That's when the
     * pubDate of the channel changes. All date-times in RSS
     * conform to the Date and Time Specification of
     * http://asg.web.cmu.edu/rf/rfc822.html RFC 822, with the
     * exception that the year may be expressed with two characters
     * or four characters (four preferred).
     */
    private java.util.Date _pubDate;

    /**
     * The last time the content of the channel changed.
     */
    private java.util.Date _lastBuildDate;

    /**
     * Specify one or more categories that the channel belongs to.
     * Follows the same rules as the item-level
     * http://blogs.law.harvard.edu/tech/rss#ltcategorygtSubelementOfLtitemgt"
     * category element. More
     * http://blogs.law.harvard.edu/tech/rss#syndic8 info.
     */
    private java.lang.String _category;

    /**
     * A string indicating the program used to generate the channel.
     */
    private java.lang.String _generator = "jACOB ApplicationServer";

    /**
     * A URL that points to the documentation for the format used
     * in the RSS file. It's probably a pointer to this page. It's
     * for people who might stumble across an RSS file on a Web
     * server 25 years from now and wonder what it is.
     */
    private java.lang.String _docs = "http://blogs.law.harvard.edu/tech/rss";

    /**
     * Field _cloud
     */
    private Cloud _cloud;

    /**
     * ttl stands for time to live. It's a number of minutes that
     * indicates how long a channel can be cached before refreshing
     * from the source. 
     * More info
     * http://blogs.law.harvard.edu/tech/rss#ltttlgtSubelementOfLtchannelgt
     * here.
     */
    private java.lang.Object _ttl;

    /**
     * Specifies a GIF, JPEG or PNG image that can be displayed
     * with the channel. More info
     * http://blogs.law.harvard.edu/tech/rss#ltimagegtSubelementOfLtchannelgt
     * here. 
     */
    private Image _image;

    /**
     * The http://www.w3.org/PICS/ PICS rating for the channel.
     */
    private java.lang.Object _rating;

    /**
     * Specifies a text input box that can be displayed with the
     * channel. More info
     * http://blogs.law.harvard.edu/tech/rss#lttextinputgtSubelementOfLtchannelgt
     * here.
     * The purpose of the textInput element is something of a
     * mystery. You can use it to specify a search engine box. Or
     * to allow a reader to provide feedback. Most aggregators
     * ignore it.
     */
    private TextInput _textInput;

    /**
     * A hint for aggregators telling them which hours they can
     * skip. 
     * More info
     * http://blogs.law.harvard.edu/tech/skipHoursDays#skiphours
     * here.
     */
    private java.lang.Object _skipHours;

    /**
     * A hint for aggregators telling them which days they can
     * skip. 
     * More info
     * http://blogs.law.harvard.edu/tech/skipHoursDays#skipdays
     * here.
     */
    private java.lang.Object _skipDays;

    /**
     * Field _itemList
     */
    private java.util.Vector _itemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Channel() {
        super();
        setLanguage("en-us");
        setGenerator("jACOB ApplicationServer");
        setDocs("http://blogs.law.harvard.edu/tech/rss");
        _itemList = new Vector();
    } //-- jacob.rss.Channel()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addItem
     * 
     * @param vItem
     */
    public void addItem(jacob.rss.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.addElement(vItem);
    } //-- void addItem(jacob.rss.Item) 

    /**
     * Method addItem
     * 
     * @param index
     * @param vItem
     */
    public void addItem(int index, jacob.rss.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.insertElementAt(vItem, index);
    } //-- void addItem(int, jacob.rss.Item) 

    /**
     * Method deleteVersion
     */
    public void deleteVersion()
    {
        this._has_version= false;
    } //-- void deleteVersion() 

    /**
     * Method enumerateItem
     */
    public java.util.Enumeration enumerateItem()
    {
        return _itemList.elements();
    } //-- java.util.Enumeration enumerateItem() 

    /**
     * Returns the value of field 'category'. The field 'category'
     * has the following description: Specify one or more
     * categories that the channel belongs to. Follows the same
     * rules as the item-level
     * http://blogs.law.harvard.edu/tech/rss#ltcategorygtSubelementOfLtitemgt"
     * category element. More
     * http://blogs.law.harvard.edu/tech/rss#syndic8 info.
     * 
     * @return the value of field 'category'.
     */
    public java.lang.String getCategory()
    {
        return this._category;
    } //-- java.lang.String getCategory() 

    /**
     * Returns the value of field 'cloud'.
     * 
     * @return the value of field 'cloud'.
     */
    public Cloud getCloud()
    {
        return this._cloud;
    } //-- Cloud getCloud() 

    /**
     * Returns the value of field 'copyright'. The field
     * 'copyright' has the following description: Copyright notice
     * for content in the channel.
     * 
     * @return the value of field 'copyright'.
     */
    public java.lang.String getCopyright()
    {
        return this._copyright;
    } //-- java.lang.String getCopyright() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: Phrase or
     * sentence describing the channel.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'docs'. The field 'docs' has the
     * following description: A URL that points to the
     * documentation for the format used in the RSS file. It's
     * probably a pointer to this page. It's for people who might
     * stumble across an RSS file on a Web server 25 years from now
     * and wonder what it is.
     * 
     * @return the value of field 'docs'.
     */
    public java.lang.String getDocs()
    {
        return this._docs;
    } //-- java.lang.String getDocs() 

    /**
     * Returns the value of field 'generator'. The field
     * 'generator' has the following description: A string
     * indicating the program used to generate the channel.
     * 
     * @return the value of field 'generator'.
     */
    public java.lang.String getGenerator()
    {
        return this._generator;
    } //-- java.lang.String getGenerator() 

    /**
     * Returns the value of field 'image'. The field 'image' has
     * the following description: Specifies a GIF, JPEG or PNG
     * image that can be displayed with the channel. More info
     * http://blogs.law.harvard.edu/tech/rss#ltimagegtSubelementOfLtchannelgt
     * here. 
     * 
     * @return the value of field 'image'.
     */
    public Image getImage()
    {
        return this._image;
    } //-- Image getImage() 

    /**
     * Method getItem
     * 
     * @param index
     */
    public jacob.rss.Item getItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (jacob.rss.Item) _itemList.elementAt(index);
    } //-- jacob.rss.Item getItem(int) 

    /**
     * Method getItem
     */
    public jacob.rss.Item[] getItem()
    {
        int size = _itemList.size();
        jacob.rss.Item[] mArray = new jacob.rss.Item[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (jacob.rss.Item) _itemList.elementAt(index);
        }
        return mArray;
    } //-- jacob.rss.Item[] getItem() 

    /**
     * Method getItemCount
     */
    public int getItemCount()
    {
        return _itemList.size();
    } //-- int getItemCount() 

    /**
     * Returns the value of field 'language'. The field 'language'
     * has the following description: The language the channel is
     * written in. This allows aggregators to group all Italian
     * language sites, for example, on a single page. A list of
     * allowable values for this element, as provided by Netscape,
     * is http://blogs.law.harvard.edu/tech/stories/storyReader$15
     * here. You may also use
     * http://www.w3.org/TR/REC-html40/struct/dirlang.html#langcodes
     * values defined by the W3C.
     * 
     * @return the value of field 'language'.
     */
    public java.lang.String getLanguage()
    {
        return this._language;
    } //-- java.lang.String getLanguage() 

    /**
     * Returns the value of field 'lastBuildDate'. The field
     * 'lastBuildDate' has the following description: The last time
     * the content of the channel changed.
     * 
     * @return the value of field 'lastBuildDate'.
     */
    public java.util.Date getLastBuildDate()
    {
        return this._lastBuildDate;
    } //-- java.util.Date getLastBuildDate() 

    /**
     * Returns the value of field 'link'. The field 'link' has the
     * following description: The URL to the HTML website
     * corresponding to the channel.
     * 
     * @return the value of field 'link'.
     */
    public java.lang.String getLink()
    {
        return this._link;
    } //-- java.lang.String getLink() 

    /**
     * Returns the value of field 'managingEditor'. The field
     * 'managingEditor' has the following description: Email
     * address for person responsible for editorial content.
     * 
     * @return the value of field 'managingEditor'.
     */
    public java.lang.String getManagingEditor()
    {
        return this._managingEditor;
    } //-- java.lang.String getManagingEditor() 

    /**
     * Returns the value of field 'pubDate'. The field 'pubDate'
     * has the following description: The publication date for the
     * content in the channel. For example, the New York Times
     * publishes on a daily basis, the publication date flips once
     * every 24 hours. That's when the pubDate of the channel
     * changes. All date-times in RSS conform to the Date and Time
     * Specification of http://asg.web.cmu.edu/rf/rfc822.html RFC
     * 822, with the exception that the year may be expressed with
     * two characters or four characters (four preferred).
     * 
     * @return the value of field 'pubDate'.
     */
    public java.util.Date getPubDate()
    {
        return this._pubDate;
    } //-- java.util.Date getPubDate() 

    /**
     * Returns the value of field 'rating'. The field 'rating' has
     * the following description: The http://www.w3.org/PICS/ PICS
     * rating for the channel.
     * 
     * @return the value of field 'rating'.
     */
    public java.lang.Object getRating()
    {
        return this._rating;
    } //-- java.lang.Object getRating() 

    /**
     * Returns the value of field 'skipDays'. The field 'skipDays'
     * has the following description: A hint for aggregators
     * telling them which days they can skip. 
     * More info
     * http://blogs.law.harvard.edu/tech/skipHoursDays#skipdays
     * here.
     * 
     * 
     * @return the value of field 'skipDays'.
     */
    public java.lang.Object getSkipDays()
    {
        return this._skipDays;
    } //-- java.lang.Object getSkipDays() 

    /**
     * Returns the value of field 'skipHours'. The field
     * 'skipHours' has the following description: A hint for
     * aggregators telling them which hours they can skip. 
     * More info
     * http://blogs.law.harvard.edu/tech/skipHoursDays#skiphours
     * here.
     * 
     * 
     * @return the value of field 'skipHours'.
     */
    public java.lang.Object getSkipHours()
    {
        return this._skipHours;
    } //-- java.lang.Object getSkipHours() 

    /**
     * Returns the value of field 'textInput'. The field
     * 'textInput' has the following description: Specifies a text
     * input box that can be displayed with the channel. More info
     * http://blogs.law.harvard.edu/tech/rss#lttextinputgtSubelementOfLtchannelgt
     * here.
     * The purpose of the textInput element is something of a
     * mystery. You can use it to specify a search engine box. Or
     * to allow a reader to provide feedback. Most aggregators
     * ignore it.
     * 
     * @return the value of field 'textInput'.
     */
    public TextInput getTextInput()
    {
        return this._textInput;
    } //-- TextInput getTextInput() 

    /**
     * Returns the value of field 'title'. The field 'title' has
     * the following description: The name of the channel. It's how
     * people refer to your service. If you have an HTML website
     * that contains the same information as your RSS file, the
     * title of your channel should be the same as the title of
     * your website. 
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
     * Returns the value of field 'ttl'. The field 'ttl' has the
     * following description: ttl stands for time to live. It's a
     * number of minutes that indicates how long a channel can be
     * cached before refreshing from the source. 
     * More info
     * http://blogs.law.harvard.edu/tech/rss#ltttlgtSubelementOfLtchannelgt
     * here.
     * 
     * @return the value of field 'ttl'.
     */
    public java.lang.Object getTtl()
    {
        return this._ttl;
    } //-- java.lang.Object getTtl() 

    /**
     * Returns the value of field 'version'.
     * 
     * @return the value of field 'version'.
     */
    public double getVersion()
    {
        return this._version;
    } //-- double getVersion() 

    /**
     * Returns the value of field 'webMaster'. The field
     * 'webMaster' has the following description: Email address for
     * person responsible for technical issues relating to channel.
     * 
     * @return the value of field 'webMaster'.
     */
    public java.lang.String getWebMaster()
    {
        return this._webMaster;
    } //-- java.lang.String getWebMaster() 

    /**
     * Method hasVersion
     */
    public boolean hasVersion()
    {
        return this._has_version;
    } //-- boolean hasVersion() 

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
     * Method removeAllItem
     */
    public void removeAllItem()
    {
        _itemList.removeAllElements();
    } //-- void removeAllItem() 

    /**
     * Method removeItem
     * 
     * @param index
     */
    public jacob.rss.Item removeItem(int index)
    {
        java.lang.Object obj = _itemList.elementAt(index);
        _itemList.removeElementAt(index);
        return (jacob.rss.Item) obj;
    } //-- jacob.rss.Item removeItem(int) 

    /**
     * Sets the value of field 'category'. The field 'category' has
     * the following description: Specify one or more categories
     * that the channel belongs to. Follows the same rules as the
     * item-level
     * http://blogs.law.harvard.edu/tech/rss#ltcategorygtSubelementOfLtitemgt"
     * category element. More
     * http://blogs.law.harvard.edu/tech/rss#syndic8 info.
     * 
     * @param category the value of field 'category'.
     */
    public void setCategory(java.lang.String category)
    {
        this._category = category;
    } //-- void setCategory(java.lang.String) 

    /**
     * Sets the value of field 'cloud'.
     * 
     * @param cloud the value of field 'cloud'.
     */
    public void setCloud(Cloud cloud)
    {
        this._cloud = cloud;
    } //-- void setCloud(Cloud) 

    /**
     * Sets the value of field 'copyright'. The field 'copyright'
     * has the following description: Copyright notice for content
     * in the channel.
     * 
     * @param copyright the value of field 'copyright'.
     */
    public void setCopyright(java.lang.String copyright)
    {
        this._copyright = copyright;
    } //-- void setCopyright(java.lang.String) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: Phrase or
     * sentence describing the channel.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'docs'. The field 'docs' has the
     * following description: A URL that points to the
     * documentation for the format used in the RSS file. It's
     * probably a pointer to this page. It's for people who might
     * stumble across an RSS file on a Web server 25 years from now
     * and wonder what it is.
     * 
     * @param docs the value of field 'docs'.
     */
    public void setDocs(java.lang.String docs)
    {
        this._docs = docs;
    } //-- void setDocs(java.lang.String) 

    /**
     * Sets the value of field 'generator'. The field 'generator'
     * has the following description: A string indicating the
     * program used to generate the channel.
     * 
     * @param generator the value of field 'generator'.
     */
    public void setGenerator(java.lang.String generator)
    {
        this._generator = generator;
    } //-- void setGenerator(java.lang.String) 

    /**
     * Sets the value of field 'image'. The field 'image' has the
     * following description: Specifies a GIF, JPEG or PNG image
     * that can be displayed with the channel. More info
     * http://blogs.law.harvard.edu/tech/rss#ltimagegtSubelementOfLtchannelgt
     * here. 
     * 
     * @param image the value of field 'image'.
     */
    public void setImage(Image image)
    {
        this._image = image;
    } //-- void setImage(Image) 

    /**
     * Method setItem
     * 
     * @param index
     * @param vItem
     */
    public void setItem(int index, jacob.rss.Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _itemList.setElementAt(vItem, index);
    } //-- void setItem(int, jacob.rss.Item) 

    /**
     * Method setItem
     * 
     * @param itemArray
     */
    public void setItem(jacob.rss.Item[] itemArray)
    {
        //-- copy array
        _itemList.removeAllElements();
        for (int i = 0; i < itemArray.length; i++) {
            _itemList.addElement(itemArray[i]);
        }
    } //-- void setItem(jacob.rss.Item) 

    /**
     * Sets the value of field 'language'. The field 'language' has
     * the following description: The language the channel is
     * written in. This allows aggregators to group all Italian
     * language sites, for example, on a single page. A list of
     * allowable values for this element, as provided by Netscape,
     * is http://blogs.law.harvard.edu/tech/stories/storyReader$15
     * here. You may also use
     * http://www.w3.org/TR/REC-html40/struct/dirlang.html#langcodes
     * values defined by the W3C.
     * 
     * @param language the value of field 'language'.
     */
    public void setLanguage(java.lang.String language)
    {
        this._language = language;
    } //-- void setLanguage(java.lang.String) 

    /**
     * Sets the value of field 'lastBuildDate'. The field
     * 'lastBuildDate' has the following description: The last time
     * the content of the channel changed.
     * 
     * @param lastBuildDate the value of field 'lastBuildDate'.
     */
    public void setLastBuildDate(java.util.Date lastBuildDate)
    {
        this._lastBuildDate = lastBuildDate;
    } //-- void setLastBuildDate(java.util.Date) 

    /**
     * Sets the value of field 'link'. The field 'link' has the
     * following description: The URL to the HTML website
     * corresponding to the channel.
     * 
     * @param link the value of field 'link'.
     */
    public void setLink(java.lang.String link)
    {
        this._link = link;
    } //-- void setLink(java.lang.String) 

    /**
     * Sets the value of field 'managingEditor'. The field
     * 'managingEditor' has the following description: Email
     * address for person responsible for editorial content.
     * 
     * @param managingEditor the value of field 'managingEditor'.
     */
    public void setManagingEditor(java.lang.String managingEditor)
    {
        this._managingEditor = managingEditor;
    } //-- void setManagingEditor(java.lang.String) 

    /**
     * Sets the value of field 'pubDate'. The field 'pubDate' has
     * the following description: The publication date for the
     * content in the channel. For example, the New York Times
     * publishes on a daily basis, the publication date flips once
     * every 24 hours. That's when the pubDate of the channel
     * changes. All date-times in RSS conform to the Date and Time
     * Specification of http://asg.web.cmu.edu/rf/rfc822.html RFC
     * 822, with the exception that the year may be expressed with
     * two characters or four characters (four preferred).
     * 
     * @param pubDate the value of field 'pubDate'.
     */
    public void setPubDate(java.util.Date pubDate)
    {
        this._pubDate = pubDate;
    } //-- void setPubDate(java.util.Date) 

    /**
     * Sets the value of field 'rating'. The field 'rating' has the
     * following description: The http://www.w3.org/PICS/ PICS
     * rating for the channel.
     * 
     * @param rating the value of field 'rating'.
     */
    public void setRating(java.lang.Object rating)
    {
        this._rating = rating;
    } //-- void setRating(java.lang.Object) 

    /**
     * Sets the value of field 'skipDays'. The field 'skipDays' has
     * the following description: A hint for aggregators telling
     * them which days they can skip. 
     * More info
     * http://blogs.law.harvard.edu/tech/skipHoursDays#skipdays
     * here.
     * 
     * 
     * @param skipDays the value of field 'skipDays'.
     */
    public void setSkipDays(java.lang.Object skipDays)
    {
        this._skipDays = skipDays;
    } //-- void setSkipDays(java.lang.Object) 

    /**
     * Sets the value of field 'skipHours'. The field 'skipHours'
     * has the following description: A hint for aggregators
     * telling them which hours they can skip. 
     * More info
     * http://blogs.law.harvard.edu/tech/skipHoursDays#skiphours
     * here.
     * 
     * 
     * @param skipHours the value of field 'skipHours'.
     */
    public void setSkipHours(java.lang.Object skipHours)
    {
        this._skipHours = skipHours;
    } //-- void setSkipHours(java.lang.Object) 

    /**
     * Sets the value of field 'textInput'. The field 'textInput'
     * has the following description: Specifies a text input box
     * that can be displayed with the channel. More info
     * http://blogs.law.harvard.edu/tech/rss#lttextinputgtSubelementOfLtchannelgt
     * here.
     * The purpose of the textInput element is something of a
     * mystery. You can use it to specify a search engine box. Or
     * to allow a reader to provide feedback. Most aggregators
     * ignore it.
     * 
     * @param textInput the value of field 'textInput'.
     */
    public void setTextInput(TextInput textInput)
    {
        this._textInput = textInput;
    } //-- void setTextInput(TextInput) 

    /**
     * Sets the value of field 'title'. The field 'title' has the
     * following description: The name of the channel. It's how
     * people refer to your service. If you have an HTML website
     * that contains the same information as your RSS file, the
     * title of your channel should be the same as the title of
     * your website. 
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Sets the value of field 'ttl'. The field 'ttl' has the
     * following description: ttl stands for time to live. It's a
     * number of minutes that indicates how long a channel can be
     * cached before refreshing from the source. 
     * More info
     * http://blogs.law.harvard.edu/tech/rss#ltttlgtSubelementOfLtchannelgt
     * here.
     * 
     * @param ttl the value of field 'ttl'.
     */
    public void setTtl(java.lang.Object ttl)
    {
        this._ttl = ttl;
    } //-- void setTtl(java.lang.Object) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(double version)
    {
        this._version = version;
        this._has_version = true;
    } //-- void setVersion(double) 

    /**
     * Sets the value of field 'webMaster'. The field 'webMaster'
     * has the following description: Email address for person
     * responsible for technical issues relating to channel.
     * 
     * @param webMaster the value of field 'webMaster'.
     */
    public void setWebMaster(java.lang.String webMaster)
    {
        this._webMaster = webMaster;
    } //-- void setWebMaster(java.lang.String) 

    /**
     * Method unmarshalChannel
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalChannel(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.rss.Channel) Unmarshaller.unmarshal(jacob.rss.Channel.class, reader);
    } //-- java.lang.Object unmarshalChannel(java.io.Reader) 

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
