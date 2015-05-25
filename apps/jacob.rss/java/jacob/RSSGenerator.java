/*
 * Created on 21.04.2005
 *
 */
package jacob;

import jacob.rss.Channel;
import jacob.rss.Item;
import jacob.rss.Rss;

import java.io.File;
import java.util.Date;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.io.FileUtils;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import de.tif.jacob.util.FastStringWriter;


/**
 *
 */
public class RSSGenerator
{
  public static void main(String[] args)
  {
    Rss rss = new Rss();
    rss.setChannel(new Channel());
    rss.getChannel().setCopyright("FreeGroup");
    rss.getChannel().setTitle("Dies ist der Titel des Channel");
    rss.getChannel().setDescription("Test RSS für den jACOB");
    rss.getChannel().setDocs("Dies ist das Feld 'docs'");
    rss.getChannel().setGenerator("jACOB");
    rss.getChannel().setLanguage("de_DE");
    rss.getChannel().setLastBuildDate(new Date());
    rss.getChannel().setLink("http://www.freegroup.de");
    rss.getChannel().setManagingEditor("Andreas Herz");
    Item item = new Item();
    rss.getChannel().addItem(item);
    item.setAuthor("Andreas Herz");
    item.setDescription("Description");
    item.setGuid("my guid");
    item.setLink("my.link.de");
    item.setTitle("Item title");
    try
    {
      Document doc = XMLUtils.newDocument();
      Marshaller.marshal(rss, doc);
      
      FastStringWriter writer = new FastStringWriter(1024*100);
      org.apache.xml.serialize.OutputFormat outFormat = new org.apache.xml.serialize.OutputFormat();
      outFormat.setIndenting(true);
      outFormat.setIndent(2);
      outFormat.setLineWidth(200);
      outFormat.setEncoding("ISO-8859-1");
      org.apache.xml.serialize.XMLSerializer xmlser = new org.apache.xml.serialize.XMLSerializer(writer, outFormat);

      xmlser.serialize(doc); //replace your_document with reference to document you want to serialize

      FileUtils.writeStringToFile(new File("test.xml"), new String(writer.toCharArray()),"ISO-8859-1");

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
