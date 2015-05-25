/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.screen.impl;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.IExternalFormDefinition;
import de.tif.jacob.core.definition.IFormGroupDefinition;
import de.tif.jacob.core.definition.IGroupContainerDefinition;
import de.tif.jacob.core.definition.IHtmlFormDefinition;
import de.tif.jacob.core.definition.IHtmlGroupDefinition;
import de.tif.jacob.core.definition.IJacobFormDefinition;
import de.tif.jacob.core.definition.IJacobGroupDefinition;
import de.tif.jacob.core.definition.IMutableFormDefinition;
import de.tif.jacob.core.definition.IMutableGroupDefinition;
import de.tif.jacob.core.definition.IToolbarButtonDefinition;
import de.tif.jacob.core.definition.IToolbarDefinition;
import de.tif.jacob.core.definition.guielements.BarChartDefinition;
import de.tif.jacob.core.definition.guielements.BreadCrumbDefinition;
import de.tif.jacob.core.definition.guielements.ButtonDefinition;
import de.tif.jacob.core.definition.guielements.CalendarDefinition;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.CheckBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.ComboBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DateInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DocumentInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DynamicImageDefinition;
import de.tif.jacob.core.definition.guielements.FlowLayoutContainerDefinition;
import de.tif.jacob.core.definition.guielements.ForeignInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.HeadingDefinition;
import de.tif.jacob.core.definition.guielements.ImageDefinition;
import de.tif.jacob.core.definition.guielements.InFormBrowserDefinition;
import de.tif.jacob.core.definition.guielements.InFormLongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LabelDefinition;
import de.tif.jacob.core.definition.guielements.LineChartDefinition;
import de.tif.jacob.core.definition.guielements.ListBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.OwnDrawElementDefinition;
import de.tif.jacob.core.definition.guielements.PasswordInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.PluginComponentDefinition;
import de.tif.jacob.core.definition.guielements.RadioButtonGroupInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.StackContainerDefinition;
import de.tif.jacob.core.definition.guielements.StaticImageDefinition;
import de.tif.jacob.core.definition.guielements.StyledTextDefinition;
import de.tif.jacob.core.definition.guielements.TabContainerDefinition;
import de.tif.jacob.core.definition.guielements.TableListBoxDefinition;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TimeInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TimeLineDefinition;
import de.tif.jacob.core.definition.guielements.TimestampInputFieldDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBarChart;
import de.tif.jacob.screen.IBreadCrumb;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.ICalendar;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IDate;
import de.tif.jacob.screen.IDateTime;
import de.tif.jacob.screen.IDocument;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IDynamicImage;
import de.tif.jacob.screen.IExternalForm;
import de.tif.jacob.screen.IFlowLayoutContainer;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGroupContainer;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IHeading;
import de.tif.jacob.screen.IImage;
import de.tif.jacob.screen.IInFormBrowser;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.ILineChart;
import de.tif.jacob.screen.IListBox;
import de.tif.jacob.screen.ILongText;
import de.tif.jacob.screen.IOwnDrawElement;
import de.tif.jacob.screen.IPassword;
import de.tif.jacob.screen.IPluginComponent;
import de.tif.jacob.screen.IRadioButtonGroup;
import de.tif.jacob.screen.IStackContainer;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.IStyledText;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.ITime;
import de.tif.jacob.screen.ITimeLine;
import de.tif.jacob.screen.IToolbar;
import de.tif.jacob.screen.IToolbarButton;
import de.tif.jacob.security.IUser;

/**
 * @author Andreas Herz
 *
 */
public interface IApplicationFactory
{
  static public final String RCS_ID = "$Id: IApplicationFactory.java,v 1.18 2010/08/03 15:18:54 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.18 $";

  public IApplication    createApplication(IUser user, IApplicationDefinition app) throws InstantiationException;
  public IDomain         createDomain(IApplication app, IDomainDefinition domain);
  public IForm           createForm(IApplication app, IGuiElement parent, IJacobFormDefinition htmlForm);
  public IForm           createForm(IApplication app, IGuiElement parent, IHtmlFormDefinition htmlForm);
  public IForm           createForm(IApplication app, IGuiElement parent, IMutableFormDefinition form);
  public IExternalForm   createForm(IApplication app, IGuiElement parent, IExternalFormDefinition form);
  public IFormGroup      createFormGroup(IApplication app, IGuiElement parent, IFormGroupDefinition formGroup);
  public IGroup          createGroup(IApplication app, IGuiElement parent, IJacobGroupDefinition group);       
  public IGroup          createGroup(IApplication app, IGuiElement parent, IMutableGroupDefinition group);       
  public IGroup          createGroup(IApplication app, IGuiElement parent, IHtmlGroupDefinition group);       
  public IGroupContainer createGroupContainer(IApplication app, IGuiElement parent, IGroupContainerDefinition definition);       
  public IText           createText(IApplication app, IGuiElement parent, TextInputFieldDefinition text);
  public IPassword       createPassword(IApplication app, IGuiElement parent, PasswordInputFieldDefinition password);
  public IButton         createButton(IApplication app, IGuiElement parent, ButtonDefinition button);
  public ICaption        createCaption(IApplication app, IGuiElement parent, Caption caption);
  public ILabel          createLabel(IApplication app, IGuiElement parent, LabelDefinition item);
  public IHeading        createHeading(IApplication app, IGuiElement parent, HeadingDefinition item);
  public IBreadCrumb     createBreadCrumb(IApplication app, IGuiElement parent, BreadCrumbDefinition item);
  public IImage          createImage(IApplication app, IGuiElement parent, ImageDefinition item);
  public IStaticImage    createStaticImage(IApplication app, IGuiElement parent, StaticImageDefinition item);
  public IDynamicImage   createDynamicImage(IApplication app, IGuiElement parent, DynamicImageDefinition item);
  public IOwnDrawElement createOwnDrawElement(IApplication app, IGuiElement parent, OwnDrawElementDefinition item);
  public ILineChart      createLineChart(IApplication app, IGuiElement parent, LineChartDefinition item);
  public IBarChart       createBarChart(IApplication app, IGuiElement parent, BarChartDefinition item);
  public IComboBox       createComboBox(IApplication app, IGuiElement parent, ComboBoxInputFieldDefinition item);
  public IListBox        createListBox(IApplication app, IGuiElement parent, ListBoxInputFieldDefinition item);
  public ITableListBox   createTableListBox(IApplication app, IGuiElement parent, TableListBoxDefinition item);
  public ICheckBox       createCheckBox(IApplication app, IGuiElement parent, CheckBoxInputFieldDefinition item);
  public ILongText       createLongText(IApplication app, IGuiElement parent, LongTextInputFieldDefinition item);
  public IInFormLongText createInFormLongText(IApplication app, IGuiElement parent, InFormLongTextInputFieldDefinition item);
  public IInFormBrowser  createInFormBrowser(IApplication app, IGuiElement parent, InFormBrowserDefinition item);
  public IForeignField   createForeignField(IApplication app, IGuiElement parent, ForeignInputFieldDefinition item);
  public IDateTime       createDateTime(IApplication app, IGuiElement parent, TimestampInputFieldDefinition item);
  public IDate           createDate(IApplication app, IGuiElement parent, DateInputFieldDefinition item);
  public ITime           createTime(IApplication app, IGuiElement parent, TimeInputFieldDefinition item);
  public IDocument       createDocument(IApplication app, IGuiElement parent, DocumentInputFieldDefinition item);
  public IToolbar        createToolbar(IApplication app, IGuiElement parent, IToolbarDefinition item);
  public IToolbarButton  createToolbarButton(IApplication app, IGuiElement parent, IToolbarButtonDefinition item);
  public ITabContainer   createTabContainer(IApplication app, IGuiElement parent, TabContainerDefinition def);
  public IStackContainer createStackContainer(IApplication app, IGuiElement parent, StackContainerDefinition def);
  public IGroup          createTabPane(IApplication app, IGuiElement parent, IJacobGroupDefinition group);       
  public IRadioButtonGroup  createRadioButtonGroup(IApplication app, IGuiElement parent, RadioButtonGroupInputFieldDefinition item);
  public IFlowLayoutContainer      createFlowLayoutContainer(IApplication app, IGuiElement parent, FlowLayoutContainerDefinition definition);
  public IStyledText     createStyledText(IApplication app, IGuiElement parent, StyledTextDefinition definition);
  public ITimeLine       createTimeLine(IApplication app, IGuiElement parent, TimeLineDefinition definition);
  public ICalendar       createCalendar(IApplication app, IGuiElement parent, CalendarDefinition definition);
  public IPluginComponent createPluginComponent(IApplication app, IGuiElement parent, PluginComponentDefinition definition);
}
