/*
 * Created on 18.12.2006
 *
 */
package de.tif.jacob.blooper.model;

import org.eclipse.core.resources.IProject;
import de.tif.jacob.blooper.BlooperPlugin;

public class SOAPEndPoint
{
  String username;
  String password;
  String serverUrl;
  
  public SOAPEndPoint(String serverUrl, String username, String password)
  {
    this.username = username;
    this.password = password;
    this.serverUrl = serverUrl;
  }
  
  public SOAPEndPoint(IProject project) throws Exception
  {
    this.serverUrl = project.getPersistentProperty(BlooperPlugin.PROPERTY_URL);
    this.username  = project.getPersistentProperty(BlooperPlugin.PROPERTY_USER);
    this.password  = project.getPersistentProperty(BlooperPlugin.PROPERTY_PASSWORD);
  }

  public String getPassword()
  {
    return password;
  }

  public String getServerUrl()
  {
    return serverUrl;
  }

  public String getUsername()
  {
    return username;
  }

}
