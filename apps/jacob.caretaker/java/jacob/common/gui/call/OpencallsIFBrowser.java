/*
 * Created on 17.07.2004
 *
 */
package jacob.common.gui.call;

import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * Diese Funktion wechselt nach anklicken des IFB Browsers in die entspechende Form<br>
 * Medlungserfassung und zeigt den Satz gemäss Relationset an.<br>
 * @author achim
 */
public class OpencallsIFBrowser extends IBrowserEventHandler 
{
	static public final transient String RCS_ID = "$Id: OpencallsIFBrowser.java,v 1.6 2005/06/17 10:55:25 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.6 $";
	
	private static Map redirects = new HashMap();
	
	static class FormRedirect
	{
	  final String form;
	  final String browser;
	  final String relation;
	
    protected FormRedirect( String form,  String browser,  String relation)
    {
      this.form = form;
      this.browser = browser;
      this.relation = relation;
    }
	}
	
	static
	{
		redirects.put("f_call_entry",     new FormRedirect("callEntryCaretaker","callBrowser","r_call_entry"));
		redirects.put("f_call_entryak_fc",new FormRedirect("callEntryCaretakerAK","callAKBrowser","r_call_entry"));
		redirects.put("f_call_manage",    new FormRedirect("callMngrCaretaker","callAKBrowser","r_call"));
		redirects.put("f_problemmanager", new FormRedirect("callMngrCaretaker","callAKBrowser","r_call"));
        redirects.put("f_call_entryMBTECH", new FormRedirect("callEntryMBTECH","callBrowser","r_call_entry"));
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IBrowserEventHandler#filterCell(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, int, int, java.lang.String)
	 */
	public String filterCell(IClientContext context, IBrowser browser, int row,	int column, String data) throws Exception 
	{
		return data;
	}


	/**
	 * 
	 */
	public void onRecordSelect(IClientContext context, IBrowser browser,	IDataTableRecord selectedRecord) throws Exception 
	{
		String pkey = selectedRecord.getStringValue("pkey");
		
		String target = context.getDomain().getName();
		FormRedirect redirect = (FormRedirect)redirects.get(target);
		
		context.setCurrentForm(redirect.form);
		IDataBrowser callbrowser = context.getDataBrowser(redirect.browser);

		context.clearDomain();
		IDataTable calltable = context.getDataTable("call");
		calltable.qbeSetValue("pkey", pkey);
		
		callbrowser.search(redirect.relation, Filldirection.BOTH);
		callbrowser.setSelectedRecordIndex(0);
		callbrowser.propagateSelections();
	}
}
