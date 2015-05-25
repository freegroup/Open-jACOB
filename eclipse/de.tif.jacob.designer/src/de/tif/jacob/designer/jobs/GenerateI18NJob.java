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
package de.tif.jacob.designer.jobs;

import java.io.File;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.util.FileUtil;
import de.tif.jacob.designer.util.SimpleLogger;


/**
 * TODO Enter purpose for class SimpleJob
 * 
 * @author andras Herz
 * @version 1.0
 * @since 09.06.2005
 */
public class GenerateI18NJob extends Job {
	private static final String PACKAGE = "jacob.resources";
	private static final String PATH    = File.separator+StringUtils.replace(PACKAGE,".",File.separator);
  private static final String TEMPLATE= "templates"+File.separator+"i18n"+File.separator+"I18N.vm";
	
	/**
	 * @param name
	 */
	public GenerateI18NJob() {
		super("Generating I18N helper");
    setPriority(Job.BUILD);
    setSystem(false);
	}

	/*
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(IProgressMonitor monitor) 
	{
		JacobModel jacob = JacobDesigner.getPlugin().getModel();
    if(jacob==null)
      return Status.CANCEL_STATUS;
    
    
		List keys = jacob.getI18NKeys();
		monitor.beginTask("", keys.size());
		SimpleLogger.log("Job [" + getName() + "] has been started. Prio [" + getPriority() + "] State: " + getState() + " Thread: " + getThread());
		
		// clean the output directory
		//
		try 
    {
	    IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
	    IPackageFragmentRoot[] roots = myJavaProject.getAllPackageFragmentRoots();
	    IPath path = roots[0].getPath();

	    IProject project = myJavaProject.getProject();
	    IFolder srcFolder = project.getFolder(path.lastSegment());
	    IFolder folder = srcFolder.getFolder(PATH);
      IResource child= folder.findMember("I18N.java");
      child.delete(true,monitor); 
		} 
    catch (Exception e1) 
    {
			e1.printStackTrace();
		}

		monitor.subTask("generating class [I18N]");
    
    // Save the .property files into the resouce directory of the porject
    //
    JacobDesigner.getPlugin().saveCurrentI18N();
    
		try
    {
      String content = generateClassContent(jacob,monitor);
      writeClassContent(content,"I18N",monitor);
    }
    catch (Throwable e)
    {
      // ignore
    }
    finally
    {
      monitor.worked(1);
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
  private String generateClassContent(JacobModel jacob,IProgressMonitor monitor) throws Exception
  {
				
				URL pluginURL = JacobDesigner.getPlugin().getBundle().getEntry("/");
				pluginURL = Platform.asLocalURL(pluginURL);
				File file = new File(pluginURL.getPath()+TEMPLATE);

				String template = FileUtils.readFileToString(file,"ISO-8859-1");
				
				/* first, we init the runtime engine. Defaults are fine. */ 
				Velocity.init(); 
				/* lets make a Context and put data into it */ 
				VelocityContext context = new VelocityContext(); 
				context.put("keys"  , jacob.getI18NKeys()); 
        context.put("date"   , new Date()); 
        context.put("package"   , PACKAGE); 
				context.put("application"    , jacob.getApplicationModel()); 
				context.put("StringUtils",new StringUtils());
				/* lets render a template */ 
				
				StringWriter w = new StringWriter(); 
				
				w = new StringWriter(); 
				Velocity.evaluate( context, w, "mystring", template ); 

				return w.toString();
  }
	
  /**
   * 
   * @param content
   * @param className
   * @param monitor
   * @throws Exception
   */
  private void writeClassContent(String content, String className, IProgressMonitor monitor) throws Exception
  {
    IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
    IPackageFragmentRoot[] roots = myJavaProject.getAllPackageFragmentRoots();
    IPath path = roots[0].getPath();

    IProject project = myJavaProject.getProject();
    IFolder srcFolder = project.getFolder(path.lastSegment());
    FileUtil.createFolder(project.getFolder(srcFolder.getName() + PATH));
    
    IFile newLogo = srcFolder.getFile(PATH+File.separator+className+".java");
    newLogo.create(new StringBufferInputStream(content), false,monitor);
  }
}