package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Jun 14 14:14:20 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.Call;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the CallCancel-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallCancel extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallCancel.java,v 1.1 2005/06/17 12:07:53 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

    public static final String STATUS = "callstatus";

    // die unterschiedlichen Meldungsstatus
    public static final String DURCHGESTELLT = "Durchgestellt";
    public static final String AK_ZUGEWIESEN = "AK zugewiesen";
    public static final String ANGENOMMEN = "Angenommen";
    // Set welches alle Status vor "Angenommen" enthält
    private static final Set allAccceptStatus = new HashSet();
    
    static
    {
        allAccceptStatus.add(DURCHGESTELLT);  
        allAccceptStatus.add(AK_ZUGEWIESEN);  
        allAccceptStatus.add(ANGENOMMEN);  
    }
    public static void setStatusCanceled(IClientContext context,  IDataTableRecord call)throws Exception
    {
        IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
        try
        {
            call.setValue(currentTransaction, "callstatus", "Verworfen");
            if (call.getValue("dateassigned") == null)
                call.setValue(currentTransaction,"dateassigned","now");
            if (call.getValue("dateowned") == null)
                call.setValue(currentTransaction,"dateowned","now");
            if (call.getValue("dateresolved") == null)
                call.setValue(currentTransaction,"dateresolved","now");
            if (call.getValue("datedocumented") == null)
                call.setValue(currentTransaction,"datedocumented","now");
            
            call.setValue(currentTransaction,"closedby_sd","1");
            call.setValue(currentTransaction,"coordinationtime","0");
            currentTransaction.commit();
        }
        finally
        {
            currentTransaction.close();
        }
    }
	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
          IDataTableRecord callRecord = context.getSelectedRecord();
            if (!Call.accessallowed(callRecord))
            {
                IMessageDialog dialog = context.createMessageDialog("Sie haben keinen schreibenden Zugriff auf diese Meldung");
                dialog.show();
                return ;
            }
            IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Wollen Sie diese Meldung wirklich stornieren?",new IOkCancelDialogCallback()
                    {
                      public void onOk(IClientContext context) throws Exception
                      {      
                        // wenn OK, dann den Status setzen
                        setStatusCanceled(context,context.getSelectedRecord());
                      }
                      public void onCancel(IClientContext context) throws Exception {}
                    });
                    
                    okCancelDialog.show();

  }

   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
        if(status == IGuiElement.SELECTED)
        {
            IDataTableRecord currentRecord = context.getSelectedRecord();
            String callstatus = currentRecord.getStringValue("callstatus");
            button.setEnable(allAccceptStatus.contains(callstatus));
        }
	}
}

