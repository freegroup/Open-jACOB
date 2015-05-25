/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Feb 12 15:39:48 CET 2009
 */
package jacob.event.data;

import java.text.MessageFormat;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import jacob.common.AppLogger;
import jacob.model.Language;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;

/**
 *
 * @author R.Spoor
 */
public class LanguageTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: LanguageTableRecord.java,v 1.2 2009/02/24 15:55:51 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

    /**
     * The format for creating the search field.
     */
    private static final String SEARCH_FORMAT = "{0} ({1})";

    public LanguageTableRecord()
    {
        super(
            Language.created_by, Language.create_date,
            Language.changed_by, Language.change_date
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

	    String language = tableRecord.getStringValue(Language.language);
	    String code1 = tableRecord.getStringValue(Language.code1);
	    String search = MessageFormat.format(SEARCH_FORMAT, language, code1);
	    tableRecord.setStringValue(transaction, Language.languagesearch, search);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
    @Override
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}
