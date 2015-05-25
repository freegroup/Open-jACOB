package de.tif.jacob.module.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
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
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import de.tif.jacob.core.Version;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.jobs.GenerateBrowserClassesJob;
import de.tif.jacob.designer.jobs.GenerateTableClassesJob;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.PhysicalDataModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;
import de.tif.jacob.designer.model.UIExternalFormModel;
import de.tif.jacob.designer.model.UIHtmlFormModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.file.Directory;

public abstract class AbstractModuleAction implements IObjectActionDelegate 
{
  protected String modulename = null;
  protected String Modulename = null;
  protected String MODULENAME = null;


  protected static final Set fileTypesToParse;
  
  static
  {
    Set tmp = new HashSet();
    tmp.add("java");
    tmp.add("xml");
    tmp.add("properties");
    tmp.add("jad");
    tmp.add("jsp");
    tmp.add("js");
    tmp.add("html");
    tmp.add("htm");
    tmp.add("txt");
    
    fileTypesToParse = SetUtils.unmodifiableSet(tmp);
  }
  
  public abstract Version getRequiredJACOBVersion();
  public abstract String readTemplateFile(String filename);
  public abstract String getModuleTypeName();
  public abstract String getModulePrefix();
  public abstract String getPluginId();
  public abstract void   modifyTargetModel(JacobModel sourceModel, JacobModel targetModel);
  
  /**
	 * Constructor for Action1.
	 */
	public AbstractModuleAction() 
  {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
  {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public final void run(IAction action) 
  {
    IProject project = JacobDesigner.getPlugin().getSelectedProject();
    String pluginId = getPluginId();
    try
    {
      Version currentJacobBaseVersion = JacobDesigner.getUsedJacobBaseVersion();
      if(!currentJacobBaseVersion.isGreaterOrEqual(getRequiredJACOBVersion()))
      {
        MessageDialog.openError(null,"Wrong jACOB Version","The project did have a wrong version of the jacobBase.jar for "+getModuleTypeName()+" integration.\n\n"+
                                                           "Required Version:"+getRequiredJACOBVersion().toString()+"\n"+
                                                           "Project Version:"+currentJacobBaseVersion.toString()+"\n\n"+
                                                           "Replace the [./build/jacobBase.jar] and [./build/OpenjACOB.war] with the latest jACOB version.");
        return;
      }
    }
    catch(Exception exc)
    {
      JacobDesigner.showException(exc);
      return;
    }
    
    if(setModuleName()==false)
      return;
    
    String jad = readTemplateFile("application.jad");
    
    jad = StringUtil.replace(jad,"{modulename}",modulename);
    jad = StringUtil.replace(jad,"{Modulename}",Modulename);
    jad = StringUtil.replace(jad,"{MODULENAME}",MODULENAME);
    
    JacobModel targetModel = JacobDesigner.getPlugin().getModel();
    try
    {
      PhysicalDataModel datamodel = targetModel.getDatasourceModels().get(0);
      String datamodelName =datamodel.getName();
      JacobModel sourceModel =new JacobModel(JacobDesigner.getPlugin().getSelectedProject(),new StringReader(jad));

      // Alle Tabellen übertragen
      //
      String datasource = modulename+"DataSource";
      for (TableModel table : sourceModel.getTableModels())
      {
        table.setJacobModel(targetModel);
        // Falls die Tabelle in der Standarddatasource des template JAD ist, dann wird
        // diese auch in die Standarddatasource des Target eingefügt.
        //
        if(datasource.equals(table.getCastor().getDatasource()))
        {
            System.out.println("using target datasource ["+datamodelName+"]");;
            table.setDatasource(datamodelName);
        }
        else
        {
            PhysicalDataModel ds = targetModel.getDatasourceModel(table.getCastor().getDatasource());
            if(ds==null)
            {
              System.out.println("create new datasource ["+table.getCastor().getDatasource()+"]");;
              ds = new PhysicalDataModel(targetModel,table.getCastor().getDatasource());
              targetModel.addElement(ds);
            }
            System.out.println("using target datasource ["+ds.getName()+"]");;
            table.setDatasource(ds.getName());
        }
        targetModel.addElement(table);
      }
      
      // Alle Aliase übertragen
      //
      for (TableAliasModel alias : sourceModel.getTableAliasModels())
      {
        alias.setJacobModel(targetModel);
        targetModel.addElement(alias);
      }
      
      // Alle Browser übertragen
      //
      for(BrowserModel browser : sourceModel.getBrowserModels())
      {
        browser.setJacobModel(targetModel);
        targetModel.addElement(browser);
      }
      
      this.modifyTargetModel(sourceModel, targetModel);
      
      // Alle Scripte übertragen
      addFiles(pluginId,"java");
      
      // Modell helper Klassen neu erzeugen
      new GenerateTableClassesJob().schedule();
      
      // Browser helper Klassen neu erzeugen
      new GenerateBrowserClassesJob().schedule();
      

      addFiles(pluginId,"web");

      // jACOB buildIn lib und project spezifische libs 
      // kopieren und in dem Klasspfad des Projektes eintragen.
      //
      addProjectLibraries(pluginId, "lib");
      addProjectLibraries(pluginId, "build");
    }
    catch(Exception exc)
    {
      JacobDesigner.showException(exc);
    }
  }


  private boolean setModuleName()
  {
    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    InputDialog dialog = new InputDialog(shell, "Add "+getModuleTypeName()+" Module", "Enter the name for the "+getModuleTypeName()+" module",
        getModulePrefix(),
        new IInputValidator()
        {
          public String isValid(String newText)
          {
            JacobModel model = JacobDesigner.getPlugin().getModel();
            if(!StringUtils.containsOnly(newText,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
              return "Invalid character in module name";
  
            if(model.getTableModel(newText)!=null)
              return "Name conflict with existing tables";
            
            if(model.getTableAliasModel(newText)!=null)
              return "Name conflict with existing tables";
            
            if(model.getFormModel(newText)!=null)
              return "Name conflict with existing forms";
            
            if(model.getBrowserModel(newText+"Browser")!=null)
              return "Name conflict with existing browser";
            
            return null;
          }
        });
    if(dialog.open()==Window.OK)
    {
       modulename = dialog.getValue();
       Modulename = StringUtils.capitalise(modulename);
       MODULENAME = modulename.toUpperCase();
       return true;
    }
    return false;
  }

  protected String replaceKeyWords(String template)
  {
    template = StringUtils.replace(template, "{date}", new Date().toString());
    template = StringUtils.replace(template, "{author}", System.getProperty("user.name"));
    template = StringUtils.replace(template, "{modulename}", modulename);
    template = StringUtils.replace(template, "{Modulename}", Modulename);
    template = StringUtils.replace(template, "{MODULENAME}", MODULENAME);
    
    return template;
  }


  
  protected void addProjectLibraries(String pluginId, String subPath) throws CoreException
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
          String pluginFilePath   = file.getAbsolutePath().substring(pluginURL.getPath().length()-1+templateProjectPath.length());
          int index = file.getParentFile().getAbsolutePath().indexOf(templateProjectPath)+templateProjectPath.length()-1;
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
      String templateProjectPath="templates"+File.separator;
      IProject project = JacobDesigner.getPlugin().getSelectedProject();
      
      IPluginRegistry registry = Platform.getPluginRegistry();
      IPluginDescriptor descriptor = registry.getPluginDescriptor(pluginId);
      URL pluginURL = descriptor.getInstallURL();
      pluginURL = Platform.asLocalURL(pluginURL);
      Iterator iter = Directory.getAll(findFileInPlugin(pluginId,templateProjectPath+subPath+File.separator).toFile(),true).iterator();
      while(iter.hasNext())
      {
        File file = (File)iter.next();
        if(file.getAbsolutePath().indexOf("/CVS/")==-1 && file.getAbsolutePath().indexOf("/.svn/")==-1)
        {
          String sourceFilePath   = file.getAbsolutePath().substring(pluginURL.getPath().length()-1+templateProjectPath.length());
          String targetFilePath   = replaceKeyWords(sourceFilePath);


          if(file.getParentFile().getAbsolutePath().indexOf(templateProjectPath)!=-1)
          {
            int index = file.getParentFile().getAbsolutePath().indexOf(templateProjectPath)+templateProjectPath.length()-1;
            String projectPath = replaceKeyWords(file.getParentFile().getAbsolutePath().substring(index));
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
            IFile newFile = project.getFile(targetFilePath);
            if(fileTypesToParse.contains(FilenameUtils.getExtension(targetFilePath)))
            {
              String template = readTemplateFile( sourceFilePath);
              template = replaceKeyWords(template);
              newFile.create(new StringBufferInputStream(template), false, null);
            }
            else
            {
                newFile.create(new FileInputStream(file), false, null);
            }
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
