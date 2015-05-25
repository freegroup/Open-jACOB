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

package de.tif.jacob.core.exception;

import java.io.ByteArrayOutputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.iterators.SingletonIterator;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.HTTPForm;
import de.tif.jacob.screen.impl.HTTPSingleDataGuiElement;

/**
 * Exception handler class which should be used to report caught throwables in a
 * well-defined way.
 * 
 * @author Andreas Herz
 */
public final class ExceptionHandler
{
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.14 $";

  static private final transient Log logger = LogFactory.getLog(ExceptionHandler.class);

  /**
   * Reports the caught throwable in the internal database and shows the current
   * user an error dialog.
   * 
   * @param context
   *          the current session context
   * @param th
   *          The throwable to reported
   */
  public static void handle(IClientContext context, Throwable th)
  {
    showErrorMessage(context, handle(th), th);
  }
  
  /**
   * Shows the current user an error dialog.
   * 
   * @param context
   *          the current session context
   * @param th
   *          The throwable to reported
   */
  public static void handleNoReport(IClientContext context, Throwable th)
  {
    showErrorMessage(context, handle(th, false), th);
  }
  
  /**
   * Handles the caught throwable in a smart way, i.e. depending on the type
   * of the throwable an error is reported or an information message is shown to
   * the current user.
   * 
   * @param context
   *          the current session context
   * @param th
   *          The throwable to reported
   */
  public static void handleSmart(IClientContext context, Throwable th)
  {
    Throwable cause = th.getCause();
    if(cause!=null && cause!=th && th instanceof RuntimeException && (cause instanceof UserException|| cause instanceof UserRuntimeException))
      th = cause;
    
    if (th instanceof TableFieldExceptionCollection)
    {      
      TableFieldExceptionCollection tablefieldExColl = (TableFieldExceptionCollection) th;
      
      IForm form = context.getForm();
      if(form instanceof HTTPForm)
      {
        HTTPForm httpForm = (HTTPForm)form;
        Iterator iter = tablefieldExColl.getExceptions().iterator();
        while(iter.hasNext())
        {
          ITableFieldException fieldException = (ITableFieldException) iter.next();
          DataField[] fields = httpForm.getDataFields();
          for (int i = 0; i < fields.length; i++)
          {
            DataField field = fields[i];
            if (field.getField().equals(fieldException.getTableField()))
            {
              HTTPSingleDataGuiElement guiElement = (HTTPSingleDataGuiElement) field.getParent();
              guiElement.setErrorDecoration(context, fieldException.getMessage());
            }
          }
        }
      }
      context.createMessageDialog(tablefieldExColl.getMessage(), tablefieldExColl.getDetails()).show();
      return;
    }
    else if (th instanceof ITableFieldsException)
    {      
      ITableFieldsException tablefieldsEx = (ITableFieldsException) th;

      IForm form = context.getForm();
      if (form instanceof HTTPForm)
      {
        HTTPForm httpForm = (HTTPForm) form;
        Iterator iter = tablefieldsEx.getTableFields().iterator();
        while (iter.hasNext())
        {
          ITableField field = (ITableField) iter.next();
          DataField[] datafields = httpForm.getDataFields();
          for (int i = 0; i < datafields.length; i++)
          {
            DataField datafield = datafields[i];
            if (datafield.getField().equals(field))
            {
              HTTPSingleDataGuiElement guiElement = (HTTPSingleDataGuiElement) datafield.getParent();
              guiElement.setErrorDecoration(context, tablefieldsEx.getMessage());
            }
          }
        }
      }
      context.createMessageDialog(tablefieldsEx.getMessage(), tablefieldsEx.getDetails()).show();
      return;
    }
    else if (th instanceof ITableFieldException)
    {      
      ITableFieldException userEx = (ITableFieldException) th;
      // 1.) Von der derzeitigen Form die DataFields geben lassen.
      // 2.) Von diesen dann die finden, welche auf das TableField passen.
      // 3.) Von den gefundenen DataFields den Parent geben lassen => GUI Element.
      // 4.) Fehlerstatus setzen.
      IForm form = context.getForm();
      if(form instanceof HTTPForm)
      {
        HTTPForm httpForm = (HTTPForm)form;
        DataField[] fields= httpForm.getDataFields();
        for(int i=0;i<fields.length;i++)
        {
          DataField field = fields[i];
          if(field.getField().equals(userEx.getTableField()))
          {
            HTTPSingleDataGuiElement guiElement =(HTTPSingleDataGuiElement)field.getParent();
            guiElement.setErrorDecoration(context, userEx.getMessage());
          }
        }
      }
      context.createMessageDialog(userEx.getMessage(), userEx.getDetails()).show();
      return;
    }
    if (th instanceof UserException)
    {      
      UserException userEx = (UserException) th;
      context.createMessageDialog(userEx.getMessage(), userEx.getDetails()).show();
      return;
    }
    if (th instanceof UserRuntimeException)
    {      
      UserRuntimeException userEx = (UserRuntimeException) th;
      context.createMessageDialog(userEx.getMessage(), userEx.getDetails()).show();
      return;
    }
    if (th instanceof RequestCanceledException)
    {
      if (logger.isInfoEnabled())
        logger.info("Request has been canceled");

      IMessageDialog dialog = context.createMessageDialog(new CoreMessage(CoreMessage.REQUEST_CANCELED_BY_USER));
      dialog.show();
      return;
    }
    
    // exception is handled as an error
    handle(context, th);
  }
  
  /**
   * Reports the caught throwable in the internal database. There is no feedback
   * to the user.
   * 
   * @param th
   *          The throwable to reported
   */
  public static String handle(Throwable th)
  {
    return handle(th, true);
  }
  
  private static String handle(Throwable th, boolean dologging)
  {
    try
    {
      if (th instanceof RequestCanceledException)
      {
        String message = "Request has been canceled";
        if (dologging)
          logger.info(message);

        return message;
      }

      String message = getExtendedExceptionReport(th);
      if (dologging)
        logger.error(message);
      return message;
    }
    catch (Exception handlerEx)
    {
      // 
      // ensure that this method NEVER throws an exception itself!!!
      
      // report the reason of the handler exception  
      try
      {
        logger.warn("Exception in ExceptionHandler", handlerEx);
      }
      catch (Exception ex)
      {
        // ignore
      }

      // try to report the original exception as best as possible
      try
      {
        logger.error("Original exception", th);
        return th.toString();
      }
      catch (Exception toStringException)
      {
        // just in case e.toString() throws an exception itself (which might be possible!)
        return "Unknown exception";
      }
    }
    
    // tryToSendErrorReport(message);
  }
 
  
  /**
   * 
   * @param context
   * @param e
   */
  private static void showErrorMessage(IClientContext context, String message, Throwable e)
  {
    final FormLayout layout= new FormLayout("10dlu,grow,10dlu", // columns
                                            "20dlu,p,5dlu,grow,20dlu");   // rows
    IFormDialog dialog = context.createFormDialog(I18N.getCoreLocalized("DIALOG_TITLE_ERROR_MESSAGE", context), layout, null);
    CellConstraints cc=new CellConstraints();
    dialog.addLabel(I18N.getCoreLocalized("LABEL_ERROR_ERROR_OCCURRED", context),cc.xywh(1,1,2,1));
    dialog.addTextArea("dummy", message, true, cc.xy(1,3));
    dialog.setCancelButton(I18N.getCoreLocalized("BUTTON_COMMON_CLOSE", context));
//    dialog.setDebug(false);
    dialog.show(500,350);
  }
  
  
  /**
   * send the error report via mail interface
   * 
   * @param body
   */
  
 
  private static String getExtendedExceptionReport(Throwable th)
  {
    // determine cause
    //
    Throwable cause = th;
    while (cause.getCause() != null)
    {
      cause = cause.getCause();
    }

  	StringBuffer report = new StringBuffer(512);
    
  	report.append(SystemUtils.LINE_SEPARATOR);
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append("==== Source Exception ===========================").append(SystemUtils.LINE_SEPARATOR);
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append("\tName: ").append(th.getClass()).append(SystemUtils.LINE_SEPARATOR);
    report.append("\tMessage: ").append(th.getMessage() == null ? "" : th.getMessage()).append(SystemUtils.LINE_SEPARATOR);
    Iterator fieldIter = IteratorUtils.EMPTY_ITERATOR; 
    if (th instanceof ITableFieldsException)
      fieldIter = ((ITableFieldsException) th).getTableFields().iterator();
    else if (th instanceof ITableFieldException)
      fieldIter = new SingletonIterator(((ITableFieldException) th).getTableField());
    if (fieldIter.hasNext())
    {
      report.append("\tDB-Fields: ");
      while (fieldIter.hasNext())
      {
        ITableField field = (ITableField) fieldIter.next();
        report.append(field.toString() + "; ");
      }
      report.append(SystemUtils.LINE_SEPARATOR);
    }
    if (th instanceof UserException && ((UserException) th).getDetails() != null)
    {
      report.append("\tDetails: ").append(((UserException) th).getDetails()).append(SystemUtils.LINE_SEPARATOR);
    }
    else if (th instanceof UserRuntimeException && ((UserRuntimeException) th).getDetails() != null)
    {
      report.append("\tDetails: ").append(((UserRuntimeException) th).getDetails()).append(SystemUtils.LINE_SEPARATOR);
    }

    if (th != cause)
    {
      report.append("\tCause: ").append(cause.getClass()).append(SystemUtils.LINE_SEPARATOR);
      report.append("\tMessage of cause: ").append(cause.getMessage() == null ? "" : cause.getMessage()).append(SystemUtils.LINE_SEPARATOR);
    }
   
    report.append("\tEngine: ").append(Version.ENGINE).append(SystemUtils.LINE_SEPARATOR);
    report.append(SystemUtils.LINE_SEPARATOR);
    
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append("=== Additional Application information ==========").append(SystemUtils.LINE_SEPARATOR);
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    Context context = Context.getCurrent();
    report.append("\tUser: ").append(context.getUser().getLoginId()).append(SystemUtils.LINE_SEPARATOR);
  	try
    {
      // to avoid exception to be thrown
      if (context.hasApplicationDefinition())
      {
        IApplicationDefinition applDef = context.getApplicationDefinition();
        report.append("\tApplication.name: ").append(applDef.getName()).append(SystemUtils.LINE_SEPARATOR);
        report.append("\tApplication.version: ").append(applDef.getVersion()).append(SystemUtils.LINE_SEPARATOR);
        report.append("\tApplication.title: ").append(applDef.getTitle()).append(SystemUtils.LINE_SEPARATOR);
      }
    }
    catch (Throwable e)
    {
      /* ignore to be on the safe side :-) */
    }
  	if (context instanceof IClientContext)
    {
  	  IClientContext cc = (IClientContext) context;
      if(cc.getDomain()!=null)
      {
        report.append("\tDomain.name: ").append(cc.getDomain().getName()).append(SystemUtils.LINE_SEPARATOR);
        report.append("\tDomain.label: ").append(I18N.localizeLabel(cc.getDomain().getLabel(), context, context.getApplicationLocale())).append(SystemUtils.LINE_SEPARATOR);
      }
      if(cc.getForm()!=null)
      {
        report.append("\tForm.name: ").append(cc.getForm().getName()).append(SystemUtils.LINE_SEPARATOR);
        report.append("\tForm.label: ").append(I18N.localizeLabel(cc.getForm().getLabel(), context, context.getApplicationLocale())).append(SystemUtils.LINE_SEPARATOR);
      }
  	}
    report.append(SystemUtils.LINE_SEPARATOR);
    
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append("=== Inheritance of the Emitter class ============").append(SystemUtils.LINE_SEPARATOR);
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append(getExtendedInheritanceInfo(cause));
    report.append(SystemUtils.LINE_SEPARATOR);
    
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append("=== Extended Stacktrace with CVS version ID =====").append(SystemUtils.LINE_SEPARATOR);
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append(getExtendedStackTrace(th));
    report.append(SystemUtils.LINE_SEPARATOR);
    
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append("=== jACOB Engine Data ===========================").append(SystemUtils.LINE_SEPARATOR);
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    try
    { // avoid exception
      report.append("\tVersion: ").append(Version.ENGINE_NAME).append(" ").append(Version.ENGINE).append(SystemUtils.LINE_SEPARATOR);
      report.append("\tWeb App Name: ").append(Bootstrap.getApplicationName()).append(SystemUtils.LINE_SEPARATOR);
      report.append("\tInstall Dir: ").append(Bootstrap.getApplicationRootPath()).append(SystemUtils.LINE_SEPARATOR);
      report.append("\tBootstrap.isOk: ").append(Bootstrap.isOk()).append(SystemUtils.LINE_SEPARATOR);
    }
    catch(Throwable th2)
    {
      //ignore
    }
    report.append(SystemUtils.LINE_SEPARATOR);

    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
    report.append("=== Java Environment of jACOB ===================").append(SystemUtils.LINE_SEPARATOR);
    report.append("=================================================").append(SystemUtils.LINE_SEPARATOR);
		report.append(getRuntimeEnviroment());
    
    return report.toString();
  }
  
  private static String getRuntimeEnviroment()
  {
		StringWriter sw = new StringWriter(2048);
    PrintWriter writer=new PrintWriter(sw);
    System.getProperties().list(writer);
    return sw.toString();
  }
  
  /**
   * Creates an extended stack trace out of the given throwable. The extended
   * stack trace contains revision information concerning the involved classes
   * as far as possible.
   * <p>
   * Example extended stack trace:
   * 
   * <pre>
   * 
   *  at de.tif.jacob.util.FastStringWriter.write(FastStringWriter.java:225)  ($Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $)
   *  at de.tif.jacob.screen.html.LongText.calculateHTML(LongText.java:120)  ($Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $)
   *  at de.tif.jacob.screen.html.GuiHtmlElement.calculateHTML(GuiHtmlElement.java:288)  ($Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $)
   *  at de.tif.jacob.screen.html.Group.calculateHTML(Group.java:172)  ($Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $)
   *  at de.tif.jacob.screen.html.GuiHtmlElement.calculateHTML(GuiHtmlElement.java:288)  ($Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $)
   *  at de.tif.jacob.screen.html.Form.calculateHTML(Form.java:233)  ($Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $)
   *  at de.tif.jacob.screen.html.Domain.calculateHTML(Domain.java:213)  ($Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $)
   *  at de.tif.jacob.screen.html.Application.calculateHTML(Application.java:208)  ($Id: ExceptionHandler.java,v 1.14 2010/06/23 20:43:45 freegroup Exp $)
   *  at org.apache.jsp.ie_005fcontent_jsp._jspService(ie_005fcontent_jsp.java:409)
   *  at org.apache.jasper.runtime.HttpJspBase.service(HttpJspBase.java:94)
   *  at javax.servlet.http.HttpServlet.service(HttpServlet.java:802)
   *  
   * </pre>
   * 
   * @param th
   *          the throwable to create an extended stack trace of
   * @return the extended stack trace
   */
  public static String getExtendedStackTrace(Throwable th)
  {
  	StringBuffer stackTrace = new StringBuffer(4096);
    try
    {
      // Retrieve call stack
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      PrintWriter outWriter = new PrintWriter( outStream );
      th.printStackTrace( outWriter );
      outWriter.flush();

      LineNumberReader lineNumberReader = new LineNumberReader( new StringReader( outStream.toString() ) );

      String line;
      while(null != (line = lineNumberReader.readLine()))
      {
        if(!line.startsWith("\tat "))
        {
          stackTrace.append(line);
          stackTrace.append(SystemUtils.LINE_SEPARATOR);
          continue;
        }
        
        stackTrace.append(line);

        // try to detect the CVS tag for the version id 
        //
        String className = line.substring(4);
        className = className.substring( 0, className.lastIndexOf( ".", className.indexOf( "(" ) ) );
        try
        {
          Class clazz = getClass(className);
          if (clazz != null)
          {
            String rcs = clazz.getField("RCS_ID").get(clazz).toString();
            stackTrace.append("  (").append(rcs).append(")");
          }
        }
        catch (Throwable x)
        {
          /* ignore */
        }
        
        stackTrace.append(SystemUtils.LINE_SEPARATOR);
      }
    }
    catch( Exception e )
    {
      System.err.println( "ERROR: Failed to analyse call stack for class name: " + e );
      e.printStackTrace();
    }
    return stackTrace.toString();
  }
  
  /**
   * 
   * @param exception
   * @return
   */
  private static Class getEmitterClass(Throwable exception)
  {
    try
    {
      // Retrieve call stack
      /*
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      PrintWriter outWriter = new PrintWriter( outStream );
      exception.printStackTrace( outWriter );
      outWriter.flush();

      // Retrieve class name from call stack.
      LineNumberReader lineNumberReader = new LineNumberReader( new StringReader( outStream.toString() ) );
      lineNumberReader.readLine();// Skip exception header
//      lineNumberReader.readLine();// Skip at least one call stack entry
      //    lineNumberReader.readLine();// Skip at least one call stack entry
      String line;
      line =lineNumberReader.readLine();
      // the first line of the stack trace can be empty
      //
      if(line==null || line.trim().length()==0)
        line = lineNumberReader.readLine();
      
      className = line.substring( 4 );
      System.out.println(className);
      className = className.substring( 0, className.lastIndexOf( ".", className.indexOf( "(" ) ) );
      //    className = className.substring( 0, className.lastIndexOf( ".") );
      return Class.forName(className);
      */
      StackTraceElement[] elements = exception.getStackTrace();
      if(elements==null || elements.length==0)
      {
        exception.fillInStackTrace();
        elements = exception.getStackTrace();
      }
      if(elements.length>0)
        return getClass(exception.getStackTrace()[0].getClassName());
      return null;
    }
    catch (Throwable th)
    {
      if (logger.isWarnEnabled())
        logger.warn("Failed to get emitter class", th);
    }
    return null;
  }
  
  private static Class getClass(String className)
  {
    try
    {
      return Class.forName(className);
    }
    catch (ClassNotFoundException e)
    {
      return getApplicationClass(className);
    }
    catch (NoClassDefFoundError e)
    {
      return getApplicationClass(className);
    }
  }
  
  private static Class getApplicationClass(String className)
  {
    // try to find the class from the *.jacapp file
    //
    Context context = Context.getCurrent();
    if (context.hasApplicationDefinition())
    {
      Object obj = ClassProvider.getInstance(context.getApplicationDefinition(), className);
      if (obj != null)
        return obj.getClass();
    }
    return null;
  }

  private static String getExtendedInheritanceInfo(Throwable th)
  {
    StringBuffer info = new StringBuffer(256);
    Class emitter = ExceptionHandler.getEmitterClass(th);
    while(emitter!=null)
    {
      info.append("\t");
      info.append(emitter.getName());
      try
      {
        String rcs = emitter.getField("RCS_ID").get(emitter).toString();
        info.append("  (").append(rcs).append(")");
      }
      catch (Exception x){/* ignore */}
      info.append(SystemUtils.LINE_SEPARATOR);
      emitter = emitter.getSuperclass();
    }
    return info.toString();
   }
}
