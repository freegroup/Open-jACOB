package de.tif.jacob.urlredirect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.util.clazz.ClassUtil;

abstract class AbstractRule
{
  protected final String match;
  protected final Pattern matchComplied;
  protected String target;
  private boolean encodeUrl = false;
  private final String applicationIdentifier;
  
  public abstract void process(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException, IOException;
 

  public AbstractRule(IApplicationDefinition appDef,String urlPattern, String target)
  {
     this.applicationIdentifier = appDef.getName()+":"+appDef.getVersion().toString();  
     this.match = urlPattern;
     // Pattern is thread-safe
     this.matchComplied = Pattern.compile(urlPattern);
     this.target = target;
  }
  
  public boolean isFrom(IApplicationDefinition appDef)
  {
    return applicationIdentifier.equals(appDef.getName()+":"+appDef.getVersion().toString());
  }
  
  /**
   * Converts a possibly relative URL to absolute URL. If the supplied URL
   * doesn't need any conversion, it remains unchanged.
   * 
   */
  protected String getAbsoluteURL(HttpServletRequest httpRequest, String url)
  {
    if (url == null)
      return null;
    
    if (url.indexOf("://") != -1)
      return url;
    
    String scheme = httpRequest.getScheme();
    String serverName = httpRequest.getServerName();
    int port = httpRequest.getServerPort();
    boolean slashLeads = url.startsWith("/");
    String absoluteURL = scheme + "://" + serverName;
    
    if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443))
      absoluteURL += ":" + port;
    
    if (!slashLeads)
      absoluteURL += "/";
    
    absoluteURL += url;
    return absoluteURL;
  }


  protected String getFinalURL(HttpServletRequest request, HttpServletResponse response, String targetURL)
  {
    String finalURL = getAbsoluteURL(request, targetURL);
    if (this.encodeUrl == true)
      finalURL = response.encodeRedirectURL(finalURL);
    return finalURL;
  }

  /**
   * Figures out whether the specified address resides within the specified
   * address range.
   * 
   * @param range
   *          : A range in notation address/netmask, for example
   *          127.0.0.1/255.0.0.0
   * @param address
   *          : An address to be tested.
   * 
   * @return true if the address is within the range, false otherwise.
   */
  protected boolean isAddressInRange(String range, String address) throws ServletException
  {
    String network;
    String mask;
    int slashPos = range.indexOf('/');
    if (slashPos == -1)
    {
      network = range;
      mask = "255.255.255.255";
    }
    else
    {
      network = range.substring(0, slashPos);
      mask = range.substring(slashPos + 1);
    }
    try
    {
      byte[] netBytes = InetAddress.getByName(network).getAddress();
      byte[] maskBytes = InetAddress.getByName(mask).getAddress();
      byte[] addrBytes = InetAddress.getByName(address).getAddress();
      for (int i = 0; i < netBytes.length; i++)
      {
        if ((netBytes[i] & maskBytes[i]) != (addrBytes[i] & maskBytes[i]))
          return false;
      }
    }
    catch (UnknownHostException e)
    {
      // Should never happen, because we work with raw IP addresses, not
      // with host names.
      throw new ServletException(e.getMessage());
    }
    return true;
  }

  public String getURLPattern()
  {
    return match;
  }

  public boolean matches(String urlToCheck)
  {
    return this.matchComplied.matcher(urlToCheck).matches();
  }


  public String toString()
  {
    return ClassUtil.getShortClassName(this.getClass())+" [app="+applicationIdentifier+", match=" + match + ", target=" + target + "]";
  }
  
}
