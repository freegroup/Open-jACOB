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
package de.tif.jacob.designer.actions.dialog;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.FilteredList;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.file.Directory;

/**
 * 
 */

public class TemplatePage extends WizardPage
{
  
  /**
   * 
   */
  static class ListItem 
  {
    
    /**
     * 
     */
    final String templateName;
    
    /**
     * 
     */
    final String description;
    
    /**
     * 
     * 
     * @param templateName 
     * @param description 
     */
    public ListItem(String templateName, String description)
    {
      this.templateName = templateName;
      this.description  = description;
    }
  }
  
  /**
   * 
   */
  static class NameLabelProvider extends LabelProvider
  {
    
    /**
     * 
     * 
     * @param element 
     * 
     * @return 
     */
    public String getText(Object element)
    {
      return ((ListItem)element).templateName;
    }
  }
  
  /**
   * 
   */
  static class DescriptionLabelProvider extends LabelProvider
  {
    
    /**
     * 
     * 
     * @param element 
     * 
     * @return 
     */
    public String getText(Object element)
    {
      return ((ListItem)element).description;
    }
  }

  /**
   * 
   */
  private StyledText text;
  
  /**
   * 
   */
  private FilteredList list;
  
  /**
   * 
   */
  private NewScheduledTaskWizard.Type type;
  
  /**
   * 
   * 
   * @param jacobModel 
   * @param type 
   */
  public TemplatePage(JacobModel jacobModel , NewScheduledTaskWizard.Type type) 
  {
    super("wizardPage2");
    this.type = type;
    setTitle("Scheduler job creation");
    setDescription("Select the template for the new scheduled job.");
  }

  /**
   * 
   * 
   * @return 
   */
  public String getTemplateName()
  {
    return ((ListItem)list.getSelection()[0]).templateName;
  }
  
  /**
   * 
   * 
   * @param parent 
   */
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 1;
    layout.verticalSpacing = 9;
    
    Label label = new Label(container, SWT.NULL);
    label.setText("Template");

    int flags = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
    list = new FilteredList(container, flags, new NameLabelProvider(),false, true, true);
		GridData data = new GridData();
    data.heightHint = 150;
    data.widthHint = 250;
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		list.setLayoutData(data);
		list.setFont(parent.getFont());
		list.setElements(getTemplates());
		list.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        if(list.getSelection().length>0)
          text.setText(((ListItem)list.getSelection()[0]).description);
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
      }
    });
		
		// DESCRIPTION
		//
  	flags = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
  	text = new StyledText (container, flags);

    data = new GridData();
    data.grabExcessVerticalSpace = true;
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.heightHint = 100;
    data.widthHint = 250;
   
    text.setEditable(false);
    text.setLayoutData(data);
    text.setFont(parent.getFont());

    setControl(container);
  }
  
	/**
	 * 
	 * 
	 * @return 
	 */
	private Object[] getTemplates()
	{
    java.util.List result = new ArrayList();
    try
    {
      String templateProjectPath="templates"+File.separator+type.toString()+File.separator;
      
      IPluginRegistry registry = Platform.getPluginRegistry();
      IPluginDescriptor descriptor = registry.getPluginDescriptor(JacobDesigner.ID);
      URL pluginURL = descriptor.getInstallURL();
      pluginURL = Platform.asLocalURL(pluginURL);
      Iterator iter = Directory.getAll(findFileInPlugin(JacobDesigner.ID,templateProjectPath).toFile(),true).iterator();
      while(iter.hasNext())
      {
        File file = (File)iter.next();
        if(file.getAbsolutePath().endsWith(".desc"))
        {
          String name = StringUtil.replace(file.getName(),".desc","");
          result.add(new ListItem(name,FileUtils.readFileToString(file,"ISO-8859-1")));
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
	  return result.toArray(new ListItem[0]);
	}
	
  /**
   * 
   * 
   * @param file 
   * @param plugin 
   * 
   * @return 
   * 
   * @throws MalformedURLException 
   * @throws IOException 
   */
  private Path findFileInPlugin(String plugin, String file) throws MalformedURLException, IOException
  {
    IPluginRegistry registry = Platform.getPluginRegistry();
    IPluginDescriptor descriptor = registry.getPluginDescriptor(plugin);
    URL pluginURL = descriptor.getInstallURL();
    URL jarURL = new URL(pluginURL, file);
    URL localJarURL = Platform.asLocalURL(jarURL);
    return new Path(localJarURL.getPath());
  }

}
