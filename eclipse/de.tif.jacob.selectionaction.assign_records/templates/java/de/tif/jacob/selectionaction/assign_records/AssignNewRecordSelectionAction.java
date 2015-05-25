package de.tif.jacob.selectionaction.assign_records;

import java.util.List;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 * @author andherz
 *
 */
public class AssignNewRecordSelectionAction extends ISelectionAction
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
    
    IApplication app = context.getApplication();
    IApplicationDefinition appDef = app.getApplicationDefinition();
    
    String browser = ((IBrowser)emitter).getData().getName();
    String guid = ""+emitter.getId();
    
    //versuche die m-n tabelle zu diesem browser zu finden
    //
    ITableAlias browserAlias = ((IBrowser)emitter).getData().getTableAlias();
    ITableAlias groupAlias = app.getApplicationDefinition().getTableAlias(context.getGroup().getGroupTableAlias());
    
    // Alle Ralationen welche zu dem browser führen prüfen
    //
    String linkTable = null;
    List<IRelation> relations = appDef.getRelations();
    for (IRelation relation : relations)
    {
      if(relation.getFromTableAlias().getName().equals(browserAlias.getName()))
      {
        // prüfen ob der andere Alias eine M-N Tabelle ist
        if(relation.getToTableAlias().getTableDefinition().isMtoNTable())
        {
          ITableAlias mnAlias = relation.getToTableAlias();
          
          // jetzt muss geprüft werdne ob von dieser M-N Tabelle eine Relation
          // zu dem Alias der derzeitigen Gruppe führt. WEnn ja, dann kann ein Eintrag in der
          // M-N Tabelle erfolgen
          List<IRelation> mnRelations = appDef.getRelations();
          for (IRelation mnRelation : mnRelations)
          {
            if(mnRelation.getToTableAlias().getName().equals(mnAlias.getName()))
            {
              if(mnRelation.getFromTableAlias().getName().equals(groupAlias.getName()))
              {
                linkTable=mnAlias.getName();
                break;
              }
            }
          }
        }
      }
      if(linkTable!=null)
        break;
    }
    String path = ClassUtil.getPackageName(this.getClass());
    String url = "application/"+app.getName()+"/"+app.getVersion()+"/"+path+"/browser.jsp?dataBrowser="+browser+"&guid="+guid;

    if(linkTable!=null)
      url = url+"&linkTable="+linkTable;
    
    IUrlDialog dialog = context.createUrlDialog(url);
    dialog.enableScrollbar(true);
    dialog.enableNavigation(false);
    dialog.show(800, 300);
  }

  @Override
  public Icon getIcon(IClientContext context)
  {
    return Icon.add;
  }

  @Override
  public String getLabel(IClientContext context)
  {
    return "Assign Record";
  }
}
