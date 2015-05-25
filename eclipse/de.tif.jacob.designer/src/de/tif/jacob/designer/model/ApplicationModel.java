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

import java.util.ArrayList;
import java.util.List;
import de.tif.jacob.core.definition.impl.jad.castor.CastorApplication;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDomainRef;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType;
import de.tif.jacob.designer.exception.DesignerException;
import de.tif.jacob.designer.exception.InvalidDomainNameException;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class ApplicationModel extends ObjectModel
{
  private final CastorApplication castor;
  private final List<UIDomainModel> domains = new ArrayList<UIDomainModel>();
  
  
  /**
   * 
   */
  public ApplicationModel(JacobModel jacob, CastorApplication application)
  {
    super(jacob);
    this.castor = application;
    
//    castor.setEventHandlerLookupMethod(CastorApplicationEventHandlerLookupMethodType.REFERENCE);
    
    for(int i=0;i<application.getDomainCount();i++)
    {
      CastorDomainRef domRef= application.getDomain(i);
      // the casting is to enshure that only the right elements will be filled in the array
      UIDomainModel domain = jacob.getDomainModel(domRef.getContent());
      if(domain==null)
        System.out.println("ERROR:"+domRef.getContent());
      else
        domains.add(domain);
    }
  }
  
  @Override
  public String getName()
  {
    return castor.getName();
  }

  @Override
  public String getLabel()
  {
    return castor.getTitle();
  }
  
  
  @Override
  public String getExtendedDescriptionLabel()
  {
    if(getJacobModel().isDirty())
      return getLabel()+" <UNSAVED>";
    return getLabel();
  }

  public void setLabel(String name)
  {
    String save = getLabel();
    if(StringUtil.saveEquals(save, name))
      return;
    
     castor.setTitle(name);
     firePropertyChange(PROPERTY_LABEL_CHANGED, save, name);
  }


  /**
   * 
   * @return List[DomainModel]
   */
  public List<UIDomainModel> getDomainModels()
  {
    return new ArrayList(domains);
  }
  
  /**
   * 
   * @param domain
   * @return UIDaomainModel
   */
  public UIDomainModel getDomain(String domain)
  {
    for(UIDomainModel obj: getDomainModels())
    {
      if(obj.getName().equals(domain))
        return obj;
    }
    return null;
  }
    
  
  // Den Namen einer form zu �ndern ist ein wenig 'tricky'. Alle Referenzen in den Dom�nen ( dies sind nur
  // nur Stringreferenzen auf die eingentliche Form) m�ssen angepasst werden.
  //
  protected void renameFormReference(String from, String to)
  {
    for(UIDomainModel obj: getDomainModels())
    {
      obj.renameFormReference(from,to);
    }
  }
  
  
  // Den Namen einer Domain zu �ndern ist ein wenig 'tricky'. Alle Referenzen in den Dom�nen ( dies sind nur
  // nur Stringreferenzen auf die eingentliche Domain) m�ssen angepasst werden.
  //
  protected void renameDomainReference(String from, String to)
  {
    for(int i=0; i< castor.getDomainCount(); i++)
    {
      CastorDomainRef domain= castor.getDomain(i);
      if(domain.getContent().equals(from))
        domain.setContent(to);
    }
  }

  
  public void removeElement(UIDomainModel domain)
  {
    int index = domains.indexOf(domain);
    if(index!=-1)
    {
      domains.remove(index);
      castor.removeDomain(index);
      getJacobModel().removeElement(domain);
  		firePropertyChange(PROPERTY_ELEMENT_REMOVED, domain, null);
    }
  }
  
  public void addElement(UIDomainModel domain) throws DesignerException
  {
    UIDomainModel current = getJacobModel().getDomainModel(domain.getName());
    if(current != null && current !=domain)
      throw new InvalidDomainNameException("Domain with name ["+domain.getName()+"] already exists in the application definition.");

    domains.add(domain);
    
    getJacobModel().addElement(domain);
    
    // add the domain to the application
    //
    CastorDomainRef domainRef = new CastorDomainRef();
    domainRef.setContent(domain.getName());
    castor.addDomain(domainRef);
    
		firePropertyChange(PROPERTY_ELEMENT_ADDED,null, domain);
  }
  
  /**
   * 
   * @param form
   */
  public void addElement(UIDomainModel existingDomain, UIDomainModel domain) throws DesignerException
  {
    UIDomainModel current = getJacobModel().getDomainModel(domain.getName());
    if(current != null && current !=domain)
      throw new InvalidDomainNameException("Domain with name ["+domain.getName()+"] already exists in the application definition.");
    
    int index = domains.indexOf(existingDomain);
    domains.add(index+1,domain);
    
    getJacobModel().addElement(domain);
    
    CastorDomainRef domainRef = new CastorDomainRef();
    domainRef.setContent(domain.getName());
    castor.addDomain(index+1,domainRef);
    
		firePropertyChange(PROPERTY_ELEMENT_ADDED,null, domain);
  }

  
  /**
   * 
   */
  @Override
  public String toString()
  {
    return "Application:"+castor.getName();
  }
  
  public CastorApplication getCastor()
  {
    return castor;
  }
  
  @Override
  public String getError()
  {
    return null;
  }
  
  @Override
  public String getWarning()
  {
    return null;
  }
  
  @Override
  public String getInfo()
  {
    return null;
  }
  

  @Override
	public void  generateHookClassName() throws Exception
	{
    if(getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = getCastorEventHandler();
      setCastorEventHandler("jacob.event.ui.Application");
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED,save,getCastorEventHandler());
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
	}
  
  @Override
	public void resetHookClassName() throws Exception 
	{
    if(isEventHandlerLookupByReference())
    {
      String save = getCastorEventHandler();
      setCastorEventHandler(null);
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED,save,null);
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
	}

	protected String getCastorEventHandler()
  {
	  if(getCastor()==null)
	    return null;
	  
    return getCastor().getEventHandler();
  }
	
  protected void setCastorEventHandler(String event)
  {
    if(getCastor()==null)
      throw new RuntimeException("Object does'nt support event handling");
    
    getCastor().setEventHandler(event);
  }

  
  @Override
  public String getHookClassName()
  {
    if(getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
      return getCastorEventHandler();
    return "jacob.common.gui.Application";
  }


  @Override
  public String getTemplateFileName()
  {
    return "IApplicationEventHandler.java";
  }
  
  @Override
  public boolean isInUse()
  {
    return true;
  }
  
  public boolean isEventHandlerLookupByReference()
  {
    if(castor.getEventHandlerLookupMethod()==null)
      return false;
    
    return castor.getEventHandlerLookupMethod().getType()== CastorApplicationEventHandlerLookupMethodType.REFERENCE_TYPE;
  }

  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }

  @Override
  public ObjectModel getParent()
  {
    return getJacobModel();
  }
}
