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

public final class SelectionActionDelete extends HTTPSelectionAction
{
  
  public SelectionActionDelete()
  {
    super("%BUTTON_COMMON_DELETE");
  }

  public void execute(IClientContext context, IGuiElement emitter,final ISelection selection) throws Exception
  {
    // do nothing. return silently
    if(selection.isEmpty())
      return;

    CoreMessage msg = selection.size()>1?new CoreMessage("CONFIRM_DELETE_RECORDS"):new CoreMessage("CONFIRM_DELETE_RECORD");

    IOkCancelDialog dialog =context.createOkCancelDialog(msg,new IOkCancelDialogCallback()
    {
      public void onOk(IClientContext context) throws Exception
      {
        int count=0;
        ExtSelection extSelection = (ExtSelection)selection;
        Iterator iter = extSelection.iterator();
        while (iter.hasNext())
        {
          IDataTransaction trans = context.getDataAccessor().newTransaction();
          try
          {
            Object obj = iter.next();
            extSelection.delete(trans,obj);
            trans.commit();
            extSelection.removeFromView(context,obj);
            count++;
          }
          catch(Exception e)
          {
            // ignore
          }
          finally
          {
            trans.close();
          }
        }
        if(count>0)
          context.showTransparentMessage(new CoreMessage("MSG_DELETE_CONFIRMATION",new Integer(count)).print(context.getLocale()));
        else
          context.showTransparentMessage(new CoreMessage("MSG_DELETE_REFUSAL").print(context.getLocale()));
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
