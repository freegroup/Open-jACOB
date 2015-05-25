/*
 * Created on 22.03.2004
 * by mike
 *
 */
package jacob.common;

import jacob.exception.BusinessException;

import java.util.Date;
import java.util.Vector;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataBrowser;
import de.tif.jacob.core.data.impl.IDataBrowserComparator;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.misc.AdhocBrowserDefinition;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.security.IUser;

/**
 * SORRY FÜR DASS DAZWISCHEN FUNKEN!!!!
 * 
 * @author mike
 * 
 */

public class Call
{
  static public final transient String RCS_ID = "$Id: Call.java,v 1.19 2008/04/29 07:51:42 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.19 $";

  static protected final transient Log logger = AppLogger.getLogger();
  static private final Vector globalAccessRoles = new Vector();

  static
  {
    globalAccessRoles.add("CQ_PM");
    globalAccessRoles.add("CQ_ADMIN");
    globalAccessRoles.add("CQ_AGENT");
    globalAccessRoles.add("CQ_SUPERAK");
    globalAccessRoles.add("CQ_SDADMIN");
  }
  public class SL_Comparator implements IDataBrowserComparator
  {

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.data.impl.IDataBrowserComparator#compare(de.tif.jacob.core.data.IDataBrowserRecord,
     *      de.tif.jacob.core.data.IDataBrowserRecord)
     */
    public int compare(IDataBrowserRecord rec1, IDataBrowserRecord rec2)
    {
      {
        try
        {
          if (rec1.getValue("sl") != null)
          {
            if (rec2.getValue("sl") != null)
            {
              long sldate1 = new Date().getTime() - rec1.getDateValue("datereported").getTime() + (1000 * rec1.getlongValue("sl"));
              long sldate2 = new Date().getTime() - rec2.getDateValue("datereported").getTime() + (1000 * rec2.getlongValue("sl"));
              if (sldate1 < sldate2)
              {
                // rec1 sl läuft eher ab oder ist abgelaufen
                return 1;
              }
              else
              {
                // rec2 sl läuft eher ab oder ist abgelaufen
                return -1;
              }
            }

            // rec1 hat SL aber rec2 hat keinen, also rec1 vor rec2
            return 1;
          }
          else
          {
            if (rec2.getValue("sl") != null)
            {
              // rec2 hat SL aber rec1 hat keinen, also rec2 vor rec1
              return -1;
            }
          }
        }
        catch (Exception ex)
        {
          throw new RuntimeException(ex);
        }

        // ansonsten ursprüngliche Reihenfolge
        return 0;
      }

    }

  }

  public void findByUserAndStateAndSortedbySL(IClientContext context, IUser user, String statusConstraint, String relationSet) throws Exception
  {
    AdhocBrowserDefinition browserDef = new AdhocBrowserDefinition(context.getApplicationDefinition(), "call");
    browserDef.addBrowserField("call", "pkey", "Meldungsnr.", SortOrder.NONE);
    browserDef.addBrowserField("call", "callstatus", "Status", SortOrder.NONE);
    browserDef.addBrowserField("callworkgroup", "name", "AK", SortOrder.NONE);
    browserDef.addBrowserField("call", "priority", "Priorität", SortOrder.DESCENDING);
    browserDef.addBrowserField("call", "datereported", "Meldungsbeginn", SortOrder.DESCENDING);
    browserDef.addBrowserField("call", "problem", "Problem", SortOrder.NONE);
    browserDef.addBrowserField("call", "sl", "sl", SortOrder.NONE);
    IDataBrowser browser = context.getDataAccessor().createBrowser(browserDef);

    Call.searchData(context, user, browser, statusConstraint, relationSet);

    IDataBrowserComparator comparator = new SL_Comparator();

    // Sort comparator setzen
    ((DataBrowser) browser).setGuiSortStrategy(comparator);

    // display the result set
    //

    context.getGUIBrowser().setData(context, browser);
  }

  public static void searchData(IClientContext context, IUser user, IDataBrowser browser, String statusConstraint, String relationSet) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor(); // current data
                                                        // connection

    IDataTable callTable = context.getDataTable(); // the table in which the
                                                    // actions performes
    IDataTable groupmemberTable = accessor.getTable("groupmember"); // the
                                                                    // groupmember
                                                                    // table to
                                                                    // perform a
                                                                    // constraint

    // Meister wollen einen Clear Focus vorab!
    context.clearDomain();

    String userKey = user.getKey();

    // set search criteria
    callTable.qbeSetValue("callstatus", statusConstraint);

    groupmemberTable.qbeSetValue("employeegroup", userKey);
    // auch nur lesenden Zugriff dies erlauben
    // groupmemberTable.qbeSetValue("accessallowed", "lesen/schreiben");

    // do the search itself
    //
    browser.search(relationSet, Filldirection.BOTH);

  }

  /**
   * Search calls of the current user
   * 
   * @param context
   *          ClientContext
   * @param statusConstraint
   * @param relationSet
   */
  public static void findByUserAndState(IClientContext context, IUser user, String statusConstraint, String relationSet) throws Exception
  {
    IDataBrowser browser = context.getDataBrowser(); // the current browser
    searchData(context, user, browser, statusConstraint, relationSet);
    context.getGUIBrowser().setData(context, browser);
  }

  /**
   * Set calls.callstatus = newStatus
   * 
   * @param context
   * @param newStatus
   */
  public static void setStatus(Context context, IDataTableRecord call, String newStatus) throws Exception
  {
    IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
    try
    {
      call.setValue(currentTransaction, "callstatus", newStatus);
      currentTransaction.commit();
    }
    finally
    {
      currentTransaction.close();
    }
    logger.debug("commit done");
  }

  /**
   * 
   * @return
   * @throws Exception
   */
  public static boolean accessallowed(IDataTableRecord record) throws Exception
  {
    Context context = Context.getCurrent();
    IUser user = context.getUser();
    boolean access;
    if (user.hasOneRoleOf(globalAccessRoles))
    {
      access = true;
    }
    else
    {
      IDataTable groupmember = context.getDataTable("groupmember");
      groupmember.qbeClear();
      groupmember.qbeSetValue("employeegroup", context.getUser().getKey());
      groupmember.qbeSetValue("workgroupgroup", record.getValue("workgroupcall"));
      groupmember.qbeSetValue("accessallowed", "lesen/schreiben");
      groupmember.search();
      access = groupmember.recordCount() > 0;
    }

    return access;
  }

  /**
   * wenn der User nur Rolle AK hat, dann muss die Tabelle groupmember <br>
   * mit dem User.key eingeschränkt werden
   * 
   * @param context
   */
  public static void setGroubmemberConstraint(IClientContext context) throws Exception
  {
    IUser user = context.getUser();
    if (!(user.hasOneRoleOf(globalAccessRoles) || context.getDomain().getName().equals("f_call_entryMBTECH")))
    {
      IDataTable grpmember = context.getDataTable("groupmember");
      grpmember.qbeClear();
      grpmember.qbeSetValue("employeegroup", user.getKey());
    }

  }

  public static void setDefault(IClientContext context, IDataTableRecord newcall, IDataTransaction newtrans) throws Exception

  {

    newcall.setValue(newtrans, "autoclosed", "Ja");
    newcall.setValue(newtrans, "cclist", null);
    newcall.setValue(newtrans, "closedby_sd", "0");
    newcall.setValue(newtrans, "closedtaskcount", "0");
    newcall.setValue(newtrans, "coordinationtime", null);
    newcall.setValue(newtrans, "cti_phone", null);
    newcall.setValue(newtrans, "dateassigned", null);
    newcall.setValue(newtrans, "datecallbackreq", null);
    newcall.setValue(newtrans, "datecallconnected", null);
    newcall.setValue(newtrans, "dateclosed", null);
    newcall.setValue(newtrans, "datedocumented", null);
    newcall.setValue(newtrans, "datefirstcontact", null);
    newcall.setValue(newtrans, "dateowned", null);
    newcall.setValue(newtrans, "datereported", null);
    newcall.setValue(newtrans, "dateresolved", null);
    newcall.setValue(newtrans, "defaultcontract", "0");
    newcall.setValue(newtrans, "forwardbyphone", "0");
    // newcall.setValue(newtrans,"history", null);
    newcall.setValue(newtrans, "ksl", null);
    newcall.setValue(newtrans, "opentaskcount", "0");
    if ("f_call_entry".equals(context.getDomain().getName()))
    {
      newcall.setValue(newtrans, "origin", "Tel.");
    }
    else
    {
      newcall.setValue(newtrans, "origin", null);

    }
    if (context.getDataTable("customerint").recordCount() == 1)
    {
      newcall.setValue(newtrans, "callbackmethod", context.getDataTable("customerint").getRecord(0).getValue("communicatepref"));
    }
    else
    {
      newcall.setValue(newtrans, "callbackmethod", null);
    }

    newcall.setValue(newtrans, "sd_time", null);
    newcall.setValue(newtrans, "sl", null);
    newcall.setValue(newtrans, "totaltaskdoc", null);
    newcall.setValue(newtrans, "totaltasktimespent", null);
    newcall.setValue(newtrans, "routinginfo", "von Hand geroutet");

    // setzen von datecallconnected
    newcall.setValue(newtrans, "datecallconnected", new Date());

    // setlinkedrecord für Agent
    IDataTable agent = context.getDataAccessor().getTable("agent");
    agent.clear();
    agent.qbeClear();
    agent.qbeSetValue("pkey", context.getUser().getKey());
    agent.search();
    if (agent.recordCount() == 1)
    {
      newcall.setLinkedRecord(newtrans, agent.getRecord(0));
    }
    else
    {
      throw new BusinessException("Sie sind nicht als Erfasser registriert. \n Bitte wenden Sie sich an einen Administrator.");
    }
    // Setzten von betroffener Person:
    // Wenn Gruppe Melder ausgefüllt und betroffene Person leer,
    // dann kopiere Melder in betroffene Person

    IDataTableRecord customer = context.getDataAccessor().getTable("customerint").getSelectedRecord();
    if (customer != null)
    {
      if (newcall.getValue("affectedperson") == null)
      {
        String value = customer.getStringValue("fullname") + " Tel: " + customer.getSaveStringValue("phonecorr");
        newcall.setValue(newtrans, "affectedperson", value);
      }

    }

  }
}
