/*
 * Created on 18.08.2004
 * by mike
 *
 */
package jacob.common.gui;


import org.apache.commons.logging.Log;
import jacob.common.AppLogger;
import java.util.Map;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;

/**
 * @author mike
 *  
 */
 public  class VoteDialog
{
	static public final transient String RCS_ID = "$Id: VoteDialog.java,v 1.6 2006/05/15 13:48:45 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.6 $";
	static protected final transient Log logger = AppLogger.getLogger();

	/**
	 * Bewertung: Ein Dialog, der eine manuelle Bewertung der Meldung durch den
	 * AK ermöglicht <br>
	 * 1.Eine Meldung kann nur einmal bewertet werden <br>
	 * 2.Bei einer Benotung schlechter als 3 ist eine Begründung nötg <br>
	 * 3 gespeichert wird die Berwertung in call.grade und call.gradestatement
	 * <br>
	 * 
	 * @author mike
	 *  
	 */
	static class DialogCallback implements IFormDialogCallback
	{
		boolean isPersonalVote;
		IDataTableRecord callRecord;
		public DialogCallback(boolean isPersonalVote)
		{
			this.isPersonalVote = isPersonalVote;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext,
		 *      java.lang.String, java.util.Map)
		 */
		public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
		{
			String grade = (String) formValues.get("vote");
			String note = (String) formValues.get("note");
			if (logger.isDebugEnabled())
				logger.debug("Rückgabewert grade=" + grade + "  note.length=" + note.length() );
			//  Überprüfen of Note schlechter als3 und keine Begründung
			if ("3".compareTo(grade) < 0 && note.length()<1)
			{
				show(context, Integer.parseInt(grade)-1, true);
				return;
			}
      IDataTableRecord call = context.getSelectedRecord();
      if (call==null)
        return;
			// Werte in CAll speichern
			IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
			try
			{
				call.setValue(currentTransaction, "grade", grade);
				call.setValue(currentTransaction, "gradestatement", note);
				call.setValue(currentTransaction, "personal_vote", isPersonalVote ? "1" : "0");

				currentTransaction.commit();
			}
			finally
			{
				currentTransaction.close();
			}
		}
	}

	/**
	 * Zeigt den Voting Dialog
	 * 
	 * @param context
	 * @param selectionIndex
	 * @param isPersonalVote
	 */

	public static void show(IClientContext context, int selectionIndex, boolean isPersonalVote)
	{

		CellConstraints cc = new CellConstraints();
		FormLayout layout = new FormLayout("10dlu,p,3dlu,50dlu,100dlu,10dlu", // columns
				"10dlu,p,10dlu,p,20dlu,p,3dlu,150dlu,10dlu,"); // rows

		// Dialog samt Callback-Klasse anlegen
		//
		IFormDialog dialog = context.createFormDialog("Benotung", layout, new DialogCallback(isPersonalVote));
		// Elemente in den Dialog einfügen
		//
		dialog.addLabel("Qualität der Meldung (Note):", cc.xy(1, 3));
		dialog.addComboBox("vote", new String[]{"1", "2", "3", "4", "5", "6"}, selectionIndex, cc.xy(3, 3));
		dialog.addLabel("Eine Begründung für eine Benotung schlechter als 3:", cc.xywh(1, 5, 4, 1));
		dialog.addTextArea("note", "", cc.xywh(1, 7, 4, 1));
		dialog.setCancelButton("Abbruch");
		dialog.addSubmitButton("ok", "Ok");

		// Dialog anzeigen
		//
		dialog.show(400, 350);
	}


}
