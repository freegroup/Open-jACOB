/*
 * Created on 29.11.2008
 *
 */
package de.tif.jacob.report.birt;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IStatusHandler;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class RunAndRenderTask implements IRunAndRenderTask
{
  final IRunAndRenderTask task;
  final Connection connection;
  
  public RunAndRenderTask(IRunAndRenderTask task, Connection connection)
  {
    this.task= task;
    this.connection = connection;

    HashMap contextMap = new HashMap();

    //add the connection object to the map
    // WICHTIG: Damit dies funktioniert muss ein zusätzliches Plugin in der Birt
    // Runtime vorhanden sein: mysql-connector-java-3.1.8-bin.jar
    //
    contextMap.put( "org.eclipse.birt.report.data.oda.subjdbc.SubOdaJdbcDriver", connection );
    
    task.setAppContext( contextMap );
  }
  

  /**
   * Close the Connection after the "run".
   * Connection must be closed by the caller it self. 
   * 
   */
  public void run() throws EngineException
  {
    try
    {
      task.run();
    }
    finally
    {
      try
      {
        connection.close();
      }
      catch(java.sql.SQLException sqlExc)
      {
        throw new EngineException("Unable to close SQL-Connection",sqlExc);
      }
    }
  }
  
  
  public void addScriptableJavaObject(String arg0, Object arg1)
  {
    task.addScriptableJavaObject(arg0, arg1);
  }

  public void cancel()
  {
    task.cancel();
  }

  public void cancel(Object arg0)
  {
    task.cancel(arg0);
  }

  public void close()
  {
    task.close();
  }

  public Map getAppContext()
  {
    return task.getAppContext();
  }

  public boolean getCancelFlag()
  {
    return task.getCancelFlag();
  }

  public IReportEngine getEngine()
  {
    return task.getEngine();
  }

  public List getErrors()
  {
    return task.getErrors();
  }

  public int getID()
  {
    return task.getID();
  }

  public Locale getLocale()
  {
    return task.getLocale();
  }

  public Logger getLogger()
  {
    return task.getLogger();
  }

  public String getParameterDisplayText(String arg0)
  {
    return task.getParameterDisplayText(arg0);
  }

  public Object getParameterValue(String arg0)
  {
    return task.getParameterValue(arg0);
  }

  public HashMap getParameterValues()
  {
    return task.getParameterValues();
  }

  public IRenderOption getRenderOption()
  {
    return task.getRenderOption();
  }

  public IReportRunnable getReportRunnable()
  {
    return task.getReportRunnable();
  }

  public int getStatus()
  {
    return task.getStatus();
  }

  public int getTaskType()
  {
    return task.getTaskType();
  }

  public ULocale getULocale()
  {
    return task.getULocale();
  }


  public void setAppContext(Map arg0)
  {
    task.setAppContext(arg0);
  }

  public void setDataSource(IDocArchiveReader arg0, String arg1)
  {
    task.setDataSource(arg0, arg1);
  }

  public void setDataSource(IDocArchiveReader arg0)
  {
    task.setDataSource(arg0);
  }

  public void setEmitterID(String arg0)
  {
    task.setEmitterID(arg0);
  }

  public void setErrorHandlingOption(int arg0)
  {
    task.setErrorHandlingOption(arg0);
  }

  public void setLocale(Locale arg0)
  {
    task.setLocale(arg0);
  }

  public void setLocale(ULocale arg0)
  {
    task.setLocale(arg0);
  }

  public void setLogger(Logger arg0)
  {
    task.setLogger(arg0);
  }

  public void setMaxRowsPerQuery(int arg0)
  {
    task.setMaxRowsPerQuery(arg0);
  }

  public void setPageHandler(IPageHandler arg0)
  {
    task.setPageHandler(arg0);
  }

  public void setParameter(String arg0, Object arg1, String arg2)
  {
    task.setParameter(arg0, arg1, arg2);
  }

  public void setParameterDisplayText(String arg0, String arg1)
  {
    task.setParameterDisplayText(arg0, arg1);
  }

  public void setParameterValue(String arg0, Object arg1)
  {
    task.setParameterValue(arg0, arg1);
  }

  public void setParameterValues(Map arg0)
  {
    task.setParameterValues(arg0);
  }

  public void setRenderOption(IRenderOption arg0)
  {
    task.setRenderOption(arg0);
  }

  public void setTimeZone(TimeZone arg0)
  {
    task.setTimeZone(arg0);
  }

  public boolean validateParameters()
  {
    return task.validateParameters();
  }


  public void setProgressMonitor(IProgressMonitor arg0)
  {
  
    
  }


  public void setStatusHandler(IStatusHandler arg0)
  {

    
  }


  public void setUserACL(String[] arg0)
  {

    
  }
  
}
