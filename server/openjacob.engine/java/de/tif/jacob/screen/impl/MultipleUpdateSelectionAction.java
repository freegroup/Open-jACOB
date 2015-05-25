/*
 * Created on 06.03.2009
 *
 */
package de.tif.jacob.screen.impl;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataMultiUpdateTransaction;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.actiontypes.ActionTypeUpdateRecord;
import de.tif.jacob.core.definition.guielements.ButtonDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.impl.html.ActionEmitter;
import de.tif.jacob.screen.impl.html.Button;


/**
 * 
 * @since 2.8.4
 */
public abstract class MultipleUpdateSelectionAction extends TwoPhaseSelectionAction
{
  public MultipleUpdateSelectionAction(boolean isolateMode)
  {
    super("%BUTTON_COMMON_MULTIPLE_UPDATE",isolateMode);
  }

  public void execute(IClientContext context, IGuiElement arg1, ISelection arg2) throws Exception
  {
    final IBrowser browser = (IBrowser)arg1;
    browser.resetErrorDecoration(context);
    browser.resetWarningDecoration(context);
    
    final IDataMultiUpdateTableRecord multipleUpdateRecord = (IDataMultiUpdateTableRecord)context.getSelectedRecord();
    final IDataMultiUpdateTransaction trans = multipleUpdateRecord.getAssociatedTransaction();
    ActionEmitter emitter = createActionEmitter(context, new ActionTypeUpdateRecord(ActionType.SCOPE_OUTERGROUP));
    emitter.setDataStatus(context, IGroup.UPDATE);
    emitter.setParent((GuiElement)browser.getGroup());
    emitter.setEventHandlerSetByMutableGroup(new IActionButtonEventHandler()
    {
      public void onSuccess(IClientContext context, IGuiElement button) throws Exception
      {
        multipleUpdateRecord.getTable().clear();
        browser.getGroup().clear(context,false);
        List records = multipleUpdateRecord.getUnderlyingRecords();
        Iterator iter = records.iterator();
        while(iter.hasNext())
        {
          IDataTableRecord record = (IDataTableRecord)iter.next();
          Exception exc = trans.getCompletionError(record);
          if(exc!=null)
          {
            browser.setErrorDecoration(context,record,new CoreMessage("LABEL_CANCELD",exc.getLocalizedMessage()).print(context.getLocale()));
          }
        }
        MultipleUpdateSelectionAction.this.done=true;
      }
    
      public void onError(IClientContext context, IGuiElement button, Exception reason)
      {
        // Fehler wid bereits vom ActionTypeHandler angezeigt. Die Fehler im "isolate" Mode werden
        // bei onSuccess angezeigt. "isolate" ist immer "erfolgreich"
        MultipleUpdateSelectionAction.this.done=false;
      }
      
      public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
      {
        return true;
      }
    });
    emitter.processEvent(context, emitter.getId(), "click",null);
  }


  private ActionEmitter createActionEmitter(IClientContext context, ActionType type)
  {
    try
    {
      Dimension dim =toDimension(new Rectangle(10,10,10,10));
      
      ButtonDefinition buttonDef = new ButtonDefinition("unused", "", null, label, true, false, null, 0, 0, type, dim, null, -1, null, null);
      Button button = (Button) buttonDef.createRepresentation(getApplicationFactory(context), context.getApplication(), null);

      return button;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }
  private final static Dimension toDimension(Rectangle rect)
  {
    return new Dimension(rect.x,rect.y,rect.width,rect.height);
  }
  
  private static IApplicationFactory getApplicationFactory(IClientContext context)
  {
    return ((HTTPClientSession) context.getSession()).getApplicationFactory();
  }

  public Icon getIcon(IClientContext context)
  {
    return Icon.application_edit;
  }
}
