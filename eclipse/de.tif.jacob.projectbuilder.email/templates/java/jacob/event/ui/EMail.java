/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 30 19:34:13 CET 2006
 */
package jacob.event.ui;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;

import jacob.common.AppLogger;
import jacob.model.Email;
import jacob.util.imap.FolderUtil;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationPanel;

/**
 *
 * @author andherz
 */
public class EMail extends IDomainEventHandler
{
	static public final transient String RCS_ID = "$Id: EMail.java,v 1.1 2007/11/25 22:12:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	static int counter=0;
	
	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  
  public INavigationPanel getNavigationPanel(IClientContext context, IDomain domain) throws Exception 
  {
		return new INavigationPanel() 
		{
			public INavigationEntry[] getNavigationEntries(IClientContext context, IDomain domain)
			{
				try 
				{
					// Die Navigation darf nicht den Rest beeinflussen
					//
					IDataAccessor acc = context.getDataAccessor().newAccessor(); 
					IDataTable folderTable = acc.getTable(jacob.model.Folder.NAME);
					if(folderTable.search()==0)
					{
						INavigationEntry[] entries = new INavigationEntry[1];
						String label = "INBOX ("+getUnreadMessages(context,"INBOX")+"/"+getMessages(context,"INBOX")+")";
						entries[0]=new INavigationEntry(domain,"unused",label,"showfolder", "INBOX");
						return entries;
					}
					else
					{
						List entries = new ArrayList();
						String label = "INBOX ("+getUnreadMessages(context,"INBOX")+"/"+getMessages(context,"INBOX")+")";
						entries.add(new INavigationEntry(domain,"unused",label,"showfolder", "INBOX"));
						for(int i=0; i<folderTable.recordCount();i++)
						{
							IDataTableRecord folder = folderTable.getRecord(i);
							String name     = folder.getStringValue(jacob.model.Folder.name);
							String fullname = folder.getStringValue(jacob.model.Folder.url);
							if(!"INBOX".equals(name))
							{
								// Die Anzahl der ungesenen eMails ermitteln
								label = name+" ("+getUnreadMessages(context,fullname)+"/"+getMessages(context,fullname)+")";
								entries.add(new INavigationEntry(domain,"unused",label,"showfolder", fullname));
							}
						
						}
						return (INavigationEntry[])entries.toArray(new INavigationEntry[0]);
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					return new INavigationEntry[0];
				}
			}
		};

	}


	public void onNavigation(IClientContext context, IDomain domain, String event, String value) throws Exception
	{
    context.setCurrentForm("eMail", "email");

    context.getDataAccessor().qbeClearAll();
    IDataBrowser browser = context.getDataBrowser("emailBrowser");
		IDataTable emailTable = context.getDataTable(Email.NAME);
		
		emailTable.qbeSetValue(Email.folder,value);
		browser.search("emailRelationset",Filldirection.BOTH);
		
		context.getGUIBrowser().setData(context,browser);
	}


	/**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param group The hidden group
   */
  public void onShow(IClientContext context, IDomain domain) throws Exception
  {
  	context.getApplication().setSearchBrowserVisible(true);
		Boolean initialSearch = (Boolean)context.getUser().getProperty(Application.INITIAL_INBOX);
		if(initialSearch==Boolean.TRUE)
		{
			onNavigation(context, domain, "","INBOX");
			context.getUser().setProperty(Application.INITIAL_INBOX,Boolean.FALSE);
		}
  }
  
  private long getUnreadMessages(IClientContext context,String folder) throws Exception
  {
  	IDataAccessor acc= context.getDataAccessor().newAccessor();
  	IDataTable table = acc.getTable(Email.NAME);
  	table.qbeSetValue(Email.folder,folder);
  	table.qbeSetValue(Email.seen,"0");
  	
  	return table.count();
  }
  
  private long getMessages(IClientContext context,String folder) throws Exception
  {
  	IDataAccessor acc= context.getDataAccessor().newAccessor();
  	IDataTable table = acc.getTable(Email.NAME);
  	table.qbeSetValue(Email.folder,folder);
  	
  	return table.count();
  }
}
