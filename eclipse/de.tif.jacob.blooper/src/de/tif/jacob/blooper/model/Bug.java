/*
 * Created on 15.12.2006
 *
 */
package de.tif.jacob.blooper.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import de.tif.jacob.blooper.BlooperPlugin;

public class Bug
{
  public static final Object CLOSED = "closed";
  
  String pkey;
  String application;
  String description;
  String grade;
  String reproduceabitlity;
  String state;
  String title;
  String ownerName;
  String creatorName;

  final SOAPEndPoint endpoint;
  
  protected Bug(SOAPEndPoint endpoint,String pkey, String application, String description, String grade, String reproduceabitlity, String state, String title, String owner, String creator)
  {
    super();
    this.pkey = pkey;
    this.application = application;
    this.description = description;
    this.grade = grade;
    this.reproduceabitlity = reproduceabitlity;
    this.state = state;
    this.title = title;
    this.ownerName = owner;
    this.creatorName = creator;
    
    this.endpoint = endpoint;
  }

  public static Bug findByPkey(IProject project, String pkey) throws Exception
  {
    return findByPkey(BlooperServer.getSOAPEndPoint(project),pkey);
  }
  
  public static Bug findByPkey(SOAPEndPoint endpoint, String pkey) throws Exception
  {
    Call call = BlooperServer.createCall(endpoint, "BugService","findByPkey");

    call.setReturnType(Constants.XSD_STRING, String[].class);
    call.addParameter("appName", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);

    String[] result = (String[])call.invoke(new String[]{pkey});
    if(result ==null)
      return null; // not found
    pkey = result[0];
    String application = result[1];
    String description = result[2];
    String grade = result[3];
    String reproduceabitlity = result[4];
    String state = result[5];
    String title = result[6];
    String owner = result[7];
    String creator = result[8];
    return new Bug(endpoint, pkey,application,description,grade,reproduceabitlity,state,title, owner, creator);
  }

  public static Bug create(IProject project, String appName) throws Exception
  {
    return create(BlooperServer.getSOAPEndPoint(project),appName);
  }
  
  public static Bug create(SOAPEndPoint endpoint, String appName) throws Exception
  {
    Call call = BlooperServer.createCall(endpoint, "BugService","create");

    call.setReturnType(Constants.XSD_STRING, String[].class);
    call.addParameter("appName", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);

    String[] result = (String[])call.invoke(new String[]{appName});
    String pkey = result[0];
    return findByPkey(endpoint, pkey);
  }

  
  public static List<Bug> findByApplicationName(IProject project, String appName) throws Exception
  {
    return findByApplicationName(BlooperServer.getSOAPEndPoint(project), appName);
  }
  
  public static List<Bug> findByApplicationName(SOAPEndPoint endpoint, String appName) throws Exception
  {
    Call call = BlooperServer.createCall(endpoint, "BugService","findByApplicationName");

    call.setReturnType(Constants.XSD_STRING, String[].class);
    call.addParameter("appName", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);

    String[] result = (String[])call.invoke(new String[]{appName});
    
    List<Bug> bugs = new ArrayList<Bug>();
    if(result!=null)
    {
      for (int i=0; i < result.length; i+=13) 
      {
        String pkey = result[i];
        String application = result[i+1];
        String description = result[i+2];
        String grade = result[i+3];
        String reproduceabitlity = result[i+4];
        String state = result[i+5];
        String title = result[i+6];
        String owner = result[i+7];
        String creator = result[i+8];
        
        Bug bug = new Bug(endpoint, pkey,application,description,grade,reproduceabitlity,state,title, owner, creator);
        bugs.add(bug);
      }
    }
    
    return bugs;
  }

  public String getApplication()
  {
    return application;
  }

  public void setApplication(String application)
  {
    this.application = application;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getGrade()
  {
    return grade;
  }

  public void setGrade(String grade)
  {
    this.grade = grade;
  }

  public String getPkey()
  {
    return pkey;
  }

  public String getReproduceabitlity()
  {
    return reproduceabitlity;
  }

  public void setReproduceabitlity(String reproduceabitlity)
  {
    this.reproduceabitlity = reproduceabitlity;
  }

  public String getState()
  {
    return state;
  }

  public String[] getValidStates()
  {
    try
    {
      Call call = BlooperServer.createCall(endpoint, "BugService","getValidStates");

      call.setReturnType(Constants.XSD_STRING, String[].class);
      call.addParameter("state", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);

      return (String[])call.invoke(new String[]{this.state});
    }
    catch (Exception e)
    {
      BlooperPlugin.showException(e);
    }
    return new String[0];
  }
  
  public String[] getValidGrades()
  {
    try
    {
      Call call = BlooperServer.createCall(endpoint, "BugService","getValidGrades");

      call.setReturnType(Constants.XSD_STRING, String[].class);
      call.addParameter("grade", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);

      return (String[])call.invoke(new String[]{this.grade});
    }
    catch (Exception e)
    {
      BlooperPlugin.showException(e);
    }
    return new String[0];
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getCreatorName()
  {
    return creatorName;
  }


  public String getOwnerName()
  {
    return ownerName;
  }

  public void commit()
  {
    try
    {
      Call call = BlooperServer.createCall(endpoint, "BugService","commit");

      call.setReturnType(Constants.XSD_BOOLEAN, Boolean.class);
      
      call.addParameter("pkey", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
      call.addParameter("state", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
      call.addParameter("grade", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
      call.addParameter("subject", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
      call.addParameter("description", Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);

      call.invoke(new String[]{pkey,state,grade,title,description});
    }
    catch (Exception e)
    {
      MessageDialog.openError(null,"Error",e.getMessage());
    }
  }

  public SOAPEndPoint getEndpoint()
  {
    return endpoint;
  }
}
