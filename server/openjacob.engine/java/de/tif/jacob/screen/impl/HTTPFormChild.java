/*
 * Created on 13.11.2008
 *
 */
package de.tif.jacob.screen.impl;

import java.util.List;
import java.util.Vector;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;

public interface HTTPFormChild
{
  public void onShow(IClientContext context) throws Exception;
  public void onHide(IClientContext context) throws Exception;
  public List getBrowsers();
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception;
  public ISingleDataGuiElement getFirstElementInTabOrder();
  public void addDataFields(Vector fields);
}
