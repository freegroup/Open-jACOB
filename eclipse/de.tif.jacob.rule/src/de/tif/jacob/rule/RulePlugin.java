package de.tif.jacob.rule;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
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
import de.tif.jacob.rule.util.OverlayImageDescriptor;
import de.tif.jacob.util.StringUtil;

/**
 * The main plugin class to be used in the desktop.
 */
public class RulePlugin extends AbstractUIPlugin  implements ISelectionListener{

  IProject selectedProject = null;
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
	private static RulePlugin plugin;
	
	/**
	 * The constructor.
	 */
	public RulePlugin() {
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
	public static RulePlugin getDefault() {
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
          selectedProject= file.getProject();
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
