/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jun 18 17:50:45 CEST 2007
 */
package jacob.common.gui.object;

import jacob.common.AppLogger;
import jacob.common.sap.CsvReader;
import jacob.model.Sapadmin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the ObjectGenericButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class ObjectGenericButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ObjectGenericButton.java,v 1.2 2007/08/08 11:38:30 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();
	
	static class ReadCSVCallback implements IUploadDialogCallback
	{
  	public ReadCSVCallback()
    {

    }

    /* 
     * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext arg0)
    {
      //ignore
    }

    /* 
     * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onOk(de.tif.jacob.screen.IClientContext, java.lang.String, byte[])
     */   
    public void onOk(IClientContext context, final String fileName, final byte[] fileData) throws Exception
    {
    	
    	
    	  //  attachment.setValue(trans, "document",fileData);
    	InputStream is = new ByteArrayInputStream(fileData);
    	Reader r = new InputStreamReader(is);
    	char delimiter = ';';
    	IDataAccessor acc = context.getDataAccessor();
		IDataTable sapadmin = acc.getTable(Sapadmin.NAME);
		sapadmin.qbeClear();
		sapadmin.qbeSetValue(Sapadmin.active,"1");
		sapadmin.search();
		IDataTableRecord sapadminrec;
		int prevcount = 25;
		String sDefHeader ="";
		if (sapadmin.recordCount()==1) 
		{
			sapadminrec = sapadmin.getRecord(0);
			prevcount=sapadminrec.getintValue(Sapadmin.obj_preview_records);
			sDefHeader = sapadminrec.getSaveStringValue(Sapadmin.objectheader);
		}
		else 
		{
		throw new UserException("SAP Konfigurationsdatensatz nicht gefunden oder nicht eindeutig");
		}
		
		CsvReader reader=new CsvReader(r, delimiter);//"/Users/achim/Desktop/products.csv"
		reader.readHeaders();
		
		
//		System.out.println("col:"+reader.getColumnCount());
//		System.out.println("Head:"+reader.getHeaderCount());
		StringBuffer checkheader = new StringBuffer();
		String[] header = new String[reader.getHeaderCount()];
		String [][] data = new String[prevcount][reader.getHeaderCount()];
		for (int i = 0; i < reader.getHeaderCount(); i++) {
			header[i]=reader.getHeader(i);
			checkheader.append(reader.getHeader(i));
			if (i<reader.getHeaderCount()-1) 
			{
				checkheader.append(";");
			}
			
		}
		if (!checkheader.toString().equals(sDefHeader)) 
		{
			throw new UserException("Die Definition des Objektimports stimmt nicht mit der Importquelle überein");
		
		}
		int j=0;
		
		
			while (reader.readRecord())
			
			{
				System.out.println("InRead");
				for (int i = 0; i < reader.getHeaderCount(); i++) {
					data[j][i]=reader.get(i);
					System.out.println(reader.get(i));
	
				}
				if (j==prevcount-1) //
				{
					break;
				}
				else {
					j=j+1;
				}

			}


		IGridTableDialog dialog1 =context.createGridTableDialog(context.getGroup(),new MyCallback());
 		dialog1.setData(data);
 		dialog1.setHeader(header);
 		dialog1.show();
		reader.close();
    }
	class MyCallback implements IGridTableDialogCallback
		{
			public void onSelect(IClientContext context, int index, Properties values) throws Exception 
			{
				IMessageDialog dialog = context.createMessageDialog("Hier machen wir noch was, \\r\\njetzt passiert erst mal nichts");
				dialog.show();
			}
		}
  }
	
	
	


	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		
	    IDialog dialog;
		dialog=context.createUploadDialog(new ReadCSVCallback());
		dialog.show();
		
		
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}
