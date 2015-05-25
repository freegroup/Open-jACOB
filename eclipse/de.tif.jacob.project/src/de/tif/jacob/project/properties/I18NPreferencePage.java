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
package de.tif.jacob.project.properties;
import net.ffxml.swtforms.builder.PanelBuilder;
import net.ffxml.swtforms.debug.FormDebugUtils;
import net.ffxml.swtforms.layout.CellConstraints;
import net.ffxml.swtforms.layout.FormLayout;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import de.tif.jacob.designer.preferences.I18NPreferences;
/**
 * Plugin preference page.
 * 
 * @version $Author: freegroup $ $Revision: 1.2 $ $Date: 2008/05/07 23:53:26 $
 */
public class I18NPreferencePage extends PropertyPage implements IWorkbenchPropertyPage
{
  /* Preference fields. */
  private Text keyGroupSeparator;
  private Button useI18N;
  I18NPreferences preferences;
  
  /**
   * Constructor.
   */
  public I18NPreferencePage()
  {
    super();
  }
  
  /**
   * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
   */
  protected Control createContents(Composite parent)
  {
    IAdaptable element = getElement();
    System.out.println(element);
    if(element instanceof IProject)
      preferences = new I18NPreferences((IProject)element);


    FormLayout layout = new FormLayout(
        "r:max(50dlu;p),5dlu,pref,50dlu:grow,5dlu",
        "pref,5dlu,pref,5dlu,pref,5dlu");
    
    PanelBuilder builder = new PanelBuilder(parent, layout);
//    FormDebugUtils.debugLayout(builder.getComposite());
    
    CellConstraints cc = new CellConstraints();

    builder.addSeparator("I18N",cc.xywh(1,1,4,1));
    
    useI18N = new Button(parent, SWT.CHECK);
    useI18N.setSelection(preferences.useI18N());
    builder.add(useI18N,cc.xy(3,3));
    builder.addLabel(" enabled",cc.xy(4,3)); //cc.FILL,cc.FILL

    builder.addLabel("Delimiter",cc.xy(1,5));
    keyGroupSeparator = new Text(parent, SWT.BORDER);
    keyGroupSeparator.setText(preferences.getSeparator());
    keyGroupSeparator.setTextLimit(2);
    builder.add(keyGroupSeparator,cc.xywh(3,5,2,1));
    
    return builder.getComposite();
  }

  /**
   * @see org.eclipse.jface.preference.IPreferencePage#performOk()
   */
  public boolean performOk()
  {
    preferences.setSeparator(keyGroupSeparator.getText());
    preferences.setUseI18N(useI18N.getSelection());
  	
    return super.performOk();
  }

  /**
   * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
   */
  protected void performDefaults()
  {
    keyGroupSeparator.setText(preferences.getSeparator());
    super.performDefaults();
  }

  private Composite createFieldComposite(Composite parent)
  {
    return createFieldComposite(parent, 0);
  }

  private Composite createFieldComposite(Composite parent, int indent)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout gridLayout = new GridLayout(2, false);
    gridLayout.marginWidth = indent;
    gridLayout.marginHeight = 0;
    gridLayout.verticalSpacing = 0;
    composite.setLayout(gridLayout);
    return composite;
  }
}
