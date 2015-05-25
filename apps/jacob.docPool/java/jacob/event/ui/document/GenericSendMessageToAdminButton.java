/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 06 16:50:00 CEST 2010
 */
package jacob.event.ui.document;

import jacob.common.DocumentUtil;
import jacob.model.Account;
import jacob.model.Document;

import java.net.URLEncoder;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the DocumentSendMessageToAdminButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class GenericSendMessageToAdminButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: GenericSendMessageToAdminButton.java,v 1.2 2010-07-16 14:26:15 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";


	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
	  // Button wurde zu einem Link gemacht. Diese Methode wird somit nicht mehr gerufen.
	}
   

	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement element) throws Exception
	{
	  IDataTableRecord docRecord = context.getDataTable(Document.NAME).getSelectedRecord();
	  if(docRecord!=null)
	  {
	    
      String name = URLEncoder.encode(docRecord.getSaveStringValue(Document.name),"ISO-8859-1");
  	  String url  =URLEncoder.encode(DocumentUtil.getUrl(context, docRecord),"ISO-8859-1");
  	  String to="";
  	  String cc="";
  	  
	    IDataAccessor acc = context.getDataAccessor().newAccessor();
	    IDataTable accountTable = acc.getTable(Account.NAME);
      accountTable.qbeSetKeyValue(Account.role, Account.role_ENUM._administrator);
      accountTable.qbeSetValue(Account.email, "!null");
	    accountTable.search(IRelationSet.LOCAL_NAME);
	    if(accountTable.recordCount()>0)
	    {
	      to = accountTable.getRecord(0).getSaveStringValue(Account.email);
        for (int i = 1; i < accountTable.recordCount(); i++)
        {
          if(cc.length()>0)
            cc=cc+", ";
          cc= cc+accountTable.getRecord(i).getSaveStringValue(Account.email);
        }
	    }
  	  String subject = "Beanstandung%20von%20"+name;
  	  String body = "Guten%20Tag%0D%0A%0D%0AIch%20beanstande%20das%20genannte%20Dokument%20%0D%0A%0D%0ADokument:%20"+url+"%0D%0AGrund%20der%20Beanstandung:%20%0D%0A%0D%0A";
  	  IButton button = (IButton)element;
      button.setLink("mailto:"+to+"?subject="+subject+"&amp;body="+body+"&amp;cc="+cc);
	  }
	}
}

