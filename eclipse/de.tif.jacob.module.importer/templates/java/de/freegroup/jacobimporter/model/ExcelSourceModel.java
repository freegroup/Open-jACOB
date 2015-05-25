package de.freegroup.jacobimporter.model;

import noNamespace.ElementDocument.Element;

public class ExcelSourceModel extends RecordSourceModel
{
  public ExcelSourceModel(ConverterModel rootModel, Element model)
  {
    super(rootModel, model);
  }

  public int getFirstDataRow()
  {
    return Integer.parseInt(model.getFirstDataRow().getDomNode().getFirstChild().getNodeValue());    
  }

  public int getColumnCount()
  {
    return Integer.parseInt(model.getColumnCount().getDomNode().getFirstChild().getNodeValue());    
  }

  public boolean isSourceModel() 
  {
	return true;
  }
}
