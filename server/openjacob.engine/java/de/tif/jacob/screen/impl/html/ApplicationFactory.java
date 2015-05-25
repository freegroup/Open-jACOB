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

package de.tif.jacob.screen.impl.html;

import java.util.Iterator;

import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.IExternalFormDefinition;
import de.tif.jacob.core.definition.IFormGroupDefinition;
import de.tif.jacob.core.definition.IGUIElementDefinition;
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
import de.tif.jacob.core.definition.guielements.InputFieldDefinition;
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
import de.tif.jacob.screen.IHtmlForm;
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
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas Herz
 *
 */
public abstract class ApplicationFactory implements IApplicationFactory
{
  static public final transient String RCS_ID = "$Id: ApplicationFactory.java,v 1.29 2010/08/03 15:18:53 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.29 $";

	/* 
	 * @see de.tif.jacob.screen.IApplicationFactory#createDomain(de.tif.jacob.core.definition.IApplicationDefinition, de.tif.jacob.core.definition.IDomainDefinition)
	 * @author Andreas Herz
	 */
	public IDomain createDomain(IApplication app, IDomainDefinition domain)
	{
    IDomain guiDomain = new Domain(app, domain);
    ((GuiHtmlElement)app).addChild(guiDomain);
		return guiDomain;
	}

	/* 
	 * @see de.tif.jacob.screen.IApplicationFactory#createForm(de.tif.jacob.screen.IDomain, de.tif.jacob.core.definition.IFormDefinition)
	 */
	public IForm createForm(IApplication app, IGuiElement parent,  IJacobFormDefinition form)
	{
    IForm item = new de.tif.jacob.screen.impl.html.Form(app,  form);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
	}

  /* 
   * @see de.tif.jacob.screen.IApplicationFactory#createForm(de.tif.jacob.screen.IDomain, de.tif.jacob.core.definition.IFormDefinition)
   */
  public IForm createForm(IApplication app, IGuiElement parent,  IMutableFormDefinition form)
  {
    IForm item = new de.tif.jacob.screen.impl.html.MutableForm(app,  form);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  /* 
   * @see de.tif.jacob.screen.IApplicationFactory#createForm(de.tif.jacob.screen.IDomain, de.tif.jacob.core.definition.IFormDefinition)
   */
  public IExternalForm createForm(IApplication app, IGuiElement parent,  IExternalFormDefinition form)
  {
    IExternalForm item = new de.tif.jacob.screen.impl.html.ExternalForm(app,  form);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  public IForm createForm(IApplication app, IGuiElement parent, IHtmlFormDefinition htmlForm)
  {
    IHtmlForm item = new de.tif.jacob.screen.impl.html.HtmlForm(app,  htmlForm);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  public IFormGroup createFormGroup(IApplication app, IGuiElement parent,  IFormGroupDefinition formGroup)
  {
    IFormGroup item = new de.tif.jacob.screen.impl.html.FormGroup(app,  formGroup);
// WICHTIG: Ein FormGroup darf nicht an den Vater geh�ngt werden.
//          Dies liegt in der Kompatibilit�t zu Version 2.6 und den bestehenden Themes begr�ndet
//    
//    if(parent!=null)
//      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IGroup createGroup(IApplication app, IGuiElement parent,  IJacobGroupDefinition group)
  {
   IGroup item = new de.tif.jacob.screen.impl.html.JacobGroup(app, group); 
   if(parent!=null)
     ((GuiHtmlElement)parent).addChild(item);
   return item;
  }
  
  
  public IGroup createGroup(IApplication app, IGuiElement parent,  IMutableGroupDefinition group)
  {
   IGroup item = new de.tif.jacob.screen.impl.html.MutableGroup(app, group); 
   if(parent!=null)
     ((GuiHtmlElement)parent).addChild(item);
   return item;
  }
  
  public IGroup createGroup(IApplication app, IGuiElement parent,  IHtmlGroupDefinition group)
  {
   IGroup item = new de.tif.jacob.screen.impl.html.HtmlGroup(app, group); 
   if(parent!=null)
     ((GuiHtmlElement)parent).addChild(item);
   return item;
  }

  
  public IGroupContainer createGroupContainer(IApplication app, IGuiElement parent, IGroupContainerDefinition definition)
  {
    IGroupContainer item = new de.tif.jacob.screen.impl.html.GroupContainer(app, definition); 
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  public IGroup createTabPane(IApplication app, IGuiElement parent,  IJacobGroupDefinition group)
  {
   de.tif.jacob.screen.impl.html.JacobGroup item = new de.tif.jacob.screen.impl.html.JacobGroup(app, group);
   if(parent!=null)
     ((GuiHtmlElement)parent).addChild(item);
   item.setTabPane();
   return item;
  }
    
  public IText createText(IApplication app, IGuiElement parent,  TextInputFieldDefinition text)
  {
  	IText item = new de.tif.jacob.screen.impl.html.Text(app,  text);
  	if(parent!=null)
  	  ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IPassword createPassword(IApplication app, IGuiElement parent,  PasswordInputFieldDefinition text)
  {
    IPassword item = new de.tif.jacob.screen.impl.html.Password(app,  text);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IOwnDrawElement createOwnDrawElement(IApplication app, IGuiElement parent,  OwnDrawElementDefinition element)
  {
  	IOwnDrawElement item = new OwnDrawElement(app,  element);
  	if(parent!=null)
  	  ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IFlowLayoutContainer createFlowLayoutContainer(IApplication app, IGuiElement parent, FlowLayoutContainerDefinition def)
  {
    IFlowLayoutContainer container = new de.tif.jacob.screen.impl.html.FlowLayoutContainer(app,  def);
    ((GuiHtmlElement)parent).addChild(container);
    
    Iterator iter = def.getGuiElementDefinitions().iterator();
    while(iter.hasNext())
    {
      IGUIElementDefinition element = (IGUIElementDefinition)iter.next();
      element.createRepresentation(this,app, container);
    }
    return container;
  }

  public IButton  createButton(IApplication app, IGuiElement parent, ButtonDefinition button)
  {
    IButton item = new de.tif.jacob.screen.impl.html.Button(app,  button);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IToolbarButton  createToolbarButton(IApplication app, IGuiElement parent, IToolbarButtonDefinition button)
  {
    IToolbarButton item = new de.tif.jacob.screen.impl.html.ToolbarButton(app,  button);
    
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IToolbar createToolbar(IApplication app, IGuiElement parent, IToolbarDefinition toolbar)
  {
    IToolbar item = new de.tif.jacob.screen.impl.html.Toolbar(app,  toolbar);
    // Toolbar ist KEIN Child eines ELementes. Wird gesondert behandelt.
//    ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public ICaption  createCaption(IApplication app, IGuiElement parent, Caption button)
  {
    ICaption item = new de.tif.jacob.screen.impl.html.Caption(app,  button);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IComboBox  createComboBox(IApplication app, IGuiElement parent, ComboBoxInputFieldDefinition def)
  {
  	ComboBox item = new de.tif.jacob.screen.impl.html.ComboBox(app, def);
  	if(parent!=null)
  	  ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IRadioButtonGroup createRadioButtonGroup(IApplication app, IGuiElement parent, RadioButtonGroupInputFieldDefinition def)
  {
    RadioButtonGroup item = new RadioButtonGroup(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IListBox  createListBox(IApplication app, IGuiElement parent, ListBoxInputFieldDefinition def)
  {
  	IListBox item = new de.tif.jacob.screen.impl.html.ListBox(app, def);
  	if(parent!=null)
  	  ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  

  public ICheckBox createCheckBox(IApplication app, IGuiElement parent, CheckBoxInputFieldDefinition def)
  {
    ICheckBox item = new de.tif.jacob.screen.impl.html.CheckBox(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public ILongText createLongText(IApplication app, IGuiElement parent, LongTextInputFieldDefinition def)
  {
    ILongText item = new de.tif.jacob.screen.impl.html.LongText(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IImage createImage(IApplication app, IGuiElement parent, ImageDefinition def)
  {
    Image item = new de.tif.jacob.screen.impl.html.Image(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  
  public ITableListBox createTableListBox(IApplication app, IGuiElement parent, TableListBoxDefinition item)
  {
    TableListBox listbox= new TableListBox(app, item);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(listbox);
    return listbox;
  }

  public IStaticImage createStaticImage(IApplication app, IGuiElement parent, StaticImageDefinition item)
  {
    StaticImage image= new StaticImage(app, item);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(image);
    return image;
  }
  
  public IDynamicImage createDynamicImage(IApplication app, IGuiElement parent, DynamicImageDefinition item)
  {
    DynamicImage image= new DynamicImage(app, item);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(image);
    return image;
  }
  
  public ILineChart createLineChart(IApplication app, IGuiElement parent, LineChartDefinition item)
  {
    LineChart chart= new LineChart(app, item);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(chart);
    return chart;
  }

  public IBarChart createBarChart(IApplication app, IGuiElement parent, BarChartDefinition item)
  {
    BarChart chart= new BarChart(app, item);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(chart);
    return chart;
  }

  
  public IInFormLongText createInFormLongText(IApplication app, IGuiElement parent, InFormLongTextInputFieldDefinition def)
  {
    IInFormLongText item =new de.tif.jacob.screen.impl.html.InFormLongText(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
 
  public IInFormBrowser createInFormBrowser(IApplication app, IGuiElement parent, InFormBrowserDefinition def)
  {
    InFormBrowser item = new de.tif.jacob.screen.impl.html.InFormBrowser(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    
    // FREEGROUP: as 12.10.07: Wenn man einen Informbrowser nur als Anzeige
    // (entsprechend Searchbrowser!) verwendet, dann braucht man die
    // einzelnen Inputfields nicht und kann auch Felder von
    // Parent-Parent-Tables, usw. anzeigen, was ja bei einem aenderbaren
    // Informbrowser nicht gehen wuerde. Habe dies fuer VE3 gebraucht: Verwaltung
    // -> Benutzergruppe -> Mangelerfasser von.
    if (def.isSearchEnabled() || def.isAddingEnabled() || def.isDeletingEnabled() || def.isUpdatingEnabled())
    {
      Iterator xiter = def.getBrowserToUse().getBrowserFields().iterator();
      while (xiter.hasNext())
      {
        IBrowserField field = (IBrowserField) xiter.next();
        InputFieldDefinition inputFieldDef = field.getInputFieldDefinition(); 
        if (inputFieldDef == null)
        {
          item.addInlineField(null);
          continue;
        }
        IGuiElement element = inputFieldDef.createRepresentation(this, app, null);
        // Die ELemente im InformBrowser haben den selben 'parent' -> die
        // Gruppe.
        // Die Gruppe darf allerdings nichts von dem ELement wissen.???!
        ((SingleDataGUIElement) element).setParent((GuiHtmlElement) parent);
        ((SingleDataGUIElement) element).getDataField().setFieldType(DataField.TYPE_INLINE);
        item.addInlineField(element);
      }
    }
    
    return item;
  }
  
  public ILabel  createLabel(IApplication app, IGuiElement parent, LabelDefinition def)
  {
    ILabel item = new de.tif.jacob.screen.impl.html.Label(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
  public IHeading  createHeading(IApplication app, IGuiElement parent, HeadingDefinition def)
  {
  	IHeading item = new de.tif.jacob.screen.impl.html.Heading(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
 
  public IBreadCrumb createBreadCrumb(IApplication app, IGuiElement parent, BreadCrumbDefinition def)
  {
    IBreadCrumb item = new de.tif.jacob.screen.impl.html.BreadCrumb(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  public IForeignField createForeignField(IApplication app, IGuiElement parent, ForeignInputFieldDefinition def)
  {
   IForeignField item = null;
   if(parent==null)
     item=new  de.tif.jacob.screen.impl.html.InFormBrowserForeignField(app, def);
   else 
   {  
     if(def.asComboBox())
       item=new  de.tif.jacob.screen.impl.html.FFComboBox(app, def);
     else
       item=new  de.tif.jacob.screen.impl.html.ForeignField(app, def);
     ((GuiHtmlElement)parent).addChild(item);
   }
   return item;
 }
  
  public IDateTime  createDateTime(IApplication app, IGuiElement parent, TimestampInputFieldDefinition def)
  {
    IDateTime item = new  de.tif.jacob.screen.impl.html.DateTime(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.IApplicationFactory#createDate(de.tif.jacob.screen.IApplication, de.tif.jacob.screen.IGuiElement, de.tif.jacob.core.definition.guielements.DateInputFieldDefinition)
	 */
	public IDate createDate(IApplication app, IGuiElement parent, DateInputFieldDefinition def)
	{
    IDate item = new  de.tif.jacob.screen.impl.html.Date(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.IApplicationFactory#createTime(de.tif.jacob.screen.IApplication, de.tif.jacob.screen.IGuiElement, de.tif.jacob.core.definition.guielements.TimeInputFieldDefinition)
	 */
	public ITime createTime(IApplication app, IGuiElement parent, TimeInputFieldDefinition def)
	{
    ITime item = new  de.tif.jacob.screen.impl.html.Time(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
	
	public ITabContainer createTabContainer(IApplication app, IGuiElement parent, TabContainerDefinition def)	
  {
    ITabContainer item = new  de.tif.jacob.screen.impl.html.TabContainer(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    
    Iterator groupIter = def.getPaneDefinitions().iterator();
    while(groupIter.hasNext())
    {
      IJacobGroupDefinition group = (IJacobGroupDefinition)groupIter.next();
      IGroup guiGroup = createTabPane(app,item,group); 
      Iterator elementIter = group.getGUIElementDefinitions().iterator();
      while(elementIter.hasNext())
      {
      	IGUIElementDefinition element = (IGUIElementDefinition)elementIter.next();
        element.createRepresentation(this,app, guiGroup);
      }
    }
    return item;
  }

  public IStackContainer createStackContainer(IApplication app, IGuiElement parent, StackContainerDefinition def)
  {
    TabContainer item = new  de.tif.jacob.screen.impl.html.TabContainer(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    
    Iterator groupIter = def.getPaneDefinitions().iterator();
    while(groupIter.hasNext())
    {
      IJacobGroupDefinition group = (IJacobGroupDefinition)groupIter.next();
      IGroup guiGroup = createTabPane(app,item,group); 
      Iterator elementIter = group.getGUIElementDefinitions().iterator();
      while(elementIter.hasNext())
      {
        IGUIElementDefinition element = (IGUIElementDefinition)elementIter.next();
        element.createRepresentation(this,app, guiGroup);
      }
    }
    return item;
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.screen.IApplicationFactory#createTime(de.tif.jacob.screen.IApplication, de.tif.jacob.screen.IGuiElement, de.tif.jacob.core.definition.guielements.TimeInputFieldDefinition)
	 */
	public IDocument createDocument(IApplication app, IGuiElement parent, DocumentInputFieldDefinition def)
	{
    IDocument item = new  de.tif.jacob.screen.impl.html.Document(app, def);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  public IStyledText createStyledText(IApplication app, IGuiElement parent, StyledTextDefinition definition)
  {
    IStyledText item = new  de.tif.jacob.screen.impl.html.StyledText(app, definition);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  public ITimeLine createTimeLine(IApplication app, IGuiElement parent, TimeLineDefinition definition)
  {
    ITimeLine item = new  de.tif.jacob.screen.impl.html.TimeLine(app, definition);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  public ICalendar createCalendar(IApplication app, IGuiElement parent, CalendarDefinition definition)
  {
    ICalendar item = new  de.tif.jacob.screen.impl.html.Calendar(app, definition);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }

  public IPluginComponent createPluginComponent(IApplication app, IGuiElement parent, PluginComponentDefinition definition)
  {
    IPluginComponent item = new PluginComponent(app,definition);
    if(parent!=null)
      ((GuiHtmlElement)parent).addChild(item);
    return item;
  }
  
}



