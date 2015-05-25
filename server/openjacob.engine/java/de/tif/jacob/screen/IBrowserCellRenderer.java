/*
 * Created on 23.01.2010
 *
 */
package de.tif.jacob.screen;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.screen.impl.html.ClientContext;


public abstract class IBrowserCellRenderer
{
  /**
   * Return the HTML content (the part between the <b>TD</b> tag) of the cell.
   * 
   * @param context
   * @param record the related record
   * @param row the current row index. Can be used for even/odd rendering (zebra)
   * @param cellContent The raw content to render. This has may be filtered by a UI filterCell Method.
   * @return
   * @since 2.10
   */
  public String renderCell(IClientContext context,IBrowser browser, IDataBrowserRecord record, int row, String cellContent)
  {
    return cellContent;
  }


  /**
   * Return the width of the cell/column or <b>0</b> if the engine should calculate
   * the best width.
   * 
   * @param context
   * @return
   */
  public int getCellWidth(ClientContext context)
  {
    return 0;
  }
  
}
