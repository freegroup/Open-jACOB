/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.call;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;

/**
 * 
 * Diese Funktion unterbindet die Suche Ohne <br>
 * mindestens ein Suchkrtierium
 * 
 * @author achim
 *
 */
public class ConstraintCallObject extends IForeignFieldEventHandler 
{
	static public final transient String RCS_ID = "$Id: ConstraintCallObject.java,v 1.9 2004/08/13 10:04:16 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.9 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IForeignFieldEventHandler#beforeSearch(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IForeignField)
	 */
	public boolean beforeSearch(IClientContext context, IForeignField foreignfield) throws Exception
	{
     ISingleDataGuiElement obj =(ISingleDataGuiElement) context.getGroup().findByName("callObject");
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
