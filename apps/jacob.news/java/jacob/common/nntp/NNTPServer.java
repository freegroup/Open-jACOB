package jacob.common.nntp;

import jacob.config.Config;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.NumberUtils;
import de.tif.jacob.thread.ApplicationThreadContext;
import de.tif.jacob.thread.ManagedThread;

public class NNTPServer extends ManagedThread
{
  static List connections = new ArrayList();
  ServerSocket socket = null;
  private static NNTPServer server;
  
  public static void removeConnection(NNTPConnection con)
  {
    synchronized(connections)
    {
      connections.remove(con);
    }
  }
  
  public static void startup()
  {
    server = new NNTPServer();
    server.start();
  }
  
  public static void shutdown()
  {
    synchronized(connections)
    {
	    try
	    {
	      // Server socket schliessen, damit keine neuen Verbindungen aufgebaut werden. 
	      server.socket.close();
	      // alle verbundenen clients sofort beenden
	      Iterator iter = connections.iterator();
	      while(iter.hasNext())
	      {
	        NNTPConnection connection = (NNTPConnection)iter.next();
	        try
	        {
	          connection.in.close();
	        }
	        catch (IOException e)
	        {
	          // ignore
	        }
	      }
	      System.out.println("NNTPServer is down...........");
	    }
	    catch (Exception e)
	    {
	      // ignore
	    }
    }
  }
  
  public void run(ApplicationThreadContext context)
  {
    try
    {
      int portNumber = NumberUtils.stringToInt(new Config().getProperty("news.port"),119);
      int portNumberFallback = NumberUtils.stringToInt(new Config().getProperty("news.port.fallback"),1190);
      System.out.println("Try to starting NNTP Server on port:"+portNumber);
      try
      {
        socket = new ServerSocket(portNumber);
      }
      catch (Throwable e2)
      {
        System.out.println("....try to starting NNTP Server on port:"+portNumberFallback);
        socket = new ServerSocket(portNumberFallback);
      }
      while (true)
      {
        Socket inbound = socket.accept();
        try
        {
          NNTPConnection thread = new NNTPConnection(inbound);
          thread.setDaemon(true);
          thread.start();
          synchronized(connections)
          {
            connections.add(thread);
          }
        }
        catch (Exception e1)
        {
          // ignore
        }
      }
    }
    catch (IOException e)
    {
      System.out.println("Error connecting to socket: " + e);
    }
  }
}
