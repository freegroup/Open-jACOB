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
 * @author Andreas Herz/Mike Döring
 *
 */
public class ShowCall extends IGuiEntryPoint
{
  static public final transient String RCS_ID = "$Id: ShowCall.java,v 1.1 2005/09/06 13:29:22 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
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
      IRelationSet relSet = context.getDataAccessor().getApplication().getRelationSet("call");
	    context.getDataAccessor().propagateRecord(userRecord, relSet, Filldirection.BOTH);
	  }
	}


	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getDomain()
	 * @author Andreas Herz
	 */
	public String getDomain()
	{
	
			return "callManage";
	
	}

	/* 
	 * @see de.tif.jacob.screen.entrypoint.IGuiEntryPoint#getForm()
	 * @author Andreas Herz
	 */
	public String getForm()
	{

			return "callOverview";

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
