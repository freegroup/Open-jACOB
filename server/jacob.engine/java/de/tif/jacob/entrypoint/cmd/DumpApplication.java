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

package de.tif.jacob.entrypoint.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.birt.report.engine.api.script.eventhandler.IImageEventHandler;

import de.tif.jacob.core.definition.IContextMenuEntry;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.IFormDefinition;
import de.tif.jacob.core.definition.IGUIElementDefinition;
import de.tif.jacob.core.definition.IGroupDefinition;
import de.tif.jacob.core.definition.INamedObjectDefinition;
import de.tif.jacob.core.definition.actiontypes.ActionTypeDeleteRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeGeneric;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeRecordSelected;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.core.definition.actiontypes.ActionTypeUpdateRecord;
import de.tif.jacob.core.definition.guielements.ButtonDefinition;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.CheckBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.ComboBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DateInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DocumentInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.ForeignInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.ImageDefinition;
import de.tif.jacob.core.definition.guielements.InFormLongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LabelDefinition;
import de.tif.jacob.core.definition.guielements.ListBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.PasswordInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.RadioButtonGroupInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TableListBoxDefinition;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TimeInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TimestampInputFieldDefinition;
import de.tif.jacob.core.definition.impl.AbstractApplicationDefinition;
import de.tif.jacob.core.definition.impl.AbstractGuiElement;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;
import de.tif.jacob.screen.event.IComboBoxEventHandler;
import de.tif.jacob.screen.event.IDateEventHandler;
import de.tif.jacob.screen.event.IDateTimeEventHandler;
import de.tif.jacob.screen.event.IDocumentFieldEventHandler;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;
import de.tif.jacob.screen.event.IFormEventHandler;
import de.tif.jacob.screen.event.IListBoxEventHandler;
import de.tif.jacob.screen.event.IPasswordFieldEventHandler;
import de.tif.jacob.screen.event.IRadioButtonGroupEventHandler;
import de.tif.jacob.screen.event.ITableListBoxEventHandler;
import de.tif.jacob.screen.event.ITextFieldEventHandler;
import de.tif.jacob.transformer.ITransformer;
import de.tif.jacob.transformer.TransformerFactory;
import de.tif.jacob.util.StringUtil;
import de.tif.qes.IQeScriptContainer;
import de.tif.qes.adf.ADFForeignInputFieldDefinition;

/**
 * @author Andreas Herz
 *
 */
public final class DumpApplication implements ICmdEntryPoint
{
  static public final transient String RCS_ID = "$Id: DumpApplication.java,v 1.8 2010-08-03 14:41:44 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";
  
  private static final Map eventHandler = new HashMap();
  
  static
  {
    eventHandler.put(ButtonDefinition.class, IButtonEventHandler.class);
    eventHandler.put(CheckBoxInputFieldDefinition.class, ICheckBoxEventHandler.class);
    eventHandler.put(ComboBoxInputFieldDefinition.class, IComboBoxEventHandler.class);
    eventHandler.put(DateInputFieldDefinition.class, IDateEventHandler.class);
    eventHandler.put(DocumentInputFieldDefinition.class, IDocumentFieldEventHandler.class);
    eventHandler.put(ListBoxInputFieldDefinition.class, IListBoxEventHandler.class);
    eventHandler.put(RadioButtonGroupInputFieldDefinition.class, IRadioButtonGroupEventHandler.class);
    eventHandler.put(ImageDefinition.class, IImageEventHandler.class);
    eventHandler.put(InFormLongTextInputFieldDefinition.class, ITextFieldEventHandler.class);
    eventHandler.put(LongTextInputFieldDefinition.class, null);
    eventHandler.put(PasswordInputFieldDefinition.class, IPasswordFieldEventHandler.class);
    eventHandler.put(TextInputFieldDefinition.class, ITextFieldEventHandler.class);
    eventHandler.put(TimeInputFieldDefinition.class, null);
    eventHandler.put(TimestampInputFieldDefinition.class, IDateTimeEventHandler.class);
    eventHandler.put(ForeignInputFieldDefinition.class, IForeignFieldEventHandler.class);
    eventHandler.put(ADFForeignInputFieldDefinition.class, IForeignFieldEventHandler.class);
    eventHandler.put(TableListBoxDefinition.class, ITableListBoxEventHandler.class);

    eventHandler.put(ActionTypeDeleteRecord.class, IActionButtonEventHandler.class);
    eventHandler.put(ActionTypeGeneric.class, IButtonEventHandler.class);
    eventHandler.put(ActionTypeRecordSelected.class, IButtonEventHandler.class);
    eventHandler.put(ActionTypeSearch.class, IActionButtonEventHandler.class);
    eventHandler.put(ActionTypeNewRecord.class, IActionButtonEventHandler.class);
    eventHandler.put(ActionTypeUpdateRecord.class, IActionButtonEventHandler.class);
  }
  
	public void enter(CmdEntryPointContext context, Properties props) throws Exception
  {
    String[] header = new String[] {
        "applicationName", "Focus/Domain", "Form", "Group", "GuiElement", "Typ", "is implemented", "Interface", "Handler", "QScript file", "QScript name", "QScript type", "all QScriptinfo" };
    Collection data = new ArrayList(1024);

    AbstractApplicationDefinition appDef = (AbstractApplicationDefinition) context.getApplicationDefinition();
    Iterator iter = appDef.getDomainDefinitions().iterator();
    while (iter.hasNext())
    {
      IDomainDefinition domainDef = (IDomainDefinition) iter.next();
      Iterator formIter = domainDef.getFormDefinitions().iterator();
      while (formIter.hasNext())
      {
        IFormDefinition formDef = (IFormDefinition) formIter.next();
        addEntry(data, appDef, domainDef, formDef, null, formDef);
        
        Iterator groupIter = formDef.getGroupDefinitions().iterator();
        while (groupIter.hasNext())
        {
          IGroupDefinition groupDef = (IGroupDefinition) groupIter.next();
          Iterator contextIter = groupDef.getContextMenuEntries().iterator();

          while (contextIter.hasNext())
          {
            IContextMenuEntry guiDef = (IContextMenuEntry) contextIter.next();
            addEntry(data, appDef, domainDef, formDef, groupDef, guiDef);
          }

          Iterator elementIter = groupDef.getGUIElementDefinitions().iterator();
          while (elementIter.hasNext())
          {
            IGUIElementDefinition guiDef = (IGUIElementDefinition) elementIter.next();
            if (guiDef.isVisible())
            {
              addEntry(data, appDef, domainDef, formDef, groupDef, guiDef);
            }
          }
        }
      }
    }
    String[][] d = (String[][]) data.toArray(new String[data.size()][]);
    ITransformer trans = TransformerFactory.get(getMimeType(context, props));
    trans.transform(context.getStream(), header, d);
  }
	
	private void addEntry(Collection data, AbstractApplicationDefinition appDef, IDomainDefinition domainDef, IFormDefinition formDef, IGroupDefinition groupDef, INamedObjectDefinition guiDef)
  {
    String guiName = guiDef.getName();
    String className = guiDef.getClass().getName();
    className = className.substring(className.lastIndexOf('.') + 1);
    
    Object handlerClass;
    if (guiDef instanceof ButtonDefinition)
    {
      ButtonDefinition button = (ButtonDefinition) guiDef;
      handlerClass = eventHandler.get(button.getActionType().getClass());
    }
    else if (guiDef instanceof IContextMenuEntry)
    {
      IContextMenuEntry entry = (IContextMenuEntry) guiDef;
      handlerClass = eventHandler.get(entry.getActionType().getClass());
    }
    else if (guiDef instanceof IFormDefinition)
    {
      handlerClass = IFormEventHandler.class;
    }
    else
    {
      handlerClass = eventHandler.get(guiDef.getClass());
    }

    String interfaceName = null;
    if (handlerClass != null)
      interfaceName = ((Class) handlerClass).getName();

    String handlerClassName;
    boolean hasHandler;
    if (appDef.lookupEventHandlerByReference())
    {
      handlerClassName = StringUtil.toSaveString(((AbstractGuiElement) guiDef).getEventHandler());
      hasHandler = handlerClassName.length() > 0;
    }
    else
    {
      // see de.tif.jacob.screen.impl.GuiElement#getEventHandler(HTTPApplication app, IDomain domain, IForm form, IGuiElement element)
      if (guiDef instanceof IFormDefinition)
        handlerClassName = "jacob.common.gui." + StringUtils.capitalise(guiName);
      else
        handlerClassName = "jacob.event.screen." + domainDef.getName() + "." + formDef.getName() + "." + StringUtils.capitalise(guiName);
      
      hasHandler = ClassProvider.getInstance(appDef, handlerClassName) != null;
    }

    if (!((guiDef instanceof Caption) || (guiDef instanceof LabelDefinition || interfaceName == null)))
    {
      String qscripts = guiDef.getProperty(IQeScriptContainer.SCRIPTS_PROPERTY);
      String qfile = guiDef.getProperty(IQeScriptContainer.SCRIPT_FILE_PROPERTY);
      String qname = guiDef.getProperty(IQeScriptContainer.SCRIPT_NAME_PROPERTY);
      String qtype = guiDef.getProperty(IQeScriptContainer.SCRIPT_TYPE_PROPERTY);
      data.add(new String[] {
          appDef.getTitle(), domainDef.getTitle(), formDef.getLabel(), groupDef == null ? null : groupDef.getLabel(), //
          guiName, className, "" + hasHandler, interfaceName, handlerClassName, qfile, qname, qtype, qscripts });
    }
  }
	
  /* 
   * @see de.tif.jacob.entrypoint.ICmdEntryPoint#getMimeType()
   */
  public String getMimeType(CmdEntryPointContext context, Properties props)
  {
    return "application/excel";
  }

}
