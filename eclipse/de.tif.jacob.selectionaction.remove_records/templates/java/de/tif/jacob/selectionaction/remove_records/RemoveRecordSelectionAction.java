package de.tif.jacob.selectionaction.remove_records;

import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IInFormBrowser;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;

/**
 * @author andherz
 *
 */
public class RemoveRecordSelectionAction extends ISelectionAction
{
  
  @Override
  public boolean isEnabled(IClientContext context, IGuiElement host)
  {
    try
    {
      return context.getSelectedRecord()!=null && context.getSelectedRecord().getCurrentTransaction()!=null;
    }
    catch (Exception e)
    {
    }
    return false;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.ISelectionAction#execute(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement, de.tif.jacob.screen.ISelection)
   */
  @Override
  public void execute(IClientContext context, IGuiElement emitter, ISelection selection) throws Exception
  {
 
    if(context.getSelectedRecord()==null || context.getSelectedRecord().getCurrentTransaction()==null)
      return;
    
    if(selection.isEmpty())
      return;
    
    IApplication app = context.getApplication();
    IApplicationDefinition appDef = app.getApplicationDefinition();
    IDataTableRecord groupRecord = context.getSelectedRecord();
    IDataTransaction transaction = groupRecord.getCurrentTransaction();
    IInFormBrowser browser = (IInFormBrowser)emitter;
    
    //versuche die m-n tabelle zu diesem browser zu finden
    //
    ITableAlias browserAlias = ((IBrowser)emitter).getData().getTableAlias();
    ITableAlias groupAlias = app.getApplicationDefinition().getTableAlias(context.getGroup().getGroupTableAlias());
   
    boolean hasLinkTable = false;
    
    //  BrowserAlias <-from-----broserRelation-----to-> LinkTableAlias <-to------ groupRelation ----from-> GroupAlias
    //
    // Alle Ralationen welche zu dem browser führen prüfen
    //
    List<IOneToManyRelation> relations = appDef.getRelations();
    for (IOneToManyRelation browserRelation : relations)
    {
      if(browserRelation.getFromTableAlias().getName().equals(browserAlias.getName()))
      {
        // prüfen ob der andere Alias eine M-N Tabelle ist
        if(browserRelation.getToTableAlias().getTableDefinition().isMtoNTable())
        {
          ITableAlias mnAlias = browserRelation.getToTableAlias();
          
          // jetzt muss geprüft werdne ob von dieser M-N Tabelle eine Relation
          // zu dem Alias der derzeitigen Gruppe führt. WEnn ja, dann kann ein Eintrag in der
          // M-N Tabelle erfolgen
          List<IOneToManyRelation> mnRelations = appDef.getRelations();
          for (IOneToManyRelation groupRelation : mnRelations)
          {
            if(groupRelation.getToTableAlias().getName().equals(mnAlias.getName()))
            {
              if(groupRelation.getFromTableAlias().getName().equals(groupAlias.getName()))
              {
                hasLinkTable = true;
                List<IDataBrowserRecord> records = selection.toList();
                for (IDataBrowserRecord record : records)
                {
                  try
                  {
                     IDataTable linkTable =context.getDataAccessor().newAccessor().getTable(mnAlias.getName());
                     IDataTableRecord browserRecord = record.getTableRecord();
                     
                     // QBE vom BrowserRecord in die LinkTable eintragen
                     //
                     {
                       List<ITableField> fromFields = browserRelation.getFromPrimaryKey().getTableFields();
                       List<ITableField> toFields = browserRelation.getToForeignKey().getTableFields();
                       for(int i=0;i<fromFields.size();i++)
                       {
                         ITableField fromField = fromFields.get(i);
                         ITableField toField = toFields.get(i);
                         linkTable.qbeSetKeyValue(toField, browserRecord.getValue(fromField));
                       }
                     }
                     
                     // QBE vom GroupRecord in die LinkTable eintragen
                     //
                     {
                       List<ITableField> fromFields = groupRelation.getFromPrimaryKey().getTableFields();
                       List<ITableField> toFields = groupRelation.getToForeignKey().getTableFields();
                       for(int i=0;i<fromFields.size();i++)
                       {
                         ITableField fromField = fromFields.get(i);
                         ITableField toField = toFields.get(i);
                         linkTable.qbeSetKeyValue(toField, groupRecord.getValue(fromField));
                       }
                     }
                     linkTable.searchAndDelete(transaction);
                     browser.getData().removeRecord(record);
                     selection.remove(record);
                  }
                  catch(Exception exc)
                  {
                    exc.printStackTrace();
                  }
                }
              }
            }
          }
        }
      }
    }
    
    // Falls es sich niht um eine M-N Relation gehandelt hat, dann wird am BrowserRecord 
    // einfach der ForeignKey zurück gesetzt (unlink)
    //
    if(hasLinkTable==false)
    {
      IDataAccessor acc = context.getDataAccessor().newAccessor();
      List<IDataBrowserRecord> records = selection.toList();
      for (IDataBrowserRecord record : records)
      {
        IDataTableRecord browserTableRecord = record.getTableRecord();
        browserTableRecord  = acc.getTable(browserAlias).loadRecord(browserTableRecord.getPrimaryKeyValue());
        selection.remove(record);
        browser.getData().removeRecord(record);
        browserTableRecord.resetLinkedRecord(transaction, groupAlias);
      }      
    }
  }

  @Override
  public Icon getIcon(IClientContext context)
  {
    return Icon.application_delete;
  }

  @Override
  public String getLabel(IClientContext context)
  {
    return "Remove Record";
  }
}
