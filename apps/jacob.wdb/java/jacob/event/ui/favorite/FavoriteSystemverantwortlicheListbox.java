/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 22:49:57 CEST 2010
 */
package jacob.event.ui.favorite;

import jacob.model.Administrator;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IMutableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableListBoxEventHandler;

/**
 *
 * @author andherz
 */
public class FavoriteSystemverantwortlicheListbox extends IMutableListBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: FavoriteSystemverantwortlicheListbox.java,v 1.1 2010-08-17 22:02:39 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


	/**
	 * Called, if the user changed the selection during the NEW or UPDATE state 
	 * of the related table record.
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param emitter The emitter of the event.
	 */
	public void onSelect(IClientContext context, IMutableListBox emitter) throws Exception
	{
		// your code here
	}
  
  public void onGroupStatusChanged(IClientContext context, GroupState state, IMutableListBox listBox) throws Exception
	{
    listBox.removeOptions();
    listBox.setEnable(false);
    
    IDataTable adminTable = context.getDataAccessor().newAccessor().getTable(Administrator.NAME);
    adminTable.search(IRelationSet.LOCAL_NAME);
    if(adminTable.recordCount()==0)
    {
      listBox.addOption("- keine Systemverantwortliche hinterlegt -");
    }
    else
    {
      for(int i=0; i<adminTable.recordCount();i++)
      {
        IDataTableRecord admin = adminTable.getRecord(i);
        String name = admin.getSaveStringValue(Administrator.fullname);
        String email = admin.getStringValue(Administrator.email);
        if(email==null)
          email = "-keine email hinterlegt-";
        listBox.addOption(name+" <"+email+">");
      }
    }
	}
}
