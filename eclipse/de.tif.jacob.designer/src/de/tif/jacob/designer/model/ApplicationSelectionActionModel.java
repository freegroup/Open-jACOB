/*
 * Created on 08.05.2008
 *
 */
package de.tif.jacob.designer.model;

import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.screen.impl.HTTPSelectionAction;
import de.tif.jacob.util.clazz.ClassUtil;


public class ApplicationSelectionActionModel extends SelectionActionModel
{

  protected ApplicationSelectionActionModel(ISelectionActionProvider provider, SelectionActionEventHandler handler)
  {
    super(provider, handler);
  }

  protected ApplicationSelectionActionModel(ISelectionActionProvider provider, String className)
  {
    super(provider, new SelectionActionEventHandler());
    getCastor().setClassName(className);
    try
    {
      if(getJacobModel().useI18N())
      {
        String calculatedLabel = "ACTION"+getJacobModel().getSeparator()+ClassUtil.getShortClassName(className).toUpperCase();
        if(!getJacobModel().getI18NKeys().contains(calculatedLabel))
          getJacobModel().addI18N(calculatedLabel,ClassUtil.getShortClassName(className),false);
        getCastor().setLabel("%"+calculatedLabel);
      }
      else
      {
        getCastor().setLabel(ClassUtil.getShortClassName(className));
      }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
  }
}
