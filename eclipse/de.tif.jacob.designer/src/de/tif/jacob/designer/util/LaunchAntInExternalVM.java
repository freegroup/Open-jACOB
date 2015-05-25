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
 * Created on 26.05.2005
 *
 */
package de.tif.jacob.designer.util;

import org.eclipse.ant.internal.ui.launchConfigurations.IAntLaunchConfigurationConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;

/**
 *..uthor ulrich
 */
public class LaunchAntInExternalVM 
{
	private static final String MAIN_TYPE_NAME = "org.eclipse.ant.internal.ui.antsupport.InternalAntRunner";
	private static final String REMOTE_ANT_PROCESS_FACTORY_ID= "org.eclipse.ant.ui.remoteAntProcessFactory";
	/**
	 * Method inExternalVM.
	 * 
	 *..aram buildFile
	 *..aram monitor
	 *..aram captureOutput
	 *..aram targets
	 *..hrows CoreException
	 */
	public static void launchAntInExternalVM(IFile buildFile, IProgressMonitor monitor, boolean captureOutput, String targets)	throws CoreException 
	{
		//ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy workingCopy = null;
		try 
		{
			//config = this.createDefaultLaunchConfiguration(buildFile,
			// monitor);
			workingCopy = LaunchAntInExternalVM.createDefaultLaunchConfiguration(buildFile,captureOutput, targets);
			//config = workingCopy.doSave();
			ILaunch launch = workingCopy.launch(ILaunchManager.RUN_MODE, new SubProgressMonitor(monitor, 1));
			if (!captureOutput) 
			{
				ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
				manager.removeLaunch(launch);
			}
			//config.setAttribute(IExternalToolConstants.ATTR_LOCATION, null);
		} 
		finally 
		{
			workingCopy = null;
		}
	}

	/**
	 * Creates and returns a default launch configuration for the given file.
	 * 
	 *..aram file
	 *..aram captureOutput
	 *..aram targets
	 *..eturn default launch configuration
	 *..hrows CoreException
	 */
	private static ILaunchConfigurationWorkingCopy createDefaultLaunchConfiguration(IFile file, boolean captureOutput, String targets) throws CoreException 
	{
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(IAntLaunchConfigurationConstants.ID_ANT_LAUNCH_CONFIGURATION_TYPE);
		StringBuffer buffer = new StringBuffer(file.getProject().getName());
		buffer.append(' ');
		buffer.append(file.getName());
		buffer.append(" (WOLips)");
		String name = buffer.toString().trim();
		name = manager.generateUniqueLaunchConfigurationNameFrom(name);
		ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null,name);
		workingCopy.setAttribute(IExternalToolConstants.ATTR_LOCATION,VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc",file.getFullPath().toString())); //$NON-NLS-1$
		workingCopy.setAttribute("org.eclipse.jdt.launching.WORKING_DIRECTORY",file.getProject().getLocation().toOSString());
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,"org.eclipse.ant.ui.AntClasspathProvider"); //$NON-NLS-1$
		IVMInstall defaultInstall = null;
		defaultInstall = JavaRuntime.getDefaultVMInstall();
		//try {
		//defaultInstall = JavaRuntime.computeVMInstall(workingCopy);
		//} catch (CoreException e) {
		//core exception thrown for non-Java project
		//defaultInstall= JavaRuntime.getDefaultVMInstall();
		//}
		if (defaultInstall != null) 
		{
			String vmName = defaultInstall.getName();
			String vmTypeID = defaultInstall.getVMInstallType().getId();
			workingCopy.setAttribute(	IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_NAME,	vmName);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE,	vmTypeID);
		}
		workingCopy.setAttribute(	IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,MAIN_TYPE_NAME);
		workingCopy.setAttribute(DebugPlugin.ATTR_PROCESS_FACTORY_ID,	REMOTE_ANT_PROCESS_FACTORY_ID);
		workingCopy.setAttribute(	IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,"org.eclipse.ant.internal.ui.antsupport.InternalAntRunner");
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,(String) null);

		workingCopy.setAttribute("org.eclipse.debug.ui.ATTR_LAUNCH_IN_BACKGROUND", true);
		if (captureOutput) 
		{
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_SHOW_CONSOLE", true);
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_CAPTURE_OUTPUT", true);
		} 
		else 
		{
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_SHOW_CONSOLE", false);
			workingCopy.setAttribute("org.eclipse.ui.externaltools.ATTR_CAPTURE_OUTPUT", false);
		}
		workingCopy.setAttribute(IDebugUIConstants.ATTR_PRIVATE, true);
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, file.getProject().getName());
		workingCopy.setAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_TARGETS, targets);
		return workingCopy;
	}

}
