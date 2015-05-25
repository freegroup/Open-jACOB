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
 * A channel may contain any number of items. An item may represent
 * a "story" -- much like a story in a newspaper or magazine; if so
 * its description is a synopsis of the story, and the link points
 * to the full story. An item may also be complete in itself, if
 * so, the description contains the text 
 * (entity-encoded HTML is allowed), and the link and title may be
 * omitted. All elements of an item are optional, however at least
 * one of title or description must be present.
 * 
 * @version $Revision$ $Date$
 */
public class Item implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _title
     */
    private java.lang.String _title;

    /**
     * The URL of the item.
     */
    private java.lang.String _link;

    /**
     * The item synopsis. 
     */
    private java.lang.String _description;

    /**
     * It's the email address of the author of the item. For
     * newspapers and magazines syndicating via RSS, the author is
     * the person who wrote the article that the item describes.
     * For collaborative weblogs, the author of the item might be
     * different from the managing editor or webmaster. For a
     * weblog authored by a single individual it would make sense
     * to omit the author element.
     */
    private java.lang.String _author;

    /**
     * Field _categoryList
     */
    private java.util.Vector _categoryList;

    /**
     * If present, it is the url of the comments page for the item. 
     */
    private java.lang.String _comments;

    /**
     * Describes a media object that is attached to the item. 
     */
    private Enclosure _enclosure;

    /**
     * A string that uniquely identifies the item. There are no
     * rules for the syntax of a guid. Aggregators must view them
     * as a string. It's up to the source of the feed to establish
     * the uniqueness of the string.
     */
    private java.lang.String _guid;

    /**
     * Indicates when the item was published.
     */
    private java.util.Date _pubDate;

    /**
     * The RSS channel that the item came from. The purpose of this
     * element is to propagate credit for links, to publicize the
     * sources of news items. It can be used in the Post command of
     * an aggregator. It should be generated automatically when
     * forwarding an item from an aggregator to a weblog authoring
     * tool.
     */
    private Source _source;


      //----------------/
     //- Constructors -/
    //----------------/

    public Item() {
        super();
        _categoryList = new Vector();
    } //-- jacob.rss.Item()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addCategory
     * 
     * @param vCategory
     */
    public void addCategory(Category vCategory)
        throws java.lang.IndexOutOfBoundsException
    {
        _categoryList.addElement(vCategory);
    } //-- void addCategory(Category) 

    /**
     * Method addCategory
     * 
     * @param index
     * @param vCategory
     */
    public void addCategory(int index, Category vCategory)
        throws java.lang.IndexOutOfBoundsException
    {
        _categoryList.insertElementAt(vCategory, index);
    } //-- void addCategory(int, Category) 

    /**
     * Method enumerateCategory
     */
    public java.util.Enumeration enumerateCategory()
    {
        return _categoryList.elements();
    } //-- java.util.Enumeration enumerateCategory() 

    /**
     * Returns the value of field 'author'. The field 'author' has
     * the following description: It's the email address of the
     * author of the item. For newspapers and magazines syndicating
     * via RSS, the author is the person who wrote the article that
     * the item describes. For collaborative weblogs, the author of
     * the item might be different from the managing editor or
     * webmaster. For a weblog authored by a single individual it
     * would make sense to omit the author element.
     * 
     * @return the value of field 'author'.
     */
    public java.lang.String getAuthor()
    {
        return this._author;
    } //-- java.lang.String getAuthor() 

    /**
     * Method getCategory
     * 
     * @param index
     */
    public Category getCategory(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _categoryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Category) _categoryList.elementAt(index);
    } //-- Category getCategory(int) 

    /**
     * Method getCategory
     */
    public Category[] getCategory()
    {
        int size = _categoryList.size();
        Category[] mArray = new Category[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Category) _categoryList.elementAt(index);
        }
        return mArray;
    } //-- Category[] getCategory() 

    /**
     * Method getCategoryCount
     */
    public int getCategoryCount()
    {
        return _categoryList.size();
    } //-- int getCategoryCount() 

    /**
     * Returns the value of field 'comments'. The field 'comments'
     * has the following description: If present, it is the url of
     * the comments page for the item. 
     * 
     * @return the value of field 'comments'.
     */
    public java.lang.String getComments()
    {
        return this._comments;
    } //-- java.lang.String getComments() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: The item
     * synopsis. 
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'enclosure'. The field
     * 'enclosure' has the following description: Describes a media
     * object that is attached to the item. 
     * 
     * @return the value of field 'enclosure'.
     */
    public Enclosure getEnclosure()
    {
        return this._enclosure;
    } //-- Enclosure getEnclosure() 

    /**
     * Returns the value of field 'guid'. The field 'guid' has the
     * following description: A string that uniquely identifies the
     * item. There are no rules for the syntax of a guid.
     * Aggregators must view them as a string. It's up to the
     * source of the feed to establish the uniqueness of the
     * string.
     * 
     * @return the value of field 'guid'.
     */
    public java.lang.String getGuid()
    {
        return this._guid;
    } //-- java.lang.String getGuid() 

    /**
     * Returns the value of field 'link'. The field 'link' has the
     * following description: The URL of the item.
     * 
     * @return the value of field 'link'.
     */
    public java.lang.String getLink()
    {
        return this._link;
    } //-- java.lang.String getLink() 

    /**
     * Returns the value of field 'pubDate'. The field 'pubDate'
     * has the following description: Indicates when the item was
     * published.
     * 
     * @return the value of field 'pubDate'.
     */
    public java.util.Date getPubDate()
    {
        return this._pubDate;
    } //-- java.util.Date getPubDate() 

    /**
     * Returns the value of field 'source'. The field 'source' has
     * the following description: The RSS channel that the item
     * came from. The purpose of this element is to propagate
     * credit for links, to publicize the sources of news items. It
     * can be used in the Post command of an aggregator. It should
     * be generated automatically when forwarding an item from an
     * aggregator to a weblog authoring tool.
     * 
     * @return the value of field 'source'.
     */
    public Source getSource()
    {
        return this._source;
    } //-- Source getSource() 

    /**
     * Returns the value of field 'title'.
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

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
     * Method removeAllCategory
     */
    public void removeAllCategory()
    {
        _categoryList.removeAllElements();
    } //-- void removeAllCategory() 

    /**
     * Method removeCategory
     * 
     * @param index
     */
    public Category removeCategory(int index)
    {
        java.lang.Object obj = _categoryList.elementAt(index);
        _categoryList.removeElementAt(index);
        return (Category) obj;
    } //-- Category removeCategory(int) 

    /**
     * Sets the value of field 'author'. The field 'author' has the
     * following description: It's the email address of the author
     * of the item. For newspapers and magazines syndicating via
     * RSS, the author is the person who wrote the article that the
     * item describes. For collaborative weblogs, the author of the
     * item might be different from the managing editor or
     * webmaster. For a weblog authored by a single individual it
     * would make sense to omit the author element.
     * 
     * @param author the value of field 'author'.
     */
    public void setAuthor(java.lang.String author)
    {
        this._author = author;
    } //-- void setAuthor(java.lang.String) 

    /**
     * Method setCategory
     * 
     * @param index
     * @param vCategory
     */
    public void setCategory(int index, Category vCategory)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _categoryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _categoryList.setElementAt(vCategory, index);
    } //-- void setCategory(int, Category) 

    /**
     * Method setCategory
     * 
     * @param categoryArray
     */
    public void setCategory(Category[] categoryArray)
    {
        //-- copy array
        _categoryList.removeAllElements();
        for (int i = 0; i < categoryArray.length; i++) {
            _categoryList.addElement(categoryArray[i]);
        }
    } //-- void setCategory(Category) 

    /**
     * Sets the value of field 'comments'. The field 'comments' has
     * the following description: If present, it is the url of the
     * comments page for the item. 
     * 
     * @param comments the value of field 'comments'.
     */
    public void setComments(java.lang.String comments)
    {
        this._comments = comments;
    } //-- void setComments(java.lang.String) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: The item
     * synopsis. 
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'enclosure'. The field 'enclosure'
     * has the following description: Describes a media object that
     * is attached to the item. 
     * 
     * @param enclosure the value of field 'enclosure'.
     */
    public void setEnclosure(Enclosure enclosure)
    {
        this._enclosure = enclosure;
    } //-- void setEnclosure(Enclosure) 

    /**
     * Sets the value of field 'guid'. The field 'guid' has the
     * following description: A string that uniquely identifies the
     * item. There are no rules for the syntax of a guid.
     * Aggregators must view them as a string. It's up to the
     * source of the feed to establish the uniqueness of the
     * string.
     * 
     * @param guid the value of field 'guid'.
     */
    public void setGuid(java.lang.String guid)
    {
        this._guid = guid;
    } //-- void setGuid(java.lang.String) 

    /**
     * Sets the value of field 'link'. The field 'link' has the
     * following description: The URL of the item.
     * 
     * @param link the value of field 'link'.
     */
    public void setLink(java.lang.String link)
    {
        this._link = link;
    } //-- void setLink(java.lang.String) 

    /**
     * Sets the value of field 'pubDate'. The field 'pubDate' has
     * the following description: Indicates when the item was
     * published.
     * 
     * @param pubDate the value of field 'pubDate'.
     */
    public void setPubDate(java.util.Date pubDate)
    {
        this._pubDate = pubDate;
    } //-- void setPubDate(java.util.Date) 

    /**
     * Sets the value of field 'source'. The field 'source' has the
     * following description: The RSS channel that the item came
     * from. The purpose of this element is to propagate credit for
     * links, to publicize the sources of news items. It can be
     * used in the Post command of an aggregator. It should be
     * generated automatically when forwarding an item from an
     * aggregator to a weblog authoring tool.
     * 
     * @param source the value of field 'source'.
     */
    public void setSource(Source source)
    {
        this._source = source;
    } //-- void setSource(Source) 

    /**
     * Sets the value of field 'title'.
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Method unmarshalItem
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalItem(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.rss.Item) Unmarshaller.unmarshal(jacob.rss.Item.class, reader);
    } //-- java.lang.Object unmarshalItem(java.io.Reader) 

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
