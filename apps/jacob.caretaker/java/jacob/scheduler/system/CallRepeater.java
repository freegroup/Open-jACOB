package jacob.scheduler.system;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jacob.common.AppLogger;
import jacob.common.data.DataUtils;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.MinutesIterator;

/**
 * @author mike
 *
 */
public class CallRepeater extends SchedulerTaskSystem
{
	static public final transient String RCS_ID = "$Id: CallRepeater.java,v 1.1 2005/08/15 15:02:18 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	// use this to log relvant information....not the System.out.println(...)    ;-)
	//
	static protected final transient Log logger = AppLogger.getLogger();
	private static final int KEINE = 0;
    private static final int TAEGLICH =1;
    private static final int WOECHENTLICH =2;
    private static final int MONATLICH =3;
    private static final int JAEHRLICH =4;

    private static final List intervallList = new ArrayList();
    
    static {
        intervallList.add(KEINE,"keine");
        intervallList.add(TAEGLICH,"täglich");
        intervallList.add(WOECHENTLICH,"wöchentlich");
        intervallList.add(MONATLICH,"monatlich");
        intervallList.add(JAEHRLICH,"jährlich");
    }
	// Start the task every 1 minutes
	// for more iterators see in the package <de.tif.jacob.scheduler.iterators.*>
	//
	final ScheduleIterator iterator = new MinutesIterator(10);


	/**
	 * Returns the Iterator which defines the run interval of this job.<br>
	 * 
	 */
	public ScheduleIterator iterator()
	{
		return iterator;
	}

	/**
	 * The run method of the job.<br>
	 * The object <code>context>/code> defines your current context in the jACOB application
	 * server.<br>
	 * You can use it to access the database or other relevatn application data.<br>
	 */
	public void run(TaskContextSystem context) throws Exception
	{
          IDataTable templateTable = context.getDataTable("calltemplate");
          templateTable.qbeSetValue("nextcreatedate","<now");
          int recordCount = templateTable.search();
          for(int i=0;i<recordCount;i++)
          {
            createAndSchedule(context ,templateTable.getRecord(i));
          }
	}
    private void createCall(IDataAccessor acc, IDataTableRecord template, IDataTransaction trans) throws Exception
    {
        acc.propagateRecord(template,Filldirection.BACKWARD);
        IDataTableRecord location=  acc.cloneRecord(trans,template.getLinkedRecord("location"));
        IDataTableRecord call = acc.getTable("call").newRecord(trans);
        call.setLinkedRecord(trans,location);
        if (template.hasLinkedRecord("accountcode"))
        {
            call.setLinkedRecord(trans,template.getLinkedRecord("accountingcode"));
        }
        if (template.hasLinkedRecord("category"))
        {
            call.setLinkedRecord(trans,template.getLinkedRecord("category"));
        }
        if (template.hasLinkedRecord("process"))
        {
            call.setLinkedRecord(trans,template.getLinkedRecord("process"));
        }

        if (template.hasLinkedRecord("object"))
        {
            call.setLinkedRecord(trans,template.getLinkedRecord("object"));
        }
        if (template.hasLinkedRecord("callworkgroup"))
        {
            call.setLinkedRecord(trans,template.getLinkedRecord("callworkgroup"));
            call.setValue(trans,"routinginfo", "von Hand geroutet");
        }
        String agent = DataUtils.getAppprofileValue(acc, "webqagent_key");
        if (template.hasLinkedRecord("customerint"))
        {
            call.setLinkedRecord(trans,template.getLinkedRecord("customerint"));
        }
        else
        {
            DataUtils.linkTable(acc,trans,call,"employeecall","customerint","pkey",agent);            
        }
        IDataTableRecord customer = acc.getTable("customerint").getSelectedRecord();
        if (customer != null)
        {
            if (call.getValue("affectedperson") == null)
            {
                String value = customer.getStringValue("fullname") + " Tel: " + customer.getSaveStringValue("phonecorr");
                call.setValue(trans, "affectedperson", value);
            }
            
        }

        DataUtils.linkTable(acc,trans,call,"agentcall","agent","pkey",agent);  
        
        call.setValue(trans,"action",template.getValue("action"));
        call.setValue(trans,"autoclosed",template.getValue("autoclosed"));
        call.setValue(trans,"callbackmethod",template.getValue("callbackmethod"));
        call.setValue(trans,"priority",template.getValue("priority"));
        call.setValue(trans,"problem",template.getValue("problem"));
        call.setValue(trans,"problemtext",template.getValue("problemtext"));
    }
    private void createAndSchedule(TaskContextSystem context , IDataTableRecord template) throws Exception
    {
        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataTransaction trans = acc.newTransaction();
        try
        {

            // next Create Date:
            Calendar cal = Calendar.getInstance();
            cal.setTime(template.getDateValue("nextcreatedate"));
            int intervalValue = template.getintValue("repeatinterval");
            boolean repeat = true;
            switch (intervallList.indexOf(template.getSaveStringValue("repeatintervalunit")))
            {
            case KEINE:
                repeat = false;
                break;
            case TAEGLICH:
                cal.add(Calendar.DATE,intervalValue);
                break;
            case WOECHENTLICH:
                cal.add(Calendar.DATE,7*intervalValue);
                break;                
            case MONATLICH:
                cal.add(Calendar.MONTH,intervalValue);
                break; 
            case JAEHRLICH:
                cal.add(Calendar.YEAR,intervalValue);
                break;                
             }
            createCall(acc , template, trans);
            if (repeat)
                template.setValue(trans,"nextcreatedate",cal.getTime());
            else
                template.setValue(trans,"nextcreatedate",null);
            
            trans.commit();
        }
        catch (RecordLockedException e)
        {
            // Do nothing, try it later
        }
        catch (Exception e) // Loggen des Fehlers im Datensatz
        {
            IDataTransaction errortrans = acc.newTransaction();
            try
            {
                template.setValue(errortrans,"nextcreatedate",null);
                template.setValue(errortrans,"error",e.toString());
                errortrans.commit();
                
            }
            finally
            {
                errortrans.close();
            }
            
        }
        finally
        {
            trans.close();
        }
    }
}
