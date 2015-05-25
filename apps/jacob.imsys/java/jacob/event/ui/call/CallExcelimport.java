package jacob.event.ui.call;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Jun 20 17:01:01 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.exception.BusinessException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the CallExcelimport-Button. <br>
 * The onAction will be calle if the user clicks on this button <br>
 * Insert your custom code in the onAction-method. <br>
 * 
 * @author mike
 * 
 */
/**
 * @author mike
 * 
 */
public class CallExcelimport extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CallExcelimport.java,v 1.6 2005/12/06 15:50:49 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.6 $";

    static private final short STARTROW = 0;

    static private final short ERRORCOL = 17;

    static private final short ESTIMATEDSTARTDATE = 0;

    static private final short ESTIMATEDSTARTHOUR = 1;

    static private final short ESTIMATEDDONEHOUR = 2;

    static private final short LOCATION = 3;

    static private final short ORDERNO = 4;

    static private final short ORDERTYPE = 5;

    static private final short PRODUCT = 12; // Bestellung in Problemtext

    static private final short CUSTOMER = 7;

    static private final short ACCOUNTINGCODE = 9;

    static private final short SITE_PERSONALNO = 10;

    static private final short AK_KEY = 14;

    static private final short CATEGORY_KEY = 13;

    // Konstanten, für die Meldung selbst
    static private final String PROCESS_KEY = "10";

    static private class RecordDataType
    {
        public String employeecall;

        public String locationnote;

        public String problem;

        public String problemtext;

        public String acountingcodetext;

        public String Estimatedstart;

        public String EstimatedDone;

        public String workgroupcall;

        public String custtext;

        public String categorycall;

        public String toString()
        {
            return "employeecall=" + employeecall + "\n" + "locationnote=" + locationnote + "\n" + "problem=" + problem + "\n" + "problemtext=" + problemtext + "\n"
                    + "acountingcodetext=" + acountingcodetext + "\n" + "Estimatedstart=" + Estimatedstart + "\n" + "EstimatedDone=" + EstimatedDone + "\n" + "workgroupcall="
                    + workgroupcall + "\n" + "custtext=" + custtext + "\n" + "categorycall=" + categorycall + "\n";
        }
    }

    // use this logger to write messages and NOT the System.println(..) ;-)
    static private final transient Log logger = AppLogger.getLogger();

    protected class ExcelFileCallBack implements IUploadDialogCallback
    {

        /*
         * (non-Javadoc)
         * 
         * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
         */
        public void onCancel(IClientContext context) throws Exception
        {
            // do nothing

        }

        /*
         * (non-Javadoc)
         * 
         * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onOk(de.tif.jacob.screen.IClientContext,
         *      java.lang.String, byte[])
         */
        public void onOk(IClientContext context, String fileName, byte[] excelDocument) throws Exception
        {

            HSSFWorkbook wb = null;
            HSSFSheet sheet = null;;
            HSSFRow row= null;
            try
            {
                POIFSFileSystem fs = new POIFSFileSystem(new ByteArrayInputStream(excelDocument));
                wb = new HSSFWorkbook(fs);
                sheet = wb.getSheetAt(0);
                row = sheet.getRow(0);
            }
            catch (Exception e)
            {
                throw new UserException("Fehler beim Öffnen des Excel-Dokuments");
            }
            short i = 0;
            IDataAccessor newAccessor = context.getDataAccessor().newAccessor();
            while (row != null)
            {
                processRow(context, newAccessor, row);
                i++;
                row = sheet.getRow(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            out.close();
            context.createDocumentDialog(null, "Ergebnis.xls", out.toByteArray()).show();

        }
    }

    /**
     * The user has been click on the corresponding button. <br>
     * Be in mind: The currentRecord can be null if the button has not the
     * [selected] flag. <br>
     * The selected flag warranted that the event can only be fired if the <br>
     * selectedRecord!=null. <br>
     * 
     * 
     * @param context
     *            The current client context
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onAction(IClientContext context, IGuiElement button) throws Exception
    {

        context.createUploadDialog(new ExcelFileCallBack()).show();

    }

    /**
     * The status of the parent group (TableAlias) has been changed. <br>
     * <br>
     * This is a good place to enable/disable the button on relation to the
     * group state or the selected record. <br>
     * <br>
     * Possible values for the state is defined in IGuiElement <br>
     * <ul>
     * <li>IGuiElement.UPDATE</li>
     * <li>IGuiElement.NEW</li>
     * <li>IGuiElement.SEARCH</li>
     * <li>IGuiElement.SELECTED</li>
     * </ul>
     * 
     * Be in mind: The currentRecord can be null if the button has not the
     * [selected] flag. <br>
     * The selected flag warranted that the event can only be fired if the <br>
     * selectedRecord!=null. <br>
     * 
     * @param context
     *            The current client context
     * @param status
     *            The new group state. The group is the parent of the
     *            corresponding event button.
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        // You can enable/disable the button in relation to your conditions.
        //
        // button.setEnable(true/false);
    }

    /**
     * konvertiert ein Exceldate-Feld in ein Calendar Object
     * 
     * @param cell
     * @return Calendar
     */
    /**
     * @param cell
     * @return
     * @throws Exception
     */
    public static Calendar getCalendarValue(HSSFCell cell) throws Exception
    {
        if (cell == null)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(HSSFDateUtil.getJavaDate(0));
            return cal;
        }
        double d = cell.getNumericCellValue();
        // test if a date!
        if (!HSSFDateUtil.isCellDateFormatted(cell))
            throw new Exception("Cell is not Date formatted");
        Calendar cal = Calendar.getInstance();
        cal.setTime(HSSFDateUtil.getJavaDate(d));
        return cal;
    }

    /**
     * sucht den Datensaz des Kunden
     * 
     * @param context
     * @param row
     * @return pkey des Kunden
     * @throws Exception
     */
    public static String getCustomerKey(IDataAccessor newAccessor, HSSFRow row) throws Exception
    {

        String fullName = row.getCell(CUSTOMER).getStringCellValue();
        String name[] = fullName.split(", ");
        String firstName = null;
        String lastName = null;
        if (name.length == 2)
        {
            firstName = name[1].toUpperCase();
            lastName = name[0].toUpperCase();
        }
        else
        {
            throw new BusinessException("Name besteht nicht aus 'Nachname, Vorname': " + fullName);
        }
        IDataTable customer = newAccessor.getTable("customerint");
        customer.qbeSetValue("firstnamecorr", firstName);
        customer.qbeSetValue("lastnamecorr", lastName);
        customer.qbeSetValue("employeeid", cellToString(row.getCell(SITE_PERSONALNO)));
        customer.search();
        if (customer.recordCount() != 1)
        {
            throw new BusinessException(fullName + " nicht eindeutig in der Datenbank gefunden");
        }

        return customer.getRecord(0).getSaveStringValue("pkey");
    }

    /**
     * gibt den Inhalt der Zelle als String zurück
     * 
     * @param cell
     * @return Inhalt der Excelzelle als String
     */
    public static String cellToString(HSSFCell cell)
    {
        if (cell == null)
            return "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
            return Long.toString(Math.round(cell.getNumericCellValue()));
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
            return cell.getStringCellValue();

        return "unknown cellformat";
    }

    /**
     * @param context
     * @param currentTransaction
     * @param record
     * @param foreignKeyName
     * @param alias
     * @param primaryKeyName
     * @param primaryKeyValue
     * @throws Exception
     */
    public static void linkTable(IDataAccessor newAccessor, IDataTransaction currentTransaction, IDataTableRecord record, String foreignKeyName, String alias,
            String primaryKeyName, String primaryKeyValue) throws Exception
    {

        IDataTable table = newAccessor.getTable(alias);
        table.qbeSetKeyValue(primaryKeyName, primaryKeyValue);
        if (table.search() == 1)
        {
            record.setValue(currentTransaction, foreignKeyName, primaryKeyValue);
        }
        else
        {
            throw new BusinessException(alias + " mit " + primaryKeyName + "= " + primaryKeyValue + " nicht gefunden");
        }

    }

    /**
     * Legt die Meldung an
     * 
     * @param context
     * @param recordData
     * @param row
     * @throws Exception
     */
    public static void createCall(IClientContext context, IDataAccessor newAccessor, RecordDataType recordData, HSSFRow row) throws Exception
    {
        IDataTable callTable = newAccessor.getTable("call");
        IDataTable locationTable = newAccessor.getTable("location");
        IDataTransaction trans = callTable.startNewTransaction();
        try
        {
            IDataTableRecord location = locationTable.newRecord(trans);
            location.setValue(trans, "note", recordData.locationnote);
            IDataTableRecord call = callTable.newRecord(trans);
            // Ort
            call.setValue(trans, "location_key", location.getValue("pkey"));
            linkTable(newAccessor, trans, call, "workgroupcall", "callworkgroup", "pkey", recordData.workgroupcall);
            linkTable(newAccessor, trans, call, "categorycall", "category", "pkey", recordData.categorycall);
            linkTable(newAccessor, trans, call, "process_key", "process", "pkey", PROCESS_KEY);
            call.setValue(trans, "employeecall", recordData.employeecall);
            call.setValue(trans, "custtext", recordData.custtext);
            call.setValue(trans, "problem", recordData.problem);
            call.setValue(trans, "problemtext", recordData.problemtext);
            call.setValue(trans, "accountingcodetext", recordData.acountingcodetext);
            call.setValue(trans, "estimatedstart", recordData.Estimatedstart);
            call.setValue(trans, "estimateddone", recordData.EstimatedDone);
            call.setValue(trans, "agentcall", context.getUser().getKey());
            trans.commit();
            // Meldungsnummer in Excel speichern
            HSSFCell infoCell = row.createCell(ERRORCOL);
            infoCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            infoCell.setCellValue("Meldung;" + call.getSaveStringValue("pkey"));
        }
        finally
        {
            trans.close();
        }
    }

    public static boolean processRow(IClientContext context, IDataAccessor newAccessor, HSSFRow row)
    {
        try
        {

            newAccessor.clear();
            // estimated Start and done
            Calendar estimatedDay = getCalendarValue(row.getCell(ESTIMATEDSTARTDATE));
            Calendar startHour = getCalendarValue(row.getCell(ESTIMATEDSTARTHOUR));
            Calendar endHour = getCalendarValue(row.getCell(ESTIMATEDDONEHOUR));
            RecordDataType recordData = new RecordDataType();
            String day = estimatedDay.get(Calendar.DAY_OF_MONTH) + "." + (estimatedDay.get(Calendar.MONTH) + 1) + "." + estimatedDay.get(Calendar.YEAR) + " ";
            recordData.Estimatedstart = day + startHour.get(Calendar.HOUR_OF_DAY) + ":" + startHour.get(Calendar.MINUTE);
            recordData.EstimatedDone = day + endHour.get(Calendar.HOUR_OF_DAY) + ":" + endHour.get(Calendar.MINUTE);
            // custText bauen
            String orderNo = cellToString(row.getCell(ORDERNO)); // Format
            // IBYYYYMMDDXXX
            recordData.custtext = "Bewirtungsservice für den " + day + "(" + orderNo + ")";

            recordData.locationnote = cellToString(row.getCell(LOCATION));
            recordData.employeecall = getCustomerKey(newAccessor, row);
            recordData.problem = recordData.locationnote;
            recordData.problemtext = cellToString(row.getCell(PRODUCT));
            recordData.workgroupcall = cellToString(row.getCell(AK_KEY));
            recordData.acountingcodetext = cellToString(row.getCell(ACCOUNTINGCODE));
            recordData.categorycall = cellToString(row.getCell(CATEGORY_KEY));
            createCall(context, newAccessor, recordData, row);

            return true;

        }
        catch (Exception e)
        {
            HSSFCell errorCell = row.createCell(ERRORCOL);
            errorCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            errorCell.setCellValue(e.toString());
            return false;
        }
    }

}
