package de.tif.jacob.module.wiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import de.tif.jacob.designer.JacobDesigner;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends de.tif.jacob.module.Activator {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.tif.jacob.module.wiki";

	
	/**
	 * The constructor
	 */
	public Activator() 
  {
		super();
	}
  
  public static String readTemplateFile(String filename) 
  {
    IPath path = new Path("templates"+SystemUtils.FILE_SEPARATOR+filename);
    Bundle bundle = getDefault().getBundle();
    String line;
    StringBuffer buffer= new StringBuffer(512);
    BufferedReader reader= null;
    try 
    {
        reader= new BufferedReader(new InputStreamReader(FileLocator.openStream(bundle,path,false)));
        while ((line= reader.readLine()) != null) 
        {
            buffer.append(line);
            buffer.append(SystemUtils.LINE_SEPARATOR);
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
