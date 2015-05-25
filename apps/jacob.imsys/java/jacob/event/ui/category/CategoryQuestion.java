/*
 * 
 * Created on 31.08.2004
 * by mike
 *
 */
package jacob.event.ui.category;

import jacob.common.AppLogger;
import jacob.common.data.Routing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * Zeigt die zum Gewerk linkten Fragen in einem Dialog <br>
 * und speichert die Fragen in die Meldung
 * 
 * @author mike
 *  
 */
public class CategoryQuestion extends IButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: CategoryQuestion.java,v 1.1 2005/06/02 16:29:44 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	static protected final transient Log logger = AppLogger.getLogger();
  private static final Set validCallMode = new HashSet();
  static 
  {
  	validCallMode.add(IGuiElement.NEW);
  	validCallMode.add(IGuiElement.SELECTED);
  	validCallMode.add(IGuiElement.UPDATE);
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		// die Fragen holen, es gibt welche da ja Button sichtbar ;-)
		IDataTableRecord currentRecord = context.getSelectedRecord();
		String categorykeys = Routing.getCategoryConstraint(context.getDataAccessor(), currentRecord.getStringValue("pkey"));
		IDataTable questionTable = context.getDataTable("question");
		questionTable.qbeClear();
		questionTable.qbeSetValue("category_key", categorykeys);
		questionTable.search();
		StringBuffer rowsString = new StringBuffer();
		Map question = new HashMap();
		rowsString.append("p,3dlu");
		for (int i = 0; i < questionTable.recordCount(); i++)
		{
			question.put("Frage" + i, questionTable.getRecord(i).getSaveStringValue("question"));
			rowsString.append(",p,25dlu,3dlu");
		}
		rowsString.append(",5dlu");
		if (logger.isDebugEnabled())
			logger.debug("Anzahl der Fragen: " + questionTable.recordCount());

		FormLayout layout = new FormLayout("10dlu,10dlu,p,10dlu", // 4 columns
				rowsString.toString()); //  rows
		CellConstraints c = new CellConstraints();
		IFormDialog dialog = context.createFormDialog("Fragenkatalog", layout, new DialogCallback(question));
		dialog.addHeader("Folgende Fragen aus dem Fragenkatalog sollten beantwortet werden:", c.xy(2, 0));
		Iterator iter = question.keySet().iterator();
		int i = 2;
		while (iter.hasNext())
		{
			String askKey = (String) iter.next();
			String ask = (String) question.get(askKey);

			dialog.addLabel(ask, c.xywh(1, i, 2, 1));
			dialog.addTextField(askKey, "", c.xy(2, i + 1));
			i = i + 3;
		}
		dialog.setCancelButton("Abbruch");
		dialog.addSubmitButton("OK", "Speichern");
		//dialog.setDebug(true);
		dialog.show();
	}

	class DialogCallback implements IFormDialogCallback
	{
		Map questions;

		private DialogCallback(Map question)
		{
			this.questions = question;
		}

		private Map getQuestions()
		{
			return this.questions;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext,
		 *      java.lang.String, java.util.Map)
		 */
		public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
		{
			Map allQuestions = getQuestions();
			StringBuffer problemtext = new StringBuffer();
			Iterator iter = allQuestions.keySet().iterator();
			while (iter.hasNext())
			{
				String askKey = (String) iter.next();
				String ask = (String) allQuestions.get(askKey);
				String answer = (String) formValues.get(askKey);
				if (answer.length() > 0)
				{
					problemtext.append(ask);
					problemtext.append("\n   ");
					problemtext.append(answer);
					problemtext.append("\n");
				}
			}
			// wenn keine Fragen beantwortet wurden, dann raus
			if (problemtext.length() == 0)
				return;

			problemtext.insert(0, "Beantwortete Fragen aus dem Fragenkatalog:\n");

			IDataTable callTable = context.getDataTable("call");
			IDataTableRecord call = callTable.getRecord(0);
			// wenn ich im selectet Mode bin, dann Antworten speichern mit commit
			if (call.isNormal())
			{
				IDataTransaction trans = context.getDataAccessor().newTransaction();
				try
				{
					call.appendLongTextValue(trans, "problemtext", problemtext.toString());
					trans.commit();
				}
				finally
				{
					trans.close();
				}
			}
			else // Antworten dranhängen?!
			{
				IDataTransaction trans = call.getCurrentTransaction();
				call.appendLongTextValue(trans, "problemtext", problemtext.toString());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IGuiElement.GroupState,
	 *      de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
	{
		if (status == IGuiElement.SELECTED)
		{
			// schauen ob es eine Frage gibt die an diesem Gewerkebaum hängt
			IDataTableRecord currentRecord = context.getSelectedRecord();
			String categorykeys = Routing.getCategoryConstraint(context.getDataAccessor(), currentRecord.getStringValue("pkey"));
			IDataTable questionTable = context.getDataTable("question");
			questionTable.qbeClear();
			questionTable.qbeSetValue("category_key", categorykeys);
			// schauen ob eine Meldung selektiert ist geht nur in der GUI
			IGroup callGroup = (IGroup)context.getForm().findByName("callEntry");
			if (questionTable.search() > 0 && validCallMode.contains(callGroup.getDataStatus()))
			{
				button.setEnable(true);
			}
			else
			{
				button.setEnable(false);
			}

		}
		else
		{
			button.setEnable(false);
		}
	}
}
