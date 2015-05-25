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

package de.tif.jacob.core.definition.impl.jad;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.IFormDefinition;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource;
import de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelation;
import de.tif.jacob.core.definition.impl.jad.castor.Forms;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceReconfigureType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceTypeType;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class JadDefinition extends AbstractDefinition
{
  static public transient final String RCS_ID = "$Id: JadDefinition.java,v 1.9 2010/06/29 12:39:01 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.9 $";
  
  private final Map forms = new HashMap();
  
  /**
   * Map<String->CastorDataSourceReconfigureType>
   */
  private final Map datasourceReconfigureModes = new HashMap();
  /**
   * Map<String->CastorDataSourceTypeType>
   */
  private final Map datasourceTypes = new HashMap();
  private final String defaultApplication;
  private final Version version;
  private final Version jadVersion;
  
	/**
	 *  
	 */
	protected JadDefinition(Jacob jacob) throws Exception
	{
		this.defaultApplication = jacob.getDefaultApplication();
    this.version = Version.parseVersion(jacob.getVersion());
    this.jadVersion = Version.parseVersion(jacob.getEngineVersion());
		
    // process datasources
    for (int i = 0; i < jacob.getDataSources().getDataSourceCount(); i++)
    {
      CastorDataSource datasource = jacob.getDataSources().getDataSource(i);
      this.datasourceReconfigureModes.put(datasource.getName(), datasource.getReconfigure());
      this.datasourceTypes.put(datasource.getName(), datasource.getType());
    }

    // process tables
    for (int i = 0; i < jacob.getTables().getTableCount(); i++)
    {
      add(new JadTableDefinition(jacob.getTables().getTable(i)));
    }

		// process tables aliases (must be after tables!)
		for (int i = 0; i < jacob.getTableAliases().getTableAliasCount(); i++)
		{
			add(new JadTableAlias(jacob.getTableAliases().getTableAlias(i)));
		}

		// process browsers (must be after table aliases!)
		for (int i = 0; i < jacob.getBrowsers().getBrowserCount(); i++)
		{
			add(new JadBrowserDefinition(jacob.getBrowsers().getBrowser(i)));
		}

		// process relations
		for (int i = 0; i < jacob.getRelations().getRelationCount(); i++)
		{
			CastorRelation relation = jacob.getRelations().getRelation(i);
			if (relation.getCastorRelationChoice().getOneToMany() != null)
			{
				add(new JadOneToManyRelation(relation));
			}
			else if (relation.getCastorRelationChoice().getManyToMany() != null)
			{
				add(new JadManyToManyRelation(relation));
			}
			else
			{
				throw new RuntimeException("Unknown relation type");
			}
		}

		// process relationsets
		for (int i = 0; i < jacob.getRelationsets().getRelationsetCount(); i++)
		{
			add(new JadRelationSet(jacob.getRelationsets().getRelationset(i)));
		}

    postProcessing(getTables());
    postProcessing(getTableAliases());
    postProcessing(getRelationSets());
    postProcessing(getRelations());

    // and initialize default relation set which contains all relations
    initDefaultRelationsSet();
    
    // initialise browsers, which depend on complete relationset initialisation (i.e. needed for foreign fields)
    postProcessing(getBrowsers());
    
		// process jacob forms
		for (int i = 0; i < jacob.getForms().getFormCount(); i++)
		{
			add(new JadJacobFormDefinition(jacob.getForms().getForm(i), this));
		}
    
    // process external forms
    for (int i = 0; i < jacob.getForms().getExternalFormCount(); i++)
    {
      add(new JadExternalFormDefinition(jacob.getForms().getExternalForm(i), this));
    }
    
    // process mutable forms
    for (int i = 0; i < jacob.getForms().getMutableFormCount(); i++)
    {
      add(new JadMutableFormDefinition(jacob.getForms().getMutableForm(i), this));
    }
    
    // process html forms
    for (int i = 0; i < jacob.getForms().getHtmlFormCount(); i++)
    {
      add(new JadHtmlFormDefinition(jacob.getForms().getHtmlForm(i), this));
    }
    
    postProcessing(getForms());

		// process domains
		for (int i = 0; i < jacob.getDomains().getDomainCount(); i++)
		{
			add(new JadDomainDefinition(jacob.getDomains().getDomain(i), this));
		}
    postProcessing(getDomains());

		// process domains
		for (int i = 0; i < jacob.getApplications().getApplicationCount(); i++)
		{
			add(new JadApplicationInfo(jacob.getApplications().getApplication(i)));
		}
	}
  
  public void add(IFormDefinition form)
  {
    this.forms.put(form.getName(), form);
  }
  
  protected Iterator getForms()
  {
    return this.forms.values().iterator();
  }

  public IFormDefinition getForm(String name)
  {
    IFormDefinition result = (IFormDefinition) this.forms.get(name);
    if (null == result)
    {
      throw new RuntimeException("No form " + name + " found!");
    }
    return result;
  }

  public void toJacob(Jacob jacob, ConvertToJacobOptions options)
  {
    super.toJacob(jacob, options);
    
    jacob.setVersion(this.version.toString());
    jacob.setEngineVersion(Version.ENGINE.toString());
    jacob.setDefaultApplication(this.defaultApplication);
    
    // fetch form data (attention: use TreeSet for sorted output!)
    Forms jacobForms = new Forms();
    jacob.setForms(jacobForms);
    Iterator iter = new TreeSet(this.forms.values()).iterator();
    while (iter.hasNext())
    {
      CastorJacobForm jacobForm = new CastorJacobForm();
      JadJacobFormDefinition jadForm = (JadJacobFormDefinition) iter.next();
      jadForm.toJacob(jacobForm, options);
      jacobForms.addForm(jacobForm);
    }
  }
  
	/**
	 * @return Returns the defaultApplication.
	 */
	protected String getDefaultApplication()
	{
		return defaultApplication;
	}

	/**
	 * @return Returns the version.
	 */
	protected Version getVersion()
	{
		return version;
	}

  public Version getJadVersion()
  {
    return jadVersion;
  }
  
  public CastorDataSourceReconfigureType getReconfigureMode(String datasourceName)
  {
    CastorDataSourceReconfigureType type = (CastorDataSourceReconfigureType) this.datasourceReconfigureModes.get(datasourceName);
    return type != null ? type : super.getReconfigureMode(datasourceName);
  }
  
  public CastorDataSourceTypeType getDataSourceType(String datasourceName)
  {
    CastorDataSourceTypeType type = (CastorDataSourceTypeType) this.datasourceTypes.get(datasourceName);
    return type != null ? type : super.getDataSourceType(datasourceName);
  }
}
