package de.tif.jacob.rule.util.letter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.TableAliasModel;

public class LetterFactoryTokenizer 
{
//	final JacobModel model;
  List suggestions  = new ArrayList();
  
	public LetterFactoryTokenizer(JacobModel model) 
	{
		super();
//		this.model = model;
    
    Iterator  aliasIter = model.getTableAliasModels().iterator();
    while(aliasIter.hasNext())
    {
      TableAliasModel alias = (TableAliasModel)aliasIter.next();
      Iterator fieldIter = alias.getFieldModels().iterator();
      while(fieldIter.hasNext())
      {
        FieldModel field = (FieldModel)fieldIter.next();
        suggestions.add("db_field("+alias.getName()+"."+field.getName()+")");
      }
    }
    
	}

	protected List suggest(String word)
	{
    List result = new ArrayList();
    Iterator iter = suggestions.iterator();
    while(iter.hasNext())
    {
      String sug = (String)iter.next();
      if(sug.startsWith(word))
        result.add(sug);
    }
		return result;
	}
}
