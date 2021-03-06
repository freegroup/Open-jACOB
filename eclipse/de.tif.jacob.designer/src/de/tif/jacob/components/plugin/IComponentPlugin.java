/*
 * Created on 20.02.2009
 *
 */
package de.tif.jacob.components.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.components.plugin.PluginComponentManager.Group;
import de.tif.jacob.core.Version;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.UIPluginComponentModel;
import de.tif.jacob.designer.util.FileUtil;
import de.tif.jacob.util.file.Directory;

public abstract class IComponentPlugin
{
  public abstract ImageDescriptor  getPaletteImage();
  public abstract String getName();
  public abstract String getJavaImplClass();
  public abstract Class getFigureClass();
  public abstract Class getEventHandlerTemplateClass();
  public abstract String getPluginId();
  public abstract String getPluginVersion();
  public abstract Version getRequiredJacobVersion();
  
  public  Group getType()
  {
    return Group.normal;
  }
  
  public void renameRelationsetReference(UIPluginComponentModel model, String from, String to)
  {
    
  }
  
  public IPropertyDescriptor[] getPropertyDescriptors(UIPluginComponentModel model, IPropertyDescriptor[] superProperties)
  {
    return superProperties;
  }

  public boolean setPropertyValue(UIPluginComponentModel model, Object propName, Object val)
  {
    return false;
  }
  
  public Object getPropertyValue(UIPluginComponentModel model, Object propName)
  {
    return null;
  }
  
  public void checkResources() throws Exception
  {
    addProjectLibraries(getPluginId(), "lib");
    addFiles(getPluginId(), "web");
  }

  protected void addProjectLibraries(String pluginId, String subPath) throws CoreException
  {
    try
    {
      IProject project = JacobDesigner.getPlugin().getSelectedProject();
      IJavaProject  javaProject = JavaCore.create(project);

      String templateProjectPath="templates"+File.separator+subPath+File.separator;

// Use this for Eclipse >3.4
URL installURL = Platform.getBundle(pluginId).getEntry("/");
String pluginInstallDir = Platform.resolve(installURL).getFile().substring(1);
System.out.println("NEW: "+pluginInstallDir);

      IPluginRegistry registry = Platform.getPluginRegistry();
      IPluginDescriptor descriptor = registry.getPluginDescriptor(pluginId);
      URL pluginURL = descriptor.getInstallURL();
      pluginURL = Platform.asLocalURL(pluginURL);
      Iterator iter = Directory.getAll(findFileInPlugin(pluginId,templateProjectPath).toFile(),true).iterator();
      while(iter.hasNext())
      {
        File file = (File)iter.next();
        if(file.getAbsolutePath().indexOf("/CVS/")==-1 && file.getAbsolutePath().indexOf("/.svn/")==-1)
        {
          String pluginFilePath   = file.getAbsolutePath().substring(pluginURL.getPath().length()-1+templateProjectPath.length());

          // platform depending
          if(!pluginFilePath.startsWith(File.separator))
            pluginFilePath = File.separator+pluginFilePath;
System.out.println("OLD: "+File.separator+subPath+pluginFilePath);

          IFile newFile = project.getFile(File.separator+subPath+pluginFilePath);
          if(!newFile.exists())
          {
            try
            {
              newFile.create(new FileInputStream(file), false, null);
              if("jar".equalsIgnoreCase(newFile.getFileExtension()))
                addJar(javaProject, newFile.getFullPath());
            }
            catch(Exception exc)
            {
              JacobDesigner.showException(exc);
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void addFiles(String pluginId, String subPath) throws CoreException
  {
    try
    {
      IProject project = JacobDesigner.getPlugin().getSelectedProject();
      IJavaProject  javaProject = JavaCore.create(project);

      String templateProjectPath="templates"+File.separator+subPath+File.separator;
      
      IPluginRegistry registry = Platform.getPluginRegistry();
      IPluginDescriptor descriptor = registry.getPluginDescriptor(pluginId);
      URL pluginURL = descriptor.getInstallURL();
      pluginURL = Platform.asLocalURL(pluginURL);
      Iterator iter = Directory.getAll(findFileInPlugin(pluginId,templateProjectPath).toFile(),true).iterator();
      while(iter.hasNext())
      {
        File file = (File)iter.next();
        if(file.getAbsolutePath().indexOf("/CVS/")==-1 && file.getAbsolutePath().indexOf("/.svn/")==-1)
        {
          
          if(file.getParentFile().getAbsolutePath().indexOf(templateProjectPath)!=-1)
          {
            int index = file.getParentFile().getAbsolutePath().indexOf(templateProjectPath)+templateProjectPath.length()-1;
            String projectPath = subPath+file.getParentFile().getAbsolutePath().substring(index);
            try
            {
              FileUtil.createFolder(project.getFolder(projectPath));
            }
            catch (Exception e1)
            {
            }
          }
          try
          {
            String pluginFilePath   = subPath+file.getAbsolutePath().substring(pluginURL.getPath().length()-1+templateProjectPath.length());
            IFile newFile = project.getFile(pluginFilePath);
            newFile.create(new FileInputStream(file), false, null);
          }
          catch(Exception e)
          {
            
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  protected static void addJar(IJavaProject javaProject, IPath jar) throws MalformedURLException, IOException, JavaModelException
  {
    IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
    // check whenether the elements is already part of the classpath
    //
    for (IClasspathEntry entry : oldEntries)
    {
      if(entry.getPath().equals(jar))
        return;
    }
    IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
    System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
    newEntries[oldEntries.length] = JavaCore.newLibraryEntry(jar, null, null);
    javaProject.setRawClasspath(newEntries, null);
  }

  protected Path findFileInPlugin(String pluginId, String file) throws MalformedURLException, IOException
  {
    IPluginRegistry registry = Platform.getPluginRegistry();
    IPluginDescriptor descriptor = registry.getPluginDescriptor(pluginId);
    URL pluginURL = descriptor.getInstallURL();
    URL jarURL = new URL(pluginURL, file);
    URL localJarURL = Platform.asLocalURL(jarURL);
    return new Path(localJarURL.getPath());
  }

}
