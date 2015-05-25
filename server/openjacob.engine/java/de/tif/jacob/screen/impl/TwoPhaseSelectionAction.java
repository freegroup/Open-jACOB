/*
 * Created on 06.03.2009
 *
 */
package de.tif.jacob.screen.impl;

import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataMultiUpdateTransaction;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.exception.RecordException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;


/**
 * 
 * @since 2.8.4
 */
public abstract class TwoPhaseSelectionAction extends HTTPSelectionAction
{
  public static final String CANCEL ="cancel"; 
  public static final String SAVE   ="save";

  final boolean isolateMode;
  boolean done=false;
  public abstract void execute(IClientContext context, IGuiElement arg1, ISelection arg2) throws Exception;
  
  public TwoPhaseSelectionAction(String label, boolean isolateMode)
  {
    super(label);
    this.isolateMode = isolateMode;
  }

  
  public final void prepare(IClientContext context, IBrowser browser, ISelection selection) throws Exception
  {
    browser.resetErrorDecoration(context);
    browser.resetWarningDecoration(context);

    if (selection.isEmpty())
      throw new UserException(I18N.getCoreLocalized(CoreMessage.NO_RECORDS_SELECTED, context));

    // Versuchen die Records zu locken.
    IDataBrowserInternal dataBrowser = ((HTTPBrowser) browser).getDataInternal();
    IDataMultiUpdateTableRecord multipleUpdateRecord = dataBrowser.startMultiUpdate(selection, this.isolateMode);
    List oldSelectedBrowserRecords = selection.toList();

    selection.clear();
    List records = multipleUpdateRecord.getUnderlyingRecords();

    Iterator iter = records.iterator();

    while (iter.hasNext())
    {
      Object obj = iter.next();
      selection.add(obj);
    }

    // Records welche nicht mit in die Transaction mit aufgenommen weden konnten
    // werden in dem Browser als Warning dargestellt
    //
    IDataMultiUpdateTransaction trans = multipleUpdateRecord.getAssociatedTransaction();
    iter = oldSelectedBrowserRecords.iterator();
    while (iter.hasNext())
    {
      IDataRecord record = (IDataRecord) iter.next();
      if (!selection.contains(record))
      {
        Exception exc = trans.getInvocationError(record);
        if (exc instanceof RecordException)
          browser.setWarningDecoration(context, record, ((RecordException) exc).getLocalizedShortMessage(context.getLocale()));
        else
          browser.setWarningDecoration(context, record, exc.getLocalizedMessage());
      }
    }
    
    // all-or-nothing (non-isolated) multiple update fails
    if (!trans.isValid())
      throw new UserException(I18N.getCoreLocalized(CoreMessage.NOT_ALL_RECORDS_AVAILABLE_FOR_MULTIPLEUPDATE, context));
    
    if (multipleUpdateRecord.getUnderlyingRecords().size() == 0)
    {
      // Do not reset error/warning decoration
      cancel(context, browser, false);
      throw new UserException(I18N.getCoreLocalized(CoreMessage.NO_RECORDS_FOR_MULTIPLEUPDATE, context));
    }

    browser.setSelectedRecordIndex(context, -1);
  }


  public final void cancel(IClientContext context, IBrowser browser) throws Exception
  {
    cancel(context, browser, true);
  }

  private void cancel(IClientContext context, IBrowser browser, boolean resetDecoration) throws Exception
  {
    IDataTable table = context.getDataTable(browser.getGroupTableAlias());
    IDataTransaction trans = table.getSelectedRecord().getCurrentTransaction();
    if(trans !=null)
      trans.close();
    table.clear();
    browser.getGroup().clear(context,false);
    if (resetDecoration)
    {
      browser.resetErrorDecoration(context);
      browser.resetWarningDecoration(context);
    }
    this.done=true;
  }

  public boolean isDone()
  {
    return done;
  }
}
