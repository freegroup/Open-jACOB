/*
 * Created on Mar 24, 2004
 *
 */
package jacob.security;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.tif.jacob.security.HttpAuthentificator;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.IUserFactory;

/**
 * @author Andreas Herz
 *
 */
public class UserFactory extends IUserFactory implements HttpAuthentificator
{
  public IUser get(HttpServletRequest request, HttpServletResponse response) throws GeneralSecurityException
  {
//    if(true)
//      return new User("caretaker");
    try
    {
      Cookie[] cookies=request.getCookies();
      if (cookies != null)
      {
      	for (int i = 0; i < cookies.length; i++)
      	{
      		if(cookies[i].getName().equals("LtpaToken"))
      		{
      			URL url = new URL("http://portal.daimlerchrysler.com/ssoservlet/SSOServlet");
      			String cookie = cookies[i].getName() + "=" + cookies[i].getValue();
      			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
      			
      			huc.setRequestProperty("GET", "http://portal.daimlerchrysler.com/ssoservlet/SSOServlet HTTP/1.1");
      			huc.setRequestProperty("Accept", "*/*");
      			huc.setRequestProperty("Accept-Language", "de");
      			huc.setRequestProperty("Accept-Encoding", "gzip, deflate");
      			huc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; MSIE 5.0; dcstd001; YComp 5.0.0.0; .NET CLR 1.1.4322)");
      			huc.setRequestProperty("Host", "portal.daimlerchrysler.com");
      			huc.setRequestProperty("Proxy-Connection", "Keep-Alive");
      			huc.setRequestProperty("Pragma", "no-cache");
      			huc.setRequestProperty("Cookie", cookie);
      			
      			String ssoUser = huc.getHeaderField("Userid");
      			if(ssoUser!=null)
      				return new User(getApplicationDefinition(), ssoUser);
      		}
      	}
      }
    }
    catch (MalformedURLException e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
    catch (IOException e)
    {
      throw new GeneralSecurityException(e.getMessage());
    }
    return null;
  }
  
	/* 
	 * @see de.tif.jacob.security.IUserFactory#findUser(java.lang.String)
	 * @author Andreas Herz
	 */
	public IUser findUser(String id) throws GeneralSecurityException
	{
		return new User(getApplicationDefinition(), id);
	}

	
	public IUser getValid(String loginId, String passwd) throws GeneralSecurityException
	{
	  throw new GeneralSecurityException("Benutzer/Passwort Anmeldung für Applikation ["+this.getApplicationDefinition().getName()+"] wird nicht unterstützt.");
	  //return findUser(loginId);
	}

	/* 
	 * @see de.tif.jacob.security.IUserFactory#findByFullName(java.lang.String)
	 */
	public List findByFullName(String fullNamePart) throws Exception
	{
	  // FREEGROUP: Suchfunktion für benutzer implementieren
	  throw new Exception("function not implemented....TODO");
	}
}