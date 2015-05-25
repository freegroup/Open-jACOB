/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Dec 18 16:02:24 CET 2008
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.TableFieldExceptionCollection;
import jacob.common.AppLogger;
import jacob.model.Account;
import jacob.model.Helpdeskcategory;
import jacob.model.Project;
import jacob.model.Projectdatabase;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;
import com.hecc.jacob.util.exception.InvalidFieldValueException;

/**
 *
 * @author R.Spoor
 */
public class ProjectTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: ProjectTableRecord.java,v 1.4 2009/03/18 09:59:54 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	public ProjectTableRecord()
	{
	    super(
	        Project.created_by, Project.create_date,
	        Project.changed_by, Project.change_date
	    );
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	@Override
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
    @Override
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
    @Override
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
        super.beforeCommitAction(tableRecord, transaction);
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

        String database = tableRecord.getStringValue(Project.database);
		if (Project.active_ENUM._active.equals(tableRecord.getValue(Project.active)))
		{
		    IDataAccessor accessor = tableRecord.getAccessor().newAccessor();
		    IDataTable dbTable = accessor.getTable(Projectdatabase.NAME);
		    if (!dbTable.exists(Projectdatabase.database, database))
		    {
		        Context context = Context.getCurrent();
		        String message = I18N.PROJECT_INVALID_DATABASE.get(context);
		        throw new InvalidFieldValueException(tableRecord, Project.database, message);
		    }
		}
		String application = tableRecord.getStringValue(Project.application);
		if (application != null && application.length() > 0)
		{
		    IDataAccessor accessor = tableRecord.getAccessor().newAccessor();
		    IDataTable projectTable = accessor.getTable(Project.NAME);
		    projectTable.qbeClear();
		    projectTable.qbeSetKeyValue(Project.application, application);
		    projectTable.qbeSetValue(Project.database, "!=" + database);
		    int count = projectTable.search();
		    if (count != 0)
		    {
		        // there are projects with the same application but a different database
                Context context = Context.getCurrent();
                String message = I18N.PROJECT_DATABASE_APPLICATION_MISMATCH.get(context);
                TableFieldExceptionCollection collection = new TableFieldExceptionCollection(message);
                collection.add(new InvalidFieldValueException(tableRecord, Project.database, message));
                collection.add(new InvalidFieldValueException(tableRecord, Project.application, message));
                throw collection;
		    }
		}
		if (tableRecord.hasChangedValue(Project.project))
		{
		    IDataTableRecord accountRecord = tableRecord.getLinkedRecord(Account.NAME);
		    updateAccountProjectNames(transaction, accountRecord, tableRecord);
		}
	}

    static void updateAccountProjectNames(IDataTransaction transaction,
                                          IDataTableRecord accountRecord,
                                          IDataTableRecord projectRecord)
           throws InvalidExpressionException, NoSuchFieldException, RecordLockedException, Exception
    {
        IDataTableRecord rootCategory = getRootCategory(projectRecord);
        if (rootCategory == null)
        {
            return;
        }
        String name = accountRecord.getStringValue(Account.account) + " " +
        projectRecord.getStringValue(Project.project);
        rootCategory.setValue(transaction, Helpdeskcategory.name, name);
        rootCategory.setValue(transaction, Helpdeskcategory.longname, "/" + name + "/");
        updateSubCategories(transaction, rootCategory);
    }

    private static void updateSubCategories(IDataTransaction transaction,
                                            IDataTableRecord categoryRecord)
            throws InvalidExpressionException, NoSuchFieldException, RecordLockedException
    {
        IDataAccessor accessor = categoryRecord.getAccessor().newAccessor();
        IDataTable categoryTable = accessor.getTable(Helpdeskcategory.NAME);
        categoryTable.qbeClear();
        Object key = categoryRecord.getValue(Helpdeskcategory.pkey);
        categoryTable.qbeSetKeyValue(Helpdeskcategory.parentcategory_key, key);
        int count = categoryTable.search();
        String longName = categoryRecord.getStringValue(Helpdeskcategory.longname);
        for (int i = 0; i < count; i++)
        {
            IDataTableRecord childRecord = categoryTable.getRecord(i);
            String name = childRecord.getStringValue(Helpdeskcategory.name);
            childRecord.setValue(transaction, Helpdeskcategory.path, longName);
            childRecord.setValue(transaction, Helpdeskcategory.longname, longName + name + "/");
            updateSubCategories(transaction, childRecord);
        }
    }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
    @Override
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}

    /**
     * Returns the root category record for a project record.
     *
     * @param projectRecord The project record for which to return the root
     *        category record.
     * @return The root category.
     * @throws InvalidExpressionException If any value is invalid
     */
    public static IDataTableRecord getRootCategory(IDataTableRecord projectRecord)
                  throws InvalidExpressionException
    {
        try
        {
            IDataAccessor accessor = projectRecord.getAccessor().newAccessor();
            IDataTable categoryTable = accessor.getTable(Helpdeskcategory.NAME);
            categoryTable.qbeClear();
            projectRecord.getStringValue(Project.project);
            Object key = projectRecord.getValue(Project.pkey);
            categoryTable.qbeSetKeyValue(Helpdeskcategory.project_key, key);
            categoryTable.setMaxRecords(1);
            int count = categoryTable.search();
            return (count == 0 ? null : categoryTable.getRecord(0));
        }
        catch (NoSuchFieldException e)
        {
            // will not happen
            return null;
        }
    }
}
