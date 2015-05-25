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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceReconfigureType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceTypeType;


/**
 *  
 */
public final class DeployEntry implements Comparable
{
  static public final transient String RCS_ID = "$Id: DeployEntry.java,v 1.4 2010/07/30 15:53:46 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  /**
   * Helper class to fetch data source infos.
   * 
   * @author Andreas Sonntag
   * @since 2.10
   */
  static final class UsedDatasourceInfo
  {
    final String datasourceName, reconfigureMode, datasourceType;

    private UsedDatasourceInfo(String datasourceName, String reconfigureMode, String datasourceType)
    {
      this.datasourceName = datasourceName;
      this.reconfigureMode = reconfigureMode;
      this.datasourceType = datasourceType;
    }
    
    boolean isIndex()
    {
      return CastorDataSourceTypeType.INDEX.toString().equals(this.datasourceType);
    }
    
    boolean isFullReconfigure()
    {
      return CastorDataSourceReconfigureType.FULL.toString().equals(this.reconfigureMode);
    }
  }

	private final String name;
	private final Version version;
  private String title;
  private final String deployPath;
  private final String deployWebPath;
	private final DeployStatus status;
	private final String jacappFile;
	private final String buildBy;
	private final String buildMachine;
	private final Date buildTime;
	private String deployError;
	private boolean daemon = false;
  /**
   * Map(String -> UsedDatasourceInfo)
   */
  private final Map usedDatasources;
  private final Map properties;
	
	// just needed to avoid to deploy an application version from database, which has already been deployed on this
	// jACOB instance
	private int installSeqNbr;
	
	private final String deployId;
	
	/**
	 * @param name
	 * @param version
	 * @param status
	 * @param jacappFile
	 * @param path
	 * @param buildBy
	 * @param buildMachine
	 * @param buildTime
	 */
	protected DeployEntry(String name, Version version, DeployStatus status, String jacappFile, String path, String webPath, String buildBy, String buildMachine, Date buildTime)
	{
	  this.status = status;
	  this.jacappFile=jacappFile;
		this.name = name;
		this.version = version;
    this.deployPath = path;
    this.deployWebPath = webPath;
		this.deployId =name+"-"+version;
		this.buildBy = buildBy;
		this.buildMachine = buildMachine;
		this.buildTime = buildTime;
    this.usedDatasources = new HashMap();
    this.properties = new HashMap();
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @return Returns the version.
	 */
	public Version getVersion()
	{
		return this.version;
	}

	/**
   * @return the title
   */
  public String getTitle()
  {
    return this.title;
  }

  /**
	 * @return Returns the status.
	 */
	public final DeployStatus getStatus()
	{
	  return status;
	}

	/**
   * Checks whether this deploy entry points to a daemon application, i.e. an
   * application with no user interface.
   * 
   * @return <code>true</code> if daemon deploy entry, otherwise
   *         <code>false</code>
   */
  public final boolean isDaemon()
  {
    return this.daemon;
  }
	
  /**
   * @return Returns the deployPath.
   */
  public String getDeployPath()
  {
    return this.deployPath;
  }

  /**
   * @return Returns the deployWebPath.
   * @since 2.8.5
   */
  public String getDeployWebPath()
  {
    return this.deployWebPath;
  }

  /**
   * @param deployError The deployError to set.
   */
  protected void setDeployError(String deployError)
  {
    this.deployError = deployError;
  }
  
  public boolean hasError()
  {
    return this.deployError != null;
  }
  
  /**
   * @return Returns the deployError.
   */
  public String getDeployError()
  {
    return deployError;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object o)
  {
		if (this == o)
		{
			return 0;
		}
    DeployEntry other = (DeployEntry) o;
    int res = this.name.compareTo(other.name);
    if (res == 0)
    {
      if (this.version.isSameForDeployProcess(other.version))
        return 0;
      return this.version.compareTo(other.version);
    }
    return res;
  }
  
	/* 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) 
	{
		if (this == obj)
		{
			return true;
		}
	  if(obj instanceof DeployEntry)
	  {  
	    return deployId.equals(((DeployEntry)obj).deployId);
	  }
	  
	  return false;
	}

	/* 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
  {
    return deployId.hashCode();
  }

  /**
   * toString methode: creates a String representation of the object
   * @return the String representation
   * @author info.vancauwenberge.tostring plugin
  
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("DeployEntry[");
    buffer.append("name = ").append(name);
    buffer.append(", version = ").append(version);
    buffer.append(", status = ").append(status);
    buffer.append(", installSeqNbr = ").append(installSeqNbr);
//    buffer.append(", deployPath = ").append(deployPath);
    buffer.append("]");
    return buffer.toString();
  }
  
  /**
   * Returns the complete .jacapp file path.
   * 
   * @return Returns the .jacapp file.
   */
  public final String getFile()
  {
    return jacappFile;
  }

	/**
	 * @return Returns the buildBy.
	 */
	public String getBuildBy()
	{
		return buildBy;
	}

	/**
	 * @return Returns the buildMachine.
	 */
	public String getBuildMachine()
	{
		return buildMachine;
	}

	/**
	 * @return Returns the buildTime.
	 */
	public Date getBuildTime()
	{
		return buildTime;
	}

  /**
   * @return Returns the installSeqNbr.
   */
  protected int getInstallSeqNbr()
  {
    return installSeqNbr;
  }
  
  /**
   * @param installSeqNbr The installSeqNbr to set.
   */
  protected void setInstallSeqNbr(int installSeqNbr)
  {
    this.installSeqNbr = installSeqNbr;
  }
  
  protected void addUsedDatasource(String datasourceName, String reconfigureMode, String datasourceType)
  {
    this.usedDatasources.put(datasourceName, new UsedDatasourceInfo(datasourceName, reconfigureMode, datasourceType));
  }
  
  public String[] getUsedDatasources()
  {
    return (String[]) this.usedDatasources.keySet().toArray(new String[this.usedDatasources.size()]);
  }
  
  /**
   * @return
   * @since 2.10
   */
  UsedDatasourceInfo[] getUsedDatasourceInfos()
  {
    return (UsedDatasourceInfo[]) this.usedDatasources.values().toArray(new UsedDatasourceInfo[this.usedDatasources.size()]);
  }
  
  /**
   * Internal method to mark the deploy entry as daemon.
   */
  protected void setDaemon()
  {
    this.daemon = true;
  }

  /**
   * @param title the title to set
   */
  protected void setTitle(String title)
  {
    this.title = title;
  }
  
  /**
   * @param name
   * @return
   * @since 2.8.5
   */
  public synchronized Object getProperty(String name)
  {
    if (null == name)
      throw new NullPointerException("name is null");
    
    return this.properties.get(name);
  }

  /**
   * @param name
   * @param value
   * @since 2.8.5
   */
  public synchronized void setProperty(String name, Object value)
  {
    if (null == name)
      throw new NullPointerException("name is null");
    
    this.properties.put(name, value);
  }
}
