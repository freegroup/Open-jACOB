/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 15:13:27 CEST 2010
 */
package jacob.event.ui.article;


import jacob.common.TextblockEditController;
import jacob.model.History;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.util.StringUtil;


/**
 * The event handler for the ArticleUpdateRecordButton update button.<br>
 * 
 * @author andherz
 */
public class ArticleUpdateRecordButton extends IActionButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ArticleUpdateRecordButton.java,v 1.4 2010-09-29 14:57:23 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";
	
	static private final String PREFILL_MESSAGE ="Grund der letzten Änderung...";
	
	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the UPDATE action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
	  return TextblockEditController.endEdit(context);
	}

	/**
	 * This event method will be called, if the UPDATE action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	  // Falls der Artikel erfolgreich gespeichert wurde hat der Anwender die Möglichkkeit eine optionale
	  // Bemerkung zu hinterlegen.
	  context.createAskDialog("Grund der Änderung",true,PREFILL_MESSAGE, new IAskDialogCallback()
    {
      public void onOk(IClientContext context, String value) throws Exception
      {
        // Falls der Anwender den prefill Text nicht gelöscht hat (Faulheit), dann tun wir dies hetzt für Ihn
        value = StringUtil.replace(value,PREFILL_MESSAGE,"");
        if(!StringUtil.emptyOrNull(value))
        {
          IDataAccessor acc = context.getDataAccessor().newAccessor();
          IDataTransaction trans = context.getDataAccessor().newTransaction();
          try
          {
            IDataTableRecord historyRecord = History.newRecord(acc,trans);
            historyRecord.setLinkedRecord(trans,context.getSelectedRecord());
            historyRecord.setValue(trans, History.reason, value);
            trans.commit();
            context.showTransparentMessage("Änderungsjournal wurde erstellt");
          }
          finally
          {
            trans.close();
          }
        }
      }
      
      public void onCancel(IClientContext context) throws Exception
      {
      }
    }).show();
	}

}
