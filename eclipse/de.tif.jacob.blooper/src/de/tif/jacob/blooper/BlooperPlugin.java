package de.tif.jacob.blooper;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import de.tif.jacob.blooper.util.OverlayImageDescriptor;


/**
 * The main plugin class to be used in the desktop.
 */
public class BlooperPlugin extends AbstractUIPlugin  implements ISelectionListener 
{
  public final static QualifiedName PROPERTY_CONNECTED   = new QualifiedName("jacob.blooper", "connected");
  public final static QualifiedName PROPERTY_URL         = new QualifiedName("jacob.blooper", "url");
  public final static QualifiedName PROPERTY_USER        = new QualifiedName("jacob.blooper", "user");
  public final static QualifiedName PROPERTY_PASSWORD    = new QualifiedName("jacob.blooper", "password");
  public final static QualifiedName PROPERTY_APPLICATION = new QualifiedName("jacob.blooper", "application");

  static class DecorationType
  {
    ImageRegistry registry   = new ImageRegistry();
    ImageDescriptor decoration;
    String decoName;
    private DecorationType(String decoImage)
    {
      decoName = decoImage;
    }

    protected Image getImage(String key)
    {
      Image image = registry.get(key);
      if (null == image)
      {
        if(decoration==null && decoName!=null)
          decoration =  this.createImageDescriptor(decoName);
        ImageDescriptor overlay;
        if(decoration!=null)
          overlay = new OverlayImageDescriptor( this.createImageDescriptor(key),decoration);
        else
          overlay = this.createImageDescriptor(key);
        registry.put(key, overlay);
        return registry.get(key);
      }
      return image;
    }
    
    protected ImageDescriptor getImageDescriptor(String key)
    {
      ImageDescriptor descriptor = registry.getDescriptor(key);
      if (descriptor == null)
      {
        if(decoration==null&& decoName!=null)
          decoration =  this.createImageDescriptor(decoName);
        ImageDescriptor overlay;
        if(decoration!=null)
          overlay = new OverlayImageDescriptor( this.createImageDescriptor(key),decoration);
        else
          overlay = this.createImageDescriptor(key);
        registry.put(key, overlay);
        return registry.getDescriptor(key);
      }
      return descriptor;
    }
    
    private ImageDescriptor createImageDescriptor(String filename) 
    {
      String iconPath = "icons/"; //$NON-NLS-1$
      System.out.println(filename);
      try 
      {
        URL installURL = getDefault().getDescriptor().getInstallURL();
        URL url = new URL(installURL, iconPath + filename);
        
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
  
  public final static DecorationType DECORATION_ERROR  = new DecorationType("deco_error.png");
  public final static DecorationType DECORATION_HOOK    = new DecorationType("deco_java.png");
  public final static DecorationType DECORATION_WARNING= new DecorationType("deco_warning.png");
  public final static DecorationType DECORATION_INFO   = new DecorationType("deco_info.png");
  public final static DecorationType DECORATION_NONE   = new DecorationType(null);
 
  //The shared instance.
	private static BlooperPlugin plugin;
  IProject selectedProject = null;
	
	/**
	 * The constructor.
	 */
	public BlooperPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this);
	}

	/**
	 * Returns the shared instance.
	 */
	public static BlooperPlugin getDefault() {
		return plugin;
	}

  public static Image getImage(String key)
  {
    return DECORATION_NONE.getImage(key);
  }
  
  public static Image getImage(String key, DecorationType type)
  {
    return type.getImage(key);
  }

  public static ImageDescriptor getImageDescriptor(String key)
  {
    return DECORATION_NONE.getImageDescriptor(key);
  }
  
  public static ImageDescriptor getImageDescriptor(String key, DecorationType type)
  {
    return type.getImageDescriptor(key);
  }
  


	public static void showException(Throwable e)
	{
    MultiStatus errorStatus = getDefault().getServiceInfo(e);

    // Write to the error log
    getDefault().getLog().log(errorStatus);
    // Show an Error Dialog
    ErrorDialog.openError(null,"Error","Unexpected Error!.\nSelect Details>> for more information.\nSee the Error Log for more information.",errorStatus);
    // print the exception to the debugger console
		e.printStackTrace();
	}
	
  public MultiStatus getServiceInfo(Throwable exception) 
  {
    Bundle bundle = getBundle();
    String symbolicName  = bundle.getSymbolicName();
    String bundleName    = ""+bundle.getHeaders().get("Bundle-Name");
    String bundleVendor  = ""+bundle.getHeaders().get("Bundle-Vendor");
    String bundleVersion = ""+bundle.getHeaders().get("Bundle-Version");
    MultiStatus status = new MultiStatus(symbolicName,Status.ERROR,  (exception.getMessage()!=null)?exception.getMessage():"", exception);
    
    // Put the information into their own status containers because this
    // forces new lines in the details section of the ErrorDialog
    //
    status.add(createStatus(Status.ERROR, "Plug-in Vendor: " + bundleVendor));
    status.add(createStatus(Status.ERROR, "Plug-in Name: " + bundleName));
    status.add(createStatus(Status.ERROR, "Plug-in ID: " + symbolicName));
    status.add(createStatus(Status.ERROR, "Version: " + bundleVersion));
    return status;
  }
  
  /**
   * Method createStatus is helper method that creates a Status object.
   */
  protected Status createStatus(int severity, String msg) 
  {
    return new Status(severity, getBundle().getSymbolicName(), IStatus.OK, msg, null);
  }
  
  public void selectionChanged(IWorkbenchPart part, ISelection selection)
  {
    try
    {
      
      if (selection instanceof IStructuredSelection)
      {
        Object first = ((IStructuredSelection) selection).getFirstElement();
        if (first instanceof IResource)
        {
          IResource file = (IResource) first;
          if(file.exists())
            selectedProject= file.getProject();
        }
        else if (first instanceof IJavaElement)
        {
          IJavaElement unit =(IJavaElement)first;
          if(unit.exists())
          {
            IResource file = unit.getCorrespondingResource();
            if(file!=null)
              selectedProject= file.getProject();
          }
         
        }
      }
    }
    catch (Throwable e)
    {
      showException(e);
    }
  }
  public IProject getSelectedProject()
  {
    return  selectedProject;
  }
  
}
