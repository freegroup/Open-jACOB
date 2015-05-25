/*
 * Created on 28.02.2010
 *
 */
package de.tif.jacob.designer.model;

import java.util.Map;

public interface IStyleProvider
{
  public void provideStyle(Map<String,Object> style, boolean withLocation);
  public void consumeStyle(Map<String,Object> style);
}
