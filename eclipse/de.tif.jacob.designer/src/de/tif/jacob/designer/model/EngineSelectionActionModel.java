/*
 * Created on 08.05.2008
 *
 */
package de.tif.jacob.designer.model;

import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.screen.impl.HTTPSelectionAction;


public class EngineSelectionActionModel extends SelectionActionModel
{
  protected EngineSelectionActionModel(ISelectionActionProvider provider, SelectionActionEventHandler handler)
  {
    super(provider, handler);
  }

  protected EngineSelectionActionModel(ISelectionActionProvider provider, String className)
  {
    super(provider, new SelectionActionEventHandler());
    getCastor().setClassName(className);
    try
    {
      getCastor().setLabel(((HTTPSelectionAction)Class.forName(className).newInstance()).getLabel(null));
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
  }
}
