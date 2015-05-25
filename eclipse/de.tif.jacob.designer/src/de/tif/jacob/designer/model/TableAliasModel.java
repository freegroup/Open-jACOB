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
 * Created on Oct 20, 2004
 *
 */
package de.tif.jacob.designer.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.aliascondition.AliasCondition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowTableAliasEditorAction;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.util.ClassFinder;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class TableAliasModel extends ObjectWithPropertyModel implements PropertyChangeListener, IOpenable
{
  final CastorTableAlias castor;
  
  List  foreignKeyRelationships = new ArrayList();
  List  primaryKeyRelationships = new ArrayList();
  
  private boolean conditionChecked = false;
  private Exception conditionSyntaxError;
  private boolean hasTableHook=false;
  private boolean hasTableHookDetermined=false;
  private TableModel tableModel; // cache them for performance reason
  
  public TableAliasModel(TableModel parentTable, String name)
  {
    super(parentTable.getJacobModel());
    castor = new CastorTableAlias();
    castor.setName(name);
    castor.setTable(parentTable.getName());
    
    this.tableModel = parentTable;
    
    parentTable.getJacobModel().addPropertyChangeListener(this);
  }
  
  protected TableAliasModel(JacobModel jacob, CastorTableAlias tableAlias)
  {
    super(jacob);
    this.castor     = tableAlias;
    jacob.addPropertyChangeListener(this);
  }

 
  public String getName()
  {
    return castor.getName();
  }
  
  
  @Override
  public String getExtendedDescriptionLabel()
  {
    return getName()+" <"+getTableModel().getName()+">";
  }

  public boolean isInUse()
  {
    // check if the alias is used in any UIGroupModel
    //
    Iterator iter = getJacobModel().getJacobFormModels().iterator();
    while (iter.hasNext())
    {
      UIJacobFormModel form = (UIJacobFormModel) iter.next();
      Iterator groupIter = form.getGroupModels().iterator();
      while (groupIter.hasNext())
      {
        UIGroupModel group = (UIGroupModel) groupIter.next();
        // the table alias is used by a group
        //
        if(group.getTableAliasModel()==this)
          return true;
        
        // ...check the elements in the group
        //
        Iterator elementIter = group.getElements().iterator();
        while (elementIter.hasNext())
        {
          UIGroupElementModel element = (UIGroupElementModel) elementIter.next();
          if(element instanceof UIDBForeignFieldModel)
          {
            UIDBForeignFieldModel ffield = (UIDBForeignFieldModel)element;
            // the element is use by a foreign field
            if(ffield.getForeignTableAliasModel()==this)
              return true;
          }
        }
      }
    }
    
    // check if the alias is used in any browser
    //
    if(getJacobModel().getBrowserModels(this).size()>0)
      return true;
    
    // check if the table alias is used in any relation
    //
    if(getJacobModel().getRelationModelsFrom(this).size()>0)
      return true;
    if(getJacobModel().getRelationModelsTo(this).size()>0)
      return true;
    
    return false;
  }
  
  public String getError()
  {
    // parse condition if existing and not already parsed
    parsedCondition();
    if (this.conditionSyntaxError != null)
      return "Condition of table alias <"+getName()+"> is syntactically wrong.";
    
    return null;
  }
  
  public String getWarning()
  {
    if(getPossibleBrowserNames().size()<=0)
      return "No browser defined for table alias <"+getName()+">";

    if(!isInUse())
      return "Table alias <"+getName()+"> is not used in any object."; 

    return null;
  }
  
  public String getInfo()
  {
    return null;
  }
  
  public void setName(String name)
  {
    String save = castor.getName();

    if(name==null || name.length()==0)
      return;
    
    if (StringUtil.saveEquals(name, save))
      return;

    if(name==null || name.indexOf(" ")!=-1)
      throw new RuntimeException("Invalid Table alias name ["+name+"]. Spaces in name is not valid.");

    if(getJacobModel().getTableAliasModel(name)!=null)
      throw new RuntimeException("Table alias name ["+name+"] is already in use. Please use another.");
    
    this.castor.setName(name);

    getJacobModel().renameAliasReference(save, name);
    
    // Die condition am Alias muss auf den neuen Namen angepasst werden
    //
    String condition = this.castor.getCondition();
    if(condition!=null && condition.length()>0)
    {
      Iterator iter = getDBFieldNames().iterator();
      while (iter.hasNext())
      {
        String field = (String) iter.next();
        condition = StringUtil.replace(condition,save+"."+field,name+"."+field);
      }
      setCondition(condition);
    }
    
    try
    {
      // Falls sich der Name eines Elementes geï¿½ndert hat, mï¿½ssen alle Eventhandler auf den neuen
      // Namen angepasst werden
      //
      IJavaProject myJavaProject = JavaCore.create(JacobDesigner.getPlugin().getSelectedProject());
      String fromClass = "jacob.event.data."+StringUtils.capitalise(save)+"TableRecord";
      String toClass   = "jacob.event.data."+StringUtils.capitalise(name)+"TableRecord";
      ClassFinder.renameClass(fromClass,toClass ,myJavaProject);
      
      // rename the common gui event handler to the new alias name
      //
      fromClass = "jacob.common.gui."+save;
      toClass   = "jacob.common.gui."+name;
      ClassFinder.renamePackage(fromClass,toClass ,myJavaProject);
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }

    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }
  
  public void setCondition(String condition)
  {
  	condition = condition!=null&&condition.length()>0?condition:null;
  	
    String save = castor.getCondition();
    
    if (StringUtil.saveEquals(condition, save))
      return;
    
    castor.setCondition(condition);
    
    // enforce parsing of syntax
    this.conditionChecked = false;
    parsedCondition();
    
    firePropertyChange(PROPERTY_CONDITION_CHANGED, save, condition);
  }
  
  public String getCondition()
  {
    return StringUtil.toSaveString(castor.getCondition());
  }
  
  private void parsedCondition()
  {
    if (!this.conditionChecked)
    {
      // check condition syntax by means of simply parsing it
      try
      {
        this.conditionSyntaxError = null;
        AliasCondition.parse(getCondition());
      }
      catch (Exception ex)
      {
        // syntax is not ok
        this.conditionSyntaxError = ex;
      }
      
      // mark condition as checked
      this.conditionChecked = true;
    }
  }
  
  /**
   * Required for Velocity template.
   * Aus unerfindlichen Gründen wird sonst die Methode "getDescription" von Velocity
   * nicht gefunden. ?!
   */
  public String getDescription()
  {
    return super.getDescription();
  }


  protected String getTable()
  {
    return castor.getTable();
  }

  public TableModel getTableModel()
  {
    if(tableModel!=null)
      return tableModel;
  	tableModel = getJacobModel().getTableModel(this.getTable());
    return tableModel;
  }
  

  /**
   * Liefert alle Tabellen zurück die den primary key dieser Tabelle enthalten.
   * 
   * @return List[TableAliasModel]
   */
  public List<TableAliasModel> getToLinkedTableAliases()
  {
    List<TableAliasModel> result = new ArrayList<TableAliasModel>();
    for(RelationModel relation: getJacobModel().getRelationModels())
    {
      if(relation.getFromTableAlias()==this)
        result.add(relation.getToTableAlias());
    }
    return result;
  }

  
  public List<String> getToLinkedTableAliasNames()
  {
    List<String> result = new ArrayList<String>();
    for(TableAliasModel alias:getToLinkedTableAliases())
    {
      result.add(alias.getName());
    }
    return result;
  }

  /**
   * Return all relations which are connected to a foreign table (the 'from table')
   * @return List[RelationModel]
   */
  public List<RelationModel> getFromRelationModels()
  {
    List<RelationModel> result = new ArrayList<RelationModel>();
    for(RelationModel relationModel: getJacobModel().getRelationModels())
    {
      if(relationModel.getToTableAlias().getName().equals(getName()))
      {
        result.add(relationModel);
      }
    }
    
    return result;
  }
  
    /**
   * Liefert alle Tabellen von denen der Primary key in dieser Tabelle enthalten ist.
   * 
   * @return List[TableAliasModel]
   */
  public List<TableAliasModel> getFromLinkedTableAliases()
  {
    List<TableAliasModel> result = new ArrayList<TableAliasModel>();
    Iterator<RelationModel> iter = getJacobModel().getRelationModels().iterator();
    while(iter.hasNext())
    {
      RelationModel relation = iter.next();
      if(relation.getToTableAlias()==this)
        result.add(relation.getFromTableAlias());
    }
    return result;
  }
 

  /**
   * 
   * @return FieldModel
   */
  public FieldModel getFieldModel(String name)
  {
    return getTableModel().getFieldModel(name);
  }
  
  /**
   * 
   * @return List[FieldModel]
   */
  public List getFieldModels()
  {
    return getTableModel().getFieldModels();
  }
  /**
   * 
   * @return KeyModel
   */
  public KeyModel getKeyModel(String keyName)
  {
    return getTableModel().getKeyModel(keyName);
  }
  
  public KeyModel getPrimaryKeyModel()
  {
    return getTableModel().getPrimaryKeyModel();
  }
  
  /**
   * 
   * @return List[KeyModel]
   */
  public List getMatchedForeingKeyModels(KeyModel key)
  {
    return getTableModel().getMatchedForeingKeyModels(key);
  }
  
  /**
   * 
   * @return List[String]
   */
  public List<String> getDBFieldNames()
  {
    return getTableModel().getDBFieldNames();
  }
 
  /**
   * 
   * @return List[String]
   */
  public List<String> getFieldNames()
  {
    return getTableModel().getFieldNames();
  }
  


  public List<FieldModel> getFields(UIDBLocalInputFieldModel inputElement)
  {
    return getTableModel().getFields(inputElement);
  }

  public List<String> getFieldNames(UIDBLocalInputFieldModel model)
  {
    return getTableModel().getFieldNames(model);
  }
  
  /**
   * 
   * @return List[String]
   */
  public List<String> getPossibleBrowserNames()
  {
    List<String> result = new ArrayList<String>();
    Iterator iter = getJacobModel().getBrowserModels(this).iterator();
    while(iter.hasNext())
    {
      BrowserModel browser = (BrowserModel)iter.next();
      result.add(browser.getName());
    }
    return result;
  }
  
  /**
   * 
   */
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
	  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
	  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length] = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,"Name", PROPERTYGROUP_DB);
		descriptors[superDescriptors.length+1] = new TextPropertyGroupingDescriptor(ID_PROPERTY_CONDITION,"Condition", PROPERTYGROUP_DB);
		
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
			setName((String) val);
		else if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_CONDITION))
			setCondition((String) val);
		else
			super.setPropertyValue(propName, val);
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_NAME))
			return getName();
		if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_CONDITION))
			return getCondition();

		return super.getPropertyValue(propName);
	}
 
  
  public String getImageBaseName()
  {
    if(getCondition().length()>0)
      return super.getImageBaseName()+"_withcondition";
    else
      return super.getImageBaseName();
  }

  
  /**
   * @return List[RelationModel]
   */
  public List getForeignKeyRelationships()
  {
    return this.foreignKeyRelationships;
  }


  /**
   * @return List[RelationModel]
   */
  public List getPrimaryKeyRelationships()
  {
    return this.primaryKeyRelationships;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void removeCastorProperty(CastorProperty property)
  {
    for(int i=0;i<castor.getPropertyCount();i++)
    {
      if(castor.getProperty(i)==property)
      {
        castor.removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property ["+property.getName()+"] is not part of "+getClass().getName());
  }
 
  
  protected CastorTableAlias getCastor()
  {
    return castor;
  }
  
  public String getHookClassName()
  {
    String className = "jacob.event.data."+StringUtils.capitalise(getName())+"TableRecord"; 
    if(hasTableHookDetermined==false)
    {
      hasTableHookDetermined=true;
      hasTableHook=ClassFinder.hasClassFile(className);
    }

    if(hasTableHook)
      return className;
    
    return null;
  }
    
  public void renameEventHandler(String fromClass, String toClass)
  {
    // es wurde ein TableHook fï¿½r diesen Alias angelegt oder ein bestehender wurde umbenannt
    //
    String hook =getHookClassName();
    if(hook!=null &&(hook.equals(toClass) || hook.equals(fromClass)))
    {
      // Table hook wurde gelï¿½scht
      if(fromClass!=null && toClass==null)
      {
        hasTableHook=false;
        hasTableHookDetermined=true;
      }
      
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, fromClass, toClass);
    }
  }  
  
  // Die condition am Alias muss auf den neuen Namen angepasst werden
  //
  protected void renameFieldReference(FieldModel field,String from , String to)
  {
    try
    {
      String condition = this.castor.getCondition();
      if(condition!=null && condition.length()>0)
      {
        Perl5Matcher matcher     = new Perl5Matcher();
        PatternCompiler compiler = new Perl5Compiler();
        Pattern         pattern  = compiler.compile("(.*\\W|^)"+getName()+"[.]"+from+"(\\W|$.*)");

        setCondition(Util.substitute(matcher, pattern, new Perl5Substitution("$1"+getName()+"."+to+"$2"),condition,Util.SUBSTITUTE_ALL));
      }
    }
    catch (MalformedPatternException e)
    {
      JacobDesigner.showException(e);
    }
  }
  
  public void resetHookClassName() throws Exception
  {
    hasTableHook=false;
  }
	
	public void  generateHookClassName() throws Exception
	{
    hasTableHook=true;
    hasTableHookDetermined=true;
	}

	/**
   * 
   */
  public String getTemplateFileName()
  {
    return "DataTableRecordEventHandler.java";
  }

  public void propertyChange(PropertyChangeEvent arg0)
  {
    // meine gecachete Tabelle ha sich geändert. Eventuell wurde diese gelöscht
    // Keine weitere Untersuchung notwendig
    if(arg0.getOldValue()== tableModel)
      tableModel=null;
  }


  public String getLabel()
  {
    return getName();
  }
  
  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getTableModel())
      result.addReferences(this);
  }
  
  @Override
  public ObjectModel getParent()
  {
    return getJacobModel().getApplicationModel();
  }
  
  public void openEditor()
  {
    new ShowTableAliasEditorAction()
    {
      @Override
      public TableAliasModel getTableAliasModel()
      {
        return TableAliasModel.this;
      }
    }.run(null);
  }


}
