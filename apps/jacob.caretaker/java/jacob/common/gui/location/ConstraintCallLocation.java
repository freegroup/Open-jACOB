/*
 * Created on 04.05.2004
 *
 */
package jacob.common.gui.location;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;

/**
 * @author mike
 *
 */
public  class ConstraintCallLocation extends   IForeignFieldEventHandler 
{
	static public final transient String RCS_ID = "$Id: ConstraintCallLocation.java,v 1.1 2004/10/20 18:09:29 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IForeignFieldEventHandler#beforeSearch(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IForeignField)
	 */
	public boolean beforeSearch(IClientContext context, IForeignField foreignfield) throws Exception
	{
     ISingleDataGuiElement obj =(ISingleDataGuiElement) context.getGroup().findByName("callLocation");
     if(obj.getValue()==null || obj.getValue().length()==0)
     {
       alert("Bitte die Suche einschränken");
       return false;
     }
     return true;
	}
	
  /* 
   * @see de.tif.jacob.screen.event.IForeignFieldEventHandler#onDeselect(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IForeignField)
   */
  public void onDeselect(IClientContext context, IForeignField foreingField)  throws Exception
  {
  }

  /* 
   * @see de.tif.jacob.screen.event.IForeignFieldEventHandler#onSelect(de.tif.jacob.screen.IClientContext, de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.screen.IForeignField)
   */
  public void onSelect(IClientContext context,IDataTableRecord foreignRecord,IForeignField foreingField)  throws Exception
  {
  }

}
