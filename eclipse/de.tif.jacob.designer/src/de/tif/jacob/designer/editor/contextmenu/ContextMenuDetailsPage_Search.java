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
package de.tif.jacob.designer.editor.contextmenu;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationsetModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ContextMenuDetailsPage_Search extends AbstractContextMenuDetailsPage
{
  private Combo  relationsetCombo;
  private Combo  filldirectionCombo;
  private Button forceConstaintCheckbox;
  
  protected boolean hasChangeableLabel()
  {
    return false;
  }
  
  protected void createDetailContents(FormToolkit toolkit, Composite parent)
  {
    Section section = toolkit.createSection(parent, Section.DESCRIPTION);
    section.marginWidth = 10;
    section.setText("Details");
    //section.setDescription("Set the detail properties of the selected double column.");
    TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
    td.grabHorizontal = true;
    section.setLayoutData(td);

    toolkit.createCompositeSeparator(section);
    Composite client = toolkit.createComposite(section);
    GridLayout glayout = new GridLayout();
    glayout.marginWidth = glayout.marginHeight = 0;
    glayout.numColumns = 2;
    client.setLayout(glayout);

    // create type combobox
    //
    toolkit.createLabel(client, "Relationset:");
    relationsetCombo = new Combo(client, SWT.READ_ONLY);
    relationsetCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getContextMenuEntryModel().setRelationset(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    relationsetCombo.setLayoutData(gd);
    toolkit.adapt(relationsetCombo);

    // create type combobox
    //
    toolkit.createLabel(client, "Filldirection:");
    filldirectionCombo = new Combo(client, SWT.READ_ONLY);
    filldirectionCombo.addSelectionListener(new SelectionListener(){
      public void widgetSelected(SelectionEvent e)
      {
        getContextMenuEntryModel().setFilldirection(((Combo)e.widget).getText());
      }
      public void widgetDefaultSelected(SelectionEvent e)
      {
        throw new UnsupportedOperationException();
      }
    });
    gd = new GridData(GridData.FILL_HORIZONTAL);
    filldirectionCombo.setLayoutData(gd);
    toolkit.adapt(filldirectionCombo);

    // create case sensitive checkbox
    //
    toolkit.createLabel(client, "Force constraint:");
    forceConstaintCheckbox = toolkit.createButton(client, null, SWT.CHECK);
    forceConstaintCheckbox.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getContextMenuEntryModel().setSafeMode(new Boolean(forceConstaintCheckbox.getSelection()));
      }
    });
    
    // register client
    toolkit.paintBordersFor(section);
    section.setClient(client);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.IDetailsPage#refresh()
   */
  public void refresh()
  {
    super.refresh();
    
    List relationsets = getContextMenuEntryModel().getJacobModel().getRelationsetNames();
    relationsets.remove(RelationsetModel.RELATIONSET_LOCAL);
    this.relationsetCombo.setItems((String[])relationsets.toArray(new String[0]));
    this.relationsetCombo.setText(getContextMenuEntryModel().getRelationset());
    this.filldirectionCombo.setItems((String[])getContextMenuEntryModel().getFilldirections().toArray(new String[0]));
    this.filldirectionCombo.setText(getContextMenuEntryModel().getFilldirection());
    this.forceConstaintCheckbox.setSelection(getContextMenuEntryModel().getSafeMode().booleanValue());
  }
  

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // do not refresh, if type changes since this page is invalid then
    if (evt.getPropertyName()==ObjectModel.PROPERTY_FIELD_TYPE_CHANGED)
      return;

    refresh();
  }
}
