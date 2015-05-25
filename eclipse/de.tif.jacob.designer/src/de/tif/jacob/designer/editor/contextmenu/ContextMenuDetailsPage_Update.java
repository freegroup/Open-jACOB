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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import de.tif.jacob.designer.model.ObjectModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ContextMenuDetailsPage_Update extends AbstractContextMenuDetailsPage
{
  protected void createDetailContents(FormToolkit toolkit, Composite parent)
  {
  }
  
  protected boolean hasChangeableLabel()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.forms.IDetailsPage#refresh()
   */
  public void refresh()
  {
    super.refresh();
  }
  
    
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // do not refresh, if type changes since this page is invalid then
    if (evt.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
      return;

    refresh();
  }
}
