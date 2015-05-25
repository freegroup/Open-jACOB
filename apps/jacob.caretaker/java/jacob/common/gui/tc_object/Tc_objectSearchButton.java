/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jul 26 16:19:17 CEST 2006
 */
package jacob.common.gui.tc_object;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;

/**
 * The event handler for the Tc_objectSearchButton search button.<br>
 * 
 * @author andreas
 */
public class Tc_objectSearchButton extends ISearchActionEventHandler
{
  static public final transient String RCS_ID = "$Id: Tc_objectSearchButton.java,v 1.5 2008/04/29 16:50:29 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  /**
   * Die Fahrzeuge aus der riesigen Objecttabelle werden durch das externe System herausgefiltert. 
   */
//  private static final String EXT_SYSTEM_NAME = "EDVIN/FUP";

  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    constrainToExtSystem(context);
    return true;
  }
  
  public static void constrainToExtSystem(IClientContext context) throws Exception
  {
    // Änderung, da nur noch Ext-System i-pro
    //ToDo: Prüfen ob eine performantere Methodode, wie z.B. neues feld möglich
    
/*    IDataTable extSystemTable = context.getDataTable("ext_system");
    extSystemTable.qbeClear();
    extSystemTable.qbeSetKeyValue("name", EXT_SYSTEM_NAME);
    extSystemTable.search();

    IDataTableRecord extSystemRecord = extSystemTable.getSelectedRecord();
    if (extSystemRecord == null)
      throw new BusinessException("Fahrzeuge können nicht gefunden werden, da es kein eindeutiges System '" + EXT_SYSTEM_NAME + "' gibt!");

    // Die Suche nach Objekten aus EXT_SYSTEM_NAME beschränken
    //
     * */
   /* 
    IDataTable tcobjectTable = context.getDataTable("tc_object");
    tcobjectTable.qbeSetValue("external_id", "PKW|NFZ");*/
  }

  public void onSuccess(IClientContext context, IGuiElement button)
  {
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}
