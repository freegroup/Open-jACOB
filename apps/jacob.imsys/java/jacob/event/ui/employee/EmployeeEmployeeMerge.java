package jacob.event.ui.employee;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 02 12:22:18 CEST 2005
 *
 */
import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormActionEmitter;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.data.DataUtils;
import jacob.common.data.Employee;


import org.apache.commons.logging.Log;



 /**
  * The Event handler for the EmployeeEmployeeMerge-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class EmployeeEmployeeMerge extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: EmployeeEmployeeMerge.java,v 1.1 2005/06/02 16:29:45 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();


  public class SearchEmployee implements IFormActionEmitter
  {
    final String backfillField;
    final String searchField;
    
    public SearchEmployee(String searchField,String backfillField)
    {
      this.backfillField=backfillField;
      this.searchField  =searchField;
    }
    public void onAction(IClientContext context,IFormDialog parent, Map formValues) throws Exception
    {
      IDataAccessor accessor= context.getDataAccessor().newAccessor();
      IDataTable table =accessor.getTable("employee");
      table.qbeSetKeyValue("pkey",formValues.get(searchField));
      if(table.search()==1)
      {
        String fullname=table.getRecord(0).getStringValue("fullname");
        formValues.put(backfillField,fullname);
      }
    }
  }
  
  /** 
   * Falls der Anwender zwei Mitarbeiter gefunden hat, werden diese jetzt gemerged
   *
   */
  public class DialogCallback implements IFormDialogCallback
  {
    public void onSubmit(IClientContext context, String buttonId,  Map formValues) throws Exception
    {
        String deleteKey =(String)formValues.get("id1");
        // prüfen ob zu löschender Datensatz vom Typ DBCS ist, wenn ja nicht löschen.
        String deleteKeyStatus = DataUtils.getValueWhere(context.getDataAccessor(),"employee","pkey",deleteKey,"employeestatus");
        if ( "DBCS".equals(deleteKeyStatus))
        {
        IMessageDialog dialog = context.createMessageDialog("WARNUNG: Der Mitarbeiter mit ID=" + deleteKey + " ist im Status 'DBCS' und diese dürfen nicht inaktiv gesetzt werden!");
        dialog.show();
    
        }
        else
        {
        Employee.merge(context,deleteKey,(String)formValues.get("id2"));
        IMessageDialog dialog = context.createMessageDialog("Mitarbeiter mit ID="+ deleteKey + " wurde erfolgreich auf inaktiv gesetzt. Alle Datensätze sind umgezogen.");
        dialog.show();
        }
 
      context.clearDomain();
    }
  }
  
  /* 
   * Der Anwender hat im Contextmenu der Gruppe 'Stammdaten->Employee' den Menupunkt
   * 'Mitarbeiter zusammenfügen' ausgewählt.
   * <br>
   * <br>
   * Dem Benutzer wird jetzt ein Dialog gezeit in dem er zwei Mitarbeiter suchen kann.<br>
   * Hat er beide gesucht, dann werden diese gemerged.
   * 
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // Anlegen und definieren wie der Dialog aufgeteilt ist
    // 
    FormLayout layout = new FormLayout("50dlu,10dlu,p,3dlu,50dlu,3dlu,200dlu,3dlu,p,50dlu",  // columns
                                       "50dlu,p,3dlu,p,10dlu,p,3dlu,p,50dlu");                       // rows
    
    // Hilfklasse um Elemente im FormDialog mittel Gitterposition zu plazieren
    //
    CellConstraints cc= new CellConstraints();
    
    // FormDialog mit vorgefüllten Titel, Layoutangaben und Callbackklasse anlegen
    // Die Callbackklasse wird gerufen wenn der Anwender einen Button in der Buttonbar drückt
    //
    IFormDialog dialog =context.createFormDialog("Mitarbeiter zusammenfassen",layout, new DialogCallback());
    
    // Eingabeelemente und Suchbutton für den ersten 'Employee' im Dialog plazieren
    //
    dialog.addHeader("Zu löschender Datensatz", cc.xywh(1,1,8,1));
    dialog.addLabel("Id", cc.xy(2,3));
    dialog.addTextField("id1"      ,"", cc.xy(4,3));
    dialog.addTextField("fullname1","", cc.xy(6,3));
    dialog.addFormButton(new SearchEmployee("id1","fullname1"),"Suchen",cc.xy(8,3));
    
    // Eingabeelemente und Suchbutton für den zweiten Employee im Dialog plazieren
    //
    dialog.addHeader("Ziel Datensatz", cc.xywh(1,5,8,1));
    dialog.addLabel("Id", cc.xy(2,7));
    dialog.addTextField("id2"      ,"", cc.xy(4,7));
    dialog.addTextField("fullname2","", cc.xy(6,7));
    dialog.addFormButton(new SearchEmployee("id2","fullname2"),"Suchen",cc.xy(8,7));
    
        dialog.addSubmitButton("ok","Ok");

    // eventuell den Dialog in den Debugmodus schalten um besser die Elementpositionen zu sehen
    //
    dialog.setDebug(false);
    
    // Dialog anzeigen
    //
    dialog.show();
  }

  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState status, IGuiElement emitter)  throws Exception
  {
  }
}

