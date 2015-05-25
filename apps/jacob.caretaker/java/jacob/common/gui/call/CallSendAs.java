/*
 * Created on Jul 27, 2004
 *
 */
package jacob.common.gui.call;

import jacob.common.gui.GenericSendAs;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;

/**
 *
 */
public class CallSendAs extends GenericSendAs
{
	static public final transient String RCS_ID = "$Id: CallSendAs.java,v 1.4 2005/03/03 15:38:15 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";
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
    docTemplateTable.qbeSetValue("isdirectprintable","0|NULL");
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
