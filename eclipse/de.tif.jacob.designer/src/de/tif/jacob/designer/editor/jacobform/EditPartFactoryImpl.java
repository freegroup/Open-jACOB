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
package de.tif.jacob.designer.editor.jacobform;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import de.tif.jacob.components.plugin.IComponentPlugin;
import de.tif.jacob.components.plugin.PluginComponentManager;
import de.tif.jacob.components.plugin.PluginComponentManager.Group;
import de.tif.jacob.designer.editor.jacobform.editpart.AbstractChartEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.BreadCrumbEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.ComboboxEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBComboboxEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBDateEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBDateTimeEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBInformLongtextEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBListboxEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBTextfieldComboboxEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DateEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DateTimeEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.HorizontalButtonBarEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.ButtonEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.CaptionEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBForeignFieldEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBLocalInputFieldEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBPasswordEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBRadioButtonGroupEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBTableListBoxEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.DBTextEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.FormEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupElementEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.HeadingEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.InformBrowserEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.InformLongtextEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.LabelEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.ListboxEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.ObjectEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.PasswordEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.PluginElementEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.StackContainerEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.StaticImageEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.StyledTextEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.TabContainerEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.TabEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.TabPanesEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.TabStripEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.TextEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.VerticalButtonBarEditPart;
import de.tif.jacob.designer.editor.jacobform.figures.BarChartFigure;
import de.tif.jacob.designer.editor.jacobform.figures.CalendarFigure;
import de.tif.jacob.designer.editor.jacobform.figures.CanvasFigure;
import de.tif.jacob.designer.editor.jacobform.figures.CheckboxFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ComboBoxFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBCheckboxFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBComboBoxFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBDateFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBDateTimeFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBImageFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBInformLongTextFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBListboxFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBLongTextFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBRadioButtonGroupFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBTextfieldComboBoxFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DBTimeFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DateFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DateTimeFigure;
import de.tif.jacob.designer.editor.jacobform.figures.DocumentFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ImageFigure;
import de.tif.jacob.designer.editor.jacobform.figures.InformLongTextFigure;
import de.tif.jacob.designer.editor.jacobform.figures.LabelFigure;
import de.tif.jacob.designer.editor.jacobform.figures.LineChartFigure;
import de.tif.jacob.designer.editor.jacobform.figures.ListboxFigure;
import de.tif.jacob.designer.editor.jacobform.figures.PasswordFigure;
import de.tif.jacob.designer.editor.jacobform.figures.RadioButtonGroupFigure;
import de.tif.jacob.designer.editor.jacobform.figures.StaticImageFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TextFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TimeFigure;
import de.tif.jacob.designer.editor.jacobform.figures.TimeLineFigure;
import de.tif.jacob.designer.model.UIBarChartModel;
import de.tif.jacob.designer.model.UIBreadCrumbModel;
import de.tif.jacob.designer.model.UIButtonBarModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UICalendarModel;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.model.UICheckboxModel;
import de.tif.jacob.designer.model.UIComboboxModel;
import de.tif.jacob.designer.model.UIDBCheckboxModel;
import de.tif.jacob.designer.model.UIDBEnumComboboxModel;
import de.tif.jacob.designer.model.UIDBDateModel;
import de.tif.jacob.designer.model.UIDBDateTimeModel;
import de.tif.jacob.designer.model.UIDBDocumentModel;
import de.tif.jacob.designer.model.UIDBForeignFieldModel;
import de.tif.jacob.designer.model.UIDBImageModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.model.UIDBInformLongTextModel;
import de.tif.jacob.designer.model.UIDBLabelModel;
import de.tif.jacob.designer.model.UIDBListboxModel;
import de.tif.jacob.designer.model.UIDBLongTextModel;
import de.tif.jacob.designer.model.UIDBPasswordModel;
import de.tif.jacob.designer.model.UIDBRadioButtonGroupModel;
import de.tif.jacob.designer.model.UIDBTableListBoxModel;
import de.tif.jacob.designer.model.UIDBTextModel;
import de.tif.jacob.designer.model.UIDBTextfieldComboboxModel;
import de.tif.jacob.designer.model.UIDBTimeModel;
import de.tif.jacob.designer.model.UIDateModel;
import de.tif.jacob.designer.model.UIDateTimeModel;
import de.tif.jacob.designer.model.UIHorizontalButtonBarModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIHeadingModel;
import de.tif.jacob.designer.model.UIImageModel;
import de.tif.jacob.designer.model.UIInformLongTextModel;
import de.tif.jacob.designer.model.UILabelModel;
import de.tif.jacob.designer.model.UILineChartModel;
import de.tif.jacob.designer.model.UICanvasModel;
import de.tif.jacob.designer.model.UIAbstractListboxModel;
import de.tif.jacob.designer.model.UIListboxModel;
import de.tif.jacob.designer.model.UIPasswordModel;
import de.tif.jacob.designer.model.UIPluginComponentModel;
import de.tif.jacob.designer.model.UIRadioButtonGroupModel;
import de.tif.jacob.designer.model.UIStackContainerModel;
import de.tif.jacob.designer.model.UIStaticImageModel;
import de.tif.jacob.designer.model.UIStyledTextModel;
import de.tif.jacob.designer.model.UITabContainerModel;
import de.tif.jacob.designer.model.UITabModel;
import de.tif.jacob.designer.model.UITabPanesModel;
import de.tif.jacob.designer.model.UITabStripModel;
import de.tif.jacob.designer.model.UITextModel;
import de.tif.jacob.designer.model.UITimeLineModel;
import de.tif.jacob.designer.model.UITimeModel;
import de.tif.jacob.designer.model.UIVerticalButtonBarModel;

public class EditPartFactoryImpl implements EditPartFactory
{
	public EditPart createEditPart(EditPart context, Object model)
	{
		if(model instanceof UIJacobFormModel)
		{
			FormEditPart part = new FormEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIGroupModel)
		{
			GroupEditPart part = new GroupEditPart();
			part.setModel(model);
			return part;
		}
    if(model instanceof UIHorizontalButtonBarModel)
    {
      HorizontalButtonBarEditPart part = new HorizontalButtonBarEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UIVerticalButtonBarModel)
    {
      VerticalButtonBarEditPart part = new VerticalButtonBarEditPart();
      part.setModel(model);
      return part;
    }
		if(model instanceof UIDBTextModel)
		{
			DBTextEditPart part = new DBTextEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UITextModel)
		{
		  GroupElementEditPart part = new TextEditPart();
			part.setModel(model);
			return part;
		}
    if(model instanceof UIDBPasswordModel)
    {
      DBPasswordEditPart part = new DBPasswordEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UIPasswordModel)
    {
      GroupElementEditPart part = new PasswordEditPart();
      part.setModel(model);
      return part;
    }
		if(model instanceof UIDBInformLongTextModel)
		{
		  DBInformLongtextEditPart part = new DBInformLongtextEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIInformLongTextModel)
		{
      GroupElementEditPart part = new InformLongtextEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBLongTextModel)
		{
		  DBLocalInputFieldEditPart part = new DBLocalInputFieldEditPart(DBLongTextFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBCheckboxModel)
		{
		  DBLocalInputFieldEditPart part = new DBLocalInputFieldEditPart(DBCheckboxFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UICheckboxModel)
		{
		  GroupElementEditPart part = new GroupElementEditPart(CheckboxFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UICaptionModel)
		{
			CaptionEditPart part = new CaptionEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBTimeModel)
		{
		  DBLocalInputFieldEditPart part = new DBLocalInputFieldEditPart(DBTimeFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UITimeModel)
		{
		  GroupElementEditPart part = new GroupElementEditPart(TimeFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBDateModel)
		{
      GroupElementEditPart part = new DBDateEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDateModel)
		{
      GroupElementEditPart part = new DateEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBImageModel)
		{
		  DBLocalInputFieldEditPart part = new DBLocalInputFieldEditPart(DBImageFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBDocumentModel)
		{
		  DBLocalInputFieldEditPart part = new DBLocalInputFieldEditPart(DocumentFigure.class);
			part.setModel(model);
			return part;
		}
    if(model instanceof UIStaticImageModel)
    {
      GroupElementEditPart part = new StaticImageEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UITimeLineModel)
    {
      GroupElementEditPart part = new GroupElementEditPart(TimeLineFigure.class);
      part.setModel(model);
      return part;
    }
    if(model instanceof UICalendarModel)
    {
      GroupElementEditPart part = new GroupElementEditPart(CalendarFigure.class);
      part.setModel(model);
      return part;
    }
		if(model instanceof UIImageModel)
		{
		  GroupElementEditPart part = new GroupElementEditPart(ImageFigure.class);
			part.setModel(model);
			return part;
		}
    if(model instanceof UIPluginComponentModel)
    {
      UIPluginComponentModel cm = (UIPluginComponentModel)model;
      IComponentPlugin plugin =PluginComponentManager.getComponentPlugin(cm.getJavaImplClass());
      PluginElementEditPart part = new PluginElementEditPart(plugin.getFigureClass());
      part.setModel(model);
      return part;
    }
		if(model instanceof UILineChartModel)
		{
      AbstractChartEditPart part = new AbstractChartEditPart(LineChartFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UICanvasModel)
		{
		  GroupElementEditPart part = new GroupElementEditPart(CanvasFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UIBarChartModel)
		{
      AbstractChartEditPart part = new AbstractChartEditPart(BarChartFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBDateTimeModel)
		{
		  DBLocalInputFieldEditPart part = new DBDateTimeEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDateTimeModel)
		{
		  GroupElementEditPart part = new DateTimeEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBEnumComboboxModel)
		{
		  DBLocalInputFieldEditPart part = new DBComboboxEditPart();
			part.setModel(model);
			return part;
		}
    if(model instanceof UIDBTextfieldComboboxModel)
    {
      DBLocalInputFieldEditPart part = new DBTextfieldComboboxEditPart();
      part.setModel(model);
      return part;
    }
		if(model instanceof UIComboboxModel)
		{
		  GroupElementEditPart part = new ComboboxEditPart();
			part.setModel(model);
			return part;
		}
    if(model instanceof UIDBRadioButtonGroupModel)
    {
      DBRadioButtonGroupEditPart part = new DBRadioButtonGroupEditPart(DBRadioButtonGroupFigure.class);
      part.setModel(model);
      return part;
    }
    if(model instanceof UIRadioButtonGroupModel)
    {
      GroupElementEditPart part = new GroupElementEditPart(RadioButtonGroupFigure.class);
      part.setModel(model);
      return part;
    }
		if(model instanceof UIDBListboxModel)
		{
		  DBLocalInputFieldEditPart part = new DBListboxEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIListboxModel)
		{
		  GroupElementEditPart part = new ListboxEditPart();
			part.setModel(model);
			return part;
		}
    if(model instanceof UIDBTableListBoxModel)
    {
      DBTableListBoxEditPart part = new DBTableListBoxEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UITabStripModel)
    {
      GroupElementEditPart part = new TabStripEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UITabModel)
    {
      ObjectEditPart part = new TabEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UITabPanesModel)
    {
      ObjectEditPart part = new TabPanesEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UITabContainerModel)
    {
      GroupElementEditPart part = new TabContainerEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UIStackContainerModel)
    {
      GroupElementEditPart part = new StackContainerEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UIBreadCrumbModel) // muss vor UILabelModel sein, da UIBreadCrumbModel instanceof ist
    {
      LabelEditPart part = new BreadCrumbEditPart();
      part.setModel(model);
      return part;
    }
		if(model instanceof UIHeadingModel) // muss vor UILabelModel sein, da UIHeadingModel instanceof ist
		{
			LabelEditPart part = new HeadingEditPart();
			part.setModel(model);
			return part;
		}
    if(model instanceof UILabelModel)
    {
      LabelEditPart part = new LabelEditPart();
      part.setModel(model);
      return part;
    }
    if(model instanceof UIStyledTextModel)
    {
      StyledTextEditPart part = new StyledTextEditPart();
      part.setModel(model);
      return part;
    }
		if(model instanceof UIDBLabelModel)
		{
		  DBLocalInputFieldEditPart part = new DBLocalInputFieldEditPart(LabelFigure.class);
			part.setModel(model);
			return part;
		}
		if(model instanceof UIButtonModel)
		{
		  ButtonEditPart part = new ButtonEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBInformBrowserModel)
		{
			InformBrowserEditPart part = new InformBrowserEditPart();
			part.setModel(model);
			return part;
		}
		if(model instanceof UIDBForeignFieldModel)
		{
			DBForeignFieldEditPart part = new DBForeignFieldEditPart();
			part.setModel(model);
			return part;
		}
    System.out.println("ERROR: no EditPart for:"+model);
		return null;
	}
}
