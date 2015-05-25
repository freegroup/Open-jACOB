/*
 * Created on 08.05.2008
 *
 */
package de.tif.jacob.screen.impl;

import java.util.Iterator;

import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;

public final class SelectionActionDeleteAllOrNothing extends HTTPSelectionAction
{
  public SelectionActionDeleteAllOrNothing()
  {
    super("%BUTTON_COMMON_DELETE");
  }

  public void execute(IClientContext context, IGuiElement emitter,final ISelection selection) throws Exception
  {
    // do nothing. return silently
    if(selection.isEmpty())
      return;
    final int count = selection.size();
    CoreMessage msg = count>1?new CoreMessage("CONFIRM_DELETE_RECORDS"):new CoreMessage("CONFIRM_DELETE_RECORD");
    
    IOkCancelDialog dialog =context.createOkCancelDialog(msg,new IOkCancelDialogCallback()
    {
      public void onOk(IClientContext context) throws Exception
      {
        IDataTransaction trans = context.getDataAccessor().newTransaction();
        try
        {
          ExtSelection extSelection = (ExtSelection)selection;
          Iterator iter = extSelection.iterator();
          
          while (iter.hasNext())
          {
            extSelection.delete(trans,iter.next());
          }
          trans.commit();

          iter = extSelection.iterator();
          while (iter.hasNext())
          {
            Object obj = iter.next();
            extSelection.removeFromView(context,obj);
          }
        }
        finally
        {
          trans.close();
        }
        context.showTransparentMessage(new CoreMessage("MSG_DELETE_CONFIRMATION",new Integer(count)).print(context.getLocale()));
      }
    
      public void onCancel(IClientContext context) throws Exception
      {
        // do nothing
      }
    });
    dialog.show();
  }

  public Icon getIcon(IClientContext context)
  {
    return Icon.delete;
  }

}
