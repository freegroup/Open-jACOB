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

import java.util.Iterator;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.IExternalFormDefinition;
import de.tif.jacob.core.definition.IFormDefinition;
import de.tif.jacob.core.definition.IFormGroupDefinition;
import de.tif.jacob.core.definition.IGUIElementDefinition;
import de.tif.jacob.core.definition.IGroupContainerDefinition;
import de.tif.jacob.core.definition.IGroupDefinition;
import de.tif.jacob.core.definition.IHtmlFormDefinition;
import de.tif.jacob.core.definition.IHtmlGroupDefinition;
import de.tif.jacob.core.definition.IJacobFormDefinition;
import de.tif.jacob.core.definition.IJacobGroupDefinition;
import de.tif.jacob.core.definition.IMutableFormDefinition;
import de.tif.jacob.core.definition.IMutableGroupDefinition;
import de.tif.jacob.core.definition.IToolbarButtonDefinition;
import de.tif.jacob.core.exception.AuthorizationException;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IExternalForm;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGroupContainer;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IToolbar;
import de.tif.jacob.security.IUser;


/**
 * @author Andreas Herz
 *
 */
public class ApplicationProvider
{
  static public final transient String RCS_ID = "$Id: ApplicationProvider.java,v 1.8 2008/11/22 12:05:28 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";

  public static IApplication create(IUser user, IApplicationFactory factory, IApplicationDefinition app) throws Exception
  {
    IApplication guiApp = factory.createApplication(user, app);

    // create toolbar
    IToolbar toolbar = factory.createToolbar(guiApp,guiApp, app.getToolbarDefinition());
    guiApp.setToolbar(toolbar);
    
    Iterator toolbarButtonIter = app.getToolbarDefinition().getToolbarButtons().iterator();
    while (toolbarButtonIter.hasNext())
    {
      IToolbarButtonDefinition button = (IToolbarButtonDefinition) toolbarButtonIter.next();
      factory.createToolbarButton(guiApp, toolbar, button);
    }
    
    // create domains
    Iterator domainIter = app.getDomainDefinitions().iterator();
    while (domainIter.hasNext())
		{
			IDomainDefinition domain = (IDomainDefinition) domainIter.next();
			
			if(domain.getRoleNames().size()>0 && !user.hasOneRoleOf(domain.getRoleNames()))
				continue;
      HTTPDomain guiDomain = (HTTPDomain)factory.createDomain(guiApp, domain);
      // Alle Formen welche "lose" an der Domain hängen werden angelegt
      //
      Iterator formIter = domain.getFormDefinitions().iterator();
      while(formIter.hasNext())
      {
      	IFormDefinition form = (IFormDefinition)formIter.next();
        if(form instanceof IExternalFormDefinition)
        {
          IExternalFormDefinition jacobForm = (IExternalFormDefinition)form;
          factory.createForm(guiApp, guiDomain, jacobForm);
        }
        else if(form instanceof IMutableFormDefinition)
        {
          IMutableFormDefinition owndrawForm = (IMutableFormDefinition)form;
          IForm guiForm  = factory.createForm(guiApp, guiDomain, owndrawForm);
          Iterator groupIter = owndrawForm.getGroupDefinitions().iterator();
          while(groupIter.hasNext())
          {
            IMutableGroupDefinition group = (IMutableGroupDefinition)groupIter.next();
            factory.createGroup(guiApp, guiForm, group);
          }
        }
        else if(form instanceof IHtmlFormDefinition)
        {
          IHtmlFormDefinition owndrawForm = (IHtmlFormDefinition)form;
          IForm guiForm  = factory.createForm(guiApp, guiDomain, owndrawForm);
          Iterator groupIter = owndrawForm.getGroupDefinitions().iterator();
          while(groupIter.hasNext())
          {
            IHtmlGroupDefinition group = (IHtmlGroupDefinition)groupIter.next();
            factory.createGroup(guiApp, guiForm, group);
          }
        }
        else
        {
          IJacobFormDefinition jacobForm = (IJacobFormDefinition)form;
          IForm guiForm  = factory.createForm(guiApp, guiDomain, jacobForm);
          // Alle normalen Gruppen anlegen
          Iterator groupIter = jacobForm.getGroupDefinitions().iterator();
          while(groupIter.hasNext())
          {
            IJacobGroupDefinition group = (IJacobGroupDefinition)groupIter.next();
            setupGroup(factory, guiApp, guiForm,group);
          }
          Iterator groupContainerIter = jacobForm.getGroupContainerDefinitions().iterator();
          while(groupContainerIter.hasNext())
          {
            IGroupContainerDefinition container = (IGroupContainerDefinition)groupIter.next();
            IGroupContainer guiContainer = factory.createGroupContainer(guiApp,null,container);
            groupIter = jacobForm.getGroupDefinitions().iterator();
            while(groupIter.hasNext())
            {
              IJacobGroupDefinition group = (IJacobGroupDefinition)groupIter.next();
              setupGroup(factory, guiApp, guiContainer, group);
            }
            ((HTTPForm)guiForm).addChild(guiContainer);
          }
          guiForm.resetTabOrder();
        }
      }
      
      // Alle Formen welche innerhalb einer Gruppe sind werden angelegt
      //
      Iterator formGroupIter = domain.getFormGroupDefinitions().iterator();
      while(formGroupIter.hasNext())
      {
        IFormGroupDefinition formGroup = (IFormGroupDefinition)formGroupIter.next();
        HTTPFormGroup guiFormGroup = (HTTPFormGroup)factory.createFormGroup(guiApp, guiDomain,formGroup);
        guiDomain.addFormGroup(guiFormGroup);
        formIter = formGroup.getFormDefinitions().iterator();
        while(formIter.hasNext())
        {
          IFormDefinition form = (IFormDefinition)formIter.next();
          if(form instanceof IExternalFormDefinition)
          {
            IExternalFormDefinition jacobForm = (IExternalFormDefinition)form;
            IExternalForm guiForm  = factory.createForm(guiApp, guiDomain, jacobForm);
            guiFormGroup.addChild(guiForm);
          }
          else if(form instanceof IMutableFormDefinition)
          {
            IMutableFormDefinition owndrawForm = (IMutableFormDefinition)form;
            IForm guiForm  = factory.createForm(guiApp, guiDomain, owndrawForm);
            Iterator groupIter = owndrawForm.getGroupDefinitions().iterator();
            while(groupIter.hasNext())
            {
              IMutableGroupDefinition group = (IMutableGroupDefinition)groupIter.next();
              factory.createGroup(guiApp, guiForm, group);
            }
          }
          else if(form instanceof IHtmlFormDefinition)
          {
            IHtmlFormDefinition htmlForm = (IHtmlFormDefinition)form;
            IForm guiForm  = factory.createForm(guiApp, guiDomain, htmlForm);
            Iterator groupIter = htmlForm.getGroupDefinitions().iterator();
            while(groupIter.hasNext())
            {
              IHtmlGroupDefinition group = (IHtmlGroupDefinition)groupIter.next();
              factory.createGroup(guiApp, guiForm, group);
            }
          }
          else
          {
            IJacobFormDefinition jacobForm = (IJacobFormDefinition)form;
            IForm guiForm  = factory.createForm(guiApp, guiDomain, jacobForm);
            guiFormGroup.addChild(guiForm);
            
            Iterator groupIter = jacobForm.getGroupDefinitions().iterator();
            while(groupIter.hasNext())
            {
              IJacobGroupDefinition group = (IJacobGroupDefinition)groupIter.next();
              setupGroup(factory, guiApp, guiForm,group);
            }
            Iterator groupContainerIter = jacobForm.getGroupContainerDefinitions().iterator();
            while(groupContainerIter.hasNext())
            {
              IGroupContainerDefinition container = (IGroupContainerDefinition)groupIter.next();
              IGroupContainer guiContainer = factory.createGroupContainer(guiApp,null,container);
              groupIter = jacobForm.getGroupDefinitions().iterator();
              while(groupIter.hasNext())
              {
                IJacobGroupDefinition group = (IJacobGroupDefinition)groupIter.next();
                setupGroup(factory, guiApp, guiContainer, group);
              }
              ((HTTPForm)guiForm).addChild(guiContainer);
            }
            guiForm.resetTabOrder();
          }
        }
      }
		}
    
    // check whether the user has access to at least one domain
    if (guiApp.getChildren().size()==0)
      throw new AuthorizationException(new CoreMessage(CoreMessage.NO_DOMAIN_ACCESS));
    
    return guiApp;
  }
  
  private static IGroup setupGroup( IApplicationFactory factory, IApplication app,IGuiElement parent, IJacobGroupDefinition group)
  {
    IGroup guiGroup  = factory.createGroup(app, parent, group);
    Iterator elementIter = group.getGUIElementDefinitions().iterator();
    while(elementIter.hasNext())
    {
      IGUIElementDefinition element = (IGUIElementDefinition)elementIter.next();
      element.createRepresentation(factory,app, guiGroup);
    }
    return guiGroup;
  }
}
