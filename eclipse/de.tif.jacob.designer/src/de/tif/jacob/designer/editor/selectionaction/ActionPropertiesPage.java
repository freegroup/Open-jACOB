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
/*
 * Created on 08.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.editor.selectionaction;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import de.tif.jacob.designer.model.SelectionActionModel;
import de.tif.jacob.designer.model.UIGroupModel;


/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class ActionPropertiesPage extends FormPage
{
  private final MasterBlock block;

  public ActionPropertiesPage(FormEditor editor)
  {
    super(editor, "Common", "Common");
    block = new MasterBlock(this);
  }
  
  protected void select(SelectionActionModel field)
  {
    this.block.select(field);
  }

  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  public void init(IEditorSite site, IEditorInput input)
  {
    super.init(site, input);
    
    EditorInput jacobInput = (EditorInput) input;
    jacobInput.getActionProvider().addPropertyChangeListener(this.block);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPart#dispose()
   */
  public void dispose()
  {
    super.dispose();
    
    EditorInput jacobInput = (EditorInput) getEditorInput();
    jacobInput.getActionProvider().removePropertyChangeListener(this.block);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
   */
  protected void createFormContent(IManagedForm managedForm)
  {
    ScrolledForm form = managedForm.getForm();
    EditorInput jacobInput = (EditorInput) getEditorInput();
    form.setText("Selection Actions:"+jacobInput.getActionProvider().getName());
    block.createContent(managedForm);
  }
}