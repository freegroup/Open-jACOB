package de.tif.jacob.ruleengine.decision;

import de.tif.jacob.ruleengine.Decision;
import de.tif.jacob.util.ObjectUtil;

public class StringDecision extends Decision 
{
	public boolean equals(String value1, String value2) throws Exception
	{
		return ObjectUtil.equalsIgnoreNull(value1,value2);
	}
  
  public boolean hasZeroLenght(String value1) throws Exception
  {
    return value1==null || value1.length()==0;
  }

}
