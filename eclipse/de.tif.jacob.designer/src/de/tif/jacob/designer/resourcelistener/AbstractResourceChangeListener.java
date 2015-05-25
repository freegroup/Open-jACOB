/*
   This file is part of Open-jACOB
   Copyright (C) 2005-2010 Andreas Herz | FreeGroup

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; version 2 of the License.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License     
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
   USA

*/
package de.tif.jacob.designer.resourcelistener;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.util.StringUtil;

// TODO: Auto-generated Javadoc
/**
 * 
 */
public abstract class AbstractResourceChangeListener implements IResourceChangeListener
{
  
  /**
   * 
   */
  private final static String JAVA_DIR="java";
  
  /**
   * 
   * 
   * @param resource 
   * 
   * @return 
   */
  protected static String toClassName(IResource resource)
  {
    String name = FilenameUtils.getBaseName(resource.getName());
    String path = StringUtil.replace(resource.getParent().getProjectRelativePath().toString(),"/",File.separator);
    
    return StringUtil.replace(StringUtil.replace(path, JAVA_DIR+File.separator,""),File.separator,".")+"."+name;
  }
  
  /**
   * 
   * 
   * @param resource 
   * 
   * @return 
   */
  protected static String toClassName(IPath resource)
  {
    // /jacob.caretaker/java/test/AllTests.java -> test/AllTests
    String path = resource.removeFileExtension().removeFirstSegments(2).toString();
    
    //  test/AllTests -> test.AllTests
    path = StringUtil.replace(path, File.separator,".");
    
    return path;
  }
  
  /**
   * 
   * 
   * @param resource 
   * 
   * @return 
   */
  protected static String toShortClassName(IResource resource)
  {
    return FilenameUtils.getBaseName(resource.getName());
  }
  
  /**
   * 
   * 
   * @param subPath 
   * 
   * @return 
   */
  protected static IPath getJavaSourcePath(String subPath)
  {
    if(subPath.startsWith("/"))
      return new Path(JacobDesigner.getPlugin().getSelectedProject().getName()+File.separator+JAVA_DIR+subPath);
    return new Path(JacobDesigner.getPlugin().getSelectedProject().getName()+File.separator+JAVA_DIR+File.separator+subPath);
  }
}
