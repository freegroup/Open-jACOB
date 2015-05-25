/*
 * Created on 18.12.2006
 *
 */
package de.tif.jacob.blooper.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.eclipse.core.resources.IProject;

public class Application
{
  String pkey;
  String name;
  
  public Application(String pkey, String name)
  {
    this.pkey = pkey;
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getPkey()
  {
    return pkey;
  }
  
  public static boolean create(IProject project, String name) throws Exception
  {
    return create(BlooperServer.getSOAPEndPoint(project),name);
  }
  
  public static boolean create(SOAPEndPoint endpoint, String name) throws Exception
  {
    Call call = BlooperServer.createCall(endpoint, "ApplicationService","create");

//    call.setReturnType(Constants.XSD_STRING, String[].class);
  
    call.invoke(new String[]{name});
    return true;
  }
  
  public static List<Application> getAll(IProject project) throws Exception
  {
    return getAll(BlooperServer.getSOAPEndPoint(project));
  }
  
  public static List<Application> getAll(SOAPEndPoint endpoint) throws Exception
  {
    Call call = BlooperServer.createCall(endpoint, "ApplicationService","getAll");

    call.setReturnType(Constants.XSD_STRING, String[].class);
  
    String[] result = (String[])call.invoke(new Object[0]);
    List<Application> apps = new ArrayList<Application>();
    if(result!=null)
    {
      for (int i=0; i < result.length; i+=2) 
      {
        Application app = new Application(result[i], result[i+1]);
        apps.add(app);
      }
    }
    
    return apps;
  }
  
  public static void main(String[] args) throws Exception
  {
    System.out.println(getAll(new SOAPEndPoint("http://localhost:8080/blooper/services/blooper/","admin","")));
    System.out.println(create(new SOAPEndPoint("http://localhost:8080/blooper/services/blooper/","admin",""),"any"));
  }
}
