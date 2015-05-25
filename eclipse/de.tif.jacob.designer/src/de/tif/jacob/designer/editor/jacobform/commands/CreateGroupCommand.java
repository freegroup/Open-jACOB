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
package de.tif.jacob.designer.editor.jacobform.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.collections.FastTreeMap;
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.dialog.NewScheduledTaskWizard;
import de.tif.jacob.designer.editor.jacobform.dialogs.NewGroupWizard;
import de.tif.jacob.designer.model.AbstractActionEmitterModel;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.preferences.I18NPreferences;

public class CreateGroupCommand extends Command
{
  private final JacobModel  jacobModel;
	private final UIJacobFormModel form;
	private final UIGroupModel newGroup;
	private final Point location;
	private final Dimension size;
  
	private TableAliasModel aliasModel;
  
	public CreateGroupCommand(JacobModel jacobModel, UIJacobFormModel form, UIGroupModel group, Point location, Dimension size)
	{
    if(size==null)
      size= new Dimension(-1,-1);

    this.newGroup = group;
		this.location = location;
		this.size = size;
		this.form = form;
		this.jacobModel = jacobModel;
	}
	
	public void execute()
	{
	  try 
		{
      NewGroupWizard wizard = new NewGroupWizard(jacobModel, aliasModel);
      // Instantiates the wizard container with the wizard and opens it
      WizardDialog d = new WizardDialog(null, wizard);
      d.setMinimumPageSize(600,400);
      d.create();
      
      Properties properties = new Properties();
      if(d.open()==Window.OK)
      {
        properties = wizard.getTemplateProperties();
        aliasModel= wizard.getTableAliasModel();
        if(aliasModel!=null)
        {
          // Berechne eine "guten" Größe und Position für die frisch 
          // einzufügende Gruppe wenn dem Command keine Größe vorgeschrieben wurde.
          //
          if(this.size.height==-1 && this.size.width==-1)
          {
            // suche alle Objekte welche über dem Punkt liegt wo der Benutzer hingeklickt hat
            // und ermittle die Gruppe welche am nächsten ist
            //
            Iterator iter = form.getElements().iterator();
            
            if(!iter.hasNext())
            {
              // Die Form enthält noch keine Gruppe an der man sich orientieren könnte
              // => setzen von halbwegs vernüftigen Werten.
              //
              location.x = ObjectModel.DEFAULT_GROUP_SPACING;
              location.y = ObjectModel.DEFAULT_GROUP_SPACING;
              size.width = ObjectModel.DEFAULT_GROUP_WIDTH;
              size.height= ObjectModel.DEFAULT_GROUP_HEIGHT;
            }
            else
            {
              Rectangle upRect   = new Rectangle(location.x,0,1,location.y);
              Rectangle downRect = new Rectangle(location.x,location.y,1, Integer.MAX_VALUE/2);
              SortedMap upElements = new FastTreeMap();
              // Die Form enthält bereits eine Gruppe. Anhand der bestehenden Gruppen wird
              // jetzt eine vernüftige Position und Größe ermittelt.
              //
              while (iter.hasNext())
              {
                UIFormElementModel element = (UIFormElementModel) iter.next();
                if(element.getConstraint().intersects(upRect))
                {
                  upElements.put(new Integer(location.y-element.getConstraint().y),element);
                }
              }
              // Ermittle alle Gruppen welche unterhalb sind
              //
              iter = form.getElements().iterator();
              while (iter.hasNext())
              {
                UIFormElementModel element = (UIFormElementModel) iter.next();
                // Falls unterhalb noch Gruppen sind, kann leider kein autolayout erfolgen.
                // Grund: Untere Gruppe kann ja nicht einfach weiter nach unten rutschen und
                //        es ist auch schwer zu beurteilen ob die neue Gruppe zwischen der oberen und
                //        und unteren Gruppe dazwischen passt => lieber kein Autolayout.
                if(element.getConstraint().intersects(downRect))
                {
                  upElements=new FastTreeMap();
                  break;
                }
              }
              
              if(upElements.size()>0)
              {
                UIFormElementModel groupAbove = (UIFormElementModel)upElements.get(upElements.firstKey());
                Rectangle aboveConstraint = groupAbove.getConstraint();
                this.location.x = aboveConstraint.x;
                this.location.y = aboveConstraint.y+aboveConstraint.height+ObjectModel.DEFAULT_GROUP_SPACING;
                this.size.width = aboveConstraint.width;
                this.size.height =aboveConstraint.height;
              }
            }
          }


          newGroup.setJacobModel(jacobModel);
					form.addElement(newGroup);
					newGroup.setLocation(location.getCopy());
					newGroup.setTableAlias(aliasModel.getName(),false);
					if(form.getJacobModel().useI18N())
						newGroup.setLabel("%GROUP"+form.getJacobModel().getSeparator()+(newGroup.getTableAlias().toUpperCase()));
					else
						newGroup.setLabel(StringUtils.capitalise(newGroup.getTableAlias()));

          String defaultName = newGroup.getTableAlias()+"Group";
          String newName     = defaultName;
          int counter=2;
          while(form.getElement(newName)!=null)
          {
            newName = defaultName+counter;
            counter++;
          }
					newGroup.setName(newName);
					
					List possibleBrowsers = form.getJacobModel().getBrowserModels(newGroup.getTableAliasModel());
					
          if(properties.getProperty("GROUP_BORDER","true").equals("false"))
              newGroup.setBorder(false);
					newGroup.setBrowserModel((BrowserModel)possibleBrowsers.get(0));
					
					if(size != null)
						newGroup.setSize(size);
          
          newGroup.addStandardButtons(properties);
        }
      }
		} 
	  catch (Exception e) 
		{
	  	JacobDesigner.showException(e);
		}
	}
	
  public void redo()
	{
		form.addElement(newGroup);
	}
	
	public void undo()
	{
		form.removeElement(newGroup);
	}

  public void setAliasModel(TableAliasModel aliasModel)
  {
    this.aliasModel = aliasModel;
  }

}
