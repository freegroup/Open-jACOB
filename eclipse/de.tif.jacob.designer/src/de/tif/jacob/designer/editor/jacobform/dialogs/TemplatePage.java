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
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (txt).
 */

public class TemplatePage extends WizardPage
{
  String templateProjectPath="templates"+File.separator+"group"+File.separator;
  private static int lastSelection = 0;

  static class ListItem 
  {
    final String templateName;
    final Image image;
    public ListItem(String templateName)
    {
      this.templateName = templateName;
      this.image  = JacobDesigner.getImage("templates/group/", templateName+".png");
    }
  }
  static class NameLabelProvider extends LabelProvider
  {
    public String getText(Object element)
    {
      return ((ListItem)element).templateName;
    }
  }

  private Label text;
  private FilteredList list;
  
  /**
   * Constructor for SampleNewWizardPage.
   * 
   * @param pageName
   */
  public TemplatePage(JacobModel jacobModel ) 
  {
    super("wizardPage2");
    setTitle("Group Template");
    setDescription("Select the template for the new table alias group.");
  }

  public String getTemplateName()
  {
    lastSelection = list.getSelectionIndex();
    return ((ListItem)list.getSelection()[0]).templateName;
  }
  /**
   * @see IDialogPage#createControl(Composite)
   */
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;
    layout.verticalSpacing = 9;
    

    int flags = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
    list = new FilteredList(container, flags, new NameLabelProvider(),false, true, true);
		GridData data = new GridData();
    data.heightHint = 150;
    data.widthHint = 150;
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
          text.setImage(((ListItem)list.getSelection()[0]).image);
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
      }
    });
    list.setSelection(new int[]{lastSelection});
		
		// Image
		//
  	flags = SWT.BORDER|SWT.CENTER;
  	text = new Label(container, flags);
    text.setBackground(ColorConstants.white);
    data = new GridData();
    data.grabExcessVerticalSpace = true;
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    data.verticalAlignment = GridData.FILL;
    data.heightHint = 100;
    data.widthHint = 250;
   
    text.setLayoutData(data);
    text.setFont(parent.getFont());

    setControl(container);
  }
  
	private Object[] getTemplates()
	{
    java.util.List result = new ArrayList();
    try
    {
      IPluginRegistry registry = Platform.getPluginRegistry();
      IPluginDescriptor descriptor = registry.getPluginDescriptor(JacobDesigner.ID);
      URL pluginURL = descriptor.getInstallURL();
      pluginURL = Platform.asLocalURL(pluginURL);
      Iterator iter = Directory.getAll(findFileInPlugin(JacobDesigner.ID,templateProjectPath).toFile(),true).iterator();
      while(iter.hasNext())
      {
        File file = (File)iter.next();
        if(file.getAbsolutePath().endsWith(".properties"))
        {
          String name = StringUtil.replace(file.getName(),".properties","");
         result.add(new ListItem(name));
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
	  return result.toArray(new ListItem[0]);
	}
	
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