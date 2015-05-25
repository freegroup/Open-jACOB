package jacob.event.ui.{modulename}_files;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import de.freegroup.jacobimporter.model.ConverterModel;
import de.freegroup.jacobimporter.model.IConverterLogger;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;

public final class ExcelImporter implements IConverterLogger
{
  private StringBuffer logger = new StringBuffer();
  private boolean hasErrors = false;
  private boolean hasWarnings = false;

  final String docName;
  final byte[] docContent;
  
  public ExcelImporter(String docName, byte[] docContent) throws Exception
  {
    this.docName = docName;
    this.docContent = docContent;
  }
  
  
  /**
   * Falls als Transaction "null" �bergeben wird ist das read nur im Testmodus.
   * Es wird versucht die Daten zu parsen und in die Datenbank zu schreiben.
   * Es wird jedoch kein Commit durchgef�hrt.
   * @param context
   * @param data
   * @param transactionImageImporter
   * @throws Exception
   */
  public void read(IClientContext context, String name, InputStream data, String xml,  IDataTransaction transaction) throws Exception
  {
    boolean closeTrans = false;
    if(transaction==null)
    {
      transaction = context.getDataAccessor().newTransaction();
      closeTrans = true;
    }
    
    try
    {
    	// import Code
    	ConverterModel converter = new ConverterModel(new StringBufferInputStream(xml));
    	converter.setLogger(this);
    	converter.importData(context, transaction, data);
    }
    finally
    {
      if(closeTrans==true)
        transaction.close();
    }
  }

  public String getLog()
  {
    return logger.toString();
  }

  public void resetLog()
  {
    this.hasErrors = false;
    this.hasWarnings = false;
    this.logger = new StringBuffer();
  }

  public void log_debug(String message)
  {
    logger.append("DEBUG: ");
    logger.append(message);
    logger.append("\n");
  }

  public void log_info(String message)
  {
//    logger.append("INFO: ");
    logger.append(message);
    logger.append("\n");
  }

  public void log_warn(String message)
  {
    logger.append("WARN: ");
    logger.append(message);
    logger.append("\n");
    this.hasWarnings = true;
  }

  public void log_error(String message)
  {
    logger.append("ERROR: ");
    logger.append(message);
    logger.append("\n");
    this.hasErrors = true;
  }

  public boolean hasErrors()
  {
    return this.hasErrors;
  }

  public boolean hasWarnings()
  {
    return this.hasWarnings;
  }
}
