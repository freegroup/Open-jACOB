/*
 * Created on 19.07.2004
 *
 */
package jacob.common.gui.call;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * @author achim
 * Bei Click auf eine Zeile des Browsers, wird der Focus gelöscht und der Datensatz
 * in der entsprechenden Maske angezeigt
 */
public class callduplicateIFBrowser extends IBrowserEventHandler {
	static public final transient String RCS_ID = "$Id: callduplicateIFBrowser.java,v 1.2 2004/07/30 17:31:52 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IBrowserEventHandler#filterCell(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, int, int, java.lang.String)
	 */
	public String filterCell(IClientContext context, IBrowser browser, int row,
			int column, String data) throws Exception {

		return data;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IBrowserEventHandler#onRecordSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void onRecordSelect(IClientContext context, IBrowser browser,
			IDataTableRecord selectedRecord) throws Exception {
		
		String pkey = selectedRecord.getStringValue("pkey");
		IDataBrowser callbrowser;
		callbrowser = context.getDataBrowser("callAKBrowser");
		

		context.clearDomain();
		IDataTable calltable = context.getDataTable("call");
		calltable.qbeSetValue("pkey", pkey);
		
		callbrowser.search("r_call", Filldirection.BOTH);
		callbrowser.setSelectedRecordIndex(0);
		callbrowser.propagateSelections();
		



	}

}
