/*
 * Created on 08.09.2004
 * by mike
 *
 */
package jacob.common.gui.call;

import jacob.common.Yan;
import jacob.exception.BusinessException;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * 
 * @author mike
 *
 */
public class CallDirectprint extends IButtonEventHandler
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		// lprIP suchen

	  String lprIP = (String) context.getUser().getProperty("lpr_ip");
	  if (lprIP == null) throw new BusinessException("Ihr Drucker ist nicht richtig konfiguriert.\\r\\nBitte wenden Sie sich an Ihren Administrator.");
	  if (lprIP.length()==0) throw new BusinessException("Ihr Drucker ist nicht richtig konfiguriert.\\r\\nBitte wenden Sie sich an Ihrem Administrator.");
	  
	
		// Direktdruckvorlage finden
		IDataTable docTemplate = context.getDataTable("doc_template");
		docTemplate.qbeClear();
		docTemplate.qbeSetKeyValue("use_in","Meldung");
		docTemplate.qbeSetValue("isdirectprintable","1");
		if (docTemplate.search()!=1) throw new BusinessException("Direktdruck ist nicht richtig konfiguriert.\\r\\nBitte wenden Sie sich an Ihrem Administrator.");
		
		IDataTableRecord doctemplateRec = docTemplate.getRecord(0);
    String template=doctemplateRec.getLinkedRecord("xml_template").getStringValue("xmltext");
    
    // Datenbankfelder in dem XML ersetzen
    //
    String xml=Yan.fillDBFields(context, context.getSelectedRecord(), doctemplateRec,template,true, null);
    Yan.createInstance(context,xml,"remoteprint://"+ lprIP, doctemplateRec.getStringValue("fax_xsl"));
    alert("Meldung wird gedruckt");
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{

		emitter.setEnable(status == IGuiElement.SELECTED) ;
	}
}
