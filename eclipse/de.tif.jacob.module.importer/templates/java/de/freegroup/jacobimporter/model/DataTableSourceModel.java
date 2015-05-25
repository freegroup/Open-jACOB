package de.freegroup.jacobimporter.model;

import noNamespace.ElementDocument.Element;

public class DataTableSourceModel extends RecordSourceModel
{
  public DataTableSourceModel(ConverterModel rootModel, Element model)
  {
    super(rootModel, model);
  }

  public boolean isSourceModel() 
  {
	return false;
  }
  
}
