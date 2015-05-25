package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Jun 15 17:03:32 CEST 2005
 *
 */
import jacob.common.gui.GenericSendAs;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;



 /**
  * The Event handler for the CallGenerateFax-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallGenerateFax extends GenericSendAs
{
    static public final transient String RCS_ID = "$Id: CallGenerateFax.java,v 1.1 2005/06/17 12:07:53 mike Exp $";
    static public final transient String RCS_REV = "$Revision: 1.1 $";
  /* 
   * @see jacob.common.gui.GenericSendAs#getDocTemplates(de.tif.jacob.screen.IClientContext)
   */
  public IDataTable getDocTemplates(IClientContext context) throws Exception
  {
    // Dokumentenvorlage bestimmen mit einem neuen DatenAccesor holen. Der
    // neue DataAccessor ist wichtig, damit zwischen dem RequestCycle die Tabelle nicht
    // gelöscht werden kann (Focus löschen Button z.B.).
    //
    IDataAccessor accessor =context.getDataAccessor().newAccessor();
    IDataTable docTemplateTable = accessor.getTable("doc_template");
    // im context vom call use_in=überall|Meldung
    docTemplateTable.qbeSetValue("use_in","überall|Meldung");
    docTemplateTable.search();
    String[] templates= new String[docTemplateTable.recordCount()];
    for (int i = 0; i < docTemplateTable.recordCount(); i++)
    {
      templates[i]=docTemplateTable.getRecord(i).getStringValue("name");
    }
    return docTemplateTable;
  }

  /* 
   * @see jacob.common.gui.GenericSendAs#getRecordToSend(de.tif.jacob.screen.IClientContext)
   */
  public IDataTableRecord getRecordToSend(IClientContext context) throws Exception
  {
    return context.getSelectedRecord();
  }
}
