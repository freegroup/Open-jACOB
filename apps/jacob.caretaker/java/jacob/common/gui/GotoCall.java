/*
 * Created on 27.04.2004
 * by mike
 *
 */
package jacob.common.gui;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;


/**
 * 
 * @author mike
 *
 */
public class GotoCall 
{
  static public final transient String RCS_ID = "$Id: GotoCall.java,v 1.4 2005/03/03 15:38:12 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  public static void fillData(IClientContext context, String formname) throws Exception
  {
    IDataTable		  objcategroryTable= context.getDataTable("objectcategory");
    if (objcategroryTable.recordCount()==1) 
    {
	    IDataTable        categoryTable    = context.getDataTable("category");    
	    IDataBrowser      categoryBrowser  = context.getDataBrowser("categoryBrowser");     
	    String categoryKey = objcategroryTable.getRecord(0).getStringValue("pkey");
      
      // clear all qbe constraints
	    categoryTable.getAccessor().qbeClearAll();
	    categoryTable.qbeSetValue("pkey",categoryKey);
	    categoryBrowser.search("r_category",Filldirection.BACKWARD);
      
      // there should always be one record
	    categoryBrowser.setSelectedRecordIndex(0);
	    categoryBrowser.propagateSelections();
    }
    //Kostenstelle des Objektes in die Meldung Kopieren
    IDataTable        objaccountingcodeTable    = context.getDataTable("objaccountingcode");    
    if (objaccountingcodeTable.recordCount()==1) 
    {
		String code= objaccountingcodeTable.getRecord(0).getStringValue("code");
		IDataTable accountingodeTable = context.getDataTable("accountingcode");
		accountingodeTable.qbeClear();
		accountingodeTable.qbeSetValue("code",code);
		accountingodeTable.search();
	}     
   
    
    
    
	context.setCurrentForm(formname);
	
    
  }
  
}