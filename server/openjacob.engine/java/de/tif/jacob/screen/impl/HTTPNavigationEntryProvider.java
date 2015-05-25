/*
 * Created on 15.06.2007
 *
 */
package de.tif.jacob.screen.impl;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;

public interface HTTPNavigationEntryProvider
{
  public INavigationEntry getNavigationEntry(IClientContext context);
  public IFormGroup getFormGroup();
  public void       setFormGroup(IFormGroup group);
}
