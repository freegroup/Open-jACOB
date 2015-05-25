/*
 * Created on 07.05.2008
 *
 */
package de.tif.jacob.designer.model;

import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.screen.impl.GlobalMultipleUpdateSelectionAction;
import de.tif.jacob.screen.impl.IsolatedMultipleUpdateSelectionAction;
import de.tif.jacob.screen.impl.SelectionActionDelete;
import de.tif.jacob.screen.impl.SelectionActionDeleteAllOrNothing;
import de.tif.jacob.util.StringUtil;

public abstract class SelectionActionModel extends ObjectModel
{
  final ISelectionActionProvider provider;
  SelectionActionEventHandler castor;
  
  private static Map<String,Class> className2classType = new HashMap<String, Class>();
  
  static
  {
    className2classType.put(SelectionActionDelete.class.getName(),SelectionActionDeleteAllOrNothing.class);
    className2classType.put(SelectionActionDeleteAllOrNothing.class.getName(),SelectionActionDeleteAllOrNothing.class);
    className2classType.put(IsolatedMultipleUpdateSelectionAction.class.getName(),IsolatedMultipleUpdateSelectionAction.class);
    className2classType.put(GlobalMultipleUpdateSelectionAction.class.getName(),GlobalMultipleUpdateSelectionAction.class);
  }
  
  public static SelectionActionModel createModelObject(ISelectionActionProvider provider, String className)
  {
    if(className2classType.containsKey(className))
    {
      return new EngineSelectionActionModel(provider,className);
    }
    return new ApplicationSelectionActionModel(provider,className);
  }

  public static SelectionActionModel createModelObject(ISelectionActionProvider provider, SelectionActionEventHandler handler)
  {
    if(className2classType.containsKey(handler.getClassName()))
    {
      return new EngineSelectionActionModel(provider,handler);
    }
    return new ApplicationSelectionActionModel(provider,handler);
  }
    
  protected SelectionActionModel(ISelectionActionProvider provider, SelectionActionEventHandler handler)
  {
    super(provider.getJacobModel());
    this.castor = handler;
    this.provider = provider;
  }
  
  public String getHookClassName()
  {
    return this.castor.getClassName();
  }
  
  public void setHookClassName( String toClass)
  {
    String old = this.castor.getClassName();
    this.castor.setClassName(toClass);
    firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, old, this.castor.getClassName());
  }
  
  /**
   * 
   * @param name
   */
  public void setLabel(String name)
  {
    String save =castor.getLabel();
    if (StringUtil.saveEquals(name, save))
      return;
    
    castor.setLabel(name);
    firePropertyChange(PROPERTY_LABEL_CHANGED, save, name);
  }

  /**
   * 
   * @param name
   */
  public String getLabel()
  {
    return castor.getLabel();
  }

    
  public SelectionActionEventHandler getCastor()
  {
    return castor;
  }
  
  public ISelectionActionProvider getProvider()
  {
    return provider;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }

  public String getError()
  {
    return null;
  }

  public String getInfo()
  {
    return null;
  }

  public String getName()
  {
    return null;
  }

  public ObjectModel getParent()
  {
    return null;
  }

  public String getWarning()
  {
    return null;
  }

  public boolean isInUse()
  {
    return false;
  }

  @Override
  public Version getRequiredJacobVersion()
  {
    if(getCastor().getClassName().equals(IsolatedMultipleUpdateSelectionAction.class.getName()))
      return new Version(2,8,5);
    if(getCastor().getClassName().equals(GlobalMultipleUpdateSelectionAction.class.getName()))
      return new Version(2,8,5);
    return super.getRequiredJacobVersion();
  }

}
