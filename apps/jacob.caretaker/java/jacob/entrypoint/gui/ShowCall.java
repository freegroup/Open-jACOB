/*
 * Created on Mar 31, 2004
 *
 */
package jacob.entrypoint.gui;

import java.util.Properties;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;

/**
 * @author Andreas Herz
 *
 */
public class ShowCall extends IGuiEntryPoint
{
  static public final transient String RCS_ID = "$Id: ShowCall.java,v 1.6 2005/03/03 15:38:13 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";
  
	/* 
	 * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
	 * @author Andreas Herz
	 */
	public void enter(IClientContext context, Properties props)throws Exception
	{
	  IDataTable table = context.getDataTable("call");
	  table.clear();
	  table.qbeSetValue("pkey", props.getProperty("pkey"));
	  table.search();
	  if (table.recordCount() == 1)
	  {
	    IDataTableRecord userRecord = table.getRecord(0);
      IRelationSet relSet = context.getDataAccessor().getApplication().getRelationSet("r_call");
	    context.getDataAccessor().propagateRecord(userRecord, relSet, Filldirection.BOTH);
	  }
	}


	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getDomain()
	 * @author Andreas Herz
	 */
	public String getDomain()
	{
		try
		{
			if (Context.getCurrent().getUser().hasRole("CQ_AGENT"))
			{
				return "f_call_entry";
			}
			else 
			{
				return "f_call_manage";
			}

		}
		catch (Exception e)
		{
			return "f_call_manage";
		}
			
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getForm()
	 * @author Andreas Herz
	 */
	public String getForm()
	{
		try
		{
			if (Context.getCurrent().getUser().hasRole("CQ_AGENT"))
			{
				return "callEntryCaretaker";
			}
			else 
			{
				return "callMngrCaretaker";
			}

		}
		catch (Exception e)
		{
			return "callMngrCaretaker";
		}
			
		
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
