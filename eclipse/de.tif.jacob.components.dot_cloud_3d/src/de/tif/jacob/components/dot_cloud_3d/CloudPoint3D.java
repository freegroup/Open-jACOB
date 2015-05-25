/*
 * Created on 15.01.2010
 *
 */
package de.tif.jacob.components.dot_cloud_3d;

import java.awt.Color;

public class CloudPoint3D
{
  private final int x;
  private final int y;
  private final int z;
  private final Color color;
  private final String id;
  
  public CloudPoint3D(String id, int x, int y, int z, Color color)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.color = color;
    this.id = id;
  }
  
}




