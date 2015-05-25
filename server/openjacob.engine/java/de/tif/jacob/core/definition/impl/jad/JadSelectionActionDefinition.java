/*
 * Created on 26.05.2008
 *
 */
package de.tif.jacob.core.definition.impl.jad;

import de.tif.jacob.core.definition.impl.AbstractSelectionActionDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;

public class JadSelectionActionDefinition extends AbstractSelectionActionDefinition
{

  public JadSelectionActionDefinition(SelectionActionEventHandler action)
  {
    super(action.getLabel(), action.getClassName(), "", action.getClassName());
  }
}
