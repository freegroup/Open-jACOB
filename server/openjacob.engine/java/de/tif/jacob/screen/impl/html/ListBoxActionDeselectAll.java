/*
 * Created on 06.02.2008
 *
 */
package de.tif.jacob.screen.impl.html;

import de.tif.jacob.screen.impl.HTTPListBox;
import de.tif.jacob.screen.impl.ListBoxAction;

public class ListBoxActionDeselectAll extends ListBoxAction
{
  public String getTooltipMessageId()
  {
    return "LISTBOX_ACTION_TOOLTIP_DESELECTALL";
  }

  public String getImageName()
  {
    return "listbox_deselectall.png";
  }

  public String getJavaScriptEventHandler(HTTPListBox listbox)
  {
    GuiHtmlElement l =(GuiHtmlElement)listbox;
    return "Listbox_doDeselectAll(getObj('"+l.getEtrHashCode()+"'),getObj('container_"+l.getEtrHashCode()+"'))";
  }
}

