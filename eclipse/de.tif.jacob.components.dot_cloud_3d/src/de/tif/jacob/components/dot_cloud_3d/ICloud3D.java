/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.dot_cloud_3d;

import de.tif.jacob.screen.IClientContext;

public interface ICloud3D
{
  /**
   * Clear the groubed list box. This will remove all entries
   * from the list.
   * 
   * @param context
   */
  public void clear(IClientContext context) throws Exception;
  

  public void addPoint(CloudPoint3D point); 
  
}

