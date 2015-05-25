/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Feb 12 15:35:38 CET 2009
 */
package jacob.event.data;

import java.text.MessageFormat;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import jacob.common.AppLogger;
import jacob.model.Country;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;

/**
 *
 * @author R.Spoor
 */
public class CountryTableRecord extends CreateChangeRecordEventHandler
{
    static public final transient String RCS_ID = "$Id: CountryTableRecord.java,v 1.2 2009/02/24 15:55:51 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The format for creating the search field.
     */
    private static final String SEARCH_FORMAT = "{0} ({1})";

    public CountryTableRecord()
    {
        super(
            Country.created_by, Country.create_date,
            Country.changed_by, Country.change_date
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

        String country = tableRecord.getStringValue(Country.country);
        String alpha2 = tableRecord.getStringValue(Country.alpha2);
        String search = MessageFormat.format(SEARCH_FORMAT, country, alpha2);
        tableRecord.setStringValue(transaction, Country.countrysearch, search);
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    @Override
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}
