/*
 * Created on 26.05.2008
 *
 */
package de.tif.jacob.screen.impl;

import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISelectionAction;

public abstract class HTTPSelectionAction extends ISelectionAction
{
  String label;
  String i18nLabel;
  
  public HTTPSelectionAction(String label)
  {
    this.label = label;
  }

  public void setLabel(String label)
  {
    this.label = label;
    this.i18nLabel=null;
  }
  
  public final String getLabel(IClientContext context)
  {
    if (i18nLabel == null)
    {
      if (this.label == null)
        return null;
      
      // required for the jACOB-Designer
      if(context==null)
        i18nLabel=this.label;
      else
        i18nLabel = I18N.localizeLabel(this.label,context);
    }
    return i18nLabel;
  }
}
