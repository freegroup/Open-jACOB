package jacob.event.ui.incident;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 06 01:39:50 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.entrypoint.gui.IncidentNewActivity;
import jacob.entrypoint.gui.IncidentNewCall;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the RouteIncident-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author andreas
 *
 */
public class RouteIncident extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RouteIncident.java,v 1.9 2006/01/12 09:36:25 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Set of all routable states
   */
  static private final Set routeableStates = new HashSet();

  static
  {
    routeableStates.add("New");
    routeableStates.add("Assigned");
    routeableStates.add("In Progress");
  }

  /**
   * Callback for routing an incident to an activity.
   * 
   * @author andreas
   */
  private class RouteToActivityCallback implements IOkCancelDialogCallback
  {
    private final IDataTableRecord incident;

    private RouteToActivityCallback(IDataTableRecord incidentRecord)
    {
      this.incident = incidentRecord;
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context) throws Exception
    {
      // and popup new window with created activity
      //
      Properties props = new Properties();
      props.put("incidentKey", this.incident.getSaveStringValue("pkey"));
      EntryPointUrl.popup(context, new IncidentNewActivity(), props);
    }
  }
  private class RouteToCallManageCallback implements IOkCancelDialogCallback
  {
    private final IDataTableRecord incident;

    private RouteToCallManageCallback(IDataTableRecord incidentRecord)
    {
      this.incident = incidentRecord;
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context) throws Exception
    {
      // and popup new window with created activity
      //
      Properties props = new Properties();
      props.put("incidentKey", this.incident.getSaveStringValue("pkey"));
      EntryPointUrl.popup(context, new IncidentNewCall(), props);
    }
  }
  /**
   * The user has been click on the corresponding button.
   * 
   * @param context
   *            The current client context
   * @param button
   *            The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord incidentRecord = context.getSelectedRecord();
    if ("Routed".equals(incidentRecord.getValue("status")))
    {
      alert(ApplicationMessage.getLocalized("RouteIncident.12"));
      return;
    }

    String type = incidentRecord.getStringValue("type");
    if ("Call".equals(type))
    {
        IOkCancelDialog dialog = context.createOkCancelDialog(ApplicationMessage.getLocalized("RouteIncident.10"), new RouteToCallManageCallback(incidentRecord));
        dialog.show();
        return;
      }       
    if ("Sales Activity".equals(type))
    {
      IOkCancelDialog dialog = context.createOkCancelDialog(ApplicationMessage.getLocalized("RouteIncident.15"), new RouteToActivityCallback(incidentRecord));
      dialog.show();
    }
    else
    {
      if ("Customer Management".equals(type) || "Quote".equals(type) || "Order".equals(type))
      {
        // simply set status to routed
        //
        IDataTransaction transaction = incidentRecord.getAccessor().newTransaction();
        try
        {
          incidentRecord.setValue(transaction, "status", "Routed");
          transaction.commit();
        }
        finally
        {
          transaction.close();
        }
      }
      else
      {
        alert(ApplicationMessage.getLocalized("RouteIncident.19"));
      }
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status == IGuiElement.SELECTED && routeableStates.contains(context.getSelectedRecord().getStringValue("status")));
  }
}
