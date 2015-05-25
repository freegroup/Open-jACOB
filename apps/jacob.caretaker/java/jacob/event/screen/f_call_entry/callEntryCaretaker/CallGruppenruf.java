package jacob.event.screen.f_call_entry.callEntryCaretaker;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on {date}
 *
 */
import jacob.common.AppLogger;
import jacob.common.Task;
import jacob.common.data.DataUtils;
import jacob.common.gui.object.Warranty;
import jacob.model.Calls;
import jacob.model.Callworkgroup;

import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
 /**
  * The Event handler for the CallGruppenruf-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author {author}
  *
  */
	public class CallGruppenruf extends IButtonEventHandler 
	{
		static public final transient String RCS_ID = "$Id: CallGruppenruf.java,v 1.17 2008/09/25 16:17:51 sonntag Exp $";
		static public final transient String RCS_REV = "$Revision: 1.17 $";
	 	public void onAction(IClientContext context, IGuiElement button) throws Exception
		{
	 		IDataTableRecord call = context.getSelectedRecord();
	 		if (call==null)
	 		{
	 			// Hack für defect 454: Muss nochmals nachgestellt werden.
				AppLogger.getLogger().error("context.getSelectedRecord liefert null obwohl der Button vom typ 'selected' ist.", new Exception(""));
				return;
			}	
	 		IDataTableRecord workgroup = call.getLinkedRecord("callworkgroup");
	 		if (!"Ja".equals(workgroup.getStringValue("groupconferencecall"))) 
	 		{
				alert("Der AK wünscht keinen Gruppenruf");
				return;
			}	
	 		String phone = workgroup.getSaveStringValue("phone");
	 		FormLayout layout = new FormLayout(
	 				"10dlu,p,p,10dlu,300dlu,10dlu", // 6 columns
					"20dlu,p,20dlu,p,10dlu,p,20dlu"); // 7 rows
	 		CellConstraints c=new CellConstraints();
	 		IFormDialog dialog=context.createFormDialog("Gruppenruf",layout, new DialogCallback(call));
	  	dialog.addLabel("Gruppenruf unter:",c.xywh(1,1,2,1));
	 		dialog.addLabel(phone,c.xy(4,1));
	 		dialog.addLabel("von:",c.xywh(1,3,2,1));
	 		dialog.addTextField("calledby","",c.xy(4,3));
	 	  dialog.setCancelButton("Abbruch");
	 	  dialog.addSubmitButton("yes","Angenommen");
	 	  dialog.addSubmitButton("no","Abgewiesen");
	 		dialog.show(450,150);
	}
	class DialogCallback implements IFormDialogCallback
	{
        private IDataTableRecord call;
        
        public DialogCallback(IDataTableRecord call )
        {
            this.call = call;
        }
		class YesNoDialogCallback implements IFormDialogCallback
		{
            private IDataTableRecord call;
            
            public YesNoDialogCallback(IDataTableRecord call )
            {
                this.call = call;
            }
			public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
			{
				if ("yes".equals(buttonId)) //Abgewiesen
				{
					addTask(context, call);
				}	
			}	
		}
		/* (non-Javadoc)
		 * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext, java.lang.String, java.util.Map)
		 */
		public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
		{
			
			String value=(String)formValues.get("calledby");	
			IDataTransaction trans = context.getDataAccessor().newTransaction();
			try 
			{
				if ("no".equals(buttonId)) //Abgewiesen
				{
          String problemtext = "Gruppenruf wurde abgewiesen von " + value;
          call.setStringValue(trans,"forwardbyphone","0");
					
          //Änderung Achim aufgrund Wunsch von SD
          //call.setStringValue(trans,"callstatus","AK zugewiesen");
          call.setStringValue(trans,"callstatus","Fehlgeroutet");
          String workgroupKey = DataUtils.getAppprofileValue(context.getDataAccessor().newAccessor(), "problemmanager_key");
          IDataTable workgroup = context.getDataTable(Callworkgroup.NAME);
          workgroup.qbeClear();
          workgroup.qbeSetKeyValue(Callworkgroup.pkey, workgroupKey);
          workgroup.search();
          if (workgroup.recordCount()==1)
          {
            call.setLinkedRecord(trans, workgroup.getRecord(0));
          }
          //Ende Änderung
          call.appendLongTextValue(trans,"problemtext",problemtext);
          // Information sollte auch in die Historie mit aufgenommen werden, da sich diese die Meister ausdrucken
          call.appendToHistory(trans,problemtext);
          trans.commit();
				}
				else //Angenommen
				{
          String problemtext = "Gruppenruf wurde angenommen von " + value;
					call.setStringValue(trans,"forwardbyphone","1");
					call.setStringValue(trans,"callstatus","Angenommen");
          call.appendLongTextValue(trans,"problemtext",problemtext);
          // Information sollte auch in die Historie mit aufgenommen werden, da sich diese die Meister ausdrucken
          call.appendToHistory(trans,problemtext);
          trans.commit();
          Warranty.checkWarranty(context,call);
          
					// Fragen ob Auftrag Angelegt werden soll
    	 		FormLayout layout = new FormLayout(
    	 				"10dlu,250dlu,10dlu", // 3 columns
    					"20dlu,p,20dlu"); // 3 rows
    	 		CellConstraints c=new CellConstraints();
    	 		IFormDialog dialog=context.createFormDialog("Auftrag anlegen",layout, new YesNoDialogCallback(call));
					dialog.addLabel("Möchten Sie für " + value + " einen Auftrag anlegen?",c.xy(1,1));
    	 	  dialog.addSubmitButton("yes","Ja");
    	 	  dialog.setCancelButton("Nein");
    	 	  
    	 	 dialog.show();
    	 	 /*
    	 	  
				  IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Möchten Sie für " + value + " einen Auftrag anlegen?",new IOkCancelDialogCallback()
				    {
				      public void onOk(IClientContext context) throws Exception
				      {      
				      	addTask(context);
				      }
				      public void onCancel(IClientContext context) throws Exception {}
				    });
				    okCancelDialog.show();
				 */
				}
			}
			finally 
			{
				trans.close();
			}
		}	
	}
	// Auftrag Anlegen
	private static void addTask(IClientContext context,IDataTableRecord agentCall ) throws Exception
	{
		context.setCurrentForm("f_call_manage", "taskadd");
		IDomain f_callmanage = (IDomain)context.getApplication().findByName("f_call_manage");
		f_callmanage.clear(context);
    // TODO: MIGRATION: prüfen ob funktioniert
    IDataAccessor accessor = context.getDataAccessor();
//    IDataAccessor accessor = f_callmanage.getDataAccessor();
		IDataTable calltable = accessor.getTable("call");
    calltable.qbeClear();
		calltable.qbeSetKeyValue("pkey", agentCall.getValue("pkey"));
		calltable.search();
		IRelationSet relationset = context.getApplicationDefinition().getRelationSet("r_call");
		accessor.propagateRecord(calltable.getRecord(0),relationset,Filldirection.BOTH);
		IDataTable taskTable = accessor.getTable("task");
		IDataTransaction tasktrans = taskTable.startNewTransaction();
		IDataTableRecord newtask = taskTable.newRecord(tasktrans);
		Task.setDefault(accessor,newtask,tasktrans);
		if (agentCall.getValue("object_key")!=null) 
			{
				IDataTableRecord object = agentCall.getLinkedRecord("object");
				newtask.setValue(tasktrans,"summary",object.getSaveStringValue("name"));
				
			}
	}
	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
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
			IDataTableRecord call = context.getSelectedRecord();
			String callstatus = call.getStringValue("callstatus");
			button.setEnable(callstatus.equals("AK zugewiesen"));
		}
	}
}

