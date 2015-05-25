/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.flotr;

import de.tif.jacob.screen.IClientContext;

public interface IFlotr
{
  public void addDataSerie(IClientContext context, DataSerie serie) throws Exception;
  public void resetDataSerie(IClientContext context) throws Exception;

  public AxisConfiguration getXAxis();
  public AxisConfiguration getYAxis();
  public void setZoomEnabled(boolean zoomEnabled);
}
