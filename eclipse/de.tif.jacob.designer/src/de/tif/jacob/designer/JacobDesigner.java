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
package de.tif.jacob.designer;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.jar.JarFile;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.exolab.castor.xml.Marshaller;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.w3c.dom.Document;

import de.tif.jacob.core.Version;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIPluginComponentModel;
import de.tif.jacob.designer.resourcelistener.ResourceChangeListenerCMD;
import de.tif.jacob.designer.resourcelistener.ResourceChangeListenerEventHandler;
import de.tif.jacob.designer.resourcelistener.ResourceChangeListenerGUI;
import de.tif.jacob.designer.resourcelistener.ResourceChangeListenerI18N;
import de.tif.jacob.designer.resourcelistener.ResourceChangeListenerJAD;
import de.tif.jacob.designer.resourcelistener.ResourceChangeListenerSOAP;
import de.tif.jacob.designer.resourcelistener.ResourceChangeListenerSchedulerSystem;
import de.tif.jacob.designer.resourcelistener.ResourceChangeListenerSchedulerUser;
import de.tif.jacob.designer.util.OverlayImageDescriptor;
import de.tif.jacob.designer.util.Trace;
import de.tif.jacob.util.FastStringWriter;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;


/**
 * 
 */
public class JacobDesigner extends AbstractUIPlugin implements ISelectionProvider, IAdaptable, ISelectionListener 
{
  
  /**
   * 
   */
  static class DecorationType
  {
    
    /**
     * 
     */
    ImageRegistry registry   = new ImageRegistry();
    
    /**
     * 
     */
    ImageDescriptor decoration;
    
    /**
     * 
     */
    String decoImage;
    
    /**
     * 
     * 
     * @param decoImage 
     */
    private DecorationType(String decoImage)
    {
      this.decoImage = decoImage;
    }

    /**
     * 
     * 
     * @param iconPath 
     * 
     * @return 
     */
    protected Image getImage(String iconPath)
    {
      Image image = registry.get(iconPath);
      if (null == image)
      {
        if(decoration==null && decoImage!=null)
          decoration =  this.createImageDescriptor( decoImage);
        ImageDescriptor overlay;
        if(decoration!=null)
          overlay = new OverlayImageDescriptor( this.createImageDescriptor( iconPath),decoration);
        else
          overlay = this.createImageDescriptor(iconPath);
        registry.put(iconPath, overlay);
        return registry.get(iconPath);
      }
      return image;
    }
    
    
    /**
     * 
     * 
     * @param iconPath 
     * 
     * @return 
     */
    protected ImageDescriptor getImageDescriptor(String iconPath)
    {
      ImageDescriptor descriptor = registry.getDescriptor(iconPath);
      if (descriptor == null)
      {
        if(decoration==null&& decoImage!=null)
          decoration =  this.createImageDescriptor(decoImage);
        ImageDescriptor overlay;
        if(decoration!=null)
          overlay = new OverlayImageDescriptor( this.createImageDescriptor(iconPath),decoration);
        else
          overlay = this.createImageDescriptor(iconPath);
        registry.put(iconPath, overlay);
        return registry.getDescriptor(iconPath);
      }
      return descriptor;
    }

    /**
     * 
     * 
     * @param iconPath 
     * 
     * @return 
     */
    private ImageDescriptor createImageDescriptor(String iconPath) 
  	{
  		try 
  		{
  		  URL installURL = getPlugin().getDescriptor().getInstallURL();
  			URL url = new URL(installURL, iconPath);
  			
  			ImageDescriptor desc= ImageDescriptor.createFromURL(url);
  			if(desc==null)
  			  System.out.println("unable to load image ["+url.toString()+"]");
  			return desc;
  		} 
  		catch (MalformedURLException e)
  		{
  		  showException(e);
  			return null;
  		}
  	}
  }
  
  /**
   * 
   */
  public final static DecorationType DECORATION_ERROR  = new DecorationType("icons/deco_error.png");
  
  /**
   * 
   */
  public final static DecorationType DECORATION_HOOK    = new DecorationType("icons/deco_java.png");
  
  /**
   * 
   */
  public final static DecorationType DECORATION_WARNING= new DecorationType("icons/deco_warning.png");
  
  /**
   * 
   */
  public final static DecorationType DECORATION_INFO   = new DecorationType("icons/deco_info.png");
  
  /**
   * 
   */
  public final static DecorationType DECORATION_NONE   = new DecorationType(null);
  
  /**
   * 
   */
  public final static String ID = "de.tif.jacob.designer";
  
  /**
   * 
   */
  private final IResourceChangeListener LISTENER_JAD    = new ResourceChangeListenerJAD();
  
  /**
   * 
   */
  private final IResourceChangeListener LISTENER_SOAP   = new ResourceChangeListenerSOAP();
  
  /**
   * 
   */
  private final IResourceChangeListener LISTENER_I18N   = new ResourceChangeListenerI18N();
  
  /**
   * 
   */
  private final IResourceChangeListener LISTENER_CMD    = new ResourceChangeListenerCMD();
  
  /**
   * 
   */
  private final IResourceChangeListener LISTENER_GUI    = new ResourceChangeListenerGUI();
  
  /**
   * 
   */
  private final IResourceChangeListener LISTENER_SYSTEM = new ResourceChangeListenerSchedulerSystem();
  
  /**
   * 
   */
  private final IResourceChangeListener LISTENER_USER   = new ResourceChangeListenerSchedulerUser();
  
  /**
   * 
   */
  private final IResourceChangeListener LISTENER_EVENT  = new ResourceChangeListenerEventHandler();

  //Resource bundle.
  /**
   * 
   */
  private ResourceBundle resourceBundle;
  
	/**
	 * 
	 */
	private static JacobDesigner instance;

	/**
	 * 
	 */
	private JacobModel jacobModel = null;
	
	/**
	 * 
	 */
	private FormColors formColors;
	
	/**
	 * 
	 */
	private IFile  currentJadFile= null;
	
	/**
	 * 
	 */
	private Set    listeners= new HashSet();
  
  /**
   * 
   */
  private long modificationTime;

  protected boolean isALTKeyPressed;
	
  public boolean isALTKeyPressed()
  {
    return isALTKeyPressed;
  }


  /**
   * 
   */
  private static final String LAST_JACOB_FILE    = "last_jacob_file";
  
  /**
   * 
   */
  private static final String LAST_JACOB_PROJECT = "last_jacob_project";
  
  /**
   * 
   */
  public  static final String DOUBLECLICK_DB     = "double_click_db";
  
  /**
   * 
   */
  public  static final String LABEL_ALIGN        = "default_label_alignment";
  
  /**
   * 
   */
  public  static final String SHOW_RELATION_LABEL= "show_relation_label";

  /**
   * 
   */
  public  static final String TEMPLATE_BUTTON_LEFT = "BUTTON_ALIGN_LEFT";
  
  /**
   * 
   */
  public  static final String TEMPLATE_BUTTON_TOP  = "BUTTON_ALIGN_TOP";
  
  /**
   * 
   */
  public  static final String TEMPLATE_USAGE       = "use button template";
  
  /**
   * 
   * 
   * @param listener 
   */
  public void addSelectionChangedListener(ISelectionChangedListener listener) 
	{
		listeners.add(listener);
	}
	
	/**
	 * 
	 * 
	 * @param listener 
	 */
	public void removeSelectionChangedListener(	ISelectionChangedListener listener) 
	{
		listeners.remove(listener);
	}
	
	/**
	 * 
	 * 
	 * @param selection 
	 */
	public void setSelection(ISelection selection) 
	{
	}
	
	/**
	 * 
	 * 
	 * @return 
	 */
	public ISelection getSelection() 
	{
		return new StructuredSelection(jacobModel);
	}
	

	/**
	 * 
	 */
	public JacobDesigner()
	{
		super();
		instance=this;
	}

	/**
	 * 
	 * 
	 * @return 
	 */
	public static JacobDesigner getPlugin()
	{
		return instance;
	}
	

	/**
	 * 
	 * 
	 * @return 
	 */
	public JacobModel getModel() 
	{
		return jacobModel;
	}
	
	/**
	 * 
	 */
	public void resetModel() 
	{
		jacobModel = null;
		currentJadFile=null;
    Iterator iter = listeners.iterator();
    final ISelectionProvider jd = this;
    while (iter.hasNext())
    {
      ISelectionChangedListener listener = (ISelectionChangedListener) iter.next();
      listener.selectionChanged(null);
    }
	}
	
	/**
	 * 
	 * 
	 * @return 
	 */
	public IFile getModelFile() 
	{
		return currentJadFile;
	}
	
  /**
   * 
   * 
   * @return 
   */
  public boolean hasModelFileChangedOnFilesystem()
  {
    try
    {
      if(currentJadFile!=null)
      {
        currentJadFile.refreshLocal(IResource.DEPTH_ONE,null);
        return currentJadFile.getModificationStamp()>this.modificationTime;
      }
    }
    catch (CoreException e)
    {
      showException(e);
    }
    return false;
  }
  
	/**
	 * 
	 */
	public void reloadModel()
	{
	    setModel(currentJadFile, null, true, true, true);
	}
	
  /**
   * 
   * 
   * @param project 
   */
  public void setModel(IProject project)
  {
    IFile file = project.getFile("application.jad");
    if(file!=null && file.exists())
      setModel(file, null, false,true, false);
    
  }
  
	/**
	 * 
	 * 
	 * @param file 
	 * @param ignoreError 
	 * @param force 
	 * @param pm 
	 */
  public void setModel(final IFile file, IProgressMonitor pm,final boolean ignoreError, boolean force, boolean reloadMode)
  {
    if (file.getName().endsWith(".jad"))
    {
      try
      {
        if (!file.isSynchronized(IResource.DEPTH_ONE))
        {
          file.refreshLocal(IResource.DEPTH_ONE, pm);
        }
        
        if (force==true || currentJadFile == null || !file.getFullPath().equals(currentJadFile.getFullPath()))
        {
          // Save the changed model if the user press [YES]
          //
          if(!reloadMode)
            requestSaveChanges();

          if(jacobModel!=null)
            jacobModel.firePropertyChange(ObjectModel.PROPERTY_JACOBMODEL_CLOSED,jacobModel,null);
            
          final ISelectionProvider jd = this;
          Job job = new Job("Loading jACOB Application definition...")
          {
            protected IStatus run(IProgressMonitor monitor)
            {
              try
              {
                Trace.start("Loading jad-file");
                jacobModel = new JacobModel(file.getProject(), new InputStreamReader(file.getContents(),file.getCharset()));
                Trace.print("reading jad with castor");
                currentJadFile = file;
                Display.getDefault().syncExec(new Runnable()
                {
                  public void run()
                  {
                    Iterator iter = listeners.iterator();
                    Trace.mark();
                    while (iter.hasNext())
                    {
                      ISelectionChangedListener listener = (ISelectionChangedListener) iter.next();
                      Trace.mark();
                      try
                      {
                        listener.selectionChanged(new SelectionChangedEvent(jd, getSelection()));
                      }
                      catch(Exception exc)
                      {
                        showException(exc);
                      }
                      Trace.print("selectionChanged:"+listener.getClass().getName());
                    }
                    Trace.print("notify listeners of model change");
                  }
                }); 
                Trace.stop("Loading jad-file");
                setPluginProperty(LAST_JACOB_FILE,currentJadFile.getProjectRelativePath().toString());
                setPluginProperty(LAST_JACOB_PROJECT,getSelectedProject().getName());
              }
              catch (Exception e)
              {
//                if(ignoreError==false)
                  showException(e);
                currentJadFile = null;
                return Status.CANCEL_STATUS;
              }
              return Status.OK_STATUS;
            }
          };
          job.schedule();
        }
      }
      catch (Throwable e)
      {
        if(ignoreError==false)
          showException(e);
      }
    }
  }
	
	/**
	 * 
	 * 
	 * @return 
	 */
	public IProject getSelectedProject()
	{
		if(currentJadFile!=null)
			return currentJadFile.getProject();
		return  null;
	}
	
  /**
   * 
   * 
   * @param key 
   * 
   * @return 
   */
  public static Image getImage(String key)
  {
    return DECORATION_NONE.getImage("icons/"+key);
  }
  
  /**
   * 
   * 
   * @param key 
   * @param type 
   * 
   * @return 
   */
  public static Image getImage(String key, DecorationType type)
  {
    return type.getImage("icons/"+key);
  }

  /**
   * 
   * 
   * @param key 
   * 
   * @return 
   */
  public static ImageDescriptor getImageDescriptor(String key)
  {
    return DECORATION_NONE.getImageDescriptor("icons/"+key);
  }
  
  /**
   * 
   * 
   * @param key 
   * @param type 
   * 
   * @return 
   */
  public static ImageDescriptor getImageDescriptor(String key, DecorationType type)
  {
    return type.getImageDescriptor("icons/"+key);
  }

  /**
   * 
   * 
   * @param imagePath 
   * @param filename 
   * 
   * @return 
   */
  public static Image getImage(String imagePath, String filename)
  {
    return DECORATION_NONE.getImage(imagePath+filename);
  }
  
	/**
	 * 
	 * 
	 * @param e 
	 */
	public static void showException(Throwable e)
	{
    MultiStatus errorStatus = getPlugin().getServiceInfo(e);

    // Write to the error log
    getPlugin().getLog().log(errorStatus);
    // Show an Error Dialog
    ErrorDialog.openError(null,"Error","Unexpected Error!.\nSelect Details>> for more information.\nSee the Error Log for more information.",errorStatus);
    // print the exception to the debugger console
		e.printStackTrace();
	}
	
  /**
   * 
   * 
   * @param exception 
   * 
   * @return 
   */
  public MultiStatus getServiceInfo(Throwable exception) 
  {
    Bundle bundle = getBundle();
    String symbolicName  = bundle.getSymbolicName();
    String bundleName    = ""+bundle.getHeaders().get("Bundle-Name");
    String bundleVendor  = ""+bundle.getHeaders().get("Bundle-Vendor");
    String bundleVersion = ""+bundle.getHeaders().get("Bundle-Version");
    MultiStatus status = new MultiStatus(symbolicName,Status.ERROR, StringUtil.toSaveString( exception.getMessage()), exception);
    
    // Put the information into their own status containers because this
    // forces new lines in the details section of the ErrorDialog
    //
    status.add(createStatus(Status.ERROR, "Plug-in Vendor: " + bundleVendor));
    status.add(createStatus(Status.ERROR, "Plug-in Name: " + bundleName));
    status.add(createStatus(Status.ERROR, "Plug-in ID: " + symbolicName));
    status.add(createStatus(Status.ERROR, "Version: " + bundleVersion));
    status.add(createStatus(Status.ERROR, "Exception:"+ExceptionUtils.getStackTrace(exception)));
    return status;
  }

  /**
   * 
   * 
   * @param msg 
   * @param severity 
   * 
   * @return 
   */
  protected Status createStatus(int severity, String msg) 
  {
    return new Status(severity, getBundle().getSymbolicName(), IStatus.OK, msg, null);
  }
  
	/**
	 * 
	 * 
	 * @param display 
	 * 
	 * @return 
	 */
	public FormColors getFormColors(Display display) 
	{
		if (formColors == null) 
		{
			formColors = new FormColors(display);
			formColors.markShared();
		}
		return formColors;
	}
	
  /**
   * 
   * 
   * @param context 
   * 
   * @throws Exception 
   */
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    try
    {
      URL messagesUrl = find(new Path("$nl$/messages.properties"));
      if (messagesUrl != null)
      {
        resourceBundle = new PropertyResourceBundle(messagesUrl.openStream());
      }
    }
    catch (IOException x)
    {
      resourceBundle = null;
    }


    Display display = this.getWorkbench().getDisplay();
    System.out.println("Register Keyboard LISTENER.......");
    display.addFilter(SWT.KeyDown, new Listener()
    {
      public void handleEvent(Event event)
      {
        if(event.keyCode==SWT.ALT)
          isALTKeyPressed=true;
      }
    });
    display.addFilter(SWT.KeyUp, new Listener()
    {
      public void handleEvent(Event event)
      {
        if(event.keyCode==SWT.ALT)
          isALTKeyPressed = false;
      }
    });
    
    
    try
    {
      startResourceListener();
      String fileName    = getPluginProperty(LAST_JACOB_FILE);
      String projectName = getPluginProperty(LAST_JACOB_PROJECT);
      if(fileName!=null && projectName!=null && fileName.length()>0 && projectName.length()>0)
      {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        IFile file = project.getFile(fileName);
        setModel(file, null, false, false, false);
      }
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
  }
  
  /**
   * 
   */
  public final void startResourceListener()
  {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
//  workspace.addResourceChangeListener(new ResourceChangeListenerReporter());
	  workspace.addResourceChangeListener(LISTENER_JAD);
	  workspace.addResourceChangeListener(LISTENER_SOAP);
	  workspace.addResourceChangeListener(LISTENER_I18N);
	  workspace.addResourceChangeListener(LISTENER_CMD);
	  workspace.addResourceChangeListener(LISTENER_GUI);
	  workspace.addResourceChangeListener(LISTENER_SYSTEM);
	  workspace.addResourceChangeListener(LISTENER_USER);
	  workspace.addResourceChangeListener(LISTENER_EVENT);
  }
  
  /**
   * 
   */
  public final void stopResourceListener()
  {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
//  workspace.addResourceChangeListener(new ResourceChangeListenerReporter());
	  workspace.removeResourceChangeListener(LISTENER_JAD);
	  workspace.removeResourceChangeListener(LISTENER_SOAP);
	  workspace.removeResourceChangeListener(LISTENER_I18N);
	  workspace.removeResourceChangeListener(LISTENER_CMD);
	  workspace.removeResourceChangeListener(LISTENER_GUI);
	  workspace.removeResourceChangeListener(LISTENER_SYSTEM);
	  workspace.removeResourceChangeListener(LISTENER_USER);
	  workspace.removeResourceChangeListener(LISTENER_EVENT);
  }
  
	/**
	 * 
	 * 
	 * @param context 
	 * 
	 * @throws Exception 
	 */
	
	public void stop(BundleContext context) throws Exception 
	{
	  try
	  {
	    requestSaveChanges();
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this);
	  }
    catch(Exception e)
    {
      // ignore during  the shutdown 
    }
	  finally
	  {
		  super.stop(context);
	  }

	}

  /**
   * 
   */
  public void saveCurrentI18N()
  {
    if(jacobModel!=null)
    {
      try
      {
        stopResourceListener();
        
        jacobModel.saveRelatedResources(getSelectedProject());

        startResourceListener();
      }
      catch (Exception e)
      {
        showException(e);
      }
    }
  }
    
	/**
	 * 
	 */
	public void saveCurrentModel()
	{
	  if(jacobModel!=null)
	  {
	    try
	    {
	      stopResourceListener();
	      
	      Document doc = XMLUtils.newDocument();
	      Marshaller.marshal(jacobModel.getJacob(), doc);
	      
	      FastStringWriter writer = new FastStringWriter(1024*100);
	      org.apache.xml.serialize.OutputFormat outFormat = new org.apache.xml.serialize.OutputFormat();
	      outFormat.setIndenting(true);
	      outFormat.setIndent(2);
	      outFormat.setLineWidth(200);
	      outFormat.setEncoding("ISO-8859-1");
	      org.apache.xml.serialize.XMLSerializer xmlser = new org.apache.xml.serialize.XMLSerializer(writer, outFormat);

	      xmlser.serialize(doc); //replace your_document with reference to document you want to serialize

	      IFile modelFile = JacobDesigner.getPlugin().getModelFile();
	      modelFile.setContents( new StringBufferInputStream(new String(writer.toCharArray())), true,true, null);
      
        modificationTime = modelFile.getModificationStamp();
	      jacobModel.saveRelatedResources(getSelectedProject());
	      jacobModel.setDirty(false);
	      
	      setPluginProperty(LAST_JACOB_FILE,modelFile.getProjectRelativePath().toString());
	      setPluginProperty(LAST_JACOB_PROJECT,getSelectedProject().getName());

	      startResourceListener();
	    }
	    catch (Exception e)
	    {
	    	showException(e);
	    }
	  }
	  
	}
  
  /**
   * 
   * 
   * @param key 
   * 
   * @return 
   */
  public static String getResourceString(String key)
  {
    ResourceBundle bundle = getPlugin().getResourceBundle();
    try
    {
      return (bundle != null) ? bundle.getString(key) : key;
    }
    catch (MissingResourceException e)
    {
      return key;
    }
  }

  /**
   * 
   * 
   * @return 
   */
  protected ResourceBundle getResourceBundle()
  {
    return resourceBundle;
  }

  /**
   * 
   * 
   * @param key 
   * 
   * @return 
   */
  public static String getPluginProperty(String key)
  {
    return getPlugin().getPreferenceStore().getString(key);
  }
  
  /**
   * 
   * 
   * @param key 
   * 
   * @return 
   */
  public static boolean hasPluginProperty(String key)
  {
    return getPlugin().getPreferenceStore().contains(key);
  }
  
  /**
   * 
   * 
   * @param key 
   * @param defaultValue 
   * 
   * @return 
   */
  public static String getPluginProperty(String key, String defaultValue)
  {
    String value= getPlugin().getPreferenceStore().getString(key);
    if(value.length()==0)
    	return defaultValue;
    return value;
  }

  /**
   * 
   * 
   * @param filePath 
   * 
   * @return 
   */
  public static URL getURL(String filePath) 
  {
    try 
    {
      URL installURL = getPlugin().getDescriptor().getInstallURL();
      return new URL(installURL, filePath);
    } 
    catch (MalformedURLException e)
    {
      showException(e);
      return null;
    }
  }

  
  /**
   * 
   * 
   * @param key 
   * @param defaultValue 
   * 
   * @return 
   */
  public static int getPluginProperty(String key, int defaultValue)
  {
    if(getPlugin().getPreferenceStore().contains(key))
       return getPlugin().getPreferenceStore().getInt(key);
    return defaultValue;
  }

  /**
   * 
   * 
   * @param key 
   * @param defaultValue 
   * 
   * @return 
   */
  public static boolean getPluginProperty(String key, boolean defaultValue)
  {
    if(getPlugin().getPreferenceStore().contains(key))
     return getPlugin().getPreferenceStore().getBoolean(key);
    return defaultValue;
  }

  /**
   * 
   * 
   * @param key 
   * @param value 
   */
  public static void setPluginProperty(String key, boolean value)
  {
    getPlugin().getPreferenceStore().setValue(key, value);
  }
  
  public static Version getUsedJacobBaseVersion() throws Exception
  {
    try
    {
      return Version.parseVersion(new JarFile(JacobDesigner.getPlugin().getSelectedProject().getFile("build"+File.separator+"jacobBase.jar").getLocation().toFile()).getManifest().getMainAttributes().getValue("jACOB-Engine-Version"));
    }
    catch(Exception exc)
    {
      try
      {
        return Version.parseVersion(new JarFile(JacobDesigner.getPlugin().getSelectedProject().getFile("build"+File.separator+"versideBase.jar").getLocation().toFile()).getManifest().getMainAttributes().getValue("Verside-Engine-Version"));
      }
      catch (Exception e)
      {
        return Version.parseVersion(new JarFile(JacobDesigner.getPlugin().getSelectedProject().getFile("build"+File.separator+"queryBase.jar").getLocation().toFile()).getManifest().getMainAttributes().getValue("Query-Engine-Version"));
      }
    }
  }

  /**
   * 
   * 
   * @param key 
   */
  public static void removePluginProperty(String key)
  {
    getPlugin().getPreferenceStore().setToDefault(key);
  }

  /**
   * 
   * 
   * @param key 
   * @param value 
   */
  public static void setPluginProperty(String key, String value)
  {
    getPlugin().getPreferenceStore().setValue(key,value);
  }
  
  /**
   * 
   */
  public void requestSaveChanges()
  {
    // Save the changed model if the user press [YES]
    //
    if(jacobModel!=null)
    {
      String projectName = getSelectedProject().getName();
      if(jacobModel.isDirty() && MessageDialog.openQuestion(null,"Save jACOB Model","Save the changes in the ["+projectName+"] jACOB Application model?")==true)
      {
        saveCurrentModel();
      }
    }
  }
  
  /**
   * 
   * 
   * @param key 
   * 
   * @return 
   */
  public Object getAdapter(Class key)
  {    
    if(key == org.eclipse.ui.IActionFilter.class)
    {
      /*
      IActionFilter filter = new IActionFilter()
      {
        public boolean testAttribute(Object target, String name, String value)
        {
          UICaptionModel caption=((CaptionEditPart)target).getCaptionModel();
          if(name.equals("caption"))
            return caption.getCaption()!=null &&  caption.getCaption().startsWith(value);
          else if(name.equals("exists"))
            return caption.getJacobModel().hasI18NKey(caption.getCaption().substring(1)) == new Boolean(value).booleanValue();
          return false;
        }
      };
      return filter;
      */
    }
    return null;
  }  
  /*
   * (non-Javadoc)
   * 
   * @see ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
   */
  /**
   * 
   * 
   * @param part 
   * @param selection 
   */
  public void selectionChanged(IWorkbenchPart part, ISelection selection)
  {
    try
    {
      if (selection instanceof IStructuredSelection)
      {
        Object first = ((IStructuredSelection) selection).getFirstElement();
        if (first instanceof IFile)
        {
          IFile file = (IFile) first;
          if(file.getName().endsWith(".jad"))
            setModel(file, null,false,false, false);
        }
      }
    }
    catch (Throwable e)
    {
      JacobDesigner.showException(e);
    }
  }
  

  public static boolean ensureVersion(String nameOfFeature, Version version)
  {
    try
    {
      Version currentJacobBaseVersion = JacobDesigner.getUsedJacobBaseVersion();
      if(!currentJacobBaseVersion.isGreaterOrEqual(version))
      {
        MessageDialog.openError(null,"Wrong jACOB Version","The project did have a wrong version of the jacobBase.jar for ["+nameOfFeature+"] integration.\n\n"+
                                                           "Required Version:"+version.toString()+"\n"+
                                                           "Project Version:"+currentJacobBaseVersion.toString()+"\n\n"+
                                                           "Replace the [./build/jacobBase.jar] and [./build/OpenjACOB.war] with the latest jACOB version.");
        return false;
      }
    }
    catch(Exception exc)
    {
      showException(exc);
      return false;
    }
    return true;
  }
}
