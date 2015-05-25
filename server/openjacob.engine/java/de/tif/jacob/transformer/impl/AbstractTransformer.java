/*
 * Created on 19.12.2008
 *
 */
package de.tif.jacob.transformer.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.transformer.ITransformer;

public abstract class AbstractTransformer implements ITransformer
{

  public void transform(OutputStream out,Context context, IDataBrowser browser) throws IOException
  {
    List browserFields = browser.getBrowserDefinition().getBrowserFields();
    String[] header = new String [browserFields.size()];
    
    for (int i=0;i<browserFields.size();i++)
    {
      IBrowserField column = (IBrowserField)browserFields.get(i);
      header[i]=de.tif.jacob.i18n.I18N.localizeLabel(column.getLabel(),context, context.getUser().getLocale());
    }
    
    String[][] data = new String[browser.recordCount()][browserFields.size()];
    for(int row=0;row<browser.recordCount();row++)
    {
      IDataBrowserRecord record = browser.getRecord(row);
      for(int column=0;column<browserFields.size();column++)
      {
        data[row][column]= record.getSaveStringValue(column);
      }
    }
    transform(out, header, data);
  }
  
}
