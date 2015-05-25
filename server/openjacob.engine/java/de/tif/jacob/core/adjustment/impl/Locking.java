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

package de.tif.jacob.core.adjustment.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.adjustment.ILocking;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.definition.impl.AbstractTableDefinition;
import de.tif.jacob.core.exception.RecordLockedException;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class Locking implements ILocking
{
  static public final transient String RCS_ID = "$Id: Locking.java,v 1.2 2009/03/06 15:32:06 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  protected static final Log logger = LogFactory.getLog(Locking.class);

  // hack: since we could not get the real pid (process id), use a magic number
  // which is very unlikely to be identical for different processes on the same
  // host.
  private static final int pid = (int) (System.currentTimeMillis() % 1000000);

  public static final Locking NO_LOCKING = new NoLocking();
  
  public static Locking get(String className)
  {
    if (null == className || "".equals(className))
      return NO_LOCKING;

    try
    {
      Class clazz = Class.forName(className);
      return (Locking) clazz.newInstance();
    }
    catch (Exception ex)
    {
      logger.fatal("Getting locking implementation " + className + " failed!", ex);
      throw new RuntimeException(ex.toString());
    }
  }

  private static int getPid()
  {
    return pid;
  }

  protected static String getHostname() throws UnknownHostException
  {
    // create pseudo hostname to ensure different hostnames for different engine
    // instances on the
    // same host.
    return InetAddress.getLocalHost().getHostName() + getPid();
  }
  
  /**
   * Da u.a. mySQL beim Einfügen von zu langen Tabellennamen (größer 30 Zeichen)
   * in die Locktabelle diesen einfach abschneidet, tun wird dieses auf eine
   * intelligentere Art selbst, damit der Lock beim Entfernen auch wieder
   * gefunden wird.
   * 
   * @param tableDBName
   *          database table name
   * @return the adjusted (i.e. max length 30) table name
   */
  protected static String adjustDBTablename4Locktable(String tableDBName)
  {
    if (tableDBName.length() > AbstractTableDefinition.MAX_TABLE_DBNAME_LENGTH)
    {
      String postfix = Integer.toString(Math.abs(tableDBName.hashCode()) % 10000);
      return tableDBName.substring(0, AbstractTableDefinition.MAX_TABLE_DBNAME_LENGTH - 1 - postfix.length()) + "$" + postfix;
    }
    return tableDBName;
  }
  
  public static void main(String[] args)
  {
    String[] dbTablenames = {
        "documenthistory_prueffproto", //
        "documenthistory_prueffprotokol", //
        "documenthistory_prueffprotokoll", //
        "documenthistory_prueffprotokol1", //
        "documenthistory_prueffprotokol2", //
        "subproject_to_vendor_responsible"};
    for (int i = 0; i < dbTablenames.length; i++)
    {
      System.out.println("\"" + dbTablenames[i] + "\" --> \"" + adjustDBTablename4Locktable(dbTablenames[i]) + "\" (Hash: "+dbTablenames[i].hashCode()+")");
    }
  }
  
  public static class RecordLock implements ILocking.IRecordLock
  {
    private final String tablename;
    private final String keyvalue;
    private final String nodename;
    private final String userid;
    private final String username;
    private final Date created;
    
    public RecordLock(String tablename, String keyvalue, Date created, String nodename, String userid, String username)
    {
      this.tablename = tablename;
      this.keyvalue = keyvalue;
      this.nodename = nodename;
      this.userid = userid;
      this.username = username;
      this.created = created;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.adjustment.ILocking.IRecordLock#created()
     */
    public Date created()
    {
      return this.created;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.adjustment.ILocking.IRecordLock#getKeyValue()
     */
    public String getKeyValue()
    {
      return this.keyvalue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.adjustment.ILocking.IRecordLock#getNodeName()
     */
    public String getNodeName()
    {
      return this.nodename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.adjustment.ILocking.IRecordLock#getTableName()
     */
    public String getTableName()
    {
      return this.tablename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.adjustment.ILocking.IRecordLock#getUserId()
     */
    public String getUserId()
    {
      return this.userid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.adjustment.ILocking.IRecordLock#getUserName()
     */
    public String getUserName()
    {
      return this.username;
    }
  }

  private static class NoLocking extends Locking
  {
    /* (non-Javadoc)
     * @see de.tif.jacob.core.adjustment.ILocking#doLocking(de.tif.jacob.core.data.impl.DataSource, de.tif.jacob.core.data.IDataRecordId)
     */
    public boolean doLocking(DataSource dataSource, IDataRecordId recordId)
    {
      return false;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.adjustment.ILocking#checkLocked(de.tif.jacob.core.data.IDataTransaction, de.tif.jacob.core.data.impl.DataSource, de.tif.jacob.core.data.IDataRecordId)
     */
    public void checkLocked(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws RecordLockedException, RuntimeException
    {
      // do nothing here
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.adjustment.ILocking#lock(de.tif.jacob.core.data.IDataTransaction,
     *      de.tif.jacob.core.data.IDataSource,
     *      de.tif.jacob.core.data.IDataRecordId)
     */
    public void lock(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws RecordLockedException, RuntimeException
    {
      throw new UnsupportedOperationException("Locking is not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.adjustment.ILocking#unlock(de.tif.jacob.core.data.IDataTransaction,
     *      de.tif.jacob.core.data.IDataSource,
     *      de.tif.jacob.core.data.IDataRecordId)
     */
    public void unlock(IDataTransaction transaction, DataSource dataSource, IDataRecordId recordId) throws Exception
    {
      throw new UnsupportedOperationException("Locking is not supported");
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.adjustment.ILocking#getCurrentLocks(de.tif.jacob.core.data.impl.DataSource)
     */
    public List getCurrentLocks(DataSource dataSource)
    {
      return Collections.EMPTY_LIST;
    }
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.adjustment.ILocking#removeLock(de.tif.jacob.core.data.impl.DataSource, java.lang.String, java.lang.String)
     */
    public void removeLock(DataSource dataSource, String tablename, String keyvalue)
    {
      // do nothing here
    }
  }
}
