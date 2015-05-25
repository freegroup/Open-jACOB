package jacob.entrypoint.gui;

import jacob.common.CDSync;
import jacob.common.DataUtils;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;

/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * 
 * You can access this entry point within an WebBrowser with the URL:
 * http://localhost:8080/jacob/enter?entry=CTI&app=missioncontrol&user=USERNAME&pwd=PASSWORD&param1=abc
 *  
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and password of the application.
 *       2. Replace localhost:8080 with the real servername and port.
 *       3. You can add any additional parameters to the url. The jACOB application servers will provide them
 *          for you via the properties.getProperty("...") method.
 *
 * @author {user}
 *
 */
public class CTI extends IGuiEntryPoint
{
  
    public boolean callerSearch(IClientContext context, String  phone)throws Exception
    {
        IRelationSet relationset = context.getApplicationDefinition().getRelationSet("caller_callerorg");

        IDataTable table = context.getDataTable("caller");   
        table.clear();
        table.qbeClear();
        table.qbeSetValue("phonecti", phone+"#");
        table.search(relationset);
        if (table.recordCount() == 1)
        {        
          IDataTableRecord userRecord = table.getRecord(0);
          context.getDataAccessor().propagateRecord(userRecord, relationset, Filldirection.BOTH);
          // Caller in der DAKS Tabelle ctiphone speichern
          IDataTable ctiphone = context.getDataTable("ctiphone");
          IDataTransaction trans = ctiphone.startNewTransaction();
          try
          {
              IDataTableRecord ctiphoneRec = ctiphone.newRecord(trans);
              ctiphoneRec.setValue(trans,"phoneno",phone);
              ctiphoneRec.setValue(trans,"caller_key",userRecord.getValue("pkey"));
              ctiphoneRec.setValue(trans,"lastaccess","now");
              trans.commit();
          }
          finally
          {
              trans.close();
          }

          
          return true;
        }
        
        return false;
    }
    
	/* 
	 * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
	 */
	public void enter(IClientContext context, Properties props)throws Exception
	{
        String phone = props.getProperty("CallingNumber"); // die Rufnummer des Anrufers
        if (phone == null) return;
        
        String newphone ="";
 
        if ("\"".equals(StringUtils.left(phone,1)) && "\"".equals(StringUtils.right(phone,1)))
        {
            phone = StringUtils.mid(phone,1,phone.length()-2); // "-Zeichen vorne hinten abschneiden abschneiden
        }
        
        if (!(phone.length() >0) ) // keine Nummer übermittelt
        {
            return;
        }
        
        // wenn extern, dann 0100 abschneiden International
        if ("0100".equals(StringUtils.left(phone,4))) //extern international mit Vorwahl
        {
            System.out.println(" is international");
            newphone = StringUtils.right(phone,phone.length()-4);
        }
        else if ("010".equals(StringUtils.left(phone,3))) //extern national mit Vorwahl
            {
            System.out.println(" is national");
            newphone = StringUtils.right(phone,phone.length()-3);
            }
        else // interner Anruf allso Daks Standortnummer anfügen
        {
            System.out.println(" is internal");
            newphone = DataUtils.getAppprofileValue(context,"daksmainnumber")+phone;        
        }


        // erst in Tabele ctiphone suchen, dort ist die DAKS - Nummer gespeichert
        IDataTable ctiphone = context.getDataTable("ctiphone");
        ctiphone.clear();
        ctiphone.qbeClear();
        ctiphone.qbeSetKeyValue("phoneno",newphone);
        IRelationSet relationset = context.getApplicationDefinition().getRelationSet("r_ctiphone_caller");     
        ctiphone.search(relationset);
        if (ctiphone.recordCount() ==1)
        {
            IDataTableRecord userRecord = ctiphone.getRecord(0).getLinkedRecord("caller");
            context.getDataAccessor().propagateRecord(userRecord, relationset, Filldirection.BOTH); 
            // für Statistik speichern um später bei Sammeltelefonen den besten zu finden
            IDataTransaction trans = ctiphone.startNewTransaction();
            try
            {
                ctiphone.getRecord(0).setValue(trans,"lastaccess","now");
                trans.commit();
            }
            finally
            {
                trans.close();
            }
            return;
        }
        
        // dann lokal in Tabelle Caller suchen
        if (callerSearch(context,newphone))
            return;

        
        // als letztes versuchen die Nummer im LDAP zu suchen
        // MIKE: Telefonnummer für LDAP aufbereiten
        
        CDSync.sync(context,"","","",newphone,"","","","","",null);
        if (callerSearch(context,newphone))
            return;
        
        // die DAKS Nummer merken und eventuell speichern
        context.clearDomain();
	}


	/**
	 * Return the domain for the GUI entry point. 
	 * A Domain is the name of a group in your outlookbar/composed application
	 */
	public String getDomain()
	{
			return "callEntry";
	}

	/**
	 * Return the name of a form within the returned domain. 
	 */
	public String getForm()
	{
		return "customer";
	}

	/**
	 * @return false if the GUI entry point has no left side navigation (outlookbar) 
	 */
	public boolean hasNavigation()
	{
		return true;
	}

	/** 
	 * @return false if the GUI entry point has no search browser
	 */
	public boolean hasSearchBrowser()
	{
		return true;
	}

	/** 
	 * @return false if the GUI entry point has no toolbar a the top
	 */
	public boolean hasToolbar()
	{
		return true;
	}
}
