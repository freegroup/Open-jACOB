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
 * Created on 25.01.2005
 *
 */
package de.tif.jacob.designer.model;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 *
 */
public class JavaClassModel extends ObjectModel
{
  String fullClassName;
  
  public JavaClassModel(JacobModel jacob, String fullClassName)
  {
    super(jacob);
    this.fullClassName  = fullClassName;
  }
  
  public final String getHookClassName()
  {
    return fullClassName;
  }
  
  public final void setShortName(String shortName) throws Exception
  {
    String save = getShortName();
    if(StringUtil.saveEquals(save,shortName))
      return;
    
    String packageName = ClassUtil.getPackageName(getName());
    
    IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
    ClassFinder.renameClass(getName(), packageName+"."+shortName, myJavaProject);
    this.fullClassName  = packageName+"."+shortName;
		firePropertyChange(PROPERTY_NAME_CHANGED, save, shortName);
  }
  
  public final String getName()
  {
    return fullClassName;
  }
  
  
  public final String getShortName()
  {
    return ClassUtil.getShortClassName(fullClassName);
  }

  
  @Override
  public String getExtendedDescriptionLabel()
  {
    return getShortName();
  }

  public String getError()
  {
    return null;
  }
  
  public String getWarning()
  {
    return null;
  }
  
  public String getInfo()
  {
    return null;
  }
  
  public boolean isInUse()
  {
    return true;
  }
  
  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }

  @Override
  public ObjectModel getParent()
  {
    return getJacobModel().getApplicationModel();
  }
}
