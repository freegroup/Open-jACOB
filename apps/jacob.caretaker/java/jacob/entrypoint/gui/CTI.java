/*
 * Created on Mar 31, 2004
 *
 */
package jacob.entrypoint.gui;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;


/**
 * @author Andreas Herz
 *
 */
public class CTI extends IGuiEntryPoint
{
  static public final transient String RCS_ID = "$Id: CTI.java,v 1.11 2005/03/03 15:38:13 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.11 $";
	/* 
	 * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
	 * @author Andreas Herz
	 */
	public void enter(IClientContext context, Properties props)throws Exception
	{
	    IDataTable table = context.getDataTable("customerint");
	    String phone = props.getProperty("phone");
	    // wenn extern, dann 01 abschneiden
	    if ("010".equals(StringUtils.left(phone,3)))
	    {
	    	String newphone = StringUtils.rightPad(phone,phone.length()-2);
		    table.qbeSetValue("phonecti",newphone+"#");
	    }
	    else
	    {
		    table.qbeSetValue("phonecti", phone+"#");    	
	    }

	    
	    table.search();
	    if (table.recordCount() == 1)
	    {
	      IDataTableRecord userRecord = table.getRecord(0);
	      context.getDataAccessor().propagateRecord(userRecord, table.getAccessor().getApplication().getRelationSet("r_customer"), Filldirection.BOTH);
	    }
	    else
	    {
	      // the elements will be displayed in the related search browser
	      // automaticly.
	      //
	    }
	    Date d = new Date();
//	  Hier muss noch datecallconnected gesetzt werden!
	    context.getDomain().setInputFieldValue("callEntryCaretaker","callDatecallconnected",d.toString());
	    
	}


	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getDomain()
	 * @author Andreas Herz
	 */
	public String getDomain()
	{
		return "f_call_entry";
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getForm()
	 * @author Andreas Herz
	 */
	public String getForm()
	{
		return "orgcustomerInt";
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasNavigation()
	 * @author Andreas Herz
	 */
	public boolean hasNavigation()
	{
		return true;
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasSearchBrowser()
	 * @author Andreas Herz
	 */
	public boolean hasSearchBrowser()
	{
		return true;
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#hasToolbar()
	 * @author Andreas Herz
	 */
	public boolean hasToolbar()
	{
		return true;
	}
}
