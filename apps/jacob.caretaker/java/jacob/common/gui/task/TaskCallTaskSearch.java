/*
 * Created on 20.08.2004
 * by mike
 *
 */
package jacob.common.gui.task;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * sucht die Aufträge des selektierten Meldung
 * @author mike
 *
 */
public class TaskCallTaskSearch extends IButtonEventHandler
{
static public final transient String RCS_ID = "$Id: TaskCallTaskSearch.java,v 1.2 2005/03/03 15:38:15 sonntag Exp $";
static public final transient String RCS_REV = "$Revision: 1.2 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTable calltable = context.getDataTable("call");
		if ( calltable.recordCount()!=1)
		{
			alert("Es ist kein Call ausgewählt");
			return;
		}
		IDataBrowser taskBrowser = context.getDataBrowser();
		IDataTable tasktable = context.getDataTable();
		String callKey= calltable.getRecord(0).getSaveStringValue("pkey");
		context.clearDomain();
		tasktable.qbeSetValue("calltask",callKey);
		taskBrowser.search("r_task",Filldirection.BOTH);
		// QBE wieder löschen!
		tasktable.qbeClear();
	}

}
