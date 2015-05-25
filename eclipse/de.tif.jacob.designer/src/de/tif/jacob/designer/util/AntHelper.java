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
 * Created on 25.05.2005
 *
 */
package de.tif.jacob.designer.util;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import org.eclipse.ant.internal.ui.launchConfigurations.IAntLaunchConfigurationConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;

public class AntHelper
{
    File workingDir = null;
    File antFile = null;
    Map userProperties = new Properties();
    String toolArguments = null;
    
    public AntHelper( File antFile)
    {
        this( antFile, antFile.getParentFile());
    }
    
    public AntHelper( File antFile, File workingDir)
    {
        this.workingDir = workingDir;
        this.antFile = antFile;
    }
    
    public ILaunch execute( IProgressMonitor monitor) throws CoreException
    {
        
        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(IAntLaunchConfigurationConstants.ID_ANT_LAUNCH_CONFIGURATION_TYPE);

        String name = launchManager.generateUniqueLaunchConfigurationNameFrom( antFile.getName());
        ILaunchConfigurationWorkingCopy workingCopy=type.newInstance(null, name);

        workingCopy.setAttribute( IExternalToolConstants.ATTR_CAPTURE_OUTPUT, true);
        workingCopy.setAttribute( IExternalToolConstants.ATTR_SHOW_CONSOLE, true);

        workingCopy.setAttribute( IAntLaunchConfigurationConstants.ATTR_ANT_PROPERTIES, userProperties);
        //String location = ToolUtil.buildVariableTag( IExternalToolConstants.VAR_WORKSPACE_LOC, BeehiveHomePart.class.getResource( "copy-localmockserver.xml").getFile());
        //workingCopy.setAttribute( IExternalToolConstants.ATTR_LOCATION, location);
        workingCopy.setAttribute( IExternalToolConstants.ATTR_TOOL_ARGUMENTS, toolArguments );
        workingCopy.setAttribute( IExternalToolConstants.ATTR_LOCATION, antFile.getAbsolutePath());
        workingCopy.setAttribute( IExternalToolConstants.ATTR_WORKING_DIRECTORY, workingDir.getAbsolutePath());
        workingCopy.setAttribute( IDebugUIConstants.ATTR_PRIVATE, true);
        workingCopy.setAttribute( IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);

        
        //workingCopy.setAttribute( "process_factory_id", "org.eclipse.ant.ui.remoteAntProcessFactory");
//        workingCopy.setAttribute( IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "org.eclipse.ant.internal.ui.antsupport.InternalAntRunner");
//        workingCopy.setAttribute( IJavaLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE, "org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType");
//        workingCopy.setAttribute( IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xmx256m" );
//        workingCopy.doSave();
        //workingCopy.setAttribute( IExternalToolConstants.VAR_WORKSPACE_LOC, getProject());
        //((ILaunchConfigurationWorkingCopy) configuration).setAttribute(IExternalToolConstants.ATTR_ANT_TARGETS, targetAttribute);
        ///IExternalToolConstants.ID_ANT_LAUNCH_CONFIGURATION_TYPE;

        return workingCopy.launch( "run", monitor);
    }
    
    public File getAntFile() 
    {
        return antFile;
    }

    public String getToolArguments() 
    {
        return toolArguments;
    }

    public Map getUserProperties() 
    {
        return userProperties;
    }

    public File getWorkingDir() 
    {
        return workingDir;
    }

    public void setAntFile(File file) 
    {
        antFile = file;
    }

    public void setToolArguments(String string) 
    {
        toolArguments = string;
    }

    public void setUserProperties(Map map) 
    {
        userProperties = map;
    }

    public void setWorkingDir(File file) 
    {
        workingDir = file;
    }
}

