/*
 * Created on 14.06.2007
 *
 */
package de.tif.jacob.designer.model;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.tif.jacob.core.definition.impl.jad.castor.CastorMutableForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

public class UIMutableFormModel extends UIFormModel
{
  private final CastorMutableForm castor;
//  private final List<UIMutableGroupModel> elements = new ArrayList<UIMutableGroupModel>();
  private UIMutableGroupModel group;
  
  public UIMutableFormModel(JacobModel jacob, String name)
  {
    super(jacob);
    this.castor = new CastorMutableForm();
    this.castor.setName(name);
    if(getJacobModel().useI18N())
      this.castor.setLabel("%"+this.suggestI18NKey());
    else
      this.castor.setLabel(StringUtils.capitalise(name));
    
    this.group = new UIMutableGroupModel(jacob, this);
    this.castor.addGroup(this.group.getCastor());
  }
  
  public UIMutableFormModel(JacobModel jacob, CastorMutableForm form)
  {
    super(jacob);
    this.castor=form;
    this.group = new UIMutableGroupModel(jacob, this, form.getGroup(0));
  }

  
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
      IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
      IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
      for (int i = 0; i < superDescriptors.length; i++)
        descriptors[i] = superDescriptors[i];
      descriptors[superDescriptors.length] = new CheckboxPropertyDescriptor(ID_PROPERTY_VISIBLE, "Visible", PROPERTYGROUP_COMMON);
    
    return descriptors;
  }
  
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_VISIBLE))
      setVisible(((Boolean) val).booleanValue());
    else
      super.setPropertyValue(propName, val);
  }
  

  public Object getPropertyValue(Object propName)
  {
    if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_VISIBLE))
      return new Boolean(getVisible());
    return super.getPropertyValue(propName);
  }
  
  private boolean getVisible()
  {
    return getCastor().getVisible();
  }

  private void setVisible(boolean booleanValue)
  {
    getCastor().setVisible(booleanValue);
  }
  
  @Override
  public String getTemplateFileName()
  {
    return "IMutableFormEventHandler.java";
  }
  

  @Override
  public String getHookClassName()
  {
    if (this.getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
      return this.castor.getEventHandler();
    return "jacob.common.gui." + StringUtils.capitalise(this.getName());
  }
  
  @Override
  public void generateHookClassName() throws Exception
  {
    if (this.getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = this.castor.getEventHandler();
      this.castor.setEventHandler("jacob.event.ui." + StringUtils.capitalise(this.getName()));
      this.firePropertyChange(ObjectModel.PROPERTY_EVENTHANDLER_CHANGED, save, this.castor.getEventHandler());
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
  }

  
  @Override
  public String getLabel()
  {
    return this.castor.getLabel();
  }
  

  public void setLabel(String newLabel)
  {
    String save = this.getLabel();
    
    if(StringUtil.saveEquals(save,newLabel))
      return;
    
    this.castor.setLabel(newLabel);
    this.firePropertyChange(ObjectModel.PROPERTY_LABEL_CHANGED, save, newLabel);
  }
  

  @Override
  public void setName(String name)
  {
    // Den Namen einer form zu ändern ist ein wenig 'tricky'. Alle Referenzen in den Domï¿½nen ( dies sind nur
    // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
    String save = this.getName();

    // unnöigerweise braucht man den Namen nicht setzten
    //
    if(StringUtil.saveEquals(save,name))
      return;
    
    // prüfen ob der Name bereits vergeben ist. Der Formname muss eindeutig sein
    //
    if(this.getJacobModel().getFormModel(name)!=null)
      return;
    
    // prüfen ob der Name bereits vergeben ist. Der Formname muss eindeutig sein
    //
    if(name.length()==0 || !StringUtils.containsOnly(name,"abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ_".toCharArray()))
      return;
   
    this.getJacobModel().renameFormReference(save,name);
    this.castor.setName(name);
    this.group.setName(name+"Group");
    
    // Es müssen alle Referenzen auf diese Form angepasst werden
    //
    this.firePropertyChange(ObjectModel.PROPERTY_NAME_CHANGED, save, name);
  }

  @Override
  public String getName()
  {
    return this.castor.getName();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  @Override
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  @Override
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  @Override
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  @Override
  void removeCastorProperty(CastorProperty property)
  {
    for(int i=0;i<this.castor.getPropertyCount();i++)
    {
      if(this.castor.getProperty(i)==property)
      {
        this.castor.removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property ["+property.getName()+"] is not part of "+this.getClass().getName());
  }

  @Override
  public String getError()
  {
    return null;
  }

  @Override
  public String getInfo()
  {
    return null;
  }

  @Override
  public String getWarning()
  {
    return null;
  }

  /**
   * 
   * @return true if the form used in any Domain or false.
   */
  @Override
  public boolean isInUse()
  {
    for(UIDomainModel domain:this.getJacobModel().getDomainModels())
    {
      // Die Form kann direkt in der Domain sein
      //
      if(domain.getFormModel(this.getName())==this)
        return true;
      
      // Oder innerhalb einer Gruppe
      //
      for(UIFormGroupModel group:domain.getFormGroupModels())
      {
        if(group.getFormModel(this.getName())==this)
          return true;
      }
    }
    return false;
  }
  
  /**
   * 
   * @param key the key to check WITH the % at first character
   * @return
   */
  protected boolean isI18NKeyInUse(String key)
  {
    if(this.getLabel()!=null && this.getLabel().equals(key))
      return true;
    
    return false;
  }  

  
  public CastorMutableForm getCastor()
  {
    return this.castor;
  }


  @Override
  public void hasLinkedTo(UIDomainModel domain)
  {
    this.firePropertyChange(ObjectModel.PROPERTY_FORM_LINKED, null, domain);
  }
  
  @Override
  public void hasLinkedTo(UIFormGroupModel formGroup)
  {
    this.firePropertyChange(ObjectModel.PROPERTY_FORM_LINKED, null, formGroup);
  }
  
  @Override
  public void hasUnlinkedTo(UIDomainModel domain)
  {
    this.firePropertyChange(ObjectModel.PROPERTY_FORM_UNLINKED,  domain, null);
  }
  

  @Override
  public void hasUnlinkedTo(UIFormGroupModel formGroup)
  {
    this.firePropertyChange(ObjectModel.PROPERTY_FORM_UNLINKED,  formGroup, null);
  }


  protected void createMissingI18NKey()
  {
    String label = this.castor.getLabel();
    if(label !=null && label.startsWith("%") && !this.getJacobModel().hasI18NKey(label.substring(1)))
      this.getJacobModel().addI18N(label.substring(1),"",false);
  }

  protected void renameI18NKey( String fromName, String toName)
  {
    if(this.castor.getLabel()!=null && this.castor.getLabel().equals(fromName))
      this.castor.setLabel(toName);
  }

  // Den Namen einer Relation zu ändern ist ein wenig 'tricky'. Alle Referenzen in den ForeignFields ( dies sind nur
  // nur Stringreferenzen auf die eingentliche Form) mï¿½ssen angepasst werden.
  //
  protected void renameRelationReference(String from, String to)
  {
  }  
  
  protected void renameAliasReference(String from, String to)
  {
      group.renameAliasReference(from,to);
  }  
  
  protected void renameRelationsetReference(String from, String to)
  {
  }  
  
  protected void renameBrowserReference(String from, String to)
  {
    group.renameBrowserReference(from,to);
  }  
  
  
  public void renameEventHandler(String fromClass, String toClass)
  {
    // Nicht StringUtils.saveEquals(..) verwenden. Da sind null und "" gleich!!
    // Desweiteren darf dieser Teil nicht ausgefï¿½hrt werden wenn zuvor kein Eventhandler
    // definert war. Wenn zuvor kein Eventhandler definiert war, dann kann man Ihn auch
    // nicht umbenennen.
    //
    if(this.castor.getEventHandler()!=null && this.castor.getEventHandler().equals(fromClass))
    {
      this.castor.setEventHandler(toClass);
      this.firePropertyChange(ObjectModel.PROPERTY_EVENTHANDLER_CHANGED, fromClass, toClass);
    }
    
    group.renameEventHandler(fromClass,toClass);
  }  

  public void setTableAlias(String tableAlias)
  {
    String save = this.getTableAlias();
    if (save != null && save.equals(tableAlias))
      return;
    if (this.getJacobModel().getTableAliasModel(tableAlias) == null)
      return;
    
    group.setTableAlias(tableAlias);
    this.firePropertyChange(ObjectModel.PROPERTY_LABEL_CHANGED, save, tableAlias);
    
    // set a default browser for the new group
    //
    this.setBrowserModel(this.getJacobModel().getBrowserModels(this.getJacobModel().getTableAliasModel(tableAlias)).get(0));
    if (this.getName() == null || this.getName().length() == 0)
      this.setName(tableAlias);
  }

  public TableAliasModel getTableAliasModel()
  {
    return group.getTableAliasModel();
  }  
  
  public String getTableAlias()
  {
    return group.getTableAlias();
  }  
  
  /**
   * 
   * @param browser
   */
  public void setBrowserModel(BrowserModel browser)
  {
    group.setBrowserModel(browser);
  }

  public BrowserModel getBrowserModel()
  {
    return group.getBrowserModel();
  }

  public String getBrowser()
  {
    return group.getBrowserModel().getName();
  }
  
  public void setBrowser(String browser)
  {
    String save = getBrowser();
    BrowserModel saveBrowser = this.getBrowserModel();
    
    // no changes
    //
    if (StringUtil.saveEquals(save, browser))
      return;
    
    BrowserModel newBrowser = this.getJacobModel().getBrowserModel(browser);
    if (newBrowser == null)
      throw new RuntimeException("Browser [" + browser + "] doesn't exists.");
    
    group.setBrowser(browser);
    this.firePropertyChange(ObjectModel.PROPERTY_BROWSER_CHANGED, save, browser);
    
    // Dem Browser und alle die diesen anzeigen mitteilen, das sich der (Error/Warning) Status 
    // möglicherweise geändert hat. Der Browser feuert ein Event und alle Listener aktualisieren sich dann
    //
    newBrowser.firePropertyChange(ObjectModel.PROPERTY_BROWSER_CHANGED, null, newBrowser);
    if(saveBrowser!=null)
      saveBrowser.firePropertyChange(ObjectModel.PROPERTY_BROWSER_CHANGED, null, saveBrowser);
  }

  public UIMutableGroupModel getRenderGroup()
  {
    return group;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
}
