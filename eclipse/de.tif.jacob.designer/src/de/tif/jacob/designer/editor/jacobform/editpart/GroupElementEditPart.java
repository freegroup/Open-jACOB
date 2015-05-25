/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
package de.tif.jacob.designer.editor.jacobform.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.IActionFilter;
import de.tif.jacob.designer.editor.jacobform.figures.IFontConsumerFigure;
import de.tif.jacob.designer.model.IFontProviderModel;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.util.FontFactory;
import de.tif.jacob.util.StringUtil;




public class GroupElementEditPart extends FormElementEditPart implements IActionFilter
{
  private final Class figureClass;
  
  public GroupElementEditPart(Class figureClass)
  {
    this.figureClass = figureClass;
  }
  
	public IFigure createFigure() 
	{
		try
    {
      return (IFigure)figureClass.newInstance();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
	}
	
	public void createEditPolicies()
  {
    super.createEditPolicies();
  	installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
  }
  

	public UIGroupElementModel getGroupElementModel()
  {
    return (UIGroupElementModel)getModel();
  }
	
  
  @Override
  public void refreshVisuals()
  {
    UIGroupElementModel model = getGroupElementModel();
    if(model instanceof IFontProviderModel)
    {
      IFontProviderModel provider = (IFontProviderModel)model;
      IFontConsumerFigure fontFigure = (IFontConsumerFigure)getObjectFigure();
      Font font = FontFactory.getFont(provider.getFontFamily(), provider.getFontStyle(), provider.getFontWeight(), provider.getFontSize());
      getObjectFigure().setFont(font);
      
      if(ObjectModel.ALIGN_LEFT.equals(provider.getAlign()))
        fontFigure.setAlign(PositionConstants.LEFT);
      else if(ObjectModel.ALIGN_CENTER.equals(provider.getAlign()))
        fontFigure.setAlign(PositionConstants.CENTER);
      else
        fontFigure.setAlign(PositionConstants.RIGHT);
    }
    super.refreshVisuals();
  }

  public boolean testAttribute(Object target, String name, String value)
  {
    if ("templateName".equals(name))
    {
      UIGroupElementModel model = ((GroupElementEditPart) target).getGroupElementModel();
      return model.getTemplateFileName() == null && model.getTemplateClass() == null;
    }
    return super.testAttribute(target,name,value);
  }
  
  public void openDBEditor(){};
}
