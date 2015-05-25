/*
 * Created on 26.05.2008
 *
 */
package de.tif.jacob.core.definition.impl;

import de.tif.jacob.core.definition.ISelectionActionDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;

public abstract class AbstractSelectionActionDefinition extends AbstractElement implements ISelectionActionDefinition
{
  private final String eventHandler;
  private final String label;

  public AbstractSelectionActionDefinition(String label, String name, String description, String eventHandler)
  {
    super(name, description);
    this.eventHandler = eventHandler;
    this.label = label;
  }

  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
  }

  public String getLabel()
  {
    return this.label;
  }

  /**
   * Returns the class name of the event handler of this gui element.
   * 
   * @return event handler class name or <code>null</code> if default method
   *         to determine the event handler should be used.
   */
  public final String getEventHandler()
  {
    return this.eventHandler;
  }
  
  public final SelectionActionEventHandler toJacob()
  {
    SelectionActionEventHandler res = new SelectionActionEventHandler();
    res.setClassName(this.eventHandler);
    res.setLabel(this.label);
    return res;
  }
}
