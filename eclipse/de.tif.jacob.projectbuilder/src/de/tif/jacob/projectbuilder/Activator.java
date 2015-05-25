package de.tif.jacob.projectbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import de.tif.jacob.designer.JacobDesigner;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.tif.jacob.projectbuilder";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) 
  {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
  
  public static String readFile(String filename) 
  {
    IPath path = new Path(filename);
    Bundle bundle = getDefault().getBundle();
    String line;
    String separator= System.getProperty("line.separator"); //$NON-NLS-1$
    StringBuffer buffer= new StringBuffer(512);
    BufferedReader reader= null;
    try 
    {
        reader= new BufferedReader(new InputStreamReader(FileLocator.openStream(bundle,path,false)));
        while ((line= reader.readLine()) != null) 
        {
            buffer.append(line);
            buffer.append(separator);
        }
    } 
    catch (IOException io) 
    {
        JacobDesigner.showException(io);
    } 
    finally 
    {
        if (reader != null) 
        {
            try { reader.close(); } catch (IOException e) {}
        }
    }
    return buffer.toString();
  }
}
