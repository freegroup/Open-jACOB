/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.deployment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.cert.Certificate;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.util.clazzloader.PriorityExtendedClassLoader;

/**
 * Application class loader.
 * <P>
 * 
 * This security class loader will assign a fixed code source to all classes
 * loaded. The URL of the code source will be build up as follows:<br>
 * 
 * <pre>
 * &quot;file:${jacob.home}/WEB-INF/jacapp/{applicationName}&quot;
 * </pre>
 * 
 * If jACOB runs under a security manager, permissions could be granted to
 * different jACOB application as follows:<br>
 * 
 * <pre>
 *     grant codeBase &quot;file:${catalina.home}/webapps/jacob/*&quot; {
 *     permission java.security.AllPermission;
 *     };
 *     grant codeBase &quot;file:${catalina.home}/webapps/jacob/WEB-INF/classes/-&quot; {
 *     permission java.security.AllPermission;
 *     };
 *     grant codeBase &quot;file:${catalina.home}/webapps/jacob/WEB-INF/lib/-&quot; {
 *     permission java.security.AllPermission;
 *     };
 *     grant codeBase &quot;file:${catalina.home}/webapps/jacob/WEB-INF/jacapp/admin&quot; {
 *     permission java.security.AllPermission;
 *     };
 *     grant codeBase &quot;file:${catalina.home}/webapps/jacob/WEB-INF/jacapp/news&quot; {
 *     permission java.security.AllPermission;
 *     };
 *     grant codeBase &quot;file:${catalina.home}/webapps/jacob/WEB-INF/jacapp/nanny&quot; {
 *     permission java.security.AllPermission;
 *     };
 * </pre>
 */
public final class ApplicationClassLoader extends PriorityExtendedClassLoader
{
  /**
   * Code source of the classes loaded by this class loader.
   */
  private final CodeSource codeSource;

  private IApplicationDefinition appDef;

  public ApplicationClassLoader(ClassLoader parentClassLoader, String applicationName)
  {
    super(parentClassLoader);
    try
    {
      URL url = new URL("file", "", Bootstrap.getJacappPath().replace(File.separatorChar, '/') + applicationName);
      
      // (Certificate[]) to avoid ambiguous compile error in jdk 1.5
      this.codeSource = new CodeSource(url, (Certificate[]) null);
    }
    catch (MalformedURLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  public IApplicationDefinition getApplicationDefinition()
  {
    return appDef;
  }

  protected void setApplicationDefinition(IApplicationDefinition appDef)
  {
    if (this.appDef == null)
      this.appDef = appDef;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.util.clazzloader.ExtendedClassLoader#determineCodeSource(java.lang.String)
   */
  protected CodeSource determineCodeSource(String name)
  {
    return this.codeSource;
  }
}
