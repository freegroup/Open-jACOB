/*
 * Created on 03.05.2004
 * by mike
 *
 */
package jacob.event.screen.f_custinfo.customercall;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * 
 * @author mike
 *
 */
public class CustomerintSave extends IButtonEventHandler {
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
			ISingleDataGuiElement description =(ISingleDataGuiElement) context.getGroup().findByName("customerintCalltext");
			
				String value = description.getValue();
				
				if (value!=null && value.length()>0)
				{
				  // create the new data record entry in the database
				  //
					IDataTable custcallTable = context.getDataTable("custcall");
					IDataTransaction currentTransaction =  new IDataTransaction();
          try
          {
            IDataTableRecord record = custcallTable.newRecord(currentTransaction);
            record.setValue(currentTransaction,"description",value);
            record.setValue(currentTransaction,"datecreated","now");
            record.setValue(currentTransaction,"employee_key",context.getUser().getKey());
            currentTransaction.commit();
          }
          finally
          {
            currentTransaction.close();
          }
					
					// get the success form and prepare and data element with the handsover call text
					//
					IForm                 nextForm = (IForm)context.getDomain().findByName("custcalladded");
					ISingleDataGuiElement element  =(ISingleDataGuiElement)nextForm.findByName("customerintCalltext");
					element.setValue(value);
					
					// show the success form
					//
					context.setCurrentForm("custcalladded");
				}
				else
				{                                                                
				  IMessageDialog dialog =context.createMessageDialog("Geben Sie bitte einen Auftragstext ein.");
				  dialog.show(450,200);
				  //alert(context,button,"Hinweis","Geben Sie bitte ein Auftragstext ein.");
				}
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement guiElement) throws Exception 
	{
	  System.out.println(context.getGroup()+"----------------------------------------");
	  ISingleDataGuiElement description =(ISingleDataGuiElement) context.getGroup().findByName("customerintCalltext");
		System.out.println(description+"---------------------------------------------------------------------------------");
		description.setEditable(true);
	}
}
