/*
 * Created on 02.12.2009
 *
 */
package de.tif.jacob.report.impl;

import de.tif.jacob.report.IReport;
import de.tif.jacob.report.impl.castor.Layouts;

public interface ILayoutReport extends IReport
{
  /**
   * Return the assigned layouts of the report or null if not
   * layout assigned to the report.
   * 
   * @return
   */
  public Layouts getLayouts();
}
