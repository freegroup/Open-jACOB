/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jan 15 12:39:59 CET 2010
 */
package jacob.event.data;

import jacob.model.Userproperty;
import de.tif.jacob.cluster.ClusterManager;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andreas
 */
public class UserpropertyTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: UserpropertyTableRecord.java,v 1.1 2010/01/20 02:04:26 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
    // nothing to do here
	}

	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
    // nothing to do here
	}

	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
    // nothing to do here
	}

	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	  // make property change immediately available within user sessions, if an
    // administrator changes a user-specific setting (e.g. max records)
    String propertyName = tableRecord.getStringValue(Userproperty.name);
    ClusterManager.getProvider().propagatePropertyChange(propertyName);
	}
}
