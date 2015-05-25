/*
 * Created on 15.12.2006
 *
 */
package de.tif.jacob.blooper.model;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import de.tif.jacob.blooper.BlooperPlugin;

public class BlooperServer
{
  
  public static boolean isValid(SOAPEndPoint endpoint) throws Exception
  {
    Call call = createCall(endpoint, "BugService","test");
    
    // ...and a String type as return value
    //
    call.setReturnType(Constants.XSD_BOOLEAN);
    return ((Boolean)call.invoke(new Object[0])).booleanValue();
  }
  
  protected static Call createCall(SOAPEndPoint endpoint, String serviceEndpoint, String operation) throws CoreException, ServiceException
  {
    String serverUrl = endpoint.getServerUrl();
    String user      = endpoint.getUsername();
    String password  = endpoint.getPassword();
    
    Service service = new org.apache.axis.client.Service();
    Call call = (Call) service.createCall();

    call.setTargetEndpointAddress(serverUrl+serviceEndpoint);
    call.setUsername(user);
    call.setPassword(password);
    call.setOperationName(new QName(serverUrl+serviceEndpoint,operation));

    
    return call;
  }

  protected static SOAPEndPoint getSOAPEndPoint(IProject project) throws Exception
  {
    String serverUrl = project.getPersistentProperty(BlooperPlugin.PROPERTY_URL);
    String user      = project.getPersistentProperty(BlooperPlugin.PROPERTY_USER);
    String password  = project.getPersistentProperty(BlooperPlugin.PROPERTY_PASSWORD);
    String connected = project.getPersistentProperty(BlooperPlugin.PROPERTY_CONNECTED);
    
    if(!"true".equals(connected))
      throw new Exception("Project ["+project.getName()+"] is not connected to a bug tracker");
    
    return new SOAPEndPoint(serverUrl, user, password);
  }
  
  public static void main(String[] args) throws Exception
  {
    System.out.println(isValid(new SOAPEndPoint("http://localhost:8080/blooper/services/blooper/","admin","")));
  }
}
