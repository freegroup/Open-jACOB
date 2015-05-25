package de.freegroup.jacobimporter.model;

import java.util.ArrayList;
import java.util.List;

import noNamespace.ElementDocument.Element;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

public abstract class RecordSourceModel implements AbstractModel
{
  final ConverterModel rootModel;
  final Element model;
  
  boolean backfilled;
  
  List<ColumnModel> columns = new ArrayList<ColumnModel>();
  List<ColumnModel> uniqueKeyColumns = new ArrayList<ColumnModel>();
  private IDataTableRecord backfilledRecord;
  
  public RecordSourceModel(ConverterModel rootModel, Element model)
  {
    this.rootModel = rootModel;
    this.model = model;
    Element[] elements = this.model.getColumns().getData().getElementArray();
    for (Element element : elements)
    {
      ColumnModel column = new ColumnModel(rootModel,this, element);
      columns.add(column);
    }
    
    if(this.model.getUniqueKeyColumns()!=null)
    {
      elements = this.model.getUniqueKeyColumns().getData().getElementArray();
      for (Element element : elements)
      {
        ColumnModel column = new ColumnModel(rootModel,this, element.getName().getDomNode().getFirstChild().getNodeValue());
        uniqueKeyColumns.add(column);
      }
    }
  }

  public void tryBackfill(Context context, IDataTransaction trans) throws Exception
  {
    if(backfilled)
      return;
    backfilled=true;
    
    // Versuche alle Columns zurückzufüllen welche zum pkey gehören
    for (ColumnModel column : this.uniqueKeyColumns)
    {
        column.getValue(context,trans);
    }

    // try to load the record from the database
    //
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable table = acc.getTable(this.getName());
    for (ColumnModel column : this.uniqueKeyColumns)
    {
        String columnName = column.getName().split("[.]")[1];
        table.qbeSetKeyValue(columnName, column.getValue(context,trans));
    }
    table.search();
    if(table.recordCount()==1 || getCreateIfNotExists()==true)
    {
    	backfilledRecord = table.getSelectedRecord();
    	if(backfilledRecord==null)
    	  backfilledRecord =table.newRecord(trans);
    	     
    	{ // logging
         String info = "Loading Record: "+table.getName()+" [";
         boolean first=true;
         for (ColumnModel column : this.uniqueKeyColumns)
         {
             String columnName = column.getName().split("[.]")[1];
             if(first)
                info = info+(columnName+"="+column.getValue(context,trans));
             else
                info = info+(";"+columnName+"="+column.getValue(context,trans));
             first=false;
         }
         info = info+"]";
         rootModel.log_info(info);
    	}
    	
     	// Werte aus dem Record in the Felder Ã¼bertragen
    	//
      for (ColumnModel column : this.columns)
      {
       	if(column.isIndicator())
       		continue;
         String columnName = column.getName();
         column.setValue(context, backfilledRecord.getStringValue(columnName));
      }
    }
    else
    {// logging
    	  String warn = "Record not found: "+table.getName()+"=> [";
    	  boolean first=true;
        for (ColumnModel column : this.uniqueKeyColumns)
        {
            String columnName = column.getName().split("[.]")[1];
            if(first)
               warn = warn+(columnName+"="+column.getValue(context,trans));
            else
               warn = warn+(";"+columnName+"="+column.getValue(context,trans));
            first=false;
        }
        warn = warn+"]";
    	  rootModel.log_warn(warn);
    }
  }
  
  public boolean store(Context context, IDataTransaction trans) throws Exception
  {
     if(backfilledRecord==null)
       return false;
     boolean changes = false;
     for (ColumnModel column : this.columns)
     {
        	if(column.isIndicator())
        		continue;
         String columnName = column.getName();
         backfilledRecord.setValue(trans, columnName, column.getValue(context,trans));
         changes = changes || backfilledRecord.hasChangedValue(columnName);
     }

     if(changes)
     {
        rootModel.log_info("Update Record: "+backfilledRecord.getTableAlias().getName()+" [ "+backfilledRecord.getPrimaryKeyValue().toString()+" ]");
        for (ColumnModel column : this.columns)
        {
            if(column.isIndicator())
               continue;
            String columnName = column.getName();
            if(backfilledRecord.hasChangedValue(columnName))
               rootModel.log_info("   "+columnName+": "+backfilledRecord.getOldSaveStringValue(columnName)+" =>"+backfilledRecord.getSaveStringValue(columnName) );
        }
     }
     return changes;
  }
  
  public void resetValue(Context context) 
  {
      for (ColumnModel column : columns) 
      {
         column.resetValue(context);
      }
      
      for (ColumnModel column : uniqueKeyColumns) 
      {
         column.resetValue(context);
      }
      
      backfilled = false;
      backfilledRecord = null;
   }

  public String getName()
  {
    return model.getName().getDomNode().getFirstChild().getNodeValue();    
  }

  public boolean getCreateIfNotExists()
  {
    return new Boolean(model.getCreateIfNotExists().getDomNode().getFirstChild().getNodeValue()).booleanValue();    
  }  
  
  public String getId()
  {
    return model.getId().getDomNode().getFirstChild().getNodeValue();    
  }
  
  public String getType()
  {
    return model.getProviderType().getDomNode().getFirstChild().getNodeValue();    
  }
  
  public String getX()
  {
    return model.getPos().getX().getDomNode().getFirstChild().getNodeValue();    
  }
  
  public String getY()
  {
    return model.getPos().getY().getDomNode().getFirstChild().getNodeValue();    
  }
    
  public List<ColumnModel> getColumns()
  {
    return this.columns;
  }

  public ColumnModel getColumn(String nodeValue)
  {
    for (ColumnModel column : columns)
    {
      if(column.getName().equals(nodeValue))
        return column;
    }
    for (ColumnModel column : uniqueKeyColumns)
    {
      if(column.getName().equals(nodeValue))
        return column;
    }
    return null;
  }

  public void dumpValue()
  {
    System.out.println(getName());
    for (ColumnModel column : columns)
    {
      column.dumpValue();
    }
  }

  public boolean isSourceModel()
  {
    return "source".equals(getType());
  }
  
  public String toString()
  {
    return "Table:"+this.getName();
  }
}
