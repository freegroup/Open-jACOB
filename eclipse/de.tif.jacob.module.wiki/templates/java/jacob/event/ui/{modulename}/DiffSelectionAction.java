package jacob.event.ui.{modulename};

import jacob.model.{Modulename};

import java.util.Iterator;
import java.util.LinkedList;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.util.Diff3;

public class DiffSelectionAction extends ISelectionAction
{

  public void execute(IClientContext context, IGuiElement emitter, ISelection selection) throws Exception
  {
    IBrowser browser = (IBrowser)emitter;
    if(selection.size()!=2)
    {
      context.createMessageDialog("You must select 2 versions in the browser for comparison").show();
      return;
    }
    Iterator iter = selection.iterator();
    IDataTableRecord rec1 = ((IDataBrowserRecord)iter.next()).getTableRecord();
    IDataTableRecord rec2 = ((IDataBrowserRecord)iter.next()).getTableRecord();
    
    String content1 = rec1.getSaveStringValue({Modulename}.wikitext); 
    String content2 = rec2.getSaveStringValue({Modulename}.wikitext);
    
    Diff3 diff = new Diff3();
    LinkedList result=null;
    if(rec1.getDateValue({Modulename}.create_date).before(rec2.getDateValue({Modulename}.create_date)))
      result = diff.diff(content1,content2,false);
    else
      result = diff.diff(content2,content1,false);
    
    diff.cleanupSemantic(result);
    String html = diff.toHtml(result);
    context.setPropertyForRequest("diff",html);
    browser.setSelectedRecord(context, rec1);
    context.getDataAccessor().propagateRecord(rec1, Filldirection.BOTH);
    context.getDataTable().setSelectedRecord(rec1.getPrimaryKeyValue());
  }

  public String getLabel(IClientContext context)
  {
    return "Compare Versions";
  }

}
