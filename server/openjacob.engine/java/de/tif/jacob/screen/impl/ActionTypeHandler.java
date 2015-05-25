/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.screen.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataMultiUpdateTransaction;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.qbe.QBE;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.data.internal.IDataTableRecordInternal;
import de.tif.jacob.core.data.internal.IDataTransactionInternal;
import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.actiontypes.ActionTypeAbout;
import de.tif.jacob.core.definition.actiontypes.ActionTypeChangeUpdateRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearApplication;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearDomain;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearForm;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearGroup;
import de.tif.jacob.core.definition.actiontypes.ActionTypeCreateReport;
import de.tif.jacob.core.definition.actiontypes.ActionTypeDeleteRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeExit;
import de.tif.jacob.core.definition.actiontypes.ActionTypeGeneric;
import de.tif.jacob.core.definition.actiontypes.ActionTypeMessaging;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNavigateToForm;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewWindow;
import de.tif.jacob.core.definition.actiontypes.ActionTypeRecordSelected;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearchUpdateRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSetUserPassword;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowAlert;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowReports;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowSQL;
import de.tif.jacob.core.definition.actiontypes.ActionTypeThemeSelect;
import de.tif.jacob.core.definition.actiontypes.ActionTypeUpdateRecord;
import de.tif.jacob.core.definition.fieldtypes.LongTextEditMode;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RequiredFieldException;
import de.tif.jacob.core.exception.TableFieldExceptionCollection;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.i18n.Message;
import de.tif.jacob.messaging.alert.AlertManager;
import de.tif.jacob.report.impl.dialog.ReportDialogBean;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILongText;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.event.IApplicationEventHandler;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.event.ISearchActionEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas Herz
 *
 */

public class ActionTypeHandler implements IActionTypeHandler
{
 static public final transient String RCS_ID = "$Id: ActionTypeHandler.java,v 1.55 2010/10/24 10:26:46 freegroup Exp $";
 static public final transient String RCS_REV = "$Revision: 1.55 $";

 static private final transient Log logger = LogFactory.getLog(ActionTypeHandler.class);

 public final static ActionTypeHandler INSTANCE = new ActionTypeHandler();

 /**
  * Es wird die �bergebene Gruppe rekursiv nach ForeignFields durchsucht. Die ForeignFields
  * werden anhand des eingegebenen Strings versucht zur�ck zu f�llen. Wenn kein eindeutiger Rekord
  * zugeordnet werden kann, wird ein Dialog angeziegt und abgebrochen.
  *
  * @param context
  * @param group
  * @throws Exception
  */
 private void clearInformbrowser(IClientContext context, IGroup group) throws Exception
 {
   Iterator iter=group.getChildren().iterator();
   while(iter.hasNext())
   {
     Object obj=iter.next();
     if(obj instanceof HTTPInFormBrowser)
     {
       HTTPInFormBrowser browser=(HTTPInFormBrowser)obj;
       browser.getDataInternal().clear();
     }
     else if(obj instanceof ITabContainer /*e.g. TabPane*/)
     {
       Iterator paneIter = ((ITabContainer)obj).getChildren().iterator();
       while(paneIter.hasNext())
       {
         IGroup pane = (IGroup)paneIter.next();
         clearInformbrowser(context,pane);
       }
     }
   }
 }

 public void execute(ActionTypeNewRecord type,IClientContext context, IActionEmitter emitter) throws Exception
 {
   // call the eventhandler if any exists
   // The event handler can override the status (enable/disable) of the button
   //
   IActionButtonEventHandler buttonHandler = (IActionButtonEventHandler)((GuiElement)emitter).getEventHandler(context);

   // the event handler has disabled the execution
   //
   if(buttonHandler!=null && buttonHandler.beforeAction(context, emitter)==false)
     return;

   try
   {
     IDataTable table = context.getDataTable();
     IDataTransaction transaction = table.startNewTransaction();
     IDataTableRecord oldDataRecord = table.getSelectedRecord();
     IDataTableRecordInternal currentDataRecord = (IDataTableRecordInternal) table.newRecord(transaction);

     // initialize record fields due to an existing template record
     // Je nach ExecuteScope des DataAccessors m�ssen eventuell ALLE FORMEN, nur die EINER DOMAIN oder
     // nur die gerade AKTIVE FORM betrachtet werden
     //
     DataScope dataScope = calculateDataScope(context, emitter);
     
     // Es m�ssen jetzt die Datafields der gesamten Application, der Domain oder nur
     // der aktuellen Form ermittelt werden. Dies ist in Abh�ngigkeit der DataAccessor ExecuteScope
     // [application, domain, form]
     DataField[] fields=null;
     if(dataScope == DataScope.APPLICATION)
       fields = ((HTTPApplication)context.getApplication()).getDataFields();
     else if(dataScope == DataScope.DOMAIN)
       fields = ((HTTPDomain) context.getDomain()).getDataFields();
     else if(dataScope == DataScope.FORM)
       fields =((HTTPForm) context.getForm()).getDataFields();
     else // default behaviour in the case of corrupt Property configuration
       fields = ((HTTPDomain) context.getDomain()).getDataFields();
     /*
     DataField[] fields = ((HTTPGroup)context.getGroup()).getDataFields();
     */

     
     ITableField historyField = table.getTableAlias().getTableDefinition().getHistoryField();
     for (int i = 0; i < fields.length; i++)
     {
       DataField field = fields[i];

       // only data of the HTTPGroup (table alias) can be commited.
       // ignore foreign fields
       if (field.getFieldType() == DataField.TYPE_NORMAL)
       {
         // nur zugeh�rige (von der gleichen Tabelle) Werte eintragen.
         // Durch die InformBrowser->Inlinefields kann es passieren, dass diese
         // nicht zu der Tabelle der Gruppe
         // geh�ren.
         //

         // IBIS: �berpr�fen, ob nicht die equals() Methode von Definitionelementen �berschrieben
         //       werden soll, damit der Alias auch dann gleich bleibt, wenn ein Hotdeployment da
         //       war.
         if (field.getTableAlias().getName().equals(currentDataRecord.getTableAlias().getName()))
         {
           // only initialize fields, which have not been initialized before
           // (by data-hooks, etc.)
           if (null == currentDataRecord.getValue(field.getField().getFieldIndex()))
           {
             // do not clone the history field
             if (historyField != null && field.getField().getFieldIndex() == historyField.getFieldIndex())
               continue;
             
             // Attention: To initialize the currentDataRecord in the copy case
             // (oldDataRecord != null) for long text dialog GUI fields in
             // APPEND or PREPEND mode, field.getValue() returns an empty
             // string. Therefore we have to get the value from the
             // oldDataRecord, i.e the record which is copied.
             if (field.getParent() instanceof HTTPLongText)
             {
               if (oldDataRecord != null)
                 currentDataRecord.setValue(transaction, field.getField().getName(), oldDataRecord.getValue(field.getField().getName()));
               continue;
             }

             Object value = field.getValue();
             // empty fields == null fields in the database
             if (value != null && value instanceof String && ((String) value).length() == 0)
               continue;

             try
             {
               if (value instanceof String)
                 // Request 437: Truncate value, if too long
                 currentDataRecord.setStringValueWithTruncation(transaction, field.getField().getName(), (String) value, context.getLocale());
               else
                 currentDataRecord.setValue(transaction, field.getField().getName(), value, context.getLocale());
             }
             catch (InvalidExpressionException ex)
             {
               // ignore data values which could not be converted properly
               // e.g. when switching from search mode to new mode, there might
               // be
               // expressions like "ENUM1|ENUM2" or "date1..date2"
             }
           }
         }
       }
     }

     // Alle Informbrowser zur�cksetzten
     // Records von InformBrowser werden per Default nicht mit kopiert.
     //
     clearInformbrowser(context,context.getGroup());

     // show the related search browser tab
     //
     if(context.getGroup().getBrowser().isVisible())
        ((HTTPForm)context.getForm()).setCurrentBrowser(context.getGroup().getBrowser());

     ((HTTPGroup)context.getGroup()).setDataStatus(context, IGuiElement.NEW);
     if(buttonHandler!=null)
       buttonHandler.onSuccess(context,emitter);
   }
   catch (Exception e)
   {
     ExceptionHandler.handleSmart(context,e);
     if(buttonHandler!=null)
       buttonHandler.onError(context,emitter,e);
   }
 }



 public void execute(ActionTypeSearchUpdateRecord type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   if(context.getGroup().getDataStatus()==GuiElement.SEARCH)
     execute(type.getSearchAction(),context,emitter);
   else
     execute(type.getUpdateAction(),context,emitter);
 }


 /**
  *
  */
 public void execute(ActionTypeDeleteRecord type,IClientContext context, IActionEmitter emitter) throws Exception
 {
   // call the eventhandler if any exists
   // The event handler can override the status (enable/disable) of the button
   //
   IActionButtonEventHandler buttonHandler = (IActionButtonEventHandler)((GuiElement)emitter).getEventHandler(context);

   // the event handler has disabled the execution
   //
   if(buttonHandler!=null && buttonHandler.beforeAction(context, emitter)==false)
     return;
   if(type.needUserConfirmation())
   {
     IOkCancelDialog dialog =context.createOkCancelDialog(new CoreMessage("CONFIRM_DELETE_RECORD"),new DeleteRecordCallback((GuiElement)emitter));
     dialog.show();
   }
   else
   {
     new DeleteRecordCallback((GuiElement)emitter).onOk(context);
   }
 }

 // Toolbar/Menu actions
 public void execute(ActionTypeExit type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   // call the eventhandler if any exists
   // The event handler can cancel the normal close operation
   //
   IApplicationEventHandler eventHandler = ((HTTPApplication)context.getApplication()).getEventHandler();
   if(eventHandler!=null)
   {
     if(!eventHandler.canClose(context, context.getApplication()))
       return;
   }

   if(context.getApplication().isDirty(context))
   {
     IOkCancelDialog dialog = context.createOkCancelDialog(new CoreMessage("CONFIRM_UNSAVE_RECORD"),new IOkCancelDialogCallback()
     {
       public void onOk(IClientContext context) throws Exception
       {
         context.getApplication().close();
       }
       public void onCancel(IClientContext context) throws Exception  { }
     });
     dialog.show();
   }
   else
   {
     context.getApplication().close();
   }
 }

 // Toolbar/Menu actions
 public void execute(ActionTypeAbout type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   FormLayout layout = new FormLayout(
       "10dlu,p,10dlu,grow,10dlu",                               // columns
       "20dlu,p,2dlu,p,2dlu,p,2dlu,p,15dlu,p,2dlu,p,2dlu,p,grow"); // rows
   CellConstraints c=new CellConstraints();

   IFormDialog dialog=context.createFormDialog(Version.ENGINE_NAME,layout,null);
   dialog.addHeader(I18N.getCoreLocalized("LABEL_ENGINE_INFO", context),c.xywh(1,1,3,1));

   dialog.addLabel(I18N.getCoreLocalized("LABEL_ENGINE_NAME", context),c.xy(1,3));
   dialog.addLabel(Version.ENGINE_NAME,c.xy(3,3));

   dialog.addLabel(I18N.getCoreLocalized("LABEL_ENGINE_VERSION", context),c.xy(1,5));
   dialog.addLabel(Version.ENGINE.toString(),c.xy(3,5));

   dialog.addLabel(I18N.getCoreLocalized("LABEL_ADMIN_VERSION", context),c.xy(1,7));
   dialog.addLabel(Version.ADMIN.toString(),c.xy(3,7));


   dialog.addHeader(I18N.getCoreLocalized("LABEL_APPLICATION_INFO", context),c.xywh(1,9,3,1));

   dialog.addLabel(I18N.getCoreLocalized("LABEL_APPLICATION_NAME", context),c.xy(1,11));
   dialog.addLabel(context.getApplicationDefinition().getName(),c.xy(3,11));

   dialog.addLabel(I18N.getCoreLocalized("LABEL_APPLICATION_VERSION", context),c.xy(1,13));
   dialog.addLabel(context.getApplicationDefinition().getVersion().toString(),c.xy(3,13));

//    dialog.setDebug(true);
   dialog.show(450,250);
 }


 /**
  *
  */
 public void execute(ActionTypeGeneric type,IClientContext context, IActionEmitter emitter) throws Exception
 {
   try
   {
     // call the eventhandler if any exists
     // The event handler can override the status (enable/disable) of the button
     //
     IButtonEventHandler buttonHandler = (IButtonEventHandler)((GuiElement)emitter).getEventHandler(context);
     if(buttonHandler!=null)
       buttonHandler.onClick(context, emitter);
   }
   catch (Exception e)
   {
     ExceptionHandler.handleSmart(context,e);
   }
 }


 /**
  *
  */
 public void execute(ActionTypeRecordSelected type,IClientContext context, IActionEmitter emitter) throws Exception
 {
   try
   {
     // call the eventhandler if any exists
     // The event handler can override the status (enable/disable) of the button
     //
     IButtonEventHandler buttonHandler = (IButtonEventHandler)((GuiElement)emitter).getEventHandler(context);
     if(buttonHandler!=null)
       buttonHandler.onClick(context, emitter);
   }
   catch (Exception e)
   {
     ExceptionHandler.handleSmart(context,e);
   }
 }



 /**
  * Reset the DataAccessor and all gui data fields
  */
 public void execute(ActionTypeClearApplication type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   if(((HTTPApplication)context.getApplication()).hasChildInDataStatus(context, IGuiElement.UPDATE) || ((HTTPApplication)context.getApplication()).hasChildInDataStatus(context, IGuiElement.NEW))
   {
     IOkCancelDialog dialog = context.createOkCancelDialog(new CoreMessage("CONFIRM_UNSAVE_RECORD"),new IOkCancelDialogCallback()
         {
       public void onOk(IClientContext context) throws Exception
       {
         context.clearApplication();
       }
       public void onCancel(IClientContext context) throws Exception  { }
     });
     dialog.show();
   }
   else
   {
     context.clearApplication();
   }
 }

 /**
  * Reset the DataAccessor and all gui data fields
  */
 public void execute(ActionTypeClearDomain type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   if(((HTTPDomain)context.getDomain()).hasChildInDataStatus(context, IGuiElement.UPDATE) || ((HTTPDomain)context.getDomain()).hasChildInDataStatus(context, IGuiElement.NEW))
   {
     IOkCancelDialog dialog = context.createOkCancelDialog(new CoreMessage("CONFIRM_UNSAVE_RECORD"),new IOkCancelDialogCallback()
     {
       public void onOk(IClientContext context) throws Exception
       {
         context.clearDomain();
       }
       public void onCancel(IClientContext context) throws Exception  { }
     });
     dialog.show();
   }
   else
   {
     context.clearDomain();
   }
 }

 /**
  *
  */
 public void execute(ActionTypeClearForm type,IClientContext context, IActionEmitter emitter) throws Exception
 {
   if(((HTTPGroup)context.getGroup()).hasChildInDataStatus(context, IGuiElement.UPDATE) || ((HTTPGroup)context.getGroup()).hasChildInDataStatus(context, IGuiElement.NEW))
   {
     IOkCancelDialog dialog = context.createOkCancelDialog(new CoreMessage("CONFIRM_UNSAVE_RECORD"),new IOkCancelDialogCallback()
         {
       public void onOk(IClientContext context) throws Exception
       {
       //  ((HTTPGroup)context.getGroup()).clear(context,false);
       //  ((HTTPGroup)context.getGroup()).resetTabOrder();
         context.clearForm();
       }
       public void onCancel(IClientContext context) throws Exception  { }
     });
     dialog.show();
   }
   else
   {
     context.clearForm();
   }
 }


 /**
  * ClearGroup wird von einem Button gerufen an dem Eventuell ein Hook h�ngt. Dieser muss gerufen werden
  *
  */
 public void execute(ActionTypeClearGroup type,IClientContext context, final IActionEmitter emitter) throws Exception
 {
   // call the eventhandler if any exists
   // The event handler can override the status (enable/disable) of the button
   //
   IActionButtonEventHandler buttonHandler = (IActionButtonEventHandler)((GuiElement)emitter).getEventHandler(context);

   // the event handler has disabled the execution
   //
   if(buttonHandler!=null && buttonHandler.beforeAction(context, emitter)==false)
     return;

   try
   {
     if(((HTTPGroup)context.getGroup()).hasChildInDataStatus(context, IGuiElement.UPDATE) || ((HTTPGroup)context.getGroup()).hasChildInDataStatus(context, IGuiElement.NEW))
     {
       IOkCancelDialog dialog = context.createOkCancelDialog(new CoreMessage("CONFIRM_UNSAVE_RECORD"),new IOkCancelDialogCallback()
           {
         public void onOk(IClientContext context) throws Exception
         {
           IActionButtonEventHandler buttonHandler = (IActionButtonEventHandler)((GuiElement)emitter).getEventHandler(context);
           try
           {
             context.clearGroup();
             if(buttonHandler!=null)
               buttonHandler.onSuccess(context,emitter);
           }
           catch (Exception e)
           {
             ExceptionHandler.handleSmart(context,e);
             if(buttonHandler!=null)
               buttonHandler.onError(context,emitter,e);
           }
         }
         public void onCancel(IClientContext context) throws Exception  { }
       });
       dialog.show();
     }
     else
     {
       context.clearGroup();
       if(buttonHandler!=null)
         buttonHandler.onSuccess(context,emitter);
     }
   }
   catch (Exception e)
   {
     ExceptionHandler.handleSmart(context,e);
     if(buttonHandler!=null)
       buttonHandler.onError(context,emitter,e);
   }
 }


 /**
  *
  */
 public void execute(ActionTypeNewWindow type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   HTTPClientContext c = (HTTPClientContext) context;
   HTTPClientSession session = (HTTPClientSession) c.getSession();
   c.addOnLoadJavascript("openNewApplicationWindow('" + session.createApplication().getHTTPApplicationId() + "');");
 }


 /**
  * Navigate to the form whih is coded in the ActionType
  */
 public void execute(ActionTypeNavigateToForm type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   try
   {
     context.setCurrentForm(type.getFormToNavigate());
   }
   catch (Exception e)
   {
     ExceptionHandler.handle(context,e);
   }
 }

 /**
  * Show the ThemeSelectc dialog.
  */
 public void execute(ActionTypeThemeSelect type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   IUrlDialog dialog = context.createUrlDialog("dialogs/ThemeSelect.jsp");
   dialog.show(600,400);
 }


 /**
  * Zeigt eine Trace window an
  */
 public void execute(ActionTypeShowSQL type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   context.setShowSQL(!context.getShowSQL());
emitter.setLabel(context.getShowSQL()?"%BUTTON_TOOLBAR_HIDE_SQL":"%BUTTON_TOOLBAR_SHOW_SQL");
 }


 /**
  * Zeigt eine Trace window an
  */
 public void execute(ActionTypeShowAlert type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   AlertManager.show(context);
 }


 /**
  * Zeigt eine Trace window an
  */
 public void execute(ActionTypeMessaging type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   IUrlDialog dialog = context.createUrlDialog("dialogs/MessagingDialog.jsp");
   dialog.show(600,400);
 }


 /**
  *
  */
 public void execute(ActionTypeSearch type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   // call the eventhandler if any exists
   // The event handler can override the status (enable/disable) of the button
   //
   Object obj = emitter!=null?((GuiElement)emitter).getEventHandler(context):null;
   if(obj!=null && !(obj instanceof IActionButtonEventHandler))
   {
     throw new UserException(new CoreMessage("WRONG_INTERFACE_FOR_EVENTHANDLER",new Object[]{obj.getClass().getName(),IActionButtonEventHandler.class.getName()}));
   }
   IActionButtonEventHandler buttonHandler =(IActionButtonEventHandler) obj;

   try
   {
     IDataAccessor accessor = context.getDataAccessor();
     IDataBrowser  browser  = context.getDataBrowser();

     // enter first the fields from the other HTTPGroups
     //
     accessor.qbeClearAll();

     // the event handler has disabled the execution
     //
     if(buttonHandler!=null && buttonHandler.beforeAction(context, emitter)==false)
       return;


     // ---------------------------------------------
     // STEP 1: For all HTTPGroups which are either in SELECTED or UPDATED mode
     // the search should be constrained to the primary key value
     // of the selected record, if existing!
     // Therefore we collect the aliases of these HTTPGroups in selectedAliases.
     // ---------------------------------------------
     Set selectedAliases = new HashSet();

     List formList = new ArrayList();

     // Je nach ExecuteScope des DataAccessors m�ssen evnetuell ALLE FORMEN, nur die EINER DOMAIN oder
     // nur die gerade AKTIVE FORM betrachtet werden
     //
     // Alle Formen betrachten
     //
     DataScope dataScope = calculateDataScope(context, emitter);
     /*
     if (emitter != null)
       dataScope = ((GuiElement) emitter).getDataScope();
     else if (context.getForm() != null)
       dataScope = ((HTTPForm) context.getForm()).getDataScope();
     else if (context.getDomain() != null)
       dataScope = ((HTTPDomain) context.getDomain()).getDataScope();
     else
       dataScope = ((HTTPApplication) context.getApplication()).getDataScope();
     */

     if(dataScope == DataScope.APPLICATION)
     {
       Iterator domainIter = context.getApplication().getChildren().iterator();
       while (domainIter.hasNext())
       {
         IGuiElement possibleDomain = (IGuiElement)domainIter.next();
         if(possibleDomain instanceof HTTPDomain)
         {
           formList.addAll(((HTTPDomain)possibleDomain).getChildren());
         }
       }
     }
     // Nur die gerade aktuelle Domain betrachten
     //
     else if(dataScope == DataScope.DOMAIN)
     {
       formList = ((HTTPDomain) context.getDomain()).getChildren();
     }
     // Nur die aktuelle Form betrachten
     //
     else if(dataScope == DataScope.FORM)
     {
       formList.add(context.getForm());
     }
     else // default behaviour in the case of corrupt Property configuration
       formList = ((HTTPDomain) context.getDomain()).getChildren();



     for (int i = 0; i < formList.size(); i++)
     {
       Object child = formList.get(i);
       if (child instanceof HTTPForm)
       {
         HTTPForm form = (HTTPForm) child;
         List HTTPGroupList = form.getChildren();
         for (int j = 0; j < HTTPGroupList.size(); j++)
         {
           child = HTTPGroupList.get(j);
           if (child instanceof HTTPGroup)
           {
             HTTPGroup httpGroup = (HTTPGroup) child;
             if (httpGroup.getDataStatus() == IGuiElement.SELECTED || httpGroup.getDataStatus() == IGuiElement.UPDATE)
             {
               ITableAlias alias = httpGroup.getDefinition().getTableAlias();
               if (selectedAliases.contains(alias) == false)
               {
                 IDataTable dataTable = accessor.getTable(alias);
                 IDataTableRecord selectedRecord = dataTable.getSelectedRecord();
                 if (selectedRecord != null)
                 {
                   IDataKeyValue primaryKeyValue = selectedRecord.getPrimaryKeyValue();
                   if (null != primaryKeyValue)
                   {
                     dataTable.qbeSetPrimaryKeyValue(primaryKeyValue);
                     // only add alias, if table has an primary key defined
                     selectedAliases.add(alias);
                   }
                 }
               }
             }
           }
         }
       }
     }

     // ----------------------------------------------------------------------
     // STEP 2: Collect the data fields in relation of the DataAccessor scope
     // ----------------------------------------------------------------------
     //
     // Es m�ssen jetzt die Datafields der gesamten Application, der Domain oder nur
     // der aktuellen Form ermittelt werden. Dies ist in Abh�ngigkeit der DataAccessor ExecuteScope
     // [application, domain, form]
     DataField[] fields=null;
     if(dataScope == DataScope.APPLICATION)
       fields = ((HTTPApplication)context.getApplication()).getDataFields();
     else if(dataScope == DataScope.DOMAIN)
       fields = ((HTTPDomain) context.getDomain()).getDataFields();
     else if(dataScope == DataScope.FORM)
       fields =((HTTPForm) context.getForm()).getDataFields();
     else // default behaviour in the case of corrupt Property configuration
       fields = ((HTTPDomain) context.getDomain()).getDataFields();

     // ---------------------------------------------
     // STEP 3: Constrain search by remaining data fields
     // ---------------------------------------------
     //
     for (int i = 0; i < fields.length; i++)
     {
       DataField field = fields[i];
       GuiElement element = (GuiElement)field.getParent();

       // CHECK 1: Check whether the data field belongs to a HTTPGroup whose
       // table alias has already been constrained by means of STEP 1
       if (element.getParent() instanceof HTTPGroup)
       {
         HTTPGroup HTTPGroup = (HTTPGroup) element.getParent();
         if (selectedAliases.contains(HTTPGroup.getDefinition().getTableAlias()))
           continue;
       }

       if (field.getField() != null && field.getField().getType().isConstrainable())
       {
         // Falls der Benutzer in eine ForeignField '!null, null...' eintr�gt,
         // darf nicht das anzeigende Feld abgefragt werden sondern der
         // Foreignkey der referenzierenden Tabelle.
         // Es reicht dabei einen Teil des Keys zu setzen und nicht
         // alle Elemente des Keys.
         //
         String value;
         ITableAlias tableAlias;
         ITableField tableField;
         if (element instanceof HTTPForeignField)
         {
           HTTPForeignField ffield = (HTTPForeignField) element;

           if (ffield.isBackfilled())
           {
             // Wenn der Record zur�ckgef�llt ist, dann direkt den Prim�rschl�ssel in der verbundenen Tabelle setzen
             ffield.setQbeValues(context, accessor);
             continue;
           }
           else if(field.isDiverse())
           {
             continue;
           }
           else if (QBE.isNullExpression(field.getQBEValue()))
           {
             // Wert aus der Anzeige eintragen
             value = field.getQBEValue();
             // Set QBE expression like:
             //
             // fromTable <-- toTable
             //                .field=(!NULL, NULL, ....)
             tableField = (ITableField) ffield.getToKey().getTableFields().get(0);
             tableAlias = ffield.getToTable();
           }
           else
           {
             tableField = field.getField();
             value = field.getQBEValue();
             tableAlias = field.getTableAlias();
           }
         }
         else
         {
           tableField = field.getField();
           value = field.getQBEValue();
           tableAlias = field.getTableAlias();
         }

         // CHECK 2: Check whether the data field's alias has already been
         // constrained by means of STEP 1
         // Note: CHECK 2 is not needless, because of ForeignFields which we
         // did not catch in CHECK 1
         if (selectedAliases.contains(tableAlias))
         {
           continue;
         }

         if (value != null && value.length() > 0)
           ((IDataTableInternal) accessor.getTable(tableAlias)).qbeSetGuiElementValue(element.getPathName(), tableField, value, context.getLocale());
       }
     }

       // check for unconstrained searches
       if (emitter!=null && emitter.isSafeSearch() && !accessor.qbeHasConstraint(browser.getTableAlias(), type.getRelationSet()))
       {
         context.createMessageDialog(new CoreMessage(CoreMessage.UNCONSTRAINED_SEARCH)).show();
         return;
       }

       // Anzahl der maximalen Ergebnissrekord einstellen.
       //
       if(browser.getBrowserDefinition().getConnectByKey()==null)
         browser.setMaxRecords(Property.BROWSER_COMMON_MAX_RECORDS.getIntValue());
       else
         browser.setUnlimitedRecords();

       // Dem Eventhandler die M�glichkeit geben, die QBE der Suche zu ver�ndern
       //
       if(buttonHandler instanceof ISearchActionEventHandler)
         ((ISearchActionEventHandler)buttonHandler).modifyQbe(context,emitter);

       // Die Suche starten....
       //
       browser.search(type.getRelationSet(),type.getFillDirection());

       // ...und das Ergebniss in den Suchbrowser eintragen
       //
       context.setDataBrowser(browser);

       // Dem Hook mitteilen, dass die Ausf�hrung geklappt hat.
       //
       if(buttonHandler!=null)
         buttonHandler.onSuccess(context,emitter);
     }
     catch (Exception e)
     {
       ExceptionHandler.handleSmart(context,e);
       if(buttonHandler!=null)
         buttonHandler.onError(context,emitter,e);
     }
 }

 /**
  * Show the dialog to create a Report
  *
  */
 public void execute(ActionTypeCreateReport type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   try
   {
     if(((IDataBrowserInternal)context.getDataBrowser()).getLastSearchConstraints()==null)
     {
       IMessageDialog dialog=context.createMessageDialog(new CoreMessage("NEED_QUERY_BEFORE_REPORT_SAVE"));
       dialog.show();
       return;
     }
     ((HTTPApplication)context.getApplication()).startReportMode(context);
   }
   catch (Exception e)
   {
     ExceptionHandler.handleSmart(context,e);
   }
 }

 /**
  * Show the dialog with all reports
  */
 public void execute(ActionTypeShowReports type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   ReportDialogBean.reset(context);
   IUrlDialog dialog = context.createUrlDialog("dialogs/ReportingDialog.jsp");
   dialog.show(850, 635);
 }

 /**
  * Es wird die �bergebene Gruppe rukursiv nach ForeignFields durchsucht. Die ForeignFields
  * werden anhand des eingegebenen Strings versucht zur�ck zu f�llen. Wenn kein eindeutiger Rekord
  * zugeordnet werden kann, wird ein Dialog angeziegt und abgebrochen.
  *
  * @param context
  * @param group
  * @throws Exception
  */
 private boolean tryBackfill(IClientContext context, IGroup group) throws Exception
 {
   Iterator iter=group.getChildren().iterator();
   while(iter.hasNext())
   {
     Object obj=iter.next();
     if(obj instanceof HTTPForeignField)
     {
       HTTPForeignField field=(HTTPForeignField)obj;
       // Falls in dem ForeignField ein DIVERSE steht, dann nehmen wir dies einfach so hin.
       // Dies passiert im MultipleUpdate wenn die betroffenen Records am ForeignField unterschiedliche
       // Werte haben.
       if(field.getDataField().isDiverse())
         return true;
       
       IDataTable table = context.getDataAccessor().getTable(field.getDataField().getTableAlias());
       IDataTableRecord currentDataRecord = table.getSelectedRecord();
       if(StringUtil.toSaveString(field.getValue()).length()>0 && currentDataRecord==null )
         field.tryBackfill(context,2,false, false);
       currentDataRecord = table.getSelectedRecord();

       field.getSelectedRecord(context);
       // Falls in dem ForeignField ein Wert eingetragen ist und KEIN eindeutiger Backfill
       // m�glich war wird das speichern verboten
       if(StringUtil.toSaveString(field.getValue()).length()>0 && currentDataRecord==null)
       {
         String label= field.getI18NLabel(context.getLocale());
         IMessageDialog dialog = context.createMessageDialog(new CoreMessage("FOREIGNFIELD_NOT_SPECIFIED",label));
         dialog.show();
         return false;
       }
       // tell te foreign field that there is a record to show.
       // This is important for the update part below. The foreignKey to the element must be set before
       // the commit.
       else if(currentDataRecord!=null)
         field.setValue(context, currentDataRecord);
     }
     else if(obj instanceof ITabContainer /*e.g. TabPane*/)
     {
       Iterator paneIter = ((ITabContainer)obj).getChildren().iterator();
       while(paneIter.hasNext())
       {
         IGroup pane = (IGroup)paneIter.next();
         if(tryBackfill(context,pane)==false)
           return false;
       }
     }
   }
   return true;
 }

 /**
  *
  */
 public void execute(ActionTypeUpdateRecord type,IClientContext context, IActionEmitter emit) throws Exception
 {
   HTTPActionEmitter emitter = (HTTPActionEmitter)emit;
   
   // call the eventhandler if any exists
   // The event handler can override the status (enable/disable) of the button
   //
   Object objh = ((GuiElement)emitter).getEventHandler(context);
   if(objh!=null && !(objh instanceof IActionButtonEventHandler))
   {
     throw new UserException(new CoreMessage("WRONG_INTERFACE_FOR_EVENTHANDLER",new Object[]{objh.getClass().getName(),IActionButtonEventHandler.class.getName()}));
   }
   IActionButtonEventHandler buttonHandler =(IActionButtonEventHandler) objh;


   try
   {
     // commit the data set
     //
     if(emitter.getDataStatus()==IGuiElement.NEW || emitter.getDataStatus()==IGuiElement.UPDATE)
     {

         // Falls in einem ForeignField ein Wert nur teilweise eingetragen ist, wird jetzt versucht
         // mit den Constraints den betroffenen Record zu suchen und ein Backfill in das ForeignField
         // zu machen.
         // Man braucht somit nicht auf die 'Lupe' druecken wenn man genau weiss, dass der Teilbegriff
         // den man eingetippt hat den Record eindeutig selektiert.
         //
         if(tryBackfill(context,getRelatedUpdateGroup(context,emitter))==false)
           return;

         // Je nach ExecuteScope des DataAccessors m�ssen eventuell ALLE FORMEN, nur die EINER DOMAIN oder
         // nur die gerade AKTIVE FORM betrachtet werden
         //
         // Alle Formen betrachten
         //
         DataScope dataScope = calculateDataScope(context, emitter);
         
         // Es muessen jetzt die Datafields der gesamten Application, der Domain oder nur
         // der aktuellen Form ermittelt werden. Dies ist in Abhaengigkeit der DataAccessor ExecuteScope
         // [application, domain, form]

 /*          
  *      Change 29.07.2009: This has been changed because: If you have DataScope Domain and more than one form
  *      based on the same tablealias, the form values might be overwritten depending on the order of the forms. 
  *       
  *       
  *       DataField[] fields=null;
        
         if(dataScope == DataScope.APPLICATION)
           fields = ((HTTPApplication)context.getApplication()).getDataFields();
         else if(dataScope == DataScope.DOMAIN)
           fields = ((HTTPDomain) context.getDomain()).getDataFields();
         else if(dataScope == DataScope.FORM)
           fields =((HTTPForm) context.getForm()).getDataFields();
         else // default behaviour in the case of corrupt Property configuration
           fields = ((HTTPDomain) context.getDomain()).getDataFields();
*/
         
         DataField[] fields=((HTTPForm) context.getForm()).getDataFields();
         
         IDataTableRecordInternal record = (IDataTableRecordInternal)getRelatedUpdateTable(context,emitter).getSelectedRecord();

         // Since the record to commit might have been created by a user hook, use
         // the transaction of the record!
         IDataTransaction transaction = record.getCurrentTransaction();
         if (null == transaction)
         {
           // otherwise use transaction registered at the table
           transaction = getRelatedUpdateTable(context,emitter).getTableTransaction();
         }

         // Alle GUI Elemente informieren, dass der edit modus verlassen wird.
         // Diese haben somit die M�glichkeite Ihre Daten (wenn noch nicht passiert)
         // vorzubereiten. siehe InformBrowser.
         //
         // Achtung: Das mu� vor dem Setzen des Savepoints geschehen, da die Zeilen welche
         //          gerade im Edit modus sind ihre Daten im DataRecord speichern und im Falle
         //          einer Exception diese durch den Rollback zum Savepoint abhanden gehen.
         //
         // TODO: Das funktioniert nicht sofern es mehrere Gruppen in einer Dom�n gibt, welche
         // dieselbe Transaktion (d.h. sie zeigen auf dieselbe DataTable) verwenden.
         //
         // TODO: Unsch�nheit: Sofern beim Speichern eine Exception (z.B. RequiredFieldException)
         // auftritt so sind die Eintr�ge in den Informbrowsern eventuell deselektriert, d.h.
         // der alte Zustand ist nicht mehr derselbe.
         Iterator iter=getRelatedUpdateGroup(context,emitter).getChildren().iterator();
         while(iter.hasNext())
         {
           ((GuiElement)iter.next()).onEndEditMode(context);
         }

         // Set transaction savepoint
         //
         // This is because, we want to rollback all changes upon this step in case of an exception.
         // Reason: If we do not do that modifications of record.appendLongTextValue()/prependLongTextValue()
         //         would not be rolled back in case of an exception. Which would lead to append/prepend the
         //         text twice if the users tries again!!!
         //
         ((IDataTransactionInternal) transaction).setSavepoint();

         try
         {
           TableFieldExceptionCollection exs = null;
           for(int i=0; i<fields.length;i++)
           {
             DataField field = fields[i];

             // TabPanes welche disabled sind werden nicht mit einbezogen
             //
             if(field.getParent().getParent() instanceof ITabPane)
             {
               ITabPane pane = (ITabPane)field.getParent().getParent();
               if(pane.isEnabled()==false)
                 continue;
             }

             // only data of the HTTPGroup (table alias) can be commited.
             // ignore foreign fields
             if (field.getFieldType()==DataField.TYPE_NORMAL)
             {
               // Das Feld bezieht sich auf den TableAlias der Gruppe.
               //
               if(field.getTableAlias().getName().equals(record.getTableAlias().getName()))
               {
                 Object value = field.getValue();
                 // empty fields == null fields in the database
                 if(value!=null && value instanceof String && ((String)value).length()==0)
                   value=null;

                 // if an optional field (i.e. a field which is not required within database) has been set to
                 // required, user input is requested and must therefore be handled at this location!
                 // Note: Required database fields are not handled here because these might be filled up by
                 // hooks on data layer and are handled anyhow within this layer!
                 if (value == null && field.getParent().isRequired() && !field.getField().isRequired())
                 {
                   if (exs == null)
                     exs = new TableFieldExceptionCollection(new CoreMessage("REQUIRED_FIELDS_MISSING"));
                   exs.add(new RequiredFieldException(field.getField()));
                 }

                 if(logger.isDebugEnabled())logger.debug("SET UPDATE Value:"+field);

                 if (field.getParent() instanceof ILongText)
                 {
                   // distinguish append/prepend mode for long text
                   //
                   LongTextEditMode inputMode = ((ILongText) field.getParent()).getEditMode();
// TODO: READONLY sollte global abgehandelt werden und nicht f�pr ein einzelnes Feld
                   if (field.getField().isReadOnly())
                   {
                     continue;
                   }
                   if (inputMode == LongTextEditMode.APPEND)
                   {
                     if (value != null)
                       record.appendLongTextValue(transaction, field.getField().getName(),"\n"+ (String) value, true);
                     continue;
                   }
                   if (inputMode == LongTextEditMode.PREPEND)
                   {
                     if (value != null)
                       record.prependLongTextValue(transaction, field.getField().getName(), (String) value+"\n", true);
                     continue;
                   }
                 }

                 record.setValue(transaction, field.getField(), value, context.getLocale());
               }
               // Das Feld bezieht sich auf einen anderen TableAlias, ist  aber in der selben Gruppe! Muss man sich erst den
               // entsprechenden Record besorgen.
               // Dies wird f�r MutableGroup ben�tigt. Man kann in einer MutableGroup Elemente einf�gen welche nicht zum TableAlias
               // der Gruppe geh�ren. Man kann sich so einen "InformBrowser" selber zusammen basteln.
               //
               // (Typischerweise wenn mittels der MutableGroup API ein Element einer Fremdtabelle eingef�gt wird.)
               //
               // ACHTUNG: Ein ForeignField in einem InformBrowser erf�llt auch diese Kriterien, hat aber displayRecordIndex==-1!!! => Exception
               //
               else if(((GuiElement)field.getParent()).getGroup()==getRelatedUpdateGroup(context,emitter) && ((HTTPSingleDataGuiElement)field.getParent()).getDisplayRecordIndex()!=-1)
               {

                 Object value = field.getValue();
                 // empty fields == null fields in the database
                 if(value!=null && value instanceof String && ((String)value).length()==0)
                   value=null;

                 // if an optional field (i.e. a field which is not required within database) has been set to
                 // required, user input is requested and must therefore be handled at this location!
                 // Note: Required database fields are not handled here because these might be filled up by
                 // hooks on data layer and are handled anyhow within this layer!
                 if (value == null && field.getParent().isRequired() && !field.getField().isRequired())
                 {
                   if (exs == null)
                     exs = new TableFieldExceptionCollection(new CoreMessage("REQUIRED_FIELDS_MISSING"));
                   exs.add(new RequiredFieldException(field.getField()));
                 }

                 if(logger.isDebugEnabled())logger.debug("SET UPDATE Value:"+field);

                 IDataTable table = context.getDataTable(field.getTableAlias().getName());
                 IDataTableRecordInternal r = (IDataTableRecordInternal) table.getRecord(((HTTPSingleDataGuiElement) field.getParent()).getDisplayRecordIndex());

                 if (field.getParent() instanceof HTTPLongText)
                 {
                   // distinguish append/prepend mode for long text
                   //
                   LongTextEditMode inputMode = ((HTTPLongText) field.getParent()).getEditMode();
// TODO: READONLY sollte global abgehandelt werden und nicht f�pr ein einzelnes Feld 
                   if (field.getField().isReadOnly())
                   {
                     continue;
                   }

                   if (inputMode == LongTextEditMode.APPEND)
                   {
                     if (value != null)
                       r.appendLongTextValue(transaction, field.getField().getName(), (String) value);
                     continue;
                   }
                   if (inputMode == LongTextEditMode.PREPEND)
                   {
                     if (value != null)
                       r.prependLongTextValue(transaction, field.getField().getName(), (String) value);
                     continue;
                   }
                 }
                 r.setValue(transaction, field.getField(), value, context.getLocale());
               }
               // Das Feld ist in einem TabPane der selben/eventausloesender(!) Gruppe und bezieht sich auf einen anderen 
               // TableAlias. Der zugehoerige Record wurde 'manuell' ueber einen API-Call oder Button auf 
               // UPDATE geschaltet.
               //
               else if((field.getParent().getDataStatus() == IGuiElement.UPDATE || field.getParent().getDataStatus() == IGuiElement.NEW) 
                   && ((HTTPSingleDataGuiElement)field.getParent()).getOuterGroup() == ((HTTPGroup)context.getGroup()).getOuterGroup())
               {
                 IDataTableRecordInternal tabPaneRecord = (IDataTableRecordInternal) context.getDataTable(field.getTableAlias().getName()).getSelectedRecord();
                 // Der Record kann eventuell mit einer anderen Transaction angelegt worden sein (EmbeddedTransaction).
                 IDataTransaction tabPaneTransaction = tabPaneRecord.getCurrentTransaction();

                 Object value = field.getValue();
                 // empty fields == null fields in the database
                 if(value!=null && value instanceof String && ((String)value).length()==0)
                   value=null;

                 // if an optional field (i.e. a field which is not required within database) has been set to
                 // required, user input is requested and must therefore be handled at this location!
                 // Note: Required database fields are not handled here because these might be filled up by
                 // hooks on data layer and are handled anyhow within this layer!
                 if (value == null && field.getParent().isRequired() && !field.getField().isRequired())
                 {
                   if (exs == null)
                     exs = new TableFieldExceptionCollection(new CoreMessage("REQUIRED_FIELDS_MISSING"));
                   exs.add(new RequiredFieldException(field.getField()));
                 }

                 if(logger.isDebugEnabled())logger.debug("SET UPDATE Value:"+field);

                 if (field.getParent() instanceof ILongText)
                 {
                   // distinguish append/prepend mode for long text
                   //
                   LongTextEditMode inputMode = ((ILongText) field.getParent()).getEditMode();
// TODO: READONLY sollte global abgehandelt werden und nicht f�pr ein einzelnes Feld
                   if (field.getField().isReadOnly())
                   {
                     continue;
                   }
                   if (inputMode == LongTextEditMode.APPEND)
                   {
                     if (value != null)
                       tabPaneRecord.appendLongTextValue(tabPaneTransaction, field.getField().getName(),"\n"+ (String) value);
                     continue;
                   }
                   if (inputMode == LongTextEditMode.PREPEND)
                   {
                     if (value != null)
                       tabPaneRecord.prependLongTextValue(tabPaneTransaction, field.getField().getName(), (String) value+"\n");
                     continue;
                   }
                 }

                 tabPaneRecord.setValue(tabPaneTransaction, field.getField(), value, context.getLocale());

               }
             }
           }
           if(exs!=null)
             throw exs;

           // check if we can commit the data
           //
           if(buttonHandler!=null && buttonHandler.beforeAction(context, emitter)==false)
             return;
           transaction.commit();
           
           transaction.close();
         }
         catch (Exception ex)
         {
           if ( transaction.isValid())
             ((IDataTransactionInternal) transaction).rollbackToSavepoint();
           throw ex;
         }

         // Falls es ein neuer Record ist, welcher in der OuterGroup erzeugt wurde, dann wird dieser in den Browsder eingef�gt.
         // Ansonsten handelt es sich um ein Record von z.B. einem TabPane.
         if (emitter.getDataStatus()==IGuiElement.NEW && context.getGroup() == getRelatedUpdateGroup(context,emitter))
         {
           // add the new record to the UI browser
           context.getGUIBrowser().add(context,record);
         }
         else
         {
           if(transaction instanceof IDataMultiUpdateTransaction)
           {
             IDataMultiUpdateTransaction multiTransaction = (IDataMultiUpdateTransaction)transaction;
             IDataMultiUpdateTableRecord multiRecord      = (IDataMultiUpdateTableRecord)record;
             
             int recordCount = multiRecord.getUnderlyingRecords().size();
             int errorCount  = multiTransaction.getCompletionErrorCount();
             int successCount= recordCount - errorCount;
             if(errorCount==0)
             {
               if(recordCount==1)
                 context.showTransparentMessage(I18N.getCoreLocalized("MSG_TRANSPARENT_RECORD_SAVED", context));
               else
                 context.showTransparentMessage(new CoreMessage("MSG_TRANSPARENT_MULTIPLE_RECORDS_SAVED", new Integer(recordCount)).print(context.getLocale()));
             }
             else
             {
               context.showTransparentMessage(new CoreMessage("MSG_TRANSPARENT_PARTIAL_RECORDS_SAVED",new Object[]{ new Integer(successCount), new Integer(recordCount)}).print(context.getLocale()),false);
             }
           }
           else
           {
             context.showTransparentMessage(I18N.getCoreLocalized("MSG_TRANSPARENT_RECORD_SAVED", context));
           }
         }
         // Kein SELECTED Event versenden wenn es sich um einen MultipleUpdateRecord handelt. Dieser wird sofort wieder
         // entfernt. GUI Hooks kommen eventuell mit dem DIVERSE Status mancher Felder nicht zurecht. Desweiteren ist dieser
         // Event nicht weiter interessant, da der Rekord sofort aus der Maske entfernt wird.
         if(!(record instanceof IDataMultiUpdateTableRecord))
         {
           ((HTTPGroup)context.getGroup()).setDataStatus(context, IGuiElement.SELECTED);
           ((HTTPGroup)context.getGroup()).getBrowser().refresh(context,record);
         }
     }
     else
     {
         // check if we can prepare the record for update
         //
         if(buttonHandler!=null && buttonHandler.beforeAction(context, emitter)==false)
           return;

         IDataTableInternal table = getRelatedUpdateTable(context,emitter);
         
         table.updateSelectedRecord(table.startNewTransaction());
         
         getRelatedUpdateGroup(context,emitter).setDataStatus(context, IGuiElement.UPDATE);
     }

     if(buttonHandler!=null)
       buttonHandler.onSuccess(context,emitter);
   }
   catch(Exception e)
   {
     ExceptionHandler.handleSmart(context,e);
     if(buttonHandler!=null)
       buttonHandler.onError(context, emitter, e);
   }
 }

 
 private IDataTableInternal getRelatedUpdateTable(IClientContext context, HTTPActionEmitter emitter) throws Exception
 {
   if(ActionType.SCOPE_OUTERGROUP == emitter.getAction(context).getExecuteScope())
     return (IDataTableInternal) context.getDataTable();
   
   return (IDataTableInternal)context.getDataTable(emitter.getGroupTableAlias());
 }
 
 private HTTPGroup getRelatedUpdateGroup(IClientContext context, HTTPActionEmitter emitter) throws Exception
 {
   if(ActionType.SCOPE_OUTERGROUP == emitter.getAction(context).getExecuteScope())
     return ((HTTPGroup)context.getGroup());
   
   return (HTTPGroup)emitter.getGroup();
 }
 
 
 public void execute(ActionTypeSetUserPassword type, IClientContext context, IActionEmitter emitter) throws Exception
 {
   FormLayout layout = new FormLayout(
       "10dlu,p,10dlu,200dlu,10dlu", // 6 columns
       "20dlu,p,2dlu,p,20dlu"); // 7 rows
   CellConstraints c=new CellConstraints();


   IFormDialog dialog=context.createFormDialog(I18N.getCoreLocalized("DIALOG_TITLE_PASSWORDCHANGE", context),layout,new IFormDialogCallback()
   {
     /*
      * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext, java.lang.String, java.util.Map)
      */
     public void onSubmit(IClientContext context, String buttonId, java.util.Map formValues) throws Exception
     {
       String pwd1=(String)formValues.get("pwd1");
       String pwd2=(String)formValues.get("pwd2");
       Message msg=new CoreMessage("CHANGE_PASSWORD_PWD1_NOT_EQUALS_PWD2");
       if(pwd1.equals(pwd2))
       {
         context.getUser().setPassword(pwd1);
         msg=new CoreMessage("CHANGE_PASSWORD_SUCCESSFUL");
       }
       IMessageDialog dialog= context.createMessageDialog(msg);
       dialog.show();
     }
   });
   dialog.addLabel(I18N.getCoreLocalized("LABEL_CHANGEPASSWORD_PASSWORD", context),c.xy(1,1));
   dialog.addPasswordField("pwd1","",c.xy(3,1));
   dialog.addLabel(I18N.getCoreLocalized("LABEL_CHANGEPASSWORD_REPEAT", context),c.xy(1,3));
   dialog.addPasswordField("pwd2","",c.xy(3,3));

   dialog.addSubmitButton("ok",I18N.getCoreLocalized("BUTTON_COMMON_APPLY", context), true);
   dialog.setCancelButton(I18N.getCoreLocalized("BUTTON_COMMON_CANCEL", context));
//    dialog.setDebug(true);
   dialog.show(300,120);
 }

 
  public void onOuterGroupStatusChanged(ActionTypeSetUserPassword type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
  {
     // do nothing
  }

 /**
  *
  */
 public void onOuterGroupStatusChanged(ActionTypeNewRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {

   // the status of the parent has changed
   //
   ((GuiElement)emitter).setDataStatus(context, status);


   if(status==IGuiElement.SELECTED)
     emitter.setLabel("%BUTTON_COMMON_COPY");
   else
     emitter.setLabel("%BUTTON_COMMON_NEW");

   if(status==IGuiElement.SEARCH || status==IGuiElement.SELECTED)
      emitter.setEnable(true);
   else
      emitter.setEnable(false);
 }

 /**
  *
  */
 public void onOuterGroupStatusChanged(ActionTypeNewWindow type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 /**
  *
  */
 public void onOuterGroupStatusChanged(ActionTypeChangeUpdateRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // the status of the parent has changed
   //
   ((GuiElement)emitter).setDataStatus(context, status);
   if(status==IGuiElement.SEARCH)
   {
     emitter.setEnable(false);
     emitter.setLabel("%BUTTON_COMMON_UPDATE");
   }
   else if(status==IGuiElement.SELECTED)
   {
     emitter.setEnable(true);
     emitter.setLabel("%BUTTON_COMMON_CHANGE");
   }
   else if(status==IGuiElement.NEW)
   {
     emitter.setEnable(true);
     emitter.setLabel("%BUTTON_COMMON_SAVE");
   }
   else if(status==IGuiElement.UPDATE)
   {
     emitter.setEnable(true);
     emitter.setLabel("%BUTTON_COMMON_SAVE");
   }
   else
   {
     emitter.setEnable(false);
   }
 }



 /**
  *
  */
 public void onOuterGroupStatusChanged(ActionTypeClearGroup type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeDeleteRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   ((GuiElement)emitter).setDataStatus(context, status);

   emitter.setEnable(status== IGuiElement.SELECTED);
 }

 /**
  * The handler for action button [Exit] if the grou status changed
  */
 public void onOuterGroupStatusChanged(ActionTypeExit type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing at the moment
 }

 public void onOuterGroupStatusChanged(ActionTypeGeneric type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 /**
  *
  */
 public void onOuterGroupStatusChanged(ActionTypeRecordSelected type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // the status of the parent has changed
   //
   ((GuiElement)emitter).setDataStatus(context, status);
   emitter.setEnable(status== IGuiElement.SELECTED);
 }

 /**
  *
  */
 public void onOuterGroupStatusChanged(ActionTypeSearch type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   ((GuiElement)emitter).setDataStatus(context, status);

   if(status==IGuiElement.SELECTED)
       emitter.setEnable(false);
   else if(status==IGuiElement.UPDATE)
     emitter.setEnable(false);
   else if(status==IGuiElement.NEW)
     emitter.setEnable(false);
   else
       emitter.setEnable(true);
 }


 /**
  *
  */
 public void onOuterGroupStatusChanged(ActionTypeUpdateRecord type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // the status of the parent has changed
   //
   if(type.getExecuteScope()==ActionType.SCOPE_OUTERGROUP)
   {
     ((GuiElement)emitter).setDataStatus(context, status);
     if(status==IGuiElement.SEARCH)
     {
         emitter.setEnable(false);
         emitter.setLabel("%BUTTON_COMMON_CHANGE");
     }
     else if(status==IGuiElement.SELECTED)
     {
         emitter.setEnable(true);
         emitter.setLabel("%BUTTON_COMMON_CHANGE");
     }
     else if(status==IGuiElement.NEW)
     {
         emitter.setEnable(true);
         emitter.setLabel("%BUTTON_COMMON_SAVE");
     }
     else if(status==IGuiElement.UPDATE)
     {
         emitter.setEnable(true);
         emitter.setLabel("%BUTTON_COMMON_SAVE");
     }
     else
     {
         emitter.setEnable(false);
     }
   }
 }

 public void onOuterGroupStatusChanged(ActionTypeClearApplication type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeClearDomain type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeClearForm type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeShowSQL type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeShowAlert type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeMessaging type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeThemeSelect type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeCreateReport type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeShowReports type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeNavigateToForm type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 public void onOuterGroupStatusChanged(ActionTypeAbout type,IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // do nothing
 }

 /**
  * Callback class for the delete of an record.
  */
 public static class  DeleteRecordCallback implements IOkCancelDialogCallback
 {
   final GuiElement emitter;
   DeleteRecordCallback(GuiElement emitter)
   {
     this.emitter=emitter;
   }

   public void onCancel(IClientContext context) throws Exception{}

   /*
    * @see de.tif.jacob.screen.dialogs.IOkCancelDialogCallback#onOk(de.tif.jacob.screen.IClientContext)
    */
   public void onOk(IClientContext context) throws Exception
   {
     // call the eventhandler if any exists
     // The event handler can override the status (enable/disable) of the
     // button
     //
     Object obj = emitter.getEventHandler(context);
     if(obj!=null && !((obj instanceof IActionButtonEventHandler)))
       throw new UserException(new CoreMessage("WRONG_INTERFACE_FOR_EVENTHANDLER",new Object[]{obj.getClass().getName(),IActionButtonEventHandler.class.getName()}));

     IActionButtonEventHandler buttonHandler = (IActionButtonEventHandler)obj;

     IDataTableRecord currentDataRecord = context.getSelectedRecord();
     try
     {
       IDataTransaction transaction = currentDataRecord.getAccessor().newTransaction();
       try
       {
         currentDataRecord.delete(transaction);
         transaction.commit();
       }
       finally
       {
         transaction.close();
       }

       // IBIS: Das austragen aus dem DatenBrowser verallgemeinern
       //
       // remove the deleted record from the data browser and in the ui
       ((IDataBrowserInternal) context.getGUIBrowser().getData()).removeSelected();
       context.getDataTable().clear();
       context.getGUIBrowser().setSelectedRecordIndex(context, -1);
       ((HTTPGroup) context.getGroup()).setDataStatus(context, IGuiElement.SEARCH);
       if (buttonHandler != null)
         buttonHandler.onSuccess(context, emitter);
     }
     catch (Exception e)
     {
       ExceptionHandler.handleSmart(context,e);
       if (buttonHandler != null)
         buttonHandler.onError(context, emitter, e);
     }
   }
 }

 public void onOuterGroupStatusChanged(ActionTypeSearchUpdateRecord type, IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   // the status of the parent has changed
   //
   ((GuiElement)emitter).setDataStatus(context, status);
   if(status==IGuiElement.SELECTED)
       emitter.setLabel("%BUTTON_COMMON_CHANGE");
   else if(status==IGuiElement.NEW)
       emitter.setLabel("%BUTTON_COMMON_SAVE");
   else if(status==IGuiElement.UPDATE)
       emitter.setLabel("%BUTTON_COMMON_SAVE");
   else
     emitter.setLabel("%BUTTON_COMMON_SEARCH");
 }
 

 public void onGroupStatusChanged(ActionTypeUpdateRecord actionTypeUpdateRecord, IClientContext context, IGuiElement emitter, GroupState status) throws Exception
 {
   if(actionTypeUpdateRecord.getExecuteScope()==ActionType.SCOPE_TABPANE)
   {
     // the status of the parent has changed
     //
     if(status==IGuiElement.SEARCH)
     {
         emitter.setEnable(false);
         emitter.setLabel("%BUTTON_COMMON_CHANGE");
     }
     else if(status==IGuiElement.SELECTED)
     {
         emitter.setEnable(true);
         emitter.setLabel("%BUTTON_COMMON_CHANGE");
     }
     else if(status==IGuiElement.NEW)
     {
         emitter.setEnable(true);
         emitter.setLabel("%BUTTON_COMMON_SAVE");
     }
     else if(status==IGuiElement.UPDATE)
     {
         emitter.setEnable(true);
         emitter.setLabel("%BUTTON_COMMON_SAVE");
     }
     else
     {
         emitter.setEnable(false);
     }
   }
 }

 public static DataScope calculateDataScope(IClientContext context, IGuiElement emitter)
 {
   DataScope dataScope;
   if (emitter != null)
     dataScope = ((GuiElement) emitter).getDataScope();
   else if (context.getForm() != null)
     dataScope = ((HTTPForm) context.getForm()).getDataScope();
   else if (context.getDomain() != null)
     dataScope = ((HTTPDomain) context.getDomain()).getDataScope();
   else
     dataScope = ((HTTPApplication) context.getApplication()).getDataScope();
   return dataScope;
 }
}

