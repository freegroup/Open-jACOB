/*
 * Created on 24.04.2009
 *
 */
package de.freegroup.dbwire.model;

import de.tif.jacob.core.Context;
import noNamespace.ElementDocument.Element;

public class ConnectionModel implements AbstractModel
{
  final ConverterModel rootModel;
  final Element model;
  
  enum TransferMode
  {
     always,
     ifTargetNull,
     ifSourceNotNull
  }
  
  public ConnectionModel(ConverterModel rootModel, Element model)
  {
    this.rootModel = rootModel;
    this.model = model;
    
  }

  public void resetValue(Context context)
  {
  }
  
  public ColumnModel getTargetColumn()
  {
    RecordSourceModel table = this.rootModel.getRecordSourceModel(this.model.getTargetTableId().getDomNode().getFirstChild().getNodeValue());
    return table.getColumn(this.model.getTargetField().getDomNode().getFirstChild().getNodeValue());
  }
  
  public ColumnModel getSourceColumn()
  {
    RecordSourceModel table = this.rootModel.getRecordSourceModel(this.model.getSourceTableId().getDomNode().getFirstChild().getNodeValue());
    return table.getColumn(this.model.getSourceField().getDomNode().getFirstChild().getNodeValue());
  }

  public TransferMode getTransferMode()
  {
     try
     {
        return TransferMode.valueOf(this.model.getTransferMode().getDomNode().getFirstChild().getNodeValue());
     }
     catch(Exception e)
     {
        return TransferMode.always;
     }
  }
  
  public void dumpValue()
  {
    System.out.println("connection");
  }
  
}
