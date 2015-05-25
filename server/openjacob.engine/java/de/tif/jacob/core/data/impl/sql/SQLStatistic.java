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

package de.tif.jacob.core.data.impl.sql;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.Context;

/**
 * Class to provide SQL statistics.
 * 
 * @author Andreas Sonntag
 * @since 2.8.5
 */
public final class SQLStatistic
{
  static public transient final String RCS_ID = "$Id: SQLStatistic.java,v 1.2 2011/02/15 08:13:56 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";

  private int numberOfUpdateRequests = 0;
  private int numberOfQueryRequests = 0;
  private int numberOfOtherRequests = 0;
  private long maxRequestMilliseconds = 0;
  private long totalRequestMilliseconds = 0;

  private List<String> sqlCommands = new ArrayList<String>();
  
  private static final String STATISTIC_FOR_REQUEST_PROPERTY = SQLStatistic.class.getName() + ":ForRequest";

  /**
   * Returns the SQL statistic for the current client request.
   * 
   * @param context
   *          the context
   * @return the SQL statistic
   */
  public static SQLStatistic getStatisticForRequest(Context context)
  {
    SQLStatistic statistic = (SQLStatistic) context.getProperty(STATISTIC_FOR_REQUEST_PROPERTY);
    if (statistic == null)
    {
      statistic = new SQLStatistic();
      context.setPropertyForRequest(STATISTIC_FOR_REQUEST_PROPERTY, statistic);
    }
    return statistic;
  }

  /**
   * Returns the number of milliseconds of the longest lasting request.
   * 
   * @return the maxRequestMilliseconds
   */
  public long getMaxRequestMilliseconds()
  {
    return maxRequestMilliseconds;
  }

  /**
   * Returns the total number of milliseconds which have been elapsed during
   * execution of all SQL requests.
   * 
   * @return the totalRequestMilliseconds
   */
  public long getTotalRequestMilliseconds()
  {
    return totalRequestMilliseconds;
  }

  /**
   * Returns the total number of SQL requests.
   * 
   * @return total number of SQL requests
   */
  public int getTotalNumberOfRequests()
  {
    return numberOfOtherRequests + numberOfUpdateRequests + numberOfQueryRequests;
  }

  protected void incNumberOfOtherRequests(long executionTime)
  {
    this.numberOfOtherRequests++;
    if (executionTime > this.maxRequestMilliseconds)
      this.maxRequestMilliseconds = executionTime;
    this.totalRequestMilliseconds += executionTime;
  }

  /**
   * Returns the number of SQL update requests, i.e. insert, update and delete
   * requests.
   * 
   * @return number of SQL update requests
   */
  public int getNumberOfUpdateRequests()
  {
    return numberOfUpdateRequests;
  }

  protected void incNumberOfUpdateRequests(long executionTime)
  {
    this.numberOfUpdateRequests++;
    if (executionTime > this.maxRequestMilliseconds)
      this.maxRequestMilliseconds = executionTime;
    this.totalRequestMilliseconds += executionTime;
  }

  /**
   * Returns the number of SQL query requests.
   * 
   * @return number of SQL query requests
   */
  public int getNumberOfQueryRequests()
  {
    return numberOfQueryRequests;
  }

  protected void incNumberOfQueryRequests(long executionTime)
  {
    this.numberOfQueryRequests++;
    if (executionTime > this.maxRequestMilliseconds)
      this.maxRequestMilliseconds = executionTime;
    this.totalRequestMilliseconds += executionTime;
  }
  
  public List<String> getCommands()
  {
    return this.sqlCommands;
  }
  
  /**
   * @since 3.0
   */
  public void log(String sqlCommand)
  {
    this.sqlCommands.add(sqlCommand);
  }
}