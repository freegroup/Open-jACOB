/*
 * Created on 22.08.2004
 *
 */
package jacob.common.gui.task;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public class TaskCancel extends IButtonEventHandler 
{
	  private static final Set validStatus = new HashSet();
	  static 
	  {
	    validStatus.add("Neu");
	    validStatus.add("Angelegt");
	    validStatus.add("Freigegeben");
	   
	  }
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)
			throws Exception 
	{
		// fragen ob storniert werden soll
	    IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Wollen Sie den Auftrag wirklich stornieren?",new IOkCancelDialogCallback()
	    {
	      public void onOk(IClientContext context) throws Exception
	      {      
	        // wenn OK, dann den Status setzen
	      	IDataTableRecord taskrec = context.getSelectedRecord();

			IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
			try
			{
		      	taskrec.setValue(currentTransaction,"taskstatus","Storniert");
		      	taskrec.setValue(currentTransaction,"datecanceld",new Date());
		      	currentTransaction.commit();
			}
			finally
			{
				currentTransaction.close();
			}
	      }
	      
	      public void onCancel(IClientContext context) throws Exception {}
	    });
	    
	    okCancelDialog.show();
		  
		}
	

	
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
		if(status == IGuiElement.SELECTED)
		{
			IDataTableRecord currentRecord = context.getSelectedRecord();
			String taskstatus = currentRecord.getStringValue("taskstatus");
			
			button.setEnable(validStatus.contains(taskstatus));
		}
	}
}



