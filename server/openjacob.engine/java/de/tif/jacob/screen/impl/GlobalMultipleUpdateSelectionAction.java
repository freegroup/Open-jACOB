/*
 * Created on 09.03.2009
 *
 */
package de.tif.jacob.screen.impl;

import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;


/**
 * 
 * @since 2.8.4
 */
public class GlobalMultipleUpdateSelectionAction extends MultipleUpdateSelectionAction
{
  /** 
   * Default Constructor ist Pflicht, da die Klasse mit Class.forName(..).newInstance geladen wird
   * 
   */
  public GlobalMultipleUpdateSelectionAction()
  {
    super(false);
  }

  /**
   * Macht nur Sinn wenn auch ein Rekord in dem Browser selektiert ist.<br>
   * 
   */
  @Override
  public boolean isVisible(IClientContext context, IGuiElement host)
  {
    return !((IBrowser)host).getSelection().isEmpty();
  }
  
}
