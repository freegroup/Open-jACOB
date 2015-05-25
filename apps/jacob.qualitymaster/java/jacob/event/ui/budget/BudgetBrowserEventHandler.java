package jacob.event.ui.budget;

import jacob.browser.BudgetBrowser;
import jacob.model.Budget;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public class BudgetBrowserEventHandler extends IBrowserEventHandler 
{
   @Override
   public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception 
   {
     String columnName = browser.getDefinition().getBrowserField(column).getName();
     if(BudgetBrowser.browserUsed_amount.equals(columnName))
     {
        int verfuegbar = record.getintValue(BudgetBrowser.browserAmount);
        int used = record.getintValue(BudgetBrowser.browserUsed_amount);
        if(verfuegbar<used)
           return Icon.bullet_red;
        return Icon.bullet_green;
     }
     else if(BudgetBrowser.browserState.equals(columnName))
     {
       if(Budget.state_ENUM._close.equals(record.getStringValue(BudgetBrowser.browserState)))
         return Icon.calculator_delete;
       else if(Budget.state_ENUM._open.equals(record.getStringValue(BudgetBrowser.browserState)))
         return Icon.calculator_add;
     }
      return super.decorateCell(context, browser, row, column,record, value);
   }

   @Override
   public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception 
   {
   }

}
