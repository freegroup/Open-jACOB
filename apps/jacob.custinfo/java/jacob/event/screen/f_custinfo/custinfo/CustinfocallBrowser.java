/*
 * Created on 04.05.2004
 * by mike
 *
 */
package jacob.event.screen.f_custinfo.custinfo;

import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * 
 * @author mike
 *
 */
public class CustinfocallBrowser extends IBrowserEventHandler
{
	  static private Map mapping =new HashMap();
	  
	  static
	  {
	    mapping.put("Rückruf","Angenommen");
	    mapping.put("Durchgestellt","Angenommen");
	    mapping.put("AK zugewiesen","Angenommen");
	    mapping.put("Fehlgeroutet","Angenommen");
	    mapping.put("Angenommen","In Bearbeitung");
	    mapping.put("Fertig gemeldet","Erledigt");
	    mapping.put("Fertig akzeptiert","Erledigt");
	    mapping.put("Dokumentiert","Erledigt");
	    mapping.put("Geschlossen","Erledigt");

	  }
	  
	  /* 
	   * @see de.tif.jacob.screen.event.IBrowserEventHandler#filterCell(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement, int, int, java.lang.String)
	   */
	  public String filterCell(  IClientContext context, IBrowser browser, int row, int column, String data)
	  {
	    if(column==2)
	    {  
	      String result = (String)mapping.get(data);
	      if(result!=null)
	        return result;
	    }
	    return data;
	  }

  /* 
   * @see de.tif.jacob.screen.event.IBrowserEventHandler#onRecordSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IBrowser, de.tif.jacob.core.data.IDataTableRecord)
   */
  public void onRecordSelect( IClientContext context, IBrowser browser, IDataTableRecord selectedRecord)
  {
  }

}