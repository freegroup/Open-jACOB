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
 * Created on Oct 19, 2004
 *
 */
package de.tif.jacob.designer.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.commons.collections.ListUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomain;
import de.tif.jacob.core.definition.impl.jad.castor.CastorExternalForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelation;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelationset;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRole;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTable;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.Diagrams;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;
import de.tif.jacob.core.definition.impl.jad.castor.Roles;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.exception.DesignerException;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.designer.util.FileUtil;
import de.tif.jacob.designer.util.SortedProperties;
import de.tif.jacob.designer.util.Trace;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.SchedulerTaskUser;
import de.tif.jacob.soap.SOAPEntryPoint;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class JacobModel extends ObjectModel implements PropertyChangeListener
{
  public static final String STATIC_IMAGE_PATH ="/web/";

  public static final String I18N_PATH   = "java/jacob/resources/i18n/";
  public static final String I18N_BUNDLE = "applicationLabel";
  
  ImageRegistry registry;

  final Jacob jacob;
  private ApplicationModel applicationModel;
  
  private final List<UIJacobFormModel> jacobForms   = new ArrayList<UIJacobFormModel>();
  private final List<UIExternalFormModel> externalForms  = new ArrayList<UIExternalFormModel>();
  private final List<UIMutableFormModel> owndrawForms  = new ArrayList<UIMutableFormModel>();
  private final List<UIHtmlFormModel> htmlForms  = new ArrayList<UIHtmlFormModel>();
  
  private final List<BrowserModel> browsers        = new ArrayList<BrowserModel>();
  private final Map<String, BrowserModel>  browsersCache = new HashMap<String, BrowserModel>(); // only for performance reason

  private final List<UIDomainModel> domains       = new ArrayList<UIDomainModel>();
  
  private final List<TableAliasModel> aliases       = new ArrayList<TableAliasModel>();
  private final Map<String, TableAliasModel>  aliasesCache  = new HashMap<String, TableAliasModel>(); // only for performance reason
  
  private final List<PhysicalDataModel> datasources   = new ArrayList<PhysicalDataModel>();
  
  private final List<TableModel> tables        = new ArrayList<TableModel>();
  private final Map<String, TableModel>  tablesCache   = new HashMap<String, TableModel>(); // only for performance reason

  private final List<RelationsetModel> relationsets  = new ArrayList<RelationsetModel>();
  private final List<RelationModel> relations     = new ArrayList<RelationModel>();
  private final List<ScheduledJobSystemModel> systemJobs    = new ArrayList<ScheduledJobSystemModel>();
  private final List<ScheduledJobUserModel> userJobs      = new ArrayList<ScheduledJobUserModel>();
  private final List<ObjectModel> userRoles     = new ArrayList<ObjectModel>();
  private       List<ObjectModel> entryPointSOAP= new ArrayList<ObjectModel>(); // liste wird eventuell neu geladen
  private final List<EntryPointCMDModel> entryPointCMD = new ArrayList<EntryPointCMDModel>();
  private final List<EntryPointGUIModel> entryPointGUI = new ArrayList<EntryPointGUIModel>();
  private final List<ActivityDiagramModel> activityDiagrams = new ArrayList<ActivityDiagramModel>();
  private       List<I18NResourceModel> i18ns         = new ArrayList<I18NResourceModel>();
  private final PhysicalDataModels   datasourcesModel;
  
  private RelationsetModel  testRelationset;
  private I18NResourceModel testResourcebundle;
  private boolean isDirty;
  private final I18NPreferences i18nPreferences;
  /**
   * 
   */
  public JacobModel(IProject project, Reader reader)throws Exception
  {
    i18nPreferences = new I18NPreferences(project);
    
    registry = new ImageRegistry(JacobDesigner.getPlugin().getWorkbench().getDisplay());
    Trace.start("creating JacobModel");
    datasourcesModel = new PhysicalDataModels(this);
    Trace.mark();
    jacob = (Jacob) Jacob.unmarshalJacob(reader);
    Trace.print("unmarschal XML model");
    
    // determine all datasources in the jacob definition
    if(jacob.getRoles()==null)
      jacob.setRoles(new Roles());
    for(int i=0;i<jacob.getRoles().getRoleCount();i++)
    {
        CastorRole role =jacob.getRoles().getRole(i);
        userRoles.add(new UserRoleModel(this, role));
    }
    Trace.print("creating user roles");

    // determine all datasources in the jacob definition
    for(int i=0;i<jacob.getDataSources().getDataSourceCount();i++)
    {
        CastorDataSource source =jacob.getDataSources().getDataSource(i);
        datasources.add(new PhysicalDataModel(this, source));
    }
    Trace.print("creating physical data sources");
    
    // determine all tables in the jacob definition
    for(int i=0;i<jacob.getTables().getTableCount();i++)
    {
        CastorTable table =jacob.getTables().getTable(i);
        // integrity check
        if(getDatasourceModel(table.getDatasource())==null)
          throw new DesignerException("Unknown datasource ["+table.getDatasource()+"] in table ["+table.getName()+"]");
        
        TableModel tableModel =  new TableModel(this, table);
        tables.add(tableModel);
        tablesCache.put(tableModel.getName(),tableModel);
    }
    Trace.print("creating physical tables");

    // determine all tableAlias in the jacob definition
    for(int i=0;i<jacob.getTableAliases().getTableAliasCount();i++)
    {
        CastorTableAlias alias =jacob.getTableAliases().getTableAlias(i);
        TableAliasModel aliasModel = new TableAliasModel(this, alias);
        aliases.add(aliasModel);
        aliasesCache.put(aliasModel.getName(),aliasModel);
    }
    Trace.print("creating tables aliases");

//    defaultRelationset = new RelationsetModel(this, "default");
//    relationsets.add(defaultRelationset);

    // determine all relations in the jacob definition
    for(int i=0;i<jacob.getRelations().getRelationCount();i++)
    {
        CastorRelation relation =jacob.getRelations().getRelation(i);
        // IBIS: M:N Relationen fehlen noch
        if (relation.getCastorRelationChoice().getOneToMany()!=null)
        {
          TableAliasModel fromTable = getTableAliasModel(relation.getCastorRelationChoice().getOneToMany().getFromAlias());
          TableAliasModel toTable   = getTableAliasModel(relation.getCastorRelationChoice().getOneToMany().getToAlias());
          RelationModel rel =  new RelationModel(this, relation, fromTable, toTable);
//          defaultRelationset.addElement(rel);
          relations.add(rel);
        }
    }
    Trace.print("creating relations");

    // determine all relationsets in the jacob definition
    // default Relationset ist impliziet immer vorhanden und wird nicht ins castor eingetragen
    //
    for(int i=0;i<jacob.getRelationsets().getRelationsetCount();i++)
    {
        CastorRelationset relationset =jacob.getRelationsets().getRelationset(i);
        relationsets.add( new RelationsetModel(this, relationset));
    }
    Trace.print("creating relationsets");

    // determine all browsers in the jacob definition
    for(int i=0;i<jacob.getBrowsers().getBrowserCount();i++)
    {
        CastorBrowser browser =jacob.getBrowsers().getBrowser(i);
        BrowserModel browserModel =  new BrowserModel(this, browser);
        browsersCache.put(browserModel.getName(),browserModel);
        browsers.add( browserModel);
    }
    Trace.print("browsers created");


    // determine all jacobForms in the jacob definition
    for(int i=0;i<jacob.getForms().getFormCount();i++)
    {
        CastorJacobForm form =jacob.getForms().getForm(i);
        jacobForms.add( new UIJacobFormModel(this, form));
    }
    Trace.print("jacobForms created");

    // determine all external Forms in the jacob definition
    for(int i=0;i<jacob.getForms().getExternalFormCount();i++)
    {
        CastorExternalForm form =jacob.getForms().getExternalForm(i);
        externalForms.add( new UIExternalFormModel(this, form));
    }
    Trace.print("externalForms created");

    // determine all external Forms in the jacob definition
    for(int i=0;i<jacob.getForms().getMutableFormCount();i++)
    {
        CastorMutableForm form =jacob.getForms().getMutableForm(i);
        owndrawForms.add( new UIMutableFormModel(this, form));
    }
    Trace.print("owndrawForms created");

    // determine all html Forms in the jacob definition
    for(int i=0;i<jacob.getForms().getHtmlFormCount();i++)
    {
        CastorHtmlForm form =jacob.getForms().getHtmlForm(i);
        htmlForms.add( new UIHtmlFormModel(this, form));
    }
    Trace.print("htmlForms created");

    // determine all domains in the jacob definition
    for(int i=0;i<jacob.getDomains().getDomainCount();i++)
    {
        CastorDomain domain =jacob.getDomains().getDomain(i);
        domains.add( new UIDomainModel(this, domain));
    }
    Trace.print("creatings domains");
    
    // get all activity diagrams
    if(jacob.getDiagrams()!=null)
    {
      for(int i=0;i<jacob.getDiagrams().getActivityDiagramCount();i++)
      {
          CastorActivityDiagram diagram =jacob.getDiagrams().getActivityDiagram(i);
          activityDiagrams.add( new ActivityDiagramModel(this, diagram));
      }
    }
    Trace.print("creatings activity diagrams");

    // determine the default application
    for(int i=0;i<jacob.getApplications().getApplicationCount();i++)
    {
      if(jacob.getDefaultApplication().equals(jacob.getApplications().getApplication(i).getName()))
      {
        applicationModel= new ApplicationModel(this,jacob.getApplications().getApplication(i));
        break;
      }
    }
    Trace.print("creating application models");
    
    // try to load the i18n resource file
    //
    reloadI18N(project);
    Trace.print("loading I18N files");
    
    try
    {
      Trace.mark();
      IJavaProject myJavaProject = JavaCore.create(project);
      Trace.print("create Java project from eclipse");
      
      IType[] types = ClassFinder.getAllSubtypes(SchedulerTaskSystem.class.getName(),myJavaProject,null);
      for (int i = 0; i < types.length; i++)
      {
        IType type = types[i];
        if(type.getFullyQualifiedName().startsWith(ScheduledJobSystemModel.PACKAGE))
          systemJobs.add(new ScheduledJobSystemModel(this,type.getElementName()));
      }
      Trace.print("creating ScheduledJobSystemModel");
      
      types = ClassFinder.getAllSubtypes(SchedulerTaskUser.class.getName(),myJavaProject,null);
      for (int i = 0; i < types.length; i++)
      {
        IType type = types[i];
        if(type.getFullyQualifiedName().startsWith(ScheduledJobUserModel.PACKAGE))
          userJobs.add(new ScheduledJobUserModel(this,type.getElementName()));
      }
      Trace.print("creating ScheduledJobUserModel");
      
      reloadSOAP(project);
      
      // CMD Entry Points
      //
      Trace.mark();
      types = ClassFinder.getAllSubtypes(ICmdEntryPoint.class.getName(),myJavaProject,null);
      for (int i = 0; i < types.length; i++)
      {
        IType type = types[i];
        if(type.getFullyQualifiedName().startsWith(EntryPointCMDModel.PACKAGE))
          entryPointCMD.add(new EntryPointCMDModel(this,type.getElementName()));
      }
      Trace.print("creating EntryPointCMDModel");
      
      // GUI Entry Points
      //
      types = ClassFinder.getAllSubtypes(IGuiEntryPoint.class.getName(),myJavaProject,null);
      for (int i = 0; i < types.length; i++)
      {
        IType type = types[i];
        if(type.getFullyQualifiedName().startsWith(EntryPointGUIModel.PACKAGE))
          entryPointGUI.add(new EntryPointGUIModel(this,type.getElementName()));
      }
      Trace.print("creating EntryPointGUIModel");
    }
    catch (Exception e)
    {
      //ignore. application.jad is not part of an java project.
    }
    Trace.stop("creating JacobModel");
  }
  
  
  public void reloadSOAP()
  {
    try
    {
      reloadSOAP(JacobDesigner.getPlugin().getSelectedProject());
    }
    catch(Exception exc)
    {
      JacobDesigner.showException(exc);
    }
  }
  
  public void reloadSOAP(IProject project) throws Exception
  {
    Trace.start("reloadSOAP classes from project");
    List<ObjectModel> added = new ArrayList<ObjectModel>();
    IJavaProject myJavaProject = JavaCore.create(project);
    // SOAP Entry points
    //
    IType[] types = ClassFinder.getAllSubtypes(SOAPEntryPoint.class.getName(),myJavaProject,null);
    for (int i = 0; i < types.length; i++)
    {
      IType type = types[i];
      String shortName = type.getElementName();
      // Die Klasse muss in dem geforderten Package liegen.
      if(type.getFullyQualifiedName().startsWith(EntryPointSOAPModel.PACKAGE))
      {
        EntryPointSOAPModel model = new EntryPointSOAPModel(this,shortName);
        addElement(model);
        added.add(getEntryPointSOAPModel(shortName));
      }
    }
    // Alte SOAP Resourcen welche nicht mehr im Filesystem gefunden wurden
    // lï¿½schen
    List toRemove = ListUtils.subtract(entryPointSOAP,added);
    Iterator iter = toRemove.iterator();
    while (iter.hasNext())
    {
      EntryPointSOAPModel model = (EntryPointSOAPModel) iter.next();
      removeElement(model);
    }
    Trace.stop("reloadSOAP classes from project");
  }
  
  public void reloadI18N()
  {
     reloadI18N(JacobDesigner.getPlugin().getSelectedProject());
  }
  
  public void reloadI18N(IProject project)
  {
    List<ObjectModel> added = new ArrayList<ObjectModel>();
    try
    {
      try
      {
        project.refreshLocal(IResource.DEPTH_INFINITE,null);
      }
      catch (CoreException e1)
      {
        // ignore. Es kann sein, dass wenn man im project eine Datei löscht, dass man
        // das Project dann nicht refreshen kann (The resource tree is locked for modifications.)
        // Man kann dies besonders behandeln oder einfach ignorieren.
      }
      catch(org.eclipse.core.runtime.AssertionFailedException as )
      {
        // kann passieren, wenn der workspace bereits refreshed wird.
        // (The workspace tree is already locked)
      }
      catch(org.eclipse.core.runtime.OperationCanceledException as )
      {
        // cancel einer operation durch den Anwender
      }
      
      IResource resourceFile = project.findMember(I18N_PATH+I18N_BUNDLE+".properties");
      // Falls kein I18N File gefunden werden kann, wird dies einfach kurzerhand angelegt
      //
      if(resourceFile==null)
      {
        IJavaProject myJavaProject = JavaCore.create(project);
        
        IPackageFragmentRoot[] roots = myJavaProject.getAllPackageFragmentRoots();
        IPath path = roots[0].getPath();
        IFolder srcFolder = project.getFolder(path.lastSegment());
        FileUtil.createFolder(project.getFolder(srcFolder.getName() + "/jacob/resources/i18n" ));
        IFile newFile = srcFolder.getFile( "/jacob/resources/i18n/"+I18N_BUNDLE+".properties");
        newFile.create(new StringBufferInputStream(""), false, null);
        resourceFile = srcFolder.findMember("/jacob/resources/i18n/"+I18N_BUNDLE+".properties");
      }
      IFile file = project.getFile(resourceFile.getProjectRelativePath());
      
      if(file==null)
        return;
      
      // Alle ähnlichen resourcen laden
      //
      IResource[] resources = file.getParent().members();
      for (int i = 0; i < resources.length; i++)
      {
        IResource resource = resources[i];
        String regex = "^(" + I18N_BUNDLE + ")" + "((_[a-z]{2,3})|(_[a-z]{2,3}_[A-Z]{2})" + "|(_[a-z]{2,3}_[A-Z]{2}_\\w*))?(\\." + file.getFileExtension() + ")$";
        String resourceName = resource.getName();
        if (resource instanceof IFile && resourceName.matches(regex))
        {
          String localeText = resourceName.replaceFirst(regex, "$2");
          StringTokenizer tokens = new StringTokenizer(localeText, "_");
          List<String> localeSections = new ArrayList<String>();
          Locale locale = null;
          while (tokens.hasMoreTokens())
            localeSections.add(tokens.nextToken());
          switch (localeSections.size())
          {
          case 1:
            locale = new Locale(localeSections.get(0));
            break;
          case 2:
            locale = new Locale( localeSections.get(0), localeSections.get(1));
            break;
          case 3:
            locale = new Locale(localeSections.get(0), localeSections.get(1), localeSections.get(2));
            break;
          default:
            break;
          }
          
          I18NResourceModel element = new I18NResourceModel(this,project, locale);
          // Beim addElement(...) ist nicht gesagt, dass dass element auch wirklich eingefï¿½gt wird. Es wird eventuell ein bestehendes
          // ResourceModel aktualisiert. Dies ist wichtig, da andere Elemente eine Referenz auf das Element halten kï¿½nnen.
          addElement(element);
          added.add(getI18NResourceModel(element.getLocale()));
        }
      }
      // Alte I18N Resourcen welche nicht mehr im Filesystem gefunden wurden
      // löschen
      List toRemove = ListUtils.subtract(i18ns,added);
      Iterator iter = toRemove.iterator();
      while (iter.hasNext())
      {
        I18NResourceModel model = (I18NResourceModel) iter.next();
        removeElement(model);
      }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
    // Jedes resource file muss die keys des default resource file enthalten!!!
    // (im Editor wird davon ausgegangen, dass dies so ist. Es vereinfacht einfach
    // ein paar Gültigkeitsabfragen
    //
    Iterator iter = getI18NResourceModels().iterator();
    I18NResourceModel defaultModel = (I18NResourceModel)iter.next();
    while (iter.hasNext())
    {
      I18NResourceModel model = (I18NResourceModel) iter.next();
      Iterator keyIter = defaultModel.getKeys().iterator();
      while (keyIter.hasNext())
      {
        String key = (String) keyIter.next();
        model.setValue(key,StringUtil.toSaveString(model.getValue(key)),false);
      }
    }
  }
  
  /**
   * 
   * @return
   */
  public ApplicationModel getApplicationModel()
  {
    return applicationModel;
  }
  
  /**
   * 
   * @return List[String]
   */
  public List<String> getTableAliasNames()
  {
    List<String> names=new ArrayList<String>(aliasesCache.keySet());
    /*
    for(Iterator iter=aliases.iterator();iter.hasNext();)
    {
      names.add(((TableAliasModel)iter.next()).getName());
    }
    */
    Collections.sort(names);
    return names;
  }

  
  /**
   * 
   * @return List[String]
   */
  public List<String> getRelationsetNames()
  {
    List<String> names=new ArrayList<String>();
    int i=0;
    for(Iterator<RelationsetModel> iter=relationsets.iterator();iter.hasNext();i++)
    {
      names.add(iter.next().getName());
    }
    Collections.sort(names);
    // add the two implicit relation sets
    //
    names.add(0,RelationsetModel.RELATIONSET_DEFAULT);
    names.add(0,RelationsetModel.RELATIONSET_LOCAL);
    return names;
  }

  /**
   * 
   * @return List[RelationsetModel]
   */
  public List<RelationsetModel> getRelationsetModels()
  {
    return relationsets;
  }


  /**
   * 
   * @return RelationsetModel
   */
  public RelationsetModel getRelationsetModel(String name)
  {
    if(name==null || name.length()==0)
      return null;
    for(Iterator<RelationsetModel> iter=relationsets.iterator();iter.hasNext();)
    {
      RelationsetModel relationset = iter.next();
      if(relationset.getName().equals(name))
        return relationset;
    }
    return null;
  }

  /**
   * 
   * @return List[RelationModel]
   */
  public List<RelationModel> getRelationModels()
  {
    return relations;
  }

  /**
   * 
   * @return List[ScheduledJobSystemModel]
   */
  public List<ScheduledJobSystemModel> getScheduledJobSystemModels()
  {
    return new ArrayList<ScheduledJobSystemModel>(systemJobs);
  }

  public ScheduledJobSystemModel getScheduledJobSystemMode(String shortName)
  {
    Iterator<ScheduledJobSystemModel> iter=systemJobs.iterator();
    while (iter.hasNext()) 
    {
      ScheduledJobSystemModel job = iter.next();
      if(job.getShortName().equals(shortName))
        return job;
    }
    return null;
  }

  /**
   * 
   * @return List[ScheduledJobUserModel]
   */
  public List<ScheduledJobUserModel> getScheduledJobUserModels()
  {
    return new ArrayList<ScheduledJobUserModel>(userJobs);
  }

  public ScheduledJobUserModel getScheduledJobUserMode(String shortName)
  {
    Iterator<ScheduledJobUserModel> iter=userJobs.iterator();
    while (iter.hasNext()) 
    {
      ScheduledJobUserModel job = iter.next();
      if(job.getShortName().equals(shortName))
        return job;
    }
    return null;
  }

  /**
   * 
   * @return List[EntryPointSOAPModel]
   */
  public List<ObjectModel> getEntryPointSOAPModels()
  {
    return new ArrayList<ObjectModel>(entryPointSOAP);
  }

  public EntryPointSOAPModel getEntryPointSOAPModel(String shortName)
  {
    Iterator<ObjectModel> iter=entryPointSOAP.iterator();
    while (iter.hasNext()) 
    {
      EntryPointSOAPModel entry= (EntryPointSOAPModel) iter.next();
      if(entry.getShortName().equals(shortName))
        return entry;
    }
    return null;
  }

  /**
   * 
   * @return List[EntryPointCMDModel]
   */
  public List<EntryPointCMDModel> getEntryPointCMDModels()
  {
    return new ArrayList<EntryPointCMDModel>(entryPointCMD);
  }

  public EntryPointCMDModel getEntryPointCMDModel(String shortName)
  {
    Iterator<EntryPointCMDModel> iter=entryPointCMD.iterator();
    while (iter.hasNext()) 
    {
      EntryPointCMDModel entry= iter.next();
      if(entry.getShortName().equals(shortName))
        return entry;
    }
    return null;
  }

  /**
   * 
   * @return List[EntryPointGUIModel]
   */
  public List<EntryPointGUIModel> getEntryPointGUIModels()
  {
    return new ArrayList<EntryPointGUIModel>(entryPointGUI);
  }

  public EntryPointGUIModel getEntryPointGUIModel(String shortName)
  {
    Iterator<EntryPointGUIModel> iter=entryPointGUI.iterator();
    while (iter.hasNext()) 
    {
      EntryPointGUIModel entry= iter.next();
      if(entry.getShortName().equals(shortName))
        return entry;
    }
    return null;
  }

  /**
   * 
   * @return List[RelationModel]
   */
  public List<RelationModel> getRelationModelsFrom(TableAliasModel fromTable)
  {
    List<RelationModel> result=new ArrayList<RelationModel>(relations.size());
    for (RelationModel  relation: relations)
    {
      if(relation.getFromTableAlias()==fromTable)
        result.add(relation);
    }
    return result;
  }
  
  /**
   * 
   * @return List[RelationModel]
   */
  public List<RelationModel> getRelationModelsTo(TableAliasModel toTable)
  {
    List<RelationModel> result=new ArrayList<RelationModel>(relations.size());
    for (RelationModel  relation: relations)
    {
      if(relation.getToTableAlias()==toTable)
        result.add(relation);
    }
    return result;
  }

  /**
   * 
   * @return List[RelationModel]
   */
  public RelationModel getRelationModel(String name)
  {
    for (RelationModel  relation: relations)
    {
      if(relation.getName().equals(name))
        return relation;
    }
    return null;
  }
  
  /**
   * 
   * @return RelationModel
   */
  public RelationModel getRelationModel(TableAliasModel fromTable, TableAliasModel toTable)
  {
    for (RelationModel  relation: relations)
    {
      if(relation.getFromTableAlias()==fromTable && relation.getToTableAlias()==toTable)
        return relation;
    }
    return null;
  }

  
  /**
   * 
   * @return List[TableAliasModel]
   */
  public List<TableAliasModel> getTableAliasModels()
  {
    return aliases;
  }


  /**
   * 
   * @param name
   * @return
   */
  public TableAliasModel getTableAliasModel(String name)
  {
    return aliasesCache.get(name);
  }

  
  /**
   * 
   * @param name
   * @return List[TableAliasModel]
   */
  public List<TableAliasModel> getTableAliasModels(TableModel table)
  {
    List<TableAliasModel> result = new ArrayList<TableAliasModel>();
    for (TableAliasModel  testAlias: aliases)
    {
      if(testAlias.getTableModel()==table)
        result.add(testAlias);
    }
    return result;
  }
  
  /**
   * 
   * @param name
   * @return List[String]
   */
  public List<String> getCorrespondingTableAliasNames(TableAliasModel alias)
  {
    List<String> result = new ArrayList<String>();
    for (TableAliasModel  testAlias: aliases)
    {
      if(testAlias.getTableModel()==alias.getTableModel())
        result.add(testAlias.getName());
    }
    return result;
  }

  /**
   * 
   * @return List[TableModel]
   */
  public List<TableModel> getTableModels()
  {
    return tables;
  }

  /**
   * 
   * @param name
   * @return
   */
  public TableModel getTableModel(String name)
  {
    return tablesCache.get(name);
    /*
    Iterator iter=tables.iterator();
    while (iter.hasNext()) 
    {
      TableModel table = (TableModel) iter.next();
      if(table.getName().equals(name))
        return table;
    }
    return null;
    */
  }

  /**
   * 
   * @param name
   * @return List[DomainModels]
   */
  public List<UIDomainModel> getDomainModels()
  {
    return ListUtils.unmodifiableList(domains);
  }
 
  /**
   * 
   * @param name
   * @return
   */
  public UIDomainModel getDomainModel(String name)
  {
    for (UIDomainModel domain : domains)
    {
      if(domain.getName().equals(name))
        return domain;
    }
    return null;
  }
  
  public void addElement(UIDomainModel domain)
  {
    domains.add(domain);
    // add the domain to the common jacob definition
    //
    jacob.getDomains().addDomain(domain.getCastor());
   }
  
  public void removeElement(UIDomainModel domain)
  {
    if(domains.remove(domain))
    {
      for(int i=0; i<jacob.getDomains().getDomainCount();i++)
      {
        CastorDomain cDomain =jacob.getDomains().getDomain(i);
        if(cDomain.getName().equals(domain.getName()))
        {
          jacob.getDomains().removeDomain(i);
          break;
        }
      }
      firePropertyChange(PROPERTY_DOMAIN_DELETED, domain,null);
    }
  }


  public void addElement(UserRoleModel role)
  {
    if(!userRoles.contains(role))
    {
      userRoles.add(role);
      jacob.getRoles().addRole(role.getCastor());
      firePropertyChange(PROPERTY_USERROLE_CREATED,null, role);
    }
  }

  /**
   * Beim addElement(...) ist nicht gesagt, dass dass Element auch wirklich eingefï¿½gt wird. Es wird eventuell ein bestehendes
   * ResourceModel aktualisiert. Dies ist wichtig, da andere Elemente eine Referenz auf das Element halten kï¿½nnen.
   * Diese Elemente mï¿½ssen sich darauf verlassen, dass sich fï¿½r ein 'Locale' immer das selbe 'I18NResourceModel' Objekt
   * zurï¿½ckgeliefert bekommen!!
   * 
   * @param newModel
   */
  public void addElement(I18NResourceModel newModel)
  {
    for(int i=0;i<i18ns.size();i++)
    {
      I18NResourceModel model = (I18NResourceModel)i18ns.get(i);
      if(model.getLocale()==null)
      {
        if(newModel.getLocale()==null)
        {
          model.setData(newModel.getData());
          firePropertyChange(PROPERTY_I18NBUNDLE_CHANGED,null,model);
          return;
        }
      }
      else if(model.getLocale().equals(newModel.getLocale()))
      {
        model.setData(newModel.getData());
        firePropertyChange(PROPERTY_I18NBUNDLE_CHANGED,null,model);
        return;
      }
    }
    i18ns.add(newModel);
    firePropertyChange(PROPERTY_I18NBUNDLE_CREATED,null, newModel);
  }

  public void removeElement(I18NResourceModel model)
  {
    if(i18ns.remove(model))
    {
      firePropertyChange(PROPERTY_I18NBUNDLE_DELETED, model,null);
    }
  }

  public void addI18N(String key, String value)
  {
    addI18N(key,value,true);
  }
  
  protected void addI18N(String key, String value, boolean fireEvent)
  {
    if(key==null || key.length()==0)
      return;

    if(hasI18NKey(key))
      return;
    
    for (I18NResourceModel model : i18ns)
      model.setValue(key,value,false);

    if(fireEvent==true)
      firePropertyChange(PROPERTY_I18NKEY_CREATED, null, key);
  }

  
  public void removeElement(UserRoleModel role)
  {
    int index = userRoles.indexOf(role);
    if(index!=-1)
    {
      userRoles.remove(role);
      jacob.getRoles().removeRole(index);
      firePropertyChange(PROPERTY_ALIAS_DELETED,role,null);
    }
  }


  public void removeElement(PhysicalDataModel model)
  {
    int index = datasources.indexOf(model);
    if(index>=0)
    {
      datasources.remove(model);
      jacob.getDataSources().removeDataSource(index);
      firePropertyChange(PROPERTY_DATASOURCE_DELETED, model,null);
    }
  }
  
  public void removeElement(ActivityDiagramModel model)
  {
    if(activityDiagrams.remove(model))
    {
      for(int i=0; i<jacob.getDiagrams().getActivityDiagramCount();i++)
      {
        CastorActivityDiagram diagram =jacob.getDiagrams().getActivityDiagram(i);
        if(diagram.getName().equals(model.getName()))
        {
          jacob.getDiagrams().removeActivityDiagram(i);
          break;
        }
      }
      firePropertyChange(PROPERTY_ACTIVITYDIAGRAM_DELETED, model,null);
    }
  }
  
  public void removeElement(RelationModel relation)
  {
    if(relations.remove(relation))
    {
      for(int i=0; i<jacob.getRelations().getRelationCount();i++)
      {
        CastorRelation cRelation =jacob.getRelations().getRelation(i);
        if(cRelation.getName().equals(relation.getName()))
        {
          jacob.getRelations().removeRelation(i);
          break;
        }
      }
      firePropertyChange(PROPERTY_RELATION_DELETED, relation,null);
      // Der key wird jetzt eventuell in keiner Relation mehr verwendet. Der Error status des Elementes 
      // kann sich dadurch ï¿½ndern -> ApplicationOutline muss dies mitbekommen.
      //
      relation.getToKey().firePropertyChange(PROPERTY_KEY_CHANGED,null,relation.getToKey());
    }
  }
  
  public void removeElement(RelationsetModel relationset)
  {
    // Falls die zugehörigen Relationen nicht mehr benötigt werden, dann werden diese
    // gleich mit gelöscht
    //
    for(RelationModel relation: relationset.getRelationModels())
    {
      relationset.removeElement(relation);
      if(!relation.isInUse())
      {
        removeElement(relation);
      }
    }
    

    if(relationsets.remove(relationset))
    {
      for(int i=0; i<jacob.getRelationsets().getRelationsetCount();i++)
      {
        CastorRelationset cRelationset =jacob.getRelationsets().getRelationset(i);
        if(cRelationset.getName().equals(relationset.getName()))
        {
          jacob.getRelationsets().removeRelationset(i);
          break;
        }
      }
      firePropertyChange(PROPERTY_RELATIONSET_DELETED, relationset,null);
    }
  }
  
  public void removeElement(ScheduledJobSystemModel model)
  {
    if(this.systemJobs.remove(model))
      firePropertyChange(PROPERTY_SYSTEMJOB_DELETED, model, null);
  }

  public void removeElement(ScheduledJobUserModel model)
  {
    if(this.userJobs.remove(model))
      firePropertyChange(PROPERTY_USERJOB_DELETED, model, null);
  }

  public void removeElement(EntryPointSOAPModel model)
  {
    if(entryPointSOAP.remove(model))
      firePropertyChange(PROPERTY_SOAPENTRY_DELETED, model, null);
  }
 
  
  public void removeElement(EntryPointCMDModel model)
  {
    if(entryPointCMD.remove(model))
      firePropertyChange(PROPERTY_CMDENTRY_DELETED, model, null);
  }
  
  public void removeElement(EntryPointGUIModel model)
  {
    if(entryPointGUI.remove(model))
      firePropertyChange(PROPERTY_GUIENTRY_DELETED, model, null);
  }

  public void removeI18N(String key)
  {
    for (I18NResourceModel model : i18ns)
      model.removeElement(key);

    if(isI18NKeyInUse(key))
      firePropertyChange(PROPERTY_I18NKEY_REMOVE, key,null);
  }

  public void resetI18N()
  {
    try
    {
      IRunnableWithProgress operation = new IRunnableWithProgress()
      {
        public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
            monitor.beginTask("Convert project to I18N",7);
            try
            {
              // Bestende resource bundle zurücksetzten und alte Werte merken. Gültige Übersetzungen
              // können so in das neu generierte übernommen werden.
              //
              monitor.subTask("Reset existing resource bundles...");
              Map<I18NResourceModel, SortedProperties> oldResources= new HashMap<I18NResourceModel, SortedProperties>();
              for (I18NResourceModel model : i18ns)
              {
                oldResources.put(model, model.getData());
                model.setData(new SortedProperties());
                // dies sollte das einzigste event sein
                model.firePropertyChange(PROPERTY_I18NBUNDLE_CHANGED, null, model);
              }
              monitor.worked(1);//=============================================================================
              
              // und falls nicht vohanden das default bundle anlegen
              //
              monitor.subTask("Creating default resource bundle...");
              Locale defaultLocale = Locale.getDefault();
              // Es interessiert nur das Land. Sprache und Variante ist in diesem Fall unwichtig
              defaultLocale = new Locale(defaultLocale.getLanguage());
              addElement( new I18NResourceModel(JacobModel.this,JacobDesigner.getPlugin().getSelectedProject(),defaultLocale));
              
              monitor.worked(1);//=============================================================================
              
              // in allen Tabellen ein Label anlegen
              //
              monitor.subTask("Create labels for table fields...");
              for(TableModel table : tables)
                table.resetI18N();

              monitor.worked(1);//=============================================================================
              
              monitor.subTask("Transform Domain titles...");
              for(UIDomainModel domain : domains)
                domain.resetI18N();

              monitor.worked(1);//=============================================================================

              // rename all references in groups, foreignFields, browsers...
              monitor.subTask("Transform GUI elements...");
              for(UIJacobFormModel form : getJacobFormModels())
                form.resetI18N();

              monitor.worked(1);//=============================================================================

              monitor.subTask("Transform browsers...");
              for (BrowserModel browser : browsers)
                browser.resetI18N();

              monitor.worked(1);//=============================================================================
              
              monitor.subTask("Collect generated I18N keys...");
              createMissingI18NKey();
              
              monitor.worked(1);//=============================================================================
              
              // Alte eventuell bestende Übersetzungen in das neu generierte übertragen
              for (I18NResourceModel model : i18ns)
              {
                Properties oldProp = oldResources.get(model);
                Properties newProp = model.getData();

                Iterator keyIter = newProp.keySet().iterator();
                while(keyIter.hasNext()&& oldProp!=null)
                {
                  String key   = (String)keyIter.next();
                  String value = oldProp.getProperty(key); 
                  if(value!=null)
                    newProp.setProperty(key,value);
                }
                
                // dies sollte das einzigste event sein
                model.firePropertyChange(PROPERTY_I18NBUNDLE_CHANGED, null, model);
              }
              firePropertyChange(PROPERTY_I18NBUNDLE_CHANGED, null, this);
              
              //   monitor.done();
            }
            catch(Exception e)
            {
              JacobDesigner.showException(e);
            }
        }
      };
      new ProgressMonitorDialog(null).run(false, false, operation);
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
  }
  
  public void addElement(RelationModel relation)
  {
    if(!relations.contains(relation))
    {
      relations.add(relation);
      jacob.getRelations().addRelation(relation.getCastor());
      Trace.start("JacobModel.addElement(RelationModel)");
      firePropertyChange(PROPERTY_RELATION_CREATED,null, relation);
      Trace.print("firePropertyChange(PROPERTY_RELATION_CREATED");
      // Der key wird jetzt in einer einer Relation verwendet. Der Error status des Elementes kann sich dadurch ändern
      // ApplicationOutline muss dies mitbekommen.
      //
      relation.getToKey().firePropertyChange(PROPERTY_KEY_CHANGED,null,relation.getToKey());
      Trace.print("firePropertyChange(PROPERTY_KEY_CHANGED");
      Trace.stop("JacobModel.addElement(RelationModel)");
    }
  }
  
  public void addElement(ActivityDiagramModel diagram)
  {
    if(!activityDiagrams.contains(diagram))
    {
      activityDiagrams.add(diagram);
      if(jacob.getDiagrams()==null)
        jacob.setDiagrams(new Diagrams());
      
      jacob.getDiagrams().addActivityDiagram(diagram.getCastor());
      firePropertyChange(PROPERTY_ACTIVITYDIAGRAM_CREATED,null, diagram);
    }
  }

  public void addElement(RelationsetModel relationset)
  {
    if(!relations.contains(relationset))
    {
      relationsets.add(relationset);
      jacob.getRelationsets().addRelationset(relationset.getCastor());
      firePropertyChange(PROPERTY_RELATIONSET_CREATED,null, relationset);
    }
  }
  
  public void addElement(TableModel table)
  {
    if(!tables.contains(table))
    {
      tables.add(table);
      tablesCache.put(table.getName(),table);
      jacob.getTables().addTable(table.getCastor());
      firePropertyChange(PROPERTY_TABLE_CREATED,null, table);
    }
  }
     
  public void addElement(TableAliasModel alias)
  {
    if(!aliases.contains(alias))
    {
      aliases.add(alias);
      aliasesCache.put(alias.getName(),alias);
      jacob.getTableAliases().addTableAlias(alias.getCastor());
      firePropertyChange(PROPERTY_ALIAS_CREATED,null, alias);
    }
  }
   
  public void addElement(BrowserModel browser)
  {
    if(!browsers.contains(browser))
    {
      browsers.add(browser);
      browsersCache.put(browser.getName(),browser);
      jacob.getBrowsers().addBrowser(browser.getCastor());
      firePropertyChange(PROPERTY_BROWSER_CREATED,null, browser);
      browser.getTableAliasModel().firePropertyChange(PROPERTY_BROWSER_CREATED,null, browser);
    }
  }

  public void addElement(PhysicalDataModel model)
  {
    if(!datasources.contains(model))
    {
      datasources.add(model);
      jacob.getDataSources().addDataSource(model.getCastor());
      firePropertyChange(PROPERTY_DATASOURCE_CREATED,null, model);
    }
  }

  public void addElement(ScheduledJobSystemModel job)
  {
    if(!systemJobs.contains(job))
    {
      systemJobs.add(job);
      firePropertyChange(PROPERTY_SYSTEMJOB_CREATED,null, job);
    }
  }
  
  public void addElement(ScheduledJobUserModel job)
  {
    if(!userJobs.contains(job))
    {
      userJobs.add(job);
      firePropertyChange(PROPERTY_USERJOB_CREATED,null, job);
    }
  }
 
  
  public void addElement(EntryPointSOAPModel entry)
  {
    for(int i=0;i<entryPointSOAP.size();i++)
    {
      EntryPointSOAPModel model = (EntryPointSOAPModel)entryPointSOAP.get(i);
      if(model.getShortName().equals(entry.getShortName()))
        return;
    }
    entryPointSOAP.add(entry);
    firePropertyChange(PROPERTY_SOAPENTRY_CREATED,null, entry);
  }

  public void addElement(EntryPointCMDModel entry)
  {
    if(!entryPointCMD.contains(entry))
    {
      entryPointCMD.add(entry);
      firePropertyChange(PROPERTY_CMDENTRY_CREATED,null, entry);
    }
  }
  
  public void addElement(EntryPointGUIModel entry)
  {
    if(!entryPointGUI.contains(entry))
    {
      entryPointGUI.add(entry);
      firePropertyChange(PROPERTY_GUIENTRY_CREATED,null, entry);
    }
  }
  public void removeElement(BrowserModel browser)
  {
    int index = browsers.indexOf(browser);
    if(index!=-1)
    {
      browsers.remove(browser);
      browsersCache.remove(browser.getName());
      jacob.getBrowsers().removeBrowser(index);
      firePropertyChange(PROPERTY_BROWSER_DELETED,browser,null);
    }
  }
 
  
  public void removeElement(TableAliasModel alias)
  {
    int index = aliases.indexOf(alias);
    if(index!=-1)
    {
      aliases.remove(alias);
      aliasesCache.remove(alias.getName());
      jacob.getTableAliases().removeTableAlias(index);
      // eventuell hat sich der alias als listener eingetragen. Dies wird
      // jetzt sicherheitshalber hier ausgetragen
      //
      this.removePropertyChangeListener(alias);
      firePropertyChange(PROPERTY_ALIAS_DELETED,alias,null);
    }
  }

  
  public void removeElement(TableModel table)
  {
    int index = tables.indexOf(table);
    if(index!=-1)
    {
      tables.remove(table);
      tablesCache.remove(table.getName());
      jacob.getTables().removeTable(index);
      firePropertyChange(PROPERTY_TABLE_DELETED,table,null);
    }
  }

  
  // Den Namen einer form zu ï¿½ndern ist ein wenig 'tricky'. Alle Referenzen in den Domï¿½nen ( dies sind nur
  // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
  //
  protected void renameFormReference(String from, String to)
  {
    getApplicationModel().renameFormReference(from, to);
  }
  
  
  // Den Namen einer form zu ï¿½ndern ist ein wenig 'tricky'. Alle Referenzen in den Domï¿½nen ( dies sind nur
  // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
  //
  protected void renameDomainReference(String from, String to)
  {
    getApplicationModel().renameDomainReference(from, to);
  }

  protected void renameTableReference(String from, String to)
  {
    // change the key entry in the cache map
    //
    tablesCache.put(to, tablesCache.remove(from));
    
    for (TableAliasModel  alias: aliases)
    {
      if(alias.getCastor().getTable().equals(from))
      {
          alias.getCastor().setTable(to);
          alias.firePropertyChange(PROPERTY_TABLE_CHANGED, from, to);
      }
    }
  }
  
  protected void renameUserRole(String from, String to)
  {
    for(UIDomainModel domain : domains)
      domain.renameUserRole(from,to);
  }
  
  protected void renameAliasReference(String from, String to)
  {
    // remove the alias in the cache and add them with the new key
    //
    aliasesCache.put(to, aliasesCache.remove(from));
    
    // rename anchor table of the browsers and the foreignFields in the browsers.
    //
    for (BrowserModel browser : getBrowserModels())
      browser.renameAliasReference(from, to);
    
    // rename the toTable/fromTable in the relations
    //
    for (RelationModel  relation: relations)
      relation.renameAliasReference(from, to);
    
    // rename the alias in the different jacobForms (groups)
    //
    for (UIJacobFormModel form : getJacobFormModels())
      form.renameAliasReference(from,to);
    
    // rename the alias in the different relationssets (groups)
    // (important for the graphical layout of the TableAlias elements)
    for (RelationsetModel relationset : getRelationsetModels())
      relationset.renameAliasReference(from,to);
  }
  
  protected void renameRelationsetReference(String from, String to)
  {
    // rename anchor table of the browsers and the foreignFields in the browsers.
    //
    for (BrowserModel browser : getBrowserModels())
      browser.renameRelationsetReference(from, to);
    
    // rename the alias in the different jacobForms (groups)
    //
    for (UIJacobFormModel form : getJacobFormModels())
      form.renameRelationsetReference(from,to);
  }

  protected void renameBrowserReference(String from, String to)
  {
    // remove the browser in the cache and add them with the new key
    //
    browsersCache.put(to, browsersCache.remove(from));

    // rename the alias in the different jacobForms (groups)
    //
    for (UIJacobFormModel form : getJacobFormModels())
      form.renameBrowserReference(from,to);
    
    // rename all references in foreignFields in browsers...
    for (BrowserModel browser : getBrowserModels())
      browser.renameBrowserReference(from,to);
  }
  
  protected void renameFieldReference(FieldModel field,String from , String to)
  {
    try
    {
      // rename all references in groups, foreignFields, browsers...
      for (UIJacobFormModel form : getJacobFormModels())
        form.renameFieldReference(field, from,to);
      
      // rename all references in groups, foreignFields, browsers...
      for (BrowserModel browser : getBrowserModels())
        browser.renameFieldReference(field, from,to);
  
      // rename all references in groups, foreignFields, browsers...
      for (TableAliasModel alias : getTableAliasModels())
        alias.renameFieldReference(field, from,to);
  
      // rename the references in the KEYS  (primary, foreign, index, unique)
      //
      field.getTableModel().renameFieldReference(field, from, to);
    }
    catch(Exception exc)
    {
      JacobDesigner.showException(exc);
    }
  }
  
  
  protected void renameKeyReference(KeyModel key,String from , String to)
  {
    for (RelationModel relation : getRelationModels())
      relation.renameKeyReference(key, from,to);

    for (BrowserModel browser : getBrowserModels())
      browser.renameKeyReference(key, from,to);
  }
  
  public void renameI18NKey(String from, String to)
  {
    if(StringUtil.saveEquals(from,to))
      return;
      
    // jeden key in allen I18N files umbenennen
    //
    for (I18NResourceModel model : i18ns)
      model.renameI18NKey(from,to);
    
    // es wird das '%' jetzt vorangestellt. Ansonsten muss jedes Element in dem es unbeannt wird selbst machen
    // Dies ist somit wesentlich schneller wenn man dies nur einmal macht
    //
    from = "%"+from;
    to   = "%"+to;
    

    // in jeder db Tabelle den key mit umbenenne
    //
    for(TableModel table : tables)
      table.renameI18NKey(from,to);
    
    // jedes domain label umbenennen
    for(UIDomainModel domain : domains)
      domain.renameI18NKey(from,to);

    // rename all references in groups, foreignFields, browsers...
    for (UIJacobFormModel form : getJacobFormModels())
      form.renameI18NKey(from,to);
    
    // rename all references in groups, foreignFields, browsers...
    for (UIExternalFormModel form : getExternalFormModels())
      form.renameI18NKey(from,to);
    
    // rename all references in groups, foreignFields, browsers...
    for (UIMutableFormModel form : getOwnDrawFormModels())
      form.renameI18NKey(from,to);
    

    // rename all references in groups, foreignFields, browsers...
    for (BrowserModel browser : getBrowserModels())
      browser.renameI18NKey(from,to);

    // Dies ist im Moment eine absolute Ausnahme, dass eine 'renameXYZ()' Methode ein
    // firePropertyChange auslï¿½st.
    //
    firePropertyChange(PROPERTY_I18NKEY_CHANGED, from,to);
  }
 
  /**
   * Alle elemente fragen ob diese das "model" benötigen.
   * 
   * @param result
   * @param model
   */
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    for(TableModel table : tables)
      table.addReferrerObject(result,model);
    
    for(TableAliasModel alias : aliases)
      alias.addReferrerObject(result,model);

    for(PhysicalDataModel datamodel : datasources)
      datamodel.addReferrerObject(result,model);

    for(UIDomainModel domain : getDomainModels())
      domain.addReferrerObject(result,model);

    for(RelationsetModel relationset : getRelationsetModels())
      relationset.addReferrerObject(result,model);

    for(RelationModel relation : getRelationModels())
      relation.addReferrerObject(result,model);

    for (UIJacobFormModel form : getJacobFormModels())
      form.addReferrerObject(result,model);
    
    for (UIExternalFormModel form : getExternalFormModels())
      form.addReferrerObject(result,model);
    
    for (UIMutableFormModel form : getOwnDrawFormModels())
      form.addReferrerObject(result,model);
    
    for (BrowserModel browser : getBrowserModels())
      browser.addReferrerObject(result,model);
  }

  
  public boolean isI18NKeyInUse(String key)
  {
    key= "%"+key;
    // in jeder db Tabelle nachsehen
    //
    for(TableModel table : tables)
    {
      if(table.isI18NKeyInUse(key))
        return true;
    }
    
    // in jeder domain nachsehen
    //
    for(UIDomainModel domain : domains)
    {
      if(domain.isI18NKeyInUse(key))
        return true;
    }

    // rename all references in groups, foreignFields, browsers...
    for (UIJacobFormModel form : getJacobFormModels())
    {
      if(form.isI18NKeyInUse(key))
        return true;
    }
    
    // check all references in groups, foreignFields, browsers...
    for (UIExternalFormModel form : getExternalFormModels())
    {
      if(form.isI18NKeyInUse(key))
        return true;
    }

    // check all references in groups, foreignFields, browsers...
    for (UIMutableFormModel form : getOwnDrawFormModels())
    {
      if(form.isI18NKeyInUse(key))
        return true;
    }

    // rename all references in groups, foreignFields, browsers...
    for (BrowserModel browser : getBrowserModels())
    {
      if(browser.isI18NKeyInUse(key))
        return true;
    }
    return false;
  }

  public void createMissingI18NKey()
  {
    // in jeder db Tabelle den key mit umbenenne
    //
    for(TableModel table : tables)
      table.createMissingI18NKey();
    
    // in jeder domain nachsehen
    for(UIDomainModel domain : domains)
      domain.createMissingI18NKey();

    // rename all references in groups, foreignFields, browsers...
    for (UIJacobFormModel form : getJacobFormModels())
      form.createMissingI18NKey();
    
    // rename all references in groups, foreignFields, browsers...
    for (UIExternalFormModel form : getExternalFormModels())
      form.createMissingI18NKey();
    
    // rename all references in groups, foreignFields, browsers...
    for (UIMutableFormModel form : getOwnDrawFormModels())
      form.createMissingI18NKey();
    
    // rename all references in groups, foreignFields, browsers...
    for (BrowserModel browser : getBrowserModels())
      browser.createMissingI18NKey();
    
    firePropertyChange(PROPERTY_I18NKEY_CREATED, null,"");
  }

  /**
   * 
   * @param name
   * @return
   */
  public UIFormModel getFormModel(String name)
  {
    for (UIJacobFormModel form : jacobForms)
    {
      if(form.getName().equals(name))
        return form;
    }
    for (UIExternalFormModel form : externalForms)
    {
      if(form.getName().equals(name))
        return form;
    }
    for (UIMutableFormModel form : owndrawForms)
    {
      if(form.getName().equals(name))
        return form;
    }
    for (UIHtmlFormModel form : htmlForms)
    {
      if(form.getName().equals(name))
        return form;
    }
    return null;
  }
  
  public void addElement(UIJacobFormModel form)
  {
    if(jacobForms.indexOf(form)==-1)
    {
      jacobForms.add(form);
      jacob.getForms().addForm(form.getCastor());
      firePropertyChange(PROPERTY_FORM_CREATED,null, form);
    }
  }
  
  public void addElement(UIExternalFormModel form)
  {
    if(externalForms.indexOf(form)==-1)
    {
      externalForms.add(form);
      jacob.getForms().addExternalForm(form.getCastor());
      firePropertyChange(PROPERTY_FORM_CREATED,null, form);
    }
  }
  
  public void addElement(UIMutableFormModel form)
  {
    if(owndrawForms.indexOf(form)==-1)
    {
      owndrawForms.add(form);
      jacob.getForms().addMutableForm(form.getCastor());
      firePropertyChange(PROPERTY_FORM_CREATED,null, form);
    }
  }
  
  public void addElement(UIHtmlFormModel form)
  {
    if(htmlForms.indexOf(form)==-1)
    {
      htmlForms.add(form);
      jacob.getForms().addHtmlForm(form.getCastor());
      firePropertyChange(PROPERTY_FORM_CREATED,null, form);
    }
  }
  

  public void removeElement(UIJacobFormModel form)
  {
    int index = jacobForms.indexOf(form);
    if(index!=-1)
    {
      jacobForms.remove(form);
      jacob.getForms().removeForm(index);
      firePropertyChange(PROPERTY_FORM_DELETED,form,null);
    }
  }

  public void removeElement(UIExternalFormModel form)
  {
    int index = externalForms.indexOf(form);
    if(index!=-1)
    {
      externalForms.remove(form);
      jacob.getForms().removeExternalForm(index);
      firePropertyChange(PROPERTY_FORM_DELETED,form,null);
    }
  }

  public void removeElement(UIMutableFormModel form)
  {
    int index = owndrawForms.indexOf(form);
    if(index!=-1)
    {
      owndrawForms.remove(form);
      jacob.getForms().removeMutableForm(index);
      firePropertyChange(PROPERTY_FORM_DELETED,form,null);
    }
  }

  public void removeElement(UIHtmlFormModel form)
  {
    int index = htmlForms.indexOf(form);
    if(index!=-1)
    {
      htmlForms.remove(form);
      jacob.getForms().removeHtmlForm(index);
      firePropertyChange(PROPERTY_FORM_DELETED,form,null);
    }
  }

  public void renameEventHandler(String fromClass, String toClass)
  {
    // rename the alias in the different jacobForms (groups)
    //
    for (TableAliasModel alias : getTableAliasModels())
      alias.renameEventHandler(fromClass, toClass);
    
    // rename the event handler in the different jacobForms elements
    //
    for (UIJacobFormModel form : getJacobFormModels())
      form.renameEventHandler(fromClass, toClass);

    // rename the event handler in the different jacobForms elements
    //
    for (UIHtmlFormModel form : getHtmlFormModels())
      form.renameEventHandler(fromClass, toClass);

    // rename the event handler in the different jacobForms elements
    //
    for (UIExternalFormModel form : getExternalFormModels())
      form.renameEventHandler(fromClass, toClass);

    // rename the event handler in the different jacobForms elements
    //
    for (UIDomainModel form : getDomainModels())
      form.renameEventHandler(fromClass, toClass);
  }
  
  /**
   * 
   * @return List[UIJacobFormModel]
   */
  public List<UIJacobFormModel> getJacobFormModels()
  {
    return jacobForms;
  }
  
  /**
   * 
   * @return List[UIJacobFormModel]
   */
  public List<UIExternalFormModel> getExternalFormModels()
  {
    return externalForms;
  }
  
  /**
   * 
   * @return List[UIMutableFormModel]
   */
  public List<UIMutableFormModel> getOwnDrawFormModels()
  {
    return owndrawForms;
  }
  
  /**
   * 
   * @return List[UIMutableFormModel]
   */
  public List<UIHtmlFormModel> getHtmlFormModels()
  {
    return htmlForms;
  }
    
  public ActivityDiagramModel getActivityDiagramModel(String name)
  {
    Iterator<ActivityDiagramModel> iter=activityDiagrams.iterator();
    while (iter.hasNext()) 
    {
      ActivityDiagramModel diagram = iter.next();
      if(diagram.getName().equals(name))
        return diagram;
    }
    
    return null;
  }
  
  /**
   * 
   * @return List[ActivityDiagramModel]
   */
  public List<ActivityDiagramModel> getActivityDiagramModels()
  {
    return activityDiagrams;
  }
 
  /**
   * 
   * @param name
   * @return BrowserModel
   */
  public BrowserModel getBrowserModel(String name)
  {
    return browsersCache.get(name);
  }
  

  /**
   * 
   * @return List[BrowserModel]
   */
  public List<BrowserModel> getBrowserModels()
  {
    return browsers;
  }
  
  /**
   * 
   * @param alias
   * @return List[BrowserModel] with valid databrowsers for the hands over table
   */
  public List<BrowserModel> getBrowserModels(TableAliasModel alias)
  {
    List<BrowserModel> result = new ArrayList<BrowserModel>();
    
    for (BrowserModel browser : getBrowserModels())
    {
      if(browser.getTableAliasModel()==alias)
        result.add(browser);
    }
    return result;
  }
  
  /**
   * 
   * @param name
   * @return List[String]
   */
  public List<String> getBrowserNames(TableAliasModel alias)
  {
    List<String> result = new ArrayList<String>();
    for (BrowserModel browser : getBrowserModels())
    {
      if(browser.getTableAliasModel()==alias)
        result.add(browser.getName());
    }
    return result;
  }
  
  /**
   * 
   * @return List[UserRoleModel] 
   */
  public List getUserRoleModels()
  {
    return ListUtils.unmodifiableList(userRoles);
  }

  /**
   * 
   * @param name
   * @return DatasourceModel
   */
  public UserRoleModel getUserRoleModel(String name)
  {
    Iterator<ObjectModel> iter=userRoles.iterator();
    while (iter.hasNext()) 
    {
      UserRoleModel source = (UserRoleModel) iter.next();
      if(source.getName().equals(name))
        return source;
    }
    return null;
  }

  public List<I18NResourceModel> getI18NResourceModels()
  {
    return ListUtils.unmodifiableList(i18ns);
  }

  /**
   * 
   * @return List[DatasourceModel] 
   */
  public List<PhysicalDataModel> getDatasourceModels()
  {
    return ListUtils.unmodifiableList(datasources);
  }

  /**
   * 
   * @param name
   * @return DatasourceModel
   */
  public PhysicalDataModel getDatasourceModel(String name)
  {
    for (PhysicalDataModel source : datasources)
    {
      if(source.getName().equals(name))
        return source;
    }
    return null;
  }
  
  /**
   * 
   * 
   * @param iconPath 
   * 
   * @return 
   */
  public Image getImage(String iconPath)
  {
    Image image = registry.get(iconPath);
    if (null == image)
    {
      IFolder folder = JacobDesigner.getPlugin().getSelectedProject().getFolder(STATIC_IMAGE_PATH);
      String imageURL = folder.getFolder(iconPath).getLocationURI().getPath();
      registry.put(iconPath, new Image(Display.getCurrent(), imageURL));
      return registry.get(iconPath);
    }
    return image;
  }
  

  /**
   * 
   * @return
   */
  public final Jacob getJacob()
  {
    // TODO: How to set application version?
    jacob.setEngineVersion(Version.ENGINE.toString());
    return jacob;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.editor.datamodel.Schema#getName()
   */
  public String getName()
  {
    return "jACOB";
  }
  
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.editor.datamodel.Schema#isLayoutManualAllowed()
   */
  public boolean isLayoutManualAllowed()
  {
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.editor.datamodel.Schema#isLayoutManualDesired()
   */
  public boolean isLayoutManualDesired()
  {
    return true;
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
  
  public PhysicalDataModels getPhysicalDataModels()
  {
    return datasourcesModel;
  }
  
  public RelationsetModel getTestRelationset()
  {
    return testRelationset;
  }
  
  public void setTestRelationset(RelationsetModel testRelationset)
  {
    RelationsetModel save = getTestRelationset();
    if(save == testRelationset)
      return;
    
    if(save!=null)
      save.removePropertyChangeListener(this);
    
    this.testRelationset = testRelationset;
    if(testRelationset!=null)
      testRelationset.addPropertyChangeListener(this);
    firePropertyChange(PROPERTY_TESTRELATIONSET_CHANGED,save,testRelationset);
  }
  
  /**
   * 
   * @return The current Resourcebundle for preview
   */
  public I18NResourceModel getTestResourcebundle()
  {
    return testResourcebundle;
  }
  
  /**
   * 
   * @param testResourcebundle
   */
  public void setTestResourcebundle(I18NResourceModel testResourcebundle)
  {
    I18NResourceModel save = getTestResourcebundle();
    if(save == testResourcebundle)
      return;
    
    if(save!=null)
      save.removePropertyChangeListener(this);
    
    this.testResourcebundle = testResourcebundle;
    if(testResourcebundle!=null)
      testResourcebundle.addPropertyChangeListener(this);
    firePropertyChange(PROPERTY_TESTRESOURCEBUNDLE_CHANGED,save,testResourcebundle);
  }

  /**
   * 
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    // Falls sich das Relationset geï¿½ndert hat, muss allen GUI elementen dies
    // mitgeteilt werden.
    if (evt.getSource() == this.testRelationset)
    {
      if(evt.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_ADDED)
        firePropertyChange(PROPERTY_TESTRELATIONSET_CHANGED,null,this.testRelationset);
      
      else if(evt.getPropertyName() == ObjectModel.PROPERTY_ELEMENT_REMOVED)
        firePropertyChange(PROPERTY_TESTRELATIONSET_CHANGED,null,this.testRelationset);
    }
    else if(evt.getSource() == this.testResourcebundle)
    {
      firePropertyChange(PROPERTY_TESTRESOURCEBUNDLE_CHANGED, null, this.testResourcebundle);
    }
  }
  
  public boolean isDirty()
  {
    return isDirty;
  }
  
  public void setDirty(boolean flag)
  {
    if(flag==isDirty)
      return;
    
    isDirty = flag;
    firePropertyChange(PROPERTY_JACOBMODEL_CHANGED,null,this);
  }
  
  public boolean hasI18NKey(String key)
  {
    // der erste eintrag in den 18n files ist das 'root' file (default) welche alle
    // schlüssel enthält
    return i18ns.get(0).getValue(key)!=null;
  }

  public List getI18NKeys()
  {
    // der erste eintrag in den 18n files ist das 'root' file (default) welche alle
    // schlüssel enthält
    return i18ns.get(0).getKeys();
  }
  
  public I18NResourceModel getDefaultI18NResourceModel()
  {
    return i18ns.get(0);
  }

  public I18NResourceModel getI18NResourceModel(Locale locale)
  {
    for (I18NResourceModel model : i18ns)
    {
      if(model.getLocale()==null)
      {
        if(locale==null)
          return model;
      }
      else if(model.getLocale().equals(locale))
        return model;
    }
    return null;
  }

  public void saveRelatedResources(IProject project) throws Exception
  {
    for (I18NResourceModel model : i18ns)
      model.save(project);
  }
  
  @Override
  public ObjectModel getParent()
  {
    return null;
  }


  public String getSeparator()
  {
    return i18nPreferences.getSeparator();
  }


  public void setSeparatorI18N(String sep)
  {
    i18nPreferences.setSeparator(sep);
  }


  public void setUseI18N(boolean flag)
  {
    i18nPreferences.setUseI18N(flag);
  }


  public boolean useI18N()
  {
    return i18nPreferences.useI18N();
  }


  public boolean getShowHirachical()
  {
    return i18nPreferences.getShowHirachical();
  }


  public void setShowHirachical(boolean flag)
  {
    i18nPreferences.setShowHirachical(flag);
  }
}
