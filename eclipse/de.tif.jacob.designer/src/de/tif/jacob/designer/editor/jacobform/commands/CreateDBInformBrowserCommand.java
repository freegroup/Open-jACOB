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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.preferences.I18NPreferences;

public class CreateDBInformBrowserCommand extends Command
{
	private final UIDBInformBrowserModel  browser;
	private final UIGroupModel group;
	private final Point location;
	private final Dimension size;
	private boolean canUndo=false;
	
	public CreateDBInformBrowserCommand(UIGroupModel group, UIDBInformBrowserModel browser, Point location, Dimension size)
	{
		this.group    = group;
		this.location = location;
		this.size     = size;
		this.browser  = browser;
	}
	
	public void execute()
	{
	  try
	  {
	    Set<BrowserModel> browsers= group.getPossibleEmbeddableInformBrowsers();
	
	    ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
	        {
	          public Image getImage(Object element)
	          {
	            return ((BrowserModel)element).getImage();
	          }
	
	          public String getText(Object element)
	          {
	            BrowserModel b= (BrowserModel)element;
	            return b.getName()+"<Alias:"+b.getTableAliasModel().getName()+">";
	          }
	        });
	    dialog.setElements(browsers.toArray());
	    dialog.setTitle("Select the data browser for the GUI element");
	    dialog.setMessage("Select the data browser for the GUI element.");
	    
	    dialog.create();
	    if(dialog.open()==Window.OK && dialog.getFirstResult()!=null)
	    {
	  		this.browser.setLocation(location);
	  		if(size != null)
	  		{
	  		  if(size.height<=0)
	  		    size.height=20;
	  		  if(size.width<=0)
	  		    size.width=100;
	  		  this.browser.setSize(size);
	  		}
	      BrowserModel b= (BrowserModel)dialog.getFirstResult();
				browser.setup(group, b,location,size);
				String defaultName = browser.getDefaultName();
				String newName     = defaultName;
				int counter=2;
				while(group.getGroupContainerModel().isUIElementNameFree(newName)==false)
				{
				  newName = defaultName+counter;
				  counter++;
				}
				browser.setName(newName);
				if(group.getJacobModel().useI18N())
				{
					browser.getCaptionModel().setCaption("%"+browser.suggestI18NKey());
					if(!group.getJacobModel().hasI18NKey(browser.suggestI18NKey()))
					  group.getJacobModel().addI18N(browser.suggestI18NKey(),browser.getDefaultCaption());
				}
				else
				{
					browser.getCaptionModel().setCaption(StringUtils.capitalise(browser.getName()));
				}
	  		group.addElement(this.browser);
	      canUndo =true;
	    }
	  }
	  catch(Exception e)
	  {
	    JacobDesigner.showException(e);
	  }
	    
	}
	
	public void redo()
	{
	  if(canUndo)
	    group.addElement(browser);
	}
	
	public void undo()
	{
	  if(canUndo)
	    group.removeElement(browser);
	}
}
