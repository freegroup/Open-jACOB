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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.definition.IDomainDefinition;
import de.tif.jacob.core.definition.IFormGroupDefinition;
import de.tif.jacob.core.definition.impl.AbstractDomainDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IFormGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.GuiEventHandler;
import de.tif.jacob.screen.event.IDomainEventHandler;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationEntry;
import de.tif.jacob.screen.event.IDomainEventHandler.INavigationPanel;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPForm;
import de.tif.jacob.screen.impl.HTTPFormGroup;
import de.tif.jacob.screen.impl.HTTPNavigationEntryProvider;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FormGroup extends GuiHtmlElement implements HTTPFormGroup
{
  static public final transient String RCS_ID = "$Id: FormGroup.java,v 1.3 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final IFormGroupDefinition definition;
  protected List childList = new ArrayList();
  
  /**
   * Initial constructor with the castor definition
   * 
   * @param parent
   * @param childs
   * @param name
   * @param name
   */
  protected FormGroup(IApplication app, IFormGroupDefinition formGroup)
  {
    super(app, formGroup.getName(), formGroup.getTitle(), true, null, formGroup.getProperties());

    definition = formGroup;
  }

  
  public INavigationEntry getNavigationEntry(IClientContext context)
  {
    return  new INavigationEntry((IForm)null,"", getI18NLabel(context.getLocale()),null,null);
  }


  /**
   * Es darf nicht die Basisimplementierung genommen werde, da diese den Vater von "child" setzt/überschreibt.
   * Dies ist dann nicht mehr die Domain, sonder die FormGroup. Dies würde dann die Abwärtskompatibilität
   * verletzten. Bestehende Applikationen gehen eventuell davon aus, dass der Vater einer Form immer die
   * zugehörige Domain ist.
   * 
   */
  public void addChild(IGuiElement child)
  {
    childList.add(child);
    ((HTTPNavigationEntryProvider)child).setFormGroup(this);
  }

  public List getChildren()
  {
    return childList;
  }
  
  protected void addDataFields(Vector fields)
  {
    // do nothing
  }

  public String getEventHandlerReference()
  {
    return null;
  }

  public String getURL()
  {
    return null;
  }


  /**
   * No recursion at the Moment. So, a FormGroup can't contains another FormGroup.
   */
  public IFormGroup getFormGroup()
  {
    return null;
  }

  /**
   * No recursion at the Moment. So, a FormGroup can't contains another FormGroup.
   */
  public void setFormGroup(IFormGroup group)
  {
    // ignore
  }
  
  
}
