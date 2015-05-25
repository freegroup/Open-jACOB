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
 * Created on Aug 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.tif.jacob.designer.util;

import java.io.File;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.exception.InvalidClassNameException;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIDomainModel;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

public class ClassFinder
{
  public static boolean hasClassFile(String className)
  {
    try
    {
     	IProject project=JacobDesigner.getPlugin().getSelectedProject();
      IJavaProject myJavaProject = JavaCore.create(project);
      IPackageFragmentRoot[] roots = myJavaProject.getPackageFragmentRoots();
      IPath path = roots[0].getPath();
      String fileName = StringUtils.replace(className,".",File.separator)+".java";
      path = path.append(fileName);
      
      IFile file = project.getFile(path.removeFirstSegments(1));
      return (file!=null && file.exists());
    }
    catch (JavaModelException e)
    {
      throw new RuntimeException(e.getMessage(),e);
    }
  }
  
	/**
	 * 
	 * @param superClassName
	 * @param project
	 * @param pm
	 * @return
	 * @throws Exception
	 */
	public static IType[] getAllSubtypes(String superClassName, IJavaProject project, IProgressMonitor pm) throws Exception
	{
		IType parentType=project.findType(superClassName);
		ITypeHierarchy h=parentType.newTypeHierarchy(project,pm);
		return h.getAllSubtypes(parentType);
	}

	/**
	 * 
	 * @param classNameFrom
	 * @param classNameTo
	 * @param project
	 * @param pm
	 * @throws Exception
	 */
	public static void renameClass(String classNameFrom, String classNameTo, IJavaProject project) throws Exception
	{
	  // nothing todo...
	  if(StringUtil.saveEquals(classNameFrom,classNameTo))
	    return;
	  
		String fromPackage = ClassUtil.getPackageName(classNameFrom);
		String toPackage   = ClassUtil.getPackageName(classNameTo);
		
		if(!fromPackage.equals(toPackage))
			throw new InvalidClassNameException("It is not possible to change the package of a class with this api.");

		IType fromClass =project.findType(classNameFrom);

		// nothing todo...fine
		//
		if(fromClass==null)
			return;

		System.out.println("RENAMING  class from:["+classNameFrom+"] to ["+classNameTo+"]");
		String newName = ClassUtil.getShortClassName(classNameTo);
	
		RenameSupport support= RenameSupport.create(fromClass.getCompilationUnit(),newName, RenameSupport.UPDATE_TEXTUAL_MATCHES|RenameSupport.UPDATE_REFERENCES);
		
		Shell activeShell = JavaPlugin.getActiveWorkbenchShell();
		support.perform(activeShell,new ProgressMonitorDialog(activeShell));
	}

	public static void deleteClass(String className, IJavaProject project) throws Exception
	{
		IType type =project.findType(className);

		// nothing todo...fine
		//
		if(type==null)
			return;

		if(type.getCompilationUnit()!=null)
		  type.getCompilationUnit().delete(true,null);
	}

	/**
	 * 
	 * @param classNameFrom
	 * @param classNameTo
	 * @param project
	 * @param pm
	 * @throws Exception
	 */
	public static void renamePackage(String packageNameFrom, String packageNameTo, IJavaProject project) throws Exception
	{
	  // nothing todo...
	  if(StringUtil.saveEquals(packageNameFrom,packageNameTo))
	    return;

	  String fromPackage = ClassUtil.getPackageName(packageNameFrom);
		String toPackage   = ClassUtil.getPackageName(packageNameTo);
		
		if(!fromPackage.equals(toPackage))
			throw new InvalidClassNameException("It is not possible to change the package root of a package with this api.");
	
		IPackageFragment p =project.getAllPackageFragmentRoots()[0].getPackageFragment(packageNameFrom);
		
		// nothing todo...fine
		//
		if(p==null || !p.exists())
			return;

		List subPackages = getSubPackages(p);
		Shell activeShell = JavaPlugin.getActiveWorkbenchShell();

		System.out.println("RENAMING package from:["+packageNameFrom+"] to ["+packageNameTo+"]");
		RenameSupport support= RenameSupport.create(p, packageNameTo, RenameSupport.UPDATE_TEXTUAL_MATCHES|RenameSupport.UPDATE_REFERENCES);
		support.perform(activeShell,new ProgressMonitorDialog(activeShell));

		for (Iterator iter = subPackages.iterator(); iter.hasNext();)
    {
      PackageFragment element = (PackageFragment) iter.next();
      String subPackNew = StringUtil.replace(element.getElementName(), packageNameFrom, packageNameTo);
  		System.out.println("RENAMING package from:["+element.getElementName()+"] to ["+subPackNew+"]");
  		support= RenameSupport.create(element, subPackNew, RenameSupport.UPDATE_TEXTUAL_MATCHES|RenameSupport.UPDATE_REFERENCES);
  		
  		support.perform(activeShell,new ProgressMonitorDialog(activeShell));
    }
	}

	/**
	 * 
	 * @return List[PackageFragment]
	 */
	private static List getSubPackages(IPackageFragment p) throws Exception
	{
	  List result = new ArrayList();
		IJavaElement[] packages= ((IPackageFragmentRoot)p.getParent()).getChildren();
		String[] names =((PackageFragment)p).names;
		int namesLength = names.length;
		newPackage: for (int i= 0, length = packages.length; i < length; i++) 
		{
			String[] otherNames = ((PackageFragment) packages[i]).names;

			if (otherNames.length <= namesLength) 
			  continue;
			
			for (int j = 0; j < namesLength; j++)
			{
				if (!names[j].equals(otherNames[j]))
					continue newPackage;
			}
			result.add(packages[i]);
		}
	  
	  return result;
	}
	
	
	public static void createEventHandlerByName(ObjectModel objectModel)
	{
	  createEventHandlerByName(objectModel, false);
	}
	
	public static void createEventHandlerByName(ObjectModel objectModel, boolean force)
	{
    if (objectModel.getTemplateFileName() == null)
    {
      return;
    }
    try
    {
      IProgressMonitor pm = null;
      IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
      IPackageFragmentRoot[] roots = myJavaProject.getAllPackageFragmentRoots();
      IPath path = roots[0].getPath();
      IType type = myJavaProject.findType(objectModel.getHookClassName());
      if(type == null && (force==true || MessageDialog.openQuestion(null, "Title", "No event handler defined. Should I create one?")))
      {
        ensureAppLogger();
        
        IProject project = myJavaProject.getProject();
        IFolder srcFolder = project.getFolder(path.lastSegment());
        String className = ClassUtil.getShortClassName(objectModel.getHookClassName());
        String packageName = ClassUtil.getPackageName(objectModel.getHookClassName());

        // Die Form kann in verschiedenen Domains vorhanden sein. Hooks in diesen zeigen dann auf die allgemeine
        // Klasse in dem jacob.common.gui package.
        // DEPRECATED!
        if(objectModel instanceof UIFormElementModel)
        {
          UIFormElementModel formElement = (UIFormElementModel)objectModel;
	        Iterator iter = formElement.getGroupContainerModel().getLinkedDomainModels().iterator();
	        String parentClass = packageName+"."+className;
	        while(iter.hasNext())
	        {
	        	UIDomainModel domain = (UIDomainModel)iter.next();
	        	try
            {
              String fromClass = UIGroupElementModel.getEventHandlerName(domain, formElement.getGroupContainerModel(), formElement.getName());
              String subClassName = ClassUtil.getShortClassName(fromClass);
              String subPackageName = ClassUtil.getPackageName(fromClass);
              String subPackagePath = StringUtils.replace(subPackageName, ".", "/");
              String subJavaFileName = subPackagePath + "/" + subClassName + ".java";
              InputStream in = JacobDesigner.getPlugin().find(new Path("templates/subclass.java")).openStream();
              String template = IOUtils.toString(in);
              in.close();
              String javaFileName = subPackagePath + "/" + className + ".java";
              FileUtil.createFolder(project.getFolder(srcFolder.getName() + "/" + subPackagePath));
              IFile newFile = srcFolder.getFile(subJavaFileName);
              template = StringUtils.replace(template, "{date}", new Date().toString());
              template = StringUtils.replace(template, "{author}", System.getProperty("user.name"));
              template = StringUtils.replace(template, "{parentclass}", parentClass);
              template = StringUtils.replace(template, "{package}", subPackageName);
              template = StringUtils.replace(template, "{class}", subClassName);
              template = StringUtils.replace(template, "{application}", objectModel.getJacobModel().getApplicationModel().getName());
              newFile.create(new StringBufferInputStream(template), false, pm);
            }
            catch (Exception e1)
            {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
	        }
        }
        
        // Den allgemeinen Eventhandler anlegen
        //
        String packagePath = StringUtils.replace(packageName, ".", "/");
        String javaFileName = packagePath + "/" + className + ".java";
        String templateFileName = "templates/" + objectModel.getTemplateFileName();
        InputStream in = JacobDesigner.getPlugin().find(new Path(templateFileName)).openStream();
        String template = IOUtils.toString(in);
        in.close();
        FileUtil.createFolder(project.getFolder(srcFolder.getName() + "/" + packagePath));
        IFile newFile = srcFolder.getFile(javaFileName);
        template = StringUtils.replace(template, "{date}", new Date().toString());
        template = StringUtils.replace(template, "{author}", System.getProperty("user.name"));
        template = StringUtils.replace(template, "{package}", packageName);
        template = StringUtils.replace(template, "{class}", className);
        template = StringUtils.replace(template, "{application}", objectModel.getJacobModel().getApplicationModel().getName());
        newFile.create(new StringBufferInputStream(template), false, pm);
        type = myJavaProject.findType(objectModel.getHookClassName());
      }
      if (type != null)
      {
        IJavaElement element = JavaCore.create(type.getResource());
        JavaUI.openInEditor(element);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      showMessage("Unable to create Eventhandler for this Class.");
    }	  
	}

	public static void createEventHandlerByReference(ObjectModel objectModel)
	{
    if (objectModel.getTemplateFileName() != null)
      generateOrOpenEventHandlerByTemplate(objectModel);
    else if(objectModel.getTemplateClass()!=null)
      generateOrOpenEventHandlerByInterface(objectModel);
	}
    
  private static void generateOrOpenEventHandlerByInterface(ObjectModel objectModel)
  {
    try
    {
      IProgressMonitor pm = null;
      IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
      IPackageFragmentRoot[] roots = myJavaProject.getAllPackageFragmentRoots();
      IPath path = roots[0].getPath();
      // Ein eventhandler welcher mal definert war wurde eventuell von Hand geloescht.
      // Den Eventhandler im jad-File dann loeschen, damit man diesen eventuell neu definieren kann.
      //
      if(objectModel.getHookClassName()!=null )
      {
        IType type = myJavaProject.findType(objectModel.getHookClassName());
        if(type==null)
          objectModel.resetHookClassName();
      }
      
      // Falls kein Hook vorhanden -> Anwender Fragen ob einer erzeugt werden soll
      //
      if((objectModel.getHookClassName()==null || myJavaProject.findType(objectModel.getHookClassName())==null))
      {
        if(MessageDialog.openQuestion(null, "Title", "No event handler defined. Should I create one?")==false)
          return;
        
        objectModel.generateHookClassName();
        
        ensureAppLogger();
        
        // Den Eventhandler anlegen
        //
        String className = ClassUtil.getShortClassName(objectModel.getHookClassName());
        String packageName = ClassUtil.getPackageName(objectModel.getHookClassName());

        NewClassWizardPage wizardPage = new NewClassWizardPage();
        wizardPage.setTypeName(className,true); // classname
        wizardPage.setSuperClass(objectModel.getTemplateClass().getName(),false);
        wizardPage.setPackageFragmentRoot(roots[0],false);
        wizardPage.setPackageFragment(roots[0].getPackageFragment(packageName),true);
        wizardPage.setAddComments(true,true);
        wizardPage.setMethodStubSelection(false,false,true,false);
        wizardPage.setModifiers(wizardPage.F_PUBLIC,false);
        
        OpenNewClassWizardAction action = new OpenNewClassWizardAction();
        action.setConfiguredWizardPage(wizardPage);
        action.run();
        if(action.getCreatedElement()!=null)
        {
          IPackageFragment packageFragment = wizardPage.getPackageFragment();
          className=packageFragment.getElementName()+"."+action.getCreatedElement().getElementName();
          objectModel.setHookClassName(className);
          objectModel.firePropertyChange(ObjectModel.PROPERTY_EVENTHANDLER_CHANGED,null,objectModel.getHookClassName());
          if(objectModel instanceof UIGroupElementModel)
            ((UIGroupElementModel)objectModel).getGroupModel().getGroupContainerModel().firePropertyChange(ObjectModel.PROPERTY_ELEMENT_CHANGED, null, objectModel);
        }
        else
        {
          objectModel.resetHookClassName();
        }
      }

      if (objectModel.getHookClassName() != null)
      {
        IType type = myJavaProject.findType(objectModel.getHookClassName());
        if(type!=null) // Kann passieren wenn eclipse ein bischen zu langsam ist eine Klasse anzulegen...
        {
          IJavaElement element = JavaCore.create(type.getResource());
          JavaUI.openInEditor(element);
        }
      }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }   
  }
  
	private static void generateOrOpenEventHandlerByTemplate(ObjectModel objectModel)
	{
    try
    {
      IProgressMonitor pm = null;
      IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
      IPackageFragmentRoot[] roots = myJavaProject.getAllPackageFragmentRoots();
      IPath path = roots[0].getPath();
      // Ein eventhandler welcher mal definert war wurde eventuell von Hand geloescht.
      // Den Eventhandler im jad-File dann loeschen, damit man diesen eventuell neu definieren kann.
      //
      if(objectModel.getHookClassName()!=null )
      {
        IType type = myJavaProject.findType(objectModel.getHookClassName());
        if(type==null)
          objectModel.resetHookClassName();
      }
      
      // Falls kein Hook vorhanden -> Anwender Fragen ob einer erzeugt werden soll
      //
      if((objectModel.getHookClassName()==null || myJavaProject.findType(objectModel.getHookClassName())==null))
      {
        if(MessageDialog.openQuestion(null, "Title", "No event handler defined. Should I create one?")==false)
          return;
        
        objectModel.generateHookClassName();
        
        ensureAppLogger();
        
        IProject project = myJavaProject.getProject();
        IFolder srcFolder = project.getFolder(path.lastSegment());
        String className = ClassUtil.getShortClassName(objectModel.getHookClassName());
        String packageName = ClassUtil.getPackageName(objectModel.getHookClassName());

        
        // Den Eventhandler anlegen
        //
        String packagePath = StringUtils.replace(packageName, ".", "/");
        String javaFileName = packagePath + "/" + className + ".java";
        String templateFileName = "templates/" + objectModel.getTemplateFileName();
        InputStream in = JacobDesigner.getPlugin().find(new Path(templateFileName)).openStream();
        String template = IOUtils.toString(in);
        in.close();
        
        FileUtil.createFolder(project.getFolder(srcFolder.getName() + "/" + packagePath));
        IFile newFile = srcFolder.getFile(javaFileName);
        template = StringUtils.replace(template, "{date}", new Date().toString());
        template = StringUtils.replace(template, "{author}", System.getProperty("user.name"));
        template = StringUtils.replace(template, "{package}", packageName);
        template = StringUtils.replace(template, "{class}",   className);
        template = StringUtils.replace(template, "{application}", objectModel.getJacobModel().getApplicationModel().getName());
        
        newFile.create(new StringBufferInputStream(template), false, pm);
        objectModel.firePropertyChange(ObjectModel.PROPERTY_EVENTHANDLER_CHANGED,null,objectModel.getHookClassName());
        if(objectModel instanceof UIGroupElementModel)
          ((UIGroupElementModel)objectModel).getGroupModel().getGroupContainerModel().firePropertyChange(ObjectModel.PROPERTY_ELEMENT_CHANGED, null, objectModel);
      }

      if (objectModel.getHookClassName() != null)
      {
        IType type = myJavaProject.findType(objectModel.getHookClassName());
        IJavaElement element = JavaCore.create(type.getResource());
        JavaUI.openInEditor(element);
      }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }	  
	}

	
	private static void ensureAppLogger() throws Exception
	{
    IProgressMonitor pm = null;
    IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
    IPackageFragmentRoot[] roots = myJavaProject.getAllPackageFragmentRoots();
    IPath path = roots[0].getPath();
    IType type = myJavaProject.findType("jacob.common.AppLogger");
    if (type == null)
    {
      IProject project = myJavaProject.getProject();
      IFolder srcFolder = project.getFolder(path.lastSegment());
      
      // Den allgemeinen Eventhandler anlegen
      //
      FileUtil.createFolder(project.getFolder(srcFolder.getName() + "/jacob/common"));
      InputStream in = JacobDesigner.getPlugin().find(new Path("templates/AppLogger.java")).openStream();
      String template = IOUtils.toString(in);
      in.close();
      IFile newLogo = srcFolder.getFile("jacob/common/AppLogger.java");
      newLogo.create(new StringBufferInputStream(template), false, pm);
      
      in = JacobDesigner.getPlugin().find(new Path("templates/AppLogger.template")).openStream();
      template = IOUtils.toString(in);
      in.close();
      newLogo = srcFolder.getFile("jacob/common/AppLogger.template");
      newLogo.create(new StringBufferInputStream(template), false, pm);
    }
	}
	
	private static void showMessage(String message)
  {
    MessageDialog.openInformation(null, "jACOB Outline", message);
  }
}
