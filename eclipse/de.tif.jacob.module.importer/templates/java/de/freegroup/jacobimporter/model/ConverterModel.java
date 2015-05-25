package de.freegroup.jacobimporter.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlOptions;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTransaction;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import noNamespace.ModelDocument;
import noNamespace.ConnectionsDocument.Connections;
import noNamespace.ElementDocument.Element;
import noNamespace.ModelDocument.Model;

public class ConverterModel implements AbstractModel, IConverterLogger
{
  Model model;
  ModelDocument empDoc;
  ExcelSourceModel sourceModel;

  IConverterLogger logger = null;
  
  List<RecordSourceModel> recordSourceModels = new ArrayList<RecordSourceModel>();
  List<ConnectionModel> connections = new ArrayList<ConnectionModel>();
  Set<ColumnModel> alreadyVisitiedColumns = new HashSet<ColumnModel>();
  
  public ConverterModel(File xml) throws Exception
  {
	  InputStream in = new FileInputStream(xml);
	  try
	  {
		  load(in);
	  }
	  finally
	  {
		  in.close();
	  }
  }
  
  public ConverterModel(InputStream stream) throws Exception
  {
	this.load(stream);  
  }
  
  public void setLogger(IConverterLogger logger)
  {
     this.logger = logger;
  }
  
  private void load(InputStream xml) throws Exception
  {
     // Bind the instance to the generated XMLBeans types.
     empDoc = ModelDocument.Factory.parse(xml);
     
    // Get and print pieces of the XML instance.
    this.model = empDoc.getModel();
    Element[] elements = model.getRecordSourceModels().getData().getElementArray();
    // Alle Elemente aus dem Dokument holen und zwischenspeichern
    //
    RecordSourceModel child;
    for (Element element : elements)
    {
      if("source".equals(element.getProviderType().getDomNode().getFirstChild().getNodeValue()))
      {
    	  child = new ExcelSourceModel(this,element);
          this.sourceModel = (ExcelSourceModel)child;
      }
      else
      {
    	  child = new DataTableSourceModel(this,element);
      }
      recordSourceModels.add(child);
      Element[] cons = element.getConnections().getData().getElementArray();
      for (Element connection : cons)
      {
        this.connections.add(new ConnectionModel(this, connection));
      }
    }
  }
  
//  public String getXml() throws Exception
//  {
//     StringWriter w = new StringWriter();
//     empDoc.save(w);
//     return w.getBuffer().toString();
//  }
  public ExcelSourceModel getSourceModel() 
  {
	return sourceModel;
  }

  public List<RecordSourceModel> getRecordSourceModels()
  {
    return recordSourceModels;
  }

  /**
   * gibt TRUE zurÃ¼ck wenn die Column noch nicht angefragt wurde.
   * @param model
   * @return
   */
  public boolean visit(ColumnModel model)
  {
    return alreadyVisitiedColumns.add(model);
  }
 
  public void resetValue(Context context)
  {
    // Über alle element laufen und versuchen die Eingangsports mit den neuen Werten zu lesen
    // 
    for (RecordSourceModel source : this.recordSourceModels)
    {
      source.resetValue(context);
    }
    this.alreadyVisitiedColumns.clear();
  }

  public ConnectionModel getConnectionByTarget(ColumnModel targetColumn)
  {
    for (ConnectionModel connection : this.connections)
    {
      if(connection.getTargetColumn()==targetColumn)
        return connection;
    }
    
    return null;
  }
  
  public void importData(Context context, IDataTransaction trans, InputStream excelInputStream) throws Exception
  {
	 WorkbookSettings settings = new WorkbookSettings();
	 settings.setEncoding("ISO-8859-1");
    Workbook workbook = Workbook.getWorkbook(excelInputStream,settings);
   
    Sheet sheet = workbook.getSheet(0);

    int rowCount = sheet.getRows();
    for(int rowIndex=this.sourceModel.getFirstDataRow(); rowIndex<rowCount; rowIndex++)
    {
      Cell[] currentRow = sheet.getRow(rowIndex); // not zero based. Starting by "1"
      if(currentRow.length==0)
      {
         log_info("Found empty row, stopping import");
         log_info("=============================================================");
    	   break;
      }
      log_info("Import row ["+(rowIndex+1)+"]"); // +1 => Excel start row count at "1"
      log_info("-------------------------------------------------------------");
      
      // an der Quelle alle Columns setzen
      //
      List<ColumnModel> columns = this.sourceModel.getColumns();
      for (ColumnModel column : columns)
      {
        int columnIndex = Integer.parseInt(column.getName());
        if(currentRow.length>columnIndex)
        {
        	  Cell cell = sheet.getCell(columnIndex, rowIndex);
        	  String value = cell.getContents();
        	  if(value!=null && value.length()==0)
        	     value = null;
           column.setValue(context,value,true);
        }
      }
      // Daten berechnen
      //
      for (RecordSourceModel source : this.recordSourceModels)
      {
        for (ColumnModel column : source.getColumns())
        {
          column.getValue(context, trans);
        }
      }
      
      // Daten in die Tablerecords zurückschreiben
      //
      boolean changes = false;
      for (RecordSourceModel source : this.recordSourceModels)
      {
    	  changes = changes || source.store(context, trans);
      }
      if(!changes)
         log_info("No data for update found.");
      
      dumpValue();
      
      resetValue(context);
      log_info("");
    }
  }

  public String getName()
  {
    return model.getName().getDomNode().getFirstChild().getNodeValue();    
  }
  
//  public void setName(String name)
//  {
//    model.getName().getDomNode().getFirstChild().setNodeValue(name);    
//  }

  public RecordSourceModel getRecordSourceModel(String id)
  {
    for (RecordSourceModel source : this.recordSourceModels)
    {
      if(source.getId().equals(id))
        return source;
    }
    return null;
  }
  
  public void dumpValue()
  {
    System.out.println("========================================================================");
    for (RecordSourceModel source : this.recordSourceModels)
    {
      source.dumpValue();
    }
  }

  public String getLog() 
  {
     if(logger!=null)
        return logger.getLog();
     return null;
  }
	
   public boolean hasErrors() 
   {
      if(logger!=null)
         return logger.hasErrors();
   	return false;
   }
   
   public boolean hasWarnings() 
   {
      if(logger!=null)
         return logger.hasWarnings();
      
   	return false;
   }
   
   public void log_debug(String message) 
   {
      if(logger!=null)
         logger.log_debug(message);
      else
         System.out.println(message);
   }
   
   public void log_error(String message) 
   {
      if(logger!=null)
         logger.log_error(message);
      else
         System.err.println(message);
   }
   
   public void log_info(String message) 
   {
      if(logger!=null)
         logger.log_info(message);
      else
         System.out.println(message);
   }
   
   public void log_warn(String message) 
   {
      if(logger!=null)
         logger.log_warn(message);
      else
         System.out.println(message);
   }
   
   public void resetLog() 
   {
      if(logger!=null)
         logger.resetLog();
   }
}
