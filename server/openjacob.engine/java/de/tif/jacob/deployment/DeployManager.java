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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.index.IndexDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.reconfigure.CommandList;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.impl.AbstractApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.definition.impl.jad.IJadApplicationDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.InvalidApplicationException;
import de.tif.jacob.core.model.Dapplication;
import de.tif.jacob.core.model.Dapplversion;
import de.tif.jacob.core.model.Datasource;
import de.tif.jacob.core.model.Japplication;
import de.tif.jacob.core.model.Japplversion;
import de.tif.jacob.core.model.Useddatasource;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.deployment.DeployEntry.UsedDatasourceInfo;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.util.DatetimeUtil;
import de.tif.jacob.util.file.Directory;
import de.tif.jacob.util.stream.Stream;

/**
 * @author Andreas Herz
 *
 */
public final class DeployManager extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: DeployManager.java,v 1.13 2010/07/30 15:53:46 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.13 $";

  public static final String MANIFESTENTRY_ENGINE       = "jACOB-Engine-Version";
  public static final String MANIFESTENTRY_VERSION      = "jACOB-Application-Version";
  public static final String MANIFESTENTRY_NAME         = "jACOB-Application-Name";
  public static final String MANIFESTENTRY_BUILDBY      = "Built-By";
  public static final String MANIFESTENTRY_BUILDMACHINE = "Built-Machine";
  public static final String MANIFESTENTRY_BUILDTIME    = "Built-Time";
  
  // das Verzeichnis im jacapp welches über HTTP verfügbar gemacht werden soll.
  // Zugreifbar über:
  // http://<servername>[<port>]/jacob/application/<applicationName>/<applicationVersion>/
  private static final String EXPORT_DIRECTORY          = "web";
  
  /**
   * Use TreeSet for sorted output of {@link #getDeployEntries()}
   * 
   * Set{DeployEntry}
   */
  private static final Set deployEntries = new TreeSet();
  
  /**
   * Set{DeployNotifyee}
   */
  private static final Set deployNotifyees = new HashSet();
  
  /**
   * Synchronization object for deployment operations. 
   */
  public static final Object MUTEX = new Object();
  
  /**
   * This number points to the most recent deployed application version change, i.e.
   * each time a application version is installed or updated (e.g. status change)
   * the corresponding field installseqnbr in japplversion table is set to the
   * next highest value. This is a flag to any jACOB instance in a cluster that
   * this change has to be processed (i.e. deployed) on that node.  
   */
  private static int highestInstallSequenceNumber = Integer.MIN_VALUE;
  
  public static final String ADMIN_APPLICATION_NAME = "admin";
  
  private static final String ADMIN_PREFIX = ADMIN_APPLICATION_NAME + "-";
  private static final String JACOB_APPLICATION_EXTENSION = ".jacapp";
  
  private static boolean readonlyDataHookAccess = false;
  
  private boolean deployIssues = false;
  
  /**
   * Checks whether data could be read but not written during intermediate
   * deployment action.
   * 
   * @return the readonlyDataHookAccess
   */
  public static boolean isReadonlyDataHookAccess()
  {
    return readonlyDataHookAccess;
  }

  /**
   * Checks whether any updates concerning installed application versions
   * exist in the persistent database. If yes, process them.
   * 
   * @throws Exception on any problem
   */
  public static void checkInstalledApplicationUpdates() throws Exception
  {
    synchronized (MUTEX)
    {
      int highest = Integer.MIN_VALUE;

      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTable japplversionTable = accessor.getTable(Japplversion.NAME);
      japplversionTable.qbeClear();
      japplversionTable.qbeSetValue(Japplversion.installseqnbr, ">" + highestInstallSequenceNumber);
      japplversionTable.search();
      for (int i = 0; i < japplversionTable.recordCount(); i++)
      {
        IDataTableRecord japplversionRecord = japplversionTable.getRecord(i);

        checkInstalledApplication(japplversionRecord);

        // remember highest sequence number
        int installseqnbr = japplversionRecord.getintValue(Japplversion.installseqnbr);
        if (highest < installseqnbr)
          highest = installseqnbr;
      }

      // if all has been successfully done -> update new max value
      if (highest > highestInstallSequenceNumber)
        highestInstallSequenceNumber = highest;
    }
  }
  
  /**
   * Checks whether any update concerning the given installed application version record 
   * has to be performed. If yes, process them.<p>
   * 
   * Note: This method is made public because it will be called from the admin
   * application to immediatly perform requested changes instead of waiting for the 
   * DeployWatch task to do this kind of processing.
   * 
   * @param japplversionRecord
   * @throws Exception
   */
  public static void checkInstalledApplication(IDataTableRecord japplversionRecord) throws Exception
  {
    String jacappPath = Bootstrap.getJacappPath();
    
    String name = japplversionRecord.getStringValue(Japplversion.name);
    String version = japplversionRecord.getStringValue(Japplversion.version);
    int installseqnbr = japplversionRecord.getintValue(Japplversion.installseqnbr);
    
    if (name.equals(ADMIN_APPLICATION_NAME))
    {
      // never fetch admin application from database since we want to use the
      // one which comes from the engine
      return;
    }

    synchronized (MUTEX)
    {
      // check whether this application change has already been processed on
      // this jACOB instance
      DeployEntry entry = getDeployEntry(name, version);
      if (entry != null && entry.getInstallSeqNbr() >= installseqnbr)
      {
        return;
      }

      // create new application file in file system
      //
      File file = new File(jacappPath + name + "-" + version + JACOB_APPLICATION_EXTENSION);
      logger.info("Loading application file from database: " + file);
      FileOutputStream fileOutput = new FileOutputStream(file, false);
      try
      {
        fileOutput.write(japplversionRecord.getBytesValue(Japplversion.content));
      }
      finally
      {
        fileOutput.close();
      }

      // and deploy it
      deploy(file.getAbsolutePath(), false);
    }
  }
  
  /*
   * 
   * 
   * @see de.tif.jacob.core.BootstrapEntry#init()
   */
	public void init() throws Throwable
	{
	  // to avoid any FileMonitor interaction
	  synchronized (MUTEX)
	  {
	    initUnsynchronized();
	  }
	}
	
	/* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#hasWarnings()
   */
  public boolean hasWarnings()
  {
    return this.deployIssues;
  }

  private void initUnsynchronized() throws Throwable
  {
	  final String ADMIN_FILE_NAME = ADMIN_PREFIX + Version.ADMIN + JACOB_APPLICATION_EXTENSION;
    String jacappPath = Bootstrap.getJacappPath();
    
    // ------------------------------------------------------------------------
    // Cleanup jacapp directory, i.e. delete all files and directories except
    // current admin application jacapp.
    // ------------------------------------------------------------------------
    logger.info("Cleaning up: " + jacappPath);
    File[] files = new File(jacappPath).listFiles();
    if (files != null)
    {
      for (int i = 0; i < files.length; i++)
      {
        File file = files[i];

        if (file.getName().equals(ADMIN_FILE_NAME))
        {
          continue;
        }

        if (file.isDirectory())
          FileUtils.deleteDirectory(file);
        else
        {
          // in OpenjACOB mode jacapp's could come within the war
          if (Version.IS_OPEN_ENGINE && file.getName().endsWith(JACOB_APPLICATION_EXTENSION))
            continue;
          
          file.delete();
        }
      }
    }

    // ------------------------------------------------------------------------
    // Check applications in database with application files in file system.
    //
    // Note: Depending on the builddatetime files in file system could be overwritten.
    //
    // Note: At this (early) step only database read access is possible!
    //       Reason: Table hook access is not possible, since
    //               admin application is not deployed so far.
    // ------------------------------------------------------------------------
    readonlyDataHookAccess = true;
    Set alreadyExistingApps = new HashSet();
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable japplversionTable = accessor.getTable(Japplversion.NAME);
    japplversionTable.qbeClear();
    japplversionTable.search();
    for (int i = 0; i < japplversionTable.recordCount(); i++)
    {
      IDataTableRecord japplversionRecord = japplversionTable.getRecord(i);
      String name = japplversionRecord.getStringValue(Japplversion.name);
      String version = japplversionRecord.getStringValue(Japplversion.version);
      int fix = japplversionRecord.getintValue(Japplversion.fix);
      if (fix > 0)
        version = version + "." + fix;
      
      // collect applications already in database, i.e. apps which have already been installed.
      alreadyExistingApps.add(name);
      
      // remember highest sequence number 
      int installseqnbr = japplversionRecord.getintValue(Japplversion.installseqnbr);
      if (highestInstallSequenceNumber < installseqnbr)
        highestInstallSequenceNumber = installseqnbr;

      if (name.equals(ADMIN_APPLICATION_NAME))
      {
        // never fetch admin application from database since we want to use the
        // one which comes from the engine
        // Note: Should never happen since we do not store admin application
        //       in database. So just to be on the safe side :-)
        continue;
      }

      // OpenjACOB mode: Check whether an application file already exists and
      // whether this one is more recent by means of comparing builddatetime.
      //
      File file = new File(jacappPath + name + "-" + version + JACOB_APPLICATION_EXTENSION);
      if (Version.IS_OPEN_ENGINE && file.exists())
      {
        try
        {
          JarFile zf = new JarFile(file);
          try
          {
            Timestamp fileBuildTime = DatetimeUtil.convertToTimestamp(zf.getManifest().getMainAttributes().getValue(MANIFESTENTRY_BUILDTIME));
            Timestamp dbBuildTime = japplversionRecord.getTimestampValue(Japplversion.builddatetime);
            if (dbBuildTime.getTime() <= fileBuildTime.getTime())
            {
              // do not overwrite newer file in file system
              continue;
            }
          }
          finally
          {
            zf.close();
          }
        }
        catch (Exception ex)
        {
          // just in case we have trash in the file system
          logger.warn("Could not read application file: " + file, ex);
        }
        
        // we must clean the deploy directory, otherwise realDeploy() won't unpack
        //
        File jacobDir = new File(getDeployClassDir(name, Version.parseVersion(version)));
        if (jacobDir.exists())
          FileUtils.deleteDirectory(jacobDir);

        logger.info("Overwriting local application file with content from database: " + file);
      }
      else
      {
        logger.info("Loading application file from database: " + file);
      }
      
      // create new application file in file system
      //
      FileOutputStream fileOutput = new FileOutputStream(file, false);
      try
      {
        fileOutput.write(japplversionRecord.getBytesValue(Japplversion.content));
      }
      finally
      {
        fileOutput.close();
      }
    }
    
    // now we could call data hooks
    readonlyDataHookAccess = false;

    // ------------------------------------------------------------------------
    // Deploy admin application first
    //
    // Note: this has to be done before storing any modifications to jACOB
    // internal tables, because otherwise calling hooks would throw an error
    // ------------------------------------------------------------------------
    File adminFile = new File(jacappPath + ADMIN_PREFIX + Version.ADMIN + JACOB_APPLICATION_EXTENSION);
    if (adminFile.exists())
    {
      deploy(adminFile.getAbsolutePath(), true);
    }
    else
    {
      throw new Exception("Admin application does not exist: " + adminFile);
    }

    // ------------------------------------------------------------------------
    // Deploying remaining application files in the file system 
    // ------------------------------------------------------------------------
    ArrayList fileArray = Directory.getAll(new File(jacappPath), false);
    for (Iterator iter = fileArray.iterator(); iter.hasNext();)
    {
      File file = (File) iter.next();

      if (file.getName().startsWith(ADMIN_PREFIX))
      {
        // skip deploying admin application once again
        continue;
      }

      // this is an application definition. extract them in the jacobClass
      // directory
      //
      if (file.getName().endsWith(JACOB_APPLICATION_EXTENSION))
      {
        DeployEntry entry;
        try
        {
          entry = deploy(file.getAbsolutePath(), false);
          if (entry.hasError())
            this.deployIssues = true;
        }
        catch (Throwable th)
        {
          this.deployIssues = true;
          logger.error("Could not deploy application " + file, th);
          continue;
        }
        
        // for OpenjACOB deliveries which come with applications
        // these should be ready for use. So all required datasources
        // have to be created and reconfigured. But this should only
        // be done once, i.e. application is not already existing
        // in jACOB repository database.
        //
        if (Version.IS_OPEN_ENGINE && !entry.hasError() && !alreadyExistingApps.contains(entry.getName()))
        {
          initializeApplicationDatasources(entry);
        }
      }
    }
  }
  
  private void initializeApplicationDatasources(DeployEntry entry) throws Exception
  {
    if (logger.isInfoEnabled())
      logger.info("Initializing application data sources of " + entry.getName() + "-" + entry.getVersion() + "..");

    // get info of application's data sources and sort data source info in that
    // way that indices come last
    //
    UsedDatasourceInfo[] datasourceInfos = entry.getUsedDatasourceInfos();
    Arrays.sort(datasourceInfos, new Comparator()
    {
      public int compare(Object o1, Object o2)
      {
        UsedDatasourceInfo info1 = (UsedDatasourceInfo) o1;
        UsedDatasourceInfo info2 = (UsedDatasourceInfo) o2;

        if (info1.isIndex() ^ info2.isIndex())
          return info1.isIndex() ? 1 : -1;
        return info1.hashCode() - info2.hashCode();
      }
    });
    
    for (int i = 0; i < datasourceInfos.length; i++)
    {
      UsedDatasourceInfo datasourceInfo = datasourceInfos[i];

      // skip predefined datasources
      if (DataSource.isPredefined(datasourceInfo.datasourceName))
        continue;

      // skip already existing datasources
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTable datasourceTable = accessor.getTable(Datasource.NAME);
      if (datasourceTable.exists(Datasource.name, datasourceInfo.datasourceName))
        continue;
      
      // skip data sources which should not be full reconfigured by this application
      if (!datasourceInfo.isFullReconfigure())
        continue;

      // start initializing data source
      try
      {
        if (logger.isInfoEnabled())
          logger.info("Initializing application data sources " + datasourceInfo.datasourceName + "..");

        // create datasource definition
        //
        IDataTransaction transaction = accessor.newTransaction();
        try
        {
          IDataTableRecord datasourceRecord = datasourceTable.newRecord(transaction);
          datasourceRecord.setValue(transaction, Datasource.name, datasourceInfo.datasourceName);
          datasourceRecord.setValue(transaction, Datasource.location, Datasource.location_ENUM._jACOB);
          datasourceRecord.setValue(transaction, Datasource.adjustment, Datasource.adjustment_ENUM._jACOB);
          if (datasourceInfo.isIndex())
          {
            datasourceRecord.setValue(transaction, Datasource.rdbtype, Datasource.rdbType_ENUM._Lucene);
            datasourceRecord.setValue(transaction, Datasource.connectstring, "index:lucene:fsDirectory:" + DataSource.WEB_APPDIR_REPLACEMENT + "WEB-INF/data/" + datasourceInfo.datasourceName);
            datasourceRecord.setValue(transaction, Datasource.configurepool, Datasource.configurePool_ENUM._No);
          }
          else
          {
            datasourceRecord.setValue(transaction, Datasource.rdbtype, Datasource.rdbType_ENUM._HSQL);
            datasourceRecord.setValue(transaction, Datasource.connectstring, "jdbc:hsqldb:file:" + DataSource.WEB_APPDIR_REPLACEMENT + "WEB-INF/data/" + datasourceInfo.datasourceName);
            datasourceRecord.setValue(transaction, Datasource.configurepool, Datasource.configurePool_ENUM._No);
            datasourceRecord.setValue(transaction, Datasource.jdbcdriverclass, org.hsqldb.jdbcDriver.class.getName());
            datasourceRecord.setValue(transaction, Datasource.username, "sa");
          }
          transaction.commit();
        }
        finally
        {
          transaction.close();
        }

        IApplicationDefinition applDef = DeployMain.getApplication(entry.getName(), entry.getVersion());
        if (datasourceInfo.isIndex())
        {
          // The rebuild must run in the context of the index updating
          // application
          //
          // IBIS: HACK with task context - misuse?
          IndexDataSource indexDataSource = (IndexDataSource) DataSource.get(datasourceInfo.datasourceName);
          indexDataSource.rebuild(new TaskContextSystem(applDef));
        }
        else
        {
          // do reconfigure of HSQL database
          //
          SQLDataSource sqlDataSource = (SQLDataSource) DataSource.get(datasourceInfo.datasourceName);
          ISchemaDefinition desiredSchemaDefinition = applDef.getSchemaDefinition(datasourceInfo.datasourceName);
          Reconfigure reconfigure = sqlDataSource.getReconfigureImpl();
          ISchemaDefinition currentSchemaDefinition = reconfigure.fetchSchemaInformation();
          sqlDataSource.setup(currentSchemaDefinition);
          CommandList commands = reconfigure.reconfigure(desiredSchemaDefinition, currentSchemaDefinition, true);
          if (commands.size() != 0)
          {
            commands.execute(sqlDataSource, false);
          }
        }
      }
      catch (Exception ex)
      {
        this.deployIssues = true;
        if (logger.isWarnEnabled())
          logger.warn("Initialization of application data source " + datasourceInfo.datasourceName + " failed!", ex);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // make copy of deploy entries first
    //
    List entryList;
    synchronized (deployEntries)
    {
      entryList = new ArrayList(deployEntries);
      deployEntries.clear();
    }
    
    // call all listeners to notify that all applications are shutdowned
    //
    Iterator iter = deployNotifyees.iterator();
    while (iter.hasNext())
    {
      DeployNotifyee notifyee = (DeployNotifyee) iter.next();
      try
      {
        for (int i = 0; i < entryList.size(); i++)
        {
          notifyee.onUndeploy((DeployEntry) entryList.get(i));
        }
      }
      catch (Exception ex)
      {
        ExceptionHandler.handle(ex);
      }
    }

  }
  
  /**
   * deploy an *.jacapp file into the jACOB-Server.<p>
   * 
   * Note: This method is called from the admin application.
   * 
   * @param jacappFile
   * @return the deploy entry of the deployed application
   * @throws Exception
   */
  public static DeployEntry deploy(String jacappFile) throws Exception
  {
    return deploy(jacappFile, true);
  }
  
  protected static String getDeployClassDir(String appName, Version version)
  {
    return Bootstrap.getJacappPath() + appName + File.separator + version.toShortString() + File.separator;
  }
  
  private static String getDeployWebDir(String appName, Version version)
  {
    return Bootstrap.getApplicationRootPath() + "application" + File.separator + appName + File.separator + version.toShortString() + File.separator;
  }
 
  /**
   * deploy an *.jacapp file into the jACOB-Server
   * 
   * @param jacappFile
   * @return the deploy entry of the deployed application
   * @throws Exception
   */
  private static DeployEntry deploy(String jacappFile, boolean abortOnDeployError) throws Exception
  {
    synchronized (MUTEX)
    {
      File file = new File(jacappFile);
      JarFile zf = new JarFile(file);
      try
      {
        // read application info from manifest
        //
        String api_engine_version = zf.getManifest().getMainAttributes().getValue(MANIFESTENTRY_ENGINE);
        String manifest_version = zf.getManifest().getMainAttributes().getValue(MANIFESTENTRY_VERSION);
        String appName = zf.getManifest().getMainAttributes().getValue(MANIFESTENTRY_NAME);

        if (manifest_version == null || appName == null)
          throw new InvalidApplicationException("Missing entry in jACOB manifest. [jACOB-Application-Version=" + manifest_version + "][jACOB-Application-Name=" + appName + "]");

        String buildBy = zf.getManifest().getMainAttributes().getValue(MANIFESTENTRY_BUILDBY);
        String buildMachine = zf.getManifest().getMainAttributes().getValue(MANIFESTENTRY_BUILDMACHINE);
        Date buildTime = DatetimeUtil.convertToTimestamp(zf.getManifest().getMainAttributes().getValue(MANIFESTENTRY_BUILDTIME));
        
        Version version = Version.parseVersion(manifest_version);

        // create the deploy directory for the .jacapp file
        //
        String deployDir = getDeployClassDir(appName, version);
        String webDir   = getDeployWebDir(appName, version);
        
        //create the deploy entry
        //
        DeployEntry entry = new DeployEntry(appName, version, determineDeployStatus(appName, version), jacappFile, deployDir, webDir, buildBy, buildMachine, buildTime);

        // try to deploy the stuff
        //
        Exception deployException = null;
        try
        {
          // ensure that only jacapp's which have been build with the correct
          // jacobBase.jar can be deployed
          //
          if (api_engine_version != null)
          {
            Version v = Version.parseVersion(api_engine_version);
            if (!v.isCompatible(Version.ENGINE))
              throw new InvalidApplicationException("Application needs jACOB Version [" + api_engine_version + "] or compatible. Installed jACOB version is [" + Version.ENGINE+ "]");
          }

          // and really deploy the application within this engine
          IApplicationDefinition appDef = realDeploy(entry, deployDir,webDir, zf);
          
          // no domains -> daemon!
          if (appDef.getDomainDefinitions().size() == 0)
            entry.setDaemon();

          // move jacapp into database, if needed
          //
          storePersistentInstalledApplication(entry, appDef, file, api_engine_version);
        }
        catch (Exception ex)
        {
          logger.warn("Could not deploy application " + file, ex);

          entry.setDeployError(ex.toString());
          
          deployException = ex;
        }

        // Store deployment information in transient database
        //
        storeTransientDeployInformation(entry);

        if (abortOnDeployError && deployException != null)
        {
          throw deployException;
        }
        
        return entry;
      }
      finally
      {
        zf.close();
      }
    }
  }
  
  /**
   * Internal method used for Scheduler Service to mark an deployed application as erroneous, if not
   * all task could be started.
   *  
   * @param entry
   * @param error
   * @throws Exception
   */
  public static void setDeployError(DeployEntry entry, String error) throws Exception
  {
    entry.setDeployError(error);
    
    // remove old entry first (if existing), i.e. the entry should not be
    // registered anymore
    synchronized (deployEntries)
    {
      deployEntries.remove(entry);
    }
    
    // Store deployment information in transient database
    //
    storeTransientDeployInformation(entry);
  }
  
  private static DeployStatus determineDeployStatus(String appName, Version version) throws Exception
  {
    if (appName.equals(ADMIN_APPLICATION_NAME))
    {
      // admin application is always productive
      return DeployStatus.PRODUCTIVE;
    }
    
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
    IDataTable japplversionTable = accessor.getTable(Japplversion.NAME);
    japplversionTable.qbeClear();
    japplversionTable.qbeSetKeyValue(Japplversion.name, appName);
    japplversionTable.qbeSetKeyValue(Japplversion.version, version.toShortString());
    if (japplversionTable.search() > 0)
    {
      // new patch has been deployed -> get original status
      return DeployStatus.parseStatus(japplversionTable.getRecord(0).getStringValue(Japplversion.status));
    }
    // new release has been deployed -> set to inactive (but productive for openjacob!)
    return Version.IS_OPEN_ENGINE ? DeployStatus.PRODUCTIVE : DeployStatus.INACTIVE;
  }
  
  /**
   * Pushes the deployed application to the jACOB datasource.
   * 
   * @param entry deploy entry
   * @param appDef application definition
   * @param file jacapp file
   * @throws Exception on any problem
   */
  private static void storePersistentInstalledApplication(DeployEntry entry, IApplicationDefinition appDef, File file, String api_engine_version) throws Exception
  {
    FileInputStream fileInput = new FileInputStream(file);
    try
    {
      byte[] fileContent = IOUtils.toByteArray(fileInput);

      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        IDataTable japplicationTable = accessor.getTable(Japplication.NAME);
        japplicationTable.qbeSetKeyValue(Japplication.name, entry.getName());
        if (japplicationTable.search() == 0)
        {
          // create the new application entry
          IDataTableRecord japplicationRecord = japplicationTable.newRecord(transaction);
          japplicationRecord.setStringValue(transaction, Japplication.name, entry.getName());
        }

        if (entry.getName().equals(ADMIN_APPLICATION_NAME))
        {
          // never push jACOB admin application to database, but ensure that an entry in japplication table exists
          transaction.commit();
          return;
        }
        
        IDataTable japplversionTable = accessor.getTable(Japplversion.NAME);
        IDataTableRecord japplversionRecord;
        IDataTable useddatasourceTable = accessor.getTable(Useddatasource.NAME);
        japplversionTable.qbeClear();
        japplversionTable.qbeSetKeyValue(Japplversion.name, entry.getName());
        japplversionTable.qbeSetKeyValue(Japplversion.version, entry.getVersion().toShortString());
        if (japplversionTable.search() > 0)
        {
          // delete all associated datasource entries
          useddatasourceTable.qbeSetKeyValue(Useddatasource.applicationname, entry.getName());
          useddatasourceTable.qbeSetKeyValue(Useddatasource.applicationversion, entry.getVersion().toShortString());
          useddatasourceTable.fastDelete(transaction);

          // get record to modify
          japplversionRecord = japplversionTable.getRecord(0);
        }
        else
        {
          // create the new deploy application entry
          japplversionRecord = japplversionTable.newRecord(transaction);
          japplversionRecord.setStringValue(transaction, Japplversion.name, entry.getName());
          japplversionRecord.setStringValue(transaction, Japplversion.version, entry.getVersion().toShortString());
          japplversionRecord.setStringValue(transaction, Japplversion.status, entry.getStatus().toString());
        }

        japplversionRecord.setIntValue(transaction, Japplversion.fix, entry.getVersion().getFix());
        japplversionRecord.setStringValue(transaction, Japplversion.api_version, api_engine_version);
        if (appDef instanceof IJadApplicationDefinition)
          japplversionRecord.setStringValue(transaction, Japplversion.jad_version, ((IJadApplicationDefinition) appDef).getJadVersion().toString());
        else
          japplversionRecord.setStringValue(transaction, Japplversion.jad_version, null);
        japplversionRecord.setStringValueWithTruncation(transaction, Japplversion.buildby, entry.getBuildBy());
        japplversionRecord.setStringValueWithTruncation(transaction, Japplversion.buildmachine, entry.getBuildMachine());
        japplversionRecord.setValue(transaction, Japplversion.builddatetime, entry.getBuildTime());
        japplversionRecord.setLongValue(transaction, Japplversion.contentsize, fileContent.length);

        // only commit to database if any effective change exists
        // Note: This should not be the case, if the file has just been
        // created by means of database content.
        if (japplversionRecord.hasChangedValues())
        {
          if (logger.isInfoEnabled())
            logger.info("Moving jacapp to database: " + file.getAbsolutePath());

          // would always returns true for hasChangedValues()
          japplversionRecord.setBinaryValue(transaction, Japplversion.content, fileContent);
          japplversionRecord.setValue(transaction, Japplversion.installedsince, "now");
          
          // create an entry for each used datasource
          Set datasources = new HashSet();
          List tableDefs = appDef.getTableDefinitions();
          AbstractApplicationDefinition appDefIntern = (AbstractApplicationDefinition) appDef;
          for (int i=0; i < tableDefs.size(); i++)
          {
            ITableDefinition tableDef = (ITableDefinition) tableDefs.get(i);
            if (datasources.add(tableDef.getDataSourceName()))
            {
              IDataTableRecord datasourceRecord = useddatasourceTable.newRecord(transaction);
              
              String datasourceName = tableDef.getDataSourceName();
              String reconfigureMode = appDefIntern.getReconfigureMode(datasourceName).toString();
              String datasourceType = appDefIntern.getDataSourceType(datasourceName).toString();
              
              datasourceRecord.setStringValue(transaction, Useddatasource.applicationname, entry.getName());
              datasourceRecord.setStringValue(transaction, Useddatasource.applicationversion, entry.getVersion().toShortString());
              datasourceRecord.setStringValue(transaction, Useddatasource.datasourcename, datasourceName);
              datasourceRecord.setStringValue(transaction, Useddatasource.reconfiguremode, reconfigureMode);
              datasourceRecord.setStringValue(transaction, Useddatasource.datasourcetype, datasourceType);
              
              entry.addUsedDatasource(tableDef.getDataSourceName(), reconfigureMode, datasourceType);
            }
          }
          transaction.commit();
        }

        // mark this entry with sequence number to avoid reload from database by means of DeployWatch task
        // Note: the sequence number will be generated and set in the appropriate table hook
        //
        entry.setInstallSeqNbr(japplversionRecord.getintValue(Japplversion.installseqnbr));
        
        entry.setTitle(appDef.getTitle());
      }
      finally
      {
        transaction.close();
      }
    }
    finally
    {
      fileInput.close();
    }
  }
  
  /**
   * Internal method for checking that the given application is registered in the transient jACOB datasource.<p>
   * 
   * Note: This is needed because of relation constraints to table dapplication
   * 
   * @param accessor the admin application accessor
   * @param transaction a valid transaction
   * @param application the name of the application to check
   * @throws Exception on any problem
   */
  public static void ensureTransientApplication(IDataAccessor accessor, IDataTransaction transaction, String application) throws Exception
  {
    IDataTable dapplicationTable = accessor.getTable(Dapplication.NAME);
    dapplicationTable.qbeSetKeyValue(Dapplication.name, application);
    if (dapplicationTable.search() == 0)
    {
      // create the new application entry
      IDataTableRecord dapplicationRecord = dapplicationTable.newRecord(transaction);
      dapplicationRecord.setStringValue(transaction, Dapplication.name, application);
    }
  }
  
  private static void storeTransientDeployInformation(DeployEntry entry) throws Exception
  {
		// and register for transient database
		IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
		IDataTransaction transaction = accessor.newTransaction();
		try
		{
		  ensureTransientApplication(accessor, transaction, entry.getName());

      IDataTable dapplversionTable = accessor.getTable(Dapplversion.NAME);
      IDataTableRecord dapplversionRecord;
      dapplversionTable.qbeClear();
			dapplversionTable.qbeSetKeyValue(Dapplversion.name, entry.getName());
			dapplversionTable.qbeSetValue(Dapplversion.version, entry.getVersion().toShortString() + "*");
			if (dapplversionTable.search() > 0)
			{
        // get record to modify
        dapplversionRecord = dapplversionTable.getRecord(0);
			}
			else
			{
	      // create the new deploy application entry
				dapplversionRecord = dapplversionTable.newRecord(transaction);
				dapplversionRecord.setStringValue(transaction, Dapplversion.name, entry.getName());
			}
      
      dapplversionRecord.setStringValue(transaction, Dapplversion.version, entry.getVersion().toString());
			dapplversionRecord.setValue(transaction, Dapplversion.deploydatetime, "now");
			dapplversionRecord.setValue(transaction, Dapplversion.status, entry.hasError() ? Dapplversion.status_ENUM._Error : Dapplversion.status_ENUM._Success);
			dapplversionRecord.setStringValueWithTruncation(transaction, Dapplversion.error, entry.getDeployError());
      
			transaction.commit();
		}
		finally
		{
			transaction.close();
		}
  }

  private static void removeTransientDeployInformation(DeployEntry entry) throws Exception
  {
		// and register for transient database
		IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
		IDataTransaction transaction = accessor.newTransaction();
		try
		{
      IDataTable dapplversionTable = accessor.getTable(Dapplversion.NAME);
      dapplversionTable.qbeClear();
			dapplversionTable.qbeSetKeyValue(Dapplversion.name, entry.getName());
			dapplversionTable.qbeSetKeyValue(Dapplversion.version, entry.getVersion().toString());
			dapplversionTable.searchAndDelete(transaction);
			transaction.commit();
		}
		finally
		{
			transaction.close();
		}
  }

  /**
   * Undeploys the given application version.
   * 
   * @param appName application name
   * @param version application version
   * @throws Exception on any problem
   */
  public static void undeploy(String appName, String version) throws Exception
  {
    // security check to prohibit to undeploy admin application
    if (ADMIN_APPLICATION_NAME.equals(appName))
      throw new RuntimeException("Could not undeploy admin application");
    
    synchronized (MUTEX)
    {
      DeployEntry oldEntry = getDeployEntry(appName, version);
      if (oldEntry != null)
      {
        if (logger.isInfoEnabled())
          logger.info("Undeploying " + appName + "-" + version + "..");
        
        // call all listeners, that the application has been undeployed.
        //
        Iterator iter = deployNotifyees.iterator();
        while (iter.hasNext())
        {
          DeployNotifyee notifyee = (DeployNotifyee) iter.next();
          try
          {
            notifyee.onUndeploy(oldEntry);
          }
          catch (Exception ex)
          {
            ExceptionHandler.handle(ex);
          }
        }

        // unregister application definition
        DeployMain.unregisterApplicationDefinition(oldEntry);
        
        // remove old class loader instance
        //
        ClassProvider.removeClassLoader(oldEntry);
        
        // delete old directory + jacapp file, if existing
        //
        File jacobDir = new File(oldEntry.getDeployPath());
        if (jacobDir.exists())
        {
          FileUtils.deleteDirectory(jacobDir);
        }
        File jacappFile = new File(oldEntry.getFile());
        if (jacappFile.exists())
        {
          jacappFile.delete();
        }

        removeTransientDeployInformation(oldEntry);
        
        // and remove deploy entry itself
        //
        synchronized (deployEntries)
        {
          // remove old entry first (if existing)
          deployEntries.remove(oldEntry);
        }
        
        if (logger.isInfoEnabled())
          logger.info("Undeployed " + appName + "-" + version);
      }
    }
  }
  
  private static IApplicationDefinition realDeploy(DeployEntry entry, String deployJacappPathName, String deployWebPathName, JarFile zf) throws Exception
  {
    File deployDir = new File(deployJacappPathName);
    File webDir = new File(deployWebPathName);
    
    synchronized (MUTEX)
    {
      try
      {
  			// notify all listeners, that the application is going to be redeployed
        //
        DeployEntry oldEntry = getDeployEntry(entry.getName(), entry.getVersion());
        if (oldEntry != null)
        {
          Iterator iter = deployNotifyees.iterator();
          while (iter.hasNext())
          {
            DeployNotifyee notifyee = (DeployNotifyee) iter.next();
            try
            {
              notifyee.beforeRedeploy(oldEntry);
            }
            catch (Exception ex)
            {
              ExceptionHandler.handle(ex);
            }
          }
        }
        
        // TODO: Folgende Schritte ben�tigen Zeit und es besteht die Gefahr von 
        // NoClassDefFoundError sofern eine Klasse noch nicht geladen war!!!


        // delete old directory + content, if existing
        if (deployDir.exists())
          FileUtils.deleteDirectory(deployDir);
        deployDir.mkdirs();
        
        // delete old directory + content, if existing
        if (webDir.exists())
          FileUtils.deleteDirectory(webDir);
        webDir.mkdirs();

        // unpack all files in the .jacapp file
        //
        Enumeration e = zf.entries();
        while (e.hasMoreElements())
        {
          ZipEntry ze = (ZipEntry) e.nextElement();
          if (!ze.isDirectory())
          {
            InputStream entryStream = zf.getInputStream(ze);
            try
            {
              File file = new File(deployJacappPathName + ze.getName());
              FileUtils.forceMkdir(file.getParentFile());
              FileOutputStream f = new FileOutputStream(file);
              try
              {
                Stream.copy(entryStream, f);
              }
              finally
              {
                f.close();
              }
            }
            finally
            {
              entryStream.close();
            }
          }
        }
        zf.close();
        
        // Den 'Web-Anteil' in das WebDirectory kopieren
        //
        String webDirectory = deployJacappPathName + EXPORT_DIRECTORY + File.separator;
        Iterator iter = Directory.getAll(new File(webDirectory), true).iterator();
        while (iter.hasNext())
        {
          File file = (File) iter.next();
          if (!file.isDirectory())
          {
            String path = file.getAbsolutePath();
            path = StringUtils.replace(path, webDirectory, deployWebPathName);
            FileUtils.copyFile(file, new File(path));
          }
        }

        // remove old class loader instance, if existing
        //
        ClassProvider.removeClassLoader(entry);

        // load application definition (i.e. design)
        //
        IApplicationDefinition appdef = DeployMain.loadApplicationDefinition(entry);

        // call all listeners, that the application has been (re)deployed.
        //
        iter = deployNotifyees.iterator();
        while (iter.hasNext())
        {
          DeployNotifyee notifyee = (DeployNotifyee) iter.next();
          try
          {
            if (oldEntry != null)
              notifyee.afterRedeploy(entry);
            else
              notifyee.onDeploy(entry);
          }
          catch (Exception ex)
          {
            if (entry.getStatus().isActive())
            {
              // abort immediately, e.g. scheduled tasks could not be started
              throw ex;
            }
              
            ExceptionHandler.handle(ex);
          }
        }

        // add the refreshed/deploy entry in the internal data set
        //
        synchronized (deployEntries)
        {
          // remove old entry first (if existing)
          if (oldEntry != null)
            deployEntries.remove(oldEntry);

          // then add new entry with new additional information
          deployEntries.add(entry);
        }

        return appdef;
      }
      catch (Throwable th)
      {
        // deployment has not been completed, i.e. partly failed

        // remove old entry first (if existing), i.e. the entry should not be
        // registered anymore
        synchronized (deployEntries)
        {
          deployEntries.remove(entry);
        }
        
        // get real cause to be displayed as deployment error in admin application
        if (th instanceof InvocationTargetException && th.getCause() != null)
          th = th.getCause();

        throw new Exception("Deployment aborted: " + th.toString(), th);
      }
    }
  }

  /**
   * Add an listener to the deployment. If an new/current application will be deployed/refreshed
   * the listener will be called
   * 
   * @param notifyee
   */
  public static void registerNotifyee(DeployNotifyee notifyee)
	{
    synchronized (MUTEX)
    {
      deployNotifyees.add(notifyee);
    }
  }
  
	/**
   * Checks whether the given application given by its definition is deployed
   * and active.
   * 
   * @param def
   *          the application definition to check
   * @return <code>true</code> application is active, otherwise
   *         <code>false</code>
   */
  public static boolean isActiveApplication(IApplicationDefinition def)
  {
    DeployEntry entry = getDeployEntry(def);
    return entry != null && entry.getStatus().isActive();
  }
	
	/**
	 * 
	 * @param def
	 * @return the deploy entry or <code>null</code>
	 */
  public static DeployEntry getDeployEntry(IApplicationDefinition def)
  {
    return getDeployEntry(def.getName(), def.getVersion());
  }
  
  public static DeployEntry getDeployEntry(String applName, String version)
  {
    return getDeployEntry(applName, Version.parseVersion(version));
  }
  
  public static DeployEntry getDeployEntry(String applName, Version version)
  {
    synchronized (deployEntries)
    {
      Iterator iter = deployEntries.iterator();
      while (iter.hasNext())
      {
        DeployEntry entry = (DeployEntry) iter.next();
        if (entry.getName().equals(applName) && entry.getVersion().isSameForDeployProcess(version))
        {
          return entry;
        }
      }
    }

    return null;
  }
	
	/**
   * Returns all deployed application entries in the jACOB application server
	 * @return List[de.tif.jacob.deployment.DeployEntry] 
	 */
	public static List getDeployEntries()
  {
	  // Note: This method will be called by means of the login page and
	  // the logon would be possible even in case a long lasting deploy
	  // procedure is processed at the same time. Therefore, this method
	  // should not be synchronized itself but the access to deployEntries
	  // must be!
    synchronized (deployEntries)
    {
      List entryList = new ArrayList(deployEntries.size());
      entryList.addAll(deployEntries);
      return entryList;
    }
  }
	
}
