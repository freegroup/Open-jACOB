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
package de.tif.jacob.refactoring.dialogs;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIDBForeignFieldModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.refactoring.Activator;
/**
 * 
 */
public class SelectGroupTableAliasDialog extends ElementListSelectionDialog
{
  boolean renameChildren = false;
  boolean createRelations = false;
  private Button renameChildrenCheckbox;
  private Button createRelationsCheckbox;
  final UIGroupModel groupModel;
  
  /**
   * Constructs a new string substitution variable selection dialog.
   * 
   * @param parent
   *          parent shell
   */
  public SelectGroupTableAliasDialog(UIGroupModel group)
  {
    super(null, new LabelProvider()
    {
      public Image getImage(Object element)
      {
        return null;
      }

      public String getText(Object element)
      {
        return ((TableAliasModel)element).getName();
      }
    });
    
    this.groupModel = group;
    
    setShellStyle(getShellStyle() | SWT.RESIZE);
    setTitle("Change UI-Group Table ALias");
    setMessage("Select the new table alias of the group");
    setMultipleSelection(false);
    
    // retrieve all related table alias
    //
    JacobModel jacob =  group.getJacobModel();
    List<TableAliasModel> aliases = jacob.getTableAliasModels(group.getTableAliasModel().getTableModel());
    
    // remove the current table alias of the group
    //
    for (TableAliasModel tableAliasModel : aliases)
    {
      if(tableAliasModel == group.getTableAliasModel())
      {
        aliases.remove(tableAliasModel);
        break;
      }
    }
    
    List<TableAliasModel> linkedTables = group.getTableAliasModel().getToLinkedTableAliases();
    for (TableAliasModel tableAliasModel : linkedTables)
    {
      System.out.println(group.getTableAlias()+" <- "+tableAliasModel.getName());
    }

    linkedTables = group.getTableAliasModel().getFromLinkedTableAliases();
    for (TableAliasModel tableAliasModel : linkedTables)
    {
      System.out.println(group.getTableAlias()+" -> "+tableAliasModel.getName());
    }
    
    this.setValidator(new ISelectionStatusValidator()
    {
      public IStatus validate(Object[] selection)
      {
        try
        {
        // Falls es bei dem selectierten Alias Probleme mit einem ForeignField oder
        // InformBrowser gibt, muß dies dem Anwender deutlich gemacht werden.
        //
        if(selection.length !=0)
        {
          TableAliasModel selectedAlais = (TableAliasModel)selection[0];
          // Prüfen ob die Gruppe ein Foreign Field enthält, und wenn ja muß geprüft
          // werden ob die Relationen dazu vorhanden sind.
          List<TableAliasModel> possibleForeignFieldAliases = selectedAlais.getFromLinkedTableAliases();
          List<TableAliasModel> possibleInformBrowserAliases = selectedAlais.getToLinkedTableAliases();

          List<UIGroupElementModel> children = groupModel.getElements();
          for (UIGroupElementModel child : children)
          {
            if(child instanceof UIDBForeignFieldModel)
            {
              UIDBForeignFieldModel ff= (UIDBForeignFieldModel)child;
              if(!possibleForeignFieldAliases.contains(ff.getForeignTableAliasModel()))
              {
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID,"Missing relation to table alias <"+ff.getForeignTableAliasModel().getName()+">");
              }
            }
            else if(child instanceof UIDBInformBrowserModel)
            {
              UIDBInformBrowserModel browser = (UIDBInformBrowserModel)child;
              if(!possibleInformBrowserAliases.contains(browser.getTableAliasModel()))
              {
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID,"Missing relation to table alias <"+browser.getTableAliasModel().getName()+">");
              }
            }
          }
        }
        }
        catch(Exception exc)
        {
          JacobDesigner.showException(exc);
          return new Status(IStatus.ERROR, Activator.PLUGIN_ID,"Interner Fehler aufgetreten");
        }
        return new Status(IStatus.OK,Activator.PLUGIN_ID,"");
      }
    });
    
    setStatusLineAboveButtons(true);
    
    setElements(aliases.toArray());
  }
  
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
  protected Control createDialogArea(Composite parent) 
  {
    Composite control = (Composite)super.createDialogArea(parent);

    Composite composite = new Composite(control, SWT.NONE);
    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 2;
    composite.setLayout(glayout);
    renameChildrenCheckbox = new Button(composite, SWT.CHECK);
    renameChildrenCheckbox.setSelection(true);
    renameChildrenCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
      }
    });
    new Label(composite, SWT.SHADOW_NONE).setText("Rename Children");
    
//    createRelationsCheckbox = new Button(composite, SWT.CHECK);
//    createRelationsCheckbox.addSelectionListener(new SelectionAdapter()
//    {
//      public void widgetSelected(SelectionEvent e)
//      {
//      }
//    });
//    new Label(composite, SWT.SHADOW_NONE).setText("Create required relations (ForeignField, InformBrowser)");
      
    
    return control;
  }

  @Override
  protected void handleSelectionChanged()
  {

    super.handleSelectionChanged();
  }


  public boolean getRenameChildren()
  {
    return renameChildren;
  }


  public boolean getCreateRelations()
  {
    return createRelations;
  }

  @Override
  protected void okPressed()
  {
    renameChildren = renameChildrenCheckbox.getSelection();
//    createRelations = createRelationsCheckbox.getSelection();

    super.okPressed();
  }
}
