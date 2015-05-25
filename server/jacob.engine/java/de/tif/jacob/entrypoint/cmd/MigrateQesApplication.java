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
/*
 * Created on 12.07.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package de.tif.jacob.entrypoint.cmd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IApplicationProvider;
import de.tif.jacob.core.definition.impl.AbstractApplicationProvider;
import de.tif.jacob.core.definition.impl.jad.JadFileApplicationProvider;
import de.tif.jacob.core.definition.impl.jad.castor.Applications;
import de.tif.jacob.core.definition.impl.jad.castor.CastorApplication;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomain;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTable;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry;
import de.tif.jacob.core.definition.impl.jad.castor.Domains;
import de.tif.jacob.core.definition.impl.jad.castor.Forms;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextInput;
import de.tif.jacob.core.definition.impl.jad.castor.TableAliases;
import de.tif.jacob.core.definition.impl.jad.castor.Tables;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextEditModeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.util.ObjectUtil;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public final class MigrateQesApplication implements ICmdEntryPoint
{
  static public final transient String RCS_ID = "$Id: MigrateQesApplication.java,v 1.1 2008-10-01 10:21:28 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  static private final Log logger = LogFactory.getLog(MigrateQesApplication.class);

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.entrypoint.ICmdEntryPoint#getMimeType(de.tif.jacob.entrypoint.CmdEntryPointContext,
   *      java.util.Properties)
   */
  public String getMimeType(CmdEntryPointContext context, Properties properties)
  {
    return "application/xml";
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.entrypoint.ICmdEntryPoint#enter(de.tif.jacob.entrypoint.CmdEntryPointContext,
   *      java.util.Properties)
   */
  public void enter(CmdEntryPointContext context, Properties properties) throws Exception
  {
    IApplicationDefinition def = context.getApplicationDefinition();

    IApplicationProvider provider = DeployMain.getApplicationProvider(def.getName(), def.getVersion().toString());
    if (provider instanceof JadFileApplicationProvider)
    {
      logger.info("Start");

      JadFileApplicationProvider jadProvider = (JadFileApplicationProvider) provider;

      Jacob jacob = jadProvider.getJacob();

      Map tableMap = new HashMap();
      {
        Tables tables = jacob.getTables();
        for (int i = 0; i < tables.getTableCount(); i++)
        {
          CastorTable table = tables.getTable(i);
          tableMap.put(table.getName(), table);
        }
      }

      Map domainMap = new HashMap();
      {
        Domains domains = jacob.getDomains();
        for (int i = 0; i < domains.getDomainCount(); i++)
        {
          CastorDomain domain = domains.getDomain(i);
          domainMap.put(domain.getName(), domain);
        }
      }

      Map formMap = new HashMap();
      {
        Forms forms = jacob.getForms();
        for (int i = 0; i < forms.getFormCount(); i++)
        {
          CastorJacobForm form = forms.getForm(i);
          formMap.put(form.getName(), form);
        }
      }

      Applications applications = jacob.getApplications();
      for (int i = 0; i < applications.getApplicationCount(); i++)
      {
        CastorApplication application = applications.getApplication(i);

        boolean convertToReference = CastorApplicationEventHandlerLookupMethodType.NAME.equals(application.getEventHandlerLookupMethod());
        if (convertToReference)
        {
          logger.info("Converting application '" + application.getName() + "' to reference lookup");

          application.setEventHandlerLookupMethod(CastorApplicationEventHandlerLookupMethodType.REFERENCE);
          application.setEventHandler(getEventHandler(application, def, getEventClassName(application.getName()), application.getEventHandler()));
        }
        else
        {
          logger.info("SKIPPED: Converting application '" + application.getName() + "' to reference lookup");
        }
        
        Set castorTableFieldSet = new HashSet();

        for (int j = 0; j < application.getDomainCount(); j++)
        {
          CastorDomainRef domainRef = application.getDomain(j);

          CastorDomain domain = (CastorDomain) domainMap.get(domainRef.getContent());
          if (convertToReference)
            domain.setEventHandler(getEventHandler(domain, def, getEventClassName2(domain.getName()), domain.getEventHandler()));

          for (int k = 0; k < domain.getFormCount(); k++)
          {
            CastorJacobForm form = (CastorJacobForm) formMap.get(domain.getForm(k));
            if (convertToReference)
              form.setEventHandler(getEventHandler(form, def, getEventClassName2(form.getName()), form.getEventHandler()));

            for (int l = 0; l < form.getGroupCount(); l++)
            {
              CastorGroup group = form.getGroup(l);
              if (convertToReference)
                group.setEventHandler(getEventHandler(group, def, getEventClassName(domain, form, group.getName()), group.getEventHandler()));

              for (int m = 0; m < group.getGuiElementCount(); m++)
              {
                CastorGuiElement guielement = group.getGuiElement(m);
                if (convertToReference)
                  guielement.setEventHandler(getEventHandler(guielement, def, getEventClassName(domain, form, guielement.getName()), guielement.getEventHandler()));
                
                // Handle Longtextmode: From GUI -> Data
                //
                LocalInputField localInputField = guielement.getCastorGuiElementChoice().getLocalInputField();
                if (localInputField != null && localInputField.getLongTextInput() != null)
                {
                  LongTextInput longtextinput = localInputField.getLongTextInput();
                  TextInputModeType textinputmode = longtextinput.getMode();
                  if (textinputmode != null)
                  {
                    String tablename = getTableAlias(jacob, group.getAlias()).getTable();
                    String fieldname = localInputField.getTableField();

                    CastorTableField tablefield = getTableField(jacob, tablename, fieldname);
                    
                    LongTextEditModeType longtexteditmode;
                    boolean readonly;
                    if (textinputmode.equals(TextInputModeType.READONLY))
                    {
                      readonly = true;
                      longtexteditmode = null;
                    }
                    else if (textinputmode.equals(TextInputModeType.FULLEDIT))
                    {
                      readonly = false;
                      longtexteditmode = LongTextEditModeType.FULLEDIT;
                    }
                    else if (textinputmode.equals(TextInputModeType.APPEND))
                    {
                      readonly = false;
                      longtexteditmode = LongTextEditModeType.APPEND;
                    }
                    else if (textinputmode.equals(TextInputModeType.PREPEND))
                    {
                      readonly = false;
                      longtexteditmode = LongTextEditModeType.PREPEND;
                    }
                    else
                      throw new RuntimeException("Unknown Mode");
                    
                    boolean alreadyProcessed = !castorTableFieldSet.add(tablefield);
                    LongTextEditModeType oldlongtexteditmode = tablefield.getCastorTableFieldChoice().getLongText().getEditMode();
                    if (!ObjectUtil.equals(longtexteditmode, oldlongtexteditmode))
                    {
                      if (alreadyProcessed)
                      {
                        System.out.println("SKIPPED: Changing Longtextfield " + tablename + "." + fieldname + " editmode from " + oldlongtexteditmode + " to " + longtexteditmode);
                      }
                      else
                      {
                        tablefield.getCastorTableFieldChoice().getLongText().setEditMode(longtexteditmode);
                        System.out.println("Changing Longtextfield " + tablename + "." + fieldname + " editmode from " + oldlongtexteditmode + " to " + longtexteditmode);
                      }
                    }
                    if (readonly ^ tablefield.getReadonly())
                    {
                      if (alreadyProcessed)
                      {
                        System.out.println("SKIPPED: Changing Longtextfield " + tablename + "." + fieldname + " readonly flag from " + tablefield.getReadonly() + " to " + readonly);
                      }
                      else
                      {
                        System.out.println("Changing Longtextfield " + tablename + "." + fieldname + " readonly flag from " + tablefield.getReadonly() + " to " + readonly);
                        tablefield.setReadonly(readonly);
                      }
                    }

                    longtextinput.setMode(null);
                  }
                }
              }

              for (int m = 0; m < group.getContextMenuEntryCount(); m++)
              {
                ContextMenuEntry contextmenuentry = group.getContextMenuEntry(m);
                if (convertToReference)
                  contextmenuentry.setEventHandler(getEventHandler(contextmenuentry, def, getEventClassName(domain, form, contextmenuentry.getName()), contextmenuentry.getEventHandler()));
              }
            }
          }
        }
      }

      logger.info("End");

      AbstractApplicationProvider.printFormated(jacob, context.getStream(), "ISO-8859-1");
    }
    else
    {
      throw new Exception("Provider is not a JadFileApplicationProvider");
    }
  }
  
  private static CastorTableField getTableField(Jacob jacob, String tablename, String fieldname)
  {
    Tables tables = jacob.getTables();
    for (int i = 0; i < tables.getTableCount(); i++)
    {
      CastorTable table = tables.getTable(i);
      if (tablename.equals(table.getName()))
      {
        for (int j = 0; j < table.getFieldCount(); j++)
        {
          CastorTableField tablefield = table.getField(j);
          if (fieldname.equals(tablefield.getName()))
            return tablefield;
        }
      }
    }
    throw new RuntimeException("Can not find table field " + tablename + "." + fieldname);
  }

  private static CastorTableAlias getTableAlias(Jacob jacob, String aliasname)
  {
    TableAliases aliases = jacob.getTableAliases();
    for (int i = 0; i < aliases.getTableAliasCount(); i++)
    {
      CastorTableAlias alias = aliases.getTableAlias(i);
      if (aliasname.equals(alias.getName()))
      {
        return alias;
      }
    }
    throw new RuntimeException("Can not find table alias " + aliasname);
  }

  private static String getEventHandler(Object element, IApplicationDefinition def, String eventClassName, String oldEventHandler)
  {
    GuiEventHandler eh = (GuiEventHandler) ClassProvider.getInstance(def, eventClassName);
    String eventHandler = eh == null ? null : eventClassName;
    if (oldEventHandler != null)
    {
      if (eventHandler == null)
        System.out.println(ClassUtil.getShortClassName(element.getClass()) + ": Missing  eventClass  : " + eventClassName + " (but " + oldEventHandler + " existing)");
      else
        System.out.println(ClassUtil.getShortClassName(element.getClass()) + ": Skipping eventHandler: " + eventClassName + " (in favour of " + oldEventHandler + ")");
      return oldEventHandler;
    }
    return eventHandler;
  }

  private static final String BY_NAME_ROOT_CLASS_NAME = "jacob.event.screen";

  /**
   * For domains and forms the designer generates a single eventhandler class under
   * the following path!!!
   */
  private static final String BY_NAME_ROOT_CLASS_NAME2 = "jacob.common.gui";

  private static String getEventClassName2(String domainOrFormName)
  {
    // if (element instanceof IDomain || element instanceof IForm)
    return BY_NAME_ROOT_CLASS_NAME2 + "." + StringUtils.capitalise(domainOrFormName);
  }

  private static String getEventClassName(String noParentElementName)
  {
    // e.g. a toolbar button. They have no form, group or domain.
    return BY_NAME_ROOT_CLASS_NAME + "." + StringUtils.capitalise(noParentElementName);
  }

  private static String getEventClassName(CastorDomain domain, CastorJacobForm form, String elementName)
  {
    return BY_NAME_ROOT_CLASS_NAME + "." + domain.getName() + "." + form.getName() + "." + StringUtils.capitalise(elementName);
  }

}
