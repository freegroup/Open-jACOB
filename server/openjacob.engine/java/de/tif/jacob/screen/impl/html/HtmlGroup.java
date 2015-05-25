/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen.impl.html;

import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.model.AbstractWikiModel;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.model.SemanticAttribute;
import info.bliki.wiki.model.SemanticRelation;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.definition.IHtmlGroupDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IHtmlGroup;
import de.tif.jacob.screen.IImageProvider;
import de.tif.jacob.screen.event.IHtmlGroupEventHandler;
import de.tif.jacob.util.FastStringWriter;
import de.tif.jacob.util.StringUtil;

public class HtmlGroup extends Group implements IHtmlGroup, IImageProvider
{
  private final IHtmlGroupDefinition definition;

  private long   changeCount = -1;
  private FastStringWriter html = new FastStringWriter(2048);
  
  private Map id2value = new HashMap();
  
  public class JacobWikiModel extends AbstractWikiModel 
  {
    protected Set categories = null;
    protected Set links = null;
    protected Set templates = null;
    protected List semanticRelations = null;
    protected List semanticAttributes = null;
    protected String fExternalImageBaseURL;
    protected String fExternalWikiBaseURL;

    public JacobWikiModel(String imageBaseURL, String linkBaseURL) 
    {
      super(Configuration.DEFAULT_CONFIGURATION);
      fExternalImageBaseURL = imageBaseURL;
      fExternalWikiBaseURL = linkBaseURL;
    }

    public void addCategory(String categoryName, String sortKey) 
    {
      categories.add(categoryName);
    }

    public void addLink(String topicName) 
    {
      links.add(topicName);
    }

    public boolean addSemanticAttribute(String attribute, String attributeValue) 
    {
      if (semanticAttributes == null) 
      {
        semanticAttributes = new ArrayList();
      }
      semanticAttributes.add(new SemanticAttribute(attribute, attributeValue));
      return true;
    }

    public boolean addSemanticRelation(String relation, String relationValue) 
    {
      if (semanticRelations == null) 
      {
        semanticRelations = new ArrayList();
      }
      semanticRelations.add(new SemanticRelation(relation, relationValue));
      return true;
    }

    public void addTemplate(String template) 
    {
      templates.add(template);
    }

    public void appendInternalLink(String topic, String hashSection, String topicDescription, String cssClass) 
    {
      String encodedtopic = StringUtil.toJavascriptString(topic);
      if (replaceColon()) 
      {
        encodedtopic = encodedtopic.replaceAll(":", "/");
      }
      String hrefLink = StringUtil.replace(fExternalWikiBaseURL, "${title}", encodedtopic);
      super.appendInternalLink(hrefLink, hashSection, topicDescription, cssClass);
    }

    public void appendRawWikipediaLink(String rawLinkText, String suffix) 
    {
      String rawTopicName = rawLinkText;
      if (rawTopicName != null) 
      {
        // trim the name for whitespace characters on the left side
        int trimLeftIndex = 0;
        while ((trimLeftIndex < rawTopicName.length()) && (rawTopicName.charAt(trimLeftIndex) <= ' ')) 
        {
          trimLeftIndex++;
        }
        if (trimLeftIndex > 0) 
        {
          rawTopicName = rawTopicName.substring(trimLeftIndex);
        }
        // Is there an alias like [alias|link] ?
        int pipeIndex = rawTopicName.lastIndexOf('|');
        String alias = "";
        if (-1 != pipeIndex) 
        {
          alias = rawTopicName.substring(pipeIndex + 1);
          rawTopicName = rawTopicName.substring(0, pipeIndex);
          if (alias.length() == 0) 
          {
            // special cases like: [[Test:hello world|]] or [[Test(hello
            // world)|]]
            // or [[Test, hello world|]]
            alias = rawTopicName;
            int index = alias.indexOf(':');
            if (index != -1) 
            {
              alias = alias.substring(index + 1).trim();
            } 
            else 
            {
              index = alias.indexOf('(');
              if (index != -1) 
              {
                alias = alias.substring(0, index).trim();
              }
              else 
              {
                index = alias.indexOf(',');
                if (index != -1) 
                {
                  alias = alias.substring(0, index).trim();
                }
              }
            }
          }
        }


        // trim the name for whitespace characters on the right side
        int trimRightIndex = rawTopicName.length() - 1;
        while ((trimRightIndex >= 0) && (rawTopicName.charAt(trimRightIndex) <= ' ')) 
        {
          trimRightIndex--;
        }
        if (trimRightIndex != rawTopicName.length() - 1) 
        {
          rawTopicName = rawTopicName.substring(0, trimRightIndex + 1);
        }

//        rawTopicName = Encoder.encodeHtml(rawTopicName);
        String viewableLinkDescription;
        if (-1 != pipeIndex) 
        {
          viewableLinkDescription = alias + suffix;
        } 
        else 
        {
          if (rawTopicName.length() > 0 && rawTopicName.charAt(0) == ':') 
          {
            viewableLinkDescription = rawTopicName.substring(1) + suffix;
          } 
          else 
          {
            viewableLinkDescription = rawTopicName + suffix;
          }
        }

        if (appendRawNamespaceLinks(rawTopicName, viewableLinkDescription, pipeIndex == (-1))) 
        {
          return;
        }

        int indx = rawTopicName.indexOf(':');
        String namespace = null;
        if (indx >= 0) {
          namespace = rawTopicName.substring(0, indx);
        }
        if (namespace != null && isImageNamespace(namespace)) {
          parseInternalImageLink(namespace, rawLinkText);
          return;
        } else {
          if (rawTopicName.length() > 0 && rawTopicName.charAt(0) == ':') {
            rawTopicName = rawTopicName.substring(1);
          }
          if (rawTopicName.length() > 0 && rawTopicName.charAt(0) == ':') {
            rawTopicName = rawTopicName.substring(1);
          }
          addLink(rawTopicName);
          appendInternalLink(rawTopicName, null, viewableLinkDescription, null);
        }
      }
    }
    /**
     * Get the set of Wikipedia category names used in this text
     * 
     * @return the set of category strings
     */
    public Set getCategories() {
      return categories;
    }

    /**
     * Get the set of Wikipedia links used in this text
     * 
     * @return the set of category strings
     */
    public Set getLinks() {
      return links;
    }

    public List getSemanticAttributes() {
      return semanticAttributes;
    }

    public List getSemanticRelations() {
      return semanticRelations;
    }

    public Set getTemplates() {
      return templates;
    }

    /**
     * Append the internal wiki image link to this model.
     * 
     * <br/><br/><b>Note</b>: the pipe symbol (i.e. &quot;|&quot;) splits the
     * <code>rawImageLink</code> into different segments. The first segment is
     * used as the <code>&lt;image-name&gt;</code> and typically ends with
     * extensions like <code>.png</code>, <code>.gif</code>,
     * <code>.jpg</code> or <code>.jpeg</code>.
     * 
     * <br/><br/><b>Note</b>: if the image link contains a "width" attribute,
     * the filename is constructed as
     * <code>&lt;size&gt;px-&lt;image-name&gt;</code>, otherwise it's only the
     * <code>&lt;image-name&gt;</code>.
     * 
     * @param imageNamespace
     *          the image namespace
     * @param rawImageLink
     *          the raw image link text without the surrounding
     *          <code>[[...]]</code>
     */
    public void parseInternalImageLink(String imageNamespace, String rawImageLink) {
      if (fExternalImageBaseURL != null) {
        String imageHref = fExternalWikiBaseURL;
        String imageSrc = fExternalImageBaseURL;
        ImageFormat imageFormat = ImageFormat.getImageFormat(rawImageLink, imageNamespace);

        String imageName = imageFormat.getFilename();
//        String sizeStr = imageFormat.getSizeStr();
//        if (sizeStr != null) {
//          imageName = sizeStr + '-' + imageName;
//        }
        if (imageName.endsWith(".svg")) {
          imageName += ".png";
        }
        imageName = Encoder.encodeUrl(imageName);
        if (replaceColon()) {
          imageName = imageName.replaceAll(":", "/");
        }
        if (replaceColon()) {
          imageHref = StringUtil.replace(imageHref, "${title}", imageNamespace + '/' + imageName);
          imageSrc = StringUtil.replace(imageSrc, "${image}", imageName);
        } else {
          imageHref = StringUtil.replace(imageHref, "${title}", imageNamespace + ':' + imageName);
          imageSrc = StringUtil.replace(imageSrc, "${image}", imageName);
        }
        appendInternalImageLink(imageHref, imageSrc, imageFormat);
      }
    }

    public boolean replaceColon() 
    {
      return false;
    }

    public void setUp() 
    {
      super.setUp();
      categories = new HashSet();
      links = new HashSet();
      templates = new HashSet();
    }

  }

  public HtmlGroup(IApplication app, IHtmlGroupDefinition group)
  {
    super(app, group, null);
    this.definition = group;
  }

  /**
   * Methode ist nur vorhanden um legacy Interface declarationen zu befriedigen.
   * Interface Deklaration wird in einem zukünftigen Release ersatzlos gestrichen.
   */
  public void setBorder(boolean flag)
  {
    // Exception werfen damit man nicht auf die Idee kommt die Methode in einer HtmlGroup zu rufen.
    throw new RuntimeException("Invalid call of HtmlGroup.setBorder(boolean..). This method is not supported");
  }
  
  
  public void resetCache()
  {
    this.id2value = new HashMap();
    super.resetCache();
  }

  /**
   * Return the HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    context.setGroup(this);
    
    // initial set of the data status
    if(getDataStatus()==UNDEFINED)
      setDataStatus(context,SEARCH);

    // update the element if the data has been changed
    // /e.g. an element of a table alias has been selected
    //
    IDataTableInternal table = (IDataTableInternal) context.getDataTable(this.definition.getTableAlias().getName());
    IDataTableRecord currentDataRecord = getSelectedRecord(context);

    if(table.getChangeCount()!= changeCount)
    {
      // den Status des Record an die GUI weiterleiten
      // Es kann sein, das ein Record durch einen Hook manipuliert worden ist. Darauf muss nun
      // reagiert werden.
      //
      if(currentDataRecord!=null && currentDataRecord.isUpdated() && getDataStatus()!=UPDATE)
        setDataStatus(context, UPDATE);
      else if(currentDataRecord!=null && currentDataRecord.isNormal() && getDataStatus()!=SELECTED)
        setDataStatus(context, SELECTED);
      else if(currentDataRecord!=null && currentDataRecord.isNormal() && getDataStatus()==SELECTED && changeCount!=table.getChangeCount())
        setDataStatus(context, SELECTED);
      else if(currentDataRecord!=null && currentDataRecord.isNew() && getDataStatus()!=NEW)
        setDataStatus(context,NEW);
      else if(currentDataRecord==null &&  getDataStatus()!=SEARCH)
        setDataStatus(context,SEARCH);
      
      // set new change counter after all children have been informed
      // (nobody knows what they will do which has an influence on the change counter as well :-)
      changeCount = table.getChangeCount();
    }
    super.calculateHTML(context);
  }
  
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    this.html.writeTo(w);
  }

  public void setHtml(String html) throws Exception
  {
    this.html = new FastStringWriter(html.length());
    this.html.write(html);
  }

  public void appendHtml(String html) throws Exception
  {
    this.html.write(html);
  }

  
  public void calculateIncludes(ClientContext context)
  {
    Object obj = getEventHandler(context);
    if(obj instanceof IHtmlGroupEventHandler)
    {
      IHtmlGroupEventHandler handler = (IHtmlGroupEventHandler)obj;
      String[] files = handler.getRequiredIncludeFiles();
      for(int i=0;i<files.length;i++)
      {
        context.addAdditionalIncludes(null, files[i]);
      }
    }
  }

  public void setWiki(String wikitext) throws Exception
  {
    this.html = new FastStringWriter(2048);
    this.html.write(wiki2html(wikitext));
  }

  
  public void appendWiki(String wikitext) throws Exception
  {
    this.html.write(wiki2html(wikitext));
  }

  public String wiki2html( String wiki) throws Exception
  {
    ClientContext context = (ClientContext)Context.getCurrent();
    // Examples codes for bliki wiki parser
    // http://www.matheclipse.org/en/Java_Wikipedia_API#Wikipedia_text_to_HTML_Example
    JacobWikiModel model = new JacobWikiModel("image?dbImage="+this.definition.getName()+"&browser="+context.clientBrowser+"&dbImageId=${image}", "FireEventData('"+getId()+"','click','${title}')");
    String link = model.render(wiki);
    return  StringUtil.replace(link,"FireEventData(","javascript:FireEventData(");
  }
  
  public String getValue(int id)
  {
    return (String)id2value.get(new Integer(id));
  }

  /**
   * The framework call this method if this event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    ((ClientContext)context).setGroup(this);

    // The event is not for this object
    //
    if(guid!=this.getId())
      return super.processEvent(context,guid,event,value);

    Object obj = getEventHandler(context);
    if(obj!=null)
    {
      if(obj instanceof IHtmlGroupEventHandler)
        ((IHtmlGroupEventHandler)obj).onClick(context,this, value);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IHtmlGroupEventHandler.class.getName()+"]");
    }
    
    return true;
  }
  
  
  public boolean processParameter(int guid, String value) throws IOException, NoSuchFieldException
  {
    id2value.put(new Integer(guid),value);
    return true;
  }


  public byte[] getImageData(IClientContext context, String imageId) throws Exception
  {
    Object obj = getEventHandler(context);
    if(obj!=null)
    {
      if(obj instanceof IHtmlGroupEventHandler)
        return ((IHtmlGroupEventHandler)obj).getImageData(context,this, imageId);
      else
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IHtmlGroupEventHandler.class.getName()+"]");
    }
    return null;
  }
}










