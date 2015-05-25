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
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabContainerModel;
import de.tif.jacob.designer.model.UITabPanesModel;
import de.tif.jacob.designer.preferences.I18NPreferences;
public class CreateTabPaneGroupCommand extends Command
{
  private final JacobModel jacobModel;
  private final UITabContainerModel container;
  private final UIGroupModel newGroup;
  private final int insertIndex;

  public CreateTabPaneGroupCommand(JacobModel jacobModel, UITabContainerModel container, UIGroupModel group, int index)
  {
    this.newGroup = group;
    this.container = container;
    this.jacobModel = jacobModel;
    this.insertIndex = index;
  }

  public void execute()
  {
    try
    {
      ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
      {
        public Image getImage(Object element)
        {
          TableAliasModel alias = ((TableAliasModel) element);
          return alias.getImage();
        }

        public String getText(Object element)
        {
          TableAliasModel alias = ((TableAliasModel) element);
          return alias.getName() + "<" + alias.getTableModel().getName() + ">";
        }
      });
      dialog.setElements(jacobModel.getTableAliasModels().toArray());
      dialog.setTitle("Select anchor table alias");
      dialog.setMessage("Select the anchor table alias for the new browser");
      dialog.create();
      if (dialog.open() == Window.OK)
      {
        TableAliasModel alias = (TableAliasModel) dialog.getFirstResult();
        if (alias != null)
        {
          newGroup.setGroupContainerModel(container.getPane());
          newGroup.setJacobModel(jacobModel);
          newGroup.setTableAlias(alias.getName(), false);
          if (jacobModel.useI18N())
            newGroup.setLabel("%GROUP" + jacobModel.getSeparator() + (newGroup.getTableAlias().toUpperCase()));
          else
            newGroup.setLabel(StringUtils.capitalise(newGroup.getTableAlias()));
          
          String defaultName = container.getName() + "Pane";
          int counter = 1;
          String newName = defaultName+counter;
          while (container.getGroupModel().getElement(newName) != null)
          {
            newName = defaultName + counter;
            counter++;
          }
          newGroup.setName(newName);
          
          List possibleBrowsers = jacobModel.getBrowserModels(newGroup.getTableAliasModel());
          newGroup.setBrowserModel((BrowserModel) possibleBrowsers.get(0));
          newGroup.getElements();
          container.addPane(newGroup, insertIndex);
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
    container.addPane(newGroup);
  }

  public void undo()
  {
    container.removePane(newGroup);
  }
}
