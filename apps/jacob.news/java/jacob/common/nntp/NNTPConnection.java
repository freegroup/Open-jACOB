package jacob.common.nntp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.IOUtils;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.thread.ApplicationThreadContext;
import de.tif.jacob.thread.ManagedThread;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.stream.StringBufferOutputStream;


public final class NNTPConnection extends ManagedThread
{
  
  final static int SOCKET_TIMEOUT = 1000*60*5;
  
  String selectedGroup   = null;
  String selectedArticle = null;
  String command;

  /**
   * The character array that indicates termination of an NNTP message
   */
  private final static char[] NNTPTerminator = { '\r', '\n', '.', '\r', '\n' };

  /**
   * The remote host name obtained by lookup on the socket.
   */
  private String remoteHost;

  /**
   * The remote IP address of the socket.
   */
  private String remoteIP;

  /**
   * The TCP/IP socket over which the POP3 interaction
   * is occurring
   */
  private Socket socket;

  /**
   * The incoming stream of bytes coming from the socket.
   */
  protected InputStream in;

  /**
   * The reader associated with incoming characters.
   */
  private BufferedReader reader;

  /**
   * The socket's output stream
   */
  private OutputStream outs;
  /**
   * The writer to which outgoing messages are written.
   */
  private PrintWriter writer;

  protected NNTPConnection(Socket inbound) throws Exception
  {
    socket = inbound;
    remoteIP   = socket.getInetAddress().getHostAddress();
    remoteHost = socket.getInetAddress().getHostName();

    in     = new BufferedInputStream(socket.getInputStream(), 1024);
    reader = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"), 1024);
//    reader = new BufferedReader(new InputStreamReader(in, "ASCII"), 512);
    
    outs   = new BufferedOutputStream(socket.getOutputStream(), 1024);
    writer = new InternetPrintWriter(outs, true);
  }

  
  public void run(ApplicationThreadContext context)
  {
    boolean done = false;
    String str = null;
    writeLoggedFlushedResponse("200 jACOB-Server NNTP Service Ready, posting permitted");

    try
    {
      socket.setSoTimeout(SOCKET_TIMEOUT);
	    while (!done)
	    {
	      str = reader.readLine();
	      if(str==null)
	      {
	        // avoiud server overrun
	        try{ sleep(1000);}catch (InterruptedException e1){}
	        continue;
	      }
        /** ************************************************************************* */
        /* Process each of the valid NNTP server command strings in turn. */
        /** ************************************************************************* */
        command = str.toUpperCase().trim();
        System.out.println("["+command+"]");
	      if (str == null || str.trim().toUpperCase().equals("QUIT"))
	      {
	        commandQUIT();
	        done = true;
	      }
        else if (command.startsWith("ARTICLE "))
        {
          selectedArticle = str.substring("ARTICLE ".length());
          commandARTICLE(selectedGroup, selectedArticle);
        }
        else if (command.startsWith("AUTHINFO "))
        {
          commandAUTHINFO();
        }
        else if (command.startsWith("HEAD "))
        {
          selectedArticle = str.substring("HEAD ".length());
          commandHEAD(selectedArticle);
        }
        else if (command.startsWith("HELP"))
        {
          commandHELP();
        }
        else if (command.startsWith("GROUP "))
        {
          selectedGroup = str.substring("GROUP ".length());
          commandGROUP(selectedGroup);
        }
        else if (command.equals("LIST"))
        {
          commandLIST();
        }
        else if (command.startsWith("LIST NEWSGROUPS"))
        {
          commandLIST();
          //get_desc(str.substring("LIST NEWSGROUPS ".length()), remoteHost);
        }
        else if (command.startsWith("MODE READER"))
        {
          commandMODEREADER();
        }
        else if (command.startsWith("NEWGROUPS "))
        {
         commandNEWSGROUPS();
        }
        else if (command.startsWith("POST"))
        {
          commandPOST();
        }
        else if (command.startsWith("XOVER "))
        {
          String article = str.substring("XOVER ".length());
          commandXOVER(selectedGroup, article);
        }
        else if (command.length() < 3)
        {
          writeLoggedFlushedResponse(command);
        }
        else if (!command.startsWith("ARTICLE ") && !command.startsWith("AUTHINFO ") && !command.startsWith("GROUP ") && !command.startsWith("HEAD ") && !command.startsWith("HELP") && !command.equals("LIST") && !command.startsWith("LIST NEWSGROUPS ")
            && !command.startsWith("MODE READER") && !command.startsWith("NEWGROUPS ") && !command.startsWith("POST") && !command.startsWith("SET HOSTNAME") && !command.startsWith("XOVER "))
        {
          writeLoggedFlushedResponse("500 command not recognized");
        }
	      str = null;
	    }
    }
    catch (IOException e)
    {
    }
    finally
    {
      try{in.close();}catch(Exception e2){/*ignore*/};
    }
    NNTPServer.removeConnection(this);
    System.out.println("close connection");
  }

  private void commandMODEREADER()
  {
    writeLoggedFlushedResponse("200 Hello, you can post");
  }
  
  private void commandAUTHINFO()
  {
    writeLoggedFlushedResponse("281 Authentication accepted");
  }
  
  /**
   * 
   */
  private void commandPOST()
  {
    writeLoggedFlushedResponse("340 Ok");
		try
		{
      String data="";
      String line="";
      
      // read the body
      data="";
      while(true)
      {
        try
        {
          line = reader.readLine();
          if(".".equals(line))
            break;
          if(line.startsWith(".."))
            line=line.substring(1);
        }
        catch (IOException e)
        {
          writeLoggedFlushedResponse("441 posting failed");
          break;
        }
        if (line != null)
          data += line + "\n";
      }
      System.out.println(data);
      MimeMessage message = new MimeMessage( Session.getDefaultInstance(new Properties()),new StringBufferInputStream(data));
      createArticle(message);
      writeLoggedFlushedResponse("240 Article posted successfully.");
		}
		catch (Exception e)
		{
		  e.printStackTrace();
      writeLoggedFlushedResponse("441 posting failed");
		}
  }

  /**
   * 
   */
  private void commandHELP()
  {
    writeLoggedFlushedResponse("100 Legal commands\n" + "  article number\n" + "  authinfo user name\n" + "  group newsgroup\n" + "  head number\n" + "  help\n" + "  list\n" + "  mode reader\n" + "  newgroups yymmdd hhmmss\n" + "  post\n" + "  xover number");
    writeLoggedFlushedResponse(".");
  }

  private void commandQUIT()
  {
    writeLoggedFlushedResponse("205 closing connection - goodbye!");
  }

  private void commandNEWSGROUPS()
  {
    writeLoggedFlushedResponse("231 New newsgroups follow.\n.");
  }
  
  private void commandLIST()
  {
    try
    {
      StringBuffer groups= new StringBuffer();
      groups.append("215 list of newsgroups follows" + "\n");
      IDataAccessor accessor = getDataAccessor();
      accessor.qbeClearAll();
      IDataTable group = accessor.getTable("group");
      group.search();
      for(int i=0;i<group.recordCount();i++)
      {
        IDataTableRecord record=group.getRecord(i);
        String groupName = record.getSaveStringValue("name");
        GroupInfo info = getGroupInfo(groupName);
        
        groups.append(groupName);         // name
        groups.append(" "); 
        groups.append(info.lastArticle);  // last
        groups.append(" "); 
        groups.append(info.firstArticle); // first
        groups.append(" Y");              // permission
        
        groups.append('\r');         
        groups.append('\n');
      }
      groups.append(".");
      writeLoggedFlushedResponse( groups.toString());
    }
    catch (Exception e)
    {
      writeLoggedFlushedResponse("401"); // connection error
    }
  }
  
  private void commandHEAD(String articleNum)
  {
    try
    {
      IDataAccessor accessor = getDataAccessor();
      accessor.qbeClearAll();
      IDataTable article = accessor.getTable("article");
      article.qbeSetValue("pkey",articleNum);
      article.search();
      if(article.recordCount()!=1)
      {
        writeLoggedFlushedResponse("423 no such article in this group");
        return;
      }
      IDataTableRecord currentRecord = article.getSelectedRecord();
      IDataTableRecord group         = currentRecord.getLinkedRecord("group");
      StringBuffer result= new StringBuffer();
//      result.append("221 ");
//      result.append(articleNum);
//      result.append(" <d33719d23f5a61b338ce5887c1f16be5$1@www.eclipse.org> Article retrieved - header follows\n");
      /*
      result.append("Xref:");
      result.append(currentRecord.getSaveStringValue("reference"));
      result.append("\n");
      result.append("Newsgroups:");
      result.append(group.getSaveStringValue("name"));
      result.append("\n");
      result.append("From:");
      result.append(currentRecord.getSaveStringValue("from"));
      result.append("\n");
      result.append("Date:");
      result.append("Tue, 14 Jun 2005 06:23:06 +0000 (UTC)");
//      result.append(currentRecord.getSaveStringValue("date"));
      result.append("\n");
      result.append("Subject:");
      result.append(currentRecord.getSaveStringValue("subject"));
      result.append("\n");
      result.append("Message-ID:");
      result.append(currentRecord.getSaveStringValue("pkey"));
      result.append("\n");
      result.append("Path:");
      result.append("q!w");
      */
      result.append("221 13 <7g5bhm$8f$2@news.vbrew.com> head\n"+
										"Path: news.vbrew.com!not-for-mail\n"+
										"From: terry@richard.geek.org.au\n"+
										"Newsgroups: jacob.applications\n"+
										"Subject: test message number 2\n"+
										"Date: 27 Apr 1999 21:51:50 GMT\n"+
										"Organization: The Virtual brewery\n"+
										"Lines: 2\n"+
										"Message-ID: <7g5bhm$8f$2@news.vbrew.com>\n"+
										"NNTP-Posting-Host: localhost\n"+
										"X-Server-Date: 27 Apr 1999 21:51:50 GMT\n"+
										"Body: \n"+
										"Xref: localhost jacob.applications:13\n"+
										".\n"
      	);
      
      writeLoggedFlushedResponse( result.toString());
//      writeLoggedFlushedResponse( ".");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      writeLoggedFlushedResponse( "401"); // connection error
    }
  }
  
  /** ************************************************************************* */
  /* The get_group_header() method processes the GROUP command. */
  /** ************************************************************************* */
  private void commandGROUP(String groupName)
  {
    try
    {
      GroupInfo info = getGroupInfo(groupName);
      StringBuffer result = new StringBuffer(500);
      result.append("211 ")
			      .append(Integer.toString(info.count))
			      .append(" ")
			      .append(info.firstArticle)
			      .append(" ")
			      .append(info.lastArticle)
			      .append(" ")
			      .append(groupName)
			      .append(" group selected");
      writeLoggedFlushedResponse(result.toString());
    }
    catch (Exception e)
    {
      System.out.println(e);
      writeLoggedFlushedResponse( "411"); // no such news group
    }
  }

  /** ************************************************************************* 
  Each line of output will be formatted with the article number,
  followed by each of the headers in the overview database or the
  article itself (when the data is not available in the overview
  database) for that article separated by a tab character.  The
  sequence of fields must be in this order: subject, author, date,
  message-id, references, byte count, and line count.  Other optional
  fields may follow line count.  Other optional fields may follow line
  count.  These fields are specified by examining the response to the
  LIST OVERVIEW.FMT command.  Where no data exists, a null field must
  be provided (i.e. the output will have two tab characters adjacent to
  each other).  Servers should not output fields for articles that have
  been removed since the XOVER database was created. 
   ************************************************************************* */
  private void commandXOVER(String groupName, String articleNum)
  {
    try
    {
      articleNum =  StringUtil.replace(articleNum,"-","..");
      IDataAccessor accessor = getDataAccessor();
      accessor.qbeClearAll();
      IDataTable group   = accessor.getTable("group");
      IDataTable article = accessor.getTable("article");
      
      article.qbeSetValue("pkey",articleNum);
      group.qbeSetValue("name",groupName);
      article.search("groupRelationset");
      if(article.recordCount()<1)
      {
        writeLoggedFlushedResponse("423 no such article in this group");
        return;
      }
      writeLoggedFlushedResponse("224 "+articleNum+" fields follows");
      for(int i=0;i<article.recordCount();i++)
      {
        IDataTableRecord current = article.getRecord(i);
        StringBuffer result= new StringBuffer();
        result.append(current.getSaveStringValue("pkey"));   // id
        result.append("\t");
        result.append(current.getSaveStringValue("subject")); // subject
        result.append("\t");
        result.append(current.getSaveStringValue("from"));   // sender
        result.append("\t");
        result.append(current.getSaveStringValue("date"));   // date
        result.append("\t");
        result.append(current.getSaveStringValue("message_id"));  // message-ID
        result.append("\t");
        result.append(current.getSaveStringValue("reference")); // references
        result.append("\t"); 
        result.append(current.getSaveStringValue("body").getBytes().length); // byte count
        result.append("\t");
        BufferedReader rdr = new BufferedReader(new StringReader(current.getSaveStringValue("body")));
        int lines = 0;
        while (rdr.readLine() != null) {
            lines++;
        }
        result.append(lines); // line count
        writeLoggedFlushedResponse( result.toString());
      }
      writeLoggedFlushedResponse( ".");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      writeLoggedFlushedResponse( "412"); // connection error
    }
  }


  /**
   * 
   * @param group
   * @param articleNum
   */
  private void commandARTICLE(String group, String articleNum)
  {
    try
    {
      articleNum =  StringUtil.replace(articleNum,"-","..");
      IDataAccessor accessor = getDataAccessor();
      accessor.qbeClearAll();
      IDataTable articles    = accessor.getTable("article");
      IDataTable attachments = accessor.getTable("attachment");
      articles.qbeSetValue("pkey",articleNum);
      articles.search();
      if(articles.recordCount()!=1)
      {
        writeLoggedFlushedResponse("423 no such article in this group");
        return;
      }
      IDataTableRecord current = articles.getSelectedRecord();
      accessor.propagateRecord(current,Filldirection.BOTH);
      writeLoggedFlushedResponse("220 "+articleNum+" "+current.getSaveStringValue("message_id")+" article follows");
      
      StringBuffer result= new StringBuffer();
      BufferedReader rdr = new BufferedReader(new StringReader(current.getSaveStringValue("body")));
      int lines = 0;
      while (rdr.readLine() != null) {lines++; }

      Session session = Session.getDefaultInstance(new Properties());

      javax.mail.Message msg = new MimeMessage(session);
      
      msg.setFrom(new InternetAddress(current.getSaveStringValue("from")));
      msg.setSubject(current.getSaveStringValue("subject"));
      msg.setHeader("Date",current.getSaveStringValue("date"));
      msg.setHeader("Newsgroups",group);
      msg.setHeader("Organization","Tarragon");
      msg.setHeader("Line",""+lines);
      msg.setHeader("Message-ID",current.getSaveStringValue("message_id"));
      msg.setHeader("Xref",current.getSaveStringValue("xref"));
      msg.setHeader("References",current.getSaveStringValue("reference"));

      Multipart mp = new MimeMultipart();
      
      // create and fill the first message part
      // the message itself
      //
      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setContent(current.getSaveStringValue("body"), "text/plain");
      mp.addBodyPart(mbp1);
      
      // create the second message part
      // attach the file to the message
      //
      for (int i=0;i<attachments.recordCount();i++)
      {
      	IDataTableRecord attachment = attachments.getRecord(i);
      	DataDocumentValue doc = attachment.getDocumentValue("file");
      
      	MimeBodyPart mbp2 = new MimeBodyPart();
        String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(doc.getName());
      	ByteArrayDataSource fds = new ByteArrayDataSource(doc.getContent(), mimeType);
      	mbp2.setContent(null, "");
      	mbp2.setDataHandler(new DataHandler(fds));
      	mbp2.setFileName(doc.getName());
      	mp.addBodyPart(mbp2);
      }
      
      // add the Multipart to the message
      //
      msg.setContent(mp);
      StringBuffer sb=new StringBuffer(1024);
      StringBufferOutputStream stream = new StringBufferOutputStream(sb);
      msg.writeTo(stream);
      // dummerweise vergibt javaMail eine eigene ID.....die wird jetzt umgebügelt
      //
      String data = sb.toString();
      data = StringUtil.replace(data,msg.getHeader("Message-ID")[0],current.getSaveStringValue("message_id"));
      writeFlushedResponse(data);
      writeLoggedFlushedResponse( ".");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      writeLoggedFlushedResponse( "412"); // connection error
    }
  }



  /**
   * @param message
   */
  private void createArticle( MimeMessage message) throws Exception
  {
    Address from = message.getFrom()[0];
    String subject  = message.getSubject();
    String date     = message.getSentDate().toGMTString();
    String groupName= message.getHeader("Newsgroups")[0];
    
    String email;
    
    if (from instanceof InternetAddress)
    {
      email = ((InternetAddress) from).getAddress();
    }
    else
    {
      email = from.toString();
    }
    
    IDataAccessor accessor = getDataAccessor();
    accessor.qbeClearAll();
    IDataTable groups      = accessor.getTable("group");
    IDataTable articles    = accessor.getTable("article");
    IDataTable attachments = accessor.getTable("attachment");
    
    groups.qbeSetValue("name",groupName);
    if(groups.search()==1)
    {
	    IDataTransaction trans = accessor.newTransaction();
	    try
	    {
	      IDataTableRecord article = articles.newRecord(trans);
	      Object content = message.getContent();
	      
	      article.setStringValue(trans,"subject",subject);
	      article.setStringValue(trans,"from",email);
	      article.setStringValue(trans,"date",date);
	      article.setLinkedRecord(trans,groups.getSelectedRecord());
	      // set parent articel
	      String reference=null;
	      if(message.getHeader("In-Reply-To")!=null)
	      {
	        reference= message.getHeader("In-Reply-To")[0];
	      }
	      else if(message.getHeader("References")!=null)
	      {
	        try 
	        {
	          reference= message.getHeader("References")[0].split(" ")[1];
	        }
	        catch(Exception exc)
	        {
	        }
	      }
	      
	      if(reference!=null)
	      {
	        reference= StringUtil.replace(reference, "<","");
	        reference= StringUtil.replace(reference, ">","");
	        IDataTable parentArticle = accessor.getTable("parent_article");
	        accessor.qbeClearAll();
	        parentArticle.qbeSetValue("message_id",reference);
	        if(parentArticle.search()==1)
	          article.setLinkedRecord(trans,parentArticle.getSelectedRecord());
	      }
	      if (content instanceof Multipart)
	      {
	        Multipart multipart = (Multipart)content;
	        for(int i=0;i<multipart.getCount();i++)
	        {
	          BodyPart part= multipart.getBodyPart(i);
	          
	          String filename = part.getFileName();
	          byte[] data     = IOUtils.toByteArray(part.getInputStream());
	          System.out.println(part.getContentType());
	          if(part.getContentType().indexOf("text/plain")>=0 && i==0)
	          {
	  		      article.setStringValue(trans,"body",new String(data));
	          }
	          else
	          {
		          IDataTableRecord attachment = attachments.newRecord(trans);
		          attachment.setDocumentValue(trans,"file",DataDocumentValue.create(filename,data));
		          attachment.setLinkedRecord(trans,article);
	          }
	        }
	      }
	      else
	      {
	        Part part = message;
		      article.setStringValue(trans,"body",part.getContent().toString());
	      }
	      trans.commit();
	    }
	    finally
	    {
	      trans.close();
	    }
    }
  }

  

  /**
   * This method logs at a "DEBUG" level the response string that 
   * was sent to the SMTP client.  The method is provided largely
   * as syntactic sugar to neaten up the code base.  It is declared
   * private and final to encourage compiler inlining.
   *
   * @param responseString the response string sent to the client
   */
  private final void logResponseString(String responseString) 
  {
    System.out.println(responseString);
  }

  /**
   * Write and flush a response string.  The response is also logged.
   * Should be used for the last line of a multi-line response or
   * for a single line response.
   *
   * @param responseString the response string sent to the client
   */
  private final void writeLoggedFlushedResponse(String responseString) 
  {
      writer.println(responseString);
      writer.flush();
      logResponseString(responseString);
  }

  private final void writeFlushedResponse(String responseString) 
  {
      writer.println(responseString);
      writer.flush();
  }

  /**
   * Write a response string.  The response is also logged. 
   * Used for multi-line responses.
   *
   * @param responseString the response string sent to the client
   */
  private final void writeLoggedResponse(String responseString) 
  {
      writer.println(responseString);
      logResponseString(responseString);
  }
  
  private GroupInfo getGroupInfo(String groupName) throws Exception
  {
      IDataAccessor accessor = getDataAccessor();
      accessor.qbeClearAll();
      IDataTable group = accessor.getTable("group");
      group.qbeSetValue("name",groupName);
      group.search();
      accessor.propagateRecord(group.getSelectedRecord(),Filldirection.BOTH);
      IDataTable articels = accessor.getTable("article");
      
      String firstId="0";
      String lastId="0";
      int    count=0;
      
      if(articels.recordCount()>0)
      {
        IDataTableRecord firstRecord = articels.getRecord(0);
        IDataTableRecord lastRecord = articels.getRecord(articels.recordCount()-1);
        firstId=firstRecord.getSaveStringValue("pkey");
        lastId=lastRecord.getSaveStringValue("pkey");
        count=articels.recordCount();
      }
      return new GroupInfo(count, firstId, lastId);
  }

  
}
