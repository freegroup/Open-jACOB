/*
 * Created on 16.07.2009
 *
 */
package de.tif.jacob.screen.impl;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;

public interface HTTPActionEmitter extends IActionEmitter
{
  public ActionType getAction(IClientContext context);
  
}
