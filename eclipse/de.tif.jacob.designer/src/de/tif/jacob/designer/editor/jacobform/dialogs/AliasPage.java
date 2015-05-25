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
package de.tif.jacob.designer.editor.jacobform.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredList;
import de.tif.jacob.designer.editor.jacobform.dialogs.TemplatePage.ListItem;
import de.tif.jacob.designer.editor.jacobform.dialogs.TemplatePage.NameLabelProvider;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (txt).
 */

public class AliasPage extends WizardPage
{
  
  private FilteredList list;
  private JacobModel jacobModel;

  public class MyLabelProvider extends LabelProvider
  {
    public Image getImage(Object element)
    {
      TableAliasModel alias = ((TableAliasModel) element);
      return alias.getImage();
    }
    
    public String getText(Object element)
    {
      TableAliasModel alias = ((TableAliasModel) element);
      return alias.getName()+"<"+alias.getTableModel().getName()+">";
    }
  }
  
  /**
   * Constructor for SampleNewWizardPage.
   * 
   * @param pageName
   */
  public AliasPage(JacobModel jacobModel) 
  {
    super("wizardPage");
    
    this.jacobModel  = jacobModel;
    
    setTitle("Group Creation");
    setDescription("Select the anchor table alias for the new UI group.");
  }

  /**
   * @see IDialogPage#createControl(Composite)
   */
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 1;
    layout.verticalSpacing = 9;

    int flags = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
    list = new FilteredList(container, flags, new MyLabelProvider(),false, true, true);
    GridData data = new GridData();
    data.heightHint = 150;
    data.widthHint = 150;
    data.grabExcessVerticalSpace = true;
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    list.setLayoutData(data);
    list.setFont(parent.getFont());
    list.setElements(jacobModel.getTableAliasModels().toArray(new TableAliasModel[0]));
    setControl(container);
  }


  private void updateStatus(String message)
  {
    setErrorMessage(message);
    setPageComplete(message == null);
  }
  

  public TableAliasModel getAlias()
  {
    return (TableAliasModel)list.getSelection()[0];
  }

}