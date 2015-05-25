/*
 * Created on 24.04.2009
 *
 */
package de.freegroup.jacobimporter.model;

import noNamespace.ElementDocument.Element;
import de.freegroup.jacobimporter.model.ConnectionModel.TransferMode;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTransaction;

public class ColumnModel implements AbstractModel
{
  final ConverterModel rootModel;
  final RecordSourceModel parent;
  
  String currentValue=null;
  boolean valueCalculated= false;
  boolean indicator = false;
  final String name;
  
  public ColumnModel(ConverterModel rootModel,RecordSourceModel parent, Element model)
  {
    this.rootModel = rootModel;
    this.parent = parent;
    this.name = model.getName().getDomNode().getFirstChild().getNodeValue();
    this.indicator = "INDICATOR".equalsIgnoreCase(model.getTypeModel().getName().getDomNode().getFirstChild().getNodeValue());
  }

  public ColumnModel(ConverterModel rootModel,RecordSourceModel parent, String name)
  {
    this.rootModel = rootModel;
    this.parent = parent;
    this.name = name;
  }

  public String getName()
  {
    return this.name;    
  }

  
  public void setValue(Context context, String value)
  {
    this.setValue(context, value,false);
  }
  
  public void setValue(Context context, String value, boolean calculated)
  {
    this.currentValue = value;
    this.valueCalculated = calculated;
  }
  
  public String getValue(Context context, IDataTransaction trans) throws Exception
  {
    if(this.valueCalculated || this.parent.isSourceModel())
      return this.currentValue;
    
    // zuerst versuchen den Mutterrecord zurückzufüllen
    //
    this.parent.tryBackfill(context,trans);
    
    if(rootModel.visit(this))
    {
      ConnectionModel connection = this.rootModel.getConnectionByTarget(this);
      if(connection!=null)
      {
         String value = connection.getSourceColumn().getValue(context, trans);
         switch(connection.getTransferMode())
         {
            case always:
               this.currentValue= value;
               break;
            case ifSourceNotNull:
               if(value!=null)
                  this.currentValue = value;
               break;
            case ifTargetNull:
               if(this.currentValue==null)
                  this.currentValue=value;
               break;
         }
      }
    }
    this.valueCalculated = true;
    return this.currentValue;
  }
  
  public void resetValue(Context context)
  {
    this.currentValue=null;
    this.valueCalculated = false;
  }

  public void dumpValue()
  {
    System.out.println("\t"+getName()+"\t"+this.currentValue);
  }
  
  public String toString()
  {
    return this.parent.getName()+"."+this.getName();
  }

  public boolean isIndicator() 
  {
	return indicator;
  }
}
