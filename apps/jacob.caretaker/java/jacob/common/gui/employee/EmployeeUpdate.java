/*
 * Created on 25.06.2004
 * by mike
 *
 */
package jacob.common.gui.employee;


import jacob.common.AppLogger;
import jacob.common.data.Employee;

import java.util.Map;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * <h2>Prüfen ob neuer Datensatz eine Duplette ist</h2>
 * <ol>
 * <li>Bei Übernahme des alten den "employeestatus" auf "von Hand" setzen wenn
 * vorher er ungleich "DBCS" war</li>
 * </ol>
 * 
 * <h2>Konsistenzprüfungen</h2>
 * <ol>
 * <li>"employeetype" = "Extern" benötigt Link zur Fremdfirma
 * "orgexternal_key"</li>
 * <li>"employeetype" = "Intern" benötigt Werk "emplsite_keycorr" und
 * "orgexternal_key" wird gelöscht</li>
 * <li>"phonecorr" wenn leer auffordern eine anzugeben</li>
 * <li>Rückmeldung ("communicatepref") wenn leer auffordern eine anzugeben und
 * prüfen:
 * <ul>
 * <li>"Email" muss "emailcorr" gefüllt sein</li>
 * <li>"FAX" muss "faxcorr" gefüllt sein</li>
 * </ul>
 * </li>
 * </ol>
 * 
 * @author Andreas Herz
 */
public class EmployeeUpdate extends IActionButtonEventHandler
{
	static protected final transient Log logger = AppLogger.getLogger();

	class UpdateCallback implements IFormDialogCallback
	{
		private final IDataTable dub;

		public UpdateCallback(IDataTable dub)
		{
			this.dub = dub;
		}

		public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
		{
			if (buttonId.equals("create"))
			{
				context.getDataTable().getTableTransaction().commit();
				return;
			}
			else if (buttonId.equals("selected"))
			{
				// Es wird kein neuer Mitarbeiter angelegt. Angefangenene Transaktion
				// kann also
				// geschlossen werden
				//
				context.getDataTable().getTableTransaction().close();
				//this.transaction.close();

				// Benutzer Auswahl holen *BEVOR* man clearDomain aufruft. clearDomain
				// löscht
				// alle DataTables Inhalte.
				//
				int index = Integer.parseInt((String) values.get("selectedCustomerIndex"));
				IDataTableRecord selectedEmployee = dub.getRecord(index);
				String pkey = selectedEmployee.getStringValue("pkey");

				// Bereits eingegebenene Daten werden gelöscht.
				//
				context.clearDomain();

				// Den selectierten Employee eventuell reaktivieren.
				//
				Employee.changeEmployeeState(null, context, pkey, "employee", "von Hand");

				// Neue Suche MIT backfill aller Relationen
				//
				IDataTable employeeTable = context.getDataTable();
				employeeTable.qbeClear();
				employeeTable.qbeSetKeyValue("pkey", pkey);
				IDataBrowser browser = context.getDataBrowser();
				// in Abhängigkeit von der Domain suchen
				if ("f_call_entry".equals(context.getDomain().getName()))
				{
					browser.search("r_customer", Filldirection.BOTH);
				}
				else
				{
					browser.search("r_employee", Filldirection.BOTH);
				}

				if (browser.recordCount() == 1)
				{
					browser.setSelectedRecordIndex(0);
					browser.propagateSelections();
				}

				// Suchergebnis anzeigen
				//
				context.getGUIBrowser().setData(context, browser);
			}
		}
	}


	/**
	 * <h2>Konsistenzprüfungen</h2>
	 * <ol>
	 * <li>"employeetype" = "Extern" benötigt Link zur Fremdfirma
	 * "orgexternal_key"</li>
	 * <li>"employeetype" = "Intern" benötigt Werk "emplsite_keycorr" und
	 * "orgexternal_key" wird gelöscht</li>
	 * <li>"phonecorr" wenn leer auffordern eine anzugeben</li>
	 * <li>Rückmeldung ("communicatepref") wenn leer auffordern eine anzugeben
	 * und prüfen:
	 * <ul>
	 * <li>"Email" muss "emailcorr" gefüllt sein</li>
	 * <li>"FAX" muss "faxcorr" gefüllt sein</li>
	 * </ul>
	 * </li>
	 * </ol>
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		if (context.getGroup().getDataStatus() != IGuiElement.SELECTED)
		{
			IDataTableRecord contextRecord = context.getSelectedRecord();
			// Falls es ein externer Miterbeiter ist, muss eine Fremdfirma angegeben
			// werden
			//
			String errorMessage = "";

			// in Abhängigkeit von der Domain suchen
			if ("f_call_entry".equals(context.getDomain().getName())||"f_call_entryMBTECH".equals(context.getDomain().getName()))
			{//orgcustomerint Maske
				IDataTable fremdfirma = context.getDataTable("orgexternal");
				if (context.getGroup().getInputFieldValue("customerintEmployeetype").equals("Extern") && fremdfirma.getSelectedRecord() == null)
					errorMessage += "Bei externen Mitarbeiter muß das Feld 'Firma' ausgefüllt sein.\\r\\n";

				// Falls es ein interner Mitarbeiter ist, muss das Werk angegeben
				// werden
				//
				IDataTable site = context.getDataTable("employeesite");
				if (context.getGroup().getInputFieldValue("customerintEmployeetype").equals("Intern") && site.getSelectedRecord() == null)
					errorMessage += "Bei internen Mitarbeiter muß das Feld 'Werk' ausgefüllt sein.\\r\\n";

				// Falls es ein interner Mitarbeiter ist, muss das Feld 'Firma' leer
				//
				if (context.getGroup().getInputFieldValue("customerintEmployeetype").equals("Intern") && fremdfirma.getSelectedRecord() != null)
					errorMessage += "Bei internen Mitarbeiter muß das Feld 'Firma' leer sein.\\r\\n";

				// Telefonnummer muss angegeben werden
				//
				String phoneCorr = context.getGroup().getInputFieldValue("customerPhone");
				if (phoneCorr == null || phoneCorr.length() == 0)
					errorMessage += "Das Feld 'Tel.'muß ausgefüllt sein.\\r\\n";

				// Falls email Rückmeldung gewünscht ist, muss die eMail adresse auch
				// ausgefüllt sein
				//
				String comm = context.getGroup().getInputFieldValue("customerintCommunicatepref");
				String email = context.getGroup().getInputFieldValue("customerEmail");
				if (comm.equals("Email") && (email == null || email.length() == 0))
					errorMessage += "Das Feld 'Email'muß ausgefüllt sein, da Feld 'Rückmeldung' auf 'Email' steht.\\r\\n";

				// Faxnummer mus ausgefüllt sein falls als Rückmeldemedium 'Fax'gewählt
				// wurde
				//
				String fax = context.getGroup().getInputFieldValue("customerFaxcorr");
				if (comm.equals("FAX") && (fax == null || fax.length() == 0))
					errorMessage += "Das Feld 'Fax.'muß ausgefüllt sein, da Feld 'Rückmeldung' auf 'FAX' steht.\\r\\n";
			}
			else
			{ // Adminmasken
				IDataTable fremdfirma = context.getDataTable("orgexternal");
				if (context.getGroup().getInputFieldValue("employeeEmployeetype").equals("Extern") && fremdfirma.getSelectedRecord() == null)
					errorMessage += "Bei externen Mitarbeiter muß das Feld 'Firma' ausgefüllt sein.\\r\\n";

				// Falls es ein interner Mitarbeiter ist, muss das Werk angegeben
				// werden
				//
				IDataTable site = context.getDataTable("employeesite");
				if (context.getGroup().getInputFieldValue("employeeEmployeetype").equals("Intern") && site.getSelectedRecord() == null)
					errorMessage += "Bei internen Mitarbeiter muß das Feld 'Werk Korr' ausgefüllt sein.\\r\\n";

				// Falls es ein interner Mitarbeiter ist, muss das Feld 'Firma' leer
				//
				if (context.getGroup().getInputFieldValue("employeeEmployeetype").equals("Intern") && fremdfirma.getSelectedRecord() != null)
					errorMessage += "Bei internen Mitarbeiter muß das Feld 'Firma' leer sein.\\r\\n";

				// Telefonnummer muss angegeben werden
				//
				String phoneCorr = context.getGroup().getInputFieldValue("employeePhonecorr");
				if (phoneCorr == null || phoneCorr.length() == 0)
					errorMessage += "Das Feld 'Tel.Korr.'muß ausgefüllt sein.\\r\\n";

				// Falls email Rückmeldung gewünscht ist, muss die eMail adresse auch
				// ausgefüllt sein
				//
				String comm = context.getGroup().getInputFieldValue("employeeCommunicatepref");
				String email = context.getGroup().getInputFieldValue("employeeEmailcorr");
				if (comm.equals("Email") && (email == null || email.length() == 0))
					errorMessage += "Das Feld 'Email.Korr.'muß ausgefüllt sein, da Feld 'Rückmeldung' auf 'Email' steht.\\r\\n";

				// Faxnummer mus ausgefüllt sein falls als Rückmeldemedium 'Fax'gewählt
				// wurde
				//
				String fax = context.getGroup().getInputFieldValue("employeeFaxcorr");
				if (comm.equals("FAX") && (fax == null || fax.length() == 0))
					errorMessage += "Das Feld 'Fax. Korr.'muß ausgefüllt sein, da Feld 'Rückmeldung' auf 'FAX' steht.\\r\\n";
			}

			if (errorMessage.length() > 0)
			{
				IMessageDialog dialog = context.createMessageDialog(errorMessage);
				dialog.show();
				return false;
			}

			if (contextRecord.isNew())
			{
				String employeeTyp = contextRecord.getStringValue("employeetype");
				String firstName = contextRecord.getStringValue("firstnamecorr");
				String lastName = contextRecord.getStringValue("lastnamecorr");
				String phone = contextRecord.getStringValue("phonecorr");
				String siteCorr = contextRecord.getSaveStringValue("emplsite_keycorr");

				// do duplicate check only if last and first name are set,
				// otherwise check is not needed because both are required fields and
				// will
				// lead to an error anyhow
				if (firstName != null && lastName != null)
				{
					IDataTable dub = Employee.findDuplicate(context, firstName, lastName);
					if (dub.recordCount() > 0)
					{
						String[] employees = new String[dub.recordCount()];
						for (int i = 0; i < dub.recordCount(); i++)
						{
							IDataTableRecord record = dub.getRecord(i);
							String d_site = record.getSaveStringValue("emplsite_keycorr");
							String d_firstName = record.getSaveStringValue("firstnamecorr");
							String d_lastName = record.getSaveStringValue("lastnamecorr");
							String d_phone = record.getSaveStringValue("phonecorr");

							employees[i] = d_firstName + " " + d_lastName + " Tel:" + d_phone + " Werk:" + d_site;
						}
						FormLayout layout = new FormLayout("30dlu,25dlu,p,3dlu,300dlu,30dlu", // columns
								"30dlu,p,3dlu,p,3dlu,p,3dlu,p,30dlu,p,3dlu,150dlu,30dlu"); // rows
						IFormDialog dialog = context.createFormDialog("Hinweis", layout, new UpdateCallback(dub/*
																																																	  * ,
																																																	  * contextRecord.getCurrentTransaction()
																																																	  */));

						CellConstraints cc = new CellConstraints();

						dialog.addHeader("Neuer Kunde", cc.xywh(1, 1, 4, 1));
						dialog.addLabel("Name:", cc.xy(2, 3));
						dialog.addLabel(firstName + " " + lastName, cc.xy(4, 3));
						dialog.addLabel("Werk:", cc.xy(2, 5));
						dialog.addLabel(siteCorr, cc.xy(4, 5));
						dialog.addLabel("Tel:", cc.xy(2, 7));
						dialog.addLabel(phone, cc.xy(4, 7));

						dialog.addHeader("Bestehende Kunden", cc.xywh(1, 9, 4, 1));
						dialog.addListBox("selectedCustomer", employees, 0, cc.xywh(2, 11, 3, 1));

						dialog.addSubmitButton("create", "Anlegen");
						dialog.addSubmitButton("selected", "Auswahl");
						dialog.setDebug(false);
						dialog.show(400, 400);
						return false;
					}
				}
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
	 *      int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
	{
	}
}
