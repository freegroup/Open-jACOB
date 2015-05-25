package jacob.common;

import jacob.model.Applicationemployee;
import jacob.model.Employee;
import jacob.model.User_application;
import jacob.security.User;

import java.security.GeneralSecurityException;
import java.util.TreeMap;
import java.util.TreeSet;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.security.IUser;

public class Access
{

  private static void addPublicApplications(IDataAccessor accessor, User user, TreeMap myApps) throws Exception
  {
    IDataTable applications = accessor.getTable(User_application.NAME);
    applications.getAccessor().clear();
    applications.qbeSetKeyValue(User_application.isglobal,"1");
    applications.search();
    for (int i = 0; i < applications.recordCount(); i++)
    {
      myApps.put(applications.getRecord(i).getSaveStringValue(User_application.displayname),applications.getRecord(i).getSaveStringValue(User_application.japplication_key));
      
    }
    
  }
  private static void addConstraintApplications(IDataAccessor accessor, User user, TreeMap myApps) throws Exception
  {
    IDataTable applications = accessor.getTable(User_application.NAME);
    applications.getAccessor().clear();
    applications.qbeSetValue(User_application.constraintfield1,"!NULL");  
    //TODO: folgender Code geht erst in 2.6
    //applications.qbeSetValue(User_application.constraintfield1,"|!NULL");
    //applications.qbeSetValue(User_application.constraintfield2,"|!NULL");
    applications.search();
    
    // schauen ob User  zu dem Constraint passt
    IDataTable employee = accessor.getTable(Employee.NAME);
    for (int i = 0; i < applications.recordCount(); i++)
    {
      IDataTableRecord appRec = applications.getRecord(i);
      employee.clear();
      employee.qbeClear();
      employee.qbeSetKeyValue(Employee.pkey,user.getKey());
      if (appRec.getValue(User_application.constraintfield1)!=null)
      {
        employee.qbeSetValue(appRec.getSaveStringValue(User_application.constraintfield1),appRec.getSaveStringValue(User_application.constraintvalue1));
      }
      if (appRec.getValue(User_application.constraintfield2)!=null)
      {
        employee.qbeSetValue(appRec.getSaveStringValue(User_application.constraintfield2),appRec.getSaveStringValue(User_application.constraintvalue2));
      }
      employee.search();
      if (employee.recordCount()==1)
      {
        myApps.put(appRec.getSaveStringValue(User_application.displayname),appRec.getSaveStringValue(User_application.japplication_key));
      }
      
    }    
  }

  private static void addAssignedApplications(IDataAccessor accessor, User user, TreeMap myApps) throws Exception
  {
    IDataTable applications = accessor.getTable(User_application.NAME);
    IDataTable applicationemployee = accessor.getTable(Applicationemployee.NAME);
    applications.getAccessor().clear();
    applicationemployee.qbeSetKeyValue(Applicationemployee.employee_key,user.getKey());
    applications.search("application");
    for (int i = 0; i < applications.recordCount(); i++)
    {
      myApps.put(applications.getRecord(i).getSaveStringValue(User_application.displayname),applications.getRecord(i).getSaveStringValue(User_application.japplication_key));
    }
  }

  public static TreeMap getAccess(IDataAccessor accessor,User user) throws GeneralSecurityException
  {
    TreeMap myApps = new TreeMap();

    if (user.availability && user.getLoginId()!=null) // Hinweis für Zugriff auf Caretaker
    {
      myApps.put("JACOB-TTS","caretaker");
     
//      // Schichtbericht
//      if ("FWT/EV".equals(user.department))
//        myApps.add("Schichtbericht");
//      // Netzverfuegbarkeit
//      if (user.hasRole("net_role"))
//        myApps.add("Netzverfuegbarkeit");
    }
    
    try
    {
      addConstraintApplications(accessor, user, myApps);
      
      addPublicApplications(accessor, user, myApps);

      addAssignedApplications(accessor, user, myApps);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if (myApps.size() == 0 && !user.hasRole("Administrator"))
      throw new GeneralSecurityException();

    return myApps;

  }

  public static void login(String AppName)
  {
    IClientContext context = (IClientContext) Context.getCurrent();
    IUser user = context.getUser();
    String login = user.getLoginId();
    String pwd = (String) user.getProperty("passwd");
    String application = "http://" + context.getSession().getHost() + "/" + Bootstrap.getApplicationName() + "/login.jsp?app=" + AppName + "&user=" + login
        + "&passwd=" + pwd;

    ((ClientContext) context).addOnLoadJavascript("top.location.href='" + application + "';");
  }

}
