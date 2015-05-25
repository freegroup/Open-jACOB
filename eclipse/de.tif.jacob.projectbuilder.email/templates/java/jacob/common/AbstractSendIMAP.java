/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 07 21:28:45 CET 2006
 */
package jacob.common;

import jacob.model.Configuration;
import jacob.model.Contact;
import jacob.util.imap.FolderUtil;
import jacob.util.imap.SendFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.Message;

import org.apache.commons.logging.Log;

import com.sun.mail.imap.IMAPFolder;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormActionEmitter;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IAutosuggestProvider;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.event.IAutosuggestProvider.AutosuggestItem;
import de.tif.jacob.util.StringUtil;


/**
 * The event handler for the DefaultName generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public abstract class AbstractSendIMAP extends IButtonEventHandler 
{
	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	static public final transient String RCS_ID = "$Id: AbstractSendIMAP.java,v 1.1 2007/11/25 22:12:37 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	IAutosuggestProvider provider = new IAutosuggestProvider() {
		
		public void suggestSelected(IClientContext context,
				AutosuggestItem selectedEntry) throws Exception {
		}
	
		public AutosuggestItem[] suggest(Context context, String userInputFragment, int caretPosition)throws Exception {
			// split: any@juhu.de; nobody@google.de; anybody@google.de
			int index =userInputFragment.lastIndexOf(';');
			Set result = new TreeSet();
			
			// we have only the focus on the last email address
			//
			String searchPart =userInputFragment;
			String firstPart  = "";
			if(index!=-1)
			{
				searchPart = userInputFragment.substring(index+1);
				firstPart  = userInputFragment.substring(0,index+1);
			}
			System.out.println(firstPart);
			
			IDataTable contact = context.getDataAccessor().newAccessor().getTable(Contact.NAME);
			
			if(searchPart==null || searchPart.length()==0)
			{
				// unconstraint search
			}
			else
			{
				// remove start/stop spaces
				searchPart = searchPart.trim();
				// We are searching in private_email OR work_email => '|'
				// and enforced left anchored => '^'
				//
				contact.qbeSetValue(Contact.privat_email,"|^"+searchPart);
				contact.qbeSetValue(Contact.work_email,"|^"+searchPart);
			}
			
			contact.search();
			for(int i=0; i<contact.recordCount();i++)
			{
				String privateMail = contact.getRecord(i).getSaveStringValue(Contact.privat_email);
				String workMail = contact.getRecord(i).getSaveStringValue(Contact.work_email);
				if(privateMail.length()>0 && privateMail.indexOf(searchPart)!=-1)
					result.add(privateMail);
				if(workMail.length()>0 && workMail.indexOf(searchPart)!=-1)
					result.add(workMail);
			}
			

			AutosuggestItem[] items = new AutosuggestItem[result.size()];
			index=0;
			Iterator iter = result.iterator();
			while (iter.hasNext()) 
			{
				String element = (String) iter.next();
				element = firstPart+element;
				items[index++]= new AutosuggestItem(element,element);
				
			}
			return items;
		}
	};

	final class DialogCallback implements IFormDialogCallback
	{
		public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception 
		{
			if(buttonId.equals("send"))
			{
				String smtp_server    = (String)context.getUser().getProperty(Configuration.smtp_server);
				String imap_server    = (String)context.getUser().getProperty(Configuration.imap_server);
				String email     = context.getUser().getEMail();
				String username  = (String)context.getUser().getProperty(Configuration.user);
				String password  = (String)context.getUser().getProperty(Configuration.password);
				if(smtp_server!=null && username!=null && password!=null && email!=null)
				{
					String[] to      = getValidEmails((String)formValues.get("to"));
					String[] cc      = getValidEmails((String)formValues.get("cc"));
          //save emailaddresses if wanted
          Integer save_outgoing_emailaddress = (Integer) context.getUser().getProperty(Configuration.save_outgoing_emailaddress);
          if (save_outgoing_emailaddress.intValue() == 1)
          {
            for (int i = 0; i < cc.length; i++)
            {
              EmailAddress.add(context, cc[i]);
            }
            for (int i = 0; i < to.length; i++)
            {
              EmailAddress.add(context, to[i]);
            }
          }
					String   subject = (String)formValues.get("subject");
					String   body    = (String)formValues.get("body");
					DataDocumentValue attachment01 = (DataDocumentValue)formValues.get("attachment01");
					DataDocumentValue attachment02 = (DataDocumentValue)formValues.get("attachment02");
					
					List attachments = new ArrayList();
					if(attachment01!=null)
						attachments.add(attachment01);
					if(attachment02!=null)
						attachments.add(attachment02);
					if(subject== null || subject.length()==0)
						subject = "-no subject-";
					if(body==null)
						body="";
					Message msg =SendFactory.send(smtp_server,username,password,email,to,cc,subject,body,(DataDocumentValue[])attachments.toArray(new DataDocumentValue[0]));
					
			    // Das senden der eMail hat geklappt. Jetzt wird die eMail in dem 'sent' Folder
			    // gespeichert den der Benutzer eingestellt hat (wenn er den einen eingetragen hat).
			    //
					if(msg!=null)
					{
						String outFolderName =  (String)context.getUser().getProperty(Configuration.folder_out);
						if(outFolderName!=null && outFolderName.length()>0)
						{
							IMAPFolder folder = FolderUtil.ensureFolderAndOpen(imap_server,username,password,outFolderName);
							folder.appendMessages(new Message[]{msg});
							folder.getStore().close();
						}
					}
				}
				else
				{
					alert("missing configuration data. Send eMail not possible");
				}
			}
		}
	}
	
	final class AddToEmitter implements IFormActionEmitter
	{
		public void onAction(IClientContext context, IFormDialog parent, Map formValues) throws Exception 
		{
			String to   = (String)formValues.get("to");
			String name = (String)formValues.get("name");
			
			if(name==null || name.length()==0)
				return;
			
			if(to.length()!=0)
				to = to+";";
			to = to+name;
			formValues.put("to",to);
		}
	}
	
	final class AddCCEmitter implements IFormActionEmitter
	{
		public void onAction(IClientContext context, IFormDialog parent, Map formValues) throws Exception 
		{
			String to   = (String)formValues.get("cc");
			String name = (String)formValues.get("name");
			
			if(name==null || name.length()==0)
				return;
			
			if(to.length()!=0)
				to = to+";";
			to = to+name;
			formValues.put("cc",to);
			System.out.println(formValues.get("attachment01"));
		}
	}
	

 
	
	protected final void showDialog(IClientContext context,String from, String to, String cc, String subject, String body) throws Exception
	{
    FormLayout layout = new FormLayout("10dlu,150dlu,50dlu,pref,4dlu,grow,10dlu", // 3 columns
    "10dlu,20dlu,4dlu,20dlu,4dlu,20dlu,4dlu,20dlu,4dlu,grow,20dlu,20dlu,10dlu"); // 5 rows
		CellConstraints c=new CellConstraints();
		
		IFormDialog dialog=context.createFormDialog("title",layout,new DialogCallback());
		
		dialog.addLabel("Address Book", c.xy(1,1));
		dialog.addListBox("name",getAddresses(context),0, c.xywh(1,3,1,7));
		dialog.addFormButton(new AddToEmitter(),"Add to To:",c.xy(1,10));
		dialog.addFormButton(new AddCCEmitter(),"Add to CC:",c.xy(1,11));
		
		
		dialog.addLabel("From:",c.xy(3,1));     dialog.addTextField("from"   ,from,true,c.xy(5,1));
		dialog.addLabel("To:",c.xy(3,3));       dialog.addTextField("to"     ,to,provider,c.xy(5,3));
		dialog.addLabel("Cc:",c.xy(3,5));       dialog.addTextField("cc"     ,cc,provider,c.xy(5,5));
		dialog.addLabel("Subject:",c.xy(3,7));  dialog.addTextField("subject",subject,c.xy(5,7));
		
		dialog.addTextArea("body", body,c.xy(5,9));
		
		dialog.addFileUpload("attachment01",c.xy(5,10) );
		dialog.addFileUpload("attachment02",c.xy(5,11) );
		dialog.setCancelButton("Cancel");
		dialog.addSubmitButton("send","Send");
		//dialog.setDebug(true);
		dialog.show(700,500);
	}
	
	private String[] getAddresses(IClientContext context) throws Exception
	{
		Set result = new TreeSet();
		IDataTable contact = context.getDataAccessor().newAccessor().getTable(Contact.NAME);
		
		contact.search();
		for(int i=0; i<contact.recordCount();i++)
		{
			String privateMail = contact.getRecord(i).getSaveStringValue(Contact.privat_email);
			String workMail = contact.getRecord(i).getSaveStringValue(Contact.work_email);
			if(privateMail.length()>0)
				result.add(privateMail);
			if(workMail.length()>0)
				result.add(workMail);
		}
		
		return (String[])result.toArray(new String[0]);
	}
	
	private static String[] getValidEmails(String emailList)
	{
		String[] splitted = emailList.split(";");
		Set result = new TreeSet(); 
		for (int i = 0; i < splitted.length; i++) 
		{
			String current =splitted[i].trim();
			if(current.length()>0)
				result.add(current);
		}
		return (String[])result.toArray(new String[0]);
	}
}
