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
package de.tif.jacob.docgen.popup.actions;

import java.io.File;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;
import de.tif.jacob.docgen.Activator;
import de.tif.jacob.docgen.util.FileUtil;
import de.tif.jacob.docgen.util.SimpleLogger;


/**
 * TODO Enter purpose for class SimpleJob
 * 
 * @author andras Herz
 * @version 1.0
 * @since 09.06.2005
 */
public class GenerateWikiTableDocumentJob extends Job 
{
  private static final String PATH= "doc"+File.separator+"wiki"+File.separator;
  private static final String TEMPLATE= "templates"+File.separator+"wiki"+File.separator+"Alias.vm";
	
	/**
	 * @param name
	 */
	public GenerateWikiTableDocumentJob() {
		super("Generating Table documentation");
    setPriority(Job.BUILD);
    setSystem(true);
	}

	/*
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(IProgressMonitor monitor) 
	{
    String all="";
		JacobModel jacob = JacobDesigner.getPlugin().getModel();
		List<TableModel> tables = jacob.getTableModels();
		monitor.beginTask("", tables.size());
		SimpleLogger.log("Job [" + getName() + "] has been started. Prio [" + getPriority() + "] State: " + getState() + " Thread: " + getThread());
		
		// clean the output directory
		//
		try {
	    IProject project = JacobDesigner.getPlugin().getSelectedProject();
	    IFolder docFolder = project.getFolder(PATH);
      IResource[] children= docFolder.members();
      for (int i = 0; i < children.length; i++)
      {
        IResource resource = children[i];
        if(resource instanceof IFolder)
        {
          // do nothing
        }
        else
        {
         resource.delete(true,monitor); 
        }
      }
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Iterator iter = tables.iterator();
		while(iter.hasNext()) 
		{
			if (monitor.isCanceled()) {
				break;
			}
			try {
				TableModel table = (TableModel)iter.next();
       
				monitor.subTask("generating wiki doc: "+table.getName());
				String content = generateContent(table,monitor);
        all = all+content;
				writeContent(content,StringUtils.capitalise(table.getName()),monitor);
				monitor.worked(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    try
    {
      writeContent(all,StringUtils.capitalise("ALL"),monitor);
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
		monitor.done();
		return Status.OK_STATUS;
	}
	
	/**
	 * 
	 * @param alias
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
  private String generateContent(TableModel table,IProgressMonitor monitor) throws Exception
  {
			URL pluginURL = Activator.getDefault().getBundle().getEntry("/");
			pluginURL = Platform.asLocalURL(pluginURL);
			File file = new File(pluginURL.getPath()+TEMPLATE);

      List<TableAliasModel> aliases = table.getJacobModel().getTableAliasModels(table);
      
			String template = FileUtils.readFileToString(file,"ISO-8859-1");
			
			/* first, we init the runtime engine. Defaults are fine. */ 
			Velocity.init(); 
			/* lets make a Context and put data into it */ 
			VelocityContext context = new VelocityContext(); 
      context.put("table"  , table); 
      context.put("description"  , table.getDescription()); 
      context.put("aliases", aliases); 
			context.put("StringUtils",new StringUtils());
			/* lets render a template */ 
			
			StringWriter w = new StringWriter(); 
			
			w = new StringWriter(); 
			Velocity.evaluate( context, w, "mystring", template ); 

			// neue .java Datei im package jacob.common erzeugen
			//
			return w.toString();
  }
	
  /**
   * 
   * @param content
   * @param className
   * @param monitor
   * @throws Exception
   */
  private void writeContent(String content, String className, IProgressMonitor monitor) throws Exception
  {
    IProject project = JacobDesigner.getPlugin().getSelectedProject();
    IFolder srcFolder = project.getFolder(PATH);
    FileUtil.createFolder(project.getFolder(PATH));
    
    IFile newLogo = srcFolder.getFile(className+".txt");
    newLogo.create(new StringBufferInputStream(content), false,monitor);
  }
}