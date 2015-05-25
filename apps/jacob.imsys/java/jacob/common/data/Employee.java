/*
 * Created on Jul 4, 2004
 *
 */
package jacob.common.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * Allgemeine Util-Funktionen zum suchen und verändern von Einträge in der Tabelle Employee
 *  
 */
public class Employee
{
	static public final transient String RCS_ID = "$Id: Employee.java,v 1.3 2006/03/20 09:34:25 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  /**
   * Falls der Anwender einen bestehenden Employee ausgewählt hat, wird der Status
   * des Employee von eventuell gelöscht auf 'von Hand' gesetzt.<br>
   * 
   * Falls der Employee den Status DBCS hat, wird der Status NICHT verändert werden, da er
   * von dem System 'DBCS' gepflegt wird.<br>
   * <br>
   * Falls die übergebenen Transaktion <code>null</code> ist, wird eine eigenen Transaktion
   * angelegt, committet und wieder geschlossen.<br>
   * 
   * @params trans Die Transaction in der die Aktion ausgeführt werden soll. Darf null sein
   * @param context Der momentane Context in dem man arbeitet.
   * @param pkey Der pkey des Employee welcher geänder werden soll 
   * @param status Der neue Status des Employees
   */
  public static void changeEmployeeState(IDataTransaction trans, Context context, String pkey, String employeeTableAlias, String newState) throws Exception
  {
    IDataTable employeeTable= context.getDataTable(employeeTableAlias);
    employeeTable.qbeClear();
    employeeTable.qbeSetKeyValue("pkey",pkey);
    // Der Status des Employee darf nur geändert werden wenn er nicht auf DBCS steht
    employeeTable.qbeSetValue("employeestatus","!DBCS");
    if(employeeTable.search()==1)
    {
      IDataTransaction mytrans = trans==null?context.getDataAccessor().newTransaction():trans;
      try
      {
        IDataTableRecord employee=employeeTable.getRecord(0);
        employee.setStringValue(mytrans,"employeestatus",newState/*"von Hand"*/);

        // transaction nur commiten wenn ich diese selbst angelegt habe
        if(trans==null)mytrans.commit();
      }
      finally
      {
        // transaction nur scchliessen wenn ich diese selbst angelegt habe
        if(trans==null)mytrans.close();
      }
    }
  }
  
  
  
  /**
   * Employees finden welche 'ungefähr' gleich sind wie der Übergebenen.<br>
   * Es werden KEINE Mitarbeiter mit dem Status 'DBCS gelöscht' zurück gelierfert.
   * <br>
   * <br> 
   * @param context Der momentane context in dem man arbeitet.
   * @param firstName Der Vorname des Referenz Employees
   * @param lastName Der Nachname des referenz Employees
   *
   * @return DataTable mit allen 'ähnlichen' Employees
   * 
   * @throws Exception falls allgemeine Datenbankpropleme auftretten.
   */
  public static IDataTable findDuplicate(Context context, String firstName, String lastName) throws Exception
  {
    IDataTable table=context.getDataTable("dupcustomer");
    table.clear();
    table.qbeClear();
    
    table.qbeSetValue("firstnamecorr",firstName);
    table.qbeSetValue("lastnamecorr",lastName);
    table.qbeSetValue("employeestatus","DBCS|von Hand");
     
    table.search();
    return table;
  }
  
  /**
   * Employees mit den übergebenen PKEY mergen.
   * 
   * @param context
   * @param deleteEmployeePkey
   * @param targetEmployeePkey
   */
  public static void merge(Context context, String deleteEmployeePkey, String targetEmployeePkey) throws Exception
  {
  	IDataTransaction mytrans =context.getDataAccessor().newTransaction();
  	try
		{
	  	// Webq Datensätze löschen
  	  //
//	    IDataTable webqTable= context.getDataTable("webqauthint");
//	    webqTable.qbeClear();
//	    webqTable.qbeSetKeyValue("agentwebqauth",deleteEmployeePkey);
//	    webqTable.fastDelete(mytrans);
//
//	    IDataTable table= context.getDataTable("webqauthint");
//	    table.qbeClear();
//	    table.qbeSetKeyValue("employeewebqauth",deleteEmployeePkey);
//	    table.fastDelete(mytrans);

	    
			// Die Gruppenmitgliederzuordnung wird gelöscht ....
	    //
      IDataTable table = context.getDataTable("groupmember" );
	    table.qbeClear();
	    table.qbeSetKeyValue("employeegroup",deleteEmployeePkey);
	    table.fastDelete(mytrans);

	    // Die Meldungen umhaengen ..."
	    //
	    // organization
	    table= context.getDataTable("organization" );
	    table.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
	    table.qbeClear();
	    table.qbeSetKeyValue("contactorg",deleteEmployeePkey);
	    table.search();
	    for (int i = 0; i < table.recordCount(); i++)
			{
	    	table.getRecord(i).setValue(mytrans,"contactorg",targetEmployeePkey);
			}
	    // Meldungen
	    table= context.getDataTable("employeecalls" );
	    table.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
	    table.qbeClear();
	    table.qbeSetKeyValue("employeecall",deleteEmployeePkey);
	    table.search();
	    for (int i = 0; i < table.recordCount(); i++)
			{
	    	table.getRecord(i).setValue(mytrans,"employeecall",targetEmployeePkey);
			}
	    
	    table.qbeClear();
	    table.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
	    table.qbeSetKeyValue("affectedperson_key",deleteEmployeePkey);
	    table.search();
	    for (int i = 0; i < table.recordCount(); i++)
			{
	    	table.getRecord(i).setValue(mytrans,"affectedperson_key",targetEmployeePkey);
			}	
	    
	    table.qbeClear();
	    table.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
	    table.qbeSetKeyValue("agentcall",deleteEmployeePkey);
	    table.search();
	    for (int i = 0; i < table.recordCount(); i++)
			{
	    	table.getRecord(i).setValue(mytrans,"agentcall",targetEmployeePkey);
			}	
	
			// Alter Datensatz wird als gelöscht markiert ...
	    //
		  changeEmployeeState(mytrans,context, deleteEmployeePkey,"dupcustomer", "von Hand gelöscht");
		  
		  // Bestender Datensatz wird reaktiviert
		  //
		  changeEmployeeState(mytrans,context, targetEmployeePkey,"employee", "von Hand");
		  
		  mytrans.commit();
		}
  	finally
		{
  		mytrans.close();
  	}
	}
}
