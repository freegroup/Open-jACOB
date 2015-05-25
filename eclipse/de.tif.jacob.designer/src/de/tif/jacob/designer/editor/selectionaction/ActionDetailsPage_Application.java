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
package de.tif.jacob.designer.editor.selectionaction;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import de.tif.jacob.designer.JacobDesigner;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ActionDetailsPage_Application extends AbstractActionDetailsPage
{
  private Hyperlink hookHyperlink;
  
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


    toolkit.createLabel(client, "Class:");
    hookHyperlink = toolkit.createHyperlink(client,"<unset>", SWT.NORMAL);
    hookHyperlink.addHyperlinkListener(new HyperlinkAdapter()
    {
      public void linkActivated(HyperlinkEvent e)
      {
        try
        {
          IProject project=JacobDesigner.getPlugin().getSelectedProject();
          IJavaProject myJavaProject = JavaCore.create(project);
          IType type = myJavaProject.findType(getSelectionActionModel().getHookClassName());
          IJavaElement element = JavaCore.create(type.getResource());
          JavaUI.openInEditor(element);
        }
        catch(Exception exc)
        {
          JacobDesigner.showException(exc);
        }
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
    if (getSelectionActionModel() == null)
      throw new IllegalStateException();

    hookHyperlink.setText(getSelectionActionModel().getHookClassName());
    hookHyperlink.layout(true);
    super.refresh();
  }
}
