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
 * Created on 04.07.2005
 *
 */
package de.tif.jacob.designer.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import de.tif.jacob.designer.util.SortedProperties;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

/**
 *
 */
public class I18NResourceModel extends ObjectModel
{
  private final Locale locale;
  private SortedProperties   data = new SortedProperties();
  private List keys;
  
  public I18NResourceModel(JacobModel jacobModel, IProject project ,Locale locale ) throws Exception
  {
    super(jacobModel);
    this.locale= locale;
    reload(project);
  }
  
  public String getImageBaseName()
  {
    return super.getImageBaseName()+"_"+getName();
  }
  
  
  /**
   * Refreshes bundle data by parsing editor content.
   */
  public void reload(IProject project ) throws Exception
  {
    IFile file = project.getFile(getFileName());
    
    if(!file.exists())
      file.create(new StringBufferInputStream("#empty"), false, null);
    
    if (!file.isSynchronized(IResource.DEPTH_ONE))
    {
      file.refreshLocal(IResource.DEPTH_ONE, null);
    }
    InputStream in = file.getContents();
    StringTokenizer tokenizer = new StringTokenizer(IOUtils.toString(in), "\n\r");
    in.close();
    SortedProperties newData = new SortedProperties();
    newData.load(file.getContents());
    data = newData;
  }


  public String getLabel()
  {
   return locale==null?"Default":locale.getDisplayLanguage(Locale.ENGLISH); 
  }

  
  @Override
  public String getExtendedDescriptionLabel()
  {
    if(getJacobModel().getTestResourcebundle()==this)
      return ">"+getName();
    return getName();
  }

  public String getName()
  {
   return locale==null?"Default":locale.toString(); 
  }  
  
  /**
   * Gets the "locale" attribute.
   * 
   * @return Returns the locale.
   */
  public Locale getLocale()
  {
    return locale;
  }

  public void removeElement(String key)
  {
    String save = getValue(key);
    data.remove(key);
    keys=null;
    firePropertyChange(ObjectModel.PROPERTY_I18NVALUE_CHANGED, save, null);
  }
 
  public String getValue(String key)
  {
    if(key==null)
      return null;
    
    return data.getProperty(key);
  }
  
  public void setValue(String key, String value)
  {
    setValue(key,value,true);
  }
  
  protected void setValue(String key, String value, boolean fireEvent)
  {
    if(key==null)
      return;
    
    String save = getValue(key);
    if(save!=null && save.equals(value))
      return;
    
    keys=null;

    if(value==null)
      data.setProperty(key,"");
    else
      data.setProperty(key,value);
    firePropertyChange(ObjectModel.PROPERTY_I18NVALUE_CHANGED, save, value);
    if(fireEvent)
      getJacobModel().firePropertyChange(PROPERTY_I18NBUNDLE_CHANGED,null,this);
  }
  
  /**
   * Gets sorted resource bundle keys for this bundle.
   * 
   * @param resource
   *          bundle keys
   */
  public List getKeys()
  {
    if(keys==null)
    {
      keys = new ArrayList();
      keys.addAll(data.keySet());
      Collections.sort(keys);
    }
    return keys;
  }

  public String getError()
  {
    I18NResourceModel parentModel = (I18NResourceModel)getJacobModel().getI18NResourceModels().get(0);
    if(parentModel.getKeys().size()>getKeys().size())
      return "Missing resource entries in localization file ["+getFileName()+"]";
    
    return null;
  }
  
  public String getInfo()
  {
    if(getJacobModel().getTestResourcebundle()==this)
      return "Current test resourcebundle";
    return null;
  }
  
  public String getWarning()
  {
    Iterator iter= data.entrySet().iterator();
    while(iter.hasNext())
    {
      Map.Entry entry = (Map.Entry)iter.next();
      if(entry.getValue()==null || ((String)entry.getValue()).length()<1)
        return "Empty resource entry ["+entry.getKey()+"] in file ["+getFileName()+"]";
    }
    return null;
  }
  
  public boolean isInUse()
  {
    return true;
  }
  
  public void save(IProject project) throws Exception
  {
//		IResource resourceFile = project.findMember(getFileName());
//    if(resourceFile==null)
//      return;
    IFile file = project.getFile(getFileName());
    
    if(file.exists()==false)
    {
      // Falls es noch keine I18N Datei gab, dann wird diese jetzt angelegt
      //
//      FileUtil.createFolder(project.getFolder(resourceFile.getProjectRelativePath().removeLastSegments(1)));
      file.create(new StringBufferInputStream("#empty"), false, null);
    }
    
    if (!file.isSynchronized(IResource.DEPTH_ONE))
    {
      file.refreshLocal(IResource.DEPTH_ONE, null);
    }
 
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    data.store(out,null);
    file.setContents(new ByteArrayInputStream(out.toByteArray()),true,true,null);
  }
  
  protected void renameI18NKey(String from, String to)
  {
    String save = data.getProperty(from);
    if(save==null)
      return;
    keys=null;
    data.remove(from);
    data.setProperty(to,save);
  }

  private String getFileName()
  {
    String localeString = locale==null?"":"_"+locale.toString();
    return JacobModel.I18N_PATH+JacobModel.I18N_BUNDLE+localeString+".properties";
  }
  
  protected SortedProperties getData()
  {
    return data;
  }
  
  protected void setData(SortedProperties data)
  {
    this.data = data;
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
