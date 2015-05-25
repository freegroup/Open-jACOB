/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.call;

import jacob.common.Call;
import jacob.common.gui.object.Warranty;

import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * 
 * 
 * 		<h1>Funktionsbeschreibung</h1>
		<p></p>
		<table width="497" border="1" cellspacing="2" cellpadding="0">
			<tr>
				<td>
					<h2>Aufbau des Scriptes Call Accept</h2>
				</td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td bgcolor="#ffff99">1. Script  selbst: CallAccept<br>
					<p>Das Script selbst steuert lediglich die Aktivierung des Buttons </p>
					<p><button name="buttonName" type="button">Meldung Annehmen</button></p>
					<p>Ist der Status </p>
					<p>(&quot;Fehlgeroutet&quot;)  oder (&quot;AK zugewiesen&quot;), ist der Button aktiv, sonst nicht.</p>
					<p>Wird der Aktibe Button gedr&uuml;ckt, wird das Script Calls ausgef&uuml;hrt. (2)</p>
					<p> </p>
					<p></p>
				</td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td bgcolor="#99ff99">2. Scrit Calls:
					<p>Dieses Script setzt den Status eines Calls auf angenommen. Wenn das Script erfolgreich durchlaufen wurde, tritt automatsch das Tabellnscript (TableRule)  </p>
					<p>CallTableRecord</p>
					<p>seinen Dienst an.</p>
				</td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td bgcolor="#6666ff">3. CallTableRecord:
					<p>Dies TableRule sorgt in diesem Kontext lediglich daf&uuml;r, dass das Datumsfeld</p>
					<p>dateowned (in der Maske &quot;angenimmen am&quot;) </p>
					<p>mit dem Aktuellen Datum vorbelegt wird</p>
					<p></p>
				</td>
			</tr>
		</table>
 * 
 * 
 * @author achim
 *
 */
public class CallAccept extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallAccept.java,v 1.14 2005/03/11 12:27:59 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.14 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
  private static final Set validStatus = new HashSet();
  static 
  {
    validStatus.add("Fehlgeroutet");
    validStatus.add("AK zugewiesen");
  }
  
	public void onAction(IClientContext context, IGuiElement button)throws Exception
	{
        IDataTableRecord callrecord = context.getSelectedRecord();
        if (!Call.accessallowed(callrecord))
        {
            IMessageDialog dialog = context.createMessageDialog("Sie haben keinen schreibenden Zugriff auf diese Meldung");
            dialog.show();
            return ;
        }

		// fragen ob angenommen werden soll
    IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Wollen Sie die Meldung wirklich annehmen?",new IOkCancelDialogCallback()
    {
      public void onOk(IClientContext context) throws Exception
      {      
        // wenn OK, dann den Status setzen
      	Call.setStatus(context,context.getSelectedRecord(),"Angenommen");
      	Warranty.checkWarranty(context,context.getSelectedRecord());
      }
      public void onCancel(IClientContext context) throws Exception {}
    });
    
    okCancelDialog.show();
	  
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
		if(status == IGuiElement.SELECTED)
		{
			IDataTableRecord currentRecord = context.getSelectedRecord();
			String callstatus = currentRecord.getStringValue("callstatus");
			
			button.setEnable(validStatus.contains(callstatus));
		}
	}
}
