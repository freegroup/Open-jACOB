/*
 * Created on 19.06.2007
 *
 */
package de.tif.jacob.screen.impl;

public class IDProvider
{
  private static int idCounter=1;
  public synchronized static int next()
  {
    return idCounter++;
  }
}
