/*
 * Created on 24.04.2009
 *
 */
package de.freegroup.dbwire.model;

import de.tif.jacob.core.Context;

public interface AbstractModel
{
  public void dumpValue();
  public void resetValue(Context context);
}
